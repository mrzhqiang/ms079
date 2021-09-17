package com.github.mrzhqiang.maplestory.config;

import org.junit.Assert;
import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class DatabasePropertiesTest {

    @Test
    public void testLoad() {
        Properties properties = new Properties();
        String path = Objects.requireNonNull(getClass().getResource("/test.ini")).getPath();
        try (FileReader reader = new FileReader(path)) {
            properties.load(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ServerProperties configuration = new ServerProperties(properties);
        Assert.assertEquals("兰达尔(v2021.1.0)", configuration.getName());
        Assert.assertEquals(100, configuration.getOnlineLimit());
    }
}