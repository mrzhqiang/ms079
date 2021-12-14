package com.github.mrzhqiang.maplestory.service;

import java.io.Closeable;

/**
 * 基础的服务接口。
 */
public interface BaseService extends Runnable, Closeable {

    void init();

}
