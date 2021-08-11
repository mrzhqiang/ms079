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
package client.anticheat;

public enum CheatingOffense {

    召唤兽快速攻击((byte) 5, 6000, 10, (byte) 1),// FAST_SUMMON_ATTACK
    快速攻击((byte) 5, 6000, 50, (byte) 2),//FASTATTACK
    快速攻击2((byte) 5, 9000, 20, (byte) 2),//FASTATTACK2
    怪物移动((byte) 1, 30000, 20, (byte) 2),//MOVE_MONSTERS
    伤害相同((byte) 2, 30000, 150, (byte) 2),//SAME_DAMAGE
    人物无敌((byte) 1, 30000, 1200, (byte) 0),//ATTACK_WITHOUT_GETTING_HIT
    魔法伤害过高((byte) 5, 30000, -1, (byte) 0),//HIGH_DAMAGE_MAGIC
    魔法伤害过高2((byte) 10, 30000, -1, (byte) 0),//HIGH_DAMAGE_MAGIC_2
    攻击力过高((byte) 5, 30000, -1, (byte) 0),//HIGH_DAMAGE
    怪物碰撞过快((byte) 1, 60000L, 100, (byte) 2),//FAST_TAKE_DAMAGE
    攻击过高2((byte) 10, 30000, -1, (byte) 0),//HIGH_DAMAGE_2
    攻击范围过大((byte) 5, 60000, 1500), // ATTACK_FARAWAY_MONSTER
    召唤兽攻击范围过大((byte) 5, 60000, 200),//ATTACK_FARAWAY_MONSTER_SUMMON
    回复过多HP((byte) 1, 30000, 1000, (byte) 0),//REGEN_HIGH_HP
    回复过多MP((byte) 1, 30000, 1000, (byte) 0),//REGEN_HIGH_MP
    全图吸物_客户端((byte) 5, 5000, 10),//ITEMVAC_CLIENT
    全图吸物_服务端((byte) 3, 5000, 100),//ITEMVAC_SERVER
    宠物全图吸物_客户端((byte) 5, 10000, 20),//PET_ITEMVAC_CLIENT
    宠物全图吸物_服务端((byte) 3, 10000, 100, (byte) 0),//PET_ITEMVAC_SERVER
    使用过远传送点((byte) 1, 60000, 100, (byte) 0),//USING_FARAWAY_PORTAL
    回避率过高((byte) 20, 180000, 100, (byte) 2),//HIGH_AVOID
    其他异常((byte) 1, 300000),//ETC_EXPLOSION
    人物死亡攻击((byte) 1, 300000, -1, (byte) 0),//ATTACKING_WHILE_DEAD
    使用不存在道具((byte) 1, 300000),//USING_UNAVAILABLE_ITEM
    添加自己声望((byte) 1, 1000, 1), //FAMING_SELF
    声望十五级以下添加((byte) 1, 1000, 1),//FAMING_UNDER_15
    金钱炸弹_不存在道具((byte) 1, 300000),//EXPLODING_NONEXISTANT
    召唤兽攻击怪物数量异常((byte) 1, 10000, 3),//SUMMON_HACK_MOBS
    治愈术攻击非不死系怪物((byte) 20, 10000, 3),//HEAL_ATTACKING_UNDEAD
    吸怪((byte) 1, 7000, 5);
    private final byte points;
    private final long validityDuration;
    private final int autobancount;
    private byte bantype = 0; // 0 = Disabled, 1 = Enabled, 2 = DC

    public final byte getPoints() {
        return points;
    }

    public final long getValidityDuration() {
        return validityDuration;
    }

    public final boolean shouldAutoban(final int count) {
        if (autobancount == -1) {
            return false;
        }
        return count >= autobancount;
    }

    public final byte getBanType() {
        return bantype;
    }

    public final void setEnabled(final boolean enabled) {
        bantype = (byte) (enabled ? 1 : 0);
    }

    public final boolean isEnabled() {
        return bantype >= 1;
    }

    private CheatingOffense(final byte points, final long validityDuration) {
        this(points, validityDuration, -1, (byte) 1);
    }

    private CheatingOffense(final byte points, final long validityDuration, final int autobancount) {
        this(points, validityDuration, autobancount, (byte) 1);
    }

    private CheatingOffense(final byte points, final long validityDuration, final int autobancount, final byte bantype) {
        this.points = points;
        this.validityDuration = validityDuration;
        this.autobancount = autobancount;
        this.bantype = bantype;
    }
}
