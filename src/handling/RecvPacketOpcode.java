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
package handling;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public enum RecvPacketOpcode implements WritableIntValueHolder {
    // GENERIC

    PONG(false),
    // LOGIN
    LOGIN_PASSWORD(false),
    HELLO_LOGIN,
    HELLO_CHANNEL,
    LICENSE_REQUEST,
    SERVERLIST_REQUEST,
    CHARLIST_REQUEST,
    SERVERSTATUS_REQUEST,
    CHECK_CHAR_NAME,
    CREATE_CHAR,
    DELETE_CHAR,
    STRANGE_DATA,
    CHAR_SELECT,
    AUTH_SECOND_PASSWORD,
    SET_GENDER,
    RSA_KEY(false),
    // CHANNEL
    PLAYER_LOGGEDIN(false),
    CHANGE_MAP,
    CHANGE_CHANNEL,
    ENTER_CASH_SHOP,
    MOVE_PLAYER,
    CANCEL_CHAIR,
    USE_CHAIR,
    CLOSE_RANGE_ATTACK,
    RANGED_ATTACK,
    MAGIC_ATTACK,
    PASSIVE_ENERGY,
    TAKE_DAMAGE,
    GENERAL_CHAT,
    CLOSE_CHALKBOARD,
    FACE_EXPRESSION,
    USE_ITEMEFFECT,
    WHEEL_OF_FORTUNE,
    MONSTER_BOOK_COVER,
    NPC_TALK,
    NPC_TALK_MORE,
    NPC_SHOP,
    STORAGE,
    USE_HIRED_MERCHANT,
    MERCH_ITEM_STORE,
    DUEY_ACTION,
    ITEM_SORT,
    ITEM_GATHER,
    ITEM_MOVE,
    USE_ITEM,
    CANCEL_ITEM_EFFECT,
    USE_SUMMON_BAG,
    PET_EXCEPTIONLIST,
    PET_FOOD,
    USE_MOUNT_FOOD,
    USE_SCRIPTED_NPC_ITEM,
    USE_CASH_ITEM,
    USE_CATCH_ITEM,
    USE_SKILL_BOOK,
    USE_RETURN_SCROLL,
    USE_UPGRADE_SCROLL,
    DISTRIBUTE_AP,
    AUTO_ASSIGN_AP,
    HEAL_OVER_TIME,
    DISTRIBUTE_SP,
    SPECIAL_MOVE,
    CANCEL_BUFF,
    SKILL_EFFECT,
    MESO_DROP,
    GIVE_FAME,
    CHAR_INFO_REQUEST,
    SPAWN_PET,
    CANCEL_DEBUFF,
    CHANGE_MAP_SPECIAL,
    USE_INNER_PORTAL,
    TROCK_ADD_MAP,
    QUEST_ACTION,
    EFFECT_ON_OFF,
    SKILL_MACRO,
    ITEM_BAOWU,
    ITEM_SUNZI,
    ITEM_MAKER,
    USE_TREASUER_CHEST,
    PARTYCHAT,
    PARTY_SS,
    WHISPER,
    MESSENGER,
    PLAYER_INTERACTION,
    PARTY_OPERATION,
    DENY_PARTY_REQUEST,
    GUILD_OPERATION,
    DENY_GUILD_REQUEST,
    BUDDYLIST_MODIFY,
    NOTE_ACTION,
    USE_DOOR,
    CHANGE_KEYMAP,
    UPDATE_CHAR_INFO,
    ENTER_MTS,
    ALLIANCE_OPERATION,
    DENY_ALLIANCE_REQUEST,
    REQUEST_FAMILY,
    OPEN_FAMILY,
    FAMILY_OPERATION,
    DELETE_JUNIOR,
    DELETE_SENIOR,
    ACCEPT_FAMILY,
    USE_FAMILY,
    FAMILY_PRECEPT,
    FAMILY_SUMMON,
    CYGNUS_SUMMON,
    ARAN_COMBO,
    BBS_OPERATION,
    TRANSFORM_PLAYER,
    MOVE_PET,
    PET_CHAT,
    PET_COMMAND,
    PET_LOOT,
    PET_AUTO_POT,
    MOVE_SUMMON,
    SUMMON_ATTACK,
    DAMAGE_SUMMON,
    MOVE_LIFE,
    AUTO_AGGRO,
    FRIENDLY_DAMAGE,
    MONSTER_BOMB,
    HYPNOTIZE_DMG,
    NPC_ACTION,
    ITEM_PICKUP,
    DAMAGE_REACTOR,
    SNOWBALL,
    LEFT_KNOCK_BACK,
    COCONUT,
    MONSTER_CARNIVAL,
    SHIP_OBJECT,
    CS_UPDATE,
    BUY_CS_ITEM,
    TOUCHING_CS,
    COUPON_CODE,
    MAPLETV,
    MOVE_DRAGON,
    REPAIR,
    REPAIR_ALL,
    TOUCHING_MTS,
    USE_MAGNIFY_GLASS,
    USE_POTENTIAL_SCROLL,
    USE_EQUIP_SCROLL,
    GAME_POLL,
    OWL,
    OWL_WARP,
    //XMAS_SURPRISE, //header -> uniqueid(long) is entire structure
    USE_OWL_MINERVA,
    RPS_GAME,
    UPDATE_QUEST,
    PLAYER_UPDATE,
    //QUEST_ITEM, //header -> questid(int) -> 1/0(byte, open or close)
    USE_ITEM_QUEST,
    FOLLOW_REQUEST,
    FOLLOW_REPLY,
    MOB_NODE,
    DISPLAY_NODE,
    TOUCH_REACTOR,
    RING_ACTION,
    MTS_TAB,
    ChatRoom_SYSTEM,
    quest_KJ,
    NEW_SX,
    BOATS,
    BEANS_GAME1,
    BEANS_GAME2,
    MOONRABBIT_HP,
    MARRAGE_RECV;
    private short code = -2;

    @Override
    public void setValue(short code) {
        this.code = code;
    }

    @Override
    public final short getValue() {
        return code;
    }
    private boolean CheckState;

    private RecvPacketOpcode() {
        this.CheckState = true;
    }

    private RecvPacketOpcode(final boolean CheckState) {
        this.CheckState = CheckState;
    }

    public final boolean NeedsChecking() {
        return CheckState;
    }

    public static Properties getDefaultProperties() throws FileNotFoundException, IOException {
        Properties props = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream("recvops.properties")) {
            props.load(fileInputStream);
        }
        return props;
    }

    /*public static boolean isSpamHeader(RecvPacketOpcode header) {
        switch (header.name()) {
            case "PONG":
            case "NPC_ACTION":
            case "MOVE_LIFE":
            case "MOVE_PLAYER":
            case "MOVE_ANDROID":
            case "MOVE_SUMMON":
            case "AUTO_AGGRO":
            case "HEAL_OVER_TIME":
            case "BUTTON_PRESSED":
            case "STRANGE_DATA":
            case "TAKE_DAMAGE":
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
                props.load(RecvPacketOpcode.class.getClassLoader().getResourceAsStream("recvops.properties"));
                ExternalCodeTableGetter.populateValues(props, values());
            } else {
                ExternalCodeTableGetter.populateValues(getDefaultProperties(), values());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load recvops", e);
        }
    }

    static {
        reloadValues();
    }
}
