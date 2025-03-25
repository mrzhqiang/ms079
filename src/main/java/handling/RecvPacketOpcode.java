package handling;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * 冒险岛网络协议
 * | Opcode (2B) | Length (2B) | Payload (N Bytes) |
 */
public enum RecvPacketOpcode implements WritableIntValueHolder {

    /**
     * Ping-Pong 0x13
     */
    PONG(false),

    /****
     *  客户端崩溃时的详细调试信息 0x14
     */
    ERROR_LOG(false),
    /**
     * 登录密码 0x01
     */
    LOGIN_PASSWORD(false),
    HELLO_LOGIN,
    HELLO_CHANNEL,
    /**
     * 许可协议回复 0x03
     */
    LICENSE_REQUEST,
    /**
     * 请求服务器列表 0x02
     */
    SERVERLIST_REQUEST,
    /**
     * 请求人物列表 0x09
     */
    CHARLIST_REQUEST,
    /**
     * 服务器状态 0x05
     */
    SERVERSTATUS_REQUEST,
    /**
     * 检查人物名字 0x0C
     */
    CHECK_CHAR_NAME,
    /**
     * 创建人物 0x11
     */
    CREATE_CHAR,
    /**
     * 删除人物 0x12
     */
    DELETE_CHAR,
    /**
     * 异常数据 0x15
     */
    STRANGE_DATA,
    /**
     * 开始游戏 0x0A
     */
    CHAR_SELECT,
    /**
     * 验证二级密码
     */
    AUTH_SECOND_PASSWORD,
    /**
     * 选择性别 0x04
     */
    SET_GENDER,
    /**
     * 加密秘钥
     */
    RSA_KEY(false),
    /**
     * 频道 0x0B
     */
    PLAYER_LOGGEDIN(false),
    /**
     * 更换地图 0x21
     */
    CHANGE_MAP,
    /**
     * 更换频道 0x22
     */
    CHANGE_CHANNEL,
    /**
     * 进入商城 0x23
     */
    ENTER_CASH_SHOP,
    /**
     * 人物移动 0x24
     */
    MOVE_PLAYER,
    /**
     * 取消椅子 0x25
     */
    CANCEL_CHAIR,
    /**
     * 使用椅子 0x26
     */
    USE_CHAIR,
    /**
     * 近距离攻击 0x28
     */
    CLOSE_RANGE_ATTACK,
    /**
     * 远距离攻击 0x29
     */
    RANGED_ATTACK,
    /**
     * 魔法攻击 0x2A
     */
    MAGIC_ATTACK,
    /**
     * 能量攻击 0x2B
     */
    PASSIVE_ENERGY,
    /**
     * 获取伤害 0x2C
     */
    TAKE_DAMAGE,
    /**
     * 普通聊天 0x2D
     */
    GENERAL_CHAT,
    /**
     * 关闭黑板 0x2E
     */
    CLOSE_CHALKBOARD,
    /**
     * 人物面部表情 0x2F
     */
    FACE_EXPRESSION,
    /**
     * 使用物品效果 0x30
     */
    USE_ITEMEFFECT,
    /**
     * 命运之轮 0x31
     */
    WHEEL_OF_FORTUNE,
    /**
     * 怪物卡片 0x35
     */
    MONSTER_BOOK_COVER,
    /**
     * NPC交谈 0x36
     */
    NPC_TALK,
    /**
     * NPC详细交谈 0x38
     */
    NPC_TALK_MORE,
    /**
     * NPC商店 0x3A
     */
    NPC_SHOP,
    /**
     * 仓库 0x3B
     */
    STORAGE,
    /**
     * 雇佣商店 0x3C
     */
    USE_HIRED_MERCHANT,
    /**
     * 雇佣仓库领取 0x3D
     */
    MERCH_ITEM_STORE,
    /**
     * 送货员 0x3E
     */
    DUEY_ACTION,
    /**
     * 物品整理 0x42
     */
    ITEM_SORT,
    /**
     * 物品排序 0x43
     */
    ITEM_GATHER,
    /**
     * 物品移动 0x44
     */
    ITEM_MOVE,
    /**
     * 使用物品 0x45
     */
    USE_ITEM,
    /**
     * 取消物品结果 0x46
     */
    CANCEL_ITEM_EFFECT,
    /**
     * 使用召唤包 0x48
     */
    USE_SUMMON_BAG,
    /**
     * 宠物排除道具 0xAA
     */
    PET_EXCEPTIONLIST,
    /**
     * 宠物食品 0x49
     */
    PET_FOOD,
    /**
     * 宠物食品 0x4A
     */
    USE_MOUNT_FOOD,
    /**
     * 使用脚本 NPC 物品
     */
    USE_SCRIPTED_NPC_ITEM,
    /**
     * 使用现金物品 0x4C
     */
    USE_CASH_ITEM,
    /**
     * 使用扑捉物品 0x4E
     */
    USE_CATCH_ITEM,
    /**
     * 使用技能书 0x4F
     */
    USE_SKILL_BOOK,
    /**
     * 使用回城卷 0x52
     */
    USE_RETURN_SCROLL,
    /**
     * 使用砸卷 0x53
     */
    USE_UPGRADE_SCROLL,
    /**
     * 分发能力点 0x54
     */
    DISTRIBUTE_AP,
    /**
     * 自动分发能力点 0x55
     */
    AUTO_ASSIGN_AP,
    /**
     * 自动回复HP/MP 0x56
     */
    HEAL_OVER_TIME,
    /**
     * 分发技能点 0x57
     */
    DISTRIBUTE_SP,
    /**
     * 特殊移动 0x58
     */
    SPECIAL_MOVE,
    /**
     * 取消增益效果 0x59
     */
    CANCEL_BUFF,
    /**
     * 技能效果 0x5A
     */
    SKILL_EFFECT,
    /**
     * 金币掉落 0x5B
     */
    MESO_DROP,
    /**
     * 给人气 0x5C
     */
    GIVE_FAME,
    /**
     * 返回人物信息 0x5E
     */
    CHAR_INFO_REQUEST,
    /**
     * 召唤宠物 0x5F
     */
    SPAWN_PET,
    /**
     * 取消负面效果 0x60
     */
    CANCEL_DEBUFF,
    /**
     * 特殊地图移动 0x61
     */
    CHANGE_MAP_SPECIAL,
    /**
     * 使用时空门 0x62
     */
    USE_INNER_PORTAL,
    /**
     * 缩地石 0x63
     */
    TROCK_ADD_MAP,
    /**
     * 任务动作 0x68
     */
    QUEST_ACTION,
    /**
     * 效果开关 0x69
     */
    EFFECT_ON_OFF,
    /**
     * 技能宏 0x6D
     */
    SKILL_MACRO,
    /**
     * 宝物盒 0x70
     */
    ITEM_BAOWU,
    /**
     * 孙子兵法 0x8E
     */
    ITEM_SUNZI,
    /**
     * 锻造技能 0x71
     */
    ITEM_MAKER,
    /**
     * 使用宝箱
     */
    USE_TREASUER_CHEST,
    /**
     * 组队/家族聊天 0x74
     */
    PARTYCHAT,
    /**
     * 玩家搜索？ 0xDF
     */
    PARTY_SS,
    /**
     * 悄悄话 0x75
     */
    WHISPER,
    /**
     * 聊天招待 0x76
     */
    MESSENGER,
    /**
     * 玩家互动 0x77
     */
    PLAYER_INTERACTION,
    /**
     * 开设组队 0x78
     */
    PARTY_OPERATION,
    /**
     * 拒绝组队邀请 0x79
     */
    DENY_PARTY_REQUEST,
    /**
     * 开设家族 0x7A
     */
    GUILD_OPERATION,
    /**
     * 拒绝家族邀请 0x7B
     */
    DENY_GUILD_REQUEST,
    /**
     * 好友操作 0x7E
     */
    BUDDYLIST_MODIFY,
    /**
     * 小纸条 0x7F
     */
    NOTE_ACTION,
    /**
     * 使用门 0x81
     */
    USE_DOOR,
    /**
     * 改变键盘布局 0x83
     */
    CHANGE_KEYMAP,
    /**
     * 更新角色信息
     */
    UPDATE_CHAR_INFO,
    /**
     * 进入拍卖 0x8D
     */
    ENTER_MTS,
    /**
     * 家族联盟 0x8A
     */
    ALLIANCE_OPERATION,
    /**
     * 拒绝家族联盟 0x8B
     */
    DENY_ALLIANCE_REQUEST,
    /**
     * 请求学院 0x95
     */
    REQUEST_FAMILY,
    /**
     * 打开学院 0x96
     */
    OPEN_FAMILY,
    /**
     * 添加学院 0x97
     */
    FAMILY_OPERATION,
    /**
     * 删除初级 0x98
     */
    DELETE_JUNIOR,
    /**
     * 删除高级 0x99
     */
    DELETE_SENIOR,
    /**
     * 接受学院 0x9A
     */
    ACCEPT_FAMILY,
    /**
     * 使用学院 0x9B
     */
    USE_FAMILY,
    /**
     * 学院训练 0x9C
     */
    FAMILY_PRECEPT,
    /**
     * 学院召唤 0x9D
     */
    FAMILY_SUMMON,
    /**
     * XXX 召唤 0x9E
     */
    CYGNUS_SUMMON,
    /**
     * 战神连击点 0x9F
     */
    ARAN_COMBO,
    /**
     * 家族BBS 0x8C
     */
    BBS_OPERATION,
    /**
     * 切换玩家
     */
    TRANSFORM_PLAYER,
    /**
     * 宠物移动 0xA5
     */
    MOVE_PET,
    /**
     * 宠物说话 0xA6
     */
    PET_CHAT,
    /**
     * 宠物命令 0xA7
     */
    PET_COMMAND,
    /**
     * 宠物拣取 0xA8
     */
    PET_LOOT,
    /**
     * 宠物自动吃药 0xA9
     */
    PET_AUTO_POT,
    /**
     * 召唤兽移动 0xAD
     */
    MOVE_SUMMON,
    /**
     * 召唤兽动作 0xAE
     */
    SUMMON_ATTACK,
    /**
     * 召唤兽伤害 0xB0
     */
    DAMAGE_SUMMON,
    /**
     * 怪物移动 0xB7
     */
    MOVE_LIFE,
    /**
     * 自动攻击 0xB8
     */
    AUTO_AGGRO,
    /**
     * 怪物攻击怪物 0xBD
     */
    FRIENDLY_DAMAGE,
    /**
     * 怪物炸弹 0xBC
     */
    MONSTER_BOMB,
    /**
     * 催眠攻击
     */
    HYPNOTIZE_DMG,
    /**
     * NPC说话 0xC0
     */
    NPC_ACTION,
    /**
     * 物品拣起 0xC6
     */
    ITEM_PICKUP,
    /**
     * 伤害反映 0xC9
     */
    DAMAGE_REACTOR,
    /**
     * 雪球 0xCF
     */
    SNOWBALL,
    /**
     * 左击退 0xD0
     */
    LEFT_KNOCK_BACK,
    /**
     * 椰子 0xD1
     */
    COCONUT,
    /**
     * 怪物嘉年华 0xD7
     */
    MONSTER_CARNIVAL,
    /**
     * 组队搜索请求 0xD9
     */
    SHIP_OBJECT,
    /**
     * 商场更新 0x161
     */
    CS_UPDATE,
    /**
     * 购买物品 0xE9
     */
    BUY_CS_ITEM,
    /**
     * 点卷确认 0xE8
     */
    TOUCHING_CS,
    /**
     * 使用兑换券 0xEA
     */
    COUPON_CODE,
    /**
     * 冒险岛TV 0xF6
     */
    MAPLETV,
    /**
     * 移动龙
     */
    MOVE_DRAGON,
    /**
     * 修理
     */
    REPAIR,
    /**
     * 全部修理
     */
    REPAIR_ALL,
    /**
     * 触摸拍卖
     */
    TOUCHING_MTS,
    /**
     * 使用放大镜
     */
    USE_MAGNIFY_GLASS,
    /**
     * 使用潜力卷轴
     */
    USE_POTENTIAL_SCROLL,
    /**
     * 使用装备卷轴
     */
    USE_EQUIP_SCROLL,
    /**
     * 游戏投票
     */
    GAME_POLL,
    /**
     *
     */
    OWL,
    /**
     *
     */
    OWL_WARP,
    /**
     *
     */
    //XMAS_SURPRISE, //header -> uniqueid(long) is entire structure
    USE_OWL_MINERVA,
    /**
     * 角色扮演游戏？ 0x84
     */
    RPS_GAME,
    /**
     * 更新任务？
     */
    UPDATE_QUEST,
    /**
     * 人物数据更新 0xE0
     */
    PLAYER_UPDATE,
    /**
     * 使用任务道具？
     */
    //QUEST_ITEM, //header -> questid(int) -> 1/0(byte, open or close)
    USE_ITEM_QUEST,
    /**
     * 追踪任务？
     */
    FOLLOW_REQUEST,
    /**
     * 追踪回复？
     */
    FOLLOW_REPLY,
    /**
     * 怪物节点？
     */
    MOB_NODE,
    /**
     * 展示节点
     */
    DISPLAY_NODE,
    /**
     * 碰触反映 0xCA
     */
    TOUCH_REACTOR,
    /**
     * 戒指 0x85
     */
    RING_ACTION,
    /**
     * 拍卖标签
     */
    MTS_TAB,
    /**
     * 聊天系统 0x104
     */
    ChatRoom_SYSTEM,
    /**
     * 快捷交任务 0x6A
     */
    quest_KJ,
    /**
     * 未知 0xCB
     */
    NEW_SX,
    /**
     * 小船
     */
    BOATS,
    /**
     * 豆豆机操作 0xE2
     */
    BEANS_GAME1,
    /**
     * 豆豆机操作 0xE3
     */
    BEANS_GAME2,
    /**
     * 月妙扣血 0xBB
     */
    MOONRABBIT_HP,
    /**
     * 婚姻恢复
     */
    MARRAGE_RECV,
    ;

    static {
        reloadValues();
    }

    public static void reloadValues() {
        try {
            ExternalCodeTableGetter.populateValues(getDefaultProperties(), values());
        } catch (IOException e) {
            throw new RuntimeException("无法加载 recvops", e);
        }
    }

    private static Properties getDefaultProperties() throws IOException {
        Properties props = new Properties();
        try (InputStream resourceAsStream = RecvPacketOpcode.class.getClassLoader().getResourceAsStream("recvops.properties")) {
            props.load(resourceAsStream);
        }
        return props;
    }

    private final boolean checkState;
    private int code = -2;

    RecvPacketOpcode() {
        this.checkState = true;
    }

    RecvPacketOpcode(boolean CheckState) {
        this.checkState = CheckState;
    }

    @Override
    public void setValue(int code) {
        this.code = code;
    }

    @Override
    public int getValue() {
        return code;
    }

    public boolean checkState() {
        return checkState;
    }

}
