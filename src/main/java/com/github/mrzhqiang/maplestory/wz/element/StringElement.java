package com.github.mrzhqiang.maplestory.wz.element;

import org.jsoup.nodes.Element;

import java.util.Map;
import java.util.stream.Collectors;

public final class StringElement extends WzElement {

    private static final String STRING_TAG = "string";

    public static Map<String, StringElement> mapChildren(Element parent) {
        return parent.children().select(STRING_TAG)
                .stream()
                .map(StringElement::new)
                .collect(Collectors.toMap(WzElement::getName, it -> it));
    }

    private final String value;

    StringElement(Element source) {
        super(source);
        this.value = source.val();
    }

    public String getValue() {
        return value;
    }
}
