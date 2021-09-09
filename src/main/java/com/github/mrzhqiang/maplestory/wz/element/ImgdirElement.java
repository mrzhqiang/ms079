package com.github.mrzhqiang.maplestory.wz.element;

import com.github.mrzhqiang.maplestory.wz.WzElement;
import org.jsoup.nodes.Element;

/**
 * imgdir 标签元素。
 * <p>
 * 通常来说，在 xml 文件中，第一个元素是 imgdir 标签，它是一个顶级元素。顶级元素在每个 xml 文件中，有且仅有一个。
 * <p>
 * 大概内容如下：
 * <pre>
 * &lt;imgdir name=&quot;Cash.img&quot;&gt;
 *     &lt;imgdir name=&quot;1112223&quot;&gt;
 *         &lt;string name=&quot;name&quot; value=&quot;海滩聊天戒指&quot;/&gt;
 *         &lt;string name=&quot;desc&quot; value=&quot;角色聊天时聊天窗变成海边装饰的样子．&quot;/&gt;
 *     &lt;/imgdir&gt;
 * &lt;/imgdir&gt;
 * </pre>
 */
public final class ImgdirElement extends BaseWzElement<Void> implements WzElement<Void> {

    /**
     * 用于从 xml 元素匹配当前元素的标签。
     */
    static final String TAG = "imgdir";

    /**
     * Imgdir 元素的工厂方法，这是 wz 元素类的入口，表示生成的类是顶级元素。
     *
     * @param source Jsoup 元素。
     * @return Imgdir 元素实例。
     */
    public static ImgdirElement of(Element source) {
        return new ImgdirElement(source);
    }

    ImgdirElement(Element source) {
        super(null, source);
    }

    ImgdirElement(WzElement<?> parent, Element source) {
        super(parent, source);
    }

    @Override
    protected WzElement<?> convertChildren(Element element) {
        switch (element.tagName()) {
            case ImgdirElement.TAG:
                return new ImgdirElement(this, element);
            case NullElement.TAG:
                return new NullElement(this, element);
            case IntElement.TAG:
                return new IntElement(this, element);
            case StringElement.TAG:
                return new StringElement(this, element);
            case CanvasElement.TAG:
                return new CanvasElement(this, element);
            case VectorElement.TAG:
                return new VectorElement(this, element);
            case UolElement.TAG:
                return new UolElement(this, element);
            case ShortElement.TAG:
                return new ShortElement(this, element);
            case FloatElement.TAG:
                return new FloatElement(this, element);
            case DoubleElement.TAG:
                return new DoubleElement(this, element);
            case ExtendedElement.TAG:
                return new ExtendedElement(this, element);
            case SoundElement.TAG:
                return new SoundElement(this, element);
        }
        return super.convertChildren(element);
    }
}
