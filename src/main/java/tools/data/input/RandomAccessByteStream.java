package tools.data.input;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(RandomAccessByteStream.class);

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
     * tools.data.input.SeekableInputStreamBytestream#seek(long)
     */
    @Override
    public final void seek(long offset) throws IOException {
        raf.seek(offset);
    }

    /**
     * @see
     * tools.data.input.SeekableInputStreamBytestream#getPosition()
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
            LOGGER.error("ERROR" + e);
            return 0;
        }
    }

    @Override
    public final String toString(final boolean b) { //?
        return toString();
    }
}
