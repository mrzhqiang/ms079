package com.github.mrzhqiang.maplestory.wz.element;

import com.github.mrzhqiang.helper.math.Numbers;
import org.jsoup.nodes.Element;

import java.util.Map;
import java.util.stream.Collectors;

public final class IntElement extends WzElement {

    private static final String INT_TAG = "int";

    public static Map<String, IntElement> mapChildren(Element parent) {
        return parent.children().select(INT_TAG)
                .stream()
                .map(IntElement::new)
                .collect(Collectors.toMap(WzElement::getName, it -> it));
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
