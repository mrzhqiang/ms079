package client;

import com.github.mrzhqiang.maplestory.domain.DCharacter;

public class BuddyEntry {

    public final DCharacter character;
    private String group;
    private boolean visible;
    private int channel;

    public BuddyEntry(DCharacter character, String group, int channel, boolean visible) {
        this.character = character;
        this.group = group;
        this.channel = channel;
        this.visible = visible;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public boolean isOnline() {
        return channel >= 0;
    }

    public void setOffline() {
        channel = -1;
    }

    public String getName() {
        return character.name;
    }

    public int getCharacterId() {
        return character.id;
    }

    public int getLevel() {
        return character.level;
    }

    public int getJob() {
        return character.job;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String newGroup) {
        this.group = newGroup;
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + character.id;
        return result;
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        final BuddyEntry other = (BuddyEntry) obj;
        return character.id == other.character.id;
    }
}
