package com.github.mrzhqiang.maplestory.wz.element;

import com.github.mrzhqiang.helper.math.Numbers;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public final class IntElement extends WzElement {

    private static final String INT_TAG = "int";

    public static Map<String, IntElement> mapChildren(Element parent) {
        Elements elements = parent.children().select(INT_TAG);
        if (elements.isEmpty()) {
            return Collections.emptyMap();
        }
        return elements.stream().map(IntElement::new).collect(Collectors.toMap(WzElement::getName, it -> it));
    }

    private final int value;

    IntElement(Element source) {
        super(source);
        this.value = Numbers.ofInt(source.val());
    }

    public int getValue() {
        return value;
    }
}
