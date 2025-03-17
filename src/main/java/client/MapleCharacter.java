package client;

import client.anticheat.CheatTracker;
import client.inventory.Equip;
import client.inventory.IItem;
import client.inventory.Item;
import client.inventory.ItemFlag;
import client.inventory.ItemLoader;
import client.inventory.MapleInventory;
import client.inventory.MapleInventoryIdentifier;
import client.inventory.MapleInventoryType;
import client.inventory.MapleMount;
import client.inventory.MaplePet;
import client.inventory.MapleRing;
import client.inventory.ModifyInventory;
import com.github.mrzhqiang.maplestory.config.ServerProperties;
import com.github.mrzhqiang.maplestory.di.Injectors;
import com.github.mrzhqiang.maplestory.domain.DAccount;
import com.github.mrzhqiang.maplestory.domain.DAccountInfo;
import com.github.mrzhqiang.maplestory.domain.DAchievement;
import com.github.mrzhqiang.maplestory.domain.DBossLog;
import com.github.mrzhqiang.maplestory.domain.DBuddy;
import com.github.mrzhqiang.maplestory.domain.DCharacter;
import com.github.mrzhqiang.maplestory.domain.DFameLog;
import com.github.mrzhqiang.maplestory.domain.DFishingJf;
import com.github.mrzhqiang.maplestory.domain.DHyPay;
import com.github.mrzhqiang.maplestory.domain.DIPBans;
import com.github.mrzhqiang.maplestory.domain.DInventorySlot;
import com.github.mrzhqiang.maplestory.domain.DKeyMap;
import com.github.mrzhqiang.maplestory.domain.DMountData;
import com.github.mrzhqiang.maplestory.domain.DNote;
import com.github.mrzhqiang.maplestory.domain.DOneTimeLog;
import com.github.mrzhqiang.maplestory.domain.DPrizeLog;
import com.github.mrzhqiang.maplestory.domain.DQuestInfo;
import com.github.mrzhqiang.maplestory.domain.DQuestStatus;
import com.github.mrzhqiang.maplestory.domain.DQuestStatusMob;
import com.github.mrzhqiang.maplestory.domain.DRegrockLocation;
import com.github.mrzhqiang.maplestory.domain.DSavedLocation;
import com.github.mrzhqiang.maplestory.domain.DSkill;
import com.github.mrzhqiang.maplestory.domain.DSkillCooldown;
import com.github.mrzhqiang.maplestory.domain.DSkillMacro;
import com.github.mrzhqiang.maplestory.domain.DTrockLocation;
import com.github.mrzhqiang.maplestory.domain.DWishList;
import com.github.mrzhqiang.maplestory.domain.Gender;
import com.github.mrzhqiang.maplestory.domain.LoginState;
import com.github.mrzhqiang.maplestory.domain.query.QDAccount;
import com.github.mrzhqiang.maplestory.domain.query.QDAccountInfo;
import com.github.mrzhqiang.maplestory.domain.query.QDAchievement;
import com.github.mrzhqiang.maplestory.domain.query.QDBossLog;
import com.github.mrzhqiang.maplestory.domain.query.QDBuddy;
import com.github.mrzhqiang.maplestory.domain.query.QDCharacter;
import com.github.mrzhqiang.maplestory.domain.query.QDFameLog;
import com.github.mrzhqiang.maplestory.domain.query.QDFishingJf;
import com.github.mrzhqiang.maplestory.domain.query.QDGuild;
import com.github.mrzhqiang.maplestory.domain.query.QDHyPay;
import com.github.mrzhqiang.maplestory.domain.query.QDInventorySlot;
import com.github.mrzhqiang.maplestory.domain.query.QDKeyMap;
import com.github.mrzhqiang.maplestory.domain.query.QDMountData;
import com.github.mrzhqiang.maplestory.domain.query.QDNote;
import com.github.mrzhqiang.maplestory.domain.query.QDOneTimeLog;
import com.github.mrzhqiang.maplestory.domain.query.QDPrizeLog;
import com.github.mrzhqiang.maplestory.domain.query.QDQuestInfo;
import com.github.mrzhqiang.maplestory.domain.query.QDQuestStatus;
import com.github.mrzhqiang.maplestory.domain.query.QDQuestStatusMob;
import com.github.mrzhqiang.maplestory.domain.query.QDRegrockLocation;
import com.github.mrzhqiang.maplestory.domain.query.QDSavedLocation;
import com.github.mrzhqiang.maplestory.domain.query.QDSkill;
import com.github.mrzhqiang.maplestory.domain.query.QDSkillCooldown;
import com.github.mrzhqiang.maplestory.domain.query.QDSkillMacro;
import com.github.mrzhqiang.maplestory.domain.query.QDTrockLocation;
import com.github.mrzhqiang.maplestory.domain.query.QDWishList;
import com.github.mrzhqiang.maplestory.timer.Timer;
import com.github.mrzhqiang.maplestory.util.Numbers;
import com.github.mrzhqiang.maplestory.wz.WzData;
import com.github.mrzhqiang.maplestory.wz.WzElement;
import com.github.mrzhqiang.maplestory.wz.WzFile;
import com.github.mrzhqiang.maplestory.wz.element.ImgdirElement;
import com.github.mrzhqiang.maplestory.wz.element.data.Vector;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import constants.GameConstants;
import constants.ServerConstants;
import handling.MaplePacket;
import handling.channel.ChannelServer;
import handling.login.LoginServer;
import handling.world.CharacterTransfer;
import handling.world.MapleMessenger;
import handling.world.MapleMessengerCharacter;
import handling.world.MapleParty;
import handling.world.MaplePartyCharacter;
import handling.world.PartyOperation;
import handling.world.PlayerBuffStorage;
import handling.world.PlayerBuffValueHolder;
import handling.world.World;
import handling.world.family.MapleFamily;
import handling.world.family.MapleFamilyBuff;
import handling.world.family.MapleFamilyBuff.MapleFamilyBuffEntry;
import handling.world.family.MapleFamilyCharacter;
import handling.world.guild.MapleGuild;
import handling.world.guild.MapleGuildCharacter;
import io.ebean.DB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scripting.EventInstanceManager;
import scripting.NPCScriptManager;
import server.CashShop;
import server.MapleCarnivalChallenge;
import server.MapleCarnivalParty;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.MaplePortal;
import server.MapleShop;
import server.MapleStatEffect;
import server.MapleStorage;
import server.MapleTrade;
import server.RandomRewards;
import server.Randomizer;
import server.life.MapleLifeFactory;
import server.life.MapleMonster;
import server.life.MobSkill;
import server.life.PlayerNPC;
import server.maps.AbstractAnimatedMapleMapObject;
import server.maps.Event_PyramidSubway;
import server.maps.FieldLimitType;
import server.maps.MapleDoor;
import server.maps.MapleDragon;
import server.maps.MapleFoothold;
import server.maps.MapleMap;
import server.maps.MapleMapEffect;
import server.maps.MapleMapFactory;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import server.maps.MapleSummon;
import server.maps.SavedLocationType;
import server.movement.LifeMovementFragment;
import server.quest.MapleQuest;
import server.shops.IMaplePlayerShop;
import tools.ConcurrentEnumMap;
import tools.FileoutputUtil;
import tools.MaplePacketCreator;
import tools.MockIOSession;
import tools.Pair;
import tools.packet.MTSCSPacket;
import tools.packet.MobPacket;
import tools.packet.MonsterCarnivalPacket;
import tools.packet.PetPacket;
import tools.packet.PlayerShopPacket;
import tools.packet.UIPacket;

