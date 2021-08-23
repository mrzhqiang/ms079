package com.github.mrzhqiang.maplestory.wz.element;

import com.github.mrzhqiang.helper.math.Numbers;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public final class VectorElement extends WzElement {

    private static final String VECTOR_TAG = "vector";

    public static Map<String, VectorElement> mapChildren(Element parent) {
        Elements elements = parent.children();
        if (elements.isEmpty()) {
            return Collections.emptyMap();
        }
        return elements.stream()
                .filter(element -> element.is(VECTOR_TAG))
                .map(VectorElement::new)
                .collect(Collectors.toMap(WzElement::getName, it -> it));
    }

    private final int x;
    private final int y;

    VectorElement(Element source) {
        super(source);
        this.x = Numbers.ofInt(source.attr("x"));
        this.y = Numbers.ofInt(source.attr("y"));
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
