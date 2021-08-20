package com.github.mrzhqiang.maplestory.wz.element;

import com.github.mrzhqiang.helper.math.Numbers;
import org.jsoup.nodes.Element;

import java.util.Map;
import java.util.stream.Collectors;

public final class ShortElement extends WzElement {

    private static final String SHORT_TAG = "short";

    public static Map<String, ShortElement> mapChildren(Element parent) {
        return parent.children().select(SHORT_TAG)
                .stream()
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
