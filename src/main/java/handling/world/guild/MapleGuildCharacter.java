package handling.world.guild;

import client.MapleCharacter;

public class MapleGuildCharacter implements java.io.Serializable { // alias for a character

    private static final long serialVersionUID = 2058609046116597760L;

    private byte channel = -1, guildrank, allianceRank;
    private short level;
    private int id, jobid, guildid;
    private boolean online;
    private String name;

    // either read from active character...
    // if it's online
    public MapleGuildCharacter(MapleCharacter c) {
        name = c.getName();
        level = c.getLevel();
        id = c.getId();
        channel = (byte) c.getClient().getChannel();
        jobid = c.getJob();
        guildrank = c.getGuildRank();
        guildid = c.getGuildId();
        allianceRank = c.getAllianceRank();
        online = true;
    }

    // or we could just read from the database
    public MapleGuildCharacter(int id, short lv, String name, byte channel, int job,
                               byte rank, int gid, byte allianceRank, boolean on) {
        this.level = lv;
        this.id = id;
        this.name = name;
        if (on) {
            this.channel = channel;
        }
        this.jobid = job;
        this.online = on;
        this.guildrank = rank;
        this.guildid = gid;
        this.allianceRank = allianceRank;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(short l) {
        level = l;
    }

    public int getId() {
        return id;
    }

    public void setChannel(byte ch) {
        channel = ch;
    }

    public int getChannel() {
        return channel;
    }

    public int getJobId() {
        return jobid;
    }

    public void setJobId(int job) {
        jobid = job;
    }

    public int getGuildId() {
        return guildid;
    }

    public void setGuildId(int gid) {
        guildid = gid;
    }

    public void setGuildRank(byte rank) {
        guildrank = rank;
    }

    public byte getGuildRank() {
        return guildrank;
    }

    public boolean isOnline() {
        return online;
    }

    public String getName() {
        return name;
    }

    public void setOnline(boolean f) {
        online = f;
    }

    public void setAllianceRank(byte rank) {
        allianceRank = rank;
    }

    public byte getAllianceRank() {
        return allianceRank;
    }
}
