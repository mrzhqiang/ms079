/*
	This file is part of the OdinMS Maple Story Server
    Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc>
		       Matthias Butz <matze@odinms.de>
		       Jan Christian Meyer <vimes@odinms.de>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation version 3 as published by
    the Free Software Foundation. You may not use, modify or distribute
    this program under any other version of the GNU Affero General Public
    License.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
var status = 0;
var qChars = new Array ("Q1: 枫之谷中，从等级1到等级2需要多少经验值？#10#12#15#20#3",
    "Q1: 根据不同职业为了第1次转职所要求的能力，被不正确叙述的是哪一个？#战士 35 力量#飞侠 20 幸运#法师 20 智力#弓箭手 25 敏捷#2",
    "Q1: 被怪物攻击时特别的异常状态没有被正确说明的是哪一个？#虚弱 — 移动速度降低#封印 - 不能使用技能#黑暗 - 命中下降#诅咒 - 减少经验#1",
    "Q1: 根据不同职业的第1次转职必须条件 敏捷25 正确的是哪一个？#战士#弓箭手#法师#飞侠#2");
var qItems = new Array( "Q2: 下列怪物中，哪组怪物与打倒它所能得到的战利品是正确对应关系的？#大幽灵-幽灵头带#蝙蝠 — 蝙蝠翅膀#煤泥 - 粘糊糊的泡泡#猪 - 丝带#2",
    "Q2: 下列怪物中，哪组怪物与打倒它所能得到的战利品是不正确对应关系的？#缎带肥肥- 蝴蝶结#煤泥 - 煤泥泡沫#绿色蜗牛 - 绿色蜗牛壳#食人花——食人花的叶子#4",
    "Q2: 冒险岛中下列药品中，哪组药品与功效是正确对应关系的？#白色药水 - 回复 250 HP#超级药水 — HP400恢复#?红色药水 - 回复 100 HP#披萨 — HP400恢复#4",
    "Q2: 冒险岛中下列药品中，哪组药水可以回复HP50%MP50%？#特殊药水#超级药水#大西瓜#矿泉水#1",
    "Q2: 冒险岛中下列药品中，哪组药品与功效是不正确对应关系的？#蓝色药水 - 回复 100 MP#活力药水 - 回复 300 MP#清晨之露——3000MP恢复#红色药水 - 回复 50 HP#3");
var qMobs = new Array(  "Q3: 绿蘑菇、蓝水灵、斧木妖、三眼章鱼，哪个是等级最高的怪物？#绿蘑菇#三眼章鱼#蓝水灵#斧木妖#4",
    "Q3: 明珠港没有哪个怪物？#小石球#蜗牛#蓝蜗牛#蘑菇仔#1",
    "Q3: 去天空之城的船上会出现哪个怪物？#扎昆#蝙蝠魔#小石球#海龙王#2",
    "Q3: 在冰封雪域没有哪个怪物？#野狼#雪人#小雪球#黑鳄鱼#4",
    "Q3: 会飞的怪物是什么？#巫婆#天线宝宝#小雪球#小老鼠#1",
    "Q3: 你觉得你是猪吗?#我不是#我是#周杰论是#回答的才是猪#4",
    "Q3: ?#1/1#12/30#11/12#2/12#2");
var qQuests = new Array("Q4: 如果遇上骗子怎么办#被他骗#打110#举报给GM#告诉我妈妈#3",
    "Q4: 在这里抵用券能免费获取吗?#能#不能#不记得了#好像可以#1",
    "Q4: GM人怎么样?#很好#不好#很恶心#讨厌她#1",
    "Q4: 本服叫什么名称?#彩虹岛#冒险岛#娃娃岛#鬼岛#2",
    "Q4: 冒险岛是哪个公司代理的#腾讯#盛大#世纪天成#任天堂#2",
    "Q4: 本服经验倍数是多少?#1#10#20#30#1");
var qTowns = new Array( "Q5: 在我们这里 一个IP 可以创几个帐号#1个#3个#4个#5个#1",
    "Q5: 如果开外挂被发现的处理方式是??#锁7天#锁180天#永久封锁#不处理#3",
    "Q5: 扎昆是在哪里被召唤#扎昆祭坛#玩具城#天空之城#2水下世界#1",
    "Q5: 绿蜗牛的战利品为?#绳子#眼珠#绿蜗牛壳#煤炭#3",
    "Q5: 多少级可以进行三转?#30#40#60#70#4",
    "Q5: 本服的版本为?#Ver117#Ver72#Ver62#Ver79#4");
var correctAnswer = 0;

function start() {
	if (cm.haveItem(4031058, 1)) {
		cm.sendOk("#h #,你已经有了 #t4031058# 不要让废我时间.");
		cm.dispose();
	}
    if (!(cm.haveItem(4031058, 1))) {
        cm.sendNext("欢迎光临 #h #, 我是 #p2030006#.\r\n看来你已经走了很远到达了这个阶段.");
    }
}

function action(mode, type, selection) {
    if (mode == -1)
        cm.dispose();
    else {
        if (mode == 0) {
            cm.sendOk("下次再见.");
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 1)
            cm.sendNextPrev("#h #, 如果你给我 #b黑暗水晶#k 我将会让你试着回答5个问题,若您5个问题都答对您将得到 #v4031058# #b智慧项链#k.");
        else if (status == 2) {
            if (!cm.haveItem(4005004)) {
                cm.sendOk("#h #, 你没有 #b黑暗水晶#k");
                cm.dispose();
            } else {
                cm.gainItem(4005004, -1);
                cm.sendSimple("测验开始 #b接受挑战吧!#k.\r\n\r\n" + getQuestion(qChars[Math.floor(Math.random() * qChars.length)]));
                status = 2;
            }
        } else if (status == 3) {
            if (selection == correctAnswer)
                cm.sendOk("#h # 你答对了.\n准备答下一题??");
            else {
                cm.sendOk("你答错了的答案!.\r\n很抱歉你必须在给我一个 #b黑暗水晶#k 才可以再挑战!");
                cm.dispose();
            }
        } else if (status == 4)
            cm.sendSimple("测验开始 #b接受挑战吧!#k.\r\n\r\n" + getQuestion(qItems[Math.floor(Math.random() * qItems.length)]));
        else if (status == 5) {
            if (selection == correctAnswer)
                cm.sendOk("#h # 你答对了.\n准备答下一题??");
            else {
                cm.sendOk("你答错了的答案!.\r\n很抱歉你必须在给我一个 #b黑暗水晶#k 才可以再挑战!");
                cm.dispose();
            }
        } else if (status == 6) {
            cm.sendSimple("测验开始 #b接受挑战吧!#k.\r\n\r\n" + getQuestion(qMobs[Math.floor(Math.random() * qMobs.length)]));
            status = 6;
        } else if (status == 7) {
            if (selection == correctAnswer)
                cm.sendOk("#h # 你答对了.\n准备答下一题??");
            else {
                cm.sendOk("你答错了的答案!.\r\n很抱歉你必须在给我一个 #b黑暗水晶#k 才可以再挑战!");
                cm.dispose();
            }
        } else if (status == 8)
            cm.sendSimple("测验开始 #b接受挑战吧!#k.\r\n\r\n" + getQuestion(qQuests[Math.floor(Math.random() * qQuests.length)]));
        else if (status == 9) {
            if (selection == correctAnswer) {
                cm.sendOk("#h # 你答对了.\n准备答下一题??");
                status = 9;
            } else {
                cm.sendOk("你答错了的答案!.\r\n很抱歉你必须在给我一个 #b黑暗水晶#k 才可以再挑战!");
                cm.dispose();
            }
        } else if (status == 10) {
            cm.sendSimple("最后一个问题.\r\n测验开始 #b接受挑战吧!#k.\r\n\r\n" + getQuestion(qTowns[Math.floor(Math.random() * qTowns.length)]));
            status = 10;
        } else if (status == 11) {
            if (selection == correctAnswer) {
                cm.gainItem(4031058, 1);
				cm.warp(211000001, 0);
                cm.sendOk("恭喜 #h #, 你太强大了.\r\n拿着这个 #v4031058# 去找你的转职教官吧!.");
                cm.dispose();
            } else {
                cm.sendOk("太可惜了,差一题就可以通关了!! 多多加油><.\r\n很抱歉你必须在给我一个 #b黑暗水晶#k 才可以再挑战!");
                cm.dispose();
            }
        }
    }
}
function getQuestion(qSet){
    var q = qSet.split("#");
    var qLine = q[0] + "\r\n\r\n#L0#" + q[1] + "#l\r\n#L1#" + q[2] + "#l\r\n#L2#" + q[3] + "#l\r\n#L3#" + q[4] + "#l";
    correctAnswer = parseInt(q[5],10);
    correctAnswer--;
    return qLine;
}