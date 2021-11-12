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
    void seek(long offset);

    /**
     * Gets the current position of the stream pointer.
     *
     * @return The current position in the stream as a long integer.
     */
    long getPosition();
}
