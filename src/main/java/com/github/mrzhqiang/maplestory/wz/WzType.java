package com.github.mrzhqiang.maplestory.wz;

import com.google.common.base.Preconditions;
import org.jsoup.nodes.Element;

import java.util.Map;
import java.util.stream.Collectors;

public enum WzType {
    IMG_DIR,
    NULL,
    STRING,
    INT,
    ;

    public static abstract class Tag {

        private final Element source;
        private final String name;

        protected Tag(Element source) {
            Preconditions.checkNotNull(source, "source == null");
            this.source = source;
            this.name = source.attr("name");
        }

        public Element getSource() {
            return source;
        }

        public String getName() {
            return name;
        }
    }

    public static class ImgDirTag extends Tag {

        private final Map<String, ImgDirTag> imgDirMap;
        private final Map<String, IntTag> intMap;
        private final Map<String, StringTag> stringMap;
        private final Map<String, CanvasTag> canvasMap;
        private final Map<String, VectorTag> vectorMap;

        public ImgDirTag(Element source) {
            super(source);
            this.imgDirMap = source.children().select("imgdir")
                    .stream()
                    .map(ImgDirTag::new)
                    .collect(Collectors.toMap(Tag::getName, it -> it));
            this.intMap = source.children().select("int")
                    .stream()
                    .map(IntTag::new)
                    .collect(Collectors.toMap(Tag::getName, it -> it));
            this.stringMap = source.children().select("string")
                    .stream()
                    .map(StringTag::new)
                    .collect(Collectors.toMap(Tag::getName, it -> it));
            this.canvasMap = source.children().select("canvas")
                    .stream()
                    .map(CanvasTag::new)
                    .collect(Collectors.toMap(Tag::getName, it -> it));
            this.vectorMap = source.children().select("vector")
                    .stream()
                    .map(VectorTag::new)
                    .collect(Collectors.toMap(Tag::getName, it -> it));
        }

        public ImgDirTag findImgDir(String name) {
            return imgDirMap.get(name);
        }

        public IntTag findInt(String name) {
            return intMap.get(name);
        }

        public StringTag findString(String name) {
            return stringMap.get(name);
        }

        public CanvasTag findCanvas(String name) {
            return canvasMap.get(name);
        }

        public VectorTag findVector(String name) {
            return vectorMap.get(name);
        }
    }

    public static class NullTag extends Tag {

        public NullTag(Element source) {
            super(source);
        }
    }

    public static class IntTag extends Tag {

        private final int value;

        public IntTag(Element source) {
            super(source);
            this.value = Integer.parseInt(source.val());
        }

        public int getValue() {
            return value;
        }
    }

    public static class StringTag extends Tag {

        private final String value;

        protected StringTag(Element source) {
            super(source);
            this.value = source.val();
        }

        public String getValue() {
            return value;
        }
    }

    public static class CanvasTag extends Tag {

        private final int width;
        private final int height;
        private final Map<String, VectorTag> vectorMap;
        private final Map<String, ImgDirTag> imgDirMap;
        private final Map<String, StringTag> stringMap;

        public CanvasTag(Element source) {
            super(source);
            this.width = Integer.parseInt(source.attr("width"));
            this.height = Integer.parseInt(source.attr("height"));
            this.vectorMap = source.children().select("vector")
                    .stream()
                    .map(VectorTag::new)
                    .collect(Collectors.toMap(Tag::getName, it -> it));
            this.imgDirMap = source.children().select("imgdir")
                    .stream()
                    .map(ImgDirTag::new)
                    .collect(Collectors.toMap(Tag::getName, it -> it));
            this.stringMap = source.children().select("string")
                    .stream()
                    .map(StringTag::new)
                    .collect(Collectors.toMap(Tag::getName, it -> it));
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public VectorTag findVector(String name) {
            return vectorMap.get(name);
        }

        public ImgDirTag findImgDir(String name) {
            return imgDirMap.get(name);
        }

        public StringTag findString(String name) {
            return stringMap.get(name);
        }
    }

    public static class VectorTag extends Tag {

        private final int x;
        private final int y;

        public VectorTag(Element source) {
            super(source);
            this.x = Integer.parseInt(source.attr("x"));
            this.y = Integer.parseInt(source.attr("y"));
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

}
