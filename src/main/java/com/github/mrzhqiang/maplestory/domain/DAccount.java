package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.*;

import javax.persistence.*;
import javax.persistence.Index;
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
    public final String name;
    @NotNull
    @Length(128)
    @DbComment("用户密码，不能为 Null")
    public final String password;
    @Length(32)
    @DbComment("用户密码的盐")
    public String salt;
    @Length(134)
//    @Column(name = "2ndpassword")
    @Column(name = "second_password")
    @DbComment("二级密码")
    public String secondPassword;
    @Length(32)
    @DbComment("二级密码的盐")
    public String salt2;
    @Column(name = "loggedin")
    @DbComment("登录状态：是否在线？0 false == 离线；1 true == 在线")
    public Boolean loggedIn = false;
    @WhenModified
    @Column(name = "lastlogin")
    @DbComment("上次登录时间")
    public LocalDateTime lastLogin;
    @WhenCreated
    @Column(name = "createdat")
    @DbComment("创建时间")
    public LocalDateTime created;
    @DbComment("生日")
    public LocalDate birthday = LocalDate.now();
    @DbComment("是否禁用")
    public Boolean banned = false;
    @Column(name = "banreason")
    @DbComment("禁用理由")
    public String banReason;
    @DbComment("是否为 GM")
    public Boolean gm = false;
    @DbComment("电子邮件")
    public String email;
    @Column(name = "macs")
    @DbComment("硬件地址")
    public String mac;
    @Column(name = "tempban")
    @DbComment("临时禁用时间")
    public LocalDateTime tempBan;
    @Column(name = "greason")
    public Integer gReason;
    @Column(name = "ACash")
    public Integer aCash;
    @Column(name = "mPoints")
    public Integer mPoint;
    // todo Enum 10 is ??
    @Column(name = "gender")
    public Integer gender;
    @Length(64)
    @Column(name = "SessionIP")
    public String sessionIP;
    @Column(name = "points")
    public Integer point;
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
    @NotNull
    @Column(name = "VIP")
    public Integer vip = 0;
    @NotNull
    @Column(name = "money")
    public Integer money = 0;
    @NotNull
    @Column(name = "moneyb")
    public Integer moneyB = 0;
    @NotNull
    @Column(name = "lastGainHM")
    public Long lastGainHM = 0L;
    @Column(name = "paypalNX")
    public Integer paypalNX;

    @OneToMany(mappedBy = "account")
    public List<DCharacter> characterList;

    public DAccount(String name, String password) {
        this.name = name;
        this.password = password;
    }
}
