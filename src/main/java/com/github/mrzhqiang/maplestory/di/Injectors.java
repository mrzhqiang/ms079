package com.github.mrzhqiang.maplestory.di;

import com.google.inject.ConfigurationException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.ProvisionException;

/**
 * 注入器工具。
 * <p>
 * 适用于 application 入口类的初始化。
 */
public enum Injectors {
    /**
     * 最好的单例模式。
     */
    SINGLETON;

    private final Injector injector = Guice.createInjector(
            ServerModule.INSTANCE,
            DatabaseModule.INSTANCE
    );

    /**
     * 返回给定注入类型的适当实例；相当于 {@code Guice.getProvider(type).get()}。
     * <p>
     * 如果可以的话，请避免使用此方法，以便让 Guice 提前注入您的依赖项。
     *
     * @throws ConfigurationException 如果此注入器无法找到或创建提供者。
     * @throws ProvisionException     如果在提供实例时出现运行时故障。
     */
    public static <T> T get(Class<T> clazz) {
        return SINGLETON.injector.getInstance(clazz);
    }
}
