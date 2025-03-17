package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.DbComment;
import io.ebean.annotation.DbForeignKey;
import io.ebean.annotation.NotNull;
import io.ebean.annotation.WhenCreated;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "characters", indexes = {
        @Index(name = "account_id", columnList = "account_id"),
        @Index(name = "party", columnList = "party"),
        @Index(name = "ranking1", columnList = "level,exp"),
        @Index(name = "ranking2", columnList = "gm,job")
})
public class DCharacter extends Model {

    @Id
    @DbComment("角色ID")
    Integer id;

    @NotNull
    @DbComment("角色名字")
    String name;
    @NotNull
    @ManyToOne
    @DbComment("所属账号")
    @JoinColumn(name = "account_id")
    DAccount account;

    @NotNull
    @DbComment("所在世界：官方版本有四个大区，公益服则没有世界的划分，所以此字段没有使用")
    Integer world = 0;

    @NotNull
    @DbComment("等级：目前是 1-255 级")
    Integer level = 0;
    @NotNull
    @DbComment("当前经验值")
    Integer exp = 0;
    @NotNull
    @DbComment("力量")
    Integer str = 0;
    @NotNull
    @DbComment("敏捷")
    Integer dex = 0;
    @NotNull
    @DbComment("运气")
    Integer luk = 0;
    @NotNull
    @DbComment("智力")
    @Column(name = "int_")
    Integer intelligence = 0;
    @NotNull
    @DbComment("当前生命值")
    Integer hp = 0;
    @NotNull
    @DbComment("当前魔法值")
    Integer mp = 0;
    @NotNull
    @DbComment("最大生命值")
    Integer maxHp = 0;
    @NotNull
    @DbComment("最大魔法值")
    Integer maxMp = 0;
    @NotNull
    Integer meso = 0;
    @NotNull
    Integer hpApUsed = 0;
    @NotNull
    @DbComment("职业")
    Integer job = 0;
    @NotNull
    @DbComment("皮肤颜色")
    Integer skinColor = 0;
    @NotNull
    @DbComment("性别")
    Gender gender = Gender.UNKNOWN;
    @NotNull
    @DbComment("人气")
    Integer fame = 0;
    @NotNull
    @DbComment("发型")
    Integer hair = 0;
    @NotNull
    @DbComment("脸型")
    Integer face = 0;
    @NotNull
    @DbComment("魔法力？")
    Integer ap = 0;
    @NotNull
    @DbComment("地图")
    Integer map = 0;
    @NotNull
    @DbComment("出生点")
    Integer spawnPoint = 0;
    @NotNull
    Integer gm = 0;
    @NotNull
    Integer party = 0;
    @NotNull
    @DbComment("好友数量上限")
    @Column(name = "buddy_capacity")
    Integer buddyCapacity = 25;
    @WhenCreated
    @DbComment("创建时间")
    LocalDateTime created;
    @ManyToOne
    @DbComment("所在公会")
    @DbForeignKey(noConstraint = true)
    @JoinColumn(name = "guild_id")
    DGuild guild;
    @NotNull
    Integer guildRank = 5;
    @NotNull
    Integer allianceRank = 5;
    @NotNull
    @DbComment("怪物书封面")
    Integer monsterBookCover = 0;
    @NotNull
    @DbComment("道场积分")
    Integer dojoPts = 0;
    @NotNull
    @DbComment("道场记录")
    Integer dojoRecord = 0;
    @NotNull
    @DbComment("宠物")
    String pets = "-1,-1,-1";
    @NotNull
    String sp = "0,0,0,0,0,0,0,0,0,0";
    @NotNull
    Integer subcategory = 0;
    @NotNull
    Integer jaguar = 0;
    @NotNull
    @Column(name = "rank_")
    Integer rank = 1;
    @NotNull
    Integer moveRank = 0;
    @NotNull
    Integer jobRank = 1;
    @NotNull
    Integer jobMoveRank = 0;
    @OneToOne
    @DbForeignKey(noConstraint = true)
    @DbComment("伴侣")
    @JoinColumn(name = "marriage_id")
    DCharacter marriage;
    @ManyToOne
    @DbForeignKey(noConstraint = true)
    DFamily family;
    @OneToOne
    @DbForeignKey(noConstraint = true)
    DCharacter senior;
    @OneToOne
    @DbForeignKey(noConstraint = true)
    @JoinColumn(name = "junior1")
    DCharacter junior1;
    @OneToOne
    @DbForeignKey(noConstraint = true)
    @JoinColumn(name = "junior2")
    DCharacter junior2;
    @NotNull
    Integer currentRep = 0;
    @NotNull
    Integer totalRep = 0;
    @NotNull
    String charMessage = "安安";
    @NotNull
    Integer expression = 0;
    @NotNull
    Integer constellation = 0;
    @NotNull
    Integer blood = 0;
    @NotNull
    Integer month = 0;
    @NotNull
    Integer day = 0;
    @NotNull
    Integer beans = 0;
    @NotNull
    BigDecimal prefix = BigDecimal.ZERO;
    @NotNull
    Integer skillzq = 0;
    @NotNull
    Integer grname = 0;
    @NotNull
    Integer jzname = 0;
    @NotNull
    Integer mrfbrw = 0;
    @NotNull
    Integer mrsjrw = 0;
    @NotNull
    Integer mrsgrw = 0;
    @NotNull
    Integer mrsbossrw = 0;
    @NotNull
    Integer hythd = 0;
    @NotNull
    Integer mrsgrwa = 0;
    @NotNull
    Integer mrfbrwa = 0;
    @NotNull
    Integer mrsbossrwa = 0;
    @NotNull
    Integer mrsgrws = 0;
    @NotNull
    Integer mrsbossrws = 0;
    @NotNull
    Integer mrfbrws = 0;
    @NotNull
    Integer mrsgrwas = 0;
    @NotNull
    Integer mrsbossrwas = 0;
    @NotNull
    Integer mrfbrwas = 0;
    Integer ddj = 0;
    Integer vip = 0;
    Integer bosslog = 0;
    @NotNull
    Integer djjl = 0;
    @NotNull
    Integer qiandao = 0;
    @NotNull
    Integer mountid = 0;
    @NotNull
    Integer sg = 0;

