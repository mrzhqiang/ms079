package com.github.mrzhqiang.maplestory.config;

import org.junit.Assert;
import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Properties;

public class DatabasePropertiesTest {

    @Test
    public void testLoad() {
        Properties properties = new Properties();
        // 直接通过 InputStream 读取资源，避免路径问题
        try (InputStream inputStream = getClass().getResourceAsStream("/test.ini");
             // 明确指定使用 UTF-8 解码
             InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            properties.load(reader);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("加载配置文件失败: " + e.getMessage());
        }
        ServerProperties configuration = new ServerProperties(properties);
        Assert.assertEquals("兰达尔(v2021.1.0)", configuration.getName());
        Assert.assertEquals(100, configuration.getOnlineLimit());
    }
}