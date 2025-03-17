package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.DbForeignKey;
import io.ebean.annotation.NotNull;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "guilds")
public class DGuild extends Model {

    @Id
    Integer id;

    @NotNull
    @OneToOne
    @JoinColumn(name = "leader")  // 指定外键列名为 leader
    @DbForeignKey(noConstraint = true)
    final DCharacter leader;
    @NotNull
    final String name;

    @NotNull
    Integer gp = 0;
    Integer logo;
    @NotNull
    Integer logoColor = 0;
    @NotNull
    Integer logoBg = 0;
    @NotNull
    Integer logoBgColor = 0;

    @NotNull
    String rank1title = "会长";
    @NotNull
    String rank2title = "副会长";
    @NotNull
    String rank3title = "公会精英";
    @NotNull
    String rank4title = "公会成员";
    @NotNull
    String rank5title = "公会萌新";
    @NotNull
    Integer capacity = 10;

    String notice;
    @NotNull
    Integer signature;
    @OneToOne
    @JoinColumn(name = "alliance")  // 指定外键列名为 leader
    DAlliance alliance;

    @OneToMany(mappedBy = "guild")
    @OrderBy("guildRank asc, name asc")
    List<DCharacter> members;

    @OneToMany(mappedBy = "guild")
    @OrderBy("localthreadid desc")
    List<DBbsThread> threads;
    @OneToMany(mappedBy = "guild")
    List<DBbsReply> replies;

    public DGuild(DCharacter leader, String name) {
        this.leader = leader;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DCharacter getLeader() {
        return leader;
    }

    public String getName() {
        return name;
    }

    public Integer getGp() {
        return gp;
    }

    public void setGp(Integer gp) {
        this.gp = gp;
    }

    public Integer getLogo() {
        return logo;
    }

    public void setLogo(Integer logo) {
        this.logo = logo;
    }

    public Integer getLogoColor() {
        return logoColor;
    }

    public void setLogoColor(Integer logoColor) {
        this.logoColor = logoColor;
    }

    public String getRank1title() {
        return rank1title;
    }

    public void setRank1title(String rank1title) {
        this.rank1title = rank1title;
    }

    public String getRank2title() {
        return rank2title;
    }

    public void setRank2title(String rank2title) {
        this.rank2title = rank2title;
    }

    public String getRank3title() {
        return rank3title;
    }

    public void setRank3title(String rank3title) {
        this.rank3title = rank3title;
    }

    public String getRank4title() {
        return rank4title;
    }

    public void setRank4title(String rank4title) {
        this.rank4title = rank4title;
    }

    public String getRank5title() {
        return rank5title;
    }

    public void setRank5title(String rank5title) {
        this.rank5title = rank5title;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getLogoBg() {
        return logoBg;
    }

    public void setLogoBg(Integer logoBg) {
        this.logoBg = logoBg;
    }

    public Integer getLogoBgColor() {
        return logoBgColor;
    }

    public void setLogoBgColor(Integer logoBgColor) {
        this.logoBgColor = logoBgColor;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public Integer getSignature() {
        return signature;
    }

    public void setSignature(Integer signature) {
        this.signature = signature;
    }

    public DAlliance getAlliance() {
        return alliance;
    }

    public void setAlliance(DAlliance alliance) {
        this.alliance = alliance;
    }

    public List<DBbsThread> getThreads() {
        return threads;
    }

    public void setThreads(List<DBbsThread> threads) {
        this.threads = threads;
    }

    public List<DBbsReply> getReplies() {
        return replies;
    }

    public void setReplies(List<DBbsReply> replies) {
        this.replies = replies;
    }

    public List<DCharacter> getMembers() {
        return members;
    }

    public void setMembers(List<DCharacter> members) {
        this.members = members;
    }
}
