package com.github.mrzhqiang.maplestory.wz.element;

import org.jsoup.nodes.Element;

import java.util.Map;
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
        return parent.children().select(IMGDIR_TAG)
                .stream()
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
    }

    public ImgDirElement findImgDir(String name) {
        return imgDirMap.get(name);
    }

    public NullElement findNull(String name) {
        return nullMap.get(name);
    }

    public IntElement findInt(String name) {
        return intMap.get(name);
    }

    public StringElement findString(String name) {
        return stringMap.get(name);
    }

    public CanvasElement findCanvas(String name) {
        return canvasMap.get(name);
    }

    public VectorElement findVector(String name) {
        return vectorMap.get(name);
    }

    public UolElement findUol(String name) {
        return uolMap.get(name);
    }

    public ShortElement findShort(String name) {
        return shortMap.get(name);
    }
}
