package com.github.mrzhqiang.maplestory.wz;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * wz 元素。
 * <p>
 * wz 元素由 标签 tag、名字 name、值 value、父元素 parent 和子元素 children 组成。
 * <p>
 * 其中，tag 由实现 WzElement<T> 接口的 T 类型决定。
 * <p>
 * name 可以是相同 tag 的不同实例。
 * <p>
 * value 则是可选的，即可能不存在任何值。
 * <p>
 * parent 不存在表示当前元素为顶级元素，否则属于子元素。
 * <p>
 * children 表示当前元素拥有子元素列表，安全起见，此接口方法不返回 null 值。
 */
public interface WzElement<T> {

    String BACK_PARENT_PATH = "..";

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
    @Nullable
    T value();

    /**
     * 父元素。
     * <p>
     * 父元素为表示当前元素为顶级元素，即 imgdir 元素。
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
     * <p>
     * 同时支持使用 '../' 跳转到父路径，表示查找当前目录上一级目录的子元素。
     *
     * @param name 子元素的名字。
     * @param <E>  子元素对应的实现类型。
     * @return 可选的子元素实例。
     */
    @SuppressWarnings("unchecked")
    default <E extends WzElement<?>> Optional<E> findByName(String name) {
        return Optional.ofNullable(name).map(it -> {
            // 一般 / 分隔符与 .. 一起使用
            if (CharMatcher.is('/').matchesAnyOf(it)) {
                // 从当前元素开始逐级查找
                WzElement<?> currentData = this;
                // /../path/to >>> [..,path,to]
                // omitEmptyStrings 可以忽略 / 是第一个字符导致出现 "" 字符串的情况
                for (String path : Splitter.on('/').omitEmptyStrings().split(it)) {
                    // 不存在的元素，返回 null 值，不会导致 NPE 异常
                    if (currentData == null) {
                        return null;
                    }
                    // 如果发现解析的路径是 .. 符号，则返回父元素
                    if (BACK_PARENT_PATH.equals(path)) {
                        // 这里进行路径跳转，而不是终止循环
                        currentData = currentData.parent().orElse(null);
                        continue;
                    }
                    // 逐级寻找子元素
                    currentData = currentData.find(path);
                }
                return (E) currentData;
            }
            return ((E) find(it));
        });
    }
}
