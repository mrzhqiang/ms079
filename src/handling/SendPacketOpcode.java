package handling;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public enum SendPacketOpcode implements WritableIntValueHolder {
    // GENERAL

    PING(),
    // LOGIN
    LOGIN_STATUS,
    PIN_OPERATION,
    SECONDPW_ERROR,
    SERVERLIST,
    SERVERSTATUS,
    SERVER_IP,
    CHARLIST,
    CHAR_NAME_RESPONSE,
    RELOG_RESPONSE,
    ADD_NEW_CHAR_ENTRY,
    DELETE_CHAR_RESPONSE,
    CHANNEL_SELECTED,
    ALL_CHARLIST,
    CHOOSE_GENDER,
    GENDER_SET,
    CHAR_CASH,
    // CHANNEL
    CHANGE_CHANNEL,
    UPDATE_STATS,
    FAME_RESPONSE,
    UPDATE_SKILLS,
    WARP_TO_MAP,
    SERVERMESSAGE,
    AVATAR_MEGA,
    SPAWN_NPC,
    REMOVE_NPC,
    SPAWN_NPC_REQUEST_CONTROLLER,
    SPAWN_MONSTER,
    SPAWN_MONSTER_CONTROL,
    MOVE_MONSTER_RESPONSE,
    CHATTEXT,
    SHOW_STATUS_INFO,
    SHOW_MESO_GAIN,
    SHOW_QUEST_COMPLETION,
    WHISPER,
    SPAWN_PLAYER,
    ANNOUNCE_PLAYER_SHOP,
    SHOW_SCROLL_EFFECT,
    SHOW_ITEM_GAIN_INCHAT,
    DOJO_WARP_UP,
    CURRENT_MAP_WARP,
    KILL_MONSTER,
    DROP_ITEM_FROM_MAPOBJECT,
    FACIAL_EXPRESSION,
    MOVE_PLAYER,
    MOVE_MONSTER,
    CLOSE_RANGE_ATTACK,
    RANGED_ATTACK,
    MAGIC_ATTACK,
    ENERGY_ATTACK,
    OPEN_NPC_SHOP,
    CONFIRM_SHOP_TRANSACTION,
    OPEN_STORAGE,
    MODIFY_INVENTORY_ITEM,
    REMOVE_PLAYER_FROM_MAP,
    REMOVE_ITEM_FROM_MAP,
    UPDATE_CHAR_LOOK,
    SHOW_FOREIGN_EFFECT,
    GIVE_FOREIGN_BUFF,
    CANCEL_FOREIGN_BUFF,
    DAMAGE_PLAYER,
    CHAR_INFO,
    UPDATE_QUEST_INFO,
    GIVE_BUFF,
    CANCEL_BUFF,
    PLAYER_INTERACTION,
    UPDATE_CHAR_BOX,
    NPC_TALK,
    KEYMAP,
    SHOW_MONSTER_HP,
    PARTY_OPERATION,
    UPDATE_PARTYMEMBER_HP,
    MULTICHAT,
    APPLY_MONSTER_STATUS,
    CANCEL_MONSTER_STATUS,
    CLOCK,
    SPAWN_PORTAL,
    SPAWN_DOOR,
    REMOVE_DOOR,
    SPAWN_SUMMON,
    REMOVE_SUMMON,
    SUMMON_ATTACK,
    MOVE_SUMMON,
    SPAWN_MIST,
    REMOVE_MIST,
    DAMAGE_SUMMON,
    DAMAGE_MONSTER,
    BUDDYLIST,
    SHOW_ITEM_EFFECT,
    SHOW_CHAIR,
    CANCEL_CHAIR,
    SKILL_EFFECT,
    CANCEL_SKILL_EFFECT,
    BOSS_ENV,
    REACTOR_SPAWN,
    REACTOR_HIT,
    REACTOR_DESTROY,
    MAP_EFFECT,
    GUILD_OPERATION,
    ALLIANCE_OPERATION,
    BBS_OPERATION,
    FAMILY,
    EARN_TITLE_MSG,
    SHOW_MAGNET,
    MERCH_ITEM_MSG,
    MERCH_ITEM_STORE,
    MESSENGER,
    NPC_ACTION,
    SPAWN_PET,
    MOVE_PET,
    PET_CHAT,
    PET_COMMAND,
    PET_NAMECHANGE,
    PET_FLAG_CHANGE,
    COOLDOWN,
    PLAYER_HINT,
    SUMMON_HINT,
    SUMMON_HINT_MSG,
    CYGNUS_INTRO_DISABLE_UI,
    CYGNUS_INTRO_LOCK,
    USE_SKILL_BOOK,
    SHOW_EQUIP_EFFECT,
    SKILL_MACRO,
    CS_OPEN,
    CS_UPDATE,
    CS_OPERATION,
    MTS_OPEN,
    PLAYER_NPC,
    SHOW_NOTES,
    SUMMON_SKILL,
    ARIANT_PQ_START,
    CATCH_MONSTER,
    CATCH_ARIANT,
    ARIANT_SCOREBOARD,
    ZAKUM_SHRINE,
    BOAT_EFFECT,
    CHALKBOARD,
    DUEY,
    TROCK_LOCATIONS,
    MONSTER_CARNIVAL_START,
    MONSTER_CARNIVAL_OBTAINED_CP,
    MONSTER_CARNIVAL_PARTY_CP,
    MONSTER_CARNIVAL_SUMMON,
    MONSTER_CARNIVAL_SUMMON1,
    MONSTER_CARNIVAL_DIED,
    SPAWN_HIRED_MERCHANT,
    UPDATE_HIRED_MERCHANT,
    SEND_TITLE_BOX,
    DESTROY_HIRED_MERCHANT,
    UPDATE_MOUNT,
    MONSTERBOOK_ADD,
    MONSTERBOOK_CHANGE_COVER,
    FAIRY_PEND_MSG,
    VICIOUS_HAMMER,
    FISHING_BOARD_UPDATE,
    FISHING_CAUGHT,
    OX_QUIZ,
    ROLL_SNOWBALL,
    HIT_SNOWBALL,
    SNOWBALL_MESSAGE,
    LEFT_KNOCK_BACK,
    FINISH_SORT,
    FINISH_GATHER,
    SEND_PEDIGREE,
    OPEN_FAMILY,
    FAMILY_MESSAGE,
    FAMILY_INVITE,
    FAMILY_JUNIOR,
    SENIOR_MESSAGE,
    REP_INCREASE,
    FAMILY_LOGGEDIN,
    FAMILY_BUFF,
    FAMILY_USE_REQUEST,
    YELLOW_CHAT,
    PIGMI_REWARD,
    GM_EFFECT,
    HIT_COCONUT,
    COCONUT_SCORE,
    LEVEL_UPDATE,
    MARRIAGE_UPDATE,
    JOB_UPDATE,
    HORNTAIL_SHRINE,
    STOP_CLOCK,
    MESOBAG_SUCCESS,
    MESOBAG_FAILURE,
    SERVER_BLOCKED,
    DRAGON_MOVE,
    DRAGON_REMOVE,
    DRAGON_SPAWN,
    ARAN_COMBO,
    TOP_MSG,
    TEMP_STATS,
    TEMP_STATS_RESET,
    TUTORIAL_SUMMON,
    REPAIR_WINDOW,
    PYRAMID_UPDATE,
    PYRAMID_RESULT,
    ENERGY,
    GET_MTS_TOKENS,
    MTS_OPERATION,
    SHOW_POTENTIAL_EFFECT,
    SHOW_POTENTIAL_RESET,
    CHAOS_ZAKUM_SHRINE,
    CHAOS_HORNTAIL_SHRINE,
    GAME_POLL_QUESTION,
    GAME_POLL_REPLY,
    GMEVENT_INSTRUCTIONS,
    BOAT_EFF,
    OWL_OF_MINERVA,
    XMAS_SURPRISE,
    CASH_SONG,
    UPDATE_INVENTORY_SLOT,
    FOLLOW_REQUEST,
    FOLLOW_EFFECT,
    FOLLOW_MOVE,
    FOLLOW_MSG,
    FOLLOW_MESSAGE,
    TALK_MONSTER,
    REMOVE_TALK_MONSTER,
    MONSTER_PROPERTIES,
    MOVE_PLATFORM,
    MOVE_ENV,
    UPDATE_ENV,
    ENGAGE_REQUEST,
    GHOST_POINT,
    GHOST_STATUS,
    ENGAGE_RESULT,
    ENGLISH_QUIZ,
    ARIANT_SCORE_UPDATE,
    RPS_GAME,
    UPDATE_BEANS,
    BLOCK_MSG,
    AUTO_HP_POT,
    AUTO_MP_POT,
    LICENSE_RESULT,
    SPAWN_LOVE,
    REMOVE_LOVE,
    FORCED_MAP_EQUIP,
    SHOW_PREDICT_CARD,
    BEANS_TIPS,
    BEANS_GAME1,
    BEANS_GAME2;
    private short code = -2;

    @Override
    public void setValue(short code) {
        this.code = code;
    }

    @Override
    public short getValue() {
        return code;
    }

    public static Properties getDefaultProperties() throws FileNotFoundException, IOException {
        Properties props = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream("sendops.properties")) {
            props.load(fileInputStream);
        }
        return props;
    }
    //这里是被屏蔽的封包输出，修端时候不要屏蔽这些东西
    /*public static boolean isSpamHeader(SendPacketOpcode opcode) {
        switch (opcode.name()) {
            case "WARP_TO_MAP":
            case "PING":
            case "NPC_ACTION":
            case "UPDATE_STATS":
            case "MOVE_PLAYER":
            case "SPAWN_NPC":
            case "SPAWN_NPC_REQUEST_CONTROLLER":
            case "REMOVE_NPC":
            case "MOVE_MONSTER":
            case "MOVE_MONSTER_RESPONSE":
            case "SPAWN_MONSTER":
            case "SPAWN_MONSTER_CONTROL":
            case "ANDROID_MOVE":
                return true;
            default:
                return false;
        }
    }*/

    public static void reloadValues() {
        boolean leibu = true;
        try {
            if (leibu) {
                Properties props = new Properties();
                props.load(SendPacketOpcode.class.getClassLoader().getResourceAsStream("sendops.properties"));
                ExternalCodeTableGetter.populateValues(props, values());
            } else {
                ExternalCodeTableGetter.populateValues(getDefaultProperties(), values());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load sendops", e);
        }
    }

    static {
        reloadValues();
    }
}
