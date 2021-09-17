package tools.data.output;

import com.github.mrzhqiang.maplestory.wz.element.data.Vector;

/**
 * Provides an interface to a writer class that writes a little-endian sequence
 * of bytes.
 *
 * @author Frz
 * @version 1.0
 * @since Revision 323
 */
public interface LittleEndianWriter {

    /**
     * Write the number of zero bytes
     *
     * @param i The bytes to write.
     */
    void writeZeroBytes(final int i);

    /**
     * Write an array of bytes to the sequence.
     *
     * @param b The bytes to write.
     */
    void write(final byte[] b);

    /**
     * Write a byte to the sequence.
     *
     * @param b The byte to write.
     */
    void write(final byte b);

    void write(final int b);

    /**
     * Writes an integer to the sequence.
     *
     * @param i The integer to write.
     */
    void writeInt(final int i);

    /**
     * Write a short integer to the sequence.
     *
     * @param s The short integer to write.
     */
    void writeShort(final short s);

    void writeShort(final int i);

    /**
     * Write a long integer to the sequence.
     *
     * @param l The long integer to write.
     */
    void writeLong(final long l);

    /**
     * Writes an ASCII string the the sequence.
     *
     * @param s The ASCII string to write.
     */
    void writeAsciiString(final String s);

    void writeAsciiString(String s, final int max);

    /**
     * Writes a 2D 4 byte position information
     *
     * @param s The Vector position to write.
     */
    void writePos(final Vector s);

    /**
     * Writes a maple-convention ASCII string to the sequence.
     *
     * @param s The ASCII string to use maple-convention to write.
     */
    void writeMapleAsciiString(final String s);
}
