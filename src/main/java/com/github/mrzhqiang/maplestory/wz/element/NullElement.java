package com.github.mrzhqiang.maplestory.wz.element;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public final class NullElement extends WzElement {

    private static final String NULL_TAG = "null";

    public static Map<String, NullElement> mapChildren(Element parent) {
        Elements elements = parent.children();
        if (elements.isEmpty()) {
            return Collections.emptyMap();
        }
        return elements.stream()
                .filter(element -> element.is(NULL_TAG))
                .map(NullElement::new)
                .collect(Collectors.toMap(WzElement::getName, it -> it));
    }

    NullElement(Element source) {
        super(source);
    }
}
