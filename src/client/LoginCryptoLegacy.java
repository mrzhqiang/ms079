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
package client;

import java.util.Random;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Provides cryptographic functions for password hashing.
 *
 * Legacy purpose as the method done here is insecure by hashing multiple times
 * and overly complicated. Will go away when/if official oms has no more users
 * with legacy passhashes.
 *
 * @author Nol888
 * @version 0.1
 */
public class LoginCryptoLegacy {

    /**
     * Map of 6 bit nibbles to base64 characters.
     */
    private static final Random rand = new Random();
    private static final char[] iota64 = new char[64];

    static {
        int i = 0;
        iota64[i++] = '.';
        iota64[i++] = '/';
        for (char c = 'A'; c <= 'Z'; c++) {
            iota64[i++] = c;
        }
        for (char c = 'a'; c <= 'z'; c++) {
            iota64[i++] = c;
        }
        for (char c = '0'; c <= '9'; c++) {
            iota64[i++] = c;
        }
    }

    /**
     * Hash the password for first time storage.
     *
     * @param password The password to be hashed.
     * @return String of the hashed password.
     */
    public static final String hashPassword(String password) {
        byte[] randomBytes = new byte[6];

        rand.setSeed(System.currentTimeMillis());
        rand.nextBytes(randomBytes);

        return myCrypt(password, genSalt(randomBytes));
    }

    /**
     * Check a password against a hash.
     *
     * @param password The password to validate.
     * @param hash The hash to validate against.
     * @return <code>true</code> if the password is correct, <code>false</code>
     * otherwise.
     */
    public static final boolean checkPassword(String password, String hash) {
        return (myCrypt(password, hash).equals(hash));
    }

    public static final boolean isLegacyPassword(String hash) {
        return hash.substring(0, 3).equals("$H$");
    }

    /**
     * Encrypt a string with <code>Seed</code> as a seed code.
     *
     * @param password Password to encrypt.
     * @param seed Seed to use.
     * @return The salted SHA1 hash of password.
     * @throws RuntimeException
     */
    private static final String myCrypt(String password, String seed) throws RuntimeException {
        String out = null;
        int count = 8;
        MessageDigest digester;

        // Check for correct Seed
        if (!seed.substring(0, 3).equals("$H$")) {
            // Oh noes! Generate a seed and continue.
            byte[] randomBytes = new byte[6];
            rand.nextBytes(randomBytes);
            seed = genSalt(randomBytes);
        }

        String salt = seed.substring(4, 12);
        if (salt.length() != 8) {
            throw new RuntimeException("Error hashing password - Invalid seed.");
        }
        try {
            digester = MessageDigest.getInstance("SHA-1");

            digester.update((salt + password).getBytes("iso-8859-1"), 0, (salt + password).length());
            byte[] sha1Hash = digester.digest();
            do {
                byte[] CombinedBytes = new byte[sha1Hash.length + password.length()];
                System.arraycopy(sha1Hash, 0, CombinedBytes, 0, sha1Hash.length);
                System.arraycopy(password.getBytes("iso-8859-1"), 0, CombinedBytes, sha1Hash.length, password.getBytes("iso-8859-1").length);
                digester.update(CombinedBytes, 0, CombinedBytes.length);
                sha1Hash = digester.digest();
            } while (--count > 0);
            out = seed.substring(0, 12);
            out += encode64(sha1Hash);
        } catch (NoSuchAlgorithmException Ex) {
            System.err.println("Error hashing password." + Ex);
        } catch (UnsupportedEncodingException Ex) {
            System.err.println("Error hashing password." + Ex);
        }
        if (out == null) {
            throw new RuntimeException("Error hashing password - out = null");
        }

        return out;
    }

    /**
     * Generates a salt string from random bytes <code>Random</code>
     *
     * @param Random Random bytes to get salt from.
     * @return Salt string.
     */
    private static final String genSalt(final byte[] Random) {
        final StringBuilder Salt = new StringBuilder("$H$");
        Salt.append(iota64[30]);
        Salt.append(encode64(Random));

        return Salt.toString();
    }

    private static final String convertToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9)) {
                    buf.append((char) ('0' + halfbyte));
                } else {
                    buf.append((char) ('a' + (halfbyte - 10)));
                }
                halfbyte = data[i] & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    public static final String encodeSHA1(final String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        final MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        return convertToHex(md.digest());
    }

    /**
     * Encodes a byte array into base64.
     *
     * @param Input Array of bytes to put into base64.
     * @return String of base64.
     */
    private static final String encode64(byte[] Input) {
        int iLen = Input.length;

        int oDataLen = (iLen * 4 + 2) / 3; // output length without padding
        int oLen = ((iLen + 2) / 3) * 4; // output length including
        // padding
        char[] out = new char[oLen];
        int ip = 0;
        int op = 0;
        while (ip < iLen) {
            int i0 = Input[ip++] & 0xff;
            int i1 = ip < iLen ? Input[ip++] & 0xff : 0;
            int i2 = ip < iLen ? Input[ip++] & 0xff : 0;
            int o0 = i0 >>> 2;
            int o1 = ((i0 & 3) << 4) | (i1 >>> 4);
            int o2 = ((i1 & 0xf) << 2) | (i2 >>> 6);
            int o3 = i2 & 0x3F;
            out[op++] = iota64[o0];
            out[op++] = iota64[o1];
            out[op] = op < oDataLen ? iota64[o2] : '=';
            op++;
            out[op] = op < oDataLen ? iota64[o3] : '=';
            op++;
        }
        return new String(out);
    }
}
