package handling.world.guild;

import com.github.mrzhqiang.maplestory.domain.DCharacter;

public class MapleGuildCharacter {

    public final DCharacter character;
    private int channel;
    private boolean online;

    public MapleGuildCharacter(DCharacter character, int channelId) {
        this(character, channelId, true);
    }

    public MapleGuildCharacter(DCharacter character, int channel, boolean online) {
        this.character = character;
        this.online = online;
        this.channel = channel;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
}