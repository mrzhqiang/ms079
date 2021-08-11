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
package client.status;

import java.io.Serializable;
//guai
public enum MonsterStatus implements Serializable {

    物攻(0x100000000L),
    物防(0x200000000L),
    魔攻(0x400000000L),
    魔防(0x800000000L),
    命中(0x1000000000L),
    回避(0x2000000000L),
    速度(0x4000000000L),
    眩晕(0x8000000000L),
    冻结(0x10000000000L), // 凍結
    中毒(0x20000000000L),
    封印(0x40000000000L),
    挑衅(0x80000000000L),
    物攻提升(0x100000000000L),
    物防提升(0x200000000000L),
    魔攻提升(0x400000000000L),
    魔防提升(0x800000000000L),
    巫毒(0x1000000000000L),
    影网(0x2000000000000L),
    免疫物攻(0x4000000000000L),
    免疫魔攻(0x8000000000000L),
    免疫伤害(0x20000000000000L),
    忍者伏击(0x40000000000000L),
    烈焰喷射(0x100000000000000L),
    恐慌(0x200000000000000L),//这里都是15位
    //DARKNESS(0x200000000000000L),//这里都是十五
    模糊领域(0x800000000000000L),
    心灵控制(0x1000000000000000L),
    反射物攻(0x2000000000000000L),
    反射魔攻(0x4000000000000000L),
    SUMMON(0x8000000000000000L), //all summon bag mobs have.*/我怀疑这个是鹦鹉 我们来个小测试就知道了了。找到火凤
    抗压(0x02), // first int on v.87 or else it won't work.
    魔击无效(0x10),;
    static final long serialVersionUID = 0L;
    private final long i;
    private final boolean first;

    private MonsterStatus(long i) {
        this.i = i;
        first = false;
    }

    private MonsterStatus(int i, boolean first) {
        this.i = i;
        this.first = first;
    }

    public boolean isFirst() {
        return first;
    }

    public boolean isEmpty() {
        return this == SUMMON || this == 模糊领域;
    }

    public long getValue() {
        return i;
    }
}
