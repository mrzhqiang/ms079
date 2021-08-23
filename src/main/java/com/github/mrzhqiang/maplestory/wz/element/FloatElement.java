package com.github.mrzhqiang.maplestory.wz.element;

import com.github.mrzhqiang.helper.math.Numbers;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public final class FloatElement extends WzElement {

    private static final String FLOAT_TAG = "float";

    public static Map<String, FloatElement> mapChildren(Element parent) {
        Elements elements = parent.children();
        if (elements.isEmpty()) {
            return Collections.emptyMap();
        }
        return elements.stream()
                .filter(element -> element.is(FLOAT_TAG))
                .map(FloatElement::new)
                .collect(Collectors.toMap(WzElement::getName, it -> it));
    }

    private final float value;

    FloatElement(Element source) {
        super(source);
        this.value = Numbers.ofFloat(source.val());
    }

    public float getValue() {
        return value;
    }
}
