package com.github.mrzhqiang.maplestory.wz.element;

import com.github.mrzhqiang.maplestory.wz.WzElement;
import org.jsoup.nodes.Element;

import javax.annotation.Nullable;

/**
 * 扩展标签元素。
 * <p>
 * 在 Map.wz 目录下，此扩展标签元素可能存在重复 name 的子元素。
 * <p>
 * 为此，我们在 BaseWzElement 中增加了 sameChildren 列表以保证所有子元素不被丢失。
 */
public final class ExtendedElement extends BaseWzElement<Void> {

    /**
     * 用于从 xml 元素匹配当前元素的标签。
     */
    static final String TAG = "extended";

    ExtendedElement(@Nullable WzElement<?> parent, Element source) {
        super(parent, source);
    }

    @Override
    protected WzElement<?> convertChildren(Element element) {
        if (VectorElement.TAG.equals(element.tagName())) {
            return new VectorElement(this, element);
        }
        return super.convertChildren(element);
    }
}
