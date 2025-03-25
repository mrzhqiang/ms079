package handling.mina;

import client.MapleClient;
import com.github.mrzhqiang.maplestory.config.ServerProperties;
import handling.MaplePacket;
import handling.SendPacketOpcode;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.FileoutputUtil;
import tools.HexTool;
import tools.MapleAESOFB;
import tools.MapleCustomEncryption;
import tools.data.input.ByteArrayByteStream;
import tools.data.input.GenericLittleEndianAccessor;

import javax.inject.Inject;
import java.util.concurrent.locks.Lock;

public final class MaplePacketEncoder implements ProtocolEncoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(MaplePacketEncoder.class);



    private static final Logger SERVER_PACKET_LOGGER = LoggerFactory.getLogger("SERVER_PACKET");

    private final ServerProperties properties;

    @Inject
    public MaplePacketEncoder(ServerProperties properties) {
        this.properties = properties;
    }

    @Override
    public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
        MapleClient client = (MapleClient) session.getAttribute(MapleClient.CLIENT_KEY);
        if (client != null) {
            MapleAESOFB sendCrypto = client.getSendCrypto();
            byte[] inputInitialPacket = ((MaplePacket) message).getBytes();
            // todo Netty LoggingHandler
            if (properties.isPacketLogger()) {
                int packetLen = inputInitialPacket.length;
                int pHeader = readFirstShort(inputInitialPacket);
                String pHeaderStr = Integer.toHexString(pHeader).toUpperCase();
                String op = lookupRecv(pHeader);
                boolean show = true;
              /*  switch (op) {
                    case "WARP_TO_MAP":
                    case "PING":
                    case "NPC_ACTION":
                    case "UPDATE_STATS":
                    case "MOVE_PLAYER":
                    case "SPAWN_NPC":
                    case "SPAWN_NPC_REQUEST_CONTROLLER":
                    case "REMOVE_NPC":
                    case "MOVE_LIFE":
                    case "MOVE_MONSTER":
                    case "MOVE_MONSTER_RESPONSE":
                    case "SPAWN_MONSTER":
                    case "SPAWN_MONSTER_CONTROL":
                    case "ANDROID_MOVE":
                        show = false;
                }*/
                String Recv = "服务端发送 " + op + " [" + pHeaderStr + "] (" + packetLen + ")\r\n";
                if (packetLen <= 50000) {
                    String recvTo = Recv + HexTool.toString(inputInitialPacket) + "\r\n" + HexTool.toStringFromAscii(inputInitialPacket);
                    if (show) {
                        SERVER_PACKET_LOGGER.info(recvTo);
                        LOGGER.debug(recvTo);
                    } //log.info("服务端发送" + "\r\n" + HexTool.toString(inputInitialPacket));
                } else {
                    LOGGER.info(HexTool.toString(new byte[]{inputInitialPacket[0], inputInitialPacket[1]}) + " ...");
                }

            }
            byte[] unencrypted = new byte[inputInitialPacket.length];
            System.arraycopy(inputInitialPacket, 0, unencrypted, 0, inputInitialPacket.length); // Copy the input > "unencrypted"
            byte[] ret = new byte[unencrypted.length + 4]; // Create new bytes with length = "unencrypted" + 4

            Lock mutex = client.getLock();
            mutex.lock();
            try {
                byte[] header = sendCrypto.getPacketHeader(unencrypted.length);
                MapleCustomEncryption.encryptData(unencrypted); // Encrypting Data
                sendCrypto.crypt(unencrypted); // Crypt it with IV
                System.arraycopy(header, 0, ret, 0, 4); // Copy the header > "Ret", first 4 bytes
            } finally {
                mutex.unlock();
            }
            System.arraycopy(unencrypted, 0, ret, 4, unencrypted.length); // Copy the unencrypted > "ret"
            out.write(IoBuffer.wrap(ret));
        } else { // no client object created yet, send unencrypted (hello)
            out.write(IoBuffer.wrap(((MaplePacket) message).getBytes()));
            // out.write(ByteBuffer.wrap(((byte[]) message)));
        }
    }

    @Override
    public void dispose(IoSession session) throws Exception {
        // nothing to do
    }

    private String lookupRecv(int val) {
        for (SendPacketOpcode op : SendPacketOpcode.values()) {
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
