package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.DbForeignKey;
import io.ebean.annotation.Length;
import io.ebean.annotation.NotNull;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "guilds")
public class DGuild extends Model {

    @Id
    @Column(name = "guildid")
    public Integer id;
    @OneToOne
    @DbForeignKey(noConstraint = true)
    @JoinColumn(name = "leader")
    public DCharacter leader;
    @NotNull
    @Column(name = "GP")
    public Integer GP;
    @Column(name = "logo")
    public Integer logo;
    @NotNull
    @Column(name = "logoColor")
    public Integer logoColor;
    @NotNull
    @Length(45)
    @Column(name = "name")
    public String name;
    @NotNull
    @Length(45)
    @Column(name = "rank1title")
    public String rank1title;
    @NotNull
    @Length(45)
    @Column(name = "rank2title")
    public String rank2title;
    @NotNull
    @Length(45)
    @Column(name = "rank3title")
    public String rank3title;
    @NotNull
    @Length(45)
    @Column(name = "rank4title")
    public String rank4title;
    @NotNull
    @Length(45)
    @Column(name = "rank5title")
    public String rank5title;
    @NotNull
    @Column(name = "capacity")
    public Integer capacity;
    @Column(name = "logoBG")
    public Integer logoBG;
    @NotNull
    @Column(name = "logoBGColor")
    public Integer logoBGColor;
    @Length(101)
    @Column(name = "notice")
    public String notice;
    @NotNull
    @Column(name = "signature")
    public Integer signature;
    @NotNull
    @OneToOne
    @JoinColumn(name = "alliance")
    public DAlliance alliance;

    @OneToMany(mappedBy = "guild")
    @OrderBy("localthreadid desc")
    public List<DBbsThread> threads;
    @OneToMany(mappedBy = "guild", cascade = CascadeType.ALL)
    public List<DBbsReply> replies;
    @OneToMany(mappedBy = "guild")
    @OrderBy("guildRank asc, name asc")
    public List<DCharacter> characters;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DGuild dGuild = (DGuild) o;
        return Objects.equals(id, dGuild.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
