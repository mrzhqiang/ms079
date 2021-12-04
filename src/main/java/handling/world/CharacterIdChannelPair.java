package handling.world;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public final class CharacterIdChannelPair implements Externalizable, Comparable<CharacterIdChannelPair> {

    private int charId = 0;
    private int channel = 1;

    public CharacterIdChannelPair() {
    }

    public CharacterIdChannelPair(int charId, int channel) {
        this.charId = charId;
        this.channel = channel;
    }

    public int getCharacterId() {
        return charId;
    }

    public int getChannel() {
        return channel;
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException {
        charId = in.readInt();
        channel = in.readByte();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(charId);
        out.writeByte(channel);
    }

    @Override
    public int compareTo(CharacterIdChannelPair o) {
        return channel - o.channel;
    }
}
