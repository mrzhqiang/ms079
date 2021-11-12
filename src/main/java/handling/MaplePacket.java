package handling;

public interface MaplePacket extends java.io.Serializable {

    byte[] getBytes();

    Runnable getOnSend();

    void setOnSend(final Runnable onSend);
}
