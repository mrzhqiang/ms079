package com.github.mrzhqiang.maplestory.wz.element;

import org.jsoup.nodes.Element;

import java.util.Map;
import java.util.stream.Collectors;

public final class NullElement extends WzElement {

    private static final String NULL_TAG = "null";

    public static Map<String, NullElement> mapChildren(Element parent) {
        return parent.children().select(NULL_TAG)
                .stream()
                .map(NullElement::new)
                .collect(Collectors.toMap(WzElement::getName, it -> it));
    }

    NullElement(Element source) {
        super(source);
    }
}
