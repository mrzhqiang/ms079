package handling;

import client.MapleClient;
import com.github.mrzhqiang.maplestory.config.ServerProperties;
import com.github.mrzhqiang.maplestory.di.Injectors;
import com.github.mrzhqiang.maplestory.domain.LoginState;
import constants.ServerConstants;
import handling.cashshop.CashShopServer;
import handling.cashshop.handler.CashShopOperation;
import handling.cashshop.handler.MTSOperation;
import handling.channel.ChannelServer;
import handling.channel.handler.AllianceHandler;
import handling.channel.handler.BBSHandler;
import handling.channel.handler.BeanGame;
import handling.channel.handler.BuddyListHandler;
import handling.channel.handler.ChatHandler;
import handling.channel.handler.DueyHandler;
import handling.channel.handler.FamilyHandler;
import handling.channel.handler.GuildHandler;
import handling.channel.handler.HiredMerchantHandler;
import handling.channel.handler.InterServerHandler;
import handling.channel.handler.InventoryHandler;
import handling.channel.handler.ItemMakerHandler;
import handling.channel.handler.MobHandler;
import handling.channel.handler.MonsterCarnivalHandler;
import handling.channel.handler.NPCHandler;
import handling.channel.handler.PartyHandler;
import handling.channel.handler.PetHandler;
import handling.channel.handler.PlayerHandler;
import handling.channel.handler.PlayerInteractionHandler;
import handling.channel.handler.PlayersHandler;
import handling.channel.handler.StatsHandling;
import handling.channel.handler.SummonHandler;
import handling.channel.handler.UserInterfaceHandler;
import handling.login.LoginServer;
import handling.login.handler.CharLoginHandler;
import handling.mina.MaplePacketDecoder;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.MTSStorage;
import server.Randomizer;
import tools.FileoutputUtil;
import tools.MapleAESOFB;
import tools.Pair;
import tools.data.input.ByteArrayByteStream;
import tools.data.input.GenericSeekableLittleEndianAccessor;
import tools.data.input.SeekableLittleEndianAccessor;
import tools.packet.LoginPacket;

import javax.inject.Inject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public final class MapleServerHandler extends IoHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapleServerHandler.class);

    //Screw locking. Doesn't matter.
    private static final ReentrantReadWriteLock IPLoggingLock = new ReentrantReadWriteLock();
    private static final String nl = System.lineSeparator();
        private static final File loggedIPs = new File("logs/LogIPs.txt");
    private static final HashMap<String, FileWriter> logIPMap = new HashMap<>();
    //零注意事项：使用枚举集。不要遍历数组。
    private static final EnumSet<RecvPacketOpcode> blocked = EnumSet.noneOf(RecvPacketOpcode.class);

    static {
//        reloadLoggedIPs();
        RecvPacketOpcode[] block = new RecvPacketOpcode[]{
                RecvPacketOpcode.NPC_ACTION,
                RecvPacketOpcode.MOVE_PLAYER,
                RecvPacketOpcode.MOVE_PET,
                RecvPacketOpcode.MOVE_SUMMON,
                RecvPacketOpcode.MOVE_DRAGON,
                RecvPacketOpcode.MOVE_LIFE,
                RecvPacketOpcode.HEAL_OVER_TIME,
                RecvPacketOpcode.STRANGE_DATA};
        blocked.addAll(Arrays.asList(block));
    }

    private int channel = -1;
    private boolean cs;

    public final CharLoginHandler handler;

    private final List<String> BlockedIP = new ArrayList<>();
    private final Map<String, Pair<Long, Byte>> tracker = new ConcurrentHashMap<>();

    @Inject
    public MapleServerHandler(CharLoginHandler handler) {
        this.handler = handler;
    }

    public static void reloadLoggedIPs() {
//        IPLoggingLock.writeLock().lock();
//        try {
        for (FileWriter fw : logIPMap.values()) {
            if (fw != null) {
                try {
                    fw.write("=== Closing Log ===");
                    fw.write(nl);
                    fw.flush(); //Just in case.
                    fw.close();
                } catch (IOException ex) {
                    LOGGER.debug("Error closing Packet Log.", ex);
                }
            }
        }
        logIPMap.clear();
        try {
            Scanner sc = new Scanner(loggedIPs);
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (!line.isEmpty()) {
                    FileWriter fw = new FileWriter("PacketLog_" + line + ".txt", true);
                    fw.write("=== Creating Log ===");
                    fw.write(nl);
                    fw.flush();
                    logIPMap.put(line, fw);
                }
            }
        } catch (Exception e) {
            LOGGER.debug("无法重新加载数据包记录的 IP。", e);
        }
