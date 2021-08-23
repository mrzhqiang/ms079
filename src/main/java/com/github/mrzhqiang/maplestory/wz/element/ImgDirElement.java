package com.github.mrzhqiang.maplestory.wz.element;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public final class ImgDirElement extends WzElement {

    private static final String IMGDIR_TAG = "imgdir";

    /*
     * <imgdir name="01010000.img">
     *     <imgdir name="info">
     *         <canvas name="icon" width="26" height="28">
     *             <vector name="origin" x="-4" y="28"/>
     *         </canvas>
     *         <canvas name="iconRaw" width="24" height="23">
     *             <vector name="origin" x="-4" y="28"/>
     *         </canvas>
     *     </imgdir>
     * </imgdir>
     * */
    public static ImgDirElement of(Element source) {
        return new ImgDirElement(source);
    }

    public static Map<String, ImgDirElement> mapChildren(Element parent) {
        Elements elements = parent.children();
        if (elements.isEmpty()) {
            return Collections.emptyMap();
        }
        return elements.stream()
                .filter(element -> element.is(IMGDIR_TAG))
                .map(ImgDirElement::new)
                .collect(Collectors.toMap(WzElement::getName, it -> it));
    }

    private final Map<String, ImgDirElement> imgDirMap;
    private final Map<String, NullElement> nullMap;
    private final Map<String, IntElement> intMap;
    private final Map<String, StringElement> stringMap;
    private final Map<String, CanvasElement> canvasMap;
    private final Map<String, VectorElement> vectorMap;
    private final Map<String, UolElement> uolMap;
    private final Map<String, ShortElement> shortMap;
    private final Map<String, FloatElement> floatMap;
    private final Map<String, ExtendedElement> extendedMap;

    ImgDirElement(Element source) {
        super(source);
        this.nullMap = NullElement.mapChildren(source);
        this.imgDirMap = ImgDirElement.mapChildren(source);
        this.intMap = IntElement.mapChildren(source);
        this.stringMap = StringElement.mapChildren(source);
        this.canvasMap = CanvasElement.mapChildren(source);
        this.vectorMap = VectorElement.mapChildren(source);
        this.uolMap = UolElement.mapChildren(source);
        this.shortMap = ShortElement.mapChildren(source);
        this.floatMap = FloatElement.mapChildren(source);
        this.extendedMap = ExtendedElement.mapChildren(source);
    }

    public Optional<ImgDirElement> findImgDir(String name) {
        return Optional.ofNullable(imgDirMap.get(name));
    }

    public Optional<NullElement> findNull(String name) {
        return Optional.ofNullable(nullMap.get(name));
    }

    public Optional<IntElement> findInt(String name) {
        return Optional.ofNullable(intMap.get(name));
    }

    public Optional<StringElement> findString(String name) {
        return Optional.ofNullable(stringMap.get(name));
    }

    public Optional<CanvasElement> findCanvas(String name) {
        return Optional.ofNullable(canvasMap.get(name));
    }

    public Optional<VectorElement> findVector(String name) {
        return Optional.ofNullable(vectorMap.get(name));
    }

    public Optional<UolElement> findUol(String name) {
        return Optional.ofNullable(uolMap.get(name));
    }

    public Optional<ShortElement> findShort(String name) {
        return Optional.ofNullable(shortMap.get(name));
    }

    public Optional<FloatElement> findFloat(String name) {
        return Optional.ofNullable(floatMap.get(name));
    }

    public Optional<ExtendedElement> findExtended(String name) {
        return Optional.ofNullable(extendedMap.get(name));
    }
}
