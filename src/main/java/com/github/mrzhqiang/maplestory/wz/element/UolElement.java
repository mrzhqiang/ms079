package com.github.mrzhqiang.maplestory.wz.element;

import com.github.mrzhqiang.maplestory.wz.WzElement;
import org.jsoup.nodes.Element;

import javax.annotation.Nullable;

/**
 * UOL 标签元素。
 */
public final class UolElement extends BaseWzElement<String> {

    /**
     * 用于从 xml 元素匹配当前元素的标签。
     */
    static final String TAG = "uol";

    UolElement(@Nullable WzElement<?> parent, Element source) {
        super(parent, source);
    }

    @Override
    protected String convertValue(Element source) {
        return source.val();
    }
}