import java.awt.*;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class MapleCharacter extends AbstractAnimatedMapleMapObject implements Serializable {

    private static final long serialVersionUID = 845748950829L;

    private static final Logger LOGGER = LoggerFactory.getLogger(MapleCharacter.class);

    /*
     * // SEA 102 final int[] array1 = {2, 3, 4, 5, 6, 7, 16, 17, 18,
     * 19, 23, 25, 26, 27, 31, 34, 37, 38, 41, 44, 45, 46, 50, 57, 59,
     * 60, 61, 62, 63, 64, 65, 8, 9, 24, 30}; final int[] array2 = {4,
     * 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 5, 5, 5, 4,
     * 5, 6, 6, 6, 6, 6, 6, 6, 4, 4, 4, 4}; final int[] array3 = {10,
     * 12, 13, 18, 6, 11, 8, 5, 0, 4, 1, 19, 14, 15, 3, 17, 9, 20, 22,
     * 50, 51, 52, 7, 53, 100, 101, 102, 103, 104, 105, 106, 16, 23, 24,
     * 2};
     */
    private static final int[] array1 = {//keymap key
            2, 3, 4, 5, 6, 7, 16, 17, 18, 19, 23, 25, 26, 27,
            29, 31, 34, 35, 37, 38, 40, 41, 43, 44, 45, 46, 48,
            50, 56, 57, 59, 60, 61, 62, 63, 64, 65};
    private static final int[] array2 = {// keymap type
            4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 5, 4, 4,
            4, 4, 4, 4, 4, 4, 5, 5, 4, 4, 4, 5, 5, 6, 6, 6, 6,
            6, 6, 6};
    private static final int[] array3 = {// keymap action
            10, 12, 13, 18, 24, 21, 8, 5, 0, 4, 1, 19, 14, 15,
            52, 2, 17, 11, 3, 20, 16, 23, 9, 50, 51, 6, 22, 7,
            53, 54, 100, 101, 102, 103, 104, 105, 106};

    public DCharacter character;

    private String name, chalktext, BlessOfFairy_Origin, charmessage;
    private long lastCombo, lastfametime, keydown_skill;
    private Gender gender;
    private int dojoRecord, gmLevel, initialSpawnPoint, skinColor, guildrank = 5, allianceRank = 5, world, fairyExp = 30, numClones, subcategory; // Make this a quest record, TODO : Transfer it somehow with the current data
    private int level, mulung_energy, combo, availableCP, totalCP, fame, hpApUsed, remainingAp;
    private int job;
    private long acash;
    private long maplepoints;
    private long points;
    private long vpoints;
    private int accountid, id, meso, exp, hair, face, mapid, bookCover, dojo, sg,
            guildid = 0, fallcounter = 0, chair, itemEffect, rank = 1, rankMove = 0, jobRank = 1, jobRankMove = 0, marriageId, marriageItemId = 0,
            currentrep, totalrep, coconutteam = 0, followid = 0, battleshipHP = 0,
            expression, constellation, blood, month, day, beans, beansNum, beansRange, prefix;
    private boolean canSetBeansNum;
    private Vector old = Vector.empty();
    private boolean smega, hidden, hasSummon = false;
    private int[] wishlist, rocks, savedLocations, regrocks, remainingSp = new int[10];
    private transient AtomicInteger inst;
    private transient List<LifeMovementFragment> lastres;
    private List<Integer> lastmonthfameids;
    private List<MapleDoor> doors;
    private List<MaplePet> pets;
    private transient WeakReference<MapleCharacter>[] clones;
    private transient Set<MapleMonster> controlled;
    private transient Set<MapleMapObject> visibleMapObjects;
    private transient ReentrantReadWriteLock visibleMapObjectsLock;
    private Map<MapleQuest, MapleQuestStatus> quests;
    private Map<Integer, String> questinfo;
    private Map<ISkill, SkillEntry> skills = new LinkedHashMap<>();
    private transient Map<MapleBuffStat, MapleBuffStatValueHolder> effects = new ConcurrentEnumMap<>(MapleBuffStat.class);
    private transient Map<Integer, MapleSummon> summons;
    private transient Map<Integer, MapleCoolDownValueHolder> coolDowns = new LinkedHashMap<>();
    private transient Map<MapleDisease, MapleDiseaseValueHolder> diseases = new ConcurrentEnumMap<>(MapleDisease.class);
    private List<MapleDisease> diseases2 = new ArrayList();
    private CashShop cs;
    private transient Deque<MapleCarnivalChallenge> pendingCarnivalRequests;
    private transient MapleCarnivalParty carnivalParty;
    private BuddyList buddylist;
    private MonsterBook monsterbook;
    private transient CheatTracker anticheat;
    private MapleClient client;
    private PlayerStats stats;
    private transient PlayerRandomStream CRand;
    private transient MapleMap map;
    private transient MapleShop shop;
    private transient MapleDragon dragon;
    private transient RockPaperScissors rps;
    private MapleStorage storage;
    private transient MapleTrade trade;
    private MapleMount mount;
    private List<Integer> finishedAchievements = new ArrayList<>();
    private MapleMessenger messenger;
    private byte[] petStore;
    private transient IMaplePlayerShop playerShop;
    private MapleParty party;
    private boolean invincible = false, canTalk = true, clone = false, followinitiator = false, followon = false;
    private MapleGuildCharacter mgc;
    private MapleFamilyCharacter mfc;
    private transient EventInstanceManager eventInstance;
    private MapleInventory[] inventory;
    private SkillMacro[] skillMacros = new SkillMacro[5];
    private MapleKeyLayout keylayout;
    private transient ScheduledFuture<?> beholderHealingSchedule, beholderBuffSchedule, BerserkSchedule,
            dragonBloodSchedule, fairySchedule, mapTimeLimitTask, fishing;
    private long nextConsume = 0, pqStartTime = 0;
    private transient Event_PyramidSubway pyramidSubway = null;
    private transient List<Integer> pendingExpiration = null, pendingSkills = null;
    private transient Map<Integer, Integer> movedMobs = new HashMap<>();
    private String teleportname = "";
    private int APQScore;
    private long lasttime = 0L;
    private long currenttime = 0L;
    private long deadtime = 1000L;
    private MapleCharacter chars;
    private long nengl = 0;
    private long nengls = 0;
    private static String[] ariantroomleader = new String[3]; // AriantPQ
    private static int[] ariantroomslot = new int[3]; // AriantPQ
    public int apprentice = 0, master = 0; // apprentice ID for master
    public boolean DebugMessage = false;
    public int ariantScore = 0;
    public long lastGainHM;
    private int touzhuNum;
    private int touzhuType;
    private int touzhuNX;
    private long 防止复制时间 = 2000L;

    private List<String> blockedPortals = new ArrayList<>();//传送点
    // private int linkMid;
    private transient Map<Integer, Integer> linkMid;
    private int skillzq = 0, bosslog = 0, grname = 0, jzname = 0, mrsjrw = 0, mrsgrw = 0, mrsbossrw = 0, mrfbrw = 0, hythd = 0, mrsgrwa = 0, mrsbossrwa = 0, mrfbrwa = 0, mrsgrws = 0, mrsbossrws = 0, mrfbrws = 0, mrsgrwas = 0, mrsbossrwas = 0, mrfbrwas = 0, ddj = 0, vip = 0, djjl = 0, qiandao = 0;

    private MapleCharacter(boolean channelServer) {
        setStance(0);
        setPosition(Vector.empty());

        MapleInventoryType[] types = MapleInventoryType.values();
        inventory = new MapleInventory[types.length];
        for (MapleInventoryType type : types) {
            inventory[type.ordinal()] = new MapleInventory(type, 100);
        }
        quests = new LinkedHashMap<>(); // Stupid erev quest.
        stats = new PlayerStats(this);
        Arrays.fill(remainingSp, 0);
        if (channelServer) {
            lastMoveItemTime = 0;
            lastCheckPeriodTime = 0;
            lastQuestTime = 0;
            lastHPTime = 0;
            lastMPTime = 0;
            lastCombo = 0;
            mulung_energy = 0;
            combo = 0;
            keydown_skill = 0;
            smega = true;
            petStore = new byte[3];
            Arrays.fill(petStore, (byte) -1);
            wishlist = new int[10];
            rocks = new int[10];
            regrocks = new int[5];
            clones = new WeakReference[25];
            for (int i = 0; i < clones.length; i++) {
                clones[i] = new WeakReference<>(null);
            }
            inst = new AtomicInteger();
            inst.set(0); // 1 = NPC/ Quest, 2 = Duey, 3 = Hired Merch store, 4 = Storage
            keylayout = new MapleKeyLayout();
            doors = new ArrayList<>();
            controlled = new LinkedHashSet<>();
            summons = new LinkedHashMap<>();
            visibleMapObjects = new LinkedHashSet<>();
            visibleMapObjectsLock = new ReentrantReadWriteLock();
            pendingCarnivalRequests = new LinkedList<>();
            linkMid = new HashMap<>();
            savedLocations = new int[SavedLocationType.values().length];
            for (int i = 0; i < SavedLocationType.values().length; i++) {
                savedLocations[i] = -1;
            }
            questinfo = new LinkedHashMap<>();
            anticheat = new CheatTracker(this);
            pets = new ArrayList<>();
        }
    }

    public static MapleCharacter getDefault(MapleClient client, final int type) {
        MapleCharacter ret = new MapleCharacter(false);
        ret.client = client;
        ret.map = null;
        ret.exp = 0;
        ret.gmLevel = 0;
        ret.job = (short) (type == 1 ? 0 : (type == 0 ? 1000 : (type == 3 ? 2001 : (type == 4 ? 3000 : 2000))));
        ret.beans = 0;
        ret.meso = 0;
        ret.level = 1;
        ret.remainingAp = 0;
        ret.fame = 0;
        ret.accountid = client.getAccID();
        ret.buddylist = new BuddyList((byte) 20);

        ret.stats.str = 12;
        ret.stats.dex = 5;
        ret.stats.int_ = 4;
        ret.stats.luk = 4;
        ret.stats.maxhp = 50;
        ret.stats.hp = 50;
        ret.stats.maxmp = 50;
        ret.stats.mp = 50;
        ret.prefix = 0;

        Optional<DAccount> optional = new QDAccount().id.eq(ret.accountid).findOneOrEmpty();
        if (optional.isPresent()) {
            DAccount account = optional.get();

//            ret.client.setAccountName(account.name);
            ret.acash = account.getCash();
            ret.maplepoints = account.getmPoints();
            ret.points = account.getPoints();
            ret.vpoints = account.getvPoints();
            ret.lastGainHM = account.getLastGainHm();
        }
        return ret;
    }

    public static MapleCharacter ReconstructChr(final CharacterTransfer ct, final MapleClient client, final boolean isChannel) {
        MapleCharacter ret = new MapleCharacter(true); // Always true, it's change channel
        ret.client = client;
        if (!isChannel) {
            ret.client.setChannel(ct.channel);
        }

        ret.mount_id = ct.mount_id;
        ret.DebugMessage = ct.DebugMessage;
        ret.id = ct.characterid;
        ret.name = ct.name;
        ret.level = ct.level;
        ret.fame = ct.fame;

        ret.CRand = new PlayerRandomStream();

        ret.stats.str = ct.str;
        ret.stats.dex = ct.dex;
        ret.stats.int_ = ct.int_;
        ret.stats.luk = ct.luk;
        ret.stats.maxhp = ct.maxhp;
        ret.stats.maxmp = ct.maxmp;
        ret.stats.hp = ct.hp;
        ret.stats.mp = ct.mp;

        ret.chalktext = ct.chalkboard;
        ret.exp = ct.exp;
        ret.hpApUsed = ct.hpApUsed;
        ret.remainingSp = ct.remainingSp;
        ret.remainingAp = ct.remainingAp;
        ret.beans = ct.beans;
        ret.meso = ct.meso;
        ret.gmLevel = ct.gmLevel;
        ret.skinColor = ct.skinColor;
        ret.gender = ct.gender;
        ret.job = ct.job;
        ret.hair = ct.hair;
        ret.face = ct.face;
        ret.accountid = ct.accountid;
        ret.mapid = ct.mapid;
        ret.initialSpawnPoint = ct.initialSpawnPoint;
        ret.world = ct.world;
        ret.bookCover = ct.mBookCover;
        ret.dojo = ct.dojo;
        ret.dojoRecord = ct.dojoRecord;
        ret.guildid = ct.guildid;
        ret.guildrank = ct.guildrank;
        ret.allianceRank = ct.alliancerank;
        ret.points = ct.points;
        ret.vpoints = ct.vpoints;
        ret.fairyExp = ct.fairyExp;
        ret.marriageId = ct.marriageId;
        ret.currentrep = ct.currentrep;
        ret.totalrep = ct.totalrep;
        ret.charmessage = ct.charmessage;
        ret.expression = ct.expression;
        ret.constellation = ct.constellation;
        ret.skillzq = ct.skillzq;
        ret.bosslog = ct.bosslog;
        ret.grname = ct.grname;
        ret.djjl = ct.djjl;
        ret.qiandao = ct.qiandao;
        ret.jzname = ct.jzname;
        ret.mrfbrw = ct.mrfbrw;
        ret.mrsbossrw = ct.mrsbossrw;
        ret.mrsgrw = ct.mrsgrw;
        ret.mrfbrwa = ct.mrfbrwa;
        ret.mrsbossrwa = ct.mrsbossrwa;
        ret.mrsgrwa = ct.mrsgrwa;
        ret.mrfbrws = ct.mrfbrws;
        ret.mrsbossrws = ct.mrsbossrws;
        ret.mrsgrws = ct.mrsgrws;
        ret.mrfbrwas = ct.mrfbrwas;
        ret.mrsbossrwas = ct.mrsbossrwas;
        ret.mrsgrwas = ct.mrsgrwas;
        ret.mrsjrw = ct.mrsjrw;
        ret.hythd = ct.hythd;
        ret.ddj = ct.ddj;
        ret.vip = ct.vip;
        ret.blood = ct.blood;
        ret.month = ct.month;
        ret.day = ct.day;
        ret.makeMFC(ct.familyid, ct.seniorid, ct.junior1, ct.junior2);
        if (ret.guildid > 0) {
            ret.mgc = new MapleGuildCharacter(ret.character, client.getChannel(), true);
        }
        ret.buddylist = new BuddyList(ct.buddysize);
        ret.subcategory = ct.subcategory;
        ret.prefix = ct.prefix;

        if (isChannel) {
            MapleMapFactory mapFactory = ChannelServer.getInstance(client.getChannel()).getMapFactory();
            ret.map = mapFactory.getMap(ret.mapid);
            if (ret.map == null) { //char is on a map that doesn't exist warp it to henesys
                ret.map = mapFactory.getMap(100000000);
            } else if (ret.map.getForcedReturnId() != 999999999) {
                ret.map = ret.map.getForcedReturnMap();
            }
            MaplePortal portal = ret.map.getPortal(ret.initialSpawnPoint);
            if (portal == null) {
                portal = ret.map.getPortal(0); // char is on a spawnpoint that doesn't exist - select the first spawnpoint instead
                ret.initialSpawnPoint = 0;
            }
            ret.setPosition(portal.getPosition());

            final int messengerid = ct.messengerid;
            if (messengerid > 0) {
                ret.messenger = World.Messenger.getMessenger(messengerid);
            }
        } else {

            ret.messenger = null;
        }
        int partyid = ct.partyid;
        if (partyid >= 0) {
            MapleParty party = World.Party.getParty(partyid);
            if (party != null && party.getMemberById(ret.id) != null) {
                ret.party = party;
            }
        }

        MapleQuestStatus queststatus;
        MapleQuestStatus queststatus_from;
        MapleQuest quest;
        for (final Map.Entry<Integer, Object> qs : ct.Quest.entrySet()) {
            quest = MapleQuest.getInstance(qs.getKey());
            queststatus_from = (MapleQuestStatus) qs.getValue();

            queststatus = new MapleQuestStatus(quest, queststatus_from.getStatus());
            queststatus.setForfeited(queststatus_from.getForfeited());
            queststatus.setCustomData(queststatus_from.getCustomData());
            queststatus.setCompletionTime(queststatus_from.getCompletionTime());

            if (queststatus_from.getMobKills() != null) {
                for (final Map.Entry<Integer, Integer> mobkills : queststatus_from.getMobKills().entrySet()) {
                    queststatus.setMobKills(mobkills.getKey(), mobkills.getValue());
                }
            }
            ret.quests.put(quest, queststatus);
        }
        for (final Map.Entry<Integer, SkillEntry> qs : ct.Skills.entrySet()) {
            ret.skills.put(SkillFactory.getSkill(qs.getKey()), qs.getValue());
        }
        for (final Integer zz : ct.finishedAchievements) {
            ret.finishedAchievements.add(zz);
        }
        ret.monsterbook = new MonsterBook(ct.mbook);
        ret.inventory = (MapleInventory[]) ct.inventorys;
        ret.BlessOfFairy_Origin = ct.BlessOfFairy;
        ret.skillMacros = (SkillMacro[]) ct.skillmacro;
        ret.petStore = ct.petStore;
        ret.keylayout = new MapleKeyLayout(ct.keymap);
        ret.questinfo = ct.InfoQuest;
        ret.savedLocations = ct.savedlocation;
        ret.wishlist = ct.wishlist;
        ret.rocks = ct.rocks;
        ret.regrocks = ct.regrocks;
        ret.buddylist.loadFromTransfer(ct.buddies);
        // ret.lastfametime
        // ret.lastmonthfameids
        ret.keydown_skill = 0; // Keydown skill can't be brought over
        ret.lastfametime = ct.lastfametime;
        ret.lastmonthfameids = ct.famedcharacters;
        ret.storage = (MapleStorage) ct.storage;
        ret.cs = (CashShop) ct.cs;
//        client.setAccountName(ct.accountname);
        ret.acash = ct.ACash;
        ret.lastGainHM = ct.lastGainHM;
        ret.maplepoints = ct.MaplePoints;
        ret.numClones = ct.clonez;
        ret.mount = new MapleMount(ret, ct.mount_itemid, GameConstants.isKOC(ret.job) ? 10001004 : (GameConstants.isAran(ret.job) ? 20001004 : (GameConstants.isEvan(ret.job) ? 20011004 : 1004)), ct.mount_Fatigue, ct.mount_level, ct.mount_exp);

        ret.stats.recalcLocalStats(true);

        return ret;
    }

    public static MapleCharacter loadCharFromDB(int charid, MapleClient client, boolean channelserver) {
        MapleCharacter ret = new MapleCharacter(channelserver);
        ret.client = client;
        ret.id = charid;

        DCharacter one = new QDCharacter().id.eq(charid).findOne();
        Preconditions.checkState(one != null,
                "Loading the Char %s Failed (char not found)", charid);
        ret.setCharacter(one);

        if (channelserver) {
            MapleMapFactory mapFactory = ChannelServer.getInstance(client.getChannel()).getMapFactory();//又爆这句错误
            ret.map = mapFactory.getMap(ret.mapid);
            if (ret.map == null) { //char is on a map that doesn't exist warp it to henesys
                ret.map = mapFactory.getMap(100000000);
            }
            MaplePortal portal = ret.map.getPortal(ret.initialSpawnPoint);
            if (portal == null) {
                portal = ret.map.getPortal(0); // char is on a spawnpoint that doesn't exist - select the first spawnpoint instead
                ret.initialSpawnPoint = 0;
            }
            ret.setPosition(portal.getPosition());

            int partyid = one.getParty();
            if (partyid >= 0) {
                MapleParty party = World.Party.getParty(partyid);
                if (party != null && party.getMemberById(ret.id) != null) {
                    ret.party = party;
                }
            }
            ret.bookCover = one.getMonsterBookCover();
            ret.dojo = one.getDojoPts();
            ret.dojoRecord = one.getDojoRecord();
            final String[] pets = one.getPets().split(",");
            for (int i = 0; i < ret.petStore.length; i++) {
                ret.petStore[i] = Byte.parseByte(pets[i]);
            }
            new QDAchievement().account.eq(one.getAccount())
                    .findEach(it -> ret.finishedAchievements.add(it.getAchievement().getAchievementId()));
        }
        boolean compensate_previousEvans = false;
        for (DQuestStatus status : new QDQuestStatus().character.eq(one).findList()) {
            int id = status.getQuest();
            if (id == 170000) {
                compensate_previousEvans = true;
            }
            MapleQuest q = MapleQuest.getInstance(id);
            MapleQuestStatus questStatus = new MapleQuestStatus(q, status.getStatus());
            long cTime = status.getTime();
            if (cTime > -1) {
                questStatus.setCompletionTime(cTime * 1000);
            }
            questStatus.setForfeited(status.getForfeited());
            questStatus.setCustomData(status.getCustomData());
            ret.quests.put(q, questStatus);
            for (DQuestStatusMob mob : new QDQuestStatusMob().questStatus.eq(status).findList()) {
                questStatus.setMobKills(mob.getMob(), mob.getCount());
            }
        }
        if (channelserver) {
            ret.CRand = new PlayerRandomStream();
            ret.monsterbook = MonsterBook.loadCards(charid);

            Optional<DInventorySlot> optional = new QDInventorySlot().character.eq(one).findOneOrEmpty();
            Preconditions.checkState(optional.isPresent(), "No Inventory slot column found in SQL. [inventoryslot]*********************");

            DInventorySlot slot = optional.get();

            ret.getInventory(MapleInventoryType.EQUIP).setSlotLimit(slot.getEquip());
            ret.getInventory(MapleInventoryType.USE).setSlotLimit(slot.getUse());
            ret.getInventory(MapleInventoryType.SETUP).setSlotLimit(slot.getSetup());
            ret.getInventory(MapleInventoryType.ETC).setSlotLimit(slot.getEtc());
            ret.getInventory(MapleInventoryType.CASH).setSlotLimit(slot.getCash());

            for (Pair<IItem, MapleInventoryType> mit : ItemLoader.loadItems(0, true, charid).values()) {
                ret.getInventory(mit.getRight()).addFromDB(mit.getLeft());
                if (mit.getLeft().getPet() != null) {
                    ret.pets.add(mit.getLeft().getPet());
                }
            }

            DAccount account = new QDAccount().id.eq(ret.accountid).findOne();
            if (account != null) {
//                ret.getClient().setAccountName(account.name);
                ret.lastGainHM = account.getLastGainHm();
                ret.acash = account.getCash();
                ret.maplepoints = account.getmPoints();
                ret.points = account.getPoints();
                ret.vpoints = account.getvPoints();

                account.setLastLogin(LocalDateTime.now());
                account.save();
            }

            List<DQuestInfo> infos = new QDQuestInfo().character.eq(one).findList();
            for (DQuestInfo info : infos) {
                ret.questinfo.put(info.getQuest(), info.getCustomData());
            }

            List<DSkill> skills = new QDSkill().character.eq(one).findList();
            ISkill skil;
            for (DSkill skill : skills) {
                skil = SkillFactory.getSkill(skill.getSkillId());
                if (skil != null && GameConstants.isApplicableSkill(skill.getSkillId())) {
                    ret.skills.put(skil, new SkillEntry(skill.getSkillLevel().byteValue(), skill.getMasterLevel().byteValue(), skill.getExpiration()));
                } else if (skil == null) { //doesnt. exist. e.g. bb
                    ret.remainingSp[GameConstants.getSkillBookForSkill(skill.getSkillId())] += skill.getSkillLevel();
                }
            }

            ret.expirationTask(false); //do it now

            // Bless of Fairy handling
            List<DCharacter> characters = new QDCharacter().account.eq(account).orderBy().level.desc().findList();
            byte maxlevel_ = 0;
            for (DCharacter character : characters) {
                if (character.getId() != charid) { // Not this character
                    byte maxlevel = (byte) (character.getLevel() / 10);

                    if (maxlevel > 20) {
                        maxlevel = 20;
                    }
                    if (maxlevel > maxlevel_) {
                        maxlevel_ = maxlevel;
                        ret.BlessOfFairy_Origin = character.getName();
                    }

                } else if (charid < 17000 && !compensate_previousEvans && ret.job >= 2200 && ret.job <= 2218) { //compensate, watch max charid
                    for (int i = 0; i <= GameConstants.getSkillBook(ret.job); i++) {
                        ret.remainingSp[i] += 2; //2 that they missed. gg
                    }
                    ret.setQuestAdd(MapleQuest.getInstance(170000), (byte) 0, null); //set it so never again
                }
            }
            ret.skills.put(SkillFactory.getSkill(GameConstants.getBOF_ForJob(ret.job)), new SkillEntry(maxlevel_, (byte) 0, -1));
            // END

            List<DSkillMacro> macros = new QDSkillMacro().character.eq(one).findList();
            int position;
            for (DSkillMacro macro : macros) {
                position = macro.getPosition();
                SkillMacro skillMacro = new SkillMacro(macro.getSkill1(), macro.getSkill2(),
                        macro.getSkill3(), macro.getName(), macro.getShout(), position);
                ret.skillMacros[position] = skillMacro;
            }

            List<DKeyMap> maps = new QDKeyMap().character.eq(one).findList();
            Map<Integer, Pair<Integer, Integer>> keyb = ret.keylayout.Layout();
            for (DKeyMap map : maps) {
                keyb.put(map.getKey(), new Pair<>(map.getType(), map.getAction()));
            }

            List<DSavedLocation> locations = new QDSavedLocation().character.eq(one).findList();
            for (DSavedLocation location : locations) {
                ret.savedLocations[location.getLocationType()] = location.getMap();
            }

            // DATEDIFF(NOW(),`when`) < 30 表示：when 距离现在的时间在 30 天之内（不超过），因此换成日期就是 when > now - 30
            List<DFameLog> logs = new QDFameLog()
                    .character.eq(one)
                    .and()
                    .when.gt(LocalDateTime.now().minusDays(30))
                    .findList();
            ret.lastfametime = 0;
            ret.lastmonthfameids = new ArrayList<>(31);
            for (DFameLog log : logs) {
                ret.lastfametime = Math.max(ret.lastfametime, log.getWhen().toInstant(ZoneOffset.UTC).toEpochMilli());
                ret.lastmonthfameids.add(log.getTo().getId());
            }

            ret.buddylist.loadFromDb(one);
            ret.storage = MapleStorage.loadStorage(ret.accountid);
            ret.cs = new CashShop(ret.accountid, charid, ret.getJob());

            List<DWishList> lists = new QDWishList().character.eq(one).findList();
            int i = 0;
            for (DWishList list : lists) {
                ret.wishlist[i] = list.getSn();
                i++;
            }
            while (i < 10) {
                ret.wishlist[i] = 0;
                i++;
            }

            List<DTrockLocation> locationList = new QDTrockLocation().character.eq(one).findList();
            int r = 0;
            for (DTrockLocation location : locationList) {
                ret.rocks[r] = location.getMapId();
                r++;
            }
            while (r < 10) {
                ret.rocks[r] = 999999999;
                r++;
            }

            List<DRegrockLocation> regrockLocations = new QDRegrockLocation().character.eq(one).findList();
            r = 0;
            for (DRegrockLocation regrockLocation : regrockLocations) {
                ret.regrocks[r] = regrockLocation.getMapId();
                r++;
            }
            while (r < 5) {
                ret.regrocks[r] = 999999999;
                r++;
            }

            DMountData mountData = new QDMountData()
                    .character.eq(one)
                    .findOneOrEmpty()
                    .orElseThrow(() -> new RuntimeException("No mount data found on SQL column"));
            IItem mount = ret.getInventory(MapleInventoryType.EQUIPPED).getItem((byte) -18);
            ret.mount = new MapleMount(ret,
                    mount != null ? mount.getItemId() : 0,
                    ret.job > 1000 && ret.job < 2000 ? 10001004 : (ret.job >= 2000 ? (ret.job == 2001 || ret.job >= 2200 ? 20011004 : (ret.job >= 3000 ? 30001004 : 20001004)) : 1004),
                    mountData.getFatigue(), mountData.getLevel(), mountData.getExp());
            ret.stats.recalcLocalStats(true);
        } else { // Not channel server
            for (Pair<IItem, MapleInventoryType> mit : ItemLoader.loadItems(0, true, charid).values()) {
                ret.getInventory(mit.getRight()).addFromDB(mit.getLeft());
            }
        }
        return ret;
    }

    private void setCharacter(DCharacter one) {
        this.character = one;
        this.mount_id = one.getMountid();
        this.name = one.getName();
        this.level = one.getLevel();
        this.fame = one.getFame();
        this.sg = one.getSg();
        this.stats.str = one.getStr();
        this.stats.dex = one.getDex();
        this.stats.int_ = one.getIntelligence();
        this.stats.luk = one.getLuk();
        this.stats.maxhp = one.getMaxHp();
        this.stats.maxmp = one.getMaxMp();
        this.stats.hp = one.getHp();
        this.stats.mp = one.getMp();

        this.exp = one.getExp();
        this.hpApUsed = one.getHpApUsed().shortValue();
        String[] sp = one.getSp().split(",");
        for (int i = 0; i < this.remainingSp.length; i++) {
            this.remainingSp[i] = Integer.parseInt(sp[i]);
        }
        this.remainingAp = one.getAp().byteValue();
        this.beans = one.getBeans();
        this.meso = one.getMeso();
        this.gmLevel = one.getGm();
        this.skinColor = one.getSkinColor();
        this.gender = one.getGender();
        this.job = one.getJob();
        this.hair = one.getHair();
        this.face = one.getFace();
        this.accountid = one.getAccount().getId();
        this.mapid = one.getMap();
        this.initialSpawnPoint = one.getSpawnPoint();
        this.world = one.getWorld();
        this.guildid = one.getGuild() == null ? 0 : one.getGuild().getId();
        this.guildrank = one.getGuildRank();
        this.allianceRank = one.getAllianceRank();
        this.currentrep = one.getCurrentRep();
        this.totalrep = one.getTotalRep();
        if (one.getFamily() != null && one.getSenior() != null && one.getJunior1() != null && one.getJunior2() != null) {
            this.makeMFC(one.getFamily().getId(), one.getSenior().getId(), one.getJunior1().getId(), one.getJunior2().getId());
        }
        if (this.guildid > 0) {
            this.mgc = new MapleGuildCharacter(this.character, client.getChannel(), true);
        }
        this.buddylist = new BuddyList(one.getBuddyCapacity());
        this.subcategory = one.getSubcategory();
        this.mount = new MapleMount(this, 0, this.job > 1000 && this.job < 2000 ? 10001004 : (this.job >= 2000 ? (this.job == 2001 || (this.job >= 2200 && this.job <= 2218) ? 20011004 : (this.job >= 3000 ? 30001004 : 20001004)) : 1004), (byte) 0, (byte) 1, 0);
        this.rank = one.getRank();
        this.rankMove = one.getMoveRank();
        this.jobRank = one.getJobRank();
        this.jobRankMove = one.getJobMoveRank();
        if (one.getMarriage() != null) {
            this.marriageId = one.getMarriage().getId();
        }
        this.charmessage = one.getCharMessage();
        this.expression = one.getExpression();
        this.constellation = one.getConstellation();
        this.skillzq = one.getSkillzq();
        this.bosslog = one.getBosslog();
        this.grname = one.getGrname();
        this.djjl = one.getDjjl();
        this.qiandao = one.getQiandao();
        this.jzname = one.getJzname();
        this.mrfbrw = one.getMrfbrw();
        this.mrsbossrw = one.getMrsbossrw();
        this.mrsgrw = one.getMrsgrw();
        this.mrfbrws = one.getMrfbrws();
        this.mrsbossrws = one.getMrsbossrws();
        this.mrsgrws = one.getMrsgrws();
        this.mrsjrw = one.getMrsjrw();
        this.hythd = one.getHythd();
        this.mrfbrwa = one.getMrfbrwa();
        this.mrsbossrwa = one.getMrsbossrwa();
        this.mrsgrwa = one.getMrsgrwa();
        this.mrfbrwas = one.getMrfbrwas();
        this.mrsbossrwas = one.getMrsbossrwas();
        this.mrsgrwas = one.getMrsgrwas();
        this.ddj = one.getDdj();
        this.vip = one.getVip();
        this.blood = one.getBlood();
        this.month = one.getMonth();
        this.day = one.getDay();
        this.prefix = one.getPrefix().intValue();
    }

    public static void saveNewCharToDB(MapleCharacter chr, int type, boolean db) {
        DAccount account = new QDAccount().id.eq(chr.accountid).findOne();
        DCharacter character = new DCharacter(account);
        character.setAccount(account);
        character.setName(chr.name);
        character.setLevel(1);
        character.setFame(0);
        character.setStr(chr.stats.str);
        character.setDex(chr.stats.dex);
        character.setLuk(chr.stats.luk);
        character.setIntelligence(chr.stats.int_);
        character.setExp(0);
        character.setHp(chr.stats.hp);
        character.setMp(chr.stats.mp);
        character.setMaxHp(chr.stats.maxhp);
        character.setMaxMp(chr.stats.maxmp);
        character.setSp("0,0,0,0,0,0,0,0,0,0");
        character.setAp(0);
        character.setGm(chr.getClient().gm ? 5 : 0);
        character.setSkinColor(chr.skinColor);
        character.setGender(chr.gender);
        character.setJob(chr.job);
        character.setHair(chr.hair);
        character.setFace(chr.face);
        character.setMap(type == 1 ? 0 : (type == 0 ? 130030000 : (type == 2 ? 914000000 : 910000000)));
        character.setMeso(chr.meso);
        character.setHpApUsed(0);
        character.setSpawnPoint(0);
        character.setParty(-1);
        character.setBuddyCapacity(chr.buddylist.getCapacity());
        character.setMonsterBookCover(0);
        character.setDojoPts(0);
        character.setDojoRecord(0);
        character.setPets("-1,-1,-1");
        character.setSubcategory(0);
        character.setMarriage(null);
        character.setCurrentRep(0);
        character.setTotalRep(0);
        character.setPrefix(BigDecimal.valueOf(chr.prefix));
        character.setDjjl(chr.djjl);
        character.setQiandao(chr.qiandao);
        character.setWorld(chr.world);
        character.setMountid(chr.mount_id);
        character.setSg(chr.sg);
        chr.character = character;
        character.save();

        chr.quests.forEach((mapleQuest, mapleQuestStatus) -> {
            DQuestStatus questStatus = new DQuestStatus();
            questStatus.setCharacter(character);
            questStatus.setQuest(mapleQuestStatus.getQuest().getId());
            questStatus.setStatus(mapleQuestStatus.getStatus());
            questStatus.setTime((int) (mapleQuestStatus.getCompletionTime() / 1000));
            questStatus.setForfeited(mapleQuestStatus.getForfeited());
            questStatus.setCustomData(mapleQuestStatus.getCustomData());
            questStatus.save();

            if (mapleQuestStatus.hasMobKills()) {
                mapleQuestStatus.getMobKills().forEach((integer, mob) -> {
                    DQuestStatusMob statusMob = new DQuestStatusMob();
                    statusMob.setQuestStatus(questStatus);
                    statusMob.setMob(mob);
                    statusMob.setCount(mapleQuestStatus.getMobKills(mob));
                    statusMob.save();
                });
            }
        });

        DInventorySlot inventorySlot = new DInventorySlot();
        inventorySlot.setCharacter(character);
        inventorySlot.setEquip(32);
        inventorySlot.setUse(32);
        inventorySlot.setSetup(32);
        inventorySlot.setEtc(32);
        inventorySlot.setCash(60);
        inventorySlot.save();

        DMountData mountData = new DMountData();
        mountData.setCharacter(character);
        mountData.setLevel(1);
        mountData.setExp(0);
        mountData.setFatigue(0);
        mountData.save();

        // todo 优化代码
        List<Pair<IItem, MapleInventoryType>> listing = new ArrayList<>();
        for (final MapleInventory iv : chr.inventory) {
            for (final IItem item : iv.list()) {
                listing.add(new Pair<>(item, iv.getType()));
            }
        }
        ItemLoader.saveItems(listing, character);

        for (int i = 0; i < array1.length; i++) {
            DKeyMap keyMap = new DKeyMap();
            keyMap.setCharacter(character);
            keyMap.setKey(array1[i]);
            keyMap.setType(array2[i]);
            keyMap.setAction(array3[i]);
            keyMap.save();
        }
    }

    public void saveToDB(boolean dc, boolean fromcs) {
        if (isClone()) {
            return;
        }

        if (character.getHp() < 1) {
            character.setHp(50);
        }
        character.setSp(Arrays.stream(remainingSp)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(",")));
        if (!fromcs && map != null) {
            if (map.getForcedReturnId() != 999999999) {
                character.setMap(map.getForcedReturnId());
            } else {
                character.setMap(stats.getHp() < 1 ? map.getReturnMapId() : map.getId());
            }
        } else {
            character.setMap(mapid);
        }
        if (map == null) {
            character.setSpawnPoint(0);
        } else {
            MaplePortal closest = map.findClosestSpawnpoint(getPosition());
            character.setSpawnPoint(closest != null ? closest.getId() : 0);
        }
        character.setParty(party != null ? party.getId() : -1);
        character.setBuddyCapacity(buddylist.getCapacity());
        StringBuilder petz = new StringBuilder();
        int petLength = 0;
        for (MaplePet pet : pets) {
            pet.saveToDb();
            if (pet.getSummoned()) {
                petz.append(pet.getInventoryPosition());
                petz.append(",");
                petLength++;
            }
        }
        while (petLength < 3) {
            petz.append("-1,");
            petLength++;
        }
        String petstring = petz.toString();
        character.setPets(petstring.substring(0, petstring.length() - 1));
        character.save();

        new QDSkillMacro().character.eq(character).delete();
        for (int i = 0; i < 5; i++) {
            SkillMacro macro = skillMacros[i];
            if (macro != null) {
                DSkillMacro skillMacro = new DSkillMacro();
                skillMacro.setCharacter(character);
                skillMacro.setSkill1(macro.getSkill1());
                skillMacro.setSkill2(macro.getSkill2());
                skillMacro.setSkill3(macro.getSkill3());
                skillMacro.setName(macro.getName());
                skillMacro.setShout(macro.getShout());
                skillMacro.setPosition(i);
            }
        }

        new QDInventorySlot().character.eq(character).delete();

        DInventorySlot inventorySlot = new DInventorySlot();
        inventorySlot.setCharacter(character);
        inventorySlot.setEquip(getInventory(MapleInventoryType.EQUIP).getSlotLimit());
        inventorySlot.setUse(getInventory(MapleInventoryType.USE).getSlotLimit());
        inventorySlot.setSetup(getInventory(MapleInventoryType.SETUP).getSlotLimit());
        inventorySlot.setEtc(getInventory(MapleInventoryType.ETC).getSlotLimit());
        inventorySlot.setCash(getInventory(MapleInventoryType.CASH).getSlotLimit());
        inventorySlot.save();

        saveInventory(character);

        new QDQuestInfo().character.eq(character).delete();

        for (final Entry<Integer, String> q : questinfo.entrySet()) {
            DQuestInfo questInfo = new DQuestInfo();
            questInfo.setCharacter(character);
            questInfo.setQuest(q.getKey());
            questInfo.setCustomData(q.getValue());
            questInfo.save();
        }

        new QDQuestStatus().character.eq(character).delete();
        for (MapleQuestStatus q : quests.values()) {
            if (q.getQuest() == null||q.getQuest().getId() < 0){
                LOGGER.error("保存任務失敗:{}"+q);
                continue;
            }
            DQuestStatus questStatus = new DQuestStatus();
            questStatus.setCharacter(character);
            questStatus.setQuest(q.getQuest().getId());
            questStatus.setStatus(q.getStatus());
            questStatus.setTime((int) (q.getCompletionTime() / 1000));
            questStatus.setForfeited(q.getForfeited());
            questStatus.setCustomData(q.getCustomData());
            questStatus.save();

            if (q.hasMobKills()) {
                for (int mob : q.getMobKills().keySet()) {
                    DQuestStatusMob questStatusMob = new DQuestStatusMob();
                    questStatusMob.setQuestStatus(questStatus);
                    questStatusMob.setMob(mob);
                    questStatusMob.setCount(q.getMobKills(mob));
                    questStatusMob.save();
                }
            }
        }

        new QDSkill().character.eq(character).delete();

        for (Entry<ISkill, SkillEntry> entryEntry : skills.entrySet()) {
            if (GameConstants.isApplicableSkill(entryEntry.getKey().getId())) { //do not save additional skills
                DSkill skill = new DSkill();
                skill.setCharacter(character);
                skill.setSkillId(entryEntry.getKey().getId());
                skill.setSkillLevel(entryEntry.getValue().skillevel);
                skill.setMasterLevel(entryEntry.getValue().masterlevel);
                skill.setExpiration(entryEntry.getValue().expiration);
                skill.save();
            }
        }

        List<MapleCoolDownValueHolder> cd = getCooldowns();
        if (dc && cd.size() > 0) {
            for (final MapleCoolDownValueHolder cooling : cd) {
                DSkillCooldown skillCooldown = new DSkillCooldown();
                skillCooldown.setSkillId(cooling.skillId);
                skillCooldown.setStartTime(cooling.startTime);
                skillCooldown.setLength(cooling.length);
                skillCooldown.save();
            }
        }

        new QDSavedLocation().character.eq(character).delete();
        for (SavedLocationType savedLocationType : SavedLocationType.values()) {
            if (savedLocations[savedLocationType.getValue()] != -1) {
                DSavedLocation savedLocation = new DSavedLocation();
                savedLocation.setCharacter(character);
                savedLocation.setLocationType(savedLocationType.getValue());
                savedLocation.setMap(savedLocations[savedLocationType.getValue()]);
                savedLocation.save();
            }
        }

        new QDAchievement().account.eq(character.getAccount()).delete();

        for (Integer achid : finishedAchievements) {
            DAchievement achievement = new DAchievement();
            achievement.setAccount(character.getAccount());
            achievement.getAchievement().setCharId(character.getId());
            achievement.getAchievement().setAchievementId(achid);
            achievement.save();
        }

        /*
         * deleteWhereCharacterId(con, "DELETE FROM buddies WHERE characterid = ? AND pending = 0");
         * ps = con.prepareStatement("INSERT INTO buddies (characterid, `buddyid`, `pending`) VALUES (?, ?, 0)");
         * ps.setInt(1, id);
         * for (BuddylistEntry entry : buddylist.getBuddies()) {
         *      if (entry.isVisible()) {
         *          ps.setInt(2, entry.getCharacterId());
         *          ps.execute();
         *      }
         * }
         * ps.close();
         */
        // if (buddylist.changed()) {
        new QDBuddy().owner.eq(character).delete();
        for (BuddyEntry entry : buddylist.getBuddies()) {
            if (entry != null) {
                DBuddy buddy = new DBuddy();
                buddy.setBuddies(DB.reference(DCharacter.class, entry.getCharacterId()));
                buddy.setPending(!entry.isVisible());
                buddy.save();
            }
        }

        new QDAccount().id.eq(client.getAccID())
                .asUpdate()
                .set("cash", acash)
                .set("mPoints", maplepoints)
                .set("points", points)
                .set("vPoints", vpoints)
                .set("lastGainHm", lastGainHM)
                .update();

        if (storage != null) {
            storage.saveToDB(character);
        }

        if (cs != null) {
            try {
                cs.save(character);
                PlayerNPC.updateByCharId(this);
                keylayout.saveKeys(id);
                mount.saveMount(id);
                monsterbook.saveCards(id);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        new QDWishList().character.eq(character).delete();
        for (int i = 0; i < getWishlistSize(); i++) {
            DWishList dWishList = new DWishList();
            dWishList.setCharacter(character);
            dWishList.setSn(wishlist[i]);
            dWishList.save();
        }

        new QDTrockLocation().character.eq(character).delete();
        for (int rock : rocks) {
            if (rock != 999999999) {
                DTrockLocation trockLocation = new DTrockLocation();
                trockLocation.setCharacter(character);
                trockLocation.setMapId(rock);
                trockLocation.save();
            }
        }

        new QDRegrockLocation().character.eq(character).delete();
        for (int regrock : regrocks) {
            if (regrock != 999999999) {
                DTrockLocation trockLocation = new DTrockLocation();
                trockLocation.setCharacter(character);
                trockLocation.setMapId(regrock);
                trockLocation.save();
            }
        }
    }

    private void deleteWhereCharacterId(Connection con, String sql) throws SQLException {
        deleteWhereCharacterId(con, sql, id);
    }

    public static void deleteWhereCharacterId(Connection con, String sql, int id) throws SQLException {
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
        ps.close();
    }

    public void saveInventory(DCharacter character) {
        List<Pair<IItem, MapleInventoryType>> listing = Lists.newArrayList();
        for (MapleInventory iv : inventory) {
            for (IItem item : iv.list()) {
                listing.add(new Pair<>(item, iv.getType()));
            }
        }
        ItemLoader.saveItems(listing, character);
    }

    public final PlayerStats getStat() {
        return stats;
    }

    public final PlayerRandomStream CRand() {
        return CRand;
    }

    public final void QuestInfoPacket(final tools.data.output.MaplePacketLittleEndianWriter mplew) {
        mplew.writeShort(questinfo.size());

        for (final Entry<Integer, String> q : questinfo.entrySet()) {
            mplew.writeShort(q.getKey());
            mplew.writeMapleAsciiString(q.getValue() == null ? "" : q.getValue());
        }
    }

    public final void updateInfoQuest(final int questid, final String data) {
        questinfo.put(questid, data);
        client.getSession().write(MaplePacketCreator.updateInfoQuest(questid, data));
    }

    public final String getInfoQuest(final int questid) {
        if (questinfo.containsKey(questid)) {
            return questinfo.get(questid);
        }
        return "";
    }

    public final int getNumQuest() {
        int i = 0;
        for (final MapleQuestStatus q : quests.values()) {
            if (q.getStatus() == 2 && !(q.isCustom())) {
                i++;
            }
        }
        return i;
    }

    public int getQuestStatus(int quest) {
        return getQuest(MapleQuest.getInstance(quest)).getStatus();
    }

    public final MapleQuestStatus getQuest(final MapleQuest quest) {
        if (!quests.containsKey(quest)) {
            return new MapleQuestStatus(quest, (byte) 0);
        }
        return quests.get(quest);
    }

    public void setQuestAdd(int quest) {
        setQuestAddZ(MapleQuest.getInstance(quest), (byte) 2, null);
    }

    public final void setQuestAddZ(final MapleQuest quest, final byte status, final String customData) {

        final MapleQuestStatus stat = new MapleQuestStatus(quest, status);
        stat.setCustomData(customData);
        quests.put(quest, stat);

    }

    public final void setQuestAdd(final MapleQuest quest, final byte status, final String customData) {
        if (!quests.containsKey(quest)) {
            final MapleQuestStatus stat = new MapleQuestStatus(quest, status);
            stat.setCustomData(customData);
            quests.put(quest, stat);
        }
    }

    public final MapleQuestStatus getQuestNAdd(final MapleQuest quest) {
        if (!quests.containsKey(quest)) {
            final MapleQuestStatus status = new MapleQuestStatus(quest, (byte) 0);
            quests.put(quest, status);
            return status;
        }
        return quests.get(quest);
    }

    public MapleQuestStatus getQuestRemove(MapleQuest quest) {
        return (MapleQuestStatus) this.quests.remove(quest);
    }

    public final MapleQuestStatus getQuestNoAdd(final MapleQuest quest) {
        return quests.get(quest);
    }

    public final void updateQuest(final MapleQuestStatus quest) {
        updateQuest(quest, false);
    }

    public final void updateQuest(final MapleQuestStatus quest, final boolean update) {
        quests.put(quest.getQuest(), quest);
        if (!(quest.isCustom())) {
            client.getSession().write(MaplePacketCreator.updateQuest(quest));
            if (quest.getStatus() == 1 && !update) {
                client.getSession().write(MaplePacketCreator.updateQuestInfo(this, quest.getQuest().getId(), quest.getNpc(), (byte) 8));
            }
        }
    }

    public final Map<Integer, String> getInfoQuest_Map() {
        return questinfo;
    }

    public final Map<MapleQuest, MapleQuestStatus> getQuest_Map() {
        return quests;
    }

    public boolean isActiveBuffedValue(int skillid) {
        LinkedList<MapleBuffStatValueHolder> allBuffs = new LinkedList<>(effects.values());
        for (MapleBuffStatValueHolder mbsvh : allBuffs) {
            if (mbsvh.effect.isSkill() && mbsvh.effect.getSourceId() == skillid) {
                return true;
            }
        }
        return false;
    }

    public Integer getBuffedValue(MapleBuffStat effect) {
        final MapleBuffStatValueHolder mbsvh = effects.get(effect);
        return mbsvh == null ? null : mbsvh.value;
    }

    public final Integer getBuffedSkill_X(final MapleBuffStat effect) {
        final MapleBuffStatValueHolder mbsvh = effects.get(effect);
        if (mbsvh == null) {
            return null;
        }
        return mbsvh.effect.getX();
    }

    public final Integer getBuffedSkill_Y(final MapleBuffStat effect) {
        final MapleBuffStatValueHolder mbsvh = effects.get(effect);
        if (mbsvh == null) {
            return null;
        }
        return mbsvh.effect.getY();
    }

    public boolean isBuffFrom(MapleBuffStat stat, ISkill skill) {
        final MapleBuffStatValueHolder mbsvh = effects.get(stat);
        if (mbsvh == null) {
            return false;
        }
        return mbsvh.effect.isSkill() && mbsvh.effect.getSourceId() == skill.getId();
    }

    public int getBuffSource(MapleBuffStat stat) {
        final MapleBuffStatValueHolder mbsvh = effects.get(stat);
        return mbsvh == null ? -1 : mbsvh.effect.getSourceId();
    }

    public int getItemQuantity(int itemid, boolean checkEquipped) {
        int possesed = inventory[GameConstants.getInventoryType(itemid).ordinal()].countById(itemid);
        if (checkEquipped) {
            possesed += inventory[MapleInventoryType.EQUIPPED.ordinal()].countById(itemid);
        }
        return possesed;
    }

    public void setBuffedValue(MapleBuffStat effect, int value) {
        final MapleBuffStatValueHolder mbsvh = effects.get(effect);
        if (mbsvh == null) {
            return;
        }
        mbsvh.value = value;
    }

    public Long getBuffedStarttime(MapleBuffStat effect) {
        final MapleBuffStatValueHolder mbsvh = effects.get(effect);
        return mbsvh == null ? null : mbsvh.startTime;
    }

    public MapleStatEffect getStatForBuff(MapleBuffStat effect) {
        final MapleBuffStatValueHolder mbsvh = effects.get(effect);
        return mbsvh == null ? null : mbsvh.effect;
    }

    private void prepareDragonBlood(final MapleStatEffect bloodEffect) {
        if (dragonBloodSchedule != null) {
            dragonBloodSchedule.cancel(false);
        }
        dragonBloodSchedule = Timer.BUFF.register(() -> {
            if (stats.getHp() - bloodEffect.getX() > 1) {
                cancelBuffStats(MapleBuffStat.DRAGONBLOOD);
            } else {
                addHP(-bloodEffect.getX());
                client.getSession().write(MaplePacketCreator.showOwnBuffEffect(bloodEffect.getSourceId(), 5));
                map.broadcastMessage(MapleCharacter.this, MaplePacketCreator.showBuffeffect(getId(), bloodEffect.getSourceId(), 5), false);
            }
        }, 4000, 4000);
    }

    public void startMapTimeLimitTask(int time, final MapleMap to) {
        client.getSession().write(MaplePacketCreator.getClock(time));

        time *= 1000;
        mapTimeLimitTask = Timer.MAP.register(() -> changeMap(to, to.getPortal(0)), time, time);
    }

    public int fishTasking = 0;
    public int fishingMap = 741000201; //地图

    public void startFishingTask(final boolean VIP) {
        if (fishTasking < 9 && getClient().getPlayer().getMapId() == getClient().getPlayer().fishingMap) {
            fishTasking++;
            StringBuilder gage = new StringBuilder();
            for (int i = 0; i <= fishTasking; i++) {
                gage.append("●");
            }
            for (int i = 9; i > fishTasking; i--) {
                gage.append("○");
            }
            getClient().getSession().write(MaplePacketCreator.sendHint("开始钓鱼...\r\n" + gage, 200, 200));
        }
        final int time = GameConstants.getFishingTime(VIP, isGM());
        cancelFishingTask();

        //no real reason for clone.
        fishing = Timer.ETC.register(() -> {
            final boolean expMulti = haveItem(2300001, 1, false, true);
            if (!expMulti && !haveItem(2300000, 1, false, true)) {
                cancelFishingTask();
                dropMessage(5, "您的鱼饵已经用光了。");
                getClient().getSession().write(MaplePacketCreator.sendHint("没有鱼饵无法钓鱼\r\n", 200, 200));
                return;
            }
            MapleInventoryManipulator.removeById(client, MapleInventoryType.USE, expMulti ? 2300001 : 2300000, 1, false, false);

            final int randval = RandomRewards.getInstance().getFishingReward();

            switch (randval) {
                case 0: // Meso
                    final int money = Randomizer.rand(expMulti ? 15 : 10, expMulti ? 75000 : 50000);
                    gainMeso(money, true);
                    client.getSession().write(UIPacket.fishingUpdate((byte) 1, money));
                    if (getItemQuantity(5340001, true) == 1) {
                        dropMessage(5, "使用高级鱼竿钓鱼。\r\n获得金币：" + money);
                        getClient().getSession().write(MaplePacketCreator.sendHint("使用高级鱼竿钓鱼。\r\n获得金币：" + money, 200, 200));
                    } else if (getItemQuantity(5340000, true) == 1) {
                        dropMessage(5, "使用普通鱼竿钓鱼。\r\n获得金币：" + money);
                        getClient().getSession().write(MaplePacketCreator.sendHint("使用普通鱼竿钓鱼。\r\n获得金币：" + money, 200, 200));
                    }
                    break;
                case 1: // EXP
                    final int experi = Randomizer.nextInt(Math.abs(GameConstants.getExpNeededForLevel(level) / 600) + 1);
                    gainExp(expMulti ? (experi * 3 / 2) : experi, true, false, true);
                    client.getSession().write(UIPacket.fishingUpdate((byte) 2, experi));
                    if (getItemQuantity(5340001, true) == 1) {
                        dropMessage(5, "使用高级鱼竿钓鱼。\r\n获得经验：" + experi);
                        getClient().getSession().write(MaplePacketCreator.sendHint("使用高级鱼竿钓鱼。\r\n获得经验：" + experi, 200, 200));
                    } else if (getItemQuantity(5340000, true) == 1) {
                        dropMessage(5, "使用普通鱼竿钓鱼。\r\n获得经验：" + experi);
                        getClient().getSession().write(MaplePacketCreator.sendHint("使用普通鱼竿钓鱼。\r\n获得经验：" + experi, 200, 200));
                    }
                    break;
                default:
                    if (getItemQuantity(5340001, true) == 1) {
                        dropMessage(5, "使用高级鱼竿钓鱼。\r\n获得经验：" + randval);
                        getClient().getSession().write(MaplePacketCreator.sendHint("使用高级鱼竿钓鱼。\r\n获得经验：" + randval, 200, 200));
                    } else if (getItemQuantity(5340000, true) == 1) {
                        dropMessage(5, "使用普通鱼竿钓鱼。\r\n获得经验：" + randval);
                        getClient().getSession().write(MaplePacketCreator.sendHint("使用普通鱼竿钓鱼。\r\n获得经验：" + randval, 200, 200));
                    }
                    MapleInventoryManipulator.addById(client, randval, (short) 1, (byte) 0);
                    client.getSession().write(UIPacket.fishingUpdate((byte) 0, randval));
                    break;
            }
            map.broadcastMessage(UIPacket.fishingCaught(id));
        }, time, time);
    }

    public void cancelMapTimeLimitTask() {
        if (mapTimeLimitTask != null) {
            mapTimeLimitTask.cancel(false);
        }
    }

    public void cancelFishingTask() {
        if (fishing != null) {
            fishing.cancel(false);
            getClient().getSession().write(MaplePacketCreator.sendHint("钓鱼中断。", 200, 200));
        }
    }

    public void registerEffect(MapleStatEffect effect, long starttime, ScheduledFuture<?> schedule) {
        registerEffect(effect, starttime, schedule, effect.getStatups());
    }

    public void registerEffect(MapleStatEffect effect, long starttime, ScheduledFuture<?>
            schedule, List<Pair<MapleBuffStat, Integer>> statups) {
        if (effect.isHide()) {
            this.hidden = true;
            map.broadcastMessage(this, MaplePacketCreator.removePlayerFromMap(getId()), false);
        } else if (effect.isDragonBlood()) {
            prepareDragonBlood(effect);
        } else if (effect.isBerserk()) {
            checkBerserk();
        } else if (effect.isMonsterRiding_()) {
            getMount().startSchedule();
        } else if (effect.isBeholder()) {
            prepareBeholderEffect();
        } else if (effect.getSourceId() == 1001 || effect.getSourceId() == 10001001 || effect.getSourceId() == 1001) {
            prepareRecovery();
        }
        int clonez = 0;
        for (Pair<MapleBuffStat, Integer> statup : statups) {
            if (statup.getLeft() == MapleBuffStat.ILLUSION) {
                clonez = statup.getRight();
            }
            int value = statup.getRight().intValue();
            if (statup.getLeft() == MapleBuffStat.骑兽技能 && effect.getSourceId() == 5221006) {
                if (battleshipHP <= 0) {//quick hack
                    battleshipHP = value; //copy this as well
                }
            }
            effects.put(statup.getLeft(), new MapleBuffStatValueHolder(effect, starttime, schedule, value));
        }
        if (clonez > 0) {
            int cloneSize = Math.max(getNumClones(), getCloneSize());
            if (clonez > cloneSize) { //how many clones to summon
                for (int i = 0; i < clonez - cloneSize; i++) { //1-1=0
                    cloneLook();
                }
            }
        }
        stats.recalcLocalStats();
        //LOGGER.debug("Effect registered. Effect: " + effect.getSourceId());
    }

    public List<MapleBuffStat> getBuffStats(final MapleStatEffect effect, final long startTime) {
        final List<MapleBuffStat> bstats = new ArrayList<>();
        final Map<MapleBuffStat, MapleBuffStatValueHolder> allBuffs = new EnumMap<>(effects);
        for (Entry<MapleBuffStat, MapleBuffStatValueHolder> stateffect : allBuffs.entrySet()) {
            final MapleBuffStatValueHolder mbsvh = stateffect.getValue();
            if (mbsvh.effect.sameSource(effect) && (startTime == -1 || startTime == mbsvh.startTime)) {
                bstats.add(stateffect.getKey());
            }
        }
        return bstats;
    }

    private boolean deregisterBuffStats(List<MapleBuffStat> stats) {
        boolean clonez = false;
        List<MapleBuffStatValueHolder> effectsToCancel = new ArrayList<>(stats.size());
        for (MapleBuffStat stat : stats) {
            final MapleBuffStatValueHolder mbsvh = effects.remove(stat);
            if (mbsvh != null) {
                boolean addMbsvh = true;
                for (MapleBuffStatValueHolder contained : effectsToCancel) {
                    if (mbsvh.startTime == contained.startTime && contained.effect == mbsvh.effect) {
                        addMbsvh = false;
                    }
                }
                if (addMbsvh) {
                    effectsToCancel.add(mbsvh);
                }
                if (stat == MapleBuffStat.SUMMON || stat == MapleBuffStat.PUPPET || stat == MapleBuffStat.REAPER) {
                    final int summonId = mbsvh.effect.getSourceId();
                    final MapleSummon summon = summons.get(summonId);
                    if (summon != null) {
                        map.broadcastMessage(MaplePacketCreator.removeSummon(summon, true));
                        map.removeMapObject(summon);
                        removeVisibleMapObject(summon);
                        summons.remove(summonId);
                        if (summon.getSkill() == 1321007) {
                            if (beholderHealingSchedule != null) {
                                beholderHealingSchedule.cancel(false);
                                beholderHealingSchedule = null;
                            }
                            if (beholderBuffSchedule != null) {
                                beholderBuffSchedule.cancel(false);
                                beholderBuffSchedule = null;
                            }
                        }
                    }
                } else if (stat == MapleBuffStat.DRAGONBLOOD) {
                    if (dragonBloodSchedule != null) {
                        dragonBloodSchedule.cancel(false);
                        dragonBloodSchedule = null;
                    }
                } else if (stat == MapleBuffStat.ILLUSION) {
                    disposeClones();
                    clonez = true;
                }
            }
        }
        for (MapleBuffStatValueHolder cancelEffectCancelTasks : effectsToCancel) {
            if (getBuffStats(cancelEffectCancelTasks.effect, cancelEffectCancelTasks.startTime).size() == 0) {
                if (cancelEffectCancelTasks.schedule != null) {
                    cancelEffectCancelTasks.schedule.cancel(false);
                }
            }
        }
        return clonez;
    }

    /**
     * @param effect
     * @param overwrite when overwrite is set no data is sent and all the
     *                  Buffstats in the StatEffect are deregistered
     * @param startTime
     */
    public void cancelEffect(final MapleStatEffect effect, final boolean overwrite, final long startTime) {
        cancelEffect(effect, overwrite, startTime, effect.getStatups());
    }

    public void cancelEffect(final MapleStatEffect effect, final boolean overwrite, final long startTime, List<
            Pair<MapleBuffStat, Integer>> statups) {
        List<MapleBuffStat> buffstats;
        if (!overwrite) {
            buffstats = getBuffStats(effect, startTime);
        } else {
            buffstats = new ArrayList<>(statups.size());
            for (Pair<MapleBuffStat, Integer> statup : statups) {
                buffstats.add(statup.getLeft());
            }
        }
        if (buffstats.isEmpty()) {
            return;
        }
        final boolean clonez = deregisterBuffStats(buffstats);
        if (effect.isMagicDoor()) {
            // remove for all on maps
            if (!getDoors().isEmpty()) {
                MapleDoor door = getDoors().iterator().next();
                for (MapleCharacter chr : door.getTarget().getCharacters()) {
                    door.sendDestroyData(chr.client);
                }
                for (MapleCharacter chr : door.getTown().getCharacters()) {
                    door.sendDestroyData(chr.client);
                }
                for (MapleDoor destroyDoor : getDoors()) {
                    door.getTarget().removeMapObject(destroyDoor);
                    door.getTown().removeMapObject(destroyDoor);
                }
                removeDoor();
                silentPartyUpdate();
            }
        } else if (effect.isMonsterRiding_() /*|| getMountId() == effect.getSourceId()*/) {
            getMount().cancelSchedule();
        } else if (effect.isMonsterRiding()) {
            cancelEffectFromBuffStat(MapleBuffStat.MECH_CHANGE);
        } else if (effect.isAranCombo()) {
            combo = 0;
        }
        // check if we are still logged in o.o
        if (!overwrite) {
            if (effect.isMonsterS()) {
                cancelPlayerBuffs(buffstats, effect);
            } else {
                cancelPlayerBuffs(buffstats);
            }
            if (effect.isHide() && client.getChannelServer().getPlayerStorage().getCharacterById(this.getId()) != null) { //Wow this is so fking hacky...
                this.hidden = false;
                map.broadcastMessage(this, MaplePacketCreator.spawnPlayerMapobject(this), false);

                for (final MaplePet pet : pets) {
                    if (pet.getSummoned()) {
                        map.broadcastMessage(this, PetPacket.showPet(this, pet, false, false), false);
                    }
                }
                for (final WeakReference<MapleCharacter> chr : clones) {
                    if (chr.get() != null) {
                        map.broadcastMessage(chr.get(), MaplePacketCreator.spawnPlayerMapobject(chr.get()), false);
                    }
                }
            }
        }
        if (!clonez) {
            for (WeakReference<MapleCharacter> chr : clones) {
                if (chr.get() != null) {
                    chr.get().cancelEffect(effect, overwrite, startTime);
                }
            }
        }
        //LOGGER.debug("Effect deregistered. Effect: " + effect.getSourceId());
    }

    public void cancelBuffStats(MapleBuffStat... stat) {
        List<MapleBuffStat> buffStatList = Arrays.asList(stat);
        deregisterBuffStats(buffStatList);
        cancelPlayerBuffs(buffStatList);
    }

    public void cancelEffectFromBuffStat(MapleBuffStat stat) {
        if (effects.get(stat) != null) {
            cancelEffect(effects.get(stat).effect, false, -1);
        }
    }

    private void cancelPlayerBuffs(List<MapleBuffStat> buffstats) {
        boolean write = client.getChannelServer().getPlayerStorage().getCharacterById(getId()) != null;
        if (buffstats.contains(MapleBuffStat.HOMING_BEACON)) {
            if (write) {
                client.getSession().write(MaplePacketCreator.cancelHoming());
            }
        } else if (buffstats.contains(MapleBuffStat.骑兽技能)) {
            /* if (write) {
                stats.recalcLocalStats();
            }*/
            client.getSession().write(MaplePacketCreator.cancelBuffMONSTER(buffstats));
            //  LOGGER.debug("坐骑取消BUFF的mask："+buffstats);
            map.broadcastMessage(this, MaplePacketCreator.cancelForeignBuffMONSTER(getId(), buffstats), false);
        } else {
            /*if (write) {
                stats.recalcLocalStats();
            }*/
            client.getSession().write(MaplePacketCreator.cancelBuff(buffstats));
            // LOGGER.debug("普通取消BUFF的mask："+buffstats);
            map.broadcastMessage(this, MaplePacketCreator.cancelForeignBuff(getId(), buffstats), false);
        }
    }

    private void cancelPlayerBuffs(List<MapleBuffStat> buffstats, final MapleStatEffect effect) {
        if (effect.isMonsterS()) {
            /* if (write) {
                stats.recalcLocalStats();
            }*/
            client.getSession().write(MaplePacketCreator.cancelBuffMONSTERS(buffstats));
            //  LOGGER.debug("坐骑取消BUFF的mask："+buffstats);
            map.broadcastMessage(this, MaplePacketCreator.cancelForeignBuffMONSTERS(getId(), buffstats), false);
        }
    }

    /* private void cancelPlayerBuffs(List<MapleBuffStat> buffstats) {
        boolean write = client.getChannelServer().getPlayerStorage().getCharacterById(getId()) != null;
        if (buffstats.contains(MapleBuffStat.导航辅助)) {
            if (write) {
                client.getSession().write(MaplePacketCreator.cancelHoming());
            }
        } else if (buffstats.contains(MapleBuffStat.MANA_REFLECTION)) {
            if (write) {
                stats.recalcLocalStats();
            }
            client.getSession().write(MaplePacketCreator.cancelBuffMONSTER(buffstats));
            map.broadcastMessage(this, MaplePacketCreator.cancelForeignBuffMONSTER(getId(), buffstats), false);
        } else {
            if (write) {
                stats.recalcLocalStats();
            }
            client.getSession().write(MaplePacketCreator.cancelBuff(buffstats));
            map.broadcastMessage(this, MaplePacketCreator.cancelForeignBuff(getId(), buffstats), false);
        }
    }*/
    public void dispel() {
        if (!isHidden()) {
            final LinkedList<MapleBuffStatValueHolder> allBuffs = new LinkedList<MapleBuffStatValueHolder>(effects.values());
            for (MapleBuffStatValueHolder mbsvh : allBuffs) {
                if (mbsvh.effect.isSkill() && mbsvh.schedule != null && !mbsvh.effect.isMorph()) {
                    cancelEffect(mbsvh.effect, false, mbsvh.startTime);
                }
            }
        }
    }

    public void dispelSkill(int skillid) {
        final LinkedList<MapleBuffStatValueHolder> allBuffs = new LinkedList<MapleBuffStatValueHolder>(effects.values());

        for (MapleBuffStatValueHolder mbsvh : allBuffs) {
            if (skillid == 0) {
                if (mbsvh.effect.isSkill() && (mbsvh.effect.getSourceId() == 4331003 || mbsvh.effect.getSourceId() == 4331002 || mbsvh.effect.getSourceId() == 4341002 || mbsvh.effect.getSourceId() == 22131001 || mbsvh.effect.getSourceId() == 1321007 || mbsvh.effect.getSourceId() == 2121005 || mbsvh.effect.getSourceId() == 2221005 || mbsvh.effect.getSourceId() == 2311006 || mbsvh.effect.getSourceId() == 2321003 || mbsvh.effect.getSourceId() == 3111002 || mbsvh.effect.getSourceId() == 3111005 || mbsvh.effect.getSourceId() == 3211002 || mbsvh.effect.getSourceId() == 3211005 || mbsvh.effect.getSourceId() == 4111002)) {
                    cancelEffect(mbsvh.effect, false, mbsvh.startTime);
                    break;
                }
            } else if (mbsvh.effect.isSkill() && mbsvh.effect.getSourceId() == skillid) {
                cancelEffect(mbsvh.effect, false, mbsvh.startTime);
                break;
            }
        }
    }

    public void dispelBuff(int skillid) {
        final LinkedList<MapleBuffStatValueHolder> allBuffs = new LinkedList<MapleBuffStatValueHolder>(effects.values());

        for (MapleBuffStatValueHolder mbsvh : allBuffs) {
            if (mbsvh.effect.getSourceId() == skillid) {
                cancelEffect(mbsvh.effect, false, mbsvh.startTime);
                break;
            }
        }
    }

    public void cancelAllBuffs_() {
        effects.clear();
    }

    public void cancelAllBuffs() {
        final LinkedList<MapleBuffStatValueHolder> allBuffs = new LinkedList<MapleBuffStatValueHolder>(effects.values());

        for (MapleBuffStatValueHolder mbsvh : allBuffs) {
            cancelEffect(mbsvh.effect, false, mbsvh.startTime);
        }
    }

    public void cancelMorphs() {
        final LinkedList<MapleBuffStatValueHolder> allBuffs = new LinkedList<MapleBuffStatValueHolder>(effects.values());

        for (MapleBuffStatValueHolder mbsvh : allBuffs) {
            switch (mbsvh.effect.getSourceId()) {
                case 5111005:
                case 5121003:
                case 15111002:
                case 13111005:
                    return; // Since we can't have more than 1, save up on loops
                default:
                    if (mbsvh.effect.isMorph()) {
                        cancelEffect(mbsvh.effect, false, mbsvh.startTime);
                        continue;
                    }
            }
        }
    }

    public int getMorphState() {
        LinkedList<MapleBuffStatValueHolder> allBuffs = new LinkedList<MapleBuffStatValueHolder>(effects.values());
        for (MapleBuffStatValueHolder mbsvh : allBuffs) {
            if (mbsvh.effect.isMorph()) {
                return mbsvh.effect.getSourceId();
            }
        }
        return -1;
    }

    public void silentGiveBuffs(List<PlayerBuffValueHolder> buffs) {
        if (buffs == null) {
            return;
        }
        for (PlayerBuffValueHolder mbsvh : buffs) {
            mbsvh.effect.silentApplyBuff(this, mbsvh.startTime);
        }
    }

    public List<PlayerBuffValueHolder> getAllBuffs() {
        List<PlayerBuffValueHolder> ret = new ArrayList<PlayerBuffValueHolder>();
        LinkedList<MapleBuffStatValueHolder> allBuffs = new LinkedList<MapleBuffStatValueHolder>(effects.values());
        for (MapleBuffStatValueHolder mbsvh : allBuffs) {
            ret.add(new PlayerBuffValueHolder(mbsvh.startTime, mbsvh.effect));
        }
        return ret;
    }

    public void cancelMagicDoor() {
        final LinkedList<MapleBuffStatValueHolder> allBuffs = new LinkedList<MapleBuffStatValueHolder>(effects.values());

        for (MapleBuffStatValueHolder mbsvh : allBuffs) {
            if (mbsvh.effect.isMagicDoor()) {
                cancelEffect(mbsvh.effect, false, mbsvh.startTime);
                break;
            }
        }
    }

    public int getSkillLevel(int skillid) {
        return getSkillLevel(SkillFactory.getSkill(skillid));
    }

    public final void handleEnergyCharge(final int skillid, final int targets) {
        final ISkill echskill = SkillFactory.getSkill(skillid);
        final byte skilllevel = getSkillLevel(echskill);
        if (skilllevel > 0) {
            final MapleStatEffect echeff = echskill.getEffect(skilllevel);
            //  LOGGER.debug("获取能量B："+ skilllevel);
            if (targets > 0) {
                //     LOGGER.debug("获取能量C："+ targets);
//                if (nengl <= 10) {
//                    nengl = nengl + 1;
//                } else {
                if (getBuffedValue(MapleBuffStat.ENERGY_CHARGE) == null) {
                    echeff.applyEnergyBuff(this, true); // Infinity time
                    //    LOGGER.debug("获取能量D：");
                } else {
                    Integer energyLevel = getBuffedValue(MapleBuffStat.ENERGY_CHARGE);
                    //TODO: bar going down
                    if (energyLevel <= 15000 /*
                     * && nengls <= 20
                     */) {
                        energyLevel += (echeff.getX() * targets);

                        client.getSession().write(MaplePacketCreator.showOwnBuffEffect(skillid, 2));
                        map.broadcastMessage(this, MaplePacketCreator.showBuffeffect(id, skillid, 2), false);

                        if (energyLevel >= 15000) {
                            energyLevel = 15000;
                        }/*
                         * if (nengls <= 20) { nengls = nengls + 1; }
                         */

                        //LOGGER.debug("获取能量E："+ energyLevel);

                        List<Pair<MapleBuffStat, Integer>> stat = Collections.singletonList(new Pair<MapleBuffStat, Integer>(MapleBuffStat.ENERGY_CHARGE, energyLevel));
                        client.getSession().write(MaplePacketCreator.能量条(stat, energyLevel / 1000)); //????????????????
                        //client.getSession().write(MaplePacketCreator.givePirateBuff(energyLevel, 0, stat));
                        // client.getSession().write(MaplePacketCreator.giveEnergyChargeTest(energyLevel, echeff.getDuration() / 1000));
                        setBuffedValue(MapleBuffStat.ENERGY_CHARGE, Integer.valueOf(energyLevel));
                        Timer.BUFF.schedule(new Runnable() {

                            @Override
                            public void run() {
                                Integer energyLevel = 0;
                                setBuffedValue(MapleBuffStat.ENERGY_CHARGE, Integer.valueOf(energyLevel));
                                List<Pair<MapleBuffStat, Integer>> stat = Collections.singletonList(new Pair<MapleBuffStat, Integer>(MapleBuffStat.ENERGY_CHARGE, energyLevel));
                                client.getSession().write(MaplePacketCreator.能量条(stat, 0)); //????????????????
                            }
                        }, 3 * 60 * 1000);
                        /*
                         * Timer.WORLD.register(new
                         * Runnable() {
                         *
                         * public void run() { Integer energyLevel =
                         * getBuffedValue(MapleBuffStat.ENERGY_CHARGE); try {
                         * energyLevel = 1; LOGGER.debug("获取能量Z：" +
                         * energyLevel); List<Pair<MapleBuffStat, Integer>> stat
                         * = Collections.singletonList(new Pair<MapleBuffStat,
                         * Integer>(MapleBuffStat.ENERGY_CHARGE, energyLevel));
                         * client.getSession().write(MaplePacketCreator.能量条(stat,
                         * energyLevel / 1000)); //????????????????
                         * setBuffedValue(MapleBuffStat.ENERGY_CHARGE,
                         * Integer.valueOf(energyLevel)); } catch (Exception e)
                         * { } } }, 10000);
                         */
                    }

                    /*
                     * else if (nengls > 20) { nengls = 0; nengl = 0; //
                     * LOGGER.debug("获取能量F："+ energyLevel);
                     * List<Pair<MapleBuffStat, Integer>> stat =
                     * Collections.singletonList(new Pair<MapleBuffStat,
                     * Integer>(MapleBuffStat.ENERGY_CHARGE, 0));
                     * client.getSession().write(MaplePacketCreator.能量条(stat,
                     * 0)); //????????????????
                     * //client.getSession().write(MaplePacketCreator.givePirateBuff(energyLevel,
                     * 0, stat)); //
                     * client.getSession().write(MaplePacketCreator.giveEnergyChargeTest(energyLevel,
                     * echeff.getDuration() / 1000));
                     * setBuffedValue(MapleBuffStat.ENERGY_CHARGE,
                     * Integer.valueOf(0)); // echeff.applyEnergyBuff(this,
                     * false); // One with time //
                     * setBuffedValue(MapleBuffStat.ENERGY_CHARGE,
                     * Integer.valueOf(10001)); }
                     */
                }
                // LOGGER.debug("能量S："+ nengls);
                // LOGGER.debug("能量："+ nengl);
                /*
                 * Timer.WORLD.register(new Runnable() {
                 * @Override public void run() { energyPoint -= 200; if
                 * (energyPoint <= 0) { echeff.applyEnergyBuff(chrs, false); //
                 * One with time setBuffedValue(MapleBuffStat.ENERGY_CHARGE,
                 * Integer.valueOf(10001)); } try { } catch (Exception e) { } }
                 * }, 60000 * 1);
                 */
                //   }
            }
        }
    }

    public final void handleBattleshipHP(int damage) {
        if (isActiveBuffedValue(5221006)) {
            battleshipHP -= damage;
            if (battleshipHP <= 0) {
                battleshipHP = 0;
                final MapleStatEffect effect = getStatForBuff(MapleBuffStat.骑兽技能);
                client.getSession().write(MaplePacketCreator.skillCooldown(5221006, effect.getCooldown()));
                addCooldown(5221006, System.currentTimeMillis(), effect.getCooldown() * 1000);
                dispelSkill(5221006);
            }
        }
    }

    public final void handleOrbgain() {
        int orbcount = getBuffedValue(MapleBuffStat.COMBO);
        ISkill combo;
        ISkill advcombo;

        switch (getJob()) {
            case 1110:
            case 1111:
            case 1112:
                combo = SkillFactory.getSkill(11111001);
                advcombo = SkillFactory.getSkill(11110005);
                break;
            default:
                combo = SkillFactory.getSkill(1111002);
                advcombo = SkillFactory.getSkill(1120003);
                break;
        }

        MapleStatEffect ceffect = null;
        int advComboSkillLevel = getSkillLevel(advcombo);
        if (advComboSkillLevel > 0) {
            ceffect = advcombo.getEffect(advComboSkillLevel);
        } else if (getSkillLevel(combo) > 0) {
            ceffect = combo.getEffect(getSkillLevel(combo));
        } else {
            return;
        }

        if (orbcount < ceffect.getX() + 1) {
            int neworbcount = orbcount + 1;
            if (advComboSkillLevel > 0 && ceffect.makeChanceResult()) {
                if (neworbcount < ceffect.getX() + 1) {
                    neworbcount++;
                }
            }
            List<Pair<MapleBuffStat, Integer>> stat = Collections.singletonList(new Pair<MapleBuffStat, Integer>(MapleBuffStat.COMBO, neworbcount));
            setBuffedValue(MapleBuffStat.COMBO, neworbcount);
            int duration = ceffect.getDuration();
            duration += (int) ((getBuffedStarttime(MapleBuffStat.COMBO) - System.currentTimeMillis()));

            client.getSession().write(MaplePacketCreator.giveBuff(combo.getId(), duration, stat, ceffect));
            map.broadcastMessage(this, MaplePacketCreator.giveForeignBuff(this, getId(), stat, ceffect), false);
        }
    }

    public void handleOrbconsume() {
        ISkill combo;

        switch (getJob()) {
            case 1110:
            case 1111:
                combo = SkillFactory.getSkill(11111001);
                break;
            default:
                combo = SkillFactory.getSkill(1111002);
                break;
        }
        if (getSkillLevel(combo) <= 0) {
            return;
        }
        MapleStatEffect ceffect = getStatForBuff(MapleBuffStat.COMBO);
        if (ceffect == null) {
            return;
        }
        List<Pair<MapleBuffStat, Integer>> stat = Collections.singletonList(new Pair<MapleBuffStat, Integer>(MapleBuffStat.COMBO, 1));
        setBuffedValue(MapleBuffStat.COMBO, 1);
        int duration = ceffect.getDuration();
        duration += (int) ((getBuffedStarttime(MapleBuffStat.COMBO) - System.currentTimeMillis()));

        client.getSession().write(MaplePacketCreator.giveBuff(combo.getId(), duration, stat, ceffect));
        map.broadcastMessage(this, MaplePacketCreator.giveForeignBuff(this, getId(), stat, ceffect), false);
    }

    public void silentEnforceMaxHpMp() {
        stats.setMp(stats.getMp());
        stats.setHp(stats.getHp(), true);
    }

    public void enforceMaxHpMp() {
        List<Pair<MapleStat, Integer>> statups = new ArrayList<Pair<MapleStat, Integer>>(2);
        if (stats.getMp() > stats.getCurrentMaxMp()) {
            stats.setMp(stats.getMp());
            statups.add(new Pair<MapleStat, Integer>(MapleStat.MP, Integer.valueOf(stats.getMp())));
        }
        if (stats.getHp() > stats.getCurrentMaxHp()) {
            stats.setHp(stats.getHp());
            statups.add(new Pair<MapleStat, Integer>(MapleStat.HP, Integer.valueOf(stats.getHp())));
        }
        if (statups.size() > 0) {
            client.getSession().write(MaplePacketCreator.updatePlayerStats(statups, getJob()));
        }
    }

    public MapleMap getMap() {
        return map;
    }

    public MonsterBook getMonsterBook() {
        return monsterbook;
    }

    public void setMap(MapleMap newmap) {
        this.map = newmap;
    }

    public void setMap(int PmapId) {
        this.mapid = PmapId;
    }

    public int getMapId() {
        if (map != null) {
            return map.getId();
        }
        return mapid;
    }

    public byte getInitialSpawnpoint() {
        return (byte) initialSpawnPoint;
    }

    public int getId() {
        return character.getId();
    }

    public String getName() {
        return name;
    }

    public final String getBlessOfFairyOrigin() {
        return this.BlessOfFairy_Origin;
    }

    public final short getLevel() {
        return (short) level;
    }

    public final short getFame() {
        return (short) fame;
    }

    public final int getDojo() {
        return dojo;
    }

    public final int getDojoRecord() {
        return dojoRecord;
    }

    public final int getFallCounter() {
        return fallcounter;
    }

    public final MapleClient getClient() {
        return client;
    }

    public final void setClient(final MapleClient client) {
        this.client = client;
    }

    public int getExp() {
        return exp;
    }

    public short getRemainingAp() {
        return (short) remainingAp;
    }

    public int getRemainingSp() {
        return remainingSp[GameConstants.getSkillBook(job)]; //default
    }

    public int getRemainingSp(final int skillbook) {
        return remainingSp[skillbook];
    }

    public int[] getRemainingSps() {
        return remainingSp;
    }

    public int getRemainingSpSize() {
        int ret = 0;
        for (int i = 0; i < remainingSp.length; i++) {
            if (remainingSp[i] > 0) {
                ret++;
            }
        }
        return ret;
    }

    public short getHpApUsed() {
        return (short) hpApUsed;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHpApUsed(short hpApUsed) {
        this.hpApUsed = hpApUsed;
    }

    public int getSkinColor() {
        return skinColor;
    }

    public void setSkinColor(byte skinColor) {
        this.skinColor = skinColor;
    }

    public int getJob() {
        return job;
    }

    public Gender getGender() {
        return gender;
    }

    public int getHair() {
        return hair;
    }

    public int getFace() {
        return face;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public void setHair(int hair) {
        this.hair = hair;
    }

    public void setFace(int face) {
        this.face = face;
    }

    public void setFame(short fame) {
        this.fame = fame;
    }

    public void setDojo(final int dojo) {
        this.dojo = dojo;
    }

    public void setDojoRecord(final boolean reset) {
        if (reset) {
            dojo = 0;
            dojoRecord = 0;
        } else {
            dojoRecord++;
        }
    }

    public void setFallCounter(int fallcounter) {
        this.fallcounter = fallcounter;
    }

    public Vector getOldPosition() {
        return old;
    }

    public void setOldPosition(Vector x) {
        this.old = x;
    }

    public void setRemainingAp(short remainingAp) {
        this.remainingAp = remainingAp;
    }

    public void setRemainingSp(int remainingSp) {
        this.remainingSp[GameConstants.getSkillBook(job)] = remainingSp; //default
    }

    public void setRemainingSp(int remainingSp, final int skillbook) {
        this.remainingSp[skillbook] = remainingSp;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setInvincible(boolean invinc) {
        invincible = invinc;
    }

    public boolean isInvincible() {
        return invincible;
    }

    public CheatTracker getCheatTracker() {
        return anticheat;
    }

    public BuddyList getBuddylist() {
        return buddylist;
    }

    public void addFame(int famechange) {
        this.fame += famechange;
        /*
         * if (this.fame >= 50) { finishAchievement(7); }
         */
    }

    public void changeMapBanish(final int mapid, final String portal, final String msg) {
        dropMessage(5, msg);
        final MapleMap map = client.getChannelServer().getMapFactory().getMap(mapid);
        if (map != null) {
            changeMap(map, map.getPortal(portal));
        }
    }

    public void changeMap(final int to) {
        MapleMap map = ChannelServer.getInstance(getClient().getChannel()).getMapFactory().getMap(to);
        changeMapInternal(map, map.getPortal(0).getPosition(), MaplePacketCreator.getWarpToMap(map, 0, this), map.getPortal(0));
    }

    public void changeMap(int map, int portal) {
        MapleMap warpMap = client.getChannelServer().getMapFactory().getMap(map);
        changeMap(warpMap, warpMap.getPortal(portal));
    }

    public void changeMap(final MapleMap to, final Vector pos) {
        changeMapInternal(to, pos, MaplePacketCreator.getWarpToMap(to, 128, this), null);
    }

    public void changeMap(final MapleMap to, final MaplePortal pto) {
        changeMapInternal(to, pto.getPosition(), MaplePacketCreator.getWarpToMap(to, pto.getId(), this), null);
    }

    public void changeMapPortal(final MapleMap to, final MaplePortal pto) {
        changeMapInternal(to, pto.getPosition(), MaplePacketCreator.getWarpToMap(to, pto.getId(), this), pto);
    }

    private void changeMapInternal(final MapleMap to, final Vector pos, MaplePacket warpPacket,
                                   final MaplePortal pto) {
        if (to == null) {
            return;
        }
        saveToDB(false, false);
        final int nowmapid = map.getId();
        if (eventInstance != null) {
            eventInstance.changedMap(this, to.getId());
        }
        final boolean pyramid = pyramidSubway != null;
        if (map.getId() == nowmapid) {
            client.getSession().write(warpPacket);

            map.removePlayer(this);
            if (!isClone() && client.getChannelServer().getPlayerStorage().getCharacterById(getId()) != null) {
                map = to;
                setPosition(pos);
                to.addPlayer(this);
                stats.relocHeal();
            }
        }
        if (party != null) {
            silentPartyUpdate();
            getClient().getSession().write(MaplePacketCreator.updateParty(getClient().getChannel(), party, PartyOperation.SILENT_UPDATE, null));
            updatePartyMemberHP();
        }
        if (pyramid && pyramidSubway != null) { //checks if they had pyramid before AND after changing
            pyramidSubway.onChangeMap(this, to.getId());
        }

    }

    public void leaveMap() {
        //controlled.clear();
        visibleMapObjectsLock.writeLock().lock();
        try {
            visibleMapObjects.clear();
        } finally {
            visibleMapObjectsLock.writeLock().unlock();
        }
        if (chair != 0) {
            cancelFishingTask();
            chair = 0;
        }
        cancelMapTimeLimitTask();
    }

    /* public void clearLinkMid() {
        linkMid.clear();
        cancelEffectFromBuffStat(MapleBuffStat.导航辅助);//导航辅助
    }*/
    public int getFirstLinkMid() {
        for (Integer lm : linkMid.keySet()) {
            return lm.intValue();
        }
        return 0;
    }

    public Map<Integer, Integer> getAllLinkMid() {
        return linkMid;
    }

    public void setLinkMid(int lm, int x) {
        linkMid.put(lm, x);
    }

    public int getDamageIncrease(int lm) {
        if (linkMid.containsKey(lm)) {
            return linkMid.get(lm);
        }
        return 0;
    }

    public void changeJob(int newJob) {
        try {
            final boolean isEv = GameConstants.isEvan(job) || GameConstants.isResist(job);
            this.job = (short) newJob;
            if (newJob != 0 && newJob != 1000 && newJob != 2000 && newJob != 2001 && newJob != 3000) {
                if (isEv) {
                    remainingSp[GameConstants.getSkillBook(newJob)] += 5;
                    client.getSession().write(UIPacket.getSPMsg((byte) 5, (short) newJob));
                } else {
                    remainingSp[GameConstants.getSkillBook(newJob)]++;
                    if (newJob % 10 >= 2) {
                        remainingSp[GameConstants.getSkillBook(newJob)] += 2;
                    }
                }
            }
            if (newJob > 0 && !isGM()) {
                resetStatsByJob(true);
                if (!GameConstants.isEvan(newJob)) {
                    if (getLevel() > (newJob == 200 ? 8 : 10) && newJob % 100 == 0 && (newJob % 1000) / 100 > 0) { //first job
                        remainingSp[GameConstants.getSkillBook(newJob)] += 3 * (getLevel() - (newJob == 200 ? 8 : 10));
                    }
                } else if (newJob == 2200) {
                    MapleQuest.getInstance(22100).forceStart(this, 0, null);
                    MapleQuest.getInstance(22100).forceComplete(this, 0);
                    expandInventory((byte) 1, 4);
                    expandInventory((byte) 2, 4);
                    expandInventory((byte) 3, 4);
                    expandInventory((byte) 4, 4);
                    client.getSession().write(MaplePacketCreator.getEvanTutorial("UI/tutorial/evan/14/0"));
                    dropMessage(5, "The baby Dragon hatched and appears to have something to tell you. Click the baby Dragon to start a conversation.");
                }
            }
            client.getSession().write(MaplePacketCreator.updateSp(this, false, isEv));
            updateSingleStat(MapleStat.JOB, newJob);

            int maxhp = stats.getMaxHp(), maxmp = stats.getMaxMp();

            switch (job) {
                case 100: // Warrior
                case 1100: // Soul Master
                case 2100: // Aran
                case 3200:
                    maxhp += Randomizer.rand(200, 250);
                    break;
                case 200: // Magician
                case 2200: //evan
                case 2210: //evan
                    maxmp += Randomizer.rand(100, 150);
                    break;
                case 300: // Bowman
                case 400: // Thief
                case 500: // Pirate
                case 3300:
                case 3500:
                    maxhp += Randomizer.rand(100, 150);
                    maxmp += Randomizer.rand(25, 50);
                    break;
                case 110: // Fighter
                    maxhp += Randomizer.rand(300, 350);
                    break;
                case 120: // Page
                case 130: // Spearman
                case 510: // 打手
                case 512: // 拳霸
                case 1110: // Soul Master
                case 2110: // Aran
                case 3210:
                    maxhp += Randomizer.rand(300, 350);
                    break;
                case 210: // FP
                case 220: // IL
                case 230: // Cleric
                    maxmp += Randomizer.rand(400, 450);
                    break;
                case 310: // Bowman
                case 312: // Bowman
                case 320: // Crossbowman
                case 322: // Bowman
                case 410: // Assasin
                case 412: // Assasin
                case 420: // Bandit
                case 422: // Assasin
                case 430: // Semi Dualer
                case 520: // 槍手
                case 522: // 槍神
                case 1310: // Wind Breaker
                case 1410: // Night Walker
                case 3310:
                case 3510:
                    maxhp += Randomizer.rand(300, 350);
                    maxhp += Randomizer.rand(150, 200);
                    break;
                case 900: // GM
                case 800: // Manager
                    maxhp += 30000;
                    maxhp += 30000;
                    break;
            }
            if (maxhp >= 30000) {
                maxhp = 30000;
            }
            if (maxmp >= 30000) {
                maxmp = 30000;
            }

            stats.setMaxHp((short) maxhp);
            stats.setMaxMp((short) maxmp);
            stats.setHp((short) maxhp);
            stats.setMp((short) maxmp);
            List<Pair<MapleStat, Integer>> statup = new ArrayList<Pair<MapleStat, Integer>>(4);
            statup.add(new Pair<MapleStat, Integer>(MapleStat.MAXHP, Integer.valueOf(maxhp)));
            statup.add(new Pair<MapleStat, Integer>(MapleStat.MAXMP, Integer.valueOf(maxmp)));
            statup.add(new Pair<MapleStat, Integer>(MapleStat.HP, Integer.valueOf(maxhp)));
            statup.add(new Pair<MapleStat, Integer>(MapleStat.MP, Integer.valueOf(maxmp)));
            stats.recalcLocalStats();
            client.getSession().write(MaplePacketCreator.updatePlayerStats(statup, getJob()));
            map.broadcastMessage(this, MaplePacketCreator.showForeignEffect(getId(), 8), false);
            silentPartyUpdate();
            guildUpdate();
            familyUpdate();
            if (dragon != null) {
                map.broadcastMessage(MaplePacketCreator.removeDragon(this.id));
                map.removeMapObject(dragon);
                dragon = null;
            }
            baseSkills();
            if (newJob >= 2200 && newJob <= 2218) { //make new
                if (getBuffedValue(MapleBuffStat.骑兽技能) != null) {
                    cancelBuffStats(MapleBuffStat.骑兽技能);
                }
                makeDragon();
                map.spawnDragon(dragon);
                map.updateMapObjectVisibility(this, dragon);
            }
        } catch (Exception e) {
            FileoutputUtil.outputFileError(FileoutputUtil.ScriptEx_Log, e); //all jobs throw errors :(
        }
    }

    public void baseSkills() {
        if (GameConstants.getJobNumber(job) >= 3) { //third job.
            List<Integer> skills = SkillFactory.getSkillsByJob(job);
            if (skills != null) {
                for (int i : skills) {
                    final ISkill skil = SkillFactory.getSkill(i);
                    if (skil != null && !skil.isInvisible() && skil.isFourthJob() && getSkillLevel(skil) <= 0 && getMasterLevel(skil) <= 0 && skil.getMasterLevel() > 0) {
                        changeSkillLevel(skil, (byte) 0, (byte) skil.getMasterLevel()); //usually 10 master
                    }
                }
            }
        }
    }

    public void makeDragon() {
        dragon = new MapleDragon(this);
    }

    public MapleDragon getDragon() {
        return dragon;
    }

    public void gainAp(short ap) {
        this.remainingAp += ap;
        updateSingleStat(MapleStat.AVAILABLEAP, this.remainingAp);
    }

    public void gainSP(int sp) {
        this.remainingSp[GameConstants.getSkillBook(job)] += sp; //default
        client.getSession().write(MaplePacketCreator.updateSp(this, false));
        client.getSession().write(UIPacket.getSPMsg((byte) sp, (short) job));
    }

    public void gainSP(int sp, final int skillbook) {
        this.remainingSp[skillbook] += sp; //default
        client.getSession().write(MaplePacketCreator.updateSp(this, false));
        client.getSession().write(UIPacket.getSPMsg((byte) sp, (short) job));
    }

    public void resetSP(int sp) {
        for (int i = 0; i < this.remainingSp.length; i++) {
            this.remainingSp[i] = sp;
        }
        updateSingleStat(MapleStat.AVAILABLESP, getRemainingSp());
        //   this.client.getSession().write(MaplePacketCreator.updateSp(this, false));
    }

    public void resetAPSP() {
        for (int i = 0; i < remainingSp.length; i++) {
            this.remainingSp[i] = 0;
        }
        client.getSession().write(MaplePacketCreator.updateSp(this, false));
        gainAp((short) -this.remainingAp);
    }

    public int getAllSkillLevels() {
        int rett = 0;
        for (Map.Entry ret : this.skills.entrySet()) {
            if ((!((Skill) ret.getKey()).isBeginnerSkill()) && (((SkillEntry) ret.getValue()).skillevel > 0)) {
                rett += ((SkillEntry) ret.getValue()).skillevel;
            }
        }
        return rett;
    }

    public void changeSkillLevel(final ISkill skill, int newLevel, int newMasterlevel) { //1 month
        if (skill == null) {
            return;
        }
        changeSkillLevel(skill, newLevel, newMasterlevel, skill.isTimeLimited() ? (System.currentTimeMillis() + (long) (30L * 24L * 60L * 60L * 1000L)) : -1);
    }

    public void changeSkillLevel(final ISkill skill, int newLevel, int newMasterlevel, long expiration) {
        if (skill == null || (!GameConstants.isApplicableSkill(skill.getId()) && !GameConstants.isApplicableSkill_(skill.getId()))) {
            return;
        }
        client.getSession().write(MaplePacketCreator.updateSkill(skill.getId(), newLevel, newMasterlevel, expiration));
        if (newLevel == 0 && newMasterlevel == 0) {
            if (skills.containsKey(skill)) {
                skills.remove(skill);
            } else {
                return; //nothing happen
            }
        } else {
            skills.put(skill, new SkillEntry(newLevel, newMasterlevel, expiration));
        }
        if (GameConstants.isRecoveryIncSkill(skill.getId())) {
            stats.relocHeal();
        } else if (GameConstants.isElementAmp_Skill(skill.getId())) {
            stats.recalcLocalStats();
        }

    }

    public void changeSkillLevel_Skip(final ISkill skill, int newLevel, int newMasterlevel) {
        if (skill == null) {
            return;
        }
        client.getSession().write(MaplePacketCreator.updateSkill(skill.getId(), newLevel, newMasterlevel, -1L));
        if (newLevel == 0 && newMasterlevel == 0) {
            if (skills.containsKey(skill)) {
                skills.remove(skill);
            } else {
                return; //nothing happen
            }
        } else {
            skills.put(skill, new SkillEntry(newLevel, newMasterlevel, -1L));
        }

    }

    public void playerDead() {
        final MapleStatEffect statss = getStatForBuff(MapleBuffStat.SOUL_STONE);
        if (statss != null) {
            dropMessage(5, "You have been revived by Soul Stone.");
            getStat().setHp(((getStat().getMaxHp() / 100) * statss.getX()));
            setStance(0);
            changeMap(getMap(), getMap().getPortal(0));
            return;
        }
        int[] charmID = {5130000, 5130002, 5131000, 4031283, 4140903};

        int possesed = 0;
        int i;

        //Check for charms
        for (i = 0; i < charmID.length; i++) {
            int quantity = getItemQuantity(charmID[i], false);
            if (possesed == 0 && quantity > 0) {
                possesed = quantity;
                break;
            }
        }
        if (possesed > 0) {
            possesed -= 1;
            getClient().getSession().write(MaplePacketCreator.serverNotice(5, "因使用了 [护身符] 死亡后您的经验不会减少！剩余 (" + possesed + " 个)"));
            MapleInventoryManipulator.removeById(getClient(), MapleItemInformationProvider.getInstance().getInventoryType(charmID[i]), charmID[i], 1, true, false);
        } else {
            if (getEventInstance() != null) {
                getEventInstance().playerKilled(this);
            }
            dispelSkill(0);
            cancelEffectFromBuffStat(MapleBuffStat.MORPH);
            cancelEffectFromBuffStat(MapleBuffStat.骑兽技能);
            cancelEffectFromBuffStat(MapleBuffStat.SUMMON);
            cancelEffectFromBuffStat(MapleBuffStat.REAPER);
            cancelEffectFromBuffStat(MapleBuffStat.PUPPET);
            checkFollow();
            if (job != 0 && job != 1000 && job != 2000) {
                float diepercentage = 0.0f;
                int expforlevel = GameConstants.getExpNeededForLevel(level);
                if (map.isTown() || FieldLimitType.RegularExpLoss.check(map.getFieldLimit())) {
                    diepercentage = 0.01f;
                } else {
                    float v8 = 0.0f;
                    if (this.job / 100 == 3) {
                        v8 = 0.08f;
                    } else {
                        v8 = 0.2f;
                    }
                    diepercentage = (float) (v8 / this.stats.getLuk() + 0.05);
                }
                int v10 = (int) (exp - (long) ((double) expforlevel * diepercentage));
                if (v10 < 0) {
                    v10 = 0;
                }
                this.exp = v10;
            }
            this.updateSingleStat(MapleStat.EXP, this.exp);
            if (!stats.checkEquipDurabilitys(this, -100)) { //i guess this is how it works ?
                dropMessage(5, "耐久度已经归零.");
            } //lol
            if (pyramidSubway != null) {
                stats.setHp((short) 50);
                pyramidSubway.fail(this);
            }
        }
    }

    public void updatePartyMemberHP() {
        if (party != null) {
            final int channel = client.getChannel();
            for (MaplePartyCharacter partychar : party.getMembers()) {
                if (partychar.getMapid() == getMapId() && partychar.getChannel() == channel) {
                    MapleCharacter other = ChannelServer.getInstance(channel).getPlayerStorage().getCharacterByName(partychar.getName());
                    if (other != null) {
                        other.getClient().getSession().write(MaplePacketCreator.updatePartyMemberHP(getId(), stats.getHp(), stats.getCurrentMaxHp()));
                    }
                }
            }
        }
    }

    public void receivePartyMemberHP() {
        if (party == null) {
            return;
        }
        int channel = client.getChannel();
        for (MaplePartyCharacter partychar : party.getMembers()) {
            if (partychar.getMapid() == getMapId() && partychar.getChannel() == channel) {
                MapleCharacter other = ChannelServer.getInstance(channel).getPlayerStorage().getCharacterByName(partychar.getName());
                if (other != null) {
                    client.getSession().write(MaplePacketCreator.updatePartyMemberHP(other.getId(), other.getStat().getHp(), other.getStat().getCurrentMaxHp()));
                }
            }
        }
    }

    public void healHP(int delta) {
        addHP(delta);
//        client.getSession().write(MaplePacketCreator.showOwnHpHealed(delta));
//        getMap().broadcastMessage(this, MaplePacketCreator.showHpHealed(getId(), delta), false);
    }

    public void healMP(int delta) {
        addMP(delta);
//        client.getSession().write(MaplePacketCreator.showOwnHpHealed(delta));
//        getMap().broadcastMessage(this, MaplePacketCreator.showHpHealed(getId(), delta), false);
    }

    /**
     * Convenience function which adds the supplied parameter to the current hp
     * then directly does a updateSingleStat.
     *
     * @param delta
     * @see MapleCharacter#setHp(int)
     */
    public void addHP(int delta) {
        if (stats.setHp(stats.getHp() + delta)) {
            updateSingleStat(MapleStat.HP, stats.getHp());
        }
    }

    /**
     * Convenience function which adds the supplied parameter to the current mp
     * then directly does a updateSingleStat.
     *
     * @param delta
     * @see MapleCharacter#setMp(int)
     */
    public void addMP(int delta) {
        if (stats.setMp(stats.getMp() + delta)) {
            updateSingleStat(MapleStat.MP, stats.getMp());
        }
    }

    public void addMPHP(int hpDiff, int mpDiff) {
        List<Pair<MapleStat, Integer>> statups = new ArrayList<Pair<MapleStat, Integer>>();

        if (stats.setHp(stats.getHp() + hpDiff)) {
            statups.add(new Pair<MapleStat, Integer>(MapleStat.HP, Integer.valueOf(stats.getHp())));
        }
        if (stats.setMp(stats.getMp() + mpDiff)) {
            statups.add(new Pair<MapleStat, Integer>(MapleStat.MP, Integer.valueOf(stats.getMp())));
        }
        if (statups.size() > 0) {
            client.getSession().write(MaplePacketCreator.updatePlayerStats(statups, getJob()));
        }
    }

    private long lastHPTime, lastMPTime, lastCheckPeriodTime, lastMoveItemTime, lastQuestTime;
    public long lastRecoveryTime = 0;

    public final boolean canQuestAction() {
        if (lastQuestTime + 250 > System.currentTimeMillis()) {
            return false;
        }
        lastQuestTime = System.currentTimeMillis();
        return true;
    }

    private void prepareRecovery() {
        lastRecoveryTime = System.currentTimeMillis();
    }

    public boolean canRecovery() {
        return lastRecoveryTime > 0 && lastRecoveryTime + 5000 < System.currentTimeMillis() + 5000;
    }

    public void doRecovery() {
        MapleStatEffect eff = getStatForBuff(MapleBuffStat.RECOVERY);
        if (eff != null) {
            prepareRecovery();
            if (stats.getHp() > stats.getCurrentMaxHp()) {
                this.cancelEffectFromBuffStat(MapleBuffStat.RECOVERY);
            } else {
                healHP(eff.getX());
            }
        }
    }

    public final boolean canHP() {
        if (lastHPTime + 5000 > System.currentTimeMillis()) {
            return false;
        }
        lastHPTime = System.currentTimeMillis();
        return true;
    }

    public final boolean canMP() {
        if (lastMPTime + 5000 > System.currentTimeMillis()) {
            return false;
        }
        lastMPTime = System.currentTimeMillis();
        return true;
    }

    public final boolean canCheckPeriod() {
        if (lastCheckPeriodTime + 30000 > System.currentTimeMillis()) {
            return false;
        }
        lastCheckPeriodTime = System.currentTimeMillis();
        return true;
    }

    public final boolean canMoveItem() {
        if (lastMoveItemTime + 250 > System.currentTimeMillis()) {
            return false;
        }
        lastMoveItemTime = System.currentTimeMillis();
        return true;
    }

    public void updateSingleStat(MapleStat stat, int newval) {
        updateSingleStat(stat, newval, false);
    }

    /**
     * Updates a single stat of this MapleCharacter for the client. This method
     * only creates and sends an update packet, it does not update the stat
     * stored in this MapleCharacter instance.
     *
     * @param stat
     * @param newval
     * @param itemReaction
     */
    public void updateSingleStat(MapleStat stat, int newval, boolean itemReaction) {
        /*
         * if (stat == MapleStat.AVAILABLESP) {
         * client.getSession().write(MaplePacketCreator.updateSp(this,
         * itemReaction, false)); return; }
         */
        Pair<MapleStat, Integer> statpair = new Pair<MapleStat, Integer>(stat, Integer.valueOf(newval));
        client.getSession().write(MaplePacketCreator.updatePlayerStats(Collections.singletonList(statpair), itemReaction, getJob()));
    }

    public void gainExp(final int total, final boolean show, final boolean inChat, final boolean white) {
        try {
            int prevexp = getExp();
            int needed = GameConstants.getExpNeededForLevel(level);
            if (level >= 200 || (GameConstants.isKOC(job) && level >= 120)) {
                if (exp + total > needed) {
                    setExp(needed);
                } else {
                    exp += total;
                }
            } else {
                boolean leveled = false;
                if (exp + total >= needed) {
                    exp += total;
                    levelUp();
                    leveled = true;
                    needed = GameConstants.getExpNeededForLevel(level);
                    if (exp > needed) {
                        setExp(needed);
                    }
                } else {
                    exp += total;
                }
                if (total > 0) {
                    // familyRep(prevexp, needed, leveled);
                }
            }
            if (total != 0) {
                if (exp < 0) { // After adding, and negative
                    if (total > 0) {
                        setExp(needed);
                    } else if (total < 0) {
                        setExp(0);
                    }
                }
                if (show) { // still show the expgain even if it's not there
                    client.getSession().write(MaplePacketCreator.GainEXP_Others(total, inChat, white));
                }
                /*
                 * if (total > 0) { stats.checkEquipLevels(this, total); //gms
                 * like }
                 */
                updateSingleStat(MapleStat.EXP, getExp());
            }
        } catch (Exception e) {
            FileoutputUtil.outputFileError(FileoutputUtil.ScriptEx_Log, e); //all jobs throw errors :(
        }
    }

    public void familyRep(int prevexp, int needed, boolean leveled) {
        if (mfc != null) {
            int onepercent = needed / 100;
            int percentrep = (prevexp / onepercent + getExp() / onepercent);
            if (leveled) {
                percentrep = 100 - percentrep + (level / 2);
            }
            if (percentrep > 0) {
                int sensen = World.Family.setRep(mfc.getFamilyId(), mfc.getSeniorId(), percentrep, level);
                if (sensen > 0) {
                    World.Family.setRep(mfc.getFamilyId(), sensen, percentrep / 2, level); //and we stop here
                }
            }
        }
    }

    public void gainExpMonster(final int gain, final boolean show, final boolean white, final byte pty,
                               int wedding_EXP, int Class_Bonus_EXP, int Equipment_Bonus_EXP, int Premium_Bonus_EXP) {
        int total = gain + Class_Bonus_EXP + Equipment_Bonus_EXP + Premium_Bonus_EXP + wedding_EXP;
        int partyinc = 0;
        int prevexp = getExp();
        if (canCheckPeriod()) {
            expirationTask(true, false);
        }
        if (pty > 1) {
            // (Exp / 20) * (member + 1)
            partyinc = (int) (((float) (gain / 20.0)) * (pty + 1));
            total += partyinc;
        }

        if (gain > 0 && total < gain) { //just in case
            total = Integer.MAX_VALUE;
        }
        int needed = GameConstants.getExpNeededForLevel(level);
        if (level >= 200 || (GameConstants.isKOC(job) && level >= 120)) {
            if (exp + total > needed) {
                setExp(needed);
            } else {
                exp += total;
            }
        } else {
            boolean leveled = false;
            if (exp + total >= needed) {
                exp += total;
                levelUp();
                leveled = true;
                needed = GameConstants.getExpNeededForLevel(level);
                if (exp > needed) {
                    setExp(needed);
                }
            } else {
                exp += total;
            }
            if (total > 0) {
                //   familyRep(prevexp, needed, leveled);
            }
        }
        if (gain != 0) {
            if (exp < 0) { // After adding, and negative
                if (gain > 0) {
                    setExp(GameConstants.getExpNeededForLevel(level));
                } else if (gain < 0) {
                    setExp(0);
                }
            }
            updateSingleStat(MapleStat.EXP, getExp());
            if (show) { // still show the expgain even if it's not there
                client.getSession().write(MaplePacketCreator.GainEXP_Monster(gain, white, wedding_EXP, partyinc, Class_Bonus_EXP, Equipment_Bonus_EXP, Premium_Bonus_EXP));
            }
            // stats.checkEquipLevels(this, total);
        }
    }

    public void forceReAddItem_NoUpdate(IItem item, MapleInventoryType type) {
        getInventory(type).removeSlot(item.getPosition());
        getInventory(type).addFromDB(item);
    }

    public void forceReAddItem(IItem item, MapleInventoryType type) { //used for stuff like durability, item exp/level, probably owner?
        forceReAddItem_NoUpdate(item, type);
        if (type != MapleInventoryType.UNDEFINED) {
            client.getSession().write(MaplePacketCreator.updateSpecialItemUse(item, type == MapleInventoryType.EQUIPPED ? (byte) 1 : type.getType()));
        }
    }

    public void forceReAddItem_Flag(IItem item, MapleInventoryType type) { //used for flags
        forceReAddItem_NoUpdate(item, type);
        if (type != MapleInventoryType.UNDEFINED) {
            client.getSession().write(MaplePacketCreator.updateSpecialItemUse_(item, type == MapleInventoryType.EQUIPPED ? (byte) 1 : type.getType()));
        }
    }

    public void silentPartyUpdate() {
        if (party != null) {
            World.Party.updateParty(party.getId(), PartyOperation.SILENT_UPDATE, new MaplePartyCharacter(this));
        }
    }

    public boolean isGM() {
        return gmLevel > 0;
    }

    public boolean isAdmin() {
        return gmLevel >= 2;
    }

    public int getGMLevel() {
        return gmLevel;
    }

    public boolean isPlayer() {
        return gmLevel == 0;
    }

    public boolean hasGmLevel(int level) {
        return gmLevel >= level;
    }

    public final MapleInventory getInventory(MapleInventoryType type) {
        return inventory[type.ordinal()];
    }

    public final MapleInventory[] getInventorys() {
        return inventory;
    }

    public final void expirationTask() {
        expirationTask(false);
    }

    public final void expirationTask(boolean pending) {
        expirationTask(false, pending);
    }

    public final void expirationTask(boolean packet, boolean pending) {
        if (pending) {
            if (pendingExpiration != null) {
                for (Integer z : pendingExpiration) {
                    client.sendPacket(MTSCSPacket.itemExpired(z));
                }
            }
            pendingExpiration = null;
            if (pendingSkills != null) {
                for (Integer z : pendingSkills) {
                    client.sendPacket(MaplePacketCreator.updateSkill(z, 0, 0, -1));
                    client.sendPacket(MaplePacketCreator.serverNotice(5, "[" + SkillFactory.getSkillName(z) + "] 技能已经过期"));
                }
            } //not real msg
            pendingSkills = null;
            return;
        }
        long expiration;
        final List<Integer> ret = new ArrayList<>();
        final long currenttime = System.currentTimeMillis();
        final List<Pair<MapleInventoryType, IItem>> toberemove = new ArrayList<Pair<MapleInventoryType, IItem>>(); // This is here to prevent deadlock.
        final List<IItem> tobeunlock = new ArrayList<IItem>(); // This is here to prevent deadlock.

        for (final MapleInventoryType inv : MapleInventoryType.values()) {
            for (final IItem item : getInventory(inv)) {
                expiration = item.getExpiration();

                if (expiration != -1 && !GameConstants.isPet(item.getItemId()) && currenttime > expiration) {
                    if (ItemFlag.LOCK.check(item.getFlag())) {
                        tobeunlock.add(item);
                    } else if (currenttime > expiration) {
                        toberemove.add(new Pair<MapleInventoryType, IItem>(inv, item));
                    }
                } else if (item.getItemId() == 5000054 && item.getPet() != null && item.getPet().getSecondsLeft() <= 0) {
                    toberemove.add(new Pair<MapleInventoryType, IItem>(inv, item));
                }
            }
        }
        IItem item;
        for (final Pair<MapleInventoryType, IItem> itemz : toberemove) {
            item = itemz.getRight();
            ret.add(item.getItemId());
            if (packet) {
                getInventory(itemz.getLeft()).removeItem(item.getPosition(), item.getQuantity(), false, this);
            } else {
                getInventory(itemz.getLeft()).removeItem(item.getPosition(), item.getQuantity(), false);
            }
        }
        for (final IItem itemz : tobeunlock) {
            itemz.setExpiration(-1);
            itemz.setFlag((byte) (itemz.getFlag() - ItemFlag.LOCK.getValue()));
        }

        this.pendingExpiration = ret;

        final List<Integer> skilz = new ArrayList<>();
        final List<ISkill> toberem = new ArrayList<>();
        for (Entry<ISkill, SkillEntry> skil : skills.entrySet()) {
            if (skil.getValue().expiration != -1 && currenttime > skil.getValue().expiration) {
                toberem.add(skil.getKey());
            }
        }
        for (ISkill skil : toberem) {
            skilz.add(skil.getId());
            this.skills.remove(skil);
        }
        this.pendingSkills = skilz;
    }

    public MapleShop getShop() {
        return shop;
    }

    public void setShop(MapleShop shop) {
        this.shop = shop;
    }

    public int getMeso() {
        return meso;
    }

    public final int[] getSavedLocations() {
        return savedLocations;
    }

    public int getSavedLocation(SavedLocationType type) {
        return savedLocations[type.getValue()];
    }

    public void saveLocation(SavedLocationType type) {
        savedLocations[type.getValue()] = getMapId();
    }

    public void saveLocation(SavedLocationType type, int mapz) {
        savedLocations[type.getValue()] = mapz;
    }

    public void clearSavedLocation(SavedLocationType type) {
        savedLocations[type.getValue()] = -1;
    }

    public long getDY() {
        return maplepoints;
    }

    public void setDY(int set) {
        this.maplepoints = set;
    }

    public void gainDY(int gain) {
        this.maplepoints += gain;
        // setDY(getDY() + gain);
    }

    public void gainMeso(int gain, boolean show) {
        gainMeso(gain, show, false, false);
    }

    public void gainMeso(int gain, boolean show, boolean enableActions) {
        gainMeso(gain, show, enableActions, false);
    }

    public void gainMeso(int gain, boolean show, boolean enableActions, boolean inChat) {
        if (meso + gain < 0) {
            client.getSession().write(MaplePacketCreator.enableActions());
            return;
        }
        meso += gain;
        updateSingleStat(MapleStat.MESO, meso, enableActions);
        if (show) {
            client.getSession().write(MaplePacketCreator.showMesoGain(gain, inChat));
        }
    }

    public void controlMonster(MapleMonster monster, boolean aggro) {
        if (clone) {
            return;
        }
        monster.setController(this);
        controlled.add(monster);
        client.getSession().write(MobPacket.controlMonster(monster, false, aggro));
    }

    public void stopControllingMonster(MapleMonster monster) {
        if (clone) {
            return;
        }
        if (monster != null && controlled.contains(monster)) {
            controlled.remove(monster);
        }
    }

    public void checkMonsterAggro(MapleMonster monster) {
        if (clone || monster == null) {
            return;
        }
        if (monster.getController() == this) {
            monster.setControllerHasAggro(true);
        } else {
            monster.switchController(this, true);
        }
    }

    public Set<MapleMonster> getControlled() {
        return controlled;
    }

    public int getControlledSize() {
        return controlled.size();
    }

    public int getAccountID() {
        return accountid;
    }

    public void mobKilled(final int id, final int skillID) {
        for (MapleQuestStatus q : quests.values()) {
            if (q.getStatus() != 1 || !q.hasMobKills()) {
                continue;
            }
            if (q.mobKilled(id, skillID)) {
                client.getSession().write(MaplePacketCreator.updateQuestMobKills(q));
                if (q.getQuest().canComplete(this, null)) {
                    client.getSession().write(MaplePacketCreator.getShowQuestCompletion(q.getQuest().getId()));
                }
            }
        }
    }

    public final List<MapleQuestStatus> getStartedQuests() {
        List<MapleQuestStatus> ret = new LinkedList<MapleQuestStatus>();
        for (MapleQuestStatus q : quests.values()) {
            if (q.getStatus() == 1 && !(q.isCustom())) {
                ret.add(q);
            }
        }
        return ret;
    }

    public final List<MapleQuestStatus> getCompletedQuests() {
        List<MapleQuestStatus> ret = new LinkedList<MapleQuestStatus>();
        for (MapleQuestStatus q : quests.values()) {
            if (q.getStatus() == 2 && !(q.isCustom())) {
                ret.add(q);
            }
        }
        return ret;
    }

    public Map<ISkill, SkillEntry> getSkills() {
        return Collections.unmodifiableMap(skills);
    }

    public byte getSkillLevel(final ISkill skill) {
        final SkillEntry ret = skills.get(skill);
        if (ret == null || ret.skillevel <= 0) {
            return 0;
        }
        return (byte) Math.min(skill.getMaxLevel(), ret.skillevel + (skill.isBeginnerSkill() ? 0 : stats.incAllskill));
    }

    public int getMasterLevel(int skill) {
        return getMasterLevel(SkillFactory.getSkill(skill));
    }

    public int getMasterLevel(final ISkill skill) {
        final SkillEntry ret = skills.get(skill);
        if (ret == null) {
            return 0;
        }
        return ret.masterlevel;
    }

    public void levelUp() {
        //  getClient().StartWindow();
        if (GameConstants.isKOC(job)) {
            if (level <= 70) {
                remainingAp += 6;
            } else {
                remainingAp += 5;
            }
        } else {
            remainingAp += 5;
        }
        int maxhp = stats.getMaxHp();
        int maxmp = stats.getMaxMp();

        if (job == 0 || job == 1000 || job == 2000 || job == 2001 || job == 3000) { // Beginner
            maxhp += Randomizer.rand(12, 16);
            maxmp += Randomizer.rand(10, 12);
        } else if (job >= 100 && job <= 132) { // Warrior
            final ISkill improvingMaxHP = SkillFactory.getSkill(1000001);
            final int slevel = getSkillLevel(improvingMaxHP);
            if (slevel > 0) {
                maxhp += improvingMaxHP.getEffect(slevel).getX();
            }
            maxhp += Randomizer.rand(24, 28);
            maxmp += Randomizer.rand(4, 6);
        } else if (job >= 200 && job <= 232) { // Magician
            final ISkill improvingMaxMP = SkillFactory.getSkill(2000001);
            final int slevel = getSkillLevel(improvingMaxMP);
            if (slevel > 0) {
                maxmp += improvingMaxMP.getEffect(slevel).getX() * 2;
            }
            maxhp += Randomizer.rand(10, 14);
            maxmp += Randomizer.rand(22, 24);
        } else if (job >= 3200 && job <= 3212) { //battle mages get their own little neat thing
            maxhp += Randomizer.rand(20, 24);
            maxmp += Randomizer.rand(42, 44);
        } else if ((job >= 300 && job <= 322) || (job >= 400 && job <= 434) || (job >= 1300 && job <= 1311) || (job >= 1400 && job <= 1411) || (job >= 3300 && job <= 3312)) { // Bowman, Thief, Wind Breaker and Night Walker
            maxhp += Randomizer.rand(20, 24);
            maxmp += Randomizer.rand(14, 16);
        } else if ((job >= 500 && job <= 522) || (job >= 3500 && job <= 3512)) { // Pirate
            final ISkill improvingMaxHP = SkillFactory.getSkill(5100000);
            final int slevel = getSkillLevel(improvingMaxHP);
            if (slevel > 0) {
                maxhp += improvingMaxHP.getEffect(slevel).getX();
            }
            maxhp += Randomizer.rand(22, 26);
            maxmp += Randomizer.rand(18, 22);
        } else if (job >= 1100 && job <= 1111) { // Soul Master
            final ISkill improvingMaxHP = SkillFactory.getSkill(11000000);
            final int slevel = getSkillLevel(improvingMaxHP);
            if (slevel > 0) {
                maxhp += improvingMaxHP.getEffect(slevel).getX();
            }
            maxhp += Randomizer.rand(24, 28);
            maxmp += Randomizer.rand(4, 6);
        } else if (job >= 1200 && job <= 1211) { // Flame Wizard
            final ISkill improvingMaxMP = SkillFactory.getSkill(12000000);
            final int slevel = getSkillLevel(improvingMaxMP);
            if (slevel > 0) {
                maxmp += improvingMaxMP.getEffect(slevel).getX() * 2;
            }
            maxhp += Randomizer.rand(10, 14);
            maxmp += Randomizer.rand(22, 24);
        } else if (job >= 1500 && job <= 1512) { // Pirate
            final ISkill improvingMaxHP = SkillFactory.getSkill(15100000);
            final int slevel = getSkillLevel(improvingMaxHP);
            if (slevel > 0) {
                maxhp += improvingMaxHP.getEffect(slevel).getX();
            }
            maxhp += Randomizer.rand(22, 26);
            maxmp += Randomizer.rand(18, 22);
        } else if (job >= 2100 && job <= 2112) { // Aran
            maxhp += Randomizer.rand(50, 52);
            maxmp += Randomizer.rand(4, 6);
        } else if (job >= 2200 && job <= 2218) { // Evan
            maxhp += Randomizer.rand(12, 16);
            maxmp += Randomizer.rand(50, 52);
        } else { // GameMaster
            maxhp += Randomizer.rand(50, 100);
            maxmp += Randomizer.rand(50, 100);
        }
        maxmp += stats.getTotalInt() / 10;
        exp -= GameConstants.getExpNeededForLevel(level);
        level += 1;
        int level = getLevel();

        // 成就系統
        /*
         * if (level == 30) { finishAchievement(2); } if (level == 70) {
         * finishAchievement(3); } if (level == 120) { finishAchievement(4); }
         * if (level == 200) { finishAchievement(5); }
         */
        if (!isGM()) {
            if (level == 10 || level == 30 || level == 70 || level == 120 || level == 200) {
                final StringBuilder sb = new StringBuilder("[恭喜] ");
                final IItem medal = getInventory(MapleInventoryType.EQUIPPED).getItem((byte) -26);
                if (medal != null) { // Medal
                    sb.append("<");
                    sb.append(MapleItemInformationProvider.getInstance().getName(medal.getItemId()));
                    sb.append("> ");
                }
                sb.append(getName());
                sb.append(" 达到了 " + level + " 级,让我们一起恭喜他/她吧!");
                World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, sb.toString()).getBytes());
            }
        }
        // maxhp = (short) Math.min(30000, Math.abs(maxhp));
        //maxmp = (short) Math.min(30000, Math.abs(maxmp));

        maxhp = Math.min(30000, maxhp);
        maxmp = Math.min(30000, maxmp);
        final List<Pair<MapleStat, Integer>> statup = new ArrayList<Pair<MapleStat, Integer>>(8);

        statup.add(new Pair(MapleStat.AVAILABLEAP, Integer.valueOf(this.remainingAp)));
        statup.add(new Pair<MapleStat, Integer>(MapleStat.MAXHP, maxhp));
        statup.add(new Pair<MapleStat, Integer>(MapleStat.MAXMP, maxmp));
        statup.add(new Pair<MapleStat, Integer>(MapleStat.HP, maxhp));
        statup.add(new Pair<MapleStat, Integer>(MapleStat.MP, maxmp));
        statup.add(new Pair<MapleStat, Integer>(MapleStat.EXP, exp));
        statup.add(new Pair<MapleStat, Integer>(MapleStat.LEVEL, (int) level));

        if (isGM() || (job != 0 && job != 1000 && job != 2000 && job != 2001 && job != 3000)) { // Not Beginner, Nobless and Legend
            remainingSp[GameConstants.getSkillBook(this.job)] += 3;
            client.getSession().write(MaplePacketCreator.updateSp(this, false));
        } else if (level <= 10) {
            stats.setStr((short) (stats.getStr() + remainingAp));
            remainingAp = 0;

            statup.add(new Pair<MapleStat, Integer>(MapleStat.STR, (int) stats.getStr()));
        }

        statup.add(new Pair<MapleStat, Integer>(MapleStat.AVAILABLEAP, (int) remainingAp));

        stats.setMaxHp((short) maxhp);
        stats.setMaxMp((short) maxmp);
        stats.setHp((short) maxhp);
        stats.setMp((short) maxmp);
        client.getSession().write(MaplePacketCreator.updatePlayerStats(statup, getJob()));
        map.broadcastMessage(this, MaplePacketCreator.showForeignEffect(getId(), 0), false);
        stats.recalcLocalStats();
        silentPartyUpdate();
        guildUpdate();
        familyUpdate();

        saveToDB(false, false);
//                if (GameConstants.isAran(job)) {
//            switch (level) {
//                case 30:
//                    client.getSession().write(MaplePacketCreator.startMapEffect("You have reached level 30! To job advance, go back to Lirin of Rien.", 5120000, true));
//                    break;
//                case 70:
//                    client.getSession().write(MaplePacketCreator.startMapEffect("You have reached level 70! To job advance, talk to your job instructor in El Nath.", 5120000, true));
//                    break;
//                case 120:
//                    client.getSession().write(MaplePacketCreator.startMapEffect("You have reached level 120! To job advance, talk to your job instructor in Leafre.", 5120000, true));
//                    break;
//            }
//        }
//        if (GameConstants.isKOC(job) && level == 70) {
//            client.getSession().write(MaplePacketCreator.startMapEffect("You have reached level 70! To job advance, talk to your job instructor in Erev.", 5120000, true));
//        }
        /*
         * if (GameConstants.isEvan(job)) { switch (level) { case 9:
         * client.getSession().write(MaplePacketCreator.startMapEffect("請確保您完成所有的任務需要達到10級之前，否則你將無法繼續.",
         * 5120000, true)); break; case 10: case 20: case 30: case 40: case 50:
         * case 60: case 80: case 100: case 120: case 160: if (job < 2218) {
         * changeJob(job == 2001 ? 2200 : (job == 2200 ? 2210 : (job + 1)));
         * //automatic } break; } }
         */
        /*
         * if (getSubcategory() == 1) { //db level 2 switch (level) { case 2:
         * client.getSession().write(MaplePacketCreator.startMapEffect("Click
         * the lightbulb above you and accept the [Required] quest. Remake the
         * character if this quest is not showing.", 5120009, true)); break;
         * case 10:
         * client.getSession().write(MaplePacketCreator.startMapEffect("Go and
         * advance to a Rogue at Dark Lord in Kerning City. Make sure you do ALL
         * the [Required] quests.", 5120000, true)); break; case 15:
         * client.getSession().write(MaplePacketCreator.startMapEffect("Make
         * sure you have been doing all the required quests. Remember that
         * saving SP is possible.", 5120000, true)); break; case 20:
         * client.getSession().write(MaplePacketCreator.startMapEffect("You have
         * reached level 20. If you have done all your required quests, you can
         * enter Secret Garden and advance.", 5120000, true)); break; case 30:
         * client.getSession().write(MaplePacketCreator.startMapEffect("You have
         * reached level 30. Please go to Lady Syl to advance.", 5120000,
         * true)); break; case 55:
         * client.getSession().write(MaplePacketCreator.startMapEffect("You have
         * reached level 55. Please go to Lady Syl and do a few quests to
         * advance.", 5120000, true)); break; case 70:
         * client.getSession().write(MaplePacketCreator.startMapEffect("You have
         * reached level 70. Please go to your job instructor in Elnath to
         * advance.", 5120000, true)); break; case 120:
         * client.getSession().write(MaplePacketCreator.startMapEffect("You have
         * reached level 120. Please go to your job instructor in Leafre to
         * advance.", 5120000, true)); break; } }
         */
        //if (map.getForceMove() > 0 && map.getForceMove() <= getLevel()) {
        //    changeMap(map.getReturnMap(), map.getReturnMap().getPortal(0));
        //    dropMessage(5, "You have been expelled from the map.");
        //}
    }

    public void changeKeybinding(int key, int type, int action) {
        if (type != 0) {
            keylayout.Layout().put(key, new Pair<>(type, action));
        } else {
            keylayout.Layout().remove(key);
        }
    }

    public void sendMacros() {
        for (int i = 0; i < 5; i++) {
            if (skillMacros[i] != null) {
                client.getSession().write(MaplePacketCreator.getMacros(skillMacros));
                break;
            }
        }
    }

    public void updateMacros(int position, SkillMacro updateMacro) {
        skillMacros[position] = updateMacro;
    }

    public final SkillMacro[] getMacros() {
        return skillMacros;
    }

    public void tempban(String reason, Calendar duration, int greason, boolean IPMac) {
        if (IPMac) {
            client.banMacs();
        }

        DIPBans bans = new DIPBans();
        bans.setIp(client.getSession().getRemoteAddress().toString().split(":")[0]);
        bans.save();

        client.getSession().close(true);

        DAccount account = character.getAccount();
        account.setTempBan(LocalDateTime.now());
        account.setBanReason(reason);
        account.setgReason(greason);
        account.save();

    }

    public final boolean ban(String reason, boolean IPMac, boolean autoban, boolean hellban) {
        hellban = false;
        if (lastmonthfameids == null) {
            throw new RuntimeException("Trying to ban a non-loaded character (testhack)");
        }

        DAccount account = character.getAccount();
        account.setBanned(autoban);
        account.setBanReason(reason);
        account.save();

        client.banMacs();
//            String ip = client.getSessionIPAddress();
//            if (!"/127.0.0.1".equals(ip)) {
//                ps = con.prepareStatement("INSERT INTO ipbans VALUES (DEFAULT, ?)");
//                ps.setString(1, ip);
//                ps.execute();
//                ps.close();
//            }

        if (hellban) {
            new QDAccount().email.eq(account.getEmail())
                    .asUpdate()
                    .set("banned", autoban ? 2 : 1)
                    .set("banReason", reason)
                    .update();
        }

        client.getSession().close(true);
        return true;
    }

    public static boolean ban(String id, String reason, boolean accountId, int gmlevel, boolean hellban) {
        if (id.matches("/[0-9]{1,3}\\..*")) {
            if (!"/127.0.0.1".equals(id)) {
//                    ps = con.prepareStatement("INSERT INTO ipbans VALUES (DEFAULT, ?)");
//                    ps.setString(1, id);
//                    ps.execute();
//                    ps.close();
            }
            return true;
        }

        Optional<DAccount> optional = new QDCharacter()
                .name.eq(id)
                .findOneOrEmpty()
                // todo gm < ?  --- gmLevel
                .map(DCharacter::getAccount);

        boolean ret = false;
        if (optional.isPresent()) {
            DAccount account = optional.get();
            account.setBanned(true);
            account.setBanReason(reason);
            account.save();

            if (gmlevel > 100) { //admin ban
                String ip = account.getSessionIp();
                if (ip != null && ip.matches("/[0-9]{1,3}\\..*")) {
                    DIPBans bans = new DIPBans();
                    bans.setIp(ip);
                    bans.save();
                }
                if (account.getMac() != null) {
                    String[] macData = account.getMac().split(", ");
                    if (macData.length > 0) {
                        MapleClient.banMacs(macData);
                    }
                }
                if (hellban) {
                    QDAccount qdAccount = new QDAccount().email.eq(account.getEmail());
                    if (account.getSessionIp() != null) {
                        qdAccount.sessionIp.eq(account.getSessionIp());
                    }
                    qdAccount.asUpdate()
                            .set("banned", 1)
                            .set("banReason", reason)
                            .update();
                }
            }
            ret = true;
        }
        return ret;
    }

    /**
     * Oid of players is always = the cid
     */
    @Override
    public int getObjectId() {
        return getId();
    }

    /**
     * Throws unsupported operation exception, oid of players is read only
     */
    @Override
    public void setObjectId(int id) {
        throw new UnsupportedOperationException();
    }

    public MapleStorage getStorage() {
        return storage;
    }

    public void addVisibleMapObject(MapleMapObject mo) {
        if (clone) {
            return;
        }
        visibleMapObjectsLock.writeLock().lock();
        try {
            visibleMapObjects.add(mo);
        } finally {
            visibleMapObjectsLock.writeLock().unlock();
        }
    }

    public void removeVisibleMapObject(MapleMapObject mo) {
        if (clone) {
            return;
        }
        visibleMapObjectsLock.writeLock().lock();
        try {
            visibleMapObjects.remove(mo);
        } finally {
            visibleMapObjectsLock.writeLock().unlock();
        }
    }

    public boolean isMapObjectVisible(MapleMapObject mo) {
        visibleMapObjectsLock.readLock().lock();
        try {
            return !clone && visibleMapObjects.contains(mo);
        } finally {
            visibleMapObjectsLock.readLock().unlock();
        }
    }

    public Collection<MapleMapObject> getAndWriteLockVisibleMapObjects() {
        visibleMapObjectsLock.writeLock().lock();
        return visibleMapObjects;
    }

    public void unlockWriteVisibleMapObjects() {
        visibleMapObjectsLock.writeLock().unlock();
    }

    public boolean isAlive() {
        return stats.getHp() > 0;
    }

    @Override
    public void sendDestroyData(MapleClient client) {
        client.getSession().write(MaplePacketCreator.removePlayerFromMap(this.getObjectId()));
        for (final WeakReference<MapleCharacter> chr : clones) {
            if (chr.get() != null) {
                chr.get().sendDestroyData(client);
            }
        }
    }

    @Override
    public void sendSpawnData(MapleClient client) {
        if (client.getPlayer().allowedToTarget(this)) {
            client.getSession().write(MaplePacketCreator.spawnPlayerMapobject(this));

            for (final MaplePet pet : pets) {
                if (pet.getSummoned()) {
                    client.getSession().write(PetPacket.showPet(this, pet, false, false));
                }
            }
            for (final WeakReference<MapleCharacter> chr : clones) {
                if (chr.get() != null) {
                    chr.get().sendSpawnData(client);
                }
            }
            if (summons != null) {
                for (final MapleSummon summon : summons.values()) {
                    client.getSession().write(MaplePacketCreator.spawnSummon(summon, false));
                }
            }
            if (followid > 0) {
                //   client.getSession().write(MaplePacketCreator.followEffect(followinitiator ? id : followid, followinitiator ? followid : id, null));
            }
        }
    }

    public final void equipChanged() {
        map.broadcastMessage(this, MaplePacketCreator.updateCharLook(this), false);
        map.broadcastMessage(MaplePacketCreator.loveEffect());
        stats.recalcLocalStats();
        if (getMessenger() != null) {
            World.Messenger.updateMessenger(getMessenger().getId(), getName(), client.getChannel());
        }
        //saveToDB(false, false);
    }

    public final MaplePet getPet(final int index) {
        byte count = 0;
        for (final MaplePet pet : pets) {
            if (pet.getSummoned()) {
                if (count == index) {
                    return pet;
                }
                count++;
            }
        }
        return null;
    }

    public void removePetCS(MaplePet pet) {
        pets.remove(pet);
    }

    public void addPet(final MaplePet pet) {
        if (pets.contains(pet)) {
            pets.remove(pet);
        }
        pets.add(pet);
        // So that the pet will be at the last
        // Pet index logic :(
    }

    public void removePet(MaplePet pet, boolean shiftLeft) {
        /*
         * int petslot = getPetIndex(pet); if (petslot < 0) { return; }
         * this.pets.remove(petslot);
        getClient().getSession().write(PetPacket.petStatUpdate(this));
         */
        pet.setSummoned(0);
        /*
         * int slot = -1; for (int i = 0; i < 3; i++) { if (pets[i] != null) {
         * if (pets[i].getUniqueId() == pet.getUniqueId()) { pets[i] = null;
         * slot = i; break; } } } if (shiftLeft) { if (slot > -1) { for (int i =
         * slot; i < 3; i++) { if (i != 2) { pets[i] = pets[i + 1]; } else {
         * pets[i] = null; } } }
         }
         */
    }

    public final byte getPetIndex(final MaplePet petz) {
        byte count = 0;
        for (final MaplePet pet : pets) {
            if (pet.getSummoned()) {
                if (pet == petz) {
                    return count;
                }
                count++;
            }
        }
        return -1;
    }

    public final byte getPetIndex(final int petId) {
        byte count = 0;
        for (final MaplePet pet : pets) {
            if (pet.getSummoned()) {
                if (pet.getUniqueId() == petId) {
                    return count;
                }
                count++;
            }
        }
        return -1;
    }

    public final byte getPetById(final int petId) {
        byte count = 0;
        for (final MaplePet pet : pets) {
            if (pet.getSummoned()) {
                if (pet.getPetItemId() == petId) {
                    return count;
                }
                count++;
            }
        }
        return -1;
    }

    public List<MaplePet> getPets() {
        return this.pets;
    }

    public int getNoPets() {
        return this.pets.size();
    }

    public final void unequipAllPets() {
        for (final MaplePet pet : pets) {
            if (pet != null) {
                unequipPet(pet, true, false);
            }
        }
    }

    public void unequipPet(MaplePet pet, boolean shiftLeft, boolean hunger) {
        if (pet.getSummoned()) {
            pet.saveToDb();
            this.client.getSession().write(PetPacket.updatePet(pet, getInventory(MapleInventoryType.CASH).getItem(pet.getInventoryPosition()), false));
            if (this.map != null) {
                this.map.broadcastMessage(this, PetPacket.showPet(this, pet, true, hunger), true);
            }
            //map.broadcastMessage(this, PetPacket.showPet(this, pet, true, hunger), true);
            //List<Pair<MapleStat, Integer>> stats = new ArrayList<Pair<MapleStat, Integer>>();
            //stats.add(new Pair<MapleStat, Integer>(MapleStat.PET, Integer.valueOf(0)));
            removePet(pet, shiftLeft);
            client.getSession().write(PetPacket.petStatUpdate(this));
            client.getSession().write(MaplePacketCreator.enableActions());
        }
    }

    public final long getLastFameTime() {
        return lastfametime;
    }

    public final List<Integer> getFamedCharacters() {
        return lastmonthfameids;
    }

    public FameStatus canGiveFame(MapleCharacter from) {
        if (lastfametime >= System.currentTimeMillis() - 60 * 60 * 24 * 1000) {
            return FameStatus.NOT_TODAY;
        } else if (from == null || lastmonthfameids == null || lastmonthfameids.contains(Integer.valueOf(from.getId()))) {
            return FameStatus.NOT_THIS_MONTH;
        }
        return FameStatus.OK;
    }

    public void hasGivenFame(MapleCharacter to) {
        lastfametime = System.currentTimeMillis();
        lastmonthfameids.add(to.getId());

        DFameLog fameLog = new DFameLog();
        fameLog.setCharacter(character);
        fameLog.setTo(to.character);
        fameLog.save();
    }

    public final MapleKeyLayout getKeyLayout() {
        return this.keylayout;
    }

    public MapleParty getParty() {
        return party;
    }

    public int getPartyId() {
        return (party != null ? party.getId() : -1);
    }

    public int getWorld() {
        return world;
    }

    public void setWorld(byte world) {
        this.world = world;
    }

    public void setParty(MapleParty party) {
        this.party = party;
    }

    public MapleTrade getTrade() {
        return trade;
    }

    public void setTrade(MapleTrade trade) {
        this.trade = trade;
    }

    public EventInstanceManager getEventInstance() {
        return eventInstance;
    }

    public void setEventInstance(EventInstanceManager eventInstance) {
        this.eventInstance = eventInstance;
    }

    public void addDoor(MapleDoor door) {
        doors.add(door);
    }

    public void clearDoors() {
        doors.clear();
    }

    public List<MapleDoor> getDoors() {
        return new ArrayList<MapleDoor>(doors);
    }

    public void setSmega() {
        if (smega) {
            smega = false;
            dropMessage(5, "You have set megaphone to disabled mode");
        } else {
            smega = true;
            dropMessage(5, "You have set megaphone to enabled mode");
        }
    }

    public boolean getSmega() {
        return smega;
    }

    public Map<Integer, MapleSummon> getSummons() {
        return summons;
    }

    public int getChair() {
        return chair;
    }

    public int getItemEffect() {
        return itemEffect;
    }

    public void setChair(int chair) {
        this.chair = chair;
        stats.relocHeal();
    }

    public void setItemEffect(int itemEffect) {
        this.itemEffect = itemEffect;
    }

    @Override
    public MapleMapObjectType getType() {
        return MapleMapObjectType.PLAYER;
    }

    public int getFamilyId() {
        if (mfc == null) {
            return 0;
        }
        return mfc.getFamilyId();
    }

    public int getSeniorId() {
        if (mfc == null) {
            return 0;
        }
        return mfc.getSeniorId();
    }

    public int getJunior1() {
        if (mfc == null) {
            return 0;
        }
        return mfc.getJunior1();
    }

    public int getJunior2() {
        if (mfc == null) {
            return 0;
        }
        return mfc.getJunior2();
    }

    public int getCurrentRep() {
        return currentrep;
    }

    public int getTotalRep() {
        return totalrep;
    }

    public void setCurrentRep(int _rank) {
        currentrep = _rank;
        if (mfc != null) {
            mfc.setCurrentRep(_rank);
        }
    }

    public void setTotalRep(int _rank) {
        totalrep = _rank;
        if (mfc != null) {
            mfc.setTotalRep(_rank);
        }
    }

    public int getGuildId() {
        if (character.getGuild() ==null){
            return 0;
        }
        return character.getGuild().getId();
    }

    public byte getGuildRank() {
        return (byte) guildrank;
    }

    public void setGuildId(int _id) {
        guildid = _id;
        if (guildid > 0) {
            if (mgc == null) {
                mgc = new MapleGuildCharacter(this.character, -1, false);

            } else {
                mgc.character.setGuild(new QDGuild().id.eq(guildid).findOne());
            }
        } else {
            mgc = null;
        }
    }

    public void setGuildRank(int _rank) {
        guildrank = _rank;
        if (mgc != null) {
            mgc.character.setGuildRank((_rank));
        }
    }

    public MapleGuildCharacter getMGC() {
        return mgc;
    }

    public void setAllianceRank(int rank) {
        allianceRank = rank;
        if (mgc != null) {
            mgc.character.setAllianceRank(rank);
        }
    }

    public byte getAllianceRank() {
        return (byte) allianceRank;
    }

    public MapleGuild getGuild() {
        if (getGuildId() <= 0) {
            return null;
        }
        return World.Guild.getGuild(getGuildId());
    }

    public void guildUpdate() {
        if (guildid <= 0) {
            return;
        }
        mgc.character.setLevel(level);
        mgc.character.setJob((job));
        World.Guild.memberLevelJobUpdate(mgc);
    }

    public void saveGuildStatus() {
        MapleGuild.setOfflineGuildStatus(guildid, guildrank, allianceRank, id);
    }

    public void familyUpdate() {
        if (mfc == null) {
            return;
        }
        World.Family.memberFamilyUpdate(mfc, this);
    }

    public void saveFamilyStatus() {
        /*try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("UPDATE characters SET familyid = ?, seniorid = ?, junior1 = ?, junior2 = ? WHERE id = ?");
            if (mfc == null) {
                ps.setInt(1, 0);
                ps.setInt(2, 0);
                ps.setInt(3, 0);
                ps.setInt(4, 0);
            } else {
                ps.setInt(1, mfc.getFamilyId());
                ps.setInt(2, mfc.getSeniorId());
                ps.setInt(3, mfc.getJunior1());
                ps.setInt(4, mfc.getJunior2());
            }
            ps.setInt(5, id);
            ps.execute();
            ps.close();
        } catch (SQLException se) {
            LOGGER.debug("SQLException: " + se.getLocalizedMessage());
            se.printStackTrace();
        }*/
        //MapleFamily.setOfflineFamilyStatus(familyid, seniorid, junior1, junior2, currentrep, totalrep, id);
    }

    public void modifyCSPoints(int type, int quantity) {
        modifyCSPoints(type, quantity, false);
    }

    public void dropMessage(String message) {
        dropMessage(6, message);
    }

    public void modifyCSPoints(int type, int quantity, boolean show) {

        switch (type) {
            case 1:
                if (acash + quantity < 0) {
                    if (show) {
                        dropMessage(5, "你的点卷已经满了");
                    }
                    return;
                }
                acash += quantity;
                break;
            case 2:
                if (maplepoints + quantity < 0) {
                    if (show) {
                        dropMessage(5, "你的抵用卷已经满了.");
                    }
                    return;
                }
                maplepoints += quantity;
                break;
            default:
                break;
        }
        if (show && quantity != 0) {
            dropMessage(5, "你已经 " + (quantity > 0 ? "获得 " : "使用 ") + quantity + (type == 1 ? " 点卷." : " 抵用卷."));
            //client.getSession().write(MaplePacketCreator.showSpecialEffect(19));
        }
    }

    public long getCSPoints(int type) {
        switch (type) {
            case 1:
                return acash;
            case 2:
                return maplepoints;
            default:
                return 0;
        }
    }

    public final boolean hasEquipped(int itemid) {
        return inventory[MapleInventoryType.EQUIPPED.ordinal()].countById(itemid) >= 1;
    }

    public final boolean haveItem(int itemid, int quantity, boolean checkEquipped, boolean greaterOrEquals) {
        final MapleInventoryType type = GameConstants.getInventoryType(itemid);
        int possesed = inventory[type.ordinal()].countById(itemid);
        if (checkEquipped && type == MapleInventoryType.EQUIP) {
            possesed += inventory[MapleInventoryType.EQUIPPED.ordinal()].countById(itemid);
        }
        if (greaterOrEquals) {
            return possesed >= quantity;
        } else {
            return possesed == quantity;
        }
    }

    public final boolean haveItem(int itemid, int quantity) {
        return haveItem(itemid, quantity, true, true);
    }

    public final boolean haveItem(int itemid) {
        return haveItem(itemid, 1, true, true);
    }

    public void maxAllSkills() {
        WzData.STRING.directory().findFile("Skill.img")
                .map(WzFile::content)
                .map(ImgdirElement::childrenStream)
                .ifPresent(stream -> stream.map(WzElement::name)
                        .map(Numbers::ofInt)
                        .map(SkillFactory::getSkill1)
                        .filter(skill -> level > 0)
                        .forEach(skill -> changeSkillLevel(skill, skill.getMaxLevel(), skill.getMaxLevel())));
    }

    public void setAPQScore(int score) {
        this.APQScore = score;
    }

    public int getAPQScore() {
        return APQScore;
    }

    public long getLasttime() {
        return this.lasttime;
    }

    public void setLasttime(long lasttime) {
        this.lasttime = lasttime;
    }

    public long getCurrenttime() {
        return this.currenttime;
    }

    public void setCurrenttime(long currenttime) {
        this.currenttime = currenttime;
    }

    public long get防止复制时间() {
        return this.防止复制时间;
    }

    public void set防止复制时间(long 防止复制时间) {
        this.防止复制时间 = 防止复制时间;
    }

    public void forceUpdateItem(MapleInventoryType type, IItem item) {
        client.getSession().write(MaplePacketCreator.clearInventoryItem(type, item.getPosition(), false));
        client.getSession().write(MaplePacketCreator.addInventorySlot(type, item, false));
    }

    public void forceUpdateItem(IItem item) {
        forceUpdateItem(item, false);
    }

    public void forceUpdateItem(IItem item, boolean updateTick) {
        List<ModifyInventory> mods = new LinkedList<ModifyInventory>();
        mods.add(new ModifyInventory(3, item)); //删除道具
        mods.add(new ModifyInventory(0, item)); //获得道具
        client.getSession().write(MaplePacketCreator.modifyInventory(false, new ModifyInventory(ModifyInventory.Types.UPDATE, item)));
        // client.getSession().write(MaplePacketCreator.modifyInventory(updateTick, mods, this));
    }

    public int getPetSlot(MaplePet pet) {
        if (this.pets.size() > 0) {
            for (int i = 0; i < this.pets.size(); i++) {
                if ((this.pets.get(i) != null) && (((MaplePet) this.pets.get(i)).getUniqueId() == pet.getUniqueId())) {
                    return i;
                }
            }
        }

        return -1;
    }

    public void dropTopMsg(String message) {
        this.client.getSession().write(UIPacket.getTopMsg(message));
    }

    public int getTouzhuNX() {
        return this.touzhuNX;
    }

    public void setTouzhuNX(int touzhuNX) {
        this.touzhuNX = touzhuNX;
    }

    public int getTouzhuNum() {
        return this.touzhuNum;
    }

    public void setTouzhuNum(int touzhuNum) {
        this.touzhuNum = touzhuNum;
    }

    public int getTouzhuType() {
        return this.touzhuType;
    }

    public void setTouzhuType(int touzhuType) {
        this.touzhuType = touzhuType;
    }

    public Rectangle getBounds() {
        return new Rectangle(getTruePosition().x - 25, getTruePosition().y - 75, 50, 75);
    }

    public int getGamePointsPS() {//跑商
        DAccountInfo one = new QDAccountInfo().account.id.eq(getClient().getAccID())
                .worldId.eq(getWorld())
                .findOneOrEmpty()
                .orElse(new DAccountInfo());
        int gamePointsRQ = one.getGamePoints() != null ? one.getGamePoints() : 0;
        LocalDateTime updateTime = one.getUpdated() != null ? one.getUpdated() : LocalDateTime.now();

        if (updateTime.isBefore(LocalDateTime.now())) {
            gamePointsRQ = 0;
            one.setGamePoints(0);
            one.setUpdated(LocalDateTime.now());
        } else {
            one.setGamePoints(0);
        }
        one.setAccount(new QDAccount().id.eq(getClient().getAccID()).findOne());
        one.setWorldId(getWorld());
        one.save();
        return gamePointsRQ;
    }

    public void gainGamePointsPS(int amount) {//跑商
        int gamePointsPS = getGamePointsPS() + amount;
        updateGamePointsPS(gamePointsPS);
    }

    public void resetGamePointsPS() {//跑商
        updateGamePointsPS(0);
    }

    public void updateGamePointsPS(int amount) {//跑商
        DAccountInfo one = new QDAccountInfo().account.id.eq(getClient().getAccID())
                .worldId.eq(getWorld())
                .findOneOrEmpty()
                .orElse(new DAccountInfo());
        one.setGamePoints(0);
        one.setUpdated(LocalDateTime.now());
        one.save();
    }

    public static enum FameStatus {

        OK, NOT_TODAY, NOT_THIS_MONTH
    }

    public int getBuddyCapacity() {
        return buddylist.getCapacity();
    }

    public void setBuddyCapacity(byte capacity) {
        buddylist.setCapacity(capacity);
        client.getSession().write(MaplePacketCreator.updateBuddyCapacity(capacity));
    }

    public MapleMessenger getMessenger() {
        return messenger;
    }

    public void setMessenger(MapleMessenger messenger) {
        this.messenger = messenger;
    }

    public void addCooldown(int skillId, long startTime, long length) {
        coolDowns.put(Integer.valueOf(skillId), new MapleCoolDownValueHolder(skillId, startTime, length));
    }

    public void removeCooldown(int skillId) {
        if (coolDowns.containsKey(Integer.valueOf(skillId))) {
            coolDowns.remove(Integer.valueOf(skillId));
        }
    }

    public boolean skillisCooling(int skillId) {
        return coolDowns.containsKey(Integer.valueOf(skillId));
    }

    public void giveCoolDowns(final int skillid, long starttime, long length) {
        addCooldown(skillid, starttime, length);
    }

    public void giveCoolDowns(final List<MapleCoolDownValueHolder> cooldowns) {
        if (cooldowns != null) {
            for (MapleCoolDownValueHolder cooldown : cooldowns) {
                coolDowns.put(cooldown.skillId, cooldown);
            }
        } else {
            new QDSkillCooldown().character.eq(character).findEach(it -> {
                if (it.getLength() + it.getStartTime() - System.currentTimeMillis() > 0) {
                    giveCoolDowns(it.getSkillId(), it.getStartTime(), it.getLength());
                }
            });
            new QDSkillCooldown().character.eq(character).delete();
        }
    }

    public List<MapleCoolDownValueHolder> getCooldowns() {
        return new ArrayList<MapleCoolDownValueHolder>(coolDowns.values());
    }

    public final List<MapleDiseaseValueHolder> getAllDiseases() {
        return new ArrayList<MapleDiseaseValueHolder>(diseases.values());
    }

    public final boolean hasDisease(final MapleDisease dis) {
        return diseases.keySet().contains(dis);
    }/*
    //怪物给玩家BUFF
    public void giveDebuff(MapleDisease disease, MobSkill skill) {
        giveDebuff(disease, skill, false);
    }
    //怪物给玩家BUFF
    public void giveDebuff(MapleDisease disease, MobSkill skill, boolean cpq) {
        MapleClient c;
        synchronized (diseases) {

            int duration = 4000;
            if (isAlive() && !isActiveBuffedValue(2321005) && !diseases2.contains(disease) && diseases2.size() < 2) {
                diseases2.add(disease);
                List<Pair<MapleDisease, Integer>> debuff = Collections.singletonList(new Pair<MapleDisease, Integer>(disease, Integer.valueOf(skill.getX())));
                long mask = 0;
                for (Pair<MapleDisease, Integer> statup : debuff) {
                    mask |= statup.getLeft().getValue();
                }
                getClient().getSession().write(MaplePacketCreator.giveDebuff(mask, debuff, skill,duration));//怪物给玩家BUFF
                getMap().broadcastMessage(this, MaplePacketCreator.giveForeignDebuff(id, mask, skill), false);//其他人看见的负面状态效果

                if (isAlive() && diseases2.contains(disease)) {
                    final MapleCharacter character = this;
                    final MapleDisease disease_ = disease;
                    Timer.WORLD.schedule(new Runnable() {
                        //..getInstance().schedule(new Runnable() {
                        @Override
                        public void run() {
                            if (character.diseases2.contains(disease_)) {
                                dispelDebuff(disease_);
                            }
                        }
                    }, skill.getDuration());
                }
            }
        }
    }
     */

    //怪物给玩家上BUFF
    public void giveDebuff(final MapleDisease disease, MobSkill skill) {
        giveDebuff(disease, skill.getX(), skill.getDuration(), skill.getSkillId(), skill.getSkillLevel());
    }

    //怪物给玩家上BUFF
    public void giveDebuff(final MapleDisease disease, int x, long duration, int skillid, int level) {
        final List<Pair<MapleDisease, Integer>> debuff = Collections.singletonList(new Pair<MapleDisease, Integer>(disease, Integer.valueOf(x)));

        if (!hasDisease(disease) && diseases.size() < 2) {
            if (!(disease == MapleDisease.SEDUCE || disease == MapleDisease.STUN)) {
                if (isActiveBuffedValue(2321005)) {
                    return;
                }
            }

            diseases.put(disease, new MapleDiseaseValueHolder(disease, System.currentTimeMillis(), duration));
            client.getSession().write(MaplePacketCreator.giveDebuff(debuff, skillid, level, (int) duration));
            map.broadcastMessage(this, MaplePacketCreator.giveForeignDebuff(id, debuff, skillid, level), false);
        }
    }

    public final void giveSilentDebuff(final List<MapleDiseaseValueHolder> ld) {
        if (ld != null) {
            for (final MapleDiseaseValueHolder disease : ld) {
                diseases.put(disease.disease, disease);
            }
        }
    }

    public void dispelDebuff(MapleDisease debuff) {
        if (hasDisease(debuff)) {
            long mask = debuff.getValue();
            boolean first = debuff.isFirst();
            client.getSession().write(MaplePacketCreator.cancelDebuff(mask, first));
            map.broadcastMessage(this, MaplePacketCreator.cancelForeignDebuff(id, mask, first), false);

            diseases.remove(debuff);
        }
    }

    public void dispelDebuffs() {
        dispelDebuff(MapleDisease.CURSE);
        dispelDebuff(MapleDisease.DARKNESS);
        dispelDebuff(MapleDisease.POISON);
        dispelDebuff(MapleDisease.SEAL);
        dispelDebuff(MapleDisease.WEAKEN);
    }

    public void cancelAllDebuffs() {
        diseases.clear();
    }

    public void setLevel(final short level) {
        this.level = (short) (level - 1);
    }

    public void sendNote(String to, String msg) {
        sendNote(to, msg, 0);
    }

    public void sendNote(String to, String msg, int fame) {
        MapleCharacterUtil.sendNote(to, getName(), msg, fame);
    }

    public void showNote() {
        List<DNote> notes = new QDNote().to.eq(getName()).findList();
        client.getSession().write(MTSCSPacket.showNotes(notes, notes.size()));
    }

    public void deleteNote(int id, int fame) {
        DNote one = new QDNote().id.eq(id).findOne();
        if (one != null) {
            if (one.getGift() == fame && fame > 0) {
                addFame(fame);
                updateSingleStat(MapleStat.FAME, getFame());
                client.getSession().write(MaplePacketCreator.getShowFameGain(fame));
            }
            one.delete();
        }
    }

    public void mulung_EnergyModify(boolean inc) {
        if (inc) {
            if (mulung_energy + 100 > 10000) {
                mulung_energy = 10000;
            } else {
                mulung_energy += 100;
            }
        } else {
            mulung_energy = 0;
        }
        //    client.getSession().write(MaplePacketCreator.MulungEnergy(mulung_energy));
    }

    public void writeMulungEnergy() {
        //  client.getSession().write(MaplePacketCreator.MulungEnergy(mulung_energy));
    }

    public void writeEnergy(String type, String inc) {
        // client.getSession().write(MaplePacketCreator.sendPyramidEnergy(type, inc));
    }

    public void writeStatus(String type, String inc) {
        //  client.getSession().write(MaplePacketCreator.sendGhostStatus(type, inc));
    }

    public void writePoint(String type, String inc) {
        //  client.getSession().write(MaplePacketCreator.sendGhostPoint(type, inc));
    }

    public final short getCombo() {
        return (short) combo;
    }

    public void setCombo(final short combo) {
        this.combo = combo;
    }

    public final long getLastCombo() {
        return lastCombo;
    }

    public void setLastCombo(final long combo) {
        this.lastCombo = combo;
    }

    public final long getKeyDownSkill_Time() {
        return keydown_skill;
    }

    public void setKeyDownSkill_Time(final long keydown_skill) {
        this.keydown_skill = keydown_skill;
    }

    public void checkBerserk() {
        if (BerserkSchedule != null) {
            BerserkSchedule.cancel(false);
            BerserkSchedule = null;
        }

        final ISkill BerserkX = SkillFactory.getSkill(1320006);
        final int skilllevel = getSkillLevel(BerserkX);
        if (skilllevel >= 1) {
            final MapleStatEffect ampStat = BerserkX.getEffect(skilllevel);
            stats.Berserk = stats.getHp() * 100 / stats.getMaxHp() <= ampStat.getX();
            client.getSession().write(MaplePacketCreator.showOwnBuffEffect(1320006, 1, (byte) (stats.Berserk ? 1 : 0)));
            map.broadcastMessage(this, MaplePacketCreator.showBuffeffect(getId(), 1320006, 1, (byte) (stats.Berserk ? 1 : 0)), false);

            BerserkSchedule = Timer.BUFF.schedule(new Runnable() {

                @Override
                public void run() {
                    checkBerserk();
                }
            }, 10000);
        }
    }

    private void prepareBeholderEffect() {
        if (beholderHealingSchedule != null) {
            beholderHealingSchedule.cancel(false);
        }
        if (beholderBuffSchedule != null) {
            beholderBuffSchedule.cancel(false);
        }
        ISkill bHealing = SkillFactory.getSkill(1320008);
        final int bHealingLvl = getSkillLevel(bHealing);
        final int berserkLvl = getSkillLevel(SkillFactory.getSkill(1320006));

        if (bHealingLvl > 0) {
            final MapleStatEffect healEffect = bHealing.getEffect(bHealingLvl);
            int healInterval = healEffect.getX() * 1000;
            beholderHealingSchedule = Timer.BUFF.register(new Runnable() {

                @Override
                public void run() {
                    int remhppercentage = (int) Math.ceil((getStat().getHp() * 100.0) / getStat().getMaxHp());
                    if (berserkLvl == 0 || remhppercentage >= berserkLvl + 10) {
                        addHP(healEffect.getHp());
                    }
                    client.getSession().write(MaplePacketCreator.showOwnBuffEffect(1321007, 2));
                    map.broadcastMessage(MaplePacketCreator.summonSkill(getId(), 1321007, 5));
                    map.broadcastMessage(MapleCharacter.this, MaplePacketCreator.showBuffeffect(getId(), 1321007, 2), false);
                }
            }, healInterval, healInterval);
        }
        ISkill bBuff = SkillFactory.getSkill(1320009);
        final int bBuffLvl = getSkillLevel(bBuff);
        if (bBuffLvl > 0) {
            final MapleStatEffect buffEffect = bBuff.getEffect(bBuffLvl);
            int buffInterval = buffEffect.getX() * 1000;
            beholderBuffSchedule = Timer.BUFF.register(new Runnable() {

                @Override
                public void run() {
                    buffEffect.applyTo(MapleCharacter.this);
                    client.getSession().write(MaplePacketCreator.showOwnBuffEffect(1321007, 2));
                    map.broadcastMessage(MaplePacketCreator.summonSkill(getId(), 1321007, Randomizer.nextInt(3) + 6));
                    map.broadcastMessage(MapleCharacter.this, MaplePacketCreator.showBuffeffect(getId(), 1321007, 2), false);
                }
            }, buffInterval, buffInterval);
        }
    }

    public void setChalkboard(String text) {
        this.chalktext = text;
        map.broadcastMessage(MTSCSPacket.useChalkboard(getId(), text));
    }

    public String getChalkboard() {
        return chalktext;
    }

    public MapleMount getMount() {
        return mount;
    }

    public int[] getWishlist() {
        return wishlist;
    }

    public void clearWishlist() {
        for (int i = 0; i < 10; i++) {
            wishlist[i] = 0;
        }
    }

    public int getWishlistSize() {
        int ret = 0;
        for (int i = 0; i < 10; i++) {
            if (wishlist[i] > 0) {
                ret++;
            }
        }
        return ret;
    }

    public void setWishlist(int[] wl) {
        this.wishlist = wl;
    }

    public int[] getRocks() {
        return rocks;
    }

    public int getRockSize() {
        int ret = 0;
        for (int i = 0; i < 10; i++) {
            if (rocks[i] != 999999999) {
                ret++;
            }
        }
        return ret;
    }

    public void deleteFromRocks(int map) {
        for (int i = 0; i < 10; i++) {
            if (rocks[i] == map) {
                rocks[i] = 999999999;
                break;
            }
        }
    }

    public void addRockMap() {
        if (getRockSize() >= 10) {
            return;
        }
        rocks[getRockSize()] = getMapId();
    }

    public boolean isRockMap(int id) {
        for (int i = 0; i < 10; i++) {
            if (rocks[i] == id) {
                return true;
            }
        }
        return false;
    }

    public int[] getRegRocks() {
        return regrocks;
    }

    public int getRegRockSize() {
        int ret = 0;
        for (int i = 0; i < 5; i++) {
            if (regrocks[i] != 999999999) {
                ret++;
            }
        }
        return ret;
    }

    public void deleteFromRegRocks(int map) {
        for (int i = 0; i < 5; i++) {
            if (regrocks[i] == map) {
                regrocks[i] = 999999999;
                break;
            }
        }
    }

    public void addRegRockMap() {
        if (getRegRockSize() >= 5) {
            return;
        }
        regrocks[getRegRockSize()] = getMapId();
    }

    public boolean isRegRockMap(int id) {
        for (int i = 0; i < 5; i++) {
            if (regrocks[i] == id) {
                return true;
            }
        }
        return false;
    }

    public List<LifeMovementFragment> getLastRes() {
        return lastres;
    }

    public void setLastRes(List<LifeMovementFragment> lastres) {
        this.lastres = lastres;
    }

    public void setMonsterBookCover(int bookCover) {
        this.bookCover = bookCover;
    }

    public int getMonsterBookCover() {
        return bookCover;
    }

    public int getOneTimeLog(String bossid) {
        return new QDOneTimeLog().character.eq(character).log.eq(bossid).findCount();
    }

    public void setOneTimeLog(String bossid) {
        DOneTimeLog log = new DOneTimeLog();
        log.setCharacter(character);
        log.setLog(bossid);
        log.save();
    }

    public int getBossLog(String bossid) {
        return new QDBossLog()
                .character.eq(character)
                .bossId.eq(bossid)
                // lastattempt >= subtime(current_timestamp, '1 0:0:0.0')
                .lastAttempt.ge(LocalDateTime.now().minus(Duration.ofDays(1)))
                .findCount();
    }

    public void setBossLog(String bossid) {
        DBossLog log = new DBossLog();
        log.setCharacter(character);
        log.setBossId(bossid);
        log.save();
    }

    public void setPrizeLog(String bossid) {
        DPrizeLog log = new DPrizeLog();
        log.setAccid(getClient().getAccID());
        log.setBossId(bossid);
        log.save();
    }

    public int getPrizeLog(String bossid) {
        return new QDPrizeLog().accid.eq(getClient().getAccID()).bossId.eq(bossid).findCount();
    }

    public void dropMessage(int type, String message) {
        /*
         * if (type == -1) {
         * client.getSession().write(UIPacket.getTopMsg(message)); } else
         */
        if (type == -2) {
            client.getSession().write(PlayerShopPacket.shopChat(message, 0)); //0 or what
        } else {
            client.getSession().write(MaplePacketCreator.serverNotice(type, message));
        }
    }

    public IMaplePlayerShop getPlayerShop() {
        return playerShop;
    }

    public void setPlayerShop(IMaplePlayerShop playerShop) {
        this.playerShop = playerShop;
    }

    public int getConversation() {
        return inst.get();
    }

    public void setConversation(int inst) {
        this.inst.set(inst);
    }

    public MapleCarnivalParty getCarnivalParty() {
        return carnivalParty;
    }

    public void setCarnivalParty(MapleCarnivalParty party) {
        carnivalParty = party;
    }

    public void addCP(int ammount) {
        totalCP += ammount;
        availableCP += ammount;
    }

    public void useCP(int ammount) {
        availableCP -= ammount;
    }

    public int getAvailableCP() {
        return availableCP;
    }

    public int getTotalCP() {
        return totalCP;
    }

    public void resetCP() {
        totalCP = 0;
        availableCP = 0;
    }

    public void addCarnivalRequest(MapleCarnivalChallenge request) {
        pendingCarnivalRequests.add(request);
    }

    public final MapleCarnivalChallenge getNextCarnivalRequest() {
        return pendingCarnivalRequests.pollLast();
    }

    public void clearCarnivalRequests() {
        pendingCarnivalRequests = new LinkedList<MapleCarnivalChallenge>();
    }

    public void startMonsterCarnival(final int enemyavailable, final int enemytotal) {
        client.getSession().write(MonsterCarnivalPacket.startMonsterCarnival(this, enemyavailable, enemytotal));
    }

    public void CPUpdate(final boolean party, final int available, final int total, final int team) {
        client.getSession().write(MonsterCarnivalPacket.CPUpdate(party, available, total, team));
    }

    public void playerDiedCPQ(final String name, final int lostCP, final int team) {
        client.getSession().write(MonsterCarnivalPacket.playerDiedMessage(name, lostCP, team));
    }

    /*
     * public void setAchievementFinished(int id) { if
     * (!finishedAchievements.contains(id)) { finishedAchievements.add(id); } }
     *
     * public boolean achievementFinished(int achievementid) { return
     * finishedAchievements.contains(achievementid); }
     *
     * public void finishAchievement(int id) { if (!achievementFinished(id)) {
     * if (isAlive() && !isClone()) {
     * MapleAchievements.getInstance().getById(id).finishAchievement(this); } }
     * }
     *
     * public List<Integer> getFinishedAchievements() { return
     * finishedAchievements; }
     *
     * public void modifyAchievementCSPoints(int type, int quantity) { switch
     * (type) { case 1: acash += quantity; break; case 2: maplepoints +=
     * quantity; break; } }
     */
    public boolean getCanTalk() {
        return this.canTalk;
    }

    public void canTalk(boolean talk) {
        this.canTalk = talk;
    }

    public int getHp() {
        return stats.hp;
    }

    public void setHp(int hp) {
        stats.setHp(hp);
    }

    public int getMp() {
        return stats.mp;
    }

    public void setMp(int mp) {
        stats.setMp(mp);
    }

    public int getStr() {
        return stats.str;
    }

    public int getDex() {
        return stats.dex;
    }

    public int getLuk() {
        return stats.luk;
    }

    public int getInt() {
        return stats.int_;
    }

    public int getEXPMod() {
        return stats.expMod;
    }

    public int getDropMod() {
        return stats.dropMod;
    }

    public int getCashMod() {
        return stats.cashMod;
    }

    public void setPoints(long p) {
        this.points = p;
        /*
         * if (this.points >= 1) { finishAchievement(1); }
         */
    }

    public long getPoints() {
        return points;
    }

    public void setVPoints(int p) {
        this.vpoints = p;
    }

    public long getVPoints() {
        return vpoints;
    }

    public CashShop getCashInventory() {
        return cs;
    }

    public void removeAll(int id) {
        removeAll(id, true, false);
    }

    public void removeAll(int id, boolean show, boolean checkEquipped) {
        MapleInventoryType type = GameConstants.getInventoryType(id);
        int possessed = getInventory(type).countById(id);

        if (possessed > 0) {
            MapleInventoryManipulator.removeById(getClient(), type, id, possessed, true, false);
            if (show) {
                getClient().getSession().write(MaplePacketCreator.getShowItemGain(id, (short) -possessed, true));
            }
        }
        if ((checkEquipped) && (type == MapleInventoryType.EQUIP)) {
            type = MapleInventoryType.EQUIPPED;
            possessed = getInventory(type).countById(id);
            if (possessed > 0) {
                MapleInventoryManipulator.removeById(getClient(), type, id, possessed, true, false);
                if (show) {
                    getClient().getSession().write(MaplePacketCreator.getShowItemGain(id, (short) (-possessed), true));
                }
                equipChanged();
            }
        }
        /*
         * if (type == MapleInventoryType.EQUIP) { //check equipped type =
         * MapleInventoryType.EQUIPPED; possessed =
         * getInventory(type).countById(id);
         *
         * if (possessed > 0) {
         * MapleInventoryManipulator.removeById(getClient(), type, id,
         * possessed, true, false);
         * getClient().getSession().write(MaplePacketCreator.getShowItemGain(id,
         * (short)-possessed, true)); } }
         */
    }

    //TODO: more than one crush/friendship ring at a time
    public Pair<List<MapleRing>, List<MapleRing>> getRings(boolean equip) {
        MapleInventory iv = getInventory(MapleInventoryType.EQUIPPED);
        Collection<IItem> equippedC = iv.list();
        List<Item> equipped = new ArrayList<>(equippedC.size());
        for (IItem item : equippedC) {
            equipped.add((Item) item);
        }
        Collections.sort(equipped);
        List<MapleRing> crings = new ArrayList<>();
        List<MapleRing> frings = new ArrayList<>();
        MapleRing ring;
        for (Item item : equipped) {
            if (item.getRing() != null) {
                ring = item.getRing();
                ring.setEquipped(true);
                if (GameConstants.isFriendshipRing(item.getItemId()) || GameConstants.isCrushRing(item.getItemId())) {
                    if (equip) {
                        if (GameConstants.isCrushRing(item.getItemId())) {
                            crings.add(ring);
                        } else if (GameConstants.isFriendshipRing(item.getItemId())) {
                            frings.add(ring);
                        }
                    } else if (crings.isEmpty() && GameConstants.isCrushRing(item.getItemId())) {
                        crings.add(ring);
                    } else if (frings.isEmpty() && GameConstants.isFriendshipRing(item.getItemId())) {
                        frings.add(ring);
                    } //for 3rd person the actual slot doesnt matter, so we'll use this to have both shirt/ring same?
                    //however there seems to be something else behind this, will have to sniff someone with shirt and ring, or more conveniently 3-4 of those
                }
            }
        }
        if (equip) {
            iv = getInventory(MapleInventoryType.EQUIP);
            for (IItem item : iv.list()) {
                if (item.getRing() != null && GameConstants.isEffectRing(item.getItemId())) {
                    ring = item.getRing();
                    ring.setEquipped(false);
                    if (GameConstants.isFriendshipRing(item.getItemId())) {
                        frings.add(ring);
                    } else if (GameConstants.isCrushRing(item.getItemId())) {
                        crings.add(ring);
                    }
                }
            }
        }
        Collections.sort(frings, new MapleRing.RingComparator());
        Collections.sort(crings, new MapleRing.RingComparator());
        return new Pair<>(crings, frings);
    }

    public int getFH() {
        MapleFoothold fh = getMap().getFootholds().findBelow(getPosition());
        if (fh != null) {
            return fh.getId();
        }
        return 0;
    }

    public void startFairySchedule(boolean exp) {
        startFairySchedule(exp, false);
    }

    public void startFairySchedule(boolean exp, boolean equipped) {
        cancelFairySchedule(exp);
        if (fairyExp < 30 && stats.equippedFairy) {
            if (equipped) {
                dropMessage(5, "The Fairy Pendant's experience points will increase to " + (fairyExp) + "% after one hour.");
            }
            fairySchedule = Timer.ETC.schedule(new Runnable() {

                public void run() {
                    if (fairyExp < 30 && stats.equippedFairy) {
                        fairyExp = 30;
                        dropMessage(5, "The Fairy Pendant's EXP was boosted to " + fairyExp + "%.");
                        startFairySchedule(false, true);
                    } else {
                        cancelFairySchedule(!stats.equippedFairy);
                    }
                }
            }, 60 * 60 * 1000);
        } else {
            cancelFairySchedule(!stats.equippedFairy);
        }
    }

    public void cancelFairySchedule(boolean exp) {
        if (fairySchedule != null) {
            fairySchedule.cancel(false);
            fairySchedule = null;
        }
        if (exp) {
            this.fairyExp = 30;
        }
    }

    public byte getFairyExp() {
        return (byte) fairyExp;
    }

    public int getCoconutTeam() {
        return coconutteam;
    }

    public void setCoconutTeam(int team) {
        coconutteam = team;
    }

    public void spawnPet(byte slot) {
        spawnPet(slot, false, true);
    }

    public void spawnPet(byte slot, boolean lead) {
        spawnPet(slot, lead, true);
    }

    public void spawnPet(byte slot, boolean lead, boolean broadcast) {
        final IItem item = getInventory(MapleInventoryType.CASH).getItem(slot);
        if (item == null || item.getItemId() > 5001000 || item.getItemId() < 5000000) {
            return;
        }
        switch (item.getItemId()) {
            case 5000047:
            case 5000028: {
                final MaplePet pet = MaplePet.createPet(item.getItemId() + 1, MapleInventoryIdentifier.getInstance());
                if (pet != null) {
                    MapleInventoryManipulator.addById(client, item.getItemId() + 1, (short) 1, item.getOwner(), pet, 45, (byte) 0);
                    MapleInventoryManipulator.removeFromSlot(client, MapleInventoryType.CASH, slot, (short) 1, false);
                }
                break;
            }
            default: {
                final MaplePet pet = item.getPet();
                if (pet != null && (item.getItemId() != 5000054 || pet.getSecondsLeft() > 0) && (item.getExpiration() == -1 || item.getExpiration() > System.currentTimeMillis())) {
                    if (pet.getSummoned()) { // Already summoned, let's keep it
                        unequipPet(pet, true, false);
                    } else {
                        int leadid = 8;
                        if (GameConstants.isKOC(getJob())) {
                            leadid = 10000018;
                        } else if (GameConstants.isAran(getJob())) {
                            leadid = 20000024;
                        } else if (GameConstants.isEvan(getJob())) {
                            leadid = 20010024;
                        } else if (GameConstants.isResist(getJob())) {
                            leadid = 30000024;
                        }
                        if (getSkillLevel(SkillFactory.getSkill(leadid)) == 0 && getPet(0) != null) {
                            unequipPet(getPet(0), false, false);
                        } else if (lead || getSkillLevel(SkillFactory.getSkill(leadid)) <= 0) { // Follow the Lead
                            // shiftPetsRight();
                        }
                        final Vector pos = getPosition();
                        pet.setPos(pos);
                        try {
                            pet.setFh(getMap().getFootholds().findBelow(pos).getId());
                        } catch (NullPointerException e) {
                            pet.setFh(0); //lol, it can be fixed by movement
                        }
                        pet.setStance(0);
                        pet.setSummoned(slot);

                        addPet(pet);
                        if (broadcast) {
                            getMap().broadcastMessage(this, PetPacket.showPet(this, pet, false, false), true);
                            client.getSession().write(PetPacket.updatePet(pet, getInventory(MapleInventoryType.CASH)
                                    .getItem(pet.getInventoryPosition()), true));
                            client.getSession().write(PetPacket.petStatUpdate(this));
                        }
                    }
                }
                break;
            }
        }
        client.getSession().write(PetPacket.emptyStatUpdate());
    }

    public void addMoveMob(int mobid) {
        if (movedMobs.containsKey(mobid)) {
            movedMobs.put(mobid, movedMobs.get(mobid) + 1);
            if (movedMobs.get(mobid) > 30) { //trying to move not null monster = broadcast dead
                for (MapleCharacter chr : getMap().getCharactersThreadsafe()) { //also broadcast to others
                    if (chr.getMoveMobs().containsKey(mobid)) { //they also tried to move this mob
                        chr.getClient().getSession().write(MobPacket.killMonster(mobid, 1));
                        chr.getMoveMobs().remove(mobid);
                    }
                }
            }
        } else {
            movedMobs.put(mobid, 1);
        }
    }

    public Map<Integer, Integer> getMoveMobs() {
        return movedMobs;
    }

    public boolean isClone() {
        return clone;
    }

    public void setClone(boolean c) {
        this.clone = c;
    }

    public WeakReference<MapleCharacter>[] getClones() {
        return clones;
    }

    public MapleCharacter cloneLooks() {
        MapleClient cs = new MapleClient(null, null, new MockIOSession(), Injectors.get(ServerProperties.class));

        int minus = (getId() + Randomizer.nextInt(getId())); // really randomize it, dont want it to fail

        MapleCharacter ret = new MapleCharacter(true);
        ret.id = minus;
        ret.client = cs;
        ret.exp = 0;
        ret.meso = 0;
        ret.beans = beans;
        ret.blood = blood;
        ret.month = month;
        ret.day = day;
        ret.charmessage = charmessage;
        ret.expression = expression;
        ret.constellation = constellation;
        ret.djjl = djjl;
        ret.qiandao = qiandao;
        ret.remainingAp = 0;
        ret.fame = 0;
        ret.accountid = client.getAccID();
        ret.name = name;
        ret.level = level;
        ret.fame = fame;
        ret.job = job;
        ret.hair = hair;
        ret.face = face;
        ret.skinColor = skinColor;
        ret.bookCover = bookCover;
        ret.monsterbook = monsterbook;
        ret.mount = mount;
        ret.CRand = new PlayerRandomStream();
        ret.gmLevel = gmLevel;
        ret.gender = gender;
        ret.mapid = map.getId();
        ret.map = map;
        ret.setStance(getStance());
        ret.chair = chair;
        ret.itemEffect = itemEffect;
        ret.guildid = guildid;
        ret.currentrep = currentrep;
        ret.totalrep = totalrep;
        ret.stats = stats;
        ret.effects.putAll(effects);
        if (ret.effects.get(MapleBuffStat.ILLUSION) != null) {
            ret.effects.remove(MapleBuffStat.ILLUSION);
        }
        if (ret.effects.get(MapleBuffStat.SUMMON) != null) {
            ret.effects.remove(MapleBuffStat.SUMMON);
        }
        if (ret.effects.get(MapleBuffStat.REAPER) != null) {
            ret.effects.remove(MapleBuffStat.REAPER);
        }
        if (ret.effects.get(MapleBuffStat.PUPPET) != null) {
            ret.effects.remove(MapleBuffStat.PUPPET);
        }
        ret.guildrank = guildrank;
        ret.allianceRank = allianceRank;
        ret.hidden = hidden;
        ret.setPosition(Vector.of(getPosition()));
        for (IItem equip : getInventory(MapleInventoryType.EQUIPPED)) {
            ret.getInventory(MapleInventoryType.EQUIPPED).addFromDB(equip);
        }
        ret.skillMacros = skillMacros;
        ret.keylayout = keylayout;
        ret.questinfo = questinfo;
        ret.savedLocations = savedLocations;
        ret.wishlist = wishlist;
        ret.rocks = rocks;
        ret.regrocks = regrocks;
        ret.buddylist = buddylist;
        ret.keydown_skill = 0;
        ret.lastmonthfameids = lastmonthfameids;
        ret.lastfametime = lastfametime;
        ret.storage = storage;
        ret.cs = this.cs;
//        ret.client.setAccountName(client.getAccountName());
        ret.acash = acash;
        ret.lastGainHM = lastGainHM;
        ret.maplepoints = maplepoints;
        ret.clone = true;
        ret.client.setChannel(this.client.getChannel());
        LOGGER.debug("cloneLooks输出：" + this.client.getChannel());
        while (map.getCharacterById(ret.id) != null || client.getChannelServer().getPlayerStorage().getCharacterById(ret.id) != null) {
            ret.id++;
        }
        ret.client.setPlayer(ret);
        return ret;
    }

    public final void cloneLook() {
        if (clone) {
            return;
        }
        for (int i = 0; i < clones.length; i++) {
            if (clones[i].get() == null) {
                MapleCharacter newp = cloneLooks();
                map.addPlayer(newp);
                map.broadcastMessage(MaplePacketCreator.updateCharLook(newp));
                map.movePlayer(newp, getPosition());
                clones[i] = new WeakReference<>(newp);
                return;
            }
        }
    }

    public final void disposeClones() {
        numClones = 0;
        for (int i = 0; i < clones.length; i++) {
            if (clones[i].get() != null) {
                map.removePlayer(clones[i].get());
                clones[i].get().getClient().disconnect(false, false);
                clones[i] = new WeakReference<MapleCharacter>(null);
                numClones++;
            }
        }
    }

    public final int getCloneSize() {
        int z = 0;
        for (WeakReference<MapleCharacter> mapleCharacterWeakReference : clones) {
            if (mapleCharacterWeakReference.get() != null) {
                z++;
            }
        }
        return z;
    }

    public void spawnClones() {
        if (numClones == 0 && stats.hasClone) {
            cloneLook(); //once and never again
        }
        for (int i = 0; i < numClones; i++) {
            cloneLook();
        }
        numClones = 0;
    }

    public byte getNumClones() {
        return (byte) numClones;
    }

    public void setDragon(MapleDragon d) {
        this.dragon = d;
    }

    public final void spawnSavedPets() {
        for (int i = 0; i < petStore.length; i++) {
            if (petStore[i] > -1) {
                spawnPet(petStore[i], false, false);
            }
        }
        client.getSession().write(PetPacket.petStatUpdate(this));
        petStore = new byte[]{-1, -1, -1};
    }

    public final byte[] getPetStores() {
        return petStore;
    }

    public void resetStats(final int str, final int dex, final int int_, final int luk) {
        List<Pair<MapleStat, Integer>> stat = new ArrayList<Pair<MapleStat, Integer>>(2);
        int total = stats.getStr() + stats.getDex() + stats.getLuk() + stats.getInt() + getRemainingAp();

        total -= str;
        stats.setStr((short) str);

        total -= dex;
        stats.setDex((short) dex);

        total -= int_;
        stats.setInt((short) int_);

        total -= luk;
        stats.setLuk((short) luk);

        setRemainingAp((short) total);

        stat.add(new Pair<MapleStat, Integer>(MapleStat.STR, str));
        stat.add(new Pair<MapleStat, Integer>(MapleStat.DEX, dex));
        stat.add(new Pair<MapleStat, Integer>(MapleStat.INT, int_));
        stat.add(new Pair<MapleStat, Integer>(MapleStat.LUK, luk));
        stat.add(new Pair<MapleStat, Integer>(MapleStat.AVAILABLEAP, total));
        client.getSession().write(MaplePacketCreator.updatePlayerStats(stat, false, getJob()));
    }

    public Event_PyramidSubway getPyramidSubway() {
        return pyramidSubway;
    }

    public void setPyramidSubway(Event_PyramidSubway ps) {
        this.pyramidSubway = ps;
    }

    public byte getSubcategory() {
        if (job >= 430 && job <= 434) {
            return 1; //dont set it
        }
        return (byte) subcategory;
    }

    public int itemQuantity(final int itemid) {
        return getInventory(GameConstants.getInventoryType(itemid)).countById(itemid);
    }

    public void setRPS(RockPaperScissors rps) {
        this.rps = rps;
    }

    public RockPaperScissors getRPS() {
        return rps;
    }

    public long getNextConsume() {
        return nextConsume;
    }

    public void setNextConsume(long nc) {
        this.nextConsume = nc;
    }

    public int getRank() {
        return rank;
    }

    public int getRankMove() {
        return rankMove;
    }

    public int getJobRank() {
        return jobRank;
    }

    public int getJobRankMove() {
        return jobRankMove;
    }

    public void changeChannel(int channel) {
        Integer energyLevel = getBuffedValue(MapleBuffStat.ENERGY_CHARGE);
        if (energyLevel != null && energyLevel > 0) {
            setBuffedValue(MapleBuffStat.ENERGY_CHARGE, Integer.valueOf(energyLevel));
            List<Pair<MapleBuffStat, Integer>> stat = Collections.singletonList(new Pair<MapleBuffStat, Integer>(MapleBuffStat.ENERGY_CHARGE, energyLevel));
            client.getSession().write(MaplePacketCreator.能量条(stat, 0)); //???????????????????????????????
        }
        String[] socket = client.getChannelServer().getIP().split(":");
        final ChannelServer toch = ChannelServer.getInstance(channel);

        if (channel == client.getChannel() || toch == null || toch.isShutdown()) {
            // client.getSession().write(MaplePacketCreator.serverBlocked(1));
            return;
        }
        changeRemoval();

        final ChannelServer ch = ChannelServer.getInstance(client.getChannel());
        if (getMessenger() != null) {
            World.Messenger.silentLeaveMessenger(getMessenger().getId(), new MapleMessengerCharacter(this));
        }
        PlayerBuffStorage.addBuffsToStorage(getId(), getAllBuffs());
        PlayerBuffStorage.addCooldownsToStorage(getId(), getCooldowns());
        PlayerBuffStorage.addDiseaseToStorage(getId(), getAllDiseases());
        World.ChannelChange_Data(new CharacterTransfer(this), getId(), channel);
        ch.removePlayer(this);
        client.updateLoginState(LoginState.CHANGE_CHANNEL, client.getSessionIPAddress());
        String s = this.client.getSessionIPAddress();
        LoginServer.addIPAuth(s.substring(s.indexOf('/') + 1));
        client.getSession().write(MaplePacketCreator.getChannelChange(client, Integer.parseInt(toch.getIP().split(":")[1])));
        //LoginServer.addIPAuth(s.substring(s.indexOf(47) + 1, s.length()));
        //try {
        //    client.getSession().write(MaplePacketCreator.getChannelChange(InetAddress.getByName(socket[0]), Integer.parseInt(toch.getIP().split(":")[1])));
        //} catch (UnknownHostException ex) {
        //   Logger.getLogger(MapleCharacter.class.getName()).log(Level.SEVERE, null, ex);
        //}
        saveToDB(false, false);
        getMap().removePlayer(this);
        client.setPlayer(null);
        client.setReceiving(false);
        expirationTask(true, false);
    }

    public void expandInventory(byte type, int amount) {
        final MapleInventory inv = getInventory(MapleInventoryType.getByType(type));
        inv.addSlot((byte) amount);
        // client.getSession().write(MaplePacketCreator.getSlotUpdate(type, (byte) inv.getSlotLimit()));
    }

    public boolean allowedToTarget(MapleCharacter other) {
        return other != null && (!other.isHidden() || getGMLevel() >= other.getGMLevel());
    }

    public int getFollowId() {
        return followid;
    }

    public void setFollowId(int fi) {
        this.followid = fi;
        if (fi == 0) {
            this.followinitiator = false;
            this.followon = false;
        }
    }

    public void setFollowInitiator(boolean fi) {
        this.followinitiator = fi;
    }

    public void setFollowOn(boolean fi) {
        this.followon = fi;
    }

    public boolean isFollowOn() {
        return followon;
    }

    public boolean isFollowInitiator() {
        return followinitiator;
    }

    public void checkFollow() {
        if (followon) {
            //  map.broadcastMessage(MaplePacketCreator.followEffect(id, 0, null));
            //  map.broadcastMessage(MaplePacketCreator.followEffect(followid, 0, null));
            MapleCharacter tt = map.getCharacterById(followid);
            //client.getSession().write(MaplePacketCreator.getFollowMessage("Follow canceled."));
            if (tt != null) {
                tt.setFollowId(0);
                // tt.getClient().getSession().write(MaplePacketCreator.getFollowMessage("Follow canceled."));
            }
            setFollowId(0);
        }
    }

    public int getMarriageId() {
        return marriageId;
    }

    public void setMarriageId(final int mi) {
        this.marriageId = mi;
    }

    public int getMarriageItemId() {
        return marriageItemId;
    }

    public void setMarriageItemId(final int mi) {
        this.marriageItemId = mi;
    }

    public boolean isStaff() {
        return this.gmLevel > ServerConstants.PlayerGMRank.NORMAL.getLevel();
    }

    // TODO: gvup, vic, lose, draw, VR
    public boolean startPartyQuest(final int questid) {
        boolean ret = false;
        if (!quests.containsKey(MapleQuest.getInstance(questid)) || !questinfo.containsKey(questid)) {
            final MapleQuestStatus status = getQuestNAdd(MapleQuest.getInstance(questid));
            status.setStatus((byte) 1);
            updateQuest(status);
            switch (questid) {
                case 1300:
                case 1301:
                case 1302: //carnival, ariants.
                    updateInfoQuest(questid, "min=0;sec=0;date=0000-00-00;have=0;rank=F;try=0;cmp=0;CR=0;VR=0;gvup=0;vic=0;lose=0;draw=0");
                    break;
                case 1204: //herb town pq
                    updateInfoQuest(questid, "min=0;sec=0;date=0000-00-00;have0=0;have1=0;have2=0;have3=0;rank=F;try=0;cmp=0;CR=0;VR=0");
                    break;
                case 1206: //ellin pq
                    updateInfoQuest(questid, "min=0;sec=0;date=0000-00-00;have0=0;have1=0;rank=F;try=0;cmp=0;CR=0;VR=0");
                    break;
                default:
                    updateInfoQuest(questid, "min=0;sec=0;date=0000-00-00;have=0;rank=F;try=0;cmp=0;CR=0;VR=0");
                    break;
            }
            ret = true;
        } //started the quest.
        return ret;
    }

    public String getOneInfo(final int questid, final String key) {
        if (!questinfo.containsKey(questid) || key == null) {
            return null;
        }
        final String[] split = questinfo.get(questid).split(";");
        for (String x : split) {
            final String[] split2 = x.split("="); //should be only 2
            if (split2.length == 2 && split2[0].equals(key)) {
                return split2[1];
            }
        }
        return null;
    }

    public void updateOneInfo(final int questid, final String key, final String value) {
        if (!questinfo.containsKey(questid) || key == null || value == null) {
            return;
        }
        final String[] split = questinfo.get(questid).split(";");
        boolean changed = false;
        final StringBuilder newQuest = new StringBuilder();
        for (String x : split) {
            final String[] split2 = x.split("="); //should be only 2
            if (split2.length != 2) {
                continue;
            }
            if (split2[0].equals(key)) {
                newQuest.append(key).append("=").append(value);
            } else {
                newQuest.append(x);
            }
            newQuest.append(";");
            changed = true;
        }

        updateInfoQuest(questid, changed ? newQuest.toString().substring(0, newQuest.toString().length() - 1) : newQuest.toString());
    }

    public void recalcPartyQuestRank(final int questid) {
        if (!startPartyQuest(questid)) {
            final String oldRank = getOneInfo(questid, "rank");
            if (oldRank == null || oldRank.equals("S")) {
                return;
            }
            final String[] split = questinfo.get(questid).split(";");
            String newRank = null;
            if (oldRank.equals("A")) {
                newRank = "S";
            } else if (oldRank.equals("B")) {
                newRank = "A";
            } else if (oldRank.equals("C")) {
                newRank = "B";
            } else if (oldRank.equals("D")) {
                newRank = "C";
            } else if (oldRank.equals("F")) {
                newRank = "D";
            } else {
                return;
            }
            final List<Pair<String, Pair<String, Integer>>> questInfo = MapleQuest.getInstance(questid).getInfoByRank(newRank);
            for (Pair<String, Pair<String, Integer>> q : questInfo) {
                boolean found = false;
                final String val = getOneInfo(questid, q.right.left);
                if (val == null) {
                    return;
                }
                int vall = 0;
                try {
                    vall = Integer.parseInt(val);
                } catch (NumberFormatException e) {
                    return;
                }
                if (q.left.equals("less")) {
                    found = vall < q.right.right;
                } else if (q.left.equals("more")) {
                    found = vall > q.right.right;
                } else if (q.left.equals("equal")) {
                    found = vall == q.right.right;
                }
                if (!found) {
                    return;
                }
            }
            //perfectly safe
            updateOneInfo(questid, "rank", newRank);
        }
    }

    public void tryPartyQuest(final int questid) {
        try {
            startPartyQuest(questid);
            pqStartTime = System.currentTimeMillis();
            updateOneInfo(questid, "try", String.valueOf(Integer.parseInt(getOneInfo(questid, "try")) + 1));
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.debug("tryPartyQuest error");
        }
    }

    public void endPartyQuest(final int questid) {
        try {
            startPartyQuest(questid);
            if (pqStartTime > 0) {
                final long changeTime = System.currentTimeMillis() - pqStartTime;
                final int mins = (int) (changeTime / 1000 / 60), secs = (int) (changeTime / 1000 % 60);
                final int mins2 = Integer.parseInt(getOneInfo(questid, "min")), secs2 = Integer.parseInt(getOneInfo(questid, "sec"));
                if (mins2 <= 0 || mins < mins2) {
                    updateOneInfo(questid, "min", String.valueOf(mins));
                    updateOneInfo(questid, "sec", String.valueOf(secs));
                    updateOneInfo(questid, "date", FileoutputUtil.CurrentReadable_Date());
                }
                final int newCmp = Integer.parseInt(getOneInfo(questid, "cmp")) + 1;
                updateOneInfo(questid, "cmp", String.valueOf(newCmp));
                updateOneInfo(questid, "CR", String.valueOf((int) Math.ceil((newCmp * 100.0) / Integer.parseInt(getOneInfo(questid, "try")))));
                recalcPartyQuestRank(questid);
                pqStartTime = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.debug("endPartyQuest error");
        }

    }

    public void havePartyQuest(final int itemId) {
        int questid = 0, index = -1;
        switch (itemId) {
            case 1002798:
                questid = 1200; //henesys
                break;
            case 1072369:
                questid = 1201; //kerning
                break;
            case 1022073:
                questid = 1202; //ludi
                break;
            case 1082232:
                questid = 1203; //orbis
                break;
            case 1002571:
            case 1002572:
            case 1002573:
            case 1002574:
                questid = 1204; //herbtown
                index = itemId - 1002571;
                break;
            case 1122010:
                questid = 1205; //magatia
                break;
            case 1032061:
            case 1032060:
                questid = 1206; //ellin
                index = itemId - 1032060;
                break;
            case 3010018:
                questid = 1300; //ariant
                break;
            case 1122007:
                questid = 1301; //carnival
                break;
            case 1122058:
                questid = 1302; //carnival2
                break;
            default:
                return;
        }
        startPartyQuest(questid);
        updateOneInfo(questid, "have" + (index == -1 ? "" : index), "1");
    }

    public void resetStatsByJob(boolean beginnerJob) {
        int baseJob = (beginnerJob ? (job % 1000) : (job % 1000 / 100 * 100)); //1112 -> 112 -> 1 -> 100
        if (baseJob == 100) { //first job = warrior
            resetStats(25, 4, 4, 4);
        } else if (baseJob == 200) {
            resetStats(4, 4, 20, 4);
        } else if (baseJob == 300 || baseJob == 400) {
            resetStats(4, 25, 4, 4);
        } else if (baseJob == 500) {
            resetStats(4, 20, 4, 4);
        }
    }

    public boolean hasSummon() {
        return hasSummon;
    }

    public void setHasSummon(boolean summ) {
        this.hasSummon = summ;
    }

    public void removeDoor() {
        final MapleDoor door = getDoors().iterator().next();
        for (final MapleCharacter chr : door.getTarget().getCharactersThreadsafe()) {
            door.sendDestroyData(chr.getClient());
        }
        for (final MapleCharacter chr : door.getTown().getCharactersThreadsafe()) {
            door.sendDestroyData(chr.getClient());
        }
        for (final MapleDoor destroyDoor : getDoors()) {
            door.getTarget().removeMapObject(destroyDoor);
            door.getTown().removeMapObject(destroyDoor);
        }
        clearDoors();
    }

    public void changeRemoval() {
        changeRemoval(false);
    }

    public void changeRemoval(boolean dc) {
        if (getTrade() != null) {
            MapleTrade.cancelTrade(getTrade(), client);
        }
        if (getCheatTracker() != null) {
            getCheatTracker().dispose();
        }
        if (!dc) {
            cancelEffectFromBuffStat(MapleBuffStat.骑兽技能);
            cancelEffectFromBuffStat(MapleBuffStat.SUMMON);
            cancelEffectFromBuffStat(MapleBuffStat.REAPER);
            cancelEffectFromBuffStat(MapleBuffStat.PUPPET);
        }
        if (getPyramidSubway() != null) {
            getPyramidSubway().dispose(this);
        }
        if (playerShop != null && !dc) {
            playerShop.removeVisitor(this);
            if (playerShop.isOwner(this)) {
                playerShop.setOpen(true);
            }
        }
        if (!getDoors().isEmpty()) {
            removeDoor();
        }
        disposeClones();
        NPCScriptManager.getInstance().dispose(client);
    }

    public void updateTick(int newTick) {
        anticheat.updateTick(newTick);
    }

    public boolean canUseFamilyBuff(MapleFamilyBuffEntry buff) {
        final MapleQuestStatus stat = getQuestNAdd(MapleQuest.getInstance(buff.questID));
        if (stat.getCustomData() == null) {
            stat.setCustomData("0");
        }
        return Long.parseLong(stat.getCustomData()) + (24 * 3600000) < System.currentTimeMillis();
    }

    public void useFamilyBuff(MapleFamilyBuffEntry buff) {
        final MapleQuestStatus stat = getQuestNAdd(MapleQuest.getInstance(buff.questID));
        stat.setCustomData(String.valueOf(System.currentTimeMillis()));
    }

    public List<Pair<Integer, Integer>> usedBuffs() {
        //assume count = 1
        List<Pair<Integer, Integer>> used = new ArrayList<Pair<Integer, Integer>>();
        for (MapleFamilyBuffEntry buff : MapleFamilyBuff.getBuffEntry()) {
            if (!canUseFamilyBuff(buff)) {
                used.add(new Pair<Integer, Integer>(buff.index, buff.count));
            }
        }
        return used;
    }

    public String getTeleportName() {
        return teleportname;
    }

    public void setTeleportName(final String tname) {
        teleportname = tname;
    }

    public int getNoJuniors() {
        if (mfc == null) {
            return 0;
        }
        return mfc.getNoJuniors();
    }

    public MapleFamilyCharacter getMFC() {
        return mfc;
    }

    public void makeMFC(final int familyid, final int seniorid, final int junior1, final int junior2) {
        if (familyid > 0) {
            MapleFamily f = World.Family.getFamily(familyid);
            if (f == null) {
                mfc = null;
            } else {
                mfc = f.getMFC(id);
                if (mfc == null) {
                    mfc = f.addFamilyMemberInfo(this, seniorid, junior1, junior2);
                }
                if (mfc.getSeniorId() != seniorid) {
                    mfc.setSeniorId(seniorid);
                }
                if (mfc.getJunior1() != junior1) {
                    mfc.setJunior1(junior1);
                }
                if (mfc.getJunior2() != junior2) {
                    mfc.setJunior2(junior2);
                }
            }
        } else {
            mfc = null;
        }
    }

    public void setFamily(final int newf, final int news, final int newj1, final int newj2) {
        if (mfc == null || newf != mfc.getFamilyId() || news != mfc.getSeniorId() || newj1 != mfc.getJunior1() || newj2 != mfc.getJunior2()) {
            makeMFC(newf, news, newj1, newj2);
        }
    }

    public int maxBattleshipHP(int skillid) {
        return (getSkillLevel(skillid) * 5000) + ((getLevel() - 120) * 3000);
    }

    public int currentBattleshipHP() {
        return battleshipHP;
    }

    public void sendEnglishQuiz(String msg) {
        client.getSession().write(MaplePacketCreator.englishQuizMsg(msg));
    }

    public void fakeRelog() {
        client.getSession().write(MaplePacketCreator.getCharInfo(this));
        final MapleMap mapp = getMap();
//        mapp.setCheckStates(false);
        mapp.removePlayer(this);
        mapp.addPlayer(this);
//        mapp.setCheckStates(true);
    }

    /*
     * public String getcharmessage(){ LOGGER.error("CharMessage(get)");
     * return charmessage; }
     *
     * public void setcharmessage(int s){
     * LOGGER.error("CharMessage(set)"); charmessage += s; }
     */
    public String getcharmessage() {
        //LOGGER.error("CharMessage(get)");
        return charmessage;
    }

    public void setcharmessage(String s) {
        //LOGGER.error("CharMessage(set)");
        charmessage /*
         * +
         */ = s;
    }

    public int getexpression() {
        return expression;
    }

    public void setexpression(int s) {
        expression /*
         * +
         */ = s;
    }

    public int getconstellation() {
        return constellation;
    }

    public void setconstellation(int s) {
        constellation /*
         * +
         */ = s;
    }

    public int getblood() {
        return blood;
    }

    public void setblood(int s) {
        blood /*
         * +
         */ = s;
    }

    public int getmonth() {
        return month;
    }

    public void setmonth(int s) {
        month /*
         * +
         */ = s;
    }

    public int getday() {
        return day;
    }

    public void setday(int s) {
        day /*
         * +
         */ = s;
    }

    public int getTeam() {
        return coconutteam;
    }

    public int getBeans() {
        return beans;
    }

    public void gainBeans(int s) {
        beans += s;
    }

    public void setBeans(int s) {
        beans = s;
    }

    public int getBeansNum() {
        return beansNum;
    }

    /**
     * @param beansNum the beansNum to set
     */
    public void setBeansNum(int beansNum) {
        this.beansNum = beansNum;
    }

    /**
     * @return the beansRange
     */
    public int getBeansRange() {
        return beansRange;
    }

    /**
     * @param beansRange the beansRange to set
     */
    public void setBeansRange(int beansRange) {
        beansRange = beansRange;
    }

    /**
     * @return the canSetBeansNum
     */
    public boolean isCanSetBeansNum() {
        return canSetBeansNum;
    }

    /**
     * @param canSetBeansNum the canSetBeansNum to set
     */
    public void setCanSetBeansNum(boolean canSetBeansNum) {
        this.canSetBeansNum = canSetBeansNum;
    }

    public boolean haveGM() {
        return gmLevel >= 2 && gmLevel <= 3;
    }

    public void setprefix(int prefix) {
        this.prefix = prefix;
    }

    public int getPrefix() {
        return prefix;
    }

    public void startMapEffect(String msg, int itemId) {
        startMapEffect(msg, itemId, 10000);
    }

    public void startMapEffect1(String msg, int itemId) {
        startMapEffect(msg, itemId, 20000);
    }

    public void startMapEffect(String msg, int itemId, int duration) {
        final MapleMapEffect mapEffect = new MapleMapEffect(msg, itemId);
        getClient().getSession().write(mapEffect.makeStartData());
        Timer.EVENT.schedule(new Runnable() {

            @Override
            public void run() {
                getClient().getSession().write(mapEffect.makeDestroyData());
            }
        }, duration);
    }

    public int getHyPay(int type) {
        DHyPay one = new QDHyPay().accName.eq(getClient().getAccountName()).findOne();
        if (one != null) {
            switch (type) {
                case 1:
                    return one.getPay();
                case 2:
                    return one.getPayUsed();
                case 3:
                    return one.getPay() + one.getPayUsed();
                case 4:
                    return one.getPayReward();
                default:
                    break;
            }
        } else {
            one = new DHyPay();
            one.setAccName(getClient().getAccountName());
            one.setPay(0);
            one.setPayUsed(0);
            one.setPayReward(0);
            one.save();
        }
        return 0;
    }

    public int gainHyPay(int hypay) {
        if (hypay <= 0) {
            return 0;
        }
        // todo 删除这些垃圾代码
        int pay = getHyPay(1);
        int payUsed = getHyPay(2);
        int payReward = getHyPay(4);
        DHyPay one = new QDHyPay().accName.eq(getClient().getAccountName()).findOne();
        if (one != null) {
            one.setPay(pay + hypay);
            one.setPayUsed(payUsed);
            one.setPayReward(payReward);
            one.save();
            return 1;
        }
        return 0;
    }

    public int addHyPay(int hypay) {
        int pay = getHyPay(1);
        if (hypay > pay) {
            return -1;
        }
        int payUsed = getHyPay(2);
        int payReward = getHyPay(4);
        DHyPay one = new QDHyPay().accName.eq(getClient().getAccountName()).findOne();
        if (one != null) {
            one.setPay(pay - hypay);
            one.setPayUsed(payUsed + hypay);
            one.setPayReward(payReward + hypay);
            one.save();
            return 1;
        }
        return -1;
    }

    public int delPayReward(int pay) {
        if (pay <= 0) {
            return -1;
        }
        int payReward = getHyPay(4);
        if (pay > payReward) {
            return -1;
        }
        DHyPay one = new QDHyPay().accName.eq(getClient().getAccountName()).findOne();
        if (one != null) {
            one.setPayReward(payReward - pay);
            one.save();
            return 1;
        }
        return -1;
    }

    public int getGamePoints() {
        DAccountInfo one = new QDAccountInfo().account.id.eq(getClient().getAccID()).worldId.eq(getWorld()).findOne();
        if (one != null) {
            if (one.getUpdated() != null && one.getUpdated().isBefore(LocalDateTime.now())) {
                one.setGamePoints(0);
                one.setUpdated(LocalDateTime.now());
            }
        } else {
            one = new DAccountInfo();
            one.setAccount(new QDAccount().id.eq(getClient().getAccID()).findOne());
            one.setWorldId(getWorld());
            one.setGamePoints(0);
        }
        one.save();
        return one.getGamePoints();
    }

    public int getGamePointsPD() {
        DAccountInfo one = new QDAccountInfo().account.id.eq(getClient().getAccID()).worldId.eq(getWorld()).findOne();
        if (one != null) {
            if (one.getUpdated() != null && one.getUpdated().isBefore(LocalDateTime.now())) {
                one.setGamePoints(0);
                one.setUpdated(LocalDateTime.now());
            }
        } else {
            one = new DAccountInfo();
            one.setAccount(new QDAccount().id.eq(getClient().getAccID()).findOne());
            one.setWorldId(getWorld());
            one.setGamePoints(0);
        }
        one.save();
        return one.getGamePoints();
    }

    public void gainGamePoints(int amount) {
        int gamePoints = getGamePoints() + amount;
        updateGamePoints(gamePoints);
    }

    public void gainGamePointsPD(int amount) {
        int gamePointsPD = getGamePointsPD() + amount;
        updateGamePointsPD(gamePointsPD);
    }

    public void resetGamePointsPD() {
        updateGamePointsPD(0);
    }

    public void updateGamePointsPD(int amount) {
        new QDAccountInfo().account.id.eq(getClient().getAccID()).worldId.eq(getWorld()).asUpdate()
                .set("gamePointsPd", amount)
                .set("updated", LocalDate.now())
                .update();
    }

    public void resetGamePoints() {
        updateGamePoints(0);
    }

    public void updateGamePoints(int amount) {
        new QDAccountInfo().account.id.eq(getClient().getAccID()).worldId.eq(getWorld()).asUpdate()
                .set("gamePoints", amount)
                .set("updated", LocalDate.now())
                .update();
    }

    public int getGamePointsRQ() {
        DAccountInfo one = new QDAccountInfo().account.id.eq(getClient().getAccID()).worldId.eq(getWorld()).findOne();
        if (one != null) {
            if (one.getUpdated() != null && one.getUpdated().isBefore(LocalDateTime.now())) {
                one.setGamePointsPd(0);
                one.setUpdated(LocalDateTime.now());
            }
        } else {
            one = new DAccountInfo();
            one.setAccount(new QDAccount().id.eq(getClient().getAccID()).findOne());
            one.setWorldId(getWorld());
            one.setGamePointsPd(0);
        }
        one.save();
        return one.getGamePointsPd();
    }

    public void gainGamePointsRQ(int amount) {
        int gamePointsRQ = getGamePointsRQ() + amount;
        updateGamePointsRQ(gamePointsRQ);
    }

    public void resetGamePointsRQ() {
        updateGamePointsRQ(0);
    }

    public void updateGamePointsRQ(int amount) {
        new QDAccountInfo().account.id.eq(getClient().getAccID()).worldId.eq(getWorld())
                .asUpdate()
                .set("gamePointsPd", amount)
                .update();
    }

    public long getDeadtime() {
        return this.deadtime;
    }

    public void setDeadtime(long deadtime) {
        this.deadtime = deadtime;
    }

    public void increaseEquipExp(int mobexp) { //道具经验
        MapleItemInformationProvider mii = MapleItemInformationProvider.getInstance();
        try {
            for (IItem item : getInventory(MapleInventoryType.EQUIPPED).list()) {
                Equip nEquip = (Equip) item;
                String itemName = mii.getName(nEquip.getItemId());
                if (itemName == null) {
                    continue;
                }
                //////LOGGER.debug("执行1");
                if ((itemName.contains("重生") && nEquip.getEquipLevel() < 4) || itemName.contains("永恒") && nEquip.getEquipLevel() < 6) {
                    //////LOGGER.debug("执行2");
                    nEquip.gainItemExp(client, mobexp, itemName.contains("永恒"));
                }
                // ////LOGGER.debug("执行3");
            }
        } catch (Exception ex) {
        }
    }

    public void reloadC() {
        client.getSession().write(MaplePacketCreator.getCharInfo(client.getPlayer()));
        client.getPlayer().getMap().removePlayer(client.getPlayer());
        client.getPlayer().getMap().addPlayer(client.getPlayer());
    }

    public void maxSkills() {
        for (ISkill sk : SkillFactory.getAllSkills()) {
            changeSkillLevel(sk, sk.getMaxLevel(), sk.getMaxLevel());
        }
    }

    public void UpdateCash() {
        getClient().getSession().write(MaplePacketCreator.showCharCash(this));
    }

    public static String getAriantRoomLeaderName(int room) {
        return ariantroomleader[room];
    }

    public static int getAriantSlotsRoom(int room) {
        return ariantroomslot[room];
    }

    public static void removeAriantRoom(int room) {
        ariantroomleader[room] = "";
        ariantroomslot[room] = 0;
    }

    public static void setAriantRoomLeader(int room, String charname) {
        ariantroomleader[room] = charname;
    }

    public static void setAriantSlotRoom(int room, int slot) {
        ariantroomslot[room] = slot;
    }

    public void addAriantScore() {
        ariantScore++;
    }

    public void resetAriantScore() {
        ariantScore = 0;
    }

    public int getAriantScore() { // TODO: code entire score system
        return ariantScore;
    }

    public void updateAriantScore() {
        getMap().broadcastMessage(MaplePacketCreator.updateAriantScore(this.getName(), getAriantScore(), false));
    }

    public int getAveragePartyLevel() {
        int averageLevel = 0, size = 0;
        for (MaplePartyCharacter pl : getParty().getMembers()) {
            averageLevel += pl.getLevel();
            size++;
        }
        if (size <= 0) {
            return level;
        }
        averageLevel /= size;
        return averageLevel;
    }

    public int getAverageMapLevel() {
        int averageLevel = 0, size = 0;
        for (MapleCharacter pl : getMap().getCharacters()) {
            averageLevel += pl.getLevel();
            size++;
        }
        if (size <= 0) {
            return level;
        }
        averageLevel /= size;
        return averageLevel;
    }

    public void setApprentice(int app) {
        this.apprentice = app;
    }

    public boolean hasApprentice() {
        if (apprentice > 0) {
            return true;
        } else {
            return false;
        }
    }

    public int getMaster() {
        return this.master;
    }

    public int getApprentice() {
        return this.apprentice;
    }

    public MapleCharacter getApp() {
        return client.getChannelServer().getPlayerStorage().getCharacterById(this.apprentice);
    }

    public MapleCharacter getMster() {
        return client.getChannelServer().getPlayerStorage().getCharacterById(this.master);
    }

    public void setMaster(int mstr) {
        this.master = mstr;
    }

    public MapleRing getMarriageRing(boolean incluedEquip) {
        MapleInventory iv = getInventory(MapleInventoryType.EQUIPPED);
        Collection<IItem> equippedC = iv.list();
        List<Item> equipped = new ArrayList<>(equippedC.size());
        MapleRing ring;
        for (IItem item : equippedC) {
            equipped.add((Item) item);
        }
        for (Item item : equipped) {
            if (item.getRing() != null) {
                ring = item.getRing();
                ring.setEquipped(true);
                if (GameConstants.isMarriageRing(item.getItemId())) {
                    return ring;
                }
            }
        }
        if (incluedEquip) {
            iv = getInventory(MapleInventoryType.EQUIP);
            for (IItem item : iv.list()) {
                if (item.getRing() != null && GameConstants.isMarriageRing(item.getItemId())) {
                    ring = item.getRing();
                    ring.setEquipped(false);
                    return ring;
                }
            }
        }
        return null;
    }

    public void setDebugMessage(boolean control) {
        DebugMessage = control;
    }

    public boolean getDebugMessage() {
        return DebugMessage;
    }

    public long getNX() {
        return getCSPoints(1);
    }

    public final boolean canHold(final int itemid) {
        return getInventory(GameConstants.getInventoryType(itemid)).getNextFreeSlot() > -1;
    }

    public int getIntRecord(int questID) {
        final MapleQuestStatus stat = getQuestNAdd(MapleQuest.getInstance(questID));
        if (stat.getCustomData() == null) {
            stat.setCustomData("0");
        }
        return Integer.parseInt(stat.getCustomData());
    }

    public int getIntNoRecord(int questID) {
        final MapleQuestStatus stat = getQuestNoAdd(MapleQuest.getInstance(questID));
        if (stat == null || stat.getCustomData() == null) {
            return 0;
        }
        return Integer.parseInt(stat.getCustomData());
    }

    /* public void updatePetEquip() {
        if (getIntNoRecord(122221) > 0) {
            client.getSession().write(MaplePacketCreator.petAutoHP(getIntRecord(122221)));
        }
        if (getIntNoRecord(122222) > 0) {
            client.getSession().write(MaplePacketCreator.petAutoMP(getIntRecord(122222)));
        }
    }*/
    public void spawnBomb() {
        final MapleMonster bomb = MapleLifeFactory.getMonster(9300166);
        bomb.changeLevel(250, true);
        getMap().spawnMonster_sSack(bomb, getPosition(), -2);
        Timer.EVENT.schedule(() -> map.killMonster(bomb, client.getPlayer(), false, false, (byte) 1), 10 * 1000);
    }

    public boolean isAriantPQMap() {
        switch (getMapId()) {
            case 980010101:
            case 980010201:
            case 980010301:
                return true;
        }
        return false;
    }

    private int MobVac = 0, MobVac2 = 0;

    public void addMobVac(int type) {
        if (type == 1) {
            MobVac++;
        } else if (type == 2) {
            MobVac2++;
        }
    }

    public int getMobVac(int type) {
        if (type == 1) {
            return MobVac;
        } else if (type == 2) {
            return MobVac2;
        } else {
            return 0;
        }
    }

    private int mount_id = 0;

    public int getMountId() {
        return mount_id;
    }

    public void setMountId(int id) {
        mount_id = id;
    }

    public void gainIten(int id, int amount) {
        MapleInventoryManipulator.addById(getClient(), id, (short) amount, (byte) 0);
    }

    public long getLastHM() {
        return lastGainHM;
    }

    public void setLastHM(long newTime) {
        lastGainHM = newTime;
    }

    public void blockPortal(String scriptName) {
        if (!blockedPortals.contains(scriptName) && scriptName != null) {
            blockedPortals.add(scriptName);
        }
        getClient().getSession().write(MaplePacketCreator.blockedPortal());
    }

    public void unblockPortal(String scriptName) {
        if (blockedPortals.contains(scriptName) && scriptName != null) {
            blockedPortals.remove(scriptName);
        }
    }

    public int getskillzq() {
        return skillzq;
    }

    public void setskillzq(int s) {
        skillzq = s;
    }

    public int getbosslog() {
        return bosslog;
    }

    public void setbosslog(int s) {
        bosslog = s;
    }

    public int getgrname() {
        return grname;
    }

    public void setgrname(int s) {
        grname = s;
    }

    public int getjzname() {
        return jzname;
    }

    public void setjzname(int s) {
        jzname = s;
    }

    //--------------------------------------------赏金任务
    public int getSJRW() {
        DAccountInfo one = new QDAccountInfo().account.id.eq(getClient().getAccID()).worldId.eq(getWorld()).findOne();
        if (one != null) {
            if (one.getUpdated() != null && one.getUpdated().isBefore(LocalDateTime.now())) {
                one.setSjrw(0);
                one.setUpdated(LocalDateTime.now());
            }
        } else {
            one = new DAccountInfo();
            one.setAccount(new QDAccount().id.eq(getClient().getAccID()).findOne());
            one.setWorldId(getWorld());
            one.setSjrw(0);
        }
        one.save();
        return one.getSjrw();
    }

    public void gainSJRW(int amount) {
        int sjrw = getSJRW() + amount;
        updateSJRW(sjrw);
    }

    public void resetSJRW() {
        updateSJRW(0);
    }

    public void updateSJRW(int amount) {
        DAccountInfo one = new QDAccountInfo().account.id.eq(getClient().getAccID()).worldId.eq(getWorld()).findOne();
        if (one == null) {
            one = new DAccountInfo();
            one.setAccount(new QDAccount().id.eq(getClient().getAccID()).findOne());
            one.setWorldId(getWorld());
        }
        one.setSjrw(amount);
        one.setUpdated(LocalDateTime.now());
        one.save();
    }

    //--------------------------------------------每日副本任务
    public int getFBRW() {
        DAccountInfo one = new QDAccountInfo().account.id.eq(getClient().getAccID()).worldId.eq(getWorld()).findOne();
        if (one != null) {
            if (one.getUpdated() != null && one.getUpdated().isBefore(LocalDateTime.now())) {
                one.setFbrw(0);
                one.setUpdated(LocalDateTime.now());
            }
        } else {
            one = new DAccountInfo();
            one.setAccount(new QDAccount().id.eq(getClient().getAccID()).findOne());
            one.setWorldId(getWorld());
            one.setFbrw(0);
        }
        one.save();
        return one.getFbrw();
    }

    public void gainFBRW(int amount) {
        int fbrw = getFBRW() + amount;
        updateFBRW(fbrw);
    }

    public void resetFBRW() {
        updateFBRW(0);
    }

    public void updateFBRW(int amount) {
        DAccountInfo one = new QDAccountInfo().account.id.eq(getClient().getAccID()).worldId.eq(getWorld()).findOne();
        if (one == null) {
            one = new DAccountInfo();
            one.setAccount(new QDAccount().id.eq(getClient().getAccID()).findOne());
            one.setWorldId(getWorld());
        }
        one.setFbrw(amount);
        one.setUpdated(LocalDateTime.now());
        one.save();
    }

    public int getFBRWA() {
        DAccountInfo one = new QDAccountInfo().account.id.eq(getClient().getAccID()).worldId.eq(getWorld()).findOne();
        if (one != null) {
            if (one.getUpdated() != null && one.getUpdated().isBefore(LocalDateTime.now())) {
                one.setFbrwa(0);
                one.setUpdated(LocalDateTime.now());
            }
        } else {
            one = new DAccountInfo();
            one.setAccount(new QDAccount().id.eq(getClient().getAccID()).findOne());
            one.setWorldId(getWorld());
            one.setFbrwa(0);
        }
        one.save();
        return one.getFbrwa();
    }

    public void gainFBRWA(int amount) {
        int fbrw = getFBRWA() + amount;
        updateFBRWA(fbrw);
    }

    public void resetFBRWA() {
        updateFBRWA(0);
    }

    public void updateFBRWA(int amount) {
        DAccountInfo one = new QDAccountInfo().account.id.eq(getClient().getAccID()).worldId.eq(getWorld()).findOne();
        if (one == null) {
            one = new DAccountInfo();
            one.setAccount(new QDAccount().id.eq(getClient().getAccID()).findOne());
            one.setWorldId(getWorld());
        }
        one.setFbrwa(amount);
        one.setUpdated(LocalDateTime.now());
        one.save();
    }

    //--------------------------------------------每日杀怪任务
    public int getSGRW() {
        DAccountInfo one = new QDAccountInfo().account.id.eq(getClient().getAccID()).worldId.eq(getWorld()).findOne();
        if (one != null) {
            if (one.getUpdated() != null && one.getUpdated().isBefore(LocalDateTime.now())) {
                one.setSgrw(0);
                one.setUpdated(LocalDateTime.now());
            }
        } else {
            one = new DAccountInfo();
            one.setAccount(new QDAccount().id.eq(getClient().getAccID()).findOne());
            one.setWorldId(getWorld());
            one.setSgrw(0);
        }
        one.save();
        return one.getSgrw();
    }

    public void gainSGRW(int amount) {
        int sgrw = getSGRW() + amount;
        updateSGRW(sgrw);
    }

    public void resetSGRW() {
        updateSGRW(0);
    }

    public void updateSGRW(int amount) {
        DAccountInfo one = new QDAccountInfo().account.id.eq(getClient().getAccID()).worldId.eq(getWorld()).findOne();
        if (one == null) {
            one = new DAccountInfo();
            one.setAccount(new QDAccount().id.eq(getClient().getAccID()).findOne());
            one.setWorldId(getWorld());
        }
        one.setSgrw(amount);
        one.setUpdated(LocalDateTime.now());
        one.save();
    }

    public int getSGRWA() {
        DAccountInfo one = new QDAccountInfo().account.id.eq(getClient().getAccID()).worldId.eq(getWorld()).findOne();
        if (one != null) {
            if (one.getUpdated() != null && one.getUpdated().isBefore(LocalDateTime.now())) {
                one.setSgrwa(0);
                one.setUpdated(LocalDateTime.now());
            }
        } else {
            one = new DAccountInfo();
            one.setAccount(new QDAccount().id.eq(getClient().getAccID()).findOne());
            one.setWorldId(getWorld());
            one.setSgrwa(0);
        }
        one.save();
        return one.getSgrwa();
    }

    public void gainSGRWA(int amount) {
        int sgrw = getSGRWA() + amount;
        updateSGRWA(sgrw);
    }

    public void resetSGRWA() {
        updateSGRWA(0);
    }

    public void updateSGRWA(int amount) {
        DAccountInfo one = new QDAccountInfo().account.id.eq(getClient().getAccID()).worldId.eq(getWorld()).findOne();
        if (one == null) {
            one = new DAccountInfo();
            one.setAccount(new QDAccount().id.eq(getClient().getAccID()).findOne());
            one.setWorldId(getWorld());
        }
        one.setSgrwa(amount);
        one.setUpdated(LocalDateTime.now());
        one.save();
    }

    //--------------------------------------------每日杀BOSS任务
    public int getSBOSSRW() {
        DAccountInfo one = new QDAccountInfo().account.id.eq(getClient().getAccID()).worldId.eq(getWorld()).findOne();
        if (one != null) {
            if (one.getUpdated() != null && one.getUpdated().isBefore(LocalDateTime.now())) {
                one.setSbossrw(0);
                one.setUpdated(LocalDateTime.now());
            }
        } else {
            one = new DAccountInfo();
            one.setAccount(new QDAccount().id.eq(getClient().getAccID()).findOne());
            one.setWorldId(getWorld());
            one.setSbossrw(0);
        }
        one.save();
        return one.getSbossrw();
    }

    public void gainSBOSSRW(int amount) {
        int sbossrw = getSBOSSRW() + amount;
        updateSBOSSRW(sbossrw);
    }

    public void resetSBOSSRW() {
        updateSBOSSRW(0);
    }

    public void updateSBOSSRW(int amount) {
        DAccountInfo one = new QDAccountInfo().account.id.eq(getClient().getAccID()).worldId.eq(getWorld()).findOne();
        if (one == null) {
            one = new DAccountInfo();
            one.setAccount(new QDAccount().id.eq(getClient().getAccID()).findOne());
            one.setWorldId(getWorld());
        }
        one.setSbossrw(amount);
        one.setUpdated(LocalDateTime.now());
        one.save();
    }

    public int getSBOSSRWA() {
        DAccountInfo one = new QDAccountInfo().account.id.eq(getClient().getAccID()).worldId.eq(getWorld()).findOne();
        if (one != null) {
            if (one.getUpdated() != null && one.getUpdated().isBefore(LocalDateTime.now())) {
                one.setSbossrwa(0);
                one.setUpdated(LocalDateTime.now());
            }
        } else {
            one = new DAccountInfo();
            one.setAccount(new QDAccount().id.eq(getClient().getAccID()).findOne());
            one.setWorldId(getWorld());
            one.setSbossrwa(0);
        }
        one.save();
        return one.getSbossrwa();
    }

    public void gainSBOSSRWA(int amount) {
        int sbossrw = getSBOSSRWA() + amount;
        updateSBOSSRWA(sbossrw);
    }

    public void resetSBOSSRWA() {
        updateSBOSSRWA(0);
    }

    public void updateSBOSSRWA(int amount) {
        DAccountInfo one = new QDAccountInfo().account.id.eq(getClient().getAccID()).worldId.eq(getWorld()).findOne();
        if (one == null) {
            one = new DAccountInfo();
            one.setAccount(new QDAccount().id.eq(getClient().getAccID()).findOne());
            one.setWorldId(getWorld());
        }
        one.setSbossrwa(amount);
        one.setUpdated(LocalDateTime.now());
        one.save();
    }

    //-------------七天礼包判断日期函数
    public int getlb() {
        DAccountInfo one = new QDAccountInfo().account.id.eq(getClient().getAccID()).worldId.eq(getWorld()).findOne();
        if (one != null) {
            if (one.getUpdated() != null && one.getUpdated().isBefore(LocalDateTime.now())) {
                one.setLb(0);
                one.setUpdated(LocalDateTime.now());
            }
        } else {
            one = new DAccountInfo();
            one.setAccount(new QDAccount().id.eq(getClient().getAccID()).findOne());
            one.setWorldId(getWorld());
            one.setLb(0);
        }
        one.save();
        return one.getLb();
    }

    public void gainlb(int amount) {
        int lb = getlb() + amount;
        updatelb(lb);
    }

    public void resetlb() {
        updatelb(0);
    }

    public void updatelb(int amount) {
        DAccountInfo one = new QDAccountInfo().account.id.eq(getClient().getAccID()).worldId.eq(getWorld()).findOne();
        if (one == null) {
            one = new DAccountInfo();
            one.setAccount(new QDAccount().id.eq(getClient().getAccID()).findOne());
            one.setWorldId(getWorld());
        }
        one.setLb(amount);
        one.setUpdated(LocalDateTime.now());
        one.save();
    }

    public int getmrsgrw() {
        return mrsgrw;
    }

    public void setmrsgrw(int s) {
        mrsgrw = s;
    }

    public int getmrsgrwa() {
        return mrsgrwa;
    }

    public void setmrsgrwa(int s) {
        mrsgrwa = s;
    }

    public int getmrsgrwas() {
        return mrsgrwas;
    }

    public void setmrsgrwas(int s) {
        mrsgrwas = s;
    }

    public int getmrsgrws() {
        return mrsgrws;
    }

    public void setmrsgrws(int s) {
        mrsgrws = s;
    }

    public int gethythd() {
        return hythd;
    }

    public void sethythd(int s) {
        hythd = s;
    }

    public int getmrsjrw() {
        return mrsjrw;
    }

    public void setmrsjrw(int s) {
        mrsjrw = s;
    }

    public int getmrfbrw() {
        return mrfbrw;
    }

    public void setmrfbrw(int s) {
        mrfbrw = s;
    }

    public int getmrsbossrw() {
        return mrsbossrw;
    }

    public void setmrsbossrw(int s) {
        mrsbossrw = s;
    }

    public int getmrfbrws() {
        return mrfbrws;
    }

    public void setmrfbrws(int s) {
        mrfbrws = s;
    }

    public int getmrsbossrws() {
        return mrsbossrws;
    }

    public void setmrsbossrws(int s) {
        mrsbossrws = s;
    }

    public int getmrfbrwa() {
        return mrfbrwa;
    }

    public void setmrfbrwa(int s) {
        mrfbrwa = s;
    }

    public int getmrsbossrwa() {
        return mrsbossrwa;
    }

    public void setmrsbossrwa(int s) {
        mrsbossrwa = s;
    }

    public int getmrfbrwas() {
        return mrfbrwas;
    }

    public void setmrfbrwas(int s) {
        mrfbrwas = s;
    }

    public int getvip() {
        return vip;
    }

    public void setvip(int s) {
        vip = s;
    }

    public void gainvip(int s) {
        vip += s;
    }

    public int getddj() {
        return ddj;
    }

    public void setddj(int s) {
        ddj = s;
    }

    public void gainddj(int s) {
        ddj += s;
    }

    public int getmrsbossrwas() {
        return mrsbossrwas;
    }

    public void setmrsbossrwas(int s) {
        mrsbossrwas = s;
    }

    public int getdjjl() {
        return djjl;
    }

    public void setdjjl(int s) {
        djjl = s;
    }

    public int getqiandao() {
        return qiandao;
    }

    public void gainqiandao(int gain) {
        this.qiandao += gain;
    }

    public void setqiandao(int set) {
        qiandao = set;
    }

    public int getsg() {//杀怪记录
        return sg;
    }

    public void setsg(int set) {
        this.sg = set;
    }

    public void gainsg(int gain) {
        this.sg += gain;
    }

    public int getFishingJF(int type) {
        DFishingJf one = new QDFishingJf().accname.eq(getClient().getAccountName()).findOne();
        if (one != null) {
            switch (type) {
                case 1:
                    return one.getFishing();
                case 2:
                    return one.getXx();
                case 3:
                    return one.getXxx();
                default:
                    break;
            }
        } else {
            one = new DFishingJf();
            one.setAccname(getClient().getAccountName());
            one.setFishing(0);
            one.setXx(0);
            one.setXxx(0);
            one.save();
        }
        return 0;
    }

    public int gainFishingJF(int hypay) {
        if (hypay <= 0) {
            return 0;
        }
        int jf = getFishingJF(1);
        int XX = getFishingJF(2);
        int XXX = getFishingJF(3);
        DFishingJf one = new QDFishingJf().accname.eq(getClient().getAccountName()).findOne();
        if (one != null) {
            one.setFishing(hypay + jf);
            one.setXx(XX);
            one.setXxx(XXX);
            one.save();
            return 1;
        }
        return 0;
    }

    public int addFishingJF(int hypay) {
        int jf = getFishingJF(1);
        if (hypay > jf) {
            return -1;
        }
        int XX = getFishingJF(2);
        int XXX = getFishingJF(3);
        DFishingJf one = new QDFishingJf().accname.eq(getClient().getAccountName()).findOne();
        if (one != null) {
            one.setFishing(jf - hypay);
            one.setXx(XX);
            one.setXxx(XXX);
            one.save();
            return 1;
        }
        return -1;
    }
}
