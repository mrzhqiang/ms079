package tools.data.input;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Provides an abstract accessor to a generic Little Endian byte stream. This
 * accessor is seekable.
 *
 * @author Frz
 * @version 1.0
 * @since Revision 323
 * @see tools.data.input.GenericLittleEndianAccessor
 */
public class GenericSeekableLittleEndianAccessor extends GenericLittleEndianAccessor implements SeekableLittleEndianAccessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenericSeekableLittleEndianAccessor.class);

    private final SeekableInputStreamBytestream bs;

    /**
     * Class constructor Provide a seekable input stream to wrap this object
     * around.
     *
     * @param bs The byte stream to wrap this around.
     */
    public GenericSeekableLittleEndianAccessor(SeekableInputStreamBytestream bs) {
        super(bs);
        this.bs = bs;
    }

    /**
     * Seek the pointer to <code>offset</code>
     *
     * @param offset The offset to seek to.
     * @see tools.data.input.SeekableInputStreamBytestream#seek
     */
    @Override
    public void seek(long offset) {
        try {
            bs.seek(offset);
        } catch (IOException e) {
            LOGGER.error("Seek failed", e);
        }
    }

    /**
     * Get the current position of the pointer.
     *
     * @return The current position of the pointer as a long integer.
     * @see
     * tools.data.input.SeekableInputStreamBytestream#getPosition
     */
    @Override
    public long getPosition() {
        try {
            return bs.getPosition();
        } catch (IOException e) {
            LOGGER.error("getPosition failed", e);
            return -1;
        }
    }

    /**
     * Skip <code>num</code> number of bytes in the stream.
     *
     * @param num The number of bytes to skip.
     */
    @Override
    public void skip(int num) {
        seek(getPosition() + num);
    }
}
