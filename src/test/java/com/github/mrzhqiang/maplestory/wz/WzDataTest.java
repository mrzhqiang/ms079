package com.github.mrzhqiang.maplestory.wz;

import com.google.common.base.Stopwatch;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class WzDataTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(WzDataTest.class);

    @Test
    public void testData() {
        Stopwatch started = Stopwatch.createStarted();
        WzData.load();
        WzDirectory root = WzData.STRING.directory();
        Optional<WzFile> cashImg = root.findFile("Cash.img");
        assertTrue(cashImg.isPresent());
        assertEquals("Cash.img", cashImg.get().content().name());
        LOGGER.info("wz 数据加载总用时: {}", started.stop());
    }
}