package com.github.mrzhqiang.maplestory.wz.element;

import com.github.mrzhqiang.helper.math.Numbers;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public final class CanvasElement extends WzElement {

    private static final String CANVAS_TAG = "canvas";

    public static Map<String, CanvasElement> mapChildren(Element parent) {
        Elements elements = parent.children();
        if (elements.isEmpty()) {
            return Collections.emptyMap();
        }
        return elements.stream()
                .filter(element -> element.is(CANVAS_TAG))
                .map(CanvasElement::new)
                .collect(Collectors.toMap(WzElement::getName, it -> it));
    }

    private final int width;
    private final int height;
    private final Map<String, ImgDirElement> imgDirMap;
    private final Map<String, StringElement> stringMap;
    private final Map<String, VectorElement> vectorMap;
    private final Map<String, IntElement> intMap;
    private final Map<String, ExtendedElement> extendedMap;

    CanvasElement(Element source) {
        super(source);
        this.width = Numbers.ofInt(source.attr("width"));
        this.height = Numbers.ofInt(source.attr("height"));
        this.vectorMap = VectorElement.mapChildren(source);
        this.imgDirMap = ImgDirElement.mapChildren(source);
        this.stringMap = StringElement.mapChildren(source);
        this.intMap = IntElement.mapChildren(source);
        this.extendedMap = ExtendedElement.mapChildren(source);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Optional<ImgDirElement> findImgDir(String name) {
        return Optional.ofNullable(imgDirMap.get(name));
    }

    public Optional<StringElement> findString(String name) {
        return Optional.ofNullable(stringMap.get(name));
    }

    public Optional<VectorElement> findVector(String name) {
        return Optional.ofNullable(vectorMap.get(name));
    }

    public Optional<IntElement> findInt(String name) {
        return Optional.ofNullable(intMap.get(name));
    }

    public Optional<ExtendedElement> findExtended(String name) {
        return Optional.ofNullable(extendedMap.get(name));
    }
}
