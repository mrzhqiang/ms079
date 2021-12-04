package com.github.mrzhqiang.maplestory.wz;

import com.google.common.base.Stopwatch;
import io.reactivex.rxjava3.subscribers.DefaultSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * wz 加载订阅者。
 * <p>
 * 适合 RxJava 订阅时，不想处理 Disposable 的场景。
 * <p>
 * 允许重写 Subscriber 接口的相关方法，感知 wz 数据加载的生命周期。
 */
public class WzLoadSubscriber extends DefaultSubscriber<WzDirectory> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WzLoadSubscriber.class);

    private final Stopwatch stopwatch = Stopwatch.createUnstarted();

    @Override
    protected void onStart() {
        super.onStart();
        LOGGER.info(">>> 初始化 [wz 数据]");
        stopwatch.start();
    }

    @Override
    public void onNext(WzDirectory wzDirectory) {
        LOGGER.info(">>> 成功初始化 {} 数据，已耗时：{}", wzDirectory.name(), stopwatch);
    }

    @Override
    public void onError(Throwable t) {
        LOGGER.error(">>> 初始化 [wz 数据] 出错", t);
    }

    @Override
    public void onComplete() {
        LOGGER.info("<<< [wz 数据] 初始化完毕，总耗时：{}", stopwatch.stop());
    }
}
