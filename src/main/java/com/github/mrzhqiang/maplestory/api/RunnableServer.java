package com.github.mrzhqiang.maplestory.api;

import java.io.Closeable;
import java.io.IOException;

/**
 * 可运行的服务器。
 */
public interface RunnableServer extends Runnable, Closeable {

    /**
     * 初始化。
     * <p>
     * 前置操作，比如加载初始数据。
     */
    void init();

    /**
     * 启动、运行。
     * <p>
     * 开始连接 IO 和 DB 等等资源。
     */
    @Override
    void run();

    /**
     * 关闭。
     * <p>
     * 即关闭 IO 和 DB 相关的资源。
     */
    @Override
    void close() throws IOException;
}
