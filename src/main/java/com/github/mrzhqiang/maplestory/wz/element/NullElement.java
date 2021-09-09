package com.github.mrzhqiang.maplestory.wz.element;

import com.github.mrzhqiang.maplestory.wz.WzElement;
import org.jsoup.nodes.Element;

import javax.annotation.Nullable;

/**
 * 空标签元素。
 */
public final class NullElement extends BaseWzElement<Void> {

    /**
     * 用于从 xml 元素匹配当前元素的标签。
     */
    static final String TAG = "null";

    NullElement(@Nullable WzElement<?> parent, Element source) {
        super(parent, source);
    }
}
