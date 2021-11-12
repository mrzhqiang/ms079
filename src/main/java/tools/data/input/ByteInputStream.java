package tools.data.input;

/**
 * Represents an abstract stream of bytes.
 *
 * @author Frz
 * @version 1.0
 * @since Revision 323
 */
public interface ByteInputStream {

    /**
     * Reads the next byte off the stream.
     *
     * @return The next byte as an integer.
     */
    int readByte();

    /**
     * Gets the number of bytes read from the stream.
     *
     * @return The number of bytes as a long integer.
     */
    long getBytesRead();

    /**
     * Gets the number of bytes still left for reading.
     *
     * @return The number of bytes as a long integer.
     */
    long available();

    String toString(boolean b);
}
