package client.anticheat;

import client.MapleCharacter;
import client.MapleCharacterUtil;
import com.github.mrzhqiang.maplestory.wz.element.data.Vector;
import constants.GameConstants;
import handling.world.World;
import server.AutobanManager;
import com.github.mrzhqiang.maplestory.timer.Timer;
import tools.FileoutputUtil;
import tools.MaplePacketCreator;
import tools.StringUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CheatTracker {

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock rL = lock.readLock(), wL = lock.writeLock();
    private final Map<CheatingOffense, CheatingOffenseEntry> offenses = new LinkedHashMap<>();
    private final WeakReference<MapleCharacter> chr;
    // For keeping track of speed attack hack.
    private int lastAttackTickCount = 0;
    private byte Attack_tickResetCount = 0;
    private long Server_ClientAtkTickDiff = 0;
    private long lastDamage = 0;
    private long takingDamageSince;
    private int numSequentialDamage = 0;
    private long lastDamageTakenTime = 0;
    private byte numZeroDamageTaken = 0;
    private int numSequentialSummonAttack = 0;
    private long summonSummonTime = 0;
    private int numSameDamage = 0;
    private Vector lastMonsterMove;
    private int monsterMoveCount;
    private int attacksWithoutHit = 0;
    private byte dropsPerSecond = 0;
    private long lastDropTime = 0;
    private byte msgsPerSecond = 0;
    private long lastMsgTime = 0;
    private ScheduledFuture<?> invalidationTask;
    private int gm_message = 50;
    private int lastTickCount = 0, tickSame = 0;
    private long lastASmegaTime = 0;
    private long[] lastTime = new long[6];
    private long lastSaveTime = 0L;

    public CheatTracker(MapleCharacter chr) {
        this.chr = new WeakReference<>(chr);
        invalidationTask = Timer.CHEAT.register(new InvalidationTask(), 60000);
        takingDamageSince = System.currentTimeMillis();
    }

    public void checkAttack(int skillId, int tickcount) {
        short AtkDelay = GameConstants.getAttackDelay(skillId);
        if ((tickcount - lastAttackTickCount) < AtkDelay) {
            registerOffense(CheatingOffense.快速攻击);
        }
        long STime_TC = System.currentTimeMillis() - tickcount; // hack = - more
        if (Server_ClientAtkTickDiff - STime_TC > 250) { // 250 is the ping, TODO
            registerOffense(CheatingOffense.快速攻击2);
        }
        // if speed hack, client tickcount values will be running at a faster pace
        // For lagging, it isn't an issue since TIME is running simotaniously, client
        // will be sending values of older time

//	LOGGER.debug("Delay [" + skillId + "] = " + (tickcount - lastAttackTickCount) + ", " + (Server_ClientAtkTickDiff - STime_TC));
        Attack_tickResetCount++; // Without this, the difference will always be at 100
        if (Attack_tickResetCount >= (AtkDelay <= 200 ? 2 : 4)) {
            Attack_tickResetCount = 0;
            Server_ClientAtkTickDiff = STime_TC;
        }
        chr.get().updateTick(tickcount);
        lastAttackTickCount = tickcount;
    }

    public void checkTakeDamage(int damage) {
        numSequentialDamage++;
        lastDamageTakenTime = System.currentTimeMillis();

        // LOGGER.debug("tb" + timeBetweenDamage);
        // LOGGER.debug("ns" + numSequentialDamage);
        // LOGGER.debug(timeBetweenDamage / 1500 + "(" + timeBetweenDamage / numSequentialDamage + ")");
        if (lastDamageTakenTime - takingDamageSince / 500 < numSequentialDamage) {
            registerOffense(CheatingOffense.怪物碰撞过快);
        }
        if (lastDamageTakenTime - takingDamageSince > 4500) {
            takingDamageSince = lastDamageTakenTime;
            numSequentialDamage = 0;
        }
        /*
         * (non-thieves) Min Miss Rate: 2% Max Miss Rate: 80% (thieves) Min Miss
         * Rate: 5% Max Miss Rate: 95%
         */
        if (damage == 0) {
            numZeroDamageTaken++;
            if (numZeroDamageTaken >= 35) { // Num count MSEA a/b players
                numZeroDamageTaken = 0;
                registerOffense(CheatingOffense.回避率过高);
            }
        } else if (damage != -1) {
            numZeroDamageTaken = 0;
        }
    }

    public void checkSameDamage(int dmg) {
        if (dmg > 2000 && lastDamage == dmg) {
            numSameDamage++;

            if (numSameDamage > 5) {
                numSameDamage = 0;
                registerOffense(CheatingOffense.伤害相同, numSameDamage + " times: " + dmg);
            }
        } else {
            lastDamage = dmg;
            numSameDamage = 0;
        }
    }

    public void checkMoveMonster(Vector pos, MapleCharacter chr) {

        //double dis = Math.abs(pos.distance(lastMonsterMove));
        if (pos.equals(this.lastMonsterMove)) {
            monsterMoveCount++;
            if (monsterMoveCount > 50) {
                //   registerOffense(CheatingOffense.MOVE_MONSTERS);
                monsterMoveCount = 0;
                World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(6, "[管理员信息] 开挂玩家[" + MapleCharacterUtil.makeMapleReadable(chr.getName()) + "] 地图ID[" + chr.getMapId() + "] 怀疑使用吸怪! ").getBytes());
                String note = "时间：" + FileoutputUtil.CurrentReadable_Time() + " "
                        + "|| 玩家名字：" + chr.getName() + ""
                        + "|| 玩家地图：" + chr.getMapId() + "\r\n";
                FileoutputUtil.packetLog("日志\\log\\吸怪检测\\" + chr.getName() + ".log", note);
            }
            /*
             * } else if ( dis > 1500 ) { monsterMoveCount++; if
             * (monsterMoveCount > 15) {
             * registerOffense(CheatingOffense.MOVE_MONSTERS); }
             */
        } else {
            lastMonsterMove = pos;
            monsterMoveCount = 1;
        }
    }

    public void resetSummonAttack() {
        summonSummonTime = System.currentTimeMillis();
        numSequentialSummonAttack = 0;
    }

    public boolean checkSummonAttack() {
        numSequentialSummonAttack++;
        //estimated
        // LOGGER.debug(numMPRegens + "/" + allowedRegens);
        if ((System.currentTimeMillis() - summonSummonTime) / (2000 + 1) < numSequentialSummonAttack) {
            registerOffense(CheatingOffense.召唤兽快速攻击);
            return false;
        }
        return true;
    }

    public void checkDrop() {
        checkDrop(false);
    }

    public void checkDrop(final boolean dc) {
        if ((System.currentTimeMillis() - lastDropTime) < 1000) {
            dropsPerSecond++;
            if (dropsPerSecond >= (dc ? 32 : 16) && chr.get() != null) {
//                if (dc) {
//                    chr.get().getClient().getSession().close();
//                } else {
                chr.get().getClient().setMonitored(true);
//                }
            }
        } else {
            dropsPerSecond = 0;
        }
        lastDropTime = System.currentTimeMillis();
    }

    public boolean canAvatarSmega2() {
        if (lastASmegaTime + 10000 > System.currentTimeMillis() && chr.get() != null && !chr.get().isGM()) {
            return false;
        }
        lastASmegaTime = System.currentTimeMillis();
        return true;
    }

    public synchronized boolean GMSpam(int limit, int type) {
        if (type < 0 || lastTime.length < type) {
            type = 1; // default xD
        }
        if (System.currentTimeMillis() < limit + lastTime[type]) {
            return true;
        }
        lastTime[type] = System.currentTimeMillis();
        return false;
    }

    public void checkMsg() { //ALL types of msg. caution with number of  msgsPerSecond
        if ((System.currentTimeMillis() - lastMsgTime) < 1000) { //luckily maplestory has auto-check for too much msging
            msgsPerSecond++;
            /*
             * if (msgsPerSecond > 10 && chr.get() != null) {
             * chr.get().getClient().getSession().close(); }
             */
        } else {
            msgsPerSecond = 0;
        }
        lastMsgTime = System.currentTimeMillis();
    }

    public int getAttacksWithoutHit() {
        return attacksWithoutHit;
    }

    public void setAttacksWithoutHit(boolean increase) {
        if (increase) {
            this.attacksWithoutHit++;
        } else {
            this.attacksWithoutHit = 0;
        }
    }

    public void registerOffense(CheatingOffense offense) {
        registerOffense(offense, null);
    }

    public void registerOffense(CheatingOffense offense, String param) {
        final MapleCharacter chrhardref = chr.get();
        if (chrhardref == null || !offense.isEnabled() || chrhardref.isClone() || chrhardref.isGM()) {
            return;
        }
        CheatingOffenseEntry entry = null;
        rL.lock();
        try {
            entry = offenses.get(offense);
        } finally {
            rL.unlock();
        }
        if (entry != null && entry.isExpired()) {
            expireEntry(entry);
            entry = null;
        }
        if (entry == null) {
            entry = new CheatingOffenseEntry(offense, chrhardref.getId());
        }
        if (param != null) {
            entry.setParam(param);
        }
        entry.incrementCount();
        if (offense.shouldAutoban(entry.getCount())) {
            final byte type = offense.getBanType();
            if (type == 1) {
                AutobanManager.getInstance().autoban(chrhardref.getClient(), StringUtil.makeEnumHumanReadable(offense.name()));
            } else if (type == 2) {
                //怪物碰撞过快 回避率过高 快速攻击 快速攻击2 怪物移动 伤害相同
                String outputFileName = "断线";
                World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(6, "[GM信息] " + chrhardref.getName() + " 自动断线 类别: " + offense.toString() + " 原因: " + (param == null ? "" : (" - " + param))).getBytes());
                FileoutputUtil.logToFile_chr(chrhardref, "日志/logs/Hack/" + outputFileName + ".txt", "\r\n " + FileoutputUtil.NowTime() + " 类别" + offense.toString() + " 原因 " + (param == null ? "" : (" - " + param)));
                chrhardref.getClient().getSession().close();
                //chrhardref.getClient().getSession().close();
            } else if (type == 3) {
            }
            gm_message = 50;
            return;
        }
        wL.lock();
        try {
            offenses.put(offense, entry);
        } finally {
            wL.unlock();
        }
        switch (offense) {

            case 魔法伤害过高:
            case 魔法伤害过高2:
            case 攻击过高2:
            case 攻击力过高:
            case 快速攻击:
            case 快速攻击2:
            case 攻击范围过大:
            case 召唤兽攻击范围过大:
            case 伤害相同:

            case 吸怪:
            case 怪物移动:

            case 回避率过高:
                String show = offense.name();
                gm_message--;
                if (gm_message % 5 == 0) {
                    String msg = "[管理员信息] " + chrhardref.getName() + " 疑似 " + show
                            + "地图ID [" + chrhardref.getMapId() + "]" + ""
                            + (param == null ? "" : (" - " + param));
                    World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(6, msg).getBytes());
                    FileoutputUtil.logToFile_chr(chrhardref, FileoutputUtil.hack_log, show);
                }
                if (gm_message == 0) {

                    // LOGGER.debug(MapleCharacterUtil.makeMapleReadable(chrhardref.getName()) + "怀疑使用外挂");
                    // World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(6, "[管理員訊息] 开挂玩家[" + MapleCharacterUtil.makeMapleReadable(chrhardref.getName()) + "] 地图ID[" + chrhardref.getMapId() + "] suspected of hacking! " + StringUtil.makeEnumHumanReadable(offense.name()) + (param == null ? "" : (" - " + param))).getBytes());
                    /*
                     * String note = "时间：" +
                     * FileoutputUtil.CurrentReadable_Time() + " " + "|| 玩家名字："
                     * + chrhardref.getName() + "" + "|| 玩家地图：" +
                     * chrhardref.getMapId() + "" + "|| 外挂类型：" + offense.name()
                     * + "\r\n"; FileoutputUtil.packetLog("日志\\log\\外挂检测\\" +
                     * chrhardref.getName() + ".log", note);
                     */ //                    AutobanManager.getInstance().autoban(chrhardref.getClient(), StringUtil.makeEnumHumanReadable(offense.name()));
                    gm_message = 50;
                }
                break;
        }
        CheatingOffensePersister.getInstance().persistEntry(entry);
    }

    public void updateTick(int newTick) {
        if (newTick == lastTickCount) { //definitely packet spamming
            /*
             * if (tickSame >= 5) { chr.get().getClient().getSession().close();
             * //i could also add a check for less than, but i'm not too worried
             * at the moment :) } else {
             */
            tickSame++;
//	    }
        } else {
            tickSame = 0;
        }
        lastTickCount = newTick;
    }

    public void expireEntry(final CheatingOffenseEntry coe) {
        wL.lock();
        try {
            offenses.remove(coe.getOffense());
        } finally {
            wL.unlock();
        }
    }

    public int getPoints() {
        int ret = 0;
        CheatingOffenseEntry[] offenses_copy;
        rL.lock();
        try {
            offenses_copy = offenses.values().toArray(new CheatingOffenseEntry[offenses.size()]);
        } finally {
            rL.unlock();
        }
        for (CheatingOffenseEntry entry : offenses_copy) {
            if (entry.isExpired()) {
                expireEntry(entry);
            } else {
                ret += entry.getPoints();
            }
        }
        return ret;
    }

    public Map<CheatingOffense, CheatingOffenseEntry> getOffenses() {
        return Collections.unmodifiableMap(offenses);
    }

    public String getSummary() {
        StringBuilder ret = new StringBuilder();
        List<CheatingOffenseEntry> offenseList = new ArrayList<>();
        rL.lock();
        try {
            for (CheatingOffenseEntry entry : offenses.values()) {
                if (!entry.isExpired()) {
                    offenseList.add(entry);
                }
            }
        } finally {
            rL.unlock();
        }
        offenseList.sort((o1, o2) -> {
            int thisVal = o1.getPoints();
            int anotherVal = o2.getPoints();
            return (Integer.compare(anotherVal, thisVal));
        });
        int to = Math.min(offenseList.size(), 4);
        for (int x = 0; x < to; x++) {
            ret.append(StringUtil.makeEnumHumanReadable(offenseList.get(x).getOffense().name()));
            ret.append(": ");
            ret.append(offenseList.get(x).getCount());
            if (x != to - 1) {
                ret.append(" ");
            }
        }
        return ret.toString();
    }

    public void dispose() {
        if (invalidationTask != null) {
            invalidationTask.cancel(false);
        }
        invalidationTask = null;
    }

    /**
     * 检测是否能保存角色数据 只针对 PLAYER_UPDATE 这个封包 设置3分钟保存 以免频繁的保存数据
     *
     * @return
     */
    public boolean canSaveDB() {
        if (lastSaveTime + 3 * 60 * 1000 > System.currentTimeMillis() && chr.get() != null) {
            return false;
        }
        lastSaveTime = System.currentTimeMillis();
        return true;
    }

    public int getlastSaveTime() {
        if (lastSaveTime <= 0) {
            lastSaveTime = System.currentTimeMillis();
        }
        return (int) (((lastSaveTime + (3 * 60 * 1000)) - System.currentTimeMillis()) / 1000);
    }

    private final class InvalidationTask implements Runnable {

        @Override
        public void run() {
            CheatingOffenseEntry[] offensesCopy;
            rL.lock();
            try {
                offensesCopy = offenses.values().toArray(new CheatingOffenseEntry[0]);
            } finally {
                rL.unlock();
            }
            for (CheatingOffenseEntry offense : offensesCopy) {
                if (offense.isExpired()) {
                    expireEntry(offense);
                }
            }
            if (chr.get() == null) {
                dispose();
            }
        }
    }
}
