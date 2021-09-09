package com.github.mrzhqiang.maplestory.wz;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * wz 元素。
 * <p>
 * wz 元素由 标签 tag、名字 name、值 value、子元素 children 组成。
 */
public interface WzElement<T> {

    /**
     * 名字。
     * <p>
     * 格式：&lt;tag name=&quot;名字&quot;&gt;&lt;/tag&gt;
     */
    String name();

    /**
     * 值。
     * <p>
     * 格式：&lt;tag value=&quot;值&quot;&gt;&lt;/tag&gt;
     */
    T value();

    /**
     * 父元素。
     * <p>
     * 不存在父元素表示当前元素为顶级元素，即 imgdir 元素。
     *
     * @return 可选的 wz 父元素。
     */
    default Optional<WzElement<?>> parent() {
        return Optional.empty();
    }

    /**
     * 子元素流。
     *
     * @return 子元素流。
     */
    default Stream<WzElement<?>> childrenStream() {
        return children().stream();
    }

    /**
     * 子元素集合。
     *
     * @return 子元素的值集合。
     */
    Collection<WzElement<?>> children();

    /**
     * 通过名字找到子元素。
     * <p>
     * 这个方法不支持 .. 和 / 操作符。
     *
     * @param name 元素名称。
     * @return 任意 wz 元素。可能返回 Null 值。
     */
    @Nullable
    WzElement<?> find(String name);

    /**
     * 通过名字找到子元素。
     * <p>
     * 支持使用 '/' 分割路径，表示查找目标元素的子元素。
     *
     * @param name 子元素的名字。
     * @param <E>  子元素对应的实现类型。
     * @return 可选的子元素实例。
     */
    @SuppressWarnings("unchecked")
    default <E extends WzElement<?>> Optional<E> findByName(String name) {
        return Optional.ofNullable(name).map(it -> {
            // 一般 / 分隔符与 .. 一起使用
            if (it.contains("/")) {
                String[] split = it.split("/");
                WzElement<?> data = this;
                for (String s : split) {
                    // 不存在的元素
                    if (data == null) {
                        return null;
                    }
                    // 返回父元素
                    if ("..".equals(s)) {
                        data = data.parent().orElse(null);
                        continue;
                    }
                    // 逐级寻找子元素
                    data = data.find(s);
                }
                return (E) data;
            }
            return ((E) find(it));
        });
    }
}
