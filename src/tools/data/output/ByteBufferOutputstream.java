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
 * Uses a bytebuffer as an underlying storage method to hold a stream of bytes.
 *
 * @author Frz
 * @version 1.0
 * @since Revision 323
 */
public class ByteBufferOutputstream implements ByteOutputStream {

    private IoBuffer bb;

    /**
     * Class constructor - Wraps this instance around ByteBuffer <code>bb</code>
     *
     * @param bb The <code>org.apache.mina.common.ByteBuffer</code> to wrap this
     * stream around.
     */
    public ByteBufferOutputstream(final IoBuffer bb) {
        super();
        this.bb = bb;
    }

    /**
     * Writes a byte to the underlying buffer.
     *
     * @param b The byte to write.
     * @see org.apache.mina.common.ByteBuffer#put(byte)
     */
    @Override
    public void writeByte(final byte b) {
        bb.put(b);
    }
}
