package com.github.mrzhqiang.maplestory.wz.element;

import com.github.mrzhqiang.helper.math.Numbers;
import com.github.mrzhqiang.maplestory.wz.WzElement;
import org.jsoup.nodes.Element;

import javax.annotation.Nullable;

/**
 * 双精度浮点数标签元素。
 */
public final class DoubleElement extends BaseWzElement<Double> {

    /**
     * 用于从 xml 元素匹配当前元素的标签。
     */
    static final String TAG = "double";

    DoubleElement(@Nullable WzElement<?> parent, Element source) {
        super(parent, source);
    }

    @Override
    protected Double convertValue(Element source) {
        return Numbers.ofDouble(source.val());
    }
}
