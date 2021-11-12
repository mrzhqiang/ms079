package handling;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Properties;

public final class ExternalCodeTableGetter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalCodeTableGetter.class);

    private static final int DEFAULT_CODE_VALUE = -2;

    private final Properties props;

    private ExternalCodeTableGetter(Properties properties) {
        props = properties;
    }

    public static <T extends Enum<? extends WritableIntValueHolder> & WritableIntValueHolder> void populateValues(
            Properties properties, T[] values) {
        ExternalCodeTableGetter exc = new ExternalCodeTableGetter(properties);
        for (T code : values) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[external code table] populate values: name={}, value={}, hex-value: {}",
                        code.name(), code.getValue(), Integer.toHexString(code.getValue()));
            }
            code.setValue(exc.getValue(code.name(), values));
        }
    }

    private <T extends Enum<? extends WritableIntValueHolder> & WritableIntValueHolder> int getValue(String name, T[] values) {
        if (Strings.isNullOrEmpty(name)) {
            return DEFAULT_CODE_VALUE;
        }

        String prop = props.getProperty(name);
        if (Strings.isNullOrEmpty(prop)) {
            return DEFAULT_CODE_VALUE;
        }

        String trimmed = CharMatcher.whitespace().trimFrom(prop);
        if (trimmed.startsWith("0x")) {
            trimmed = trimmed.substring(2);
        } else {
            int base = 0;
            Iterator<String> iterator = Splitter.on(' ').trimResults().split(trimmed).iterator();
            if (iterator.hasNext()) {
                String next = iterator.next();

            }
            T t = valueOf(name, values);
            if (t != null) {
                return t.getValue();
            }
        }
        try {
            return Integer.parseInt(trimmed, 16);
        } catch (NumberFormatException ignored) {
            return DEFAULT_CODE_VALUE;
        }
    }

    private static <T extends Enum<? extends WritableIntValueHolder> & WritableIntValueHolder> T valueOf(String name, T[] values) {
        for (T val : values) {
            if (val.name().equals(name)) {
                return val;
            }
        }
        return null;
    }
}
