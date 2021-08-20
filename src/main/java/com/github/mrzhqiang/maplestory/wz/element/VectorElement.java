package com.github.mrzhqiang.maplestory.wz.element;

import com.github.mrzhqiang.helper.math.Numbers;
import org.jsoup.nodes.Element;

import java.util.Map;
import java.util.stream.Collectors;

public final class VectorElement extends WzElement {

    private static final String VECTOR_TAG = "vector";

    public static Map<String, VectorElement> mapChildren(Element parent) {
        return parent.children().select(VECTOR_TAG)
                .stream()
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
