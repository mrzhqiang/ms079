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
package tools;

/**
 * Provides MapleStory's custom encryption routines.
 *
 * @author Frz
 * @since Revision 211
 * @version 1.0
 */
public class MapleCustomEncryption {

    /**
     * Encrypts <code>data</code> with Maple's encryption routines.
     *
     * @param data The data to encrypt.
     * @return The encrypted data.
     */
    public static final byte[] encryptData(final byte data[]) {

        for (int j = 0; j < 6; j++) {
            byte remember = 0;
            byte dataLength = (byte) (data.length & 0xFF);
            // printByteArray(data);
            if (j % 2 == 0) {
                for (int i = 0; i < data.length; i++) {
                    byte cur = data[i];
                    cur = BitTools.rollLeft(cur, 3);
                    cur += dataLength;
                    cur ^= remember;
                    remember = cur;
                    cur = BitTools.rollRight(cur, (int) dataLength & 0xFF);
                    cur = ((byte) ((~cur) & 0xFF));
                    cur += 0x48;
                    dataLength--;
                    data[i] = cur;
                }
            } else {
                for (int i = data.length - 1; i >= 0; i--) {
                    byte cur = data[i];
                    cur = BitTools.rollLeft(cur, 4);
                    cur += dataLength;
                    cur ^= remember;
                    remember = cur;
                    cur ^= 0x13;
                    cur = BitTools.rollRight(cur, 3);
                    dataLength--;
                    data[i] = cur;
                }
            }
            //System.out.println("enc after iteration " + j + ": " + HexTool.toString(data) + " al: " + al);
        }
        return data;
    }

    /**
     * Decrypts <code>data</code> with Maple's encryption routines.
     *
     * @param data The data to decrypt.
     * @return The decrypted data.
     */
    public static final byte[] decryptData(final byte data[]) {
        for (int j = 1; j <= 6; j++) {
            byte remember = 0;
            byte dataLength = (byte) (data.length & 0xFF);
            byte nextRemember = 0;

            if (j % 2 == 0) {
                for (int i = 0; i < data.length; i++) {
                    byte cur = data[i];
                    cur = (byte) (cur - 72);
                    cur = (byte) ((cur ^ 0xFFFFFFFF) & 0xFF);
                    cur = BitTools.rollLeft(cur, dataLength & 0xFF);
                    nextRemember = cur;
                    cur = (byte) (cur ^ remember);
                    remember = nextRemember;
                    cur = (byte) (cur - dataLength);
                    cur = BitTools.rollRight(cur, 3);
                    data[i] = cur;
                    dataLength = (byte) (dataLength - 1);
                }
            } else {
                for (int i = data.length - 1; i >= 0; i--) {
                    byte cur = data[i];
                    cur = BitTools.rollLeft(cur, 3);
                    cur = (byte) (cur ^ 0x13);
                    nextRemember = cur;
                    cur = (byte) (cur ^ remember);
                    remember = nextRemember;
                    cur = (byte) (cur - dataLength);
                    cur = BitTools.rollRight(cur, 4);
                    data[i] = cur;
                    dataLength = (byte) (dataLength - 1);
                }
            }
            //System.out.println("dec after iteration " + j + ": " + HexTool.toString(data));
        }
        return data;
    }
}
