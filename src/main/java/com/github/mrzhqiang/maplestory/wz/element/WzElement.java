package com.github.mrzhqiang.maplestory.wz.element;

import com.google.common.base.Preconditions;
import org.jsoup.nodes.Element;

public abstract class WzElement {

    private final Element source;
    private final String name;

    protected WzElement(Element source) {
        Preconditions.checkNotNull(source, "source == null");
        this.source = source;
        this.name = source.attr("name");
    }

    public Element getSource() {
        return source;
    }

    public String getName() {
        return name;
    }

}
