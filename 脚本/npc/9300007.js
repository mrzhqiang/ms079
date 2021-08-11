/*
 红鸾店 - 守卫兵 天长
 */

var status = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (status >= 0 && mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    } else {
        status--;
    }
    if (status == 0) {
        cm.sendSimple("这里结婚的红鸾宫门口。你想做什么？\r\n#b#L0#我想进去红鸾宫。#l\r\n#L1#请告诉我关于结婚的信息。#l\r\n#L2#我是贺客。我想去宴客堂。#l\r\n#L3#请告诉我关于离婚的说明。#l\r\n#L4#我想进去孤星殿。#l\r\n#L5#我想回家。#l");
    } else if (status == 1) {
        if (selection == 0) { //我想进去红鸾宫
            if (cm.getParty() == null || !cm.isLeader()) {
                cm.sendOk("只有组队长来跟我说才可以允许让你们进去。你们俩中队长来跟我说吧。");
                cm.dispose();
                return;
            } else {
                cm.sendYesNo("哦……你想进去红鸾殿吗？结婚乃人生大事，要进去必须要满足几个条件，如果你想听你要满足的条件。我愿给你说明。");
            }
        } else if (selection == 1) { //请告诉我关于结婚的信息。
            status = 3;
            cm.sendNext("结婚是二个人的事，你想结婚就先要找一个对象吧？你一个人怎么能结婚？");
        } else if (selection == 2) { //我是贺客。我想去宴客堂。
            status = 9;
            cm.sendNext("你要去宴客堂吗？要去宴客堂必须有#v4212002#(1个)，才可以进去。");
        } else if (selection == 3) { //请告诉我关于离婚的说明。
            status = 11;
            cm.sendNext("你想离婚吗？你再想想吧。");
        } else if (selection == 4) { //我想进去孤星殿。
            status = 14;
            cm.sendNext("你想去孤星殿吗？要进去你一定要有结婚戒指和离婚手续费。");
        } else if (selection == 5) { //我想进去孤星殿。
            status = 15;
            cm.sendNext("你想回去吗？你这次下去再次上来的时候还要付费。");
        }
    } else if (status == 2) {
        cm.sendNext("好！我看看你是否满足结婚的条件后，就送你到宫殿里。");
    } else if (status == 3) {
        if (!cm.getParty().getMembers().size() == 2) { //判断组队成员是否达到2人。
            cm.sendNext("组队人员不能超过两个人。不是你们两个人结婚吗？");
            cm.dispose();
        } else if (!cm.isLeader()) { // 不是队长
            cm.sendOk("你想结婚吗？那就请你的组队长和我讲话吧…");
            cm.dispose();
        } else if (cm.getPlayer().getMarriageId() > 0) { //查看玩家是否已经结婚。
            cm.sendNext("你已经结婚了吧… 结婚的话是不能再结婚的。");
            cm.dispose();
        } else if (cm.MarrageChecking() == 3) { //检测组队中是否已经结婚
            cm.sendNext("你的组队中，已经有人结过婚了。\r\n请检查后再试。");
            cm.dispose();
        } else if (cm.allMembersHere() == false) { //检测是否在同1地图
            cm.sendNext("请确保您的伴侣和您在同一地图。");
            cm.dispose();
        } else if (cm.MarrageChecking() == 4) {
            cm.sendNext("我不支持同性结婚。所以不让你们进去");
            cm.dispose();
        } else if (cm.MarrageChecking() == 5) {
            cm.sendNext("男士:#b#b#t1050121##k或#b#b#t1050122##k或#b#b#t1050113##k，女士:#b#t1051129##k或#b#t1051130##k或#b#t1051114##k。其中#b#t1050121##k，#b#t1051129##k，#b#t1050113##k，#b#t1051114##k,这些道具在冒险商城可以购买，#b#t1050122##k和#b#t1051130##k是在那边那位红线女那里卖。\r\n\r\n#b请穿上礼服后再和我对话。");
            cm.dispose();
        } else if (cm.MarrageChecking() == 6) {
            cm.sendNext("组队成员中有人没有结婚戒指。");
            cm.dispose();
        } else {
            var maps = Array(700000100, 700000200, 700000300);
            for (var i = 0; i < maps.length; i++) {
                if (cm.getMap(maps[i]).getCharactersSize() > 0) {
                    cm.sendNext("结婚地图现在有别的玩家正在举行婚礼，请稍后在试。");
                    cm.dispose();
                    return;
                }
            }
            cm.warpParty(700000100);
            cm.dispose();
            //cm.worldMessage(5, "<频道 " + cm.getClient().getChannel() + "> " + cm.getPlayer().getName() + " 和 " + chr.getName() + " 的婚礼即将开始。");
        }
        cm.dispose();
    } else if (status == 4) {
        cm.sendNextPrev("两位来我这里后你们一定要给我能证明两位是真正恋人的标志，就是恋人戒指。同时两位必须戴好恋人戒指，才能进去结婚。");
    } else if (status == 5) {
        cm.sendNextPrev("要结婚还需要满足另外条件，第一个是两位应该组队，组队后请队长来跟我说，就能一起进去。");
    } else if (status == 6) {
        cm.sendNextPrev("第二个条件是你们应该穿好结婚礼服，想要进到神圣的红鸾殿，一定要做好“结婚的准备”");
    } else if (status == 7) {
        cm.sendNextPrev("要穿的衣服是这样。男士:#b#b#t1050121##k或#b#b#t1050122##k或#b#b#t1050113##k，女士:#b#t1051129##k或#b#t1051130##k或#b#t1051114##k。其中#b#t1050121##k，#b#t1051129##k，#b#t1050113##k，#b#t1051114##k,这些道具在冒险商城可以购买，#b#t1050122##k和#b#t1051130##k是在那边那位红线女那里卖。");
    } else if (status == 8) {
        cm.sendNextPrev("另外你要结婚一定要付结婚登记费，要10万金币。这是必须的哦。呵呵。");
    } else if (status == 9) {
        cm.sendNextPrev("这里只能一对一对新人结婚，后面的恋人需要等待。所以你们进去结婚时，请务必在5分钟之内办完所有手续。");
        cm.dispose();
    } else if (status == 10) {
        if (cm.getMap(700000100).getCharactersSize() <= 0 && cm.getMap(700000200).getCharactersSize() <= 0) {
            cm.sendNext("结婚地图现在没有玩家进行结婚，请稍后在试。");
            cm.dispose();
             return;
        }
        if (cm.haveItem(4212002)) {
            cm.gainItem(4212002, -1);
            cm.warp(700000200);
            cm.sendNext("看来你带来了#v4212002#(1个)，我已经将你送到宴客堂。");
            cm.dispose();
        } else {
            cm.sendNext("你好像没有#v4212002#(1个)吧。没有#v4212002#(1个)就进不去");
            cm.dispose();
        }
    } else if (status == 11) {
        cm.sendNextPrev("如果你坚持离婚，进去孤星殿可以进行离婚。在孤星殿有法海师傅，听说他是月下老人的弟弟。");
    } else if (status == 12) {
        cm.sendNextPrev("但是要离婚请在背包放结婚戒指，而且要付更多的钱，因为离婚不是那么容易的事情。离婚费要100万金币。");
    } else if (status == 13) {
        cm.sendNextPrev("付给我100万金币后进去孤星殿，法海师傅会帮你处理离婚事宜。提醒一下离婚后，结婚戒指会同时消失。");
        cm.dispose();
    } else if (status == 14) {
        //to do
    } else if (status == 15) {
        cm.warp(700000101);
        cm.dispose();
        //if (cm.haveItem(1112804)) { //结婚戒指
        //cm.warp(700000101)
        //cm.dispose();
        //} else {
        //cm.sendOk("你好像没有结婚戒指吧。没有戒指就进不去.")
        //cm.dispose();
        //}
    } else if (status == 16) {
        var returnMap = cm.getSavedLocation("MULUNG_TC");
        cm.clearSavedLocation("MULUNG_TC");
        if (returnMap < 0) {
            returnMap = 211000000;
        }
        var target = cm.getMap(returnMap);
        var portal = target.getPortal("unityPortal2");
        if (portal == null) {
            portal = target.getPortal(0);
        }
        if (cm.getMapId() != target) {
            cm.getPlayer().changeMap(target, portal);
        }
        cm.dispose();
    }
}
