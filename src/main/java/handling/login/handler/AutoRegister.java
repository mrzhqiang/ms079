package handling.login.handler;

import client.LoginCrypto;
import com.github.mrzhqiang.maplestory.domain.DAccount;
import com.github.mrzhqiang.maplestory.domain.query.QDAccount;
import constants.ServerConstants;

import java.time.LocalDate;

public final class AutoRegister {
    private AutoRegister() {
        // no instance
    }

    private static final int ACCOUNTS_PER_MAC = 1;
    private static final boolean autoRegister = ServerConstants.getAutoReg();
    private static boolean success = false;
    private static boolean mac = true;

    public static boolean isAutoRegister() {
        return autoRegister;
    }

    public static boolean isSuccess() {
        return success;
    }

    public static void reset() {
        AutoRegister.success = true;
        AutoRegister.mac = true;
    }

    public static boolean isMac() {
        return mac;
    }

    public static boolean getAccountExists(String login) {
        return new QDAccount().name.eq(login).exists();
    }

    public static void createAccount(String login, String pwd, String eip, String macs) {
        int macCount = findCountByMac(macs);
        if (macCount < ACCOUNTS_PER_MAC) {
            DAccount account = new DAccount(login, LoginCrypto.hexSha1(pwd));
            account.email = "autoregister@mail.com";
            account.birthday = LocalDate.now();
            account.mac = macs;
            account.sessionIP = "/" + eip.substring(1, eip.lastIndexOf(':'));
            account.save();
            success = true;
        }

        macCount = findCountByMac(macs);
        if (macCount >= ACCOUNTS_PER_MAC) {
            mac = false;
        }
    }

    private static int findCountByMac(String macs) {
        return new QDAccount().mac.eq(macs).findCount();
    }
}
