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
 * Provides a class with tools for working with IP addresses, in both strings
 * and as long integers.
 *
 * @author Nol888
 * @version 0.1
 * @since Revision 890
 */
public class IPAddressTool {

    /**
     * Converts a dotted-quad IP (<code>127.0.0.1</code>) and turns it into a
     * long integer IP.
     *
     * @param dottedQuad The IP address in dotted-quad form.
     * @return The IP as a long integer.
     * @throws RuntimeException
     */
    public static final long dottedQuadToLong(final String dottedQuad) throws RuntimeException {
        final String[] quads = dottedQuad.split("\\.");
        if (quads.length != 4) {
            throw new RuntimeException("Invalid IP Address format.");
        }
        long ipAddress = 0;
        for (int i = 0; i < 4; i++) {
            ipAddress += (long) (Integer.parseInt(quads[i]) % 256) * (long) Math.pow(256, (double) (4 - i));
        }
        return ipAddress;
    }

    /**
     * Converts a long integer IP into a dotted-quad IP.
     *
     * @param longIP The IP as a long integer.
     * @return The IP as a dotted-quad string.
     * @throws RuntimeException
     */
    public static final String longToDottedQuad(long longIP) throws RuntimeException {
        StringBuilder ipAddress = new StringBuilder();

        for (int i = 0; i < 4; i++) {
            int quad = (int) (longIP / (long) Math.pow(256, (double) (4 - i)));
            longIP -= (long) quad * (long) Math.pow(256, (double) (4 - i));
            if (i > 0) {
                ipAddress.append(".");
            }
            if (quad > 255) {
                throw new RuntimeException("Invalid long IP address.");
            }
            ipAddress.append(quad);
        }

        return ipAddress.toString();
    }
}
