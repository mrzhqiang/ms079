package com.github.mrzhqiang.maplestory.wz.element;

import com.github.mrzhqiang.maplestory.util.Numbers;
import com.github.mrzhqiang.maplestory.wz.WzElement;
import com.github.mrzhqiang.maplestory.wz.element.data.Canvas;
import org.jsoup.nodes.Element;

import javax.annotation.Nullable;

/**
 * 画布元素。
 */
public final class CanvasElement extends BaseWzElement<Canvas> {

    /**
     * 用于从 xml 元素匹配当前元素的标签。
     */
    static final String TAG = "canvas";

    CanvasElement(@Nullable WzElement<?> parent, Element source) {
        super(parent, source);
    }

    @Override
    public Canvas convertValue(Element source) {
        return Canvas.of(source);
    }

    @Override
    protected WzElement<?> convertChildren(Element element) {
        switch (element.tagName()) {
            case CanvasElement.TAG:
                return new CanvasElement(this, element);
            case ImgdirElement.TAG:
                return new ImgdirElement(this, element);
            case StringElement.TAG:
                return new StringElement(this, element);
            case VectorElement.TAG:
                return new VectorElement(this, element);
            case IntElement.TAG:
                return new IntElement(this, element);
            case ShortElement.TAG:
                return new ShortElement(this, element);
            case ExtendedElement.TAG:
                return new ExtendedElement(this, element);
            case UolElement.TAG:
                return new UolElement(this, element);
        }
        return super.convertChildren(element);
    }
}
