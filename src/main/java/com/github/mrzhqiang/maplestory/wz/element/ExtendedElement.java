package com.github.mrzhqiang.maplestory.wz.element;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public final class ExtendedElement extends WzElement {

    private static final String EXTENDED_TAG = "extended";

    public static Map<String, ExtendedElement> mapChildren(Element parent) {
        Elements elements = parent.children();
        if (elements.isEmpty()) {
            return Collections.emptyMap();
        }
        return elements.stream()
                .filter(element -> element.is(EXTENDED_TAG))
                .map(ExtendedElement::new)
                .collect(Collectors.toMap(WzElement::getName, it -> it));
    }

    private final Map<String, VectorElement> vectorMap;

    ExtendedElement(Element source) {
        super(source);
        this.vectorMap = VectorElement.mapChildren(source);
    }

    public Optional<VectorElement> findVector(String name) {
        return Optional.ofNullable(vectorMap.get(name));
    }
}
