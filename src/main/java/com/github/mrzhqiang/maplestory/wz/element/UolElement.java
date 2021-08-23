package com.github.mrzhqiang.maplestory.wz.element;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public final class UolElement extends WzElement {

    private static final String UOL_TAG = "uol";

    public static Map<String, UolElement> mapChildren(Element parent) {
        Elements elements = parent.children().select(UOL_TAG);
        if (elements.isEmpty()) {
            return Collections.emptyMap();
        }
        return elements.stream().map(UolElement::new).collect(Collectors.toMap(WzElement::getName, it -> it));
    }

    private final String value;

    UolElement(Element source) {
        super(source);
        this.value = source.val();
    }

    public String getValue() {
        return value;
    }
}
