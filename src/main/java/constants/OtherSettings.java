package constants;

import com.github.mrzhqiang.maplestory.config.ServerProperties;
import javax.inject.Inject;

public class OtherSettings {

    private final ServerProperties properties;

    @Inject
    public OtherSettings(ServerProperties properties) {
        this.properties = properties;
    }

    public String[] getItempb_id() {
        return properties.getMallDisabled();
    }

    public String[] getItemgy_id() {
        return properties.getGysj();
    }

    public String[] getItemjy_id() {
        return properties.getCashJy();
    }
}
