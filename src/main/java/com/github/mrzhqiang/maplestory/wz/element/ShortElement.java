package com.github.mrzhqiang.maplestory.wz.element;

import com.github.mrzhqiang.helper.math.Numbers;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public final class ShortElement extends WzElement {

    private static final String SHORT_TAG = "short";

    public static Map<String, ShortElement> mapChildren(Element parent) {
        Elements elements = parent.children();
        if (elements.isEmpty()) {
            return Collections.emptyMap();
        }
        return elements.stream()
                .filter(element -> element.is(SHORT_TAG))
                .map(ShortElement::new)
                .collect(Collectors.toMap(WzElement::getName, it -> it));
    }

    private final short value;

    ShortElement(Element source) {
        super(source);
        this.value = Numbers.ofShort(source.val());
    }

    public short getValue() {
        return value;
    }
}
