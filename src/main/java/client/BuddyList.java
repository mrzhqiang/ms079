package client;

import com.github.mrzhqiang.maplestory.domain.DBuddy;
import com.github.mrzhqiang.maplestory.domain.DCharacter;
import com.github.mrzhqiang.maplestory.domain.query.QDBuddy;
import com.github.mrzhqiang.maplestory.domain.query.QDCharacter;
import tools.MaplePacketCreator;

import java.io.Serializable;
import java.util.*;

public class BuddyList implements Serializable {

    /**
     * 預設的好友群組
     */
    public static final String DEFAULT_GROUP = "其他";

    /**
     * 好友名單操作
     */
    public enum BuddyOperation {

        ADDED, DELETED
    }

    /**
     * 好友名單操作結果
     */
    public enum BuddyAddResult {

        BUDDYLIST_FULL, ALREADY_ON_LIST, OK
    }


    /**
     * 儲存的好友
     */
    private final Map<Integer, BuddyEntry> buddies = new LinkedHashMap<>();

    /**
     * 好友清單的容量
     */
    private byte capacity;

    /**
     * 待處理的好友請求
     */
    private final Deque<BuddyEntry> pendingReqs = new LinkedList<>();

    /**
     * 好友清單建構子
     *
     * @param capacity 好友容量
     */
    public BuddyList(byte capacity) {
        this.capacity = capacity;
    }

    /**
     * 好友清單建構子
     *
     * @param capacity 好友容量
     */
    public BuddyList(int capacity) {
        this.capacity = (byte) capacity;
    }

    public boolean contains(int characterId) {
        return buddies.containsKey(characterId);
    }

    /**
     * 確認有這個好友且是不是在線上
     *
     * @param charId 好友ID
     * @return 是否再現上
     */
    public boolean containsVisible(int charId) {
        BuddyEntry ble = buddies.get(charId);
        if (ble == null) {
            return false;
        }
        return ble.isVisible();
    }

    /**
     * 取得好友清單的容量
     *
     * @return 目前好友清單容量
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * 設定好友清單容量
     *
     * @param newCapacity 新的容量
     */
    public void setCapacity(byte newCapacity) {
        this.capacity = newCapacity;
    }

    /**
     * 由好友ID取得好友
     *
     * @param characterId
     * @return 傳回要找的好友，沒有則null
     */
    public BuddyEntry get(int characterId) {
        return buddies.get(characterId);
    }

    /**
     * 由好友名稱取得好友
     *
     * @param characterName 角色名稱
     * @return 傳回要找的好友，沒有則null
     */
    public BuddyEntry get(String characterName) {
        String searchName = characterName.toLowerCase();
        for (BuddyEntry ble : buddies.values()) {
            if (ble.getName().toLowerCase().equals(searchName)) {
                return ble;
            }
        }
        return null;
    }

    /**
     * 新增好友
     *
     * @param newEntry 新增的好友
     */
    public void put(BuddyEntry newEntry) {
        buddies.put(newEntry.getCharacterId(), newEntry);
    }

    /**
     * 由角色ID從清單中刪除好友
     *
     * @param characterId 角色ID
     */
    public void remove(int characterId) {
        buddies.remove(characterId);
    }

    /**
     * 回傳好友清單
     *
     * @return 好友清單集合
     */
    public Collection<BuddyEntry> getBuddies() {
        return buddies.values();
    }

    /**
     * 取得好友名單是否滿
     *
     * @return 好友名單是否已經滿了
     */
    public boolean isFull() {
        return buddies.size() >= capacity;
    }

    /**
     * 取得所有好友的ID
     *
     * @return 好友清單的ID集合
     */
    public Collection<Integer> getBuddiesIds() {
        return buddies.keySet();
    }

    /**
     * @param data
     */
    public void loadFromTransfer(Map<BuddyEntry, Boolean> data) {
        BuddyEntry buddyid;
        boolean pair;
        for (Map.Entry<BuddyEntry, Boolean> qs : data.entrySet()) {
            buddyid = qs.getKey();
            pair = qs.getValue();
            if (!pair) {
                pendingReqs.push(buddyid);
            } else {
                put(new BuddyEntry(buddyid.character, buddyid.getGroup(), -1, true));
            }
        }
    }

    /**
     * 从数据库读取好友列表。
     */
    public void loadFromDb(DCharacter character) {
        if (character != null) {
            List<DBuddy> buddies = new QDBuddy().owner.eq(character).findList();
            for (DBuddy buddy : buddies) {
                if (buddy.pending == 1) {
                    pendingReqs.push(new BuddyEntry(buddy.owner, buddy.groupName, -1, false));
                } else {
                    put(new BuddyEntry(buddy.owner, buddy.groupName, -1, true));
                }
            }
        }

        // todo what is the means?
        new QDBuddy().pending.eq(1).and().owner.eq(character).delete();
    }

    /**
     * 取得並移除最後的好友請求
     *
     * @return 最後一個好友請求
     */
    public BuddyEntry pollPendingRequest() {
        return pendingReqs.pollLast();
    }

    /**
     * 新增好友請求
     *
     * @param client       欲增加好友的角色客戶端
     * @param buddyChannel 新增的好友頻道
     * @param character    好友信息
     */
    public void addBuddyRequest(MapleClient client, int buddyChannel, DCharacter character) {

        this.put(new BuddyEntry(character, BuddyList.DEFAULT_GROUP, buddyChannel, false));

        if (pendingReqs.isEmpty()) {

            client.sendPacket(MaplePacketCreator.requestBuddylistAdd(character));

        } else {

            BuddyEntry newPair = new BuddyEntry(character, BuddyList.DEFAULT_GROUP, -1, false);
            pendingReqs.push(newPair);

        }
    }

    public static int getBuddyCount(int chrId, int pending) {
        return new QDBuddy().owner.id.eq(chrId).pending.eq(pending).findCount();
    }

    public static int getBuddyCapacity(int charId) {
        return new QDCharacter().id.eq(charId).findOneOrEmpty()
                .map(it -> it.buddyCapacity)
                .orElse(-1);
    }

    public static int getBuddyPending(int chrId, int buddyId) {
        return new QDBuddy().owner.id.eq(chrId).buddies.eq(buddyId).findOneOrEmpty()
                .map(it -> it.pending)
                .orElse(-1);
    }

    public static void addBuddyToDB(MapleCharacter player, BuddyEntry buddy) {
        DBuddy dBuddy = new DBuddy();
        dBuddy.owner = new QDCharacter().id.eq(buddy.getCharacterId()).findOne();
        dBuddy.buddies = player.getId();
        dBuddy.groupName = buddy.getGroup();
        dBuddy.pending = 1;
        dBuddy.save();
    }
}
