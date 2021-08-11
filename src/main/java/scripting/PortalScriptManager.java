/*
 This file is part of the OdinMS Maple Story Server
 Copyright (C) 2008 ~ 2010 Patrick Huy <patrick.huy@frz.cc> 
 Matthias Butz <matze@odinms.de>
 Jan Christian Meyer <vimes@odinms.de>

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License version 3
 as published by the Free Software Foundation. You may not use, modify
 or distribute this program under any other version of the
 GNU Affero General Public License.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package scripting;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;

import client.MapleClient;
import java.io.*;
import server.MaplePortal;
import tools.FileoutputUtil;

public class PortalScriptManager {

    private static final PortalScriptManager instance = new PortalScriptManager();
    private final Map<String, PortalScript> scripts = new HashMap<String, PortalScript>();
    private final static ScriptEngineFactory sef = new ScriptEngineManager().getEngineByName("javascript").getFactory();

    public final static PortalScriptManager getInstance() {
        return instance;
    }

    private PortalScript getPortalScript(MapleClient c, final String scriptName) {
        if (scripts.containsKey(scriptName)) {
            scripts.clear();
            return scripts.get(scriptName);
        }

        final File scriptFile = new File("脚本/传送点/" + scriptName + ".js");
        if (!scriptFile.exists()) {
            //scripts.put(scriptName, null);
            return null;
        }

        InputStream fr = null;
        final ScriptEngine portal = sef.getScriptEngine();
        try {
            fr = new FileInputStream(scriptFile);
            BufferedReader bf = new BufferedReader(new InputStreamReader(fr, EncodingDetect.getJavaEncode(scriptFile)));
            CompiledScript compiled = ((Compilable) portal).compile(bf);
            compiled.eval();
        } catch (final Exception e) {
            System.err.println("Error executing Portalscript: " + scriptName + ":" + e);
            FileoutputUtil.log(FileoutputUtil.ScriptEx_Log, "Error executing Portal script. (" + scriptName + ") " + e);
        } finally {
            if (fr != null) {
                try {
                    fr.close();
                } catch (final IOException e) {
                    System.err.println("ERROR CLOSING" + e);
                }
            }
        }
        final PortalScript script = ((Invocable) portal).getInterface(PortalScript.class);
        scripts.put(scriptName, script);
        return script;
    }

    public void executePortalScript(MaplePortal portal, MapleClient c) {
        final PortalScript script = getPortalScript(c, portal.getScriptName());

        if (script != null) {
            try {
                script.enter(new PortalPlayerInteraction(c, portal));
                if (c.getPlayer().isAdmin()) {
                    c.getPlayer().dropMessage(5, "[执行传送点]:名为:(" + portal.getScriptName() + ".js)的文件 在地图 " + c.getPlayer().getMapId() + " - " + c.getPlayer().getMap().getMapName());
                }
            } catch (Exception e) {
                //System.err.println("执行地图脚本过程中发生错误.请检查Portal为:( " + portal.getScriptName() + ".js)的文件." + e);
                FileoutputUtil.log("log\\Script_Except.log", "执行地图脚本过程中发生错误.请检查Portal为:( " + portal.getScriptName() + ".js)的文件.\r\n错误信息:" + e);
            }
        } else {
            if (c.getPlayer().isAdmin()) {
                c.getPlayer().dropMessage(5, "未找到Portal为:(" + portal.getScriptName() + ".js)的文件 在地图 " + c.getPlayer().getMapId() + " - " + c.getPlayer().getMap().getMapName());
            }
            FileoutputUtil.log("log\\Script_Except.log", "执行地图脚本过程中发生错误.未找到Portal为:(" + portal.getScriptName() + ".js)的文件 在地图 " + c.getPlayer().getMapId() + " - " + c.getPlayer().getMap().getMapName());
        }
    }

    public final void clearScripts() {
        scripts.clear();
    }
}
