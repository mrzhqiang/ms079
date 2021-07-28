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
package tools.data.input;

/**
 * This provides an interface to a seekable accessor to a stream of little
 * endian bytes.
 *
 *
 * @author Frz
 * @since Revision 299
 * @version 1.0
 */
public interface SeekableLittleEndianAccessor extends LittleEndianAccessor {

    /**
     * Seeks the stream by <code>offset</code>
     *
     * @param offset Number of bytes to seek ahead.
     */
    void seek(final long offset);

    /**
     * Gets the current position of the stream pointer.
     *
     * @return The current position in the stream as a long integer.
     */
    long getPosition();
}
