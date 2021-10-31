package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.*;

import javax.persistence.*;
import javax.persistence.Index;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "characters", indexes = {
        @Index(name = "accountid", columnList = "accountid"),
        @Index(name = "party", columnList = "party"),
        @Index(name = "ranking1", columnList = "level,exp"),
        @Index(name = "ranking2", columnList = "gm,job")
})
public class DCharacter extends Model {

    @Id
    public Integer id;

    @NotNull
    @ManyToOne
    @DbForeignKey(noConstraint = true)
    @JoinColumn(name = "accountid")
    @DbComment("所属账号")
    public DAccount account;
    @NotNull
    @Length(20)
    @DbComment("角色名")
    public String name;

    @NotNull
    @DbComment("世界？大区？")
    public Integer world = 0;

    @NotNull
    @DbComment("等级")
    public Integer level = 0;
    @NotNull
    @DbComment("经验值")
    public Integer exp = 0;
    @NotNull
    @DbComment("力量")
    public Integer str = 0;
    @NotNull
    @DbComment("敏捷")
    public Integer dex = 0;
    @NotNull
    @DbComment("运气")
    public Integer luk = 0;
    @NotNull
    @Column(name = "int")
    @DbComment("智力")
    public Integer intelligence = 0;
    @NotNull
    public Integer hp = 0;
    @NotNull
    public Integer mp = 0;
    @NotNull
    @Column(name = "maxhp")
    public Integer maxHP = 0;
    @NotNull
    @Column(name = "maxmp")
    public Integer maxMP = 0;
    @NotNull
    @Column(name = "meso")
    public Integer meso = 0;
    @NotNull
    @Column(name = "hpApUsed")
    public Integer hpApUsed = 0;
    @NotNull
    public Integer job = 0;
    @NotNull
    @Column(name = "skincolor")
    @DbComment("皮肤颜色")
    public Integer skinColor = 0;
    @NotNull
    @DbComment("性别")
    public Integer gender = 0;
    @NotNull
    @DbComment("人气")
    public Integer fame = 0;
    @NotNull
    @DbComment("发型")
    public Integer hair = 0;
    @NotNull
    @DbComment("脸型")
    public Integer face = 0;
    @NotNull
    public Integer ap = 0;
    @NotNull
    public Integer map = 0;
    @NotNull
    @Column(name = "spawnpoint")
    public Integer spawnPoint = 0;
    @NotNull
    @Column(name = "gm")
    public Integer gm = 0;
    @NotNull
    public Integer party = 0;
    @NotNull
    @Column(name = "buddyCapacity")
    public Integer buddyCapacity = 25;
    @WhenCreated
    @Column(name = "createdate")
    public LocalDateTime created;
    @ManyToOne
    @DbForeignKey(noConstraint = true)
    @JoinColumn(name = "guildid")
    public DGuild guild;
    @NotNull
    @Column(name = "guildrank")
    public Integer guildRank = 5;
    @NotNull
    @Column(name = "allianceRank")
    public Integer allianceRank = 5;
    @NotNull
    @Column(name = "monsterbookcover")
    public Integer monsterBookCover = 0;
    @NotNull
    @Column(name = "dojo_pts")
    public Integer dojoPts = 0;
    @NotNull
    @Column(name = "dojoRecord")
    public Integer dojoRecord = 0;
    @NotNull
    @Length(13)
    @Column(name = "pets")
    public String pets = "-1,-1,-1";
    @NotNull
    @Length(255)
    @Column(name = "sp")
    public String sp = "0,0,0,0,0,0,0,0,0,0";
    @NotNull
    @Column(name = "subcategory")
    public Integer subcategory = 0;
    @NotNull
    @Column(name = "Jaguar")
    public Integer jaguar = 0;
    @NotNull
    @Column(name = "rank")
    public Integer rank = 1;
    @NotNull
    @Column(name = "rankMove")
    public Integer rankMove = 0;
    @NotNull
    @Column(name = "jobRank")
    public Integer jobRank = 1;
    @NotNull
    @Column(name = "jobRankMove")
    public Integer jobRankMove = 0;
    @OneToOne
    @DbForeignKey(noConstraint = true)
    @JoinColumn(name = "marriageId")
    @DbComment("伴侣")
    public DCharacter marriage;
    @ManyToOne
    @DbForeignKey(noConstraint = true)
    @JoinColumn(name = "familyid")
    public DFamily family;
    @OneToOne
    @DbForeignKey(noConstraint = true)
    @JoinColumn(name = "seniorid")
    public DCharacter senior;
    @OneToOne
    @DbForeignKey(noConstraint = true)
    @JoinColumn(name = "junior1")
    public DCharacter junior1;
    @OneToOne
    @DbForeignKey(noConstraint = true)
    @JoinColumn(name = "junior2")
    public DCharacter junior2;
    @NotNull
    @Column(name = "currentrep")
    public Integer currentRep = 0;
    @NotNull
    @Column(name = "totalrep")
    public Integer totalRep = 0;
    @NotNull
    @Length(128)
    @Column(name = "charmessage")
    public String charMessage = "安安";
    @NotNull
    @Column(name = "expression")
    public Integer expression = 0;
    @NotNull
    @Column(name = "constellation")
    public Integer constellation = 0;
    @NotNull
    @Column(name = "blood")
    public Integer blood = 0;
    @NotNull
    @Column(name = "month")
    public Integer month = 0;
    @NotNull
    @Column(name = "day")
    public Integer day = 0;
    @NotNull
    @Column(name = "beans")
    public Integer beans = 0;
    @NotNull
    @Column(name = "prefix")
    public BigDecimal prefix = BigDecimal.ZERO;
    @NotNull
    @Column(name = "skillzq")
    public Integer skillzq = 0;
    @NotNull
    @Column(name = "grname")
    public Integer grname = 0;
    @NotNull
    @Column(name = "jzname")
    public Integer jzname = 0;
    @NotNull
    @Column(name = "mrfbrw")
    public Integer mrfbrw = 0;
    @NotNull
    @Column(name = "mrsjrw")
    public Integer mrsjrw = 0;
    @NotNull
    @Column(name = "mrsgrw")
    public Integer mrsgrw = 0;
    @NotNull
    @Column(name = "mrsbossrw")
    public Integer mrsbossrw = 0;
    @NotNull
    @Column(name = "hythd")
    public Integer hythd = 0;
    @NotNull
    @Column(name = "mrsgrwa")
    public Integer mrsgrwa = 0;
    @NotNull
    @Column(name = "mrfbrwa")
    public Integer mrfbrwa = 0;
    @NotNull
    @Column(name = "mrsbossrwa")
    public Integer mrsbossrwa = 0;
    @NotNull
    @Column(name = "mrsgrws")
    public Integer mrsgrws = 0;
    @NotNull
    @Column(name = "mrsbossrws")
    public Integer mrsbossrws = 0;
    @NotNull
    @Column(name = "mrfbrws")
    public Integer mrfbrws = 0;
    @NotNull
    @Column(name = "mrsgrwas")
    public Integer mrsgrwas = 0;
    @NotNull
    @Column(name = "mrsbossrwas")
    public Integer mrsbossrwas = 0;
    @NotNull
    @Column(name = "mrfbrwas")
    public Integer mrfbrwas = 0;
    @Column(name = "ddj")
    public Integer ddj = 0;
    @Column(name = "vip")
    public Integer vip = 0;
    @Column(name = "bosslog")
    public Integer bosslog = 0;
    @NotNull
    @Column(name = "djjl")
    public Integer djjl = 0;
    @NotNull
    @Column(name = "qiandao")
    public Integer qiandao = 0;
    @NotNull
    @Column(name = "mountid")
    public Integer mountid = 0;
    @NotNull
    @Column(name = "sg")
    public Integer sg = 0;

    @DbForeignKey(noConstraint = true)
    @OneToOne(mappedBy = "owner")
    public DBuddy buddy;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DCharacter that = (DCharacter) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
