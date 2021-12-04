package client;

import com.github.mrzhqiang.maplestory.domain.DAccount;
import com.github.mrzhqiang.maplestory.domain.DCharacter;
import com.github.mrzhqiang.maplestory.domain.DGamePollReply;
import com.github.mrzhqiang.maplestory.domain.DNote;
import com.github.mrzhqiang.maplestory.domain.DNxCode;
import com.github.mrzhqiang.maplestory.domain.query.QDAccount;
import com.github.mrzhqiang.maplestory.domain.query.QDCharacter;
import com.github.mrzhqiang.maplestory.domain.query.QDGamePollReply;
import com.github.mrzhqiang.maplestory.domain.query.QDNxCode;
import constants.GameConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.Pair;

import java.sql.SQLException;
import java.util.regex.Pattern;

public class MapleCharacterUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapleCharacterUtil.class);

    private static final Pattern namePattern = Pattern.compile("[a-zA-Z0-9_-]{3,12}");
    private static final Pattern petPattern = Pattern.compile("[a-zA-Z0-9_-]{4,12}");

    public static boolean canCreateChar(String name) {
        return getIdByName(name) == -1 && isEligibleCharName(name);
    }

    public static boolean isEligibleCharName(String name) {
        if (name.length() > 15) {
            return false;
        }
        if (name.length() < 2) {
            return false;
        }
        for (String z : GameConstants.RESERVED) {
            if (name.contains(z)) {
                return false;
            }
        }
        return true;
    }

    public static final boolean canChangePetName(final String name) {
        if (petPattern.matcher(name).matches()) {
            for (String z : GameConstants.RESERVED) {
                if (name.indexOf(z) != -1) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static final String makeMapleReadable(final String in) {
        String wui = in.replace('I', 'i');
        wui = wui.replace('l', 'L');
        wui = wui.replace("rn", "Rn");
        wui = wui.replace("vv", "Vv");
        wui = wui.replace("VV", "Vv");
        return wui;
    }

    public static int getIdByName(String name) {
        DCharacter one = new QDCharacter().name.eq(name).findOne();
        if (one == null) {
            return -1;
        }

        return one.getId();
    }

    public static boolean PromptPoll(int accountid) {
        DGamePollReply one = new QDGamePollReply().account.id.eq(accountid).findOne();
        return one == null;
    }

    public static boolean SetPoll(int accountid, int selection) {
        if (!PromptPoll(accountid)) { // Hacking OR spamming the db.
            return false;
        }

        DGamePollReply reply = new DGamePollReply();
        reply.setAccount(new QDAccount().id.eq(accountid).findOne());
        reply.setSelectAns(selection);
        reply.save();

        return true;
    }

    // -2 = 出现未知错误
    // -1 = 在数据库中找不到帐户
    // 0 = 您当前没有设置第二个密码。
    // 1 = 您输入的密码错误
    // 2 = 密码修改成功
    public static int Change_SecondPassword(int accid, String password, String newpassword) {
        DAccount one = new QDAccount().id.eq(accid).findOne();
        if (one == null) {
            return -1;
        }

        if (one.getSecondPassword() == null && one.getSecondSalt() == null) {
            return 0;
        }

        if (one.getSecondPassword() != null && one.getSecondSalt() != null) {
            one.setSecondPassword(LoginCrypto.rand_r(one.getSecondPassword()));
        }

        if (!check_ifPasswordEquals(one.getSecondPassword(), password, one.getSecondSalt())) {
            return 1;
        }

        String SHA1hashedsecond;
        try {
            SHA1hashedsecond = LoginCryptoLegacy.encodeSHA1(newpassword);
        } catch (Exception e) {
            return -2;
        }
        one.setSecondPassword(SHA1hashedsecond);
        one.setSecondSalt(null);
        one.save();
        return 2;
    }

    private static boolean check_ifPasswordEquals(String passhash, String pwd, String salt) {
        // 在这里检查密码是否正确。 :B
        if (LoginCryptoLegacy.isLegacyPassword(passhash) && LoginCryptoLegacy.checkPassword(pwd, passhash)) {
            // 检查是否需要密码升级。
            return true;
        } else if (salt == null && LoginCrypto.checkSha1Hash(passhash, pwd)) {
            return true;
        } else return LoginCrypto.checkSaltedSha512Hash(passhash, pwd, salt);
    }

    //id accountid gender
    public static Pair<Integer, Pair<Integer, Integer>> getInfoByName(String name, int world) {
        DCharacter one = new QDCharacter().name.eq(name).world.eq(world).findOne();
        if (one == null) {
            return null;
        }
        return new Pair<>(one.getId(), new Pair<>(one.getAccount().getId(), one.getGender().getCodeInt()));
    }

    public static void setNXCodeUsed(String name, String code) {
        new QDNxCode().code.eq(code).asUpdate().set("user", name).update();
    }

    public static void sendNote(String to, String name, String msg, int fame) {
        DNote note = new DNote();
        note.setTo(to);
        note.setFrom(name);
        note.setMessage(msg);
        note.setTimestamp(System.currentTimeMillis());
        note.setGift(fame);
        note.save();
    }

    public static boolean getNXCodeValid(String code, boolean validcode) throws SQLException {
        return new QDNxCode().code.eq(code).findOneOrEmpty().map(it -> it.getValid() > 0).orElse(validcode);
    }

    public static int getNXCodeType(String code) throws SQLException {
        return new QDNxCode().code.eq(code).findOneOrEmpty().map(DNxCode::getType).orElse(-1);
    }

    public static int getNXCodeItem(String code) throws SQLException {
        return new QDNxCode().code.eq(code).findOneOrEmpty().map(DNxCode::getItem).orElse(-1);
    }
}
