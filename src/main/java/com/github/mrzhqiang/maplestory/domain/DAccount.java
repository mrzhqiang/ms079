package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.DbComment;
import io.ebean.annotation.Length;
import io.ebean.annotation.NotNull;
import io.ebean.annotation.WhenCreated;
import io.ebean.annotation.WhenModified;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 账号表
 */
@Entity
@Table(name = "accounts", indexes = {
        @Index(name = "ranking1", columnList = "id, banned, gm")
})
public class DAccount extends Model {

    @Id
    @DbComment("账号ID，自增长")
    public Integer id;
    @NotNull
    @Length(30)
    @Column(name = "name", unique = true)
    @DbComment("用户名/用户账号，不能为 Null")
    public String name;
    @NotNull
    @Length(128)
    @DbComment("用户密码，不能为 Null")
    public String password;
    @Length(32)
    @DbComment("用户密码的盐")
    public String salt;
    @Length(134)
    @Column(name = "2ndpassword")
//    @Column(name = "second_password")
    @DbComment("二级密码")
    public String secondPassword;
    @Length(32)
    @DbComment("二级密码的盐")
    public String salt2;
    @NotNull
    @Column(name = "loggedin")
    @DbComment("登录状态")
    public Integer loggedIn;
    @WhenModified
    @Column(name = "lastlogin")
    @DbComment("上次登录时间")
    public LocalDateTime lastLogin;
    @NotNull
    @WhenCreated
    @Column(name = "createdat")
    @DbComment("创建时间")
    public LocalDateTime created;
    @NotNull
    @DbComment("生日")
    public LocalDate birthday;
    @NotNull
    @DbComment("是否禁用")
    public Integer banned;
    @Column(name = "banreason")
    @DbComment("禁用理由")
    public String banReason;
    @NotNull
    @DbComment("是否为 GM")
    public Integer gm;
    @DbComment("电子邮件")
    public String email;
    @Column(name = "macs")
    @DbComment("硬件地址")
    public String mac;
    @NotNull
    @Column(name = "tempban")
    @DbComment("临时禁用时间")
    public LocalDateTime tempBan;
    @Column(name = "greason")
    public Integer gReason;
    @Column(name = "ACash")
    public Integer aCash;
    @Column(name = "mPoints")
    public Integer mPoint;
    @NotNull
    @Column(name = "gender")
    public Integer gender;
    @Length(64)
    @Column(name = "SessionIP")
    public String sessionIP;
    @NotNull
    @Column(name = "points")
    public Integer point;
    @NotNull
    @Column(name = "vpoints")
    public Integer vPoint;
    @Column(name = "lastlogon")
    public LocalDateTime lastLogon;
    @Length(255)
    @Column(name = "facebook_id")
    public String facebookId;
    @Length(255)
    @Column(name = "access_token")
    public String accessToken;
    @Length(255)
    @Column(name = "password_otp")
    public String passwordOtp;
    @Column(name = "expiration")
    public LocalDateTime expiration;
    @Column(name = "VIP")
    public Integer vip;
    @NotNull
    @Column(name = "money")
    public Integer money;
    @NotNull
    @Column(name = "moneyb")
    public Integer moneyB;
    @NotNull
    @Column(name = "lastGainHM")
    public Long lastGainHM;
    @Column(name = "paypalNX")
    public Integer paypalNX;

    @OneToMany(mappedBy = "account")
    public List<DCharacter> characters;

}
