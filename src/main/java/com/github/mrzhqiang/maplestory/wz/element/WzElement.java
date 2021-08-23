package com.github.mrzhqiang.maplestory.wz.element;

import com.google.common.base.Preconditions;
import org.jsoup.nodes.Element;

public abstract class WzElement {

    private static final String NAME_KEY = "name";

    private final String name;

    protected WzElement(Element source) {
        Preconditions.checkNotNull(source, "source == null");
        this.name = source.attr(NAME_KEY);
    }

    public String getName() {
        return name;
    }
}
