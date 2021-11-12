package tools.data.input;

import com.github.mrzhqiang.maplestory.wz.element.data.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides a generic interface to a Little Endian stream of bytes.
 *
 * @author Frz
 * @version 1.0
 * @since Revision 323
 */
public class GenericLittleEndianAccessor implements LittleEndianAccessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenericLittleEndianAccessor.class);

    private final ByteInputStream bs;

    /**
     * Class constructor - Wraps the accessor around a stream of bytes.
     *
     * @param bs The byte stream to wrap the accessor around.
     */
    public GenericLittleEndianAccessor(ByteInputStream bs) {
        this.bs = bs;
    }

    @Override
    public int readByteAsInt() {
        return bs.readByte();
    }

    /**
     * Read a single byte from the stream.
     *
     * @return The byte read.
     * @see tools.data.input.ByteInputStream#readByte
     */
    @Override
    public byte readByte() {
        return (byte) bs.readByte();
    }

    /**
     * Reads an integer from the stream.
     *
     * @return The integer read.
     */
    @Override
    public int readInt() {
        final int byte1 = bs.readByte();
        final int byte2 = bs.readByte();
        final int byte3 = bs.readByte();
        final int byte4 = bs.readByte();
        return (byte4 << 24) + (byte3 << 16) + (byte2 << 8) + byte1;
    }

    /**
     * Reads a short integer from the stream.
     *
     * @return The short read.
     */
    @Override
    public short readShort() {
        final int byte1 = bs.readByte();
        final int byte2 = bs.readByte();
        return (short) ((byte2 << 8) + byte1);
    }

    /**
     * Reads a single character from the stream.
     *
     * @return The character read.
     */
    @Override
    public char readChar() {
        return (char) readShort();
    }

    /**
     * Reads a long integer from the stream.
     *
     * @return The long integer read.
     */
    @Override
    public long readLong() {
        final int byte1 = bs.readByte();
        final int byte2 = bs.readByte();
        final int byte3 = bs.readByte();
        final int byte4 = bs.readByte();
        final int byte5 = bs.readByte();
        final int byte6 = bs.readByte();
        final int byte7 = bs.readByte();
        final int byte8 = bs.readByte();

        return (long) ((byte8 << 56) + (byte7 << 48) + (byte6 << 40) + (byte5 << 32) + (byte4 << 24) + (byte3 << 16)
                + (byte2 << 8) + byte1);
    }

    /**
     * Reads a floating point integer from the stream.
     *
     * @return The float-type integer read.
     */
    @Override
    public float readFloat() {
        return Float.intBitsToFloat(readInt());
    }

    /**
     * Reads a double-precision integer from the stream.
     *
     * @return The double-type integer read.
     */
    @Override
    public double readDouble() {
        return Double.longBitsToDouble(readLong());
    }

    /**
     * Reads an ASCII string from the stream with length <code>n</code>.
     *
     * @param n Number of characters to read.
     * @return The string read.
     */
    @Override
    public String readAsciiString(int n) {
//char ret[] = new char[n];
        byte[] ret = new byte[n];
        for (int x = 0; x < n; x++) {
            ret[x] = readByte();
        }
        try {
            return new String(ret, "gbk");
        } catch (Exception e) {
            LOGGER.error("", e);
        }
        return null;
    }

    /*public final String readAsciiString(final int n) {
     final byte ret[] = new byte[n];
     for (int x = 0; x < n; x++) {
     ret[x] = (byte) readByte();
     }
     try {
     String str = new String(ret, "BIG5");
     return str;
     } catch (Exception e) {
     LOGGER.error(e);
     }
     return "";
     }*/

    /**
     * Gets the number of bytes read from the stream so far.
     *
     * @return A long integer representing the number of bytes read.
     * @see tools.data.input.ByteInputStream#getBytesRead()
     */
    public long getBytesRead() {
        return bs.getBytesRead();
    }

    /**
     * Reads a MapleStory convention lengthed ASCII string. This consists of a
     * short integer telling the length of the string, then the string itself.
     *
     * @return The string read.
     */
    @Override
    public String readMapleAsciiString() {
        return readAsciiString(readShort());
    }

    /**
     * Reads a MapleStory Position information. This consists of 2 short
     * integer.
     *
     * @return The Position read.
     */
    @Override
    public Vector readPos() {
        final int x = readShort();
        final int y = readShort();
        return Vector.of(x, y);
    }

    /**
     * Reads <code>num</code> bytes off the stream.
     *
     * @param num The number of bytes to read.
     * @return An array of bytes with the length of <code>num</code>
     */
    @Override
    public byte[] read(int num) {
        byte[] ret = new byte[num];
        for (int x = 0; x < num; x++) {
            ret[x] = readByte();
        }
        return ret;
    }

    /**
     * Skips the current position of the stream <code>num</code> bytes ahead.
     *
     * @param num Number of bytes to skip.
     */
    @Override
    public void skip(int num) {
        for (int x = 0; x < num; x++) {
            readByte();
        }
    }

    /**
     * @see tools.data.input.ByteInputStream#available
     */
    @Override
    public long available() {
        return bs.available();
    }

    /**
     * @see java.lang.Object#toString
     */
    @Override
    public String toString() {
        return bs.toString();
    }

    @Override
    public String toString(boolean b) {
        return bs.toString(b);
    }
}
