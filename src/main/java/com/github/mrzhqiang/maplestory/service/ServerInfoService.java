package com.github.mrzhqiang.maplestory.service;

import com.github.mrzhqiang.maplestory.config.ServerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class ServerInfoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerInfoService.class);

    private final ServerProperties properties;

    @Inject
    public ServerInfoService(ServerProperties properties) {
        this.properties = properties;
    }

    public void display() {
        if (properties.isAdminLogin()) {
            LOGGER.info(">>> 登录模式：只允许管理员登录");
        } else {
            LOGGER.info(">>> 登录模式：允许所有用户登录");
        }
        if (properties.isAutoRegister()) {
            LOGGER.info(">>> 注册模式：自动");
        } else {
            LOGGER.info(">>> 注册模式：手动（目前未实现网页注册、程序注册，需要使用 GM 工具手动创建账号）");
        }
        LOGGER.info(">>> 经验倍率：{}，物品倍率：{}，金币倍率：{}",
                properties.getExpRate(), properties.getDropRate(), properties.getGoldRate());
        LOGGER.info(">>> 当前开放职业：冒险家 = {}, 骑士团 = {}, 战神 = {}",
                properties.isAdventurer(), properties.isKnights(), properties.isWarGod());
    }
}
