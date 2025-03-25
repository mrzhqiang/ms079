package handling;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public enum SendPacketOpcode implements WritableIntValueHolder {

    /**
     * Ping-Pong 0x14
     */
    PING(),
    /**
     * 登录 0x00
     */
    LOGIN_STATUS,
    /**
     * 屏蔽 0xFF
     */
    PIN_OPERATION,
    /**
     * 第二次错误
     */
    SECONDPW_ERROR,
    /**
     * 服务器列表 0x09
     */
    SERVERLIST,
    /**
     * 服务器状态 0x06
     */
    SERVERSTATUS,
    /**
     * 服务器IP 0x0B
     */
    SERVER_IP,
    /**
     * 人物列表 0x0A
     */
    CHARLIST,
    /**
     * 检查人物名反馈 0x0C
     */
    CHAR_NAME_RESPONSE,
    /**
     * 重新记录响应 0x16
     */
    RELOG_RESPONSE,
    /**
     * 增加新人物 0x11
     */
    ADD_NEW_CHAR_ENTRY,
    /**
     * 删除角色 0x7FFE
     */
    DELETE_CHAR_RESPONSE,
    /**
     * 0x18
     */
    CHANNEL_SELECTED,
    /**
     * 0xFF
     */
    ALL_CHARLIST,
    /**
     * 选择性别 0x04
     */
    CHOOSE_GENDER,
    /**
     * 性别选择返回 0x05
     */
    GENDER_SET,
    /**
     * 抵用卷 0x7D
     */
    CHAR_CASH,
    /**
     * 频道更换 0x13
     */
    CHANGE_CHANNEL,
    /**
     * 更新能力值 0x22
     */
    UPDATE_STATS,
    /**
     * 人气反馈 0x29
     */
    FAME_RESPONSE,
    /**
     * 更新技能 0x27
     */
    UPDATE_SKILLS,
    /**
     * 传送到地图 0x81
     */
    WARP_TO_MAP,
    /**
     * 服务器公告 0x41
     */
    SERVERMESSAGE,
    /**
     * 情景喇叭 0x56
     */
    AVATAR_MEGA,
    /**
     * 召唤NPC 0x104
     */
    SPAWN_NPC,
    /**
     * 移除NPC 0x105
     */
    REMOVE_NPC,
    /**
     * 召唤NPC 请求控制权 0x106
     */
    SPAWN_NPC_REQUEST_CONTROLLER,
    /**
     * 怪物召唤 0xEE
     */
    SPAWN_MONSTER,
    /**
     * 怪物召唤控制 0xF0
     */
    SPAWN_MONSTER_CONTROL,
    /**
     * 移动怪物回应 0xF2
     */
    MOVE_MONSTER_RESPONSE,
    /**
     * 聊天信息 0xA4
     */
    CHATTEXT,
    /**
     * 人物具体信息 0x2A
     */
    SHOW_STATUS_INFO,
    /**
     *
     */
    SHOW_MESO_GAIN,
    /**
     * 任务完成效果 0x33
     */
    SHOW_QUEST_COMPLETION,
    /**
     * 悄悄话 0x8B
     */
    WHISPER,
    /**
     * 召唤玩家 0xA2
     */
    SPAWN_PLAYER,
    /**
     *
     */
    ANNOUNCE_PLAYER_SHOP,
    /**
     * 卷轴效果 0xA9
     */
    SHOW_SCROLL_EFFECT,
    /**
     * 增益物品效果显示 0xD0
     */
    SHOW_ITEM_GAIN_INCHAT,
    /**
     * 武陵 SHOW_ITEM_GAIN_INCHAT 1
     * <p>
     * SHOW_ITEM_GAIN_INCHAT 0xD0
     */
    DOJO_WARP_UP,
    /**
     *
     */
    CURRENT_MAP_WARP,
    /**
     * 杀死怪物 0xEF
     */
    KILL_MONSTER,
    /**
     * 掉落物品在地图 0x110
     */
    DROP_ITEM_FROM_MAPOBJECT,
    /**
     * 人物面部表情 0xC3
     */
    FACIAL_EXPRESSION,
    /**
     * 移动玩家 0xBB
     */
    MOVE_PLAYER,
    /**
     * 怪物移动 0xF1
     */
    MOVE_MONSTER,
    /**
     * 近距离攻击 0xBC
     */
    CLOSE_RANGE_ATTACK,
    /**
     * 遠距离攻击 0xBD
     */
    RANGED_ATTACK,
    /**
     * 0xBE
     */
    MAGIC_ATTACK,
    /**
     *
     */
    ENERGY_ATTACK,
    /**
     * 打开NPC商店 0x146
     */
    OPEN_NPC_SHOP,
    /**
     * NPC商店确认 0x147
     */
    CONFIRM_SHOP_TRANSACTION,
    /**
     * 打开仓库 0x14A
     */
    OPEN_STORAGE,
    /**
     * 道具栏信息 0x20
     * 操作码（2） → 固定头（3字节） → 背包类型（1） → 位置（2） → 附加条件数据（可选）
     * ByteBuffer packet = new ByteBuffer();
     * packet.writeShort(MODIFY_INVENTORY_ITEM); // 操作码（2字节）
     * packet.writeByte(optMode);               // 操作模式（1字节）
     * packet.writeByte(optType);               // 操作类型（1字节）
     * packet.writeByte(optAddVer);               // 保留字段或版本标记（1字节）
     * packet.writeByte(inventoryType);         // 背包类型（1字节，如装备栏/消耗栏等）
     * packet.writeShort(position);             // 物品位置（2字节，背包内索引）
     * packet.writeBytes(additionalData);       // 附加数据（长度可变，取决于操作类型）
     * 固定头（示例）	对应操作	附加数据
     * 01 01 03	删除单个物品	无
     * 01 02 03	移动物品	目标位置（2字节）
     * 01 03 03	更新物品数量	新数量（2字节）
     * 01 04 03	装备强化	强化等级+星力（2字节）
     * 固定头组成：操作模式、操作类型、保留字段或版本标记。
     */
    MODIFY_INVENTORY_ITEM,
    /**
     * 移除玩家 0xA3
     */
    REMOVE_PLAYER_FROM_MAP,
    /**
     * 从地图上删除物品 0x111
     */
    REMOVE_ITEM_FROM_MAP,
    /**
     * 更新玩家外观 0xC7
     */
    UPDATE_CHAR_LOOK,
    /**
     * 玩家外观效果 0xC8
     */
    SHOW_FOREIGN_EFFECT,
    /**
     * 获取异常状态 0xC9
     */
    GIVE_FOREIGN_BUFF,
    /**
     * 取消异常状态 0xCA
     */
    CANCEL_FOREIGN_BUFF,
    /**
     * 人物伤害 0xC2
     */
    DAMAGE_PLAYER,
    /**
     * 角色信息 0x3A
     */
    CHAR_INFO,
    /**
     * 更新任务信息 0xD6
     */
    UPDATE_QUEST_INFO,
    /**
     * 获得增益效果状态 0x23
     */
    GIVE_BUFF,
    /**
     * 取消增益效果状态 0x24
     */
    CANCEL_BUFF,
    /**
     * 玩家互动 0x14F
     */
    PLAYER_INTERACTION,
    /**
     * 更新玩家 0xA7
     */
    UPDATE_CHAR_BOX,
    /**
     * NPC交谈 0x145
     */
    NPC_TALK,
    /**
     * 键盘排序 0x16F
     */
    KEYMAP,
    /**
     * 显示怪物HP 0xFC
     */
    SHOW_MONSTER_HP,
    /**
     * 开启组队 0x3B
     */
    PARTY_OPERATION,
    /**
     * 更新组队HP显示 0xCB
     */
    UPDATE_PARTYMEMBER_HP,
    /**
     * 组队家族聊天 0x8A
     */
    MULTICHAT,
    /**
     * 添加怪物状态 0xF4
     */
    APPLY_MONSTER_STATUS,
    /**
     * 取消怪物状态 0xF5
     */
    CANCEL_MONSTER_STATUS,
    /**
     * 时钟 0x96
     */
    CLOCK,
    /**
     * 召唤门 0x40
     */
    SPAWN_PORTAL,
    /**
     * 召唤门 0x117
     */
    SPAWN_DOOR,
    /**
     * 取消门 0x118
     */
    REMOVE_DOOR,
    /**
     * 召唤兽到地图 0xB4
     */
    SPAWN_SUMMON,
    /**
     * 召唤兽从地图删除 0xB5
     */
    REMOVE_SUMMON,
    /**
     * 召唤兽动作 0xB7
     */
    SUMMON_ATTACK,
    /**
     * 召唤兽移动 0xB6
     */
    MOVE_SUMMON,
    /**
     * 召唤烟雾 0x115
     */
    SPAWN_MIST,
    /**
     * 取消烟雾 0x116
     */
    REMOVE_MIST,
    /**
     * 召唤兽伤害 0xB9
     */
    DAMAGE_SUMMON,
    /**
     * 怪物伤害 0xF8
     */
    DAMAGE_MONSTER,
    /**
     * 好友列表 0x3C
     */
    BUDDYLIST,
    /**
     * 显示物品效果 0xC5
     */
    SHOW_ITEM_EFFECT,
    /**
     * 椅子效果 0xC6
     */
    SHOW_CHAIR,
    /**
     * 取消椅子 0xCF
     */
    CANCEL_CHAIR,
    /**
     * 技能效果 0xC0
     */
    SKILL_EFFECT,
    /**
     * 取消技能效果 0xC1
     */
    CANCEL_SKILL_EFFECT,
    /**
     * BOSS血条 0x8D
     */
    BOSS_ENV,
    /**
     * 反应堆召唤 0x11E
     */
    REACTOR_SPAWN,
    /**
     * 反应堆 0x11C
     */
    REACTOR_HIT,
    /**
     * 重置反映堆 0x11F
     */
    REACTOR_DESTROY,
    /**
     * 地图效果 0x91
     */
    MAP_EFFECT,
    /**
     * 家族操作 0x3E
     */
    GUILD_OPERATION,
    /**
     * 家族联盟 0x3F
     */
    ALLIANCE_OPERATION,
    /**
     * 0x74
     */
    BBS_OPERATION,
    /**
     * 学院权限列表 0x6A
     */
    FAMILY,
    /**
     *
     */
    EARN_TITLE_MSG,
    /**
     * 磁铁效果 0xFD
     */
    SHOW_MAGNET,
    /**
     * 领取雇佣物品提示 0x14B
     */
    MERCH_ITEM_MSG,
    /**
     * 领取雇佣物品 0x14C
     */
    MERCH_ITEM_STORE,
    /**
     * 聊天招待 0x14E
     */
    MESSENGER,
    /**
     * NPC说话 0x107
     */
    NPC_ACTION,
    /**
     * 召唤宠物 0xAD
     * 操作码(2) → 角色ID(4) → 宠物栏位置(1) ‌→显隐标记‌(1)→ 宠物唯一ID(4) →‌宠物名称‌(动态长度、UTF-16LE编码 + 双字节终止符‌) → ‌坐标X(2) → ‌坐标Y(2) → 状态标记(1)
     *
     */
    SPAWN_PET,
    /**
     * 宠物移动 0xAF
     */
    MOVE_PET,
    /**
     * 0xB0
     */
    PET_CHAT,
    /**
     * 0xB3
     */
    PET_COMMAND,
    /**
     * 0xB1
     */
    PET_NAMECHANGE,
    /**
     * 增加(取消)宠物技能 0xD8
     */
    PET_FLAG_CHANGE,
    /**
     * 技能冷却 0xEC
     */
    COOLDOWN,
    /**
     * 玩家提示 0xD9
     */
    PLAYER_HINT,
    /**
     * 0xE5
     */
    SUMMON_HINT,
    /**
     *
     */
    SUMMON_HINT_MSG,
    /**
     * 地图动画结束 0xE3
     */
    CYGNUS_INTRO_DISABLE_UI,
    /**
     * 地图动画播放 0xE4
     */
    CYGNUS_INTRO_LOCK,
    /**
     * 使用技能书 0x35
     */
    USE_SKILL_BOOK,
    /**
     *
     */
    SHOW_EQUIP_EFFECT,
    /**
     * 技能宏 0x80
     */
    SKILL_MACRO,
    /**
     * 进入商城 0x83
     */
    CS_OPEN,
    /**
     * 现金商店更新 0x161
     */
    CS_UPDATE,
    /**
     * 现金商店操作 0x162
     */
    CS_OPERATION,
    /**
     * 进入拍卖 0x82
     */
    MTS_OPEN,
    /**
     * 玩家NPC 0x5A
     */
    PLAYER_NPC,
    /**
     * 小纸条 0x2B
     */
    SHOW_NOTES,
    /**
     * 召唤兽技能 0xBA
     */
    SUMMON_SKILL,
    /**
     * 0xFF
     */
    ARIANT_PQ_START,
    /**
     * 抓获怪物 0x103
     */
    CATCH_MONSTER,
    /**
     * 阿里安特系列 0x100
     */
    CATCH_ARIANT,
    /**
     * 阿里安特系列 0x9A
     */
    ARIANT_SCOREBOARD,
    /**
     * 扎昆门 0x144
     */
    ZAKUM_SHRINE,
    /**
     * 船效果 0x98
     */
    BOAT_EFFECT,
    /**
     * 小黑板 0xA6
     */
    CHALKBOARD,
    /**
     * 送货员 0x15F
     */
    DUEY,
    /**
     * 缩地石 0x2C
     */
    TROCK_LOCATIONS,
    /**
     * 怪物嘉年华 0x129
     */
    MONSTER_CARNIVAL_START,
    /**
     * 0x12A
     */
    MONSTER_CARNIVAL_OBTAINED_CP,
    /**
     * 0x12B
     */
    MONSTER_CARNIVAL_PARTY_CP,
    /**
     * 0x12C
     */
    MONSTER_CARNIVAL_SUMMON,
    /**
     * 0x12D
     */
    MONSTER_CARNIVAL_SUMMON1,
    /**
     * 0x12E
     */
    MONSTER_CARNIVAL_DIED,
    /**
     * 召唤雇佣商店 0x10D
     */
    SPAWN_HIRED_MERCHANT,
    /**
     * 0x10F
     */
    UPDATE_HIRED_MERCHANT,
    /**
     * 0x34
     */
    SEND_TITLE_BOX,
    /**
     * 0x10E
     */
    DESTROY_HIRED_MERCHANT,
    /**
     * 更新骑宠 0x32
     */
    UPDATE_MOUNT,
    /**
     * 怪物卡 0x5B
     */
    MONSTERBOOK_ADD,
    /**
     * 怪物卡 0x5C
     */
    MONSTERBOOK_CHANGE_COVER,
    /**
     * 0x60
     */
    FAIRY_PEND_MSG,
    /**
     * 金锤子 0x182
     */
    VICIOUS_HAMMER,
    /**
     * 0x75
     */
    FISHING_BOARD_UPDATE,
    /**
     * 0xAB
     */
    FISHING_CAUGHT,
    /**
     * OX答题 0x94
     */
    OX_QUIZ,
    /**
     * 雪球副本 0x120
     */
    ROLL_SNOWBALL,
    /**
     * 0x121
     */
    HIT_SNOWBALL,
    /**
     * 0x122
     */
    SNOWBALL_MESSAGE,
    /**
     * 0x123
     */
    LEFT_KNOCK_BACK,
    /**
     * 0x36
     */
    FINISH_SORT,
    /**
     * 0x37
     */
    FINISH_GATHER,
    /**
     * 打开学院 0x64
     */
    SEND_PEDIGREE,
    /**
     * 学院信息 0x65
     */
    OPEN_FAMILY,
    /**
     * 学院显示结果 0x66
     */
    FAMILY_MESSAGE,
    /**
     * 学院邀请窗口 0x67
     */
    FAMILY_INVITE,
    /**
     * 学院接受拒绝 0x68
     */
    FAMILY_JUNIOR,
    /**
     * 成为了你的老师 0x69
     */
    SENIOR_MESSAGE,
    /**
     * 名声度 0x6B
     */
    REP_INCREASE,
    /**
     * 学院登录提醒 0x6C
     */
    FAMILY_LOGGEDIN,
    /**
     * 学院buff使用 0x6D
     */
    FAMILY_BUFF,
    /**
     * 学院召唤 0x6E
     */
    FAMILY_USE_REQUEST,
    /**
     * 聊天窗显示黄色字体 0x4E
     */
    YELLOW_CHAT,
    /**
     *
     */
    PIGMI_REWARD,
    /**
     * GM效果 0x93
     */
    GM_EFFECT,
    /**
     * 0x124
     */
    HIT_COCONUT,
    /**
     * 0x125
     */
    COCONUT_SCORE,
    /**
     * 学院等级提升信息 0x6F
     */
    LEVEL_UPDATE,
    /**
     * (家族)结婚提醒 0x70
     */
    MARRIAGE_UPDATE,
    /**
     * (家族)转职提醒 0x71
     */
    JOB_UPDATE,
    /**
     *
     */
    HORNTAIL_SHRINE,
    /**
     * 停止时钟 0x9C
     */
    STOP_CLOCK,
    /**
     *
     */
    MESOBAG_SUCCESS,
    /**
     *
     */
    MESOBAG_FAILURE,
    /**
     *
     */
    SERVER_BLOCKED,
    /**
     *
     */
    DRAGON_MOVE,
    /**
     *
     */
    DRAGON_REMOVE,
    /**
     *
     */
    DRAGON_SPAWN,
    /**
     * 连击效果 0xE7
     */
    ARAN_COMBO,
    /**
     * 0x73
     */
    TOP_MSG,
    /**
     * 临时能力值开始 0x25
     */
    TEMP_STATS,
    /**
     * 临时能力值结束 0x26
     */
    TEMP_STATS_RESET,
    /**
     * 0xE5
     */
    TUTORIAL_SUMMON,
    /**
     * 金字塔 0x9A
     */
    REPAIR_WINDOW,
    /**
     * 0x9D
     */
    PYRAMID_UPDATE,
    /**
     * 0x9E
     */
    PYRAMID_RESULT,
    /**
     *
     */
    ENERGY,
    /**
     *
     */
    GET_MTS_TOKENS,
    /**
     * 拍卖操作 0x17C
     */
    MTS_OPERATION,
    /**
     *
     */
    SHOW_POTENTIAL_EFFECT,
    /**
     *
     */
    SHOW_POTENTIAL_RESET,
    /**
     *
     */
    CHAOS_ZAKUM_SHRINE,
    /**
     *
     */
    CHAOS_HORNTAIL_SHRINE,
    /**
     *
     */
    GAME_POLL_QUESTION,
    /**
     *
     */
    GAME_POLL_REPLY,
    /**
     * GM活动命令 0x95
     */
    GMEVENT_INSTRUCTIONS,
    /**
     *
     */
    BOAT_EFF,
    /**
     * 搜索器 0x45
     */
    OWL_OF_MINERVA,
    /**
     *
     */
    XMAS_SURPRISE,
    /**
     * 音乐盒 0x92
     */
    CASH_SONG,
    /**
     * 0x21
     */
    UPDATE_INVENTORY_SLOT,
    /**
     *
     */
    FOLLOW_REQUEST,
    /**
     *
     */
    FOLLOW_EFFECT,
    /**
     *
     */
    FOLLOW_MOVE,
    /**
     *
     */
    FOLLOW_MSG,
    /**
     *
     */
    FOLLOW_MESSAGE,
    /**
     *
     */
    TALK_MONSTER,
    /**
     *
     */
    REMOVE_TALK_MONSTER,
    /**
     *
     */
    MONSTER_PROPERTIES,
    /**
     * 0x9F
     */
    MOVE_PLATFORM,
    /**
     *
     */
    MOVE_ENV,
    /**
     *
     */
    UPDATE_ENV,
    /**
     * 英语学校 0x45
     */
    ENGAGE_REQUEST,
    /**
     *
     */
    GHOST_POINT,
    /**
     *
     */
    GHOST_STATUS,
    /**
     * 0x46
     */
    ENGAGE_RESULT,
    /**
     * 0x130
     */
    ENGLISH_QUIZ,
    /**
     * 0x132
     */
    ARIANT_SCORE_UPDATE,
    /**
     *
     */
    RPS_GAME,
    /**
     * 更新豆豆 0x15E
     */
    UPDATE_BEANS,
    /**
     * 返回错误信息 0x87
     */
    BLOCK_MSG,
    /**
     * 自动吃药HP 0x170
     */
    AUTO_HP_POT,
    /**
     * 自动吃药MP 0x171
     */
    AUTO_MP_POT,
    /**
     * 许可协议 0x02
     */
    LICENSE_RESULT,
    /**
     * 召唤LOVE 0x113
     */
    SPAWN_LOVE,
    /**
     * 取消LOVE 0x114
     */
    REMOVE_LOVE,
    /**
     * 地图装备效果 0x8F
     */
    FORCED_MAP_EQUIP,
    /**
     * 塔罗牌 0x46
     */
    SHOW_PREDICT_CARD,
    /**
     *
     */
    BEANS_TIPS,
    /**
     * 豆豆机系统 0x15C
     */
    BEANS_GAME1,
    /**
     * 0x15D
     */
    BEANS_GAME2;

    static {
        reloadValues();
    }

    public static void reloadValues() {
        try {
            ExternalCodeTableGetter.populateValues(getDefaultProperties(), values());
        } catch (IOException e) {
            throw new RuntimeException("无法加载 sendops", e);
        }
    }

    private static Properties getDefaultProperties() throws IOException {
        Properties props = new Properties();
        try (InputStream resourceAsStream = SendPacketOpcode.class.getClassLoader().getResourceAsStream("sendops.properties")) {
            props.load(resourceAsStream);
        }
        return props;
    }

    private int code = -2;

    @Override
    public void setValue(int code) {
        this.code = code;
    }

    @Override
    public int getValue() {
        return code;
    }

}
