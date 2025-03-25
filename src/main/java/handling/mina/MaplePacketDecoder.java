package handling.mina;

import client.MapleClient;
import com.github.mrzhqiang.maplestory.config.ServerProperties;
import handling.RecvPacketOpcode;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.FileoutputUtil;
import tools.HexTool;
import tools.MapleAESOFB;
import tools.MapleCustomEncryption;
import tools.data.input.ByteArrayByteStream;
import tools.data.input.GenericLittleEndianAccessor;

import javax.inject.Inject;

public final class MaplePacketDecoder extends CumulativeProtocolDecoder {

    public static class DecoderState {
        public int packetlength = -1;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(MaplePacketDecoder.class);

    private static final Logger CLIENT_PACKET_LOGGER = LoggerFactory.getLogger("CLIENT_PACKET");



    public static final String DECODER_STATE_KEY = MaplePacketDecoder.class.getName() + ".STATE";

    private final ServerProperties properties;

    @Inject
    public MaplePacketDecoder(ServerProperties properties) {
        this.properties = properties;
    }

    protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) {
        DecoderState decoderState = (DecoderState) session.getAttribute(DECODER_STATE_KEY);

        if (decoderState == null) {
            decoderState = new DecoderState();
            session.setAttribute(DECODER_STATE_KEY, decoderState);
        }

        MapleClient client = (MapleClient) session.getAttribute(MapleClient.CLIENT_KEY);

        if (decoderState.packetlength == -1) {
            if (in.remaining() >= 4) {
                int packetHeader = in.getInt();
                if (!client.getReceiveCrypto().checkPacket(packetHeader)) {
                    session.close();
                    return false;
                }
                decoderState.packetlength = MapleAESOFB.getPacketLength(packetHeader);
            } else if ((in.remaining() < 4) && (decoderState.packetlength == -1)) {
                LOGGER.warn("解码…没有足够的数据/就是所谓的包不完整");
                return false;
            }
        }
        if (in.remaining() >= decoderState.packetlength) {//079
            byte[] decryptedPacket = new byte[decoderState.packetlength];
            in.get(decryptedPacket, 0, decoderState.packetlength);
            decoderState.packetlength = -1;

            client.getReceiveCrypto().crypt(decryptedPacket);
            MapleCustomEncryption.decryptData(decryptedPacket);
            out.write(decryptedPacket);
            if (properties.isPacketLogger()) {
                int packetLen = decryptedPacket.length;
                // opCode
                int pHeader = readFirstShort(decryptedPacket);
                // opCode转16进制
                String pHeaderStr = Integer.toHexString(pHeader).toUpperCase();
                String op = lookupSend(pHeader);
                boolean show = true;
                switch (op) {
                    case "PONG":
                    case "NPC_ACTION":
                    case "MOVE_LIFE":
                    case "MOVE_PLAYER":
                    case "MOVE_ANDROID":
                    case "MOVE_SUMMON":
                    case "AUTO_AGGRO":
                    case "HEAL_OVER_TIME":
                    case "BUTTON_PRESSED":
                    case "STRANGE_DATA":
                    case "ERROR_LOG":
                        show = false;
                }
                String Send = "客户端发送 " + op + " [" + pHeaderStr + "] (" + packetLen + ")\r\n";
                if (packetLen <= 3000) {
                    String SendTo = Send + HexTool.toString(decryptedPacket) + "\r\n" + HexTool.toStringFromAscii(decryptedPacket);
                    //log.info(HexTool.toString(decryptedPacket) + "客户端发送");
                    if (show) {
                        CLIENT_PACKET_LOGGER.info(SendTo);
                        LOGGER.debug(SendTo);
                    }
                    String SendTos = "\r\n时间：" + FileoutputUtil.CurrentReadable_Time() + "  ";
                    if (op.equals("UNKNOWN")) {
                        CLIENT_PACKET_LOGGER.info(SendTos + SendTo);
                    }
                } else {
                    LOGGER.info(HexTool.toString(new byte[]{decryptedPacket[0], decryptedPacket[1]}) + "...");
                }
            }
            return true;
        }
        /*
         * if (in.remaining() >= decoderState.packetlength) { final byte
         * decryptedPacket[] = new byte[decoderState.packetlength];
         * in.get(decryptedPacket, 0, decoderState.packetlength);
         * decoderState.packetlength = -1;
         *
         * client.getReceiveCrypto().crypt(decryptedPacket); //
         * MapleCustomEncryption.decryptData(decryptedPacket);
         * out.write(decryptedPacket); return true; }
         */
        return false;
    }

    private String lookupSend(int val) {
        for (RecvPacketOpcode op : RecvPacketOpcode.values()) {
            if (op.getValue() == val) {
                return op.name();
            }
        }
        return "UNKNOWN";
    }

    private int readFirstShort(byte[] arr) {
        return new GenericLittleEndianAccessor(new ByteArrayByteStream(arr)).readShort();
    }
}