    @DbForeignKey(noConstraint = true)
    @OneToOne(mappedBy = "owner")
    DBuddy buddy;

    @DbForeignKey(noConstraint = true)
    @OneToMany(mappedBy = "buddy")
    List<DBuddy> buddies;

    public DCharacter(DAccount account) {
        this.account = account;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DAccount getAccount() {
        return account;
    }

    public void setAccount(DAccount account) {
        this.account = account;
    }

    public Integer getWorld() {
        return world;
    }

    public void setWorld(Integer world) {
        this.world = world;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getExp() {
        return exp;
    }

    public void setExp(Integer exp) {
        this.exp = exp;
    }

    public Integer getStr() {
        return str;
    }

    public void setStr(Integer str) {
        this.str = str;
    }

    public Integer getDex() {
        return dex;
    }

    public void setDex(Integer dex) {
        this.dex = dex;
    }

    public Integer getLuk() {
        return luk;
    }

    public void setLuk(Integer luk) {
        this.luk = luk;
    }

    public Integer getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(Integer intelligence) {
        this.intelligence = intelligence;
    }

    public Integer getHp() {
        return hp;
    }

    public void setHp(Integer hp) {
        this.hp = hp;
    }

    public Integer getMp() {
        return mp;
    }

    public void setMp(Integer mp) {
        this.mp = mp;
    }

    public Integer getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(Integer maxHp) {
        this.maxHp = maxHp;
    }

    public Integer getMaxMp() {
        return maxMp;
    }

    public void setMaxMp(Integer maxMp) {
        this.maxMp = maxMp;
    }

    public Integer getMeso() {
        return meso;
    }

    public void setMeso(Integer meso) {
        this.meso = meso;
    }

    public Integer getHpApUsed() {
        return hpApUsed;
    }

    public void setHpApUsed(Integer hpApUsed) {
        this.hpApUsed = hpApUsed;
    }

    public Integer getJob() {
        return job;
    }

    public void setJob(Integer job) {
        this.job = job;
    }

    public Integer getSkinColor() {
        return skinColor;
    }

    public void setSkinColor(Integer skinColor) {
        this.skinColor = skinColor;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Integer getFame() {
        return fame;
    }

    public void setFame(Integer fame) {
        this.fame = fame;
    }

    public Integer getHair() {
        return hair;
    }

    public void setHair(Integer hair) {
        this.hair = hair;
    }

    public Integer getFace() {
        return face;
    }

    public void setFace(Integer face) {
        this.face = face;
    }

    public Integer getAp() {
        return ap;
    }

    public void setAp(Integer ap) {
        this.ap = ap;
    }

    public Integer getMap() {
        return map;
    }

    public void setMap(Integer map) {
        this.map = map;
    }

    public Integer getSpawnPoint() {
        return spawnPoint;
    }

    public void setSpawnPoint(Integer spawnPoint) {
        this.spawnPoint = spawnPoint;
    }

    public Integer getGm() {
        return gm;
    }

    public void setGm(Integer gm) {
        this.gm = gm;
    }

    public Integer getParty() {
        return party;
    }

    public void setParty(Integer party) {
        this.party = party;
    }

    public Integer getBuddyCapacity() {
        return buddyCapacity;
    }

    public void setBuddyCapacity(Integer buddyCapacity) {
        this.buddyCapacity = buddyCapacity;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public DGuild getGuild() {
        return guild;
    }

    public void setGuild(DGuild guild) {
        this.guild = guild;
    }

    public Integer getGuildRank() {
        return guildRank;
    }

    public void setGuildRank(Integer guildRank) {
        this.guildRank = guildRank;
    }

    public Integer getAllianceRank() {
        return allianceRank;
    }

    public void setAllianceRank(Integer allianceRank) {
        this.allianceRank = allianceRank;
    }

    public Integer getMonsterBookCover() {
        return monsterBookCover;
    }

    public void setMonsterBookCover(Integer monsterBookCover) {
        this.monsterBookCover = monsterBookCover;
    }

    public Integer getDojoPts() {
        return dojoPts;
    }

    public void setDojoPts(Integer dojoPts) {
        this.dojoPts = dojoPts;
    }

    public Integer getDojoRecord() {
        return dojoRecord;
    }

    public void setDojoRecord(Integer dojoRecord) {
        this.dojoRecord = dojoRecord;
    }

    public String getPets() {
        return pets;
    }

    public void setPets(String pets) {
        this.pets = pets;
    }

    public String getSp() {
        return sp;
    }

    public void setSp(String sp) {
        this.sp = sp;
    }

    public Integer getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(Integer subcategory) {
        this.subcategory = subcategory;
    }

    public Integer getJaguar() {
        return jaguar;
    }

    public void setJaguar(Integer jaguar) {
        this.jaguar = jaguar;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Integer getMoveRank() {
        return moveRank;
    }

    public void setMoveRank(Integer moveRank) {
        this.moveRank = moveRank;
    }

    public Integer getJobRank() {
        return jobRank;
    }

    public void setJobRank(Integer jobRank) {
        this.jobRank = jobRank;
    }

    public Integer getJobMoveRank() {
        return jobMoveRank;
    }

    public void setJobMoveRank(Integer jobMoveRank) {
        this.jobMoveRank = jobMoveRank;
    }

    public DCharacter getMarriage() {
        return marriage;
    }

    public void setMarriage(DCharacter marriage) {
        this.marriage = marriage;
    }

    public DFamily getFamily() {
        return family;
    }

    public void setFamily(DFamily family) {
        this.family = family;
    }

    public DCharacter getSenior() {
        return senior;
    }

    public void setSenior(DCharacter senior) {
        this.senior = senior;
    }

    public DCharacter getJunior1() {
        return junior1;
    }

    public void setJunior1(DCharacter junior1) {
        this.junior1 = junior1;
    }

    public DCharacter getJunior2() {
        return junior2;
    }

    public void setJunior2(DCharacter junior2) {
        this.junior2 = junior2;
    }

    public Integer getCurrentRep() {
        return currentRep;
    }

    public void setCurrentRep(Integer currentRep) {
        this.currentRep = currentRep;
    }

    public Integer getTotalRep() {
        return totalRep;
    }

    public void setTotalRep(Integer totalRep) {
        this.totalRep = totalRep;
    }

    public String getCharMessage() {
        return charMessage;
    }

    public void setCharMessage(String charMessage) {
        this.charMessage = charMessage;
    }

    public Integer getExpression() {
        return expression;
    }

    public void setExpression(Integer expression) {
        this.expression = expression;
    }

    public Integer getConstellation() {
        return constellation;
    }

    public void setConstellation(Integer constellation) {
        this.constellation = constellation;
    }

    public Integer getBlood() {
        return blood;
    }

    public void setBlood(Integer blood) {
        this.blood = blood;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getBeans() {
        return beans;
    }

    public void setBeans(Integer beans) {
        this.beans = beans;
    }

    public BigDecimal getPrefix() {
        return prefix;
    }

    public void setPrefix(BigDecimal prefix) {
        this.prefix = prefix;
    }

    public Integer getSkillzq() {
        return skillzq;
    }

    public void setSkillzq(Integer skillzq) {
        this.skillzq = skillzq;
    }

    public Integer getGrname() {
        return grname;
    }

    public void setGrname(Integer grname) {
        this.grname = grname;
    }

    public Integer getJzname() {
        return jzname;
    }

    public void setJzname(Integer jzname) {
        this.jzname = jzname;
    }

    public Integer getMrfbrw() {
        return mrfbrw;
    }

    public void setMrfbrw(Integer mrfbrw) {
        this.mrfbrw = mrfbrw;
    }

    public Integer getMrsjrw() {
        return mrsjrw;
    }

    public void setMrsjrw(Integer mrsjrw) {
        this.mrsjrw = mrsjrw;
    }

    public Integer getMrsgrw() {
        return mrsgrw;
    }

    public void setMrsgrw(Integer mrsgrw) {
        this.mrsgrw = mrsgrw;
    }

    public Integer getMrsbossrw() {
        return mrsbossrw;
    }

    public void setMrsbossrw(Integer mrsbossrw) {
        this.mrsbossrw = mrsbossrw;
    }

    public Integer getHythd() {
        return hythd;
    }

    public void setHythd(Integer hythd) {
        this.hythd = hythd;
    }

    public Integer getMrsgrwa() {
        return mrsgrwa;
    }

    public void setMrsgrwa(Integer mrsgrwa) {
        this.mrsgrwa = mrsgrwa;
    }

    public Integer getMrfbrwa() {
        return mrfbrwa;
    }

    public void setMrfbrwa(Integer mrfbrwa) {
        this.mrfbrwa = mrfbrwa;
    }

    public Integer getMrsbossrwa() {
        return mrsbossrwa;
    }

    public void setMrsbossrwa(Integer mrsbossrwa) {
        this.mrsbossrwa = mrsbossrwa;
    }

    public Integer getMrsgrws() {
        return mrsgrws;
    }

    public void setMrsgrws(Integer mrsgrws) {
        this.mrsgrws = mrsgrws;
    }

    public Integer getMrsbossrws() {
        return mrsbossrws;
    }

    public void setMrsbossrws(Integer mrsbossrws) {
        this.mrsbossrws = mrsbossrws;
    }

    public Integer getMrfbrws() {
        return mrfbrws;
    }

    public void setMrfbrws(Integer mrfbrws) {
        this.mrfbrws = mrfbrws;
    }

    public Integer getMrsgrwas() {
        return mrsgrwas;
    }

    public void setMrsgrwas(Integer mrsgrwas) {
        this.mrsgrwas = mrsgrwas;
    }

    public Integer getMrsbossrwas() {
        return mrsbossrwas;
    }

    public void setMrsbossrwas(Integer mrsbossrwas) {
        this.mrsbossrwas = mrsbossrwas;
    }

    public Integer getMrfbrwas() {
        return mrfbrwas;
    }

    public void setMrfbrwas(Integer mrfbrwas) {
        this.mrfbrwas = mrfbrwas;
    }

    public Integer getDdj() {
        return ddj;
    }

    public void setDdj(Integer ddj) {
        this.ddj = ddj;
    }

    public Integer getVip() {
        return vip;
    }

    public void setVip(Integer vip) {
        this.vip = vip;
    }

    public Integer getBosslog() {
        return bosslog;
    }

    public void setBosslog(Integer bosslog) {
        this.bosslog = bosslog;
    }

    public Integer getDjjl() {
        return djjl;
    }

    public void setDjjl(Integer djjl) {
        this.djjl = djjl;
    }

    public Integer getQiandao() {
        return qiandao;
    }

    public void setQiandao(Integer qiandao) {
        this.qiandao = qiandao;
    }

    public Integer getMountid() {
        return mountid;
    }

    public void setMountid(Integer mountid) {
        this.mountid = mountid;
    }

    public Integer getSg() {
        return sg;
    }

    public void setSg(Integer sg) {
        this.sg = sg;
    }

    public DBuddy getBuddy() {
        return buddy;
    }

    public void setBuddy(DBuddy buddy) {
        this.buddy = buddy;
    }

    public List<DBuddy> getBuddies() {
        return buddies;
    }

    public void setBuddies(List<DBuddy> buddies) {
        this.buddies = buddies;
    }
}
