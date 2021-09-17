package tools.data.output;

import constants.ServerConstants;
import handling.ByteArrayMaplePacket;
import handling.MaplePacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.mrzhqiang.maplestory.config.ServerConfiguration;
import tools.HexTool;

import java.io.ByteArrayOutputStream;

/**
 * Writes a maplestory-packet little-endian stream of bytes.
 *
 * @author Frz
 * @version 1.0
 * @since Revision 352
 */
public class MaplePacketLittleEndianWriter extends GenericLittleEndianWriter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MaplePacketLittleEndianWriter.class);

    private final ByteArrayOutputStream baos;

    /**
     * Constructor - initializes this stream with a default size.
     */
    public MaplePacketLittleEndianWriter() {
        this(32);
    }

    /**
     * Constructor - initializes this stream with size <code>size</code>.
     *
     * @param size The size of the underlying stream.
     */
    public MaplePacketLittleEndianWriter(final int size) {
        this.baos = new ByteArrayOutputStream(size);
        setByteOutputStream(new BAOSByteOutputStream(baos));
    }

    /**
     * Gets a <code>MaplePacket</code> instance representing this sequence of
     * bytes.
     *
     * @return A <code>MaplePacket</code> with the bytes in this stream.
     */
    public final MaplePacket getPacket() {
        if (ServerConstants.properties.isDebug()) {
            MaplePacket packet = new ByteArrayMaplePacket(baos.toByteArray());
            LOGGER.debug("Packet to be sent:\n" + packet.toString());
        }
        return new ByteArrayMaplePacket(baos.toByteArray());
    }

    /**
     * Changes this packet into a human-readable hexadecimal stream of bytes.
     *
     * @return This packet as hex digits.
     */
    @Override
    public final String toString() {
        return HexTool.toString(baos.toByteArray());
    }
}
