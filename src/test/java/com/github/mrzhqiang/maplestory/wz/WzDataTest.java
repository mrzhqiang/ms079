package com.github.mrzhqiang.maplestory.wz;

import com.google.common.base.Stopwatch;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import provider.MapleData;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class WzDataTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(WzDataTest.class);

    @Test
    public void testData() {
        Stopwatch started = Stopwatch.createStarted();
        WzData.load();
        WzData.WzDirectory root = WzData.STRING.root();
        WzData.WzFile cashImg = root.file("Cash.img");
        assertNotNull(cashImg);
        assertEquals("Cash.img", cashImg.imgDir().getName());
        LOGGER.info("Wz data load use time: {}", started.stop());
    }

    @Test
    public void testOldData() {
        Stopwatch started = Stopwatch.createStarted();
        MapleDataProviderFactory.getDataProvider(WzFiles.CHARACTER_DIR);
        MapleDataProviderFactory.getDataProvider(WzFiles.ETC_DIR);
        MapleDataProviderFactory.getDataProvider(WzFiles.ITEM_DIR);
        MapleDataProviderFactory.getDataProvider(WzFiles.MAP_DIR);
        MapleDataProviderFactory.getDataProvider(WzFiles.MOB_DIR);
        MapleDataProviderFactory.getDataProvider(WzFiles.NPC_DIR);
        MapleDataProviderFactory.getDataProvider(WzFiles.QUEST_DIR);
        MapleDataProviderFactory.getDataProvider(WzFiles.REACTOR_DIR);
        MapleDataProviderFactory.getDataProvider(WzFiles.SKILL_DIR);
        MapleDataProvider provider = MapleDataProviderFactory.getDataProvider(WzFiles.STRING_DIR);
        MapleData data = provider.getData("Cash.img");
        assertEquals("Cash.img", data.getName());
        LOGGER.info("Maple data provider factory use time: {}", started.stop());
    }
}