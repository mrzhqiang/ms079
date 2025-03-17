package com.github.mrzhqiang.maplestory.wz;

import com.google.common.base.Stopwatch;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.maps.MapleMap;
import server.maps.MapleMapFactory;

import java.util.Optional;

import static org.junit.Assert.*;

public class WzDataTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(WzDataTest.class);

    @Test
    public void testData() throws InterruptedException {
        Stopwatch started = Stopwatch.createStarted();
        WzResource.load().subscribe();
        Thread.sleep(10000L);
        WzDirectory root = WzData.STRING.directory();
        Optional<WzFile> cashImg = root.findFile("Cash.img");
        assertTrue(cashImg.isPresent());
        assertEquals("Cash.img", cashImg.get().content().name());
        LOGGER.info("wz 数据加载总用时: {}", started.stop());
    }


    @Test
    public void testData2() throws InterruptedException {
        Stopwatch started = Stopwatch.createStarted();
        WzResource.load().subscribe();
        Thread.sleep(10000L);
        MapleMapFactory mapFactory =  new MapleMapFactory();

        MapleMap map = mapFactory.getMap(100000000);
        assertNotNull(map);
        LOGGER.info("wz 数据加载总用时: {}", started.stop());
    }
}