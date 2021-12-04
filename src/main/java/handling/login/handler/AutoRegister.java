package handling.login.handler;

import client.LoginCrypto;
import com.github.mrzhqiang.maplestory.config.ServerProperties;
import com.github.mrzhqiang.maplestory.domain.DAccount;
import com.github.mrzhqiang.maplestory.domain.Gender;
import com.github.mrzhqiang.maplestory.domain.query.QDAccount;
import com.github.mrzhqiang.maplestory.domain.LoginState;

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
            DAccount account = new DAccount(login, LoginCrypto.hexSha1(pwd));
            account.setEmail(DEFAULT_EMAIL_ADDRESS);
            account.setBirthday(LocalDate.now());
            account.setMac(macs);
            account.setState(LoginState.NOT_LOGIN);
            account.setBanned(false);
            account.setGm(0);
            account.setCash(0L);
            account.setPoints(0L);
            account.setMoney(0L);
            account.setMoneyB(0L);
            account.setmPoints(0L);
            account.setvPoints(0L);
            account.setLastGainHm(0L);
            account.setPaypalNx(0);
            account.setGender(Gender.UNKNOWN);
            account.setTempBan(LocalDateTime.now().minusDays(1));
            account.setSessionIp(String.format(SESSION_IP_FORMAT, eip.substring(1, eip.lastIndexOf(':'))));
            account.save();
            success = true;
        }

        if (macCount >= properties.getAccountLimit()) {
            mac = false;
        }
    }
}
