package com.github.mrzhqiang.maplestory.di;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import handling.mina.MapleCodecFactory;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

/**
 * Mina 模块。
 */
final class MinaModule extends AbstractModule {

    static final MinaModule INSTANCE = new MinaModule();

    @Provides
    static NioSocketAcceptor socketAcceptor() {
        return new NioSocketAcceptor();
    }

    @Provides
    static ProtocolCodecFilter codecFilter(MapleCodecFactory factory) {
        return new ProtocolCodecFilter(factory);
    }
}
