package com.github.mrzhqiang.maplestory.service;

import com.github.mrzhqiang.maplestory.domain.LoginState;
import com.github.mrzhqiang.maplestory.domain.query.QDAccount;
import com.google.common.base.Stopwatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class AccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

    private final QDAccount qdAccount;

    @Inject
    public AccountService(QDAccount qdAccount) {
        this.qdAccount = qdAccount;
    }

    public void resetAccount() {
        LOGGER.info(">>> 重置 [账号状态]");
        Stopwatch stopwatch = Stopwatch.createStarted();
        int count = qdAccount.asUpdate()
                .set("state", LoginState.NOT_LOGIN)
                .set("lastGainHm", 0L)
                .update();
        LOGGER.info("<<< [账号状态] 重置完毕，耗时：{}，影响行数：{}", stopwatch.stop(), count);
    }
}
