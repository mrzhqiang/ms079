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

import java.io.IOException;

import tools.HexTool;

/**
 * Provides for an abstraction layer for an array of bytes.
 *
 * @author Frz
 * @version 1.0
 * @since Revision 326
 */
public class ByteArrayByteStream implements SeekableInputStreamBytestream {

    private int pos = 0;
    private long bytesRead = 0;
    private final byte[] arr;

    /**
     * Class constructor.
     *
     * @param arr Array of bytes to wrap the stream around.
     */
    public ByteArrayByteStream(final byte[] arr) {
        this.arr = arr;
    }

    /**
     * Gets the current position of the stream.
     *
     * @return The current position of the stream.
     * @see
     * net.sf.odinms.tools.data.input.SeekableInputStreamBytestream#getPosition()
     */
    @Override
    public long getPosition() {
        return pos;
    }

    /**
     * Seeks the pointer the the specified position.
     *
     * @param offset The position you wish to seek to.
     * @see
     * net.sf.odinms.tools.data.input.SeekableInputStreamBytestream#seek(long)
     */
    @Override
    public void seek(final long offset) throws IOException {
        pos = (int) offset;
    }

    /**
     * Returns the numbers of bytes read from the stream.
     *
     * @return The number of bytes read.
     * @see net.sf.odinms.tools.data.input.ByteInputStream#getBytesRead()
     */
    @Override
    public long getBytesRead() {
        return bytesRead;
    }

    /**
     * Reads a byte from the current position.
     *
     * @return The byte as an integer.
     * @see net.sf.odinms.tools.data.input.ByteInputStream#readByte()
     */
    @Override
    public int readByte() {
        bytesRead++;
        return ((int) arr[pos++]) & 0xFF;
    }

    /**
     * Returns the current stream as a hexadecimal string of values. Shows the
     * entire stream, and the remaining data at the current position.
     *
     * @return The current stream as a string.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return toString(false);
    }

    @Override
    public String toString(final boolean b) {
        String nows = "";
        if (arr.length - pos > 0) {
            byte[] now = new byte[arr.length - pos];
            System.arraycopy(arr, pos, now, 0, arr.length - pos);
            nows = HexTool.toString(now);
        }
        if (b) {
            return "All: " + HexTool.toString(arr) + "\nNow: " + nows;
        } else {
            return "Data: " + nows;
        }
    }

    /**
     * Returns the number of bytes available from the stream.
     *
     * @return Number of bytes available as a long integer.
     * @see net.sf.odinms.tools.data.input.ByteInputStream#available()
     */
    @Override
    public long available() {
        return arr.length - pos;
    }
}
