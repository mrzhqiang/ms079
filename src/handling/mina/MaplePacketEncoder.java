/*
 This file is part of the OdinMS Maple Story Server
 Copyright (C) 2008 ~ 2010 Patrick Huy <patrick.huy@frz.cc> 
 Matthias Butz <matze@odinms.de>
 Jan Christian Meyer <vimes@odinms.de>

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License version 3
 as published by the Free Software Foundation. You may not use, modify
 or distribute this program under any other version of the
 GNU Affero General Public License.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package handling.mina;

import client.MapleClient;
import constants.ServerConstants;
import handling.MaplePacket;
import handling.SendPacketOpcode;
import java.nio.ByteBuffer;
import tools.MapleAESOFB;
import tools.MapleCustomEncryption;

import java.util.concurrent.locks.Lock;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;


import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.FileoutputUtil;
import tools.HexTool;
import tools.data.input.ByteArrayByteStream;
import tools.data.input.GenericLittleEndianAccessor;

public class MaplePacketEncoder implements ProtocolEncoder {

    private static Logger log = LoggerFactory.getLogger(MaplePacketEncoder.class);

    @Override
    public void encode(final IoSession session, final Object message, final ProtocolEncoderOutput out) throws Exception {
        final MapleClient client = (MapleClient) session.getAttribute(MapleClient.CLIENT_KEY);

        if (client != null) {
            final MapleAESOFB send_crypto = client.getSendCrypto();

            //final byte[] inputInitialPacket = ((byte[]) message);
            final byte[] inputInitialPacket = ((MaplePacket) message).getBytes();
            if (ServerConstants.封包显示) {
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
                    String RecvTo = Recv + HexTool.toString(inputInitialPacket) + "\r\n" + HexTool.toStringFromAscii(inputInitialPacket);
                    if (show) {
                        FileoutputUtil.packetLog("日志\\log\\服务端封包.log", RecvTo);
                        System.out.println(RecvTo);
                    } //log.info("服务端发送" + "\r\n" + HexTool.toString(inputInitialPacket));
                } else {
                    log.info(HexTool.toString(new byte[]{inputInitialPacket[0], inputInitialPacket[1]}) + " ...");
                }

            }
            final byte[] unencrypted = new byte[inputInitialPacket.length];
            System.arraycopy(inputInitialPacket, 0, unencrypted, 0, inputInitialPacket.length); // Copy the input > "unencrypted"
            final byte[] ret = new byte[unencrypted.length + 4]; // Create new bytes with length = "unencrypted" + 4

            final Lock mutex = client.getLock();
            mutex.lock();
            try {
                final byte[] header = send_crypto.getPacketHeader(unencrypted.length);
                MapleCustomEncryption.encryptData(unencrypted); // Encrypting Data
                send_crypto.crypt(unencrypted); // Crypt it with IV
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
