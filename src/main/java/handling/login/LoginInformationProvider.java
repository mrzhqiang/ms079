package handling.login;

import com.github.mrzhqiang.maplestory.wz.WzData;
import com.github.mrzhqiang.maplestory.wz.WzElement;
import com.github.mrzhqiang.maplestory.wz.WzFile;
import com.github.mrzhqiang.maplestory.wz.element.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LoginInformationProvider {

    private final static LoginInformationProvider instance = new LoginInformationProvider();
    protected final List<String> ForbiddenName = new ArrayList<>();

    public static LoginInformationProvider getInstance() {
        return instance;
    }

    protected LoginInformationProvider() {
        WzData.ETC.directory().findFile("ForbiddenName.img")
                .map(WzFile::content)
                .map(WzElement::childrenStream)
                .map(stream -> stream.map(Elements::ofString).collect(Collectors.toList()))
                .ifPresent(ForbiddenName::addAll);
    }

    public final boolean isForbiddenName(final String in) {
        for (final String name : ForbiddenName) {
            if (in.contains(name)) {
                return true;
            }
        }
        return false;
    }
}
