package handling.login;

import com.github.mrzhqiang.maplestory.wz.WzData;
import com.github.mrzhqiang.maplestory.wz.WzElement;
import com.github.mrzhqiang.maplestory.wz.WzFile;
import com.github.mrzhqiang.maplestory.wz.element.Elements;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.stream.Collectors;

public class LoginInformationProvider {

    private static final LoginInformationProvider instance = new LoginInformationProvider();

    private final List<String> ForbiddenName = Lists.newArrayList();

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void init() {
        getInstance();
    }

    public static LoginInformationProvider getInstance() {
        return instance;
    }

    private LoginInformationProvider() {
        WzData.ETC.directory().findFile("ForbiddenName.img")
                .map(WzFile::content)
                .map(WzElement::childrenStream)
                .map(stream -> stream.map(Elements::ofString).collect(Collectors.toList()))
                .ifPresent(ForbiddenName::addAll);
    }

    public boolean isForbiddenName(String in) {
        for (String name : ForbiddenName) {
            if (in.contains(name)) {
                return true;
            }
        }
        return false;
    }
}
