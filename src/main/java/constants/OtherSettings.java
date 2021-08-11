// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   set.java
package constants;

import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OtherSettings {

    private static OtherSettings instance = null;
    private static boolean CANLOG;
    private Properties itempb_cfg;
    private String itempb_id[];
    private String itemjy_id[];
    private String itemgy_id[];
    private String mappb_id[];
    private static Logger log = LoggerFactory.getLogger(OtherSettings.class);

    public OtherSettings() {
        itempb_cfg = new Properties();
        try {
            InputStreamReader is = new FileReader("服务端配置.ini");
            itempb_cfg.load(is);
            is.close();
            itempb_id = itempb_cfg.getProperty("cashban").split(",");
            itemjy_id = itempb_cfg.getProperty("cashjy", "0").split(",");
            itemgy_id = itempb_cfg.getProperty("gysj", "0").split(",");
        } catch (Exception e) {
            log.error("加载商城配置出错，请检查", e);
        }
    }
    public String[] getItempb_id() {
        return itempb_id;
    }

    public String[] getItemgy_id() {
        return itemgy_id;
    }

    public String[] getItemjy_id() {
        return itemjy_id;
    }
    public String[] getMappb_id() {
        return mappb_id;
    }
    public boolean isCANLOG() {
        return CANLOG;
    }

    public void setCANLOG(boolean CANLOG) {
        OtherSettings.CANLOG = CANLOG;
    }

    public static OtherSettings getInstance() {
        if (instance == null) {
            instance = new OtherSettings();
        }
        return instance;
    }
}
