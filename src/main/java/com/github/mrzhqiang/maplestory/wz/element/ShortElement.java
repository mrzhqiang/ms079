package com.github.mrzhqiang.maplestory.wz.element;

import com.github.mrzhqiang.helper.math.Numbers;
import com.github.mrzhqiang.maplestory.wz.WzElement;
import org.jsoup.nodes.Element;

import javax.annotation.Nullable;

/**
 * 短整型标签元素。
 */
public final class ShortElement extends BaseWzElement<Short> {

    /**
     * 用于从 xml 元素匹配当前元素的标签。
     */
    static final String TAG = "short";

    ShortElement(@Nullable WzElement<?> parent, Element source) {
        super(parent, source);
    }

    @Override
    protected Short convertValue(Element source) {
        return Numbers.ofShort(source.val());
    }
}
