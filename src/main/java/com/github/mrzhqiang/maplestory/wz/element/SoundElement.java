package com.github.mrzhqiang.maplestory.wz.element;

import com.github.mrzhqiang.maplestory.wz.WzElement;
import org.jsoup.nodes.Element;

import javax.annotation.Nullable;

public final class SoundElement extends BaseWzElement<Void> {

    public static final String TAG = "sound";

    SoundElement(@Nullable WzElement<?> parent, Element source) {
        super(parent, source);
    }
}
