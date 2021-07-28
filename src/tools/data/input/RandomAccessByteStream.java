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
import java.io.RandomAccessFile;

/**
 * Provides an abstract layer to a byte stream. This layer can be accessed
 * randomly.
 *
 * @author Frz
 * @version 1.0
 * @since Revision 323
 */
public class RandomAccessByteStream implements SeekableInputStreamBytestream {

    private final RandomAccessFile raf;
    private long read = 0;

    /**
     * Class constructor. Wraps this object around a RandomAccessFile.
     *
     * @param raf The RandomAccessFile instance to wrap this around.
     * @see java.io.RandomAccessFile
     */
    public RandomAccessByteStream(final RandomAccessFile raf) {
        super();
        this.raf = raf;
    }

    /**
     * Reads a byte off of the file.
     *
     * @return The byte read as an integer.
     */
    @Override
    public final int readByte() {
        int temp;
        try {
            temp = raf.read();
            if (temp == -1) {
                throw new RuntimeException("EOF");
            }
            read++;
            return temp;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @see
     * net.sf.odinms.tools.data.input.SeekableInputStreamBytestream#seek(long)
     */
    @Override
    public final void seek(long offset) throws IOException {
        raf.seek(offset);
    }

    /**
     * @see
     * net.sf.odinms.tools.data.input.SeekableInputStreamBytestream#getPosition()
     */
    @Override
    public final long getPosition() throws IOException {
        return raf.getFilePointer();
    }

    /**
     * Get the number of bytes read.
     *
     * @return The number of bytes read as a long integer.
     */
    @Override
    public final long getBytesRead() {
        return read;
    }

    /**
     * Get the number of bytes available for reading.
     *
     * @return The number of bytes available for reading as a long integer.
     */
    @Override
    public final long available() {
        try {
            return raf.length() - raf.getFilePointer();
        } catch (IOException e) {
            System.err.println("ERROR" + e);
            return 0;
        }
    }

    @Override
    public final String toString(final boolean b) { //?
        return toString();
    }
}
