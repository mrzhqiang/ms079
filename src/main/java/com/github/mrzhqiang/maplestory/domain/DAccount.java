package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.DbComment;
import io.ebean.annotation.Length;
import io.ebean.annotation.NotNull;
import io.ebean.annotation.WhenCreated;
import io.ebean.annotation.WhenModified;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "accounts",
        uniqueConstraints = @UniqueConstraint(columnNames = {"name"}),
        indexes = {@Index(name = "ranking1", columnList = "id,banned,gm")})
public class DAccount extends Model {

    @Id
    @DbComment("账户ID")
    Integer id;

    @NotNull
    @DbComment("账户名")
    @Length(16)
    final String name;
    @NotNull
    @DbComment("密码")
    String password;

    @DbComment("密码盐")
    String salt;
    @DbComment("二级密码")
    @Column(name = "second_password")
    String secondPassword;
    @DbComment("二级密码盐")
    @Column(name = "second_salt")
    String secondSalt;

    @NotNull
    @DbComment("登录状态：0-未登录(默认)，1-服务过渡，2-已登录，3-等待，4-商城过渡，5-商城已登录，6-切换频道。")
    LoginState state = LoginState.NOT_LOGIN;
    @DbComment("上次登录的时间戳")
    LocalDateTime lastLogin;
    @DbComment("上次离线的时间戳")
    LocalDateTime lastLogon;
    LocalDateTime expiration;

    @NotNull
    @DbComment("性别：0-男，1-女，10-未知")
    Gender gender = Gender.UNKNOWN;
    @DbComment("生日")
    LocalDate birthday = LocalDate.of(2000, 1, 1);
    @DbComment("电子邮箱地址")
    String email;
    @DbComment("网卡地址")
    String mac;
    @DbComment("会话IP地址")
    @Column(name = "session_ip")
    String sessionIp;

    @NotNull
    @DbComment("GM 等级：0-非GM-普通用户，1-1级GM，2-2级GM，...")
    Integer gm = 0;
    Integer vip;
    Long cash = 0L;
    @Column(name = "m_points")
    Long mPoints = 0L;
    @NotNull
    Long points = 0L;
    @NotNull
    @Column(name = "v_points")
    Long vPoints = 0L;
    @NotNull
    Long money = 0L;
    @NotNull
    @Column(name = "money_b")
    Long moneyB = 0L;
    @NotNull
    @Column(name = "last_gain_hm")
    Long lastGainHm = 0L;
    Integer paypalNx;

    @NotNull
    @DbComment("是否禁用：0-false-未禁用，!0-true-禁用")
    Boolean banned = false;
    @DbComment("禁用原因")
    @Column(name = "ban_reason")
    String banReason;
    @DbComment("临时禁用时间")
    @Column(name = "temp_ban")
    LocalDateTime tempBan;
    @Column(name = "g_Reason")
    Integer gReason;

    @DbComment("创建时间")
    @WhenCreated
    LocalDateTime created;
    @DbComment("修改时间")
    @WhenModified
    LocalDateTime modified;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    List<DCharacter> characters;

    public DAccount(String name, String password) {
        this.name = name;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getSecondPassword() {
        return secondPassword;
    }

    public void setSecondPassword(String secondPassword) {
        this.secondPassword = secondPassword;
    }

    public String getSecondSalt() {
        return secondSalt;
    }

    public void setSecondSalt(String secondSalt) {
        this.secondSalt = secondSalt;
    }

    public LoginState getState() {
        return state;
    }

    public void setState(LoginState state) {
        this.state = state;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public LocalDateTime getLastLogon() {
        return lastLogon;
    }

    public void setLastLogon(LocalDateTime lastLogon) {
        this.lastLogon = lastLogon;
    }

    public LocalDateTime getExpiration() {
        return expiration;
    }

    public void setExpiration(LocalDateTime expiration) {
        this.expiration = expiration;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getSessionIp() {
        return sessionIp;
    }

    public void setSessionIp(String sessionIp) {
        this.sessionIp = sessionIp;
    }

    public Integer getGm() {
        return gm;
    }

    public void setGm(Integer gm) {
        this.gm = gm;
    }

    public Integer getVip() {
        return vip;
    }

    public void setVip(Integer vip) {
        this.vip = vip;
    }

    public Long getCash() {
        return cash;
    }

    public void setCash(Long cash) {
        this.cash = cash;
    }

    public Long getmPoints() {
        return mPoints;
    }

    public void setmPoints(Long mPoints) {
        this.mPoints = mPoints;
    }

    public Long getPoints() {
        return points;
    }

    public void setPoints(Long points) {
        this.points = points;
    }

    public Long getvPoints() {
        return vPoints;
    }

    public void setvPoints(Long vPoints) {
        this.vPoints = vPoints;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    public Long getMoneyB() {
        return moneyB;
    }

    public void setMoneyB(Long moneyB) {
        this.moneyB = moneyB;
    }

    public Long getLastGainHm() {
        return lastGainHm;
    }

    public void setLastGainHm(Long lastGainHm) {
        this.lastGainHm = lastGainHm;
    }

    public Integer getPaypalNx() {
        return paypalNx;
    }

    public void setPaypalNx(Integer paypalNx) {
        this.paypalNx = paypalNx;
    }

    public Boolean getBanned() {
        return banned;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }

    public String getBanReason() {
        return banReason;
    }

    public void setBanReason(String banReason) {
        this.banReason = banReason;
    }

    public LocalDateTime getTempBan() {
        return tempBan;
    }

    public void setTempBan(LocalDateTime tempBan) {
        this.tempBan = tempBan;
    }

    public Integer getgReason() {
        return gReason;
    }

    public void setgReason(Integer gReason) {
        this.gReason = gReason;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getModified() {
        return modified;
    }

    public void setModified(LocalDateTime modified) {
        this.modified = modified;
    }

    public List<DCharacter> getCharacters() {
        return characters;
    }

    public void setCharacters(List<DCharacter> characters) {
        this.characters = characters;
    }
}
