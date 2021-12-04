package com.github.mrzhqiang.maplestory.wz.element;

import com.github.mrzhqiang.maplestory.wz.WzElement;
import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 抽象的基础 wz 元素。
 */
public abstract class BaseWzElement<T> implements WzElement<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseWzElement.class);

    /**
     * 名字属性的键。
     */
    public static final String NAME_KEY = "name";

    /**
     * 元素名字。
     */
    private final String name;
    /**
     * 元素值。
     */
    private final T value;

    /**
     * 父元素。
     */
    private final WzElement<?> parent;

    /**
     * 子元素列表。
     */
    private final Map<String, WzElement<?>> children;
    /**
     * 相同子元素列表。
     */
    private final List<WzElement<?>> sameChildren = Lists.newArrayList();

    /**
     * 从 Jsoup 元素生成基础的 wz 元素。
     *
     * @param source Jsoup 元素，不能为 Null，否则将抛出 NPE 异常。
     */
    protected BaseWzElement(@Nullable WzElement<?> parent, Element source) {
        Preconditions.checkNotNull(source, "source == null");
        this.name = Strings.nullToEmpty(source.attr(NAME_KEY));
        this.value = convertValue(source);
        this.parent = parent;
        this.children = childrenOf(source, this::matching, this::convertChildren);
    }

    /**
     * 从 Jsoup 元素获得当前元素的值。
     * <p>
     * 有值的元素类需要重写这个方法。
     *
     * @param source Jsoup 元素。
     * @return 当前元素的值。默认返回 Null 值，表示不存在值。
     */
    protected T convertValue(Element source) {
        return null;
    }

    /**
     * 筛选子元素。
     * <p>
     * 默认是匹配所有标签的 Jsoup 元素，即任何元素都可以作为子元素。
     *
     * @param element 需要测试是否匹配的 Jsoup 元素。
     * @return true 留下；false 丢弃。
     */
    protected boolean matching(Element element) {
        return true;
    }

    /**
     * 将 Jsoup 元素转换为当前元素的子元素。
     * <p>
     * 有子元素的元素类需要重写这个方法。
     *
     * @param element Jsoup 元素。
     * @return 当前元素的子元素。默认返回 Null 值，表示不存在子元素。
     */
    protected WzElement<?> convertChildren(Element element) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("遗漏的元素：" + element.parent());
        }
        return null;
    }

    @Override
    public String name() {
        return name;
    }

    @Nullable
    @Override
    public T value() {
        return value;
    }

    @Override
    public Optional<WzElement<?>> parent() {
        return Optional.ofNullable(parent);
    }

    @Override
    public Collection<WzElement<?>> children() {
        List<WzElement<?>> children = Lists.newArrayList(this.children.values());
        children.addAll(sameChildren);
        return children;
    }

    @Nullable
    @Override
    public WzElement<?> find(String name) {
        if (Strings.isNullOrEmpty(name)) {
            return null;
        }

        Preconditions.checkArgument(CharMatcher.is('/').matchesNoneOf(name),
                "查找子元素 %s 时，发现存在不支持的路径符号：/", name);
        Preconditions.checkArgument(CharMatcher.anyOf("..").matchesNoneOf(name),
                "查找子元素 %s 时，发现不支持的父元素符号：..", name);
        return children.get(name);
    }

    private Map<String, WzElement<?>> childrenOf(Element source,
                                                 Predicate<Element> filter,
                                                 Function<Element, WzElement<?>> mapper) {
        if (source == null || source.childNodeSize() == 0) {
            return Collections.emptyMap();
        }

        Elements elements = source.children();
        if (elements.isEmpty()) {
            return Collections.emptyMap();
        }

        return elements.stream()
                .filter(filter)
                .map(mapper)
                // 如果存在相同的 Key 名称，那么只允许 first value 值
                .collect(Collectors.toMap(WzElement::name, it -> it, this::mergeSameElement));
    }

    private WzElement<?> mergeSameElement(WzElement<?> first, WzElement<?> second) {
        sameChildren.add(second);
        return first;
    }
}
