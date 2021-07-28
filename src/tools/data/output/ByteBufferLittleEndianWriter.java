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
package tools.data.output;

import org.apache.mina.core.buffer.IoBuffer;



/**
 * Uses a <code>org.apache.mina.common.ByteBuffer</code> to implement a generic
 * little-endian sequence of bytes.
 *
 * @author Frz
 * @version 1.0
 * @since Revision 323
 */
public class ByteBufferLittleEndianWriter extends GenericLittleEndianWriter {

    private IoBuffer bb;

    /**
     * Constructor - Constructs this object as fixed at the default size.
     */
    public ByteBufferLittleEndianWriter() {
        this(50, true);
    }

    /**
     * Constructor - Constructs this object as fixed at size <code>size</code>.
     *
     * @param size The size of the fixed bytebuffer.
     */
    public ByteBufferLittleEndianWriter(final int size) {
        this(size, false);
    }

    /**
     * Constructor - Constructs this object as optionally fixed at size
     * <code>size</code>.
     *
     * @param initialSize The size of the fixed bytebuffer.
     * @param autoExpand Expand if needed.
     */
    public ByteBufferLittleEndianWriter(final int initialSize, final boolean autoExpand) {
        bb = IoBuffer.allocate(initialSize);
        bb.setAutoExpand(autoExpand);
        setByteOutputStream(new ByteBufferOutputstream(bb));
    }

    /**
     * Returns a flipped version of the underlying bytebuffer.
     *
     * @return A flipped version of the underlying bytebuffer.
     */
    public IoBuffer getFlippedBB() {
        return bb.flip();
    }

    /**
     * Returns the underlying bytebuffer.
     *
     * @return The underlying bytebuffer.
     */
    public IoBuffer getByteBuffer() {
        return bb;
    }
}
