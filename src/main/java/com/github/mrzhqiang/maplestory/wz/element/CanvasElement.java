package com.github.mrzhqiang.maplestory.wz.element;

import com.github.mrzhqiang.helper.math.Numbers;
import org.jsoup.nodes.Element;

import java.util.Map;
import java.util.stream.Collectors;

public final class CanvasElement extends WzElement {

    private static final String CANVAS_TAG = "canvas";

    public static Map<String, CanvasElement> mapChildren(Element parent) {
        return parent.children().select(CANVAS_TAG)
                .stream()
                .map(CanvasElement::new)
                .collect(Collectors.toMap(WzElement::getName, it -> it));
    }

    private final int width;
    private final int height;
    private final Map<String, ImgDirElement> imgDirMap;
    private final Map<String, StringElement> stringMap;
    private final Map<String, VectorElement> vectorMap;
    private final Map<String, IntElement> intMap;

    CanvasElement(Element source) {
        super(source);
        this.width = Numbers.ofInt(source.attr("width"));
        this.height = Numbers.ofInt(source.attr("height"));
        this.vectorMap = VectorElement.mapChildren(source);
        this.imgDirMap = ImgDirElement.mapChildren(source);
        this.stringMap = StringElement.mapChildren(source);
        this.intMap = IntElement.mapChildren(source);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public ImgDirElement findImgDir(String name) {
        return imgDirMap.get(name);
    }

    public StringElement findString(String name) {
        return stringMap.get(name);
    }

    public VectorElement findVector(String name) {
        return vectorMap.get(name);
    }

    public IntElement findInt(String name) {
        return intMap.get(name);
    }
}