//        } finally {
//            IPLoggingLock.writeLock().unlock();
//        }
    }

    //Return the Filewriter if the IP is logged. Null otherwise.
    private static FileWriter isLoggedIP(IoSession sess) {
        String a = sess.getRemoteAddress().toString();
        String realIP = a.substring(a.indexOf('/') + 1, a.indexOf(':'));
        return logIPMap.get(realIP);
    }

    // <editor-fold defaultstate="collapsed" desc="Packet Log Implementation">
    private static final int Log_Size = 10000;
    private static final ArrayList<LoggedPacket> Packet_Log = new ArrayList<LoggedPacket>(Log_Size);
    private static final ReentrantReadWriteLock Packet_Log_Lock = new ReentrantReadWriteLock();
    private static final File Packet_Log_Output = new File("PacketLog.txt");

    public static void log(SeekableLittleEndianAccessor packet, RecvPacketOpcode op, MapleClient c, IoSession io) {
        if (blocked.contains(op)) {
            return;
        }
        try {
            Packet_Log_Lock.writeLock().lock();
            LoggedPacket logged = null;
            if (Packet_Log.size() == Log_Size) {
                logged = Packet_Log.remove(0);
            }
            //This way, we don't create new LoggedPacket objects, we reuse them =]
            if (logged == null) {
                logged = new LoggedPacket(packet, op, io.getRemoteAddress().toString(),
                        c == null ? -1 : c.getAccID(),
                        c == null || c.getAccountName() == null ? "[Null]" : c.getAccountName(),
                        c == null || c.getPlayer() == null || c.getPlayer().getName() == null ? "[Null]" : c.getPlayer().getName());
            } else {
                logged.setInfo(packet, op, io.getRemoteAddress().toString(),
                        c == null ? -1 : c.getAccID(),
                        c == null || c.getAccountName() == null ? "[Null]" : c.getAccountName(),
                        c == null || c.getPlayer() == null || c.getPlayer().getName() == null ? "[Null]" : c.getPlayer().getName());
            }
            Packet_Log.add(logged);
        } finally {
            Packet_Log_Lock.writeLock().unlock();
        }
    }

    public void setChannel(int channel) {
        LOGGER.debug("channel change before" + this.channel + "after" + channel);
        this.channel = channel;
    }

    private static class LoggedPacket {

        private static final String nl = System.getProperty("line.separator");
        private String ip, accName, accId, chrName;
        private SeekableLittleEndianAccessor packet;
        private long timestamp;
        private RecvPacketOpcode op;

        public LoggedPacket(SeekableLittleEndianAccessor p, RecvPacketOpcode op, String ip, int id, String accName, String chrName) {
            setInfo(p, op, ip, id, accName, chrName);
        }

        public final void setInfo(SeekableLittleEndianAccessor p, RecvPacketOpcode op, String ip, int id, String accName, String chrName) {
            this.ip = ip;
            this.op = op;
            packet = p;
            this.accName = accName;
            this.chrName = chrName;
            timestamp = System.currentTimeMillis();
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("[IP: ").append(ip).append("] [").append(accId).append('|').append(accName).append('|').append(chrName).append("] [Time: ").append(timestamp).append(']');
            sb.append(nl);
            sb.append("[Op: ").append(op.toString()).append(']');
            sb.append(" [Data: ").append(packet.toString()).append(']');
            return sb.toString();
        }
    }

    public void writeLog() {
        try {
            FileWriter fw = new FileWriter(Packet_Log_Output, true);
            try {
                Packet_Log_Lock.readLock().lock();
                String nl = System.getProperty("line.separator");
                for (LoggedPacket loggedPacket : Packet_Log) {
                    fw.write(loggedPacket.toString());
                    fw.write(nl);
                }
                fw.flush();
                fw.close();
            } finally {
                Packet_Log_Lock.readLock().unlock();
            }
        } catch (IOException ex) {
            LOGGER.debug("Error writing log to file.");
        }
    }

    public void setCs(boolean cs) {
        this.cs = cs;
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        Runnable r = ((MaplePacket) message).getOnSend();
        if (r != null) {
            r.run();
        }
        super.messageSent(session, message);
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) {
        /*
         * MapleClient client = (MapleClient)
         * session.getAttribute(MapleClient.CLIENT_KEY);
         * log.error(MapleClient.getLogMessage(client, cause.getMessage()),
         * cause);
         */
//	cause.printStackTrace();
        LOGGER.error("连接出现异常", cause);
    }

    @Override
    public void sessionOpened(IoSession session) {
        // Start of IP checking
        String address = session.getRemoteAddress().toString().split(":")[0];
        String portStr = session.getLocalAddress().toString().split(":")[1];
        channel = ChannelServer.getChannelByPort(Integer.valueOf(portStr));

        if (BlockedIP.contains(address)) {
//            System.out.print("自动断开连接A");
//            session.close();
//            return;
        }
        Pair<Long, Byte> track = tracker.get(address);

        byte count;
        if (track == null) {
            count = 1;
        } else {
            count = track.right;

            long difference = System.currentTimeMillis() - track.left;
            if (difference < 2000) { // Less than 2 sec
                count++;
            } else if (difference > 20000) { // Over 20 sec
                count = 1;
            }
            if (count >= 10) {
                LOGGER.warn("自动断开连接A2");
                BlockedIP.add(address);
                tracker.remove(address); // Cleanup
                session.close();
                return;
            }
        }
        tracker.put(address, new Pair<>(System.currentTimeMillis(), count));
        String IP = address.substring(address.indexOf('/') + 1);
        // End of IP checking.

        if (channel > -1) {
            if (ChannelServer.getInstance(channel)!= null && ChannelServer.getInstance(channel).isShutdown()) {
                LOGGER.warn("自动断开连接B");
                session.close();
                return;
            }
            if (!LoginServer.containsIPAuth(IP)) {
//                System.out.print("自动断开连接C");
//                session.close();
//                return;
            }
        } else if (cs) {
            if (CashShopServer.isShutdown()) {
                LOGGER.warn("自动断开连接D");
                session.close();
                return;
            }
        } else if (handler.loginServer.isShutdown()) {
            LOGGER.warn("自动断开连接E");
            session.close();
            return;
        }
        LoginServer.removeIPAuth(IP);
        byte[] serverRecv = new byte[]{70, 114, 122, (byte) Randomizer.nextInt(255)};
        byte[] serverSend = new byte[]{82, 48, 120, (byte) Randomizer.nextInt(255)};
        byte[] ivRecv = ServerConstants.Use_Fixed_IV ? new byte[]{9, 0, 0x5, 0x5F} : serverRecv;
        byte[] ivSend = ServerConstants.Use_Fixed_IV ? new byte[]{1, 0x5F, 4, 0x3F} : serverSend;

        MapleClient client = new MapleClient(
                new MapleAESOFB(ivSend, (short) (0xFFFF - ServerConstants.MAPLE_VERSION)), // Sent Cypher
                new MapleAESOFB(ivRecv, ServerConstants.MAPLE_VERSION), // Recv Cypher
                session, Injectors.get(ServerProperties.class));
        LOGGER.debug("channel is " + channel + "");

        client.setChannel(channel);

        MaplePacketDecoder.DecoderState decoderState = new MaplePacketDecoder.DecoderState();
        session.setAttribute(MaplePacketDecoder.DECODER_STATE_KEY, decoderState);

        session.write(LoginPacket.getHello(ServerConstants.MAPLE_VERSION,
                ServerConstants.Use_Fixed_IV ? serverSend : ivSend, ServerConstants.Use_Fixed_IV ? serverRecv : ivRecv));
        session.setAttribute(MapleClient.CLIENT_KEY, client);
        session.setAttribute(IdleStatus.READER_IDLE, 60);
        session.setAttribute(IdleStatus.WRITER_IDLE, 60);

        StringBuilder sb = new StringBuilder();
        if (channel > -1) {
            sb.append("[频道服务器] 频道 ").append(channel).append(" : ");
        } else if (cs) {
            sb.append("[商城服务器]");
        } else {
            sb.append("[登录服务器]");
            /*if (!"/127.0.0.1".equals(address)) {
                LOGGER.debug("侦测到非登录器登录： " + address);
            }*/
        }
        sb.append("IoSession opened ").append(address);
        LOGGER.debug(sb.toString());

        if (channel > -1) {
            LOGGER.info("logged into channel {}", channel);
        } else if (cs) {
            LOGGER.info("logged into cash shop server");
        } else {
            LOGGER.info("logged into login server");
        }

        /*FileWriter fw = isLoggedIP(session);
        if (fw != null) {
            if (channel > -1) {
                fw.write("=== Logged Into Channel " + channel + " ===");
                fw.write(nl);
            } else if (cs) {
                fw.write("=== Logged Into CashShop Server ===");
                fw.write(nl);
            } else {
                fw.write("=== Logged Into Login Server ===");
                fw.write(nl);
            }
            fw.flush();
            //client.setMonitored(true);
        }*/
    }

    @Override
    public void sessionClosed(final IoSession session) throws Exception {
        MapleClient client = (MapleClient) session.getAttribute(MapleClient.CLIENT_KEY);

        if (client != null) {
            try {
                /*FileWriter fw = isLoggedIP(session);
                if (fw != null) {
                    fw.write("=== Session Closed ===");
                    fw.write(nl);
                    fw.flush();
                }*/
                LOGGER.info("IoSession session {} closed", client.getSession().getRemoteAddress());
                client.disconnect(true, cs);
            } finally {
                session.close();
                session.removeAttribute(MapleClient.CLIENT_KEY);
            }
        }
        super.sessionClosed(session);
    }

    @Override
    public void messageReceived(IoSession session, Object message) {
        try {
            SeekableLittleEndianAccessor slea = new GenericSeekableLittleEndianAccessor(new ByteArrayByteStream((byte[]) message));
            if (slea.available() < 2) {
                return;
            }
            // opCode
            short header_num = slea.readShort();
            // Console output part
            for (RecvPacketOpcode recv : RecvPacketOpcode.values()) {
                if (recv.getValue() == header_num) {

                    if (ServerConstants.properties.isDebug()) {//&& !RecvPacketOpcode.isSpamHeader(recv)
                        LOGGER.debug("Received data 已處理 :" + recv + "\n"
                                + tools.HexTool.toString((byte[]) message) + "\n"
                                + tools.HexTool.toStringFromAscii((byte[]) message));
                    }
                    MapleClient client = (MapleClient) session.getAttribute(MapleClient.CLIENT_KEY);
                    if (!client.isReceiving()) {
                        return;
                    }
                    // false 表示无需检测是否登录，true 表示要检测登录状态
                    if (recv.checkState()) {
                        if (!client.isLoggedIn() && client.getLoginState() != LoginState.SERVER_TRANSITION ) {
                            return;
                        }
                    }
                    if (client.getPlayer() != null && client.isMonitored()) {
                        if (!blocked.contains(recv)) {
//                            FileoutputUtil.log("日志/logs/Monitored/" + client.getPlayer().getName() + ".txt", recv + " (" + Integer.toHexString(header_num) + ") Handled: \r\n" + slea + "\r\n");

//                            FileWriter fw = new FileWriter(new File("日志/logs/MonitorLogs/" + client.getPlayer().getName() + "_log.txt"), true);
//                            fw.write(String.valueOf(recv) + " (" + Integer.toHexString(header_num) + ") Handled: \r\n" + slea.toString() + "\r\n");
//                            fw.flush();
//                            fw.close();
                        }
                    }
//                    if (Log_Packets) {
//                        log(slea, recv, client, session);
//                    }
                    handlePacket(recv, slea, client, cs);

                    //处理完数据包后记录。你会明白为什么=]
//                    FileWriter fw = isLoggedIP(session);
                    if (/*fw != null && */!blocked.contains(recv)) {
                        if (recv == RecvPacketOpcode.PLAYER_LOGGEDIN) { // << 这就是为什么。赢。
                            LOGGER.info(">> [AccountName: "
                                    + (client.getAccountName() == null ? "null" : client.getAccountName())
                                    + "] | [IGN: "
                                    + (client.getPlayer() == null || client.getPlayer().getName() == null
                                    ? "null"
                                    : client.getPlayer().getName())
                                    + "] | [Time: " + FileoutputUtil.CurrentReadable_Time() + "]");
//                            fw.write(nl);
                        }
                        LOGGER.info("[" + recv + "]" + slea.toString(true));
//                        fw.write(nl);
//                        fw.flush();
                    }
                    return;
                }
            }
            if (ServerConstants.properties.isDebug()) {
                String sb = "Received data 未處理 : "
                        + tools.HexTool.toString((byte[]) message) + "\n"
                        + tools.HexTool.toStringFromAscii((byte[]) message);
                LOGGER.debug(sb);
            }
        } catch (RejectedExecutionException ignored) {
        } catch (Exception e) {
            FileoutputUtil.outputFileError(FileoutputUtil.PacketEx_Log, e);
            LOGGER.error("处理消息时出错", e);
        }

    }

    @Override
    public void sessionIdle(final IoSession session, final IdleStatus status) throws Exception {
        final MapleClient client = (MapleClient) session.getAttribute(MapleClient.CLIENT_KEY);

        /*
         * if (client != null && client.getPlayer() != null) {
         * LOGGER.debug("玩家 "+ client.getPlayer().getName() +" 正在掛網"); }
         */
        if (client != null) {
            client.sendPing();
        } else {
            session.close();
            return;
        }
        super.sessionIdle(session, status);
    }

    public void handlePacket(RecvPacketOpcode header, SeekableLittleEndianAccessor slea,
                             MapleClient client, boolean cs) {
        //LOGGER.debug(header);
        switch (header) {
            case PONG:
                client.pongReceived();
                break;
            case ERROR_LOG:
                client.errorLogReceived(slea);
                break;
            case STRANGE_DATA:
                // 现在什么都不做，HackShield 的心跳
                break;
            case EFFECT_ON_OFF:
            case NEW_SX:
                // 现在什么都不做，HackShield 的心跳
                break;
            case HELLO_LOGIN:
                handler.Welcome(client);
                // 现在什么都不做，HackShield 的心跳
                break;
            case HELLO_CHANNEL:
                handler.Welcome(client);
                break;
            case LOGIN_PASSWORD:
                /*
                 * try { Thread.sleep(1000L);//2ms } catch (InterruptedException
                 * ex) {//捕获该线程异常 LOGGER.debug("线程锁开启失败"); }
                 */
                handler.login(slea, client);
                break;
            case SERVERLIST_REQUEST:
            case LICENSE_REQUEST:
                handler.ServerListRequest(client);
                break;
            case CHARLIST_REQUEST:
                handler.CharlistRequest(slea, client);
                break;
            case SERVERSTATUS_REQUEST:
                handler.ServerStatusRequest(client);
                break;
            case CHECK_CHAR_NAME:
                handler.CheckCharName(slea.readMapleAsciiString(), client);
                break;
            case CREATE_CHAR:
                handler.CreateChar(slea, client);
                break;
            case DELETE_CHAR:
                handler.DeleteChar(slea, client);
                break;
            case CHAR_SELECT:
                handler.Character_WithoutSecondPassword(slea, client);
                break;
            case AUTH_SECOND_PASSWORD:
                handler.Character_WithSecondPassword(slea, client);
                break;
            case SET_GENDER:
                handler.SetGenderRequest(slea, client);
                break;
            /*case RSA_KEY: // Fix this somehow
                client.getSession().write(LoginPacket.StrangeDATA());
                break;*/
            // END OF LOGIN SERVER
            case CHANGE_CHANNEL:
                InterServerHandler.ChangeChannel(slea, client, client.getPlayer());
                break;
            case PLAYER_LOGGEDIN:
                int playerid = slea.readInt();
                if (cs) {
                    CashShopOperation.EnterCS(playerid, client);
                } else {
                    InterServerHandler.Loggedin(playerid, client);
                }
                break;
            case ENTER_CASH_SHOP:
                slea.readInt();
                InterServerHandler.EnterCS(client, client.getPlayer(), false);
                break;
            case ENTER_MTS:
                InterServerHandler.EnterMTS(client, client.getPlayer(), true);
                break;
            case PLAYER_UPDATE:
                PlayerHandler.UpdateHandler(slea, client, client.getPlayer());
                break;
            case MOVE_PLAYER:
                PlayerHandler.MovePlayer(slea, client, client.getPlayer());
                break;
            case CHAR_INFO_REQUEST:
                client.getPlayer().updateTick(slea.readInt());
                PlayerHandler.CharInfoRequest(slea.readInt(), client, client.getPlayer());
                //LOGGER.error("CHAR_INFO_REQUEST");
                break;
            case CLOSE_RANGE_ATTACK:
                PlayerHandler.closeRangeAttack(slea, client, client.getPlayer(), false);
                break;
            case RANGED_ATTACK:
                PlayerHandler.rangedAttack(slea, client, client.getPlayer());
                break;
            case MAGIC_ATTACK:
                PlayerHandler.MagicDamage(slea, client, client.getPlayer());
                break;
            case SPECIAL_MOVE:
                PlayerHandler.SpecialMove(slea, client, client.getPlayer());
                break;
            case PASSIVE_ENERGY:
                PlayerHandler.closeRangeAttack(slea, client, client.getPlayer(), true);
                break;
            case FACE_EXPRESSION:
                PlayerHandler.ChangeEmotion(slea.readInt(), client.getPlayer());
                break;
            case TAKE_DAMAGE:
                PlayerHandler.TakeDamage(slea, client, client.getPlayer());
                break;
            case HEAL_OVER_TIME:
                PlayerHandler.Heal(slea, client.getPlayer());
                break;
            case CANCEL_BUFF:
                PlayerHandler.CancelBuffHandler(slea.readInt(), client.getPlayer());
                break;
            case CANCEL_ITEM_EFFECT:
                PlayerHandler.CancelItemEffect(slea.readInt(), client.getPlayer());
                break;
            case USE_CHAIR:
                PlayerHandler.UseChair(slea.readInt(), client, client.getPlayer());
                break;
            case CANCEL_CHAIR:
                PlayerHandler.CancelChair(slea.readShort(), client, client.getPlayer());
                break;
            case USE_ITEMEFFECT:
                //case WHEEL_OF_FORTUNE:
                PlayerHandler.UseItemEffect(slea, client, client.getPlayer());
                // PlayerHandler.UseItemEffect(slea.readInt(), client, client.getPlayer());
                break;
            case WHEEL_OF_FORTUNE:
                LOGGER.debug("WHEEL_OF_FORTUNE啊啊啊");
                break;
            case SKILL_EFFECT:
                PlayerHandler.SkillEffect(slea, client.getPlayer());
                break;
            case MESO_DROP:
                client.getPlayer().updateTick(slea.readInt());
                PlayerHandler.DropMeso(slea.readInt(), client.getPlayer());
                break;
            case MONSTER_BOOK_COVER:
                PlayerHandler.ChangeMonsterBookCover(slea.readInt(), client, client.getPlayer());
                break;
            case CHANGE_KEYMAP:
                PlayerHandler.ChangeKeymap(slea, client.getPlayer());
                break;
            case CHANGE_MAP:
                if (cs) {
                    if (ServerConstants.properties.isPacketDebugLogger()) {
                        LOGGER.debug("退出商城");
                    }
                    CashShopOperation.LeaveCS(slea, client, client.getPlayer());
                } else {
                    PlayerHandler.ChangeMap(slea, client, client.getPlayer());
                }
                break;
            case CHANGE_MAP_SPECIAL:
                slea.skip(1);
                PlayerHandler.ChangeMapSpecial(slea, slea.readMapleAsciiString(), client, client.getPlayer());
                break;
            case USE_INNER_PORTAL:
                slea.skip(1);
                PlayerHandler.InnerPortal(slea, client, client.getPlayer());
                break;
            case TROCK_ADD_MAP:
                PlayerHandler.TrockAddMap(slea, client, client.getPlayer());
                break;
            case ARAN_COMBO:
                PlayerHandler.AranCombo(client, client.getPlayer());
                break;
            case SKILL_MACRO:
                PlayerHandler.ChangeSkillMacro(slea, client.getPlayer());
                break;
            case ITEM_BAOWU:
                InventoryHandler.UsePenguinBox(slea, client);
                break;
            case ITEM_SUNZI:
                InventoryHandler.SunziBF(slea, client);
                break;
            case GIVE_FAME:
                PlayersHandler.GiveFame(slea, client, client.getPlayer());
                break;
            case TRANSFORM_PLAYER:
                PlayersHandler.TransformPlayer(slea, client, client.getPlayer());
                break;
            case NOTE_ACTION:
                PlayersHandler.Note(slea, client.getPlayer());
                break;
            case USE_DOOR:
                PlayersHandler.UseDoor(slea, client.getPlayer());
                break;
            case DAMAGE_REACTOR:
                PlayersHandler.HitReactor(slea, client);
                break;
            case TOUCH_REACTOR:
                PlayersHandler.TouchReactor(slea, client);
                break;
            case CLOSE_CHALKBOARD:
                client.getPlayer().setChalkboard(null);
                break;
            case ITEM_MAKER:
                ItemMakerHandler.ItemMaker(slea, client);
                break;
            case ITEM_SORT://道具集合
                InventoryHandler.ItemSort(slea, client);
                break;
            case ITEM_GATHER://道具排序
                InventoryHandler.ItemGather(slea, client);
                break;
            case ITEM_MOVE:
                InventoryHandler.ItemMove(slea, client);
                break;
            case ITEM_PICKUP:
                InventoryHandler.Pickup_Player(slea, client, client.getPlayer());
                break;
            case USE_CASH_ITEM:
                InventoryHandler.UseCashItem(slea, client);
                break;
            case quest_KJ:
                InventoryHandler.QuestKJ(slea, client, client.getPlayer());
                break;
            case USE_ITEM:
                InventoryHandler.UseItem(slea, client, client.getPlayer());
                break;
            case USE_MAGNIFY_GLASS:
                InventoryHandler.UseMagnify(slea, client);
                break;
            case USE_SCRIPTED_NPC_ITEM:
                InventoryHandler.UseScriptedNPCItem(slea, client, client.getPlayer());
                break;
            case USE_RETURN_SCROLL:
                InventoryHandler.UseReturnScroll(slea, client, client.getPlayer());
                break;
            case USE_UPGRADE_SCROLL:
                client.getPlayer().updateTick(slea.readInt());
                InventoryHandler.UseUpgradeScroll((byte) slea.readShort(), (byte) slea.readShort(), (byte) slea.readShort(), client, client.getPlayer());
                break;
            case USE_POTENTIAL_SCROLL:
                client.getPlayer().updateTick(slea.readInt());
                InventoryHandler.UseUpgradeScroll((byte) slea.readShort(), (byte) slea.readShort(), (byte) 0, client, client.getPlayer());
                break;
            case USE_EQUIP_SCROLL:
                client.getPlayer().updateTick(slea.readInt());
                InventoryHandler.UseUpgradeScroll((byte) slea.readShort(), (byte) slea.readShort(), (byte) 0, client, client.getPlayer());
                break;
            case USE_SUMMON_BAG:
                InventoryHandler.UseSummonBag(slea, client, client.getPlayer());
                break;
            case USE_TREASUER_CHEST:
                InventoryHandler.UseTreasureChest(slea, client, client.getPlayer());
                break;
            case USE_SKILL_BOOK:
//                client.getPlayer().updateTick(slea.readInt());
                InventoryHandler.UseSkillBook(slea, client, client.getPlayer());
                break;
            case USE_CATCH_ITEM:
                InventoryHandler.UseCatchItem(slea, client, client.getPlayer());
                break;
            case USE_MOUNT_FOOD:
                InventoryHandler.UseMountFood(slea, client, client.getPlayer());
                break;
            case HYPNOTIZE_DMG:
                MobHandler.HypnotizeDmg(slea, client.getPlayer());
                break;
            case MOB_NODE:
                MobHandler.MobNode(slea, client.getPlayer());
                break;
            case DISPLAY_NODE:
                MobHandler.DisplayNode(slea, client.getPlayer());
                break;
            case MOVE_LIFE:
                MobHandler.MoveMonster(slea, client, client.getPlayer());
                break;
            case AUTO_AGGRO:
                MobHandler.AutoAggro(slea.readInt(), client.getPlayer());
                break;
            case FRIENDLY_DAMAGE:
                MobHandler.FriendlyDamage(slea, client.getPlayer());
                break;
            case MONSTER_BOMB:
                MobHandler.MonsterBomb(slea.readInt(), client.getPlayer());
                break;
            case NPC_SHOP:
                NPCHandler.NPCShop(slea, client, client.getPlayer());
                break;
            case NPC_TALK:
                NPCHandler.NPCTalk(slea, client, client.getPlayer());
                break;
            case NPC_TALK_MORE:
                NPCHandler.NPCMoreTalk(slea, client);
                break;
            case MARRAGE_RECV:
                NPCHandler.MarrageNpc(client);
                break;
            case NPC_ACTION:
                NPCHandler.NPCAnimation(slea, client);
                break;
            case QUEST_ACTION:
                NPCHandler.QuestAction(slea, client, client.getPlayer());
                break;
            case STORAGE:
                NPCHandler.Storage(slea, client, client.getPlayer());
                break;
            case GENERAL_CHAT:
//                client.getPlayer().updateTick(slea.readInt());
                ChatHandler.GeneralChat(slea.readMapleAsciiString(), slea.readByte(), client, client.getPlayer());
                break;
            case PARTYCHAT:
                ChatHandler.Others(slea, client, client.getPlayer());
                break;
//            case PARTY_SS:
//                ChatHandler.PARTY_SS(slea, client, client.getPlayer());
//                break;
            case WHISPER:
                ChatHandler.Whisper_Find(slea, client);
                break;
            case MESSENGER:
                ChatHandler.Messenger(slea, client);
                break;
            case AUTO_ASSIGN_AP:
                StatsHandling.AutoAssignAP(slea, client, client.getPlayer());
                break;
            case DISTRIBUTE_AP:
                StatsHandling.DistributeAP(slea, client, client.getPlayer());
                break;
            case DISTRIBUTE_SP:
                client.getPlayer().updateTick(slea.readInt());
                StatsHandling.DistributeSP(slea.readInt(), client, client.getPlayer());
                break;
            case PLAYER_INTERACTION:
                PlayerInteractionHandler.PlayerInteraction(slea, client, client.getPlayer());
                break;
            case GUILD_OPERATION:
                GuildHandler.Guild(slea, client);
                break;
            case UPDATE_CHAR_INFO:
                //LOGGER.error("UPDATE_CHAR_INFO");
                //PlayersHandler.UpdateCharInfo(slea.readAsciiString(), client);
                PlayersHandler.UpdateCharInfo(slea, client, client.getPlayer());
                break;
            case DENY_GUILD_REQUEST:
                slea.skip(1);
                GuildHandler.DenyGuildRequest(slea.readMapleAsciiString(), client);
                break;
            case ALLIANCE_OPERATION:
                AllianceHandler.HandleAlliance(slea, client, false);
                break;
            case DENY_ALLIANCE_REQUEST:
                AllianceHandler.HandleAlliance(slea, client, true);
                break;
            case BBS_OPERATION:
                BBSHandler.BBSOperatopn(slea, client);
                break;
            case PARTY_OPERATION:
                PartyHandler.PartyOperatopn(slea, client);
                break;
            case DENY_PARTY_REQUEST:
                PartyHandler.DenyPartyRequest(slea, client);
                break;
            case BUDDYLIST_MODIFY:
                BuddyListHandler.BuddyOperation(slea, client);
                break;
            case CYGNUS_SUMMON:
                UserInterfaceHandler.CygnusSummon_NPCRequest(client);
                break;
            case SHIP_OBJECT:
                UserInterfaceHandler.ShipObjectRequest(slea.readInt(), client);
                break;
            case BUY_CS_ITEM:
                CashShopOperation.BuyCashItem(slea, client, client.getPlayer());
                break;
            case TOUCHING_CS:
                CashShopOperation.TouchingCashShop(client);
                break;
            case COUPON_CODE:
                FileoutputUtil.log(FileoutputUtil.PacketEx_Log, "Coupon : \n" + slea.toString(true));
                LOGGER.debug(slea.toString());
                slea.skip(2);
                CashShopOperation.CouponCode(slea.readMapleAsciiString(), client);
                break;
            case CS_UPDATE:
                CashShopOperation.CSUpdate(client);
                break;
            case TOUCHING_MTS:
                MTSOperation.MTSUpdate(MTSStorage.getInstance().getCart(client.getPlayer().getId()), client);
                break;
            case MTS_TAB:
                MTSOperation.MTSOperation(slea, client);
                break;
            case DAMAGE_SUMMON:
                //   slea.skip(4);
                SummonHandler.DamageSummon(slea, client.getPlayer());
                break;
            case MOVE_SUMMON:
                SummonHandler.MoveSummon(slea, client.getPlayer());
                break;
            case SUMMON_ATTACK:
                SummonHandler.SummonAttack(slea, client, client.getPlayer());
                break;
            case MOVE_DRAGON:
                SummonHandler.MoveDragon(slea, client.getPlayer());
                break;
            case PET_EXCEPTIONLIST:
                PetHandler.PickExceptionList(slea, client, client.getPlayer());
                break;
            case SPAWN_PET:
                PetHandler.SpawnPet(slea, client, client.getPlayer());
                break;
            case MOVE_PET:
                PetHandler.MovePet(slea, client.getPlayer());
                break;
            case PET_CHAT:
                if (slea.available() < 12) {
                    break;
                }
                PetHandler.PetChat((int) slea.readLong(), slea.readShort(), slea.readMapleAsciiString(), client.getPlayer());
                break;
            case PET_COMMAND:
                PetHandler.PetCommand(slea, client, client.getPlayer());
                break;
            case PET_FOOD:
                PetHandler.PetFood(slea, client, client.getPlayer());
                break;
            case PET_LOOT:
                InventoryHandler.Pickup_Pet(slea, client, client.getPlayer());
                break;
            case PET_AUTO_POT:
                PetHandler.Pet_AutoPotion(slea, client, client.getPlayer());
                break;
            case MONSTER_CARNIVAL:
                MonsterCarnivalHandler.MonsterCarnival(slea, client);
                break;
            case DUEY_ACTION:
                DueyHandler.DueyOperation(slea, client);
                break;
            case USE_HIRED_MERCHANT:
                HiredMerchantHandler.UseHiredMerchant(slea, client);
                break;
            case MERCH_ITEM_STORE:
                HiredMerchantHandler.MerchantItemStore(slea, client);
                break;
            case CANCEL_DEBUFF:
                // Ignore for now
                break;
            case LEFT_KNOCK_BACK:
                PlayerHandler.leftKnockBack(slea, client);
                break;
            case SNOWBALL:
                PlayerHandler.snowBall(slea, client);
                break;
            case ChatRoom_SYSTEM:
                PlayersHandler.ChatRoomHandler(slea, client);
                break;
            case COCONUT:
                PlayersHandler.hitCoconut(slea, client);
                break;
            case REPAIR:
                NPCHandler.repair(slea, client);
                break;
            case REPAIR_ALL:
                NPCHandler.repairAll(client);
                break;
            case GAME_POLL:
                UserInterfaceHandler.InGame_Poll(slea, client);
                break;
            case OWL:
                InventoryHandler.Owl(slea, client);
                break;
            case OWL_WARP:
                InventoryHandler.OwlWarp(slea, client);
                break;
            case USE_OWL_MINERVA:
                InventoryHandler.OwlMinerva(slea, client);
                break;
            case RPS_GAME:
                NPCHandler.RPSGame(slea, client);
                break;
            case UPDATE_QUEST:
                NPCHandler.UpdateQuest(slea, client);
                break;
            case USE_ITEM_QUEST:
                NPCHandler.UseItemQuest(slea, client);
                break;
            case FOLLOW_REQUEST:
                PlayersHandler.FollowRequest(slea, client);
                break;
            case FOLLOW_REPLY:
                PlayersHandler.FollowReply(slea, client);
                break;
            case RING_ACTION:
                PlayersHandler.RingAction(slea, client);
                break;
            case REQUEST_FAMILY:
                FamilyHandler.RequestFamily(slea, client);
                break;
            case OPEN_FAMILY:
                FamilyHandler.OpenFamily(slea, client);
                break;
            case FAMILY_OPERATION:
                FamilyHandler.FamilyOperation(slea, client);
                break;
            case DELETE_JUNIOR:
                FamilyHandler.DeleteJunior(slea, client);
                break;
            case DELETE_SENIOR:
                FamilyHandler.DeleteSenior(slea, client);
                break;
            case USE_FAMILY:
                FamilyHandler.UseFamily(slea, client);
                break;
            case FAMILY_PRECEPT:
                FamilyHandler.FamilyPrecept(slea, client);
                break;
            case FAMILY_SUMMON:
                FamilyHandler.FamilySummon(slea, client);
                break;
            case ACCEPT_FAMILY:
                FamilyHandler.AcceptFamily(slea, client);
                break;
            case BEANS_GAME1: // Fix this somehow
                BeanGame.BeanGame1(slea, client);
                break;
            case BEANS_GAME2: // Fix this somehow
                BeanGame.BeanGame2(slea, client);
                break;
            case MOONRABBIT_HP:
                PlayerHandler.Rabbit(slea, client);
                break;
            default:
                LOGGER.debug("[未经处理的] 客户端包 [" + header + "] 发现了");
                break;
        }
     /*   if (client.getPlayer().getNX() >= 10) {
            client.getPlayer().modifyCSPoints(1, -10);
        } else {
            client.getPlayer().dropMessage("您的点卷已经消耗光了。无法再做任何操作");
            return;
        }*/
    }
}
