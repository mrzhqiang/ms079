package client;

public class BuddyEntry {

    private final String name;
    private String group;
    private final int characterId;
    private final int level;
    private final int job;
    private boolean visible;
    private int channel;

    public BuddyEntry(String name, int characterId, String group, int channel, boolean visible, int level, int job) {
        this.name = name;
        this.characterId = characterId;
        this.group = group;
        this.channel = channel;
        this.visible = visible;
        this.level = level;
        this.job = job;
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
        return name;
    }

    public int getCharacterId() {
        return characterId;
    }

    public int getLevel() {
        return level;
    }

    public int getJob() {
        return job;
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
        result = prime * result + characterId;
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
        return characterId == other.characterId;
    }
}
