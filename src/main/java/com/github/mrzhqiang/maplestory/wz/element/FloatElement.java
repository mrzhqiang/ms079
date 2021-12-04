package com.github.mrzhqiang.maplestory.wz.element;

import com.github.mrzhqiang.maplestory.util.Numbers;
import com.github.mrzhqiang.maplestory.wz.WzElement;
import org.jsoup.nodes.Element;

import javax.annotation.Nullable;

/**
 * 单精度浮点数标签元素。
 */
public final class FloatElement extends BaseWzElement<Float> {

    /**
     * 用于从 xml 元素匹配当前元素的标签。
     */
    static final String TAG = "float";

    FloatElement(@Nullable WzElement<?> parent, Element source) {
        super(parent, source);
    }

    @Override
    protected Float convertValue(Element source) {
        return Numbers.ofFloat(source.val());
    }
}
