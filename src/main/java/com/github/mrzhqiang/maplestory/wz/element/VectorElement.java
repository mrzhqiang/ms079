package com.github.mrzhqiang.maplestory.wz.element;

import com.github.mrzhqiang.maplestory.util.Numbers;
import com.github.mrzhqiang.maplestory.wz.WzElement;
import com.github.mrzhqiang.maplestory.wz.element.data.Vector;
import org.jsoup.nodes.Element;

import javax.annotation.Nullable;

/**
 * 矢量标签元素。
 */
public final class VectorElement extends BaseWzElement<Vector> {

    /**
     * 用于从 xml 元素匹配当前元素的标签。
     */
    static final String TAG = "vector";

    private static final String X_KEY = "x";
    private static final String Y_KEY = "y";

    VectorElement(@Nullable WzElement<?> parent, Element source) {
        super(parent, source);
    }

    @Override
    protected Vector convertValue(Element source) {
        int x = Numbers.ofInt(source.attr(X_KEY));
        int y = Numbers.ofInt(source.attr(Y_KEY));
        return Vector.of(x, y);
    }
}
