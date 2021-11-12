package handling.login.handler;

import client.LoginCrypto;
import com.github.mrzhqiang.maplestory.config.ServerProperties;
import com.github.mrzhqiang.maplestory.domain.DAccount;
import com.github.mrzhqiang.maplestory.domain.query.QDAccount;
import javax.inject.Inject;
import javax.inject.Singleton;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 自动注册工具类。
 */
@Singleton
public final class AutoRegister {

    private static final String DEFAULT_EMAIL_ADDRESS = "auto.register@mail.com";
    private static final String SESSION_IP_FORMAT = "%s";

    private boolean success = false;
    private boolean mac = true;

    private final ServerProperties properties;

    @Inject
    public AutoRegister(ServerProperties properties) {
        this.properties = properties;
    }

    public boolean isAutoRegister() {
        return properties.isAutoRegister();
    }

    public boolean isSuccess() {
        return success;
    }

    public void reset() {
        success = true;
        mac = true;
    }

    public boolean isMac() {
        return mac;
    }

    public boolean getAccountExists(String login) {
        return new QDAccount().name.eq(login).exists();
    }

    public void createAccount(String login, String pwd, String eip, String macs) {
        int macCount = new QDAccount().mac.eq(macs).findCount();
        if (macCount < properties.getAccountLimit()) {
            DAccount account = new DAccount();
            account.name = login;
            account.password = LoginCrypto.hexSha1(pwd);
            account.email = DEFAULT_EMAIL_ADDRESS;
            account.birthday = LocalDate.now();
            account.mac = macs;
            account.loggedIn = 0;
            account.banned = 0;
            account.gm = 0;
            account.aCash = 0;
            account.point = 0;
            account.money = 0;
            account.moneyB = 0;
            account.mPoint = 0;
            account.vPoint = 0;
            account.lastGainHM = 0L;
            account.paypalNX = 0;
            account.gender = 10;
            account.tempBan = LocalDateTime.now().minusDays(1);
            account.sessionIP = String.format(SESSION_IP_FORMAT, eip.substring(1, eip.lastIndexOf(':')));
            account.save();
            success = true;
        }

        if (macCount >= properties.getAccountLimit()) {
            mac = false;
        }
    }
}
