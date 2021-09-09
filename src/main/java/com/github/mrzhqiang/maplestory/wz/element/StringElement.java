package com.github.mrzhqiang.maplestory.wz.element;

import com.github.mrzhqiang.maplestory.wz.WzElement;
import org.jsoup.nodes.Element;

import javax.annotation.Nullable;

/**
 * 字符串标签元素。
 */
public final class StringElement extends BaseWzElement<String> {

    /**
     * 用于从 xml 元素匹配当前元素的标签。
     */
    static final String TAG = "string";

    StringElement(@Nullable WzElement<?> parent, Element source) {
        super(parent, source);
    }

    @Override
    protected String convertValue(Element source) {
        return source.val();
    }
}
