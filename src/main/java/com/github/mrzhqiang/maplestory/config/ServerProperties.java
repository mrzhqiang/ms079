package com.github.mrzhqiang.maplestory.config;

import com.github.mrzhqiang.maplestory.util.Numbers;
import com.google.inject.name.Named;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.Properties;

/**
 * 服务器属性。
 */
@Singleton
public final class ServerProperties {

    private final String name;
    private final int flag;
    private final int onlineLimit;
    private final int charactersLimit;
    private final int accountLimit;
    private final String address;

    private final boolean autoRegister;
    private final boolean randDrop;

    private final int loginPort;
    private final boolean adminLogin;
    private final String loginMessage;
    private final String loginEventMessage;

    private final int channelPort;
    private final int channelCount;

    private final int expRate;
    private final int goldRate;
    private final int dropRate;
    private final int bossDropRate;
    private final int cashRate;

    private final int worldFlags;

    private final int mallPort;

    private final boolean debug;
    private final boolean packetLogger;
    private final boolean packetDebugLogger;

    private final boolean adventurer;
    private final boolean knights;
    private final boolean warGod;
    private final boolean banSwitch;

    private final String[] events;
    private final String[] mallDisabled;
    private final String[] cashJy;
    private final String[] gysj;

    @Inject
    public ServerProperties(@Named("config") Properties properties) {
        this.name = properties.getProperty("server.name", "");
        this.flag = Numbers.ofInt(properties.getProperty("server.flag", "3"));
        this.onlineLimit = Numbers.ofInt(properties.getProperty("server.limit.online", "100"));
        this.charactersLimit = Numbers.ofInt(properties.getProperty("server.limit.characters", "3"));
        this.accountLimit = Numbers.ofInt(properties.getProperty("server.limit.account", "1"));
        this.address = properties.getProperty("server.address", "127.0.0.1");
        this.autoRegister = Boolean.parseBoolean(properties.getProperty("server.register.auto", "true"));
        this.randDrop = Boolean.parseBoolean(properties.getProperty("server.rand.drop", "false"));
        this.loginPort = Numbers.ofInt(properties.getProperty("server.login.port", "9595"));
        this.adminLogin = Boolean.parseBoolean(properties.getProperty("server.login.admin", "false"));
        this.loginMessage = properties.getProperty("server.login.message", "");
        this.loginEventMessage = properties.getProperty("server.login.message.event", "");
        this.channelPort = Numbers.ofInt(properties.getProperty("server.channel.port", "7575"));
        this.channelCount = Numbers.ofInt(properties.getProperty("server.channel.count", "3"));
        this.expRate = Numbers.ofInt(properties.getProperty("server.world.rate.exp", "1"));
        this.goldRate = Numbers.ofInt(properties.getProperty("server.world.rate.gold", "1"));
        this.dropRate = Numbers.ofInt(properties.getProperty("server.world.rate.drop", "1"));
        this.bossDropRate = Numbers.ofInt(properties.getProperty("server.world.rate.drop.boss", "1"));
        this.cashRate = Numbers.ofInt(properties.getProperty("server.world.rate.cash", "1"));
        this.worldFlags = Numbers.ofInt(properties.getProperty("server.world.flags", "0"));
        this.mallPort = Numbers.ofInt(properties.getProperty("server.mall.port", "8600"));
        this.debug = Boolean.parseBoolean(properties.getProperty("server.debug.enabled", "false"));
        this.packetLogger = Boolean.parseBoolean(properties.getProperty("server.logger.packet", "false"));
        this.packetDebugLogger = Boolean.parseBoolean(properties.getProperty("server.logger.packet.debug", "false"));
        this.adventurer = Boolean.parseBoolean(properties.getProperty("server.job.adventurer", "true"));
        this.knights = Boolean.parseBoolean(properties.getProperty("server.job.knights", "false"));
        this.warGod = Boolean.parseBoolean(properties.getProperty("server.job.war-god", "false"));
        this.banSwitch = Boolean.parseBoolean(properties.getProperty("server.ban.switch", "false"));
        this.events = properties.getProperty("server.events", "").split(",");
        this.mallDisabled = properties.getProperty("server.mall.disabled", "").split(",");
        this.cashJy = properties.getProperty("server.cashjy", "").split(",");
        this.gysj = properties.getProperty("server.gysj", "").split(",");
    }

    public int getChannelCount() {
        return channelCount;
    }

    public int getExpRate() {
        return expRate;
    }

    public int getGoldRate() {
        return goldRate;
    }

    public int getDropRate() {
        return dropRate;
    }

    public int getBossDropRate() {
        return bossDropRate;
    }

    public int getCashRate() {
        return cashRate;
    }

    public String getName() {
        return name;
    }

    public String getLoginEventMessage() {
        return loginEventMessage;
    }

    public String getLoginMessage() {
        return loginMessage;
    }

    public int getFlag() {
        return flag;
    }

    public boolean isAutoRegister() {
        return autoRegister;
    }

    public boolean isRandDrop() {
        return randDrop;
    }

    public int getWorldFlags() {
        return worldFlags;
    }

    public boolean isAdminLogin() {
        return adminLogin;
    }

    public int getOnlineLimit() {
        return onlineLimit;
    }

    public int getCharactersLimit() {
        return charactersLimit;
    }

    public int getAccountLimit() {
        return accountLimit;
    }

    public int getChannelPort() {
        return channelPort;
    }

    public int getLoginPort() {
        return loginPort;
    }

    public int getMallPort() {
        return mallPort;
    }

    public String getAddress() {
        return address;
    }

    public boolean isDebug() {
        return debug;
    }

    public boolean isPacketLogger() {
        return packetLogger;
    }

    public boolean isPacketDebugLogger() {
        return packetDebugLogger;
    }

    public boolean isAdventurer() {
        return adventurer;
    }

    public boolean isKnights() {
        return knights;
    }

    public boolean isWarGod() {
        return warGod;
    }

    public String[] getEvents() {
        return Arrays.copyOf(events, events.length);
    }

    public String[] getMallDisabled() {
        return Arrays.copyOf(mallDisabled, mallDisabled.length);
    }

    public String[] getCashJy() {
        return Arrays.copyOf(cashJy, cashJy.length);
    }

    public String[] getGysj() {
        return Arrays.copyOf(gysj, gysj.length);
    }

    public boolean isBanSwitch() {
        return banSwitch;
    }
}
