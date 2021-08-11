/*
 寻找组队发广播npc
 by:Kodan
 */

var status = -1;
var msg = "";
var edit = false;
var pt = 0;
var selected = 0;
var ppl = -1;

function start() {
    if (edit && !cm.getPlayer().isGM()) {
        msg = "本NPC#r维修中#k，请稍后再试。";
        cm.sendNext(msg);
        cm.dispose();
        return;
    }
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else if (mode == 0) {
        status--;
    } else {
        cm.dispose();
        return;
    }

    if (status == 0) {
        msg = "我可以提供给您招收组队人数\r\n" +
                "但需要#r 1枫点#k\r\n" +
                "但需要#r 1枫点#k\r\n" +
                "但需要#r 1枫点#k\r\n" +
                "但需要#r 1枫点#k\r\n" +
                "但需要#r 1枫点#k\r\n" +
                "但需要#r 1枫点#k\r\n" +
                "#L1##r是，我要征收组队#l";
        cm.sendSimple(msg);
    } else if (status == 1) {
        if (mode == 1) {
            pt = selection;
        }
        msg = "你要征收几个人??#b\r\n" +
                "#L1#1\r\n" +
                "#L2#2\r\n" +
                "#L3#3\r\n" +
                "#L4#4\r\n" +
                "#L5#5\r\n";
        cm.sendSimple(msg);
    } else if (status == 2) {
        if (cm.getParty() == null) {
            cm.sendOk("请组队再来找我....");
            cm.dispose();
            return;
        } else if (!cm.isLeader()) {
            cm.sendOk("请叫你的队长来找我!");
            cm.dispose();
            return;
        } else if (cm.getPlayer().getCSPoints(2) < 1) {
            cm.sendOk("#d抱歉，你的枫点不够!!!!!!!！");
            cm.dispose();
            return;
        }
        cm.getPlayer().modifyCSPoints(2, -1, false);
        ppl = selection;
        pqname = getPQMap(cm.getPlayer());
        cm.sendNext("已发出去公告了，请等人来吧。");
        cm.worldMessage("‘组队招募公告<频道: " + cm.getClient().getChannel() + ">’：玩家" + cm.getChar().getName() + " 组队任务:" + pqname + " 缺了:" + ppl + " 人");
        cm.dispose();
    } else {
        cm.dispose();
    }
}

function getPQMap(chr) {
    switch (chr.getMapId()) {
        case 103000000:
            pqname = "超绿";
            break;
        case 221024500:
            pqname = "101";
            break;
        case 300030100:
            pqname = "毒雾";
            break;
        case 200080101:
            pqname = "女神";
            break;
        case 261000011:
        case 261000021:
            pqname = "罗密欧&&茱丽叶";
            break;
        case 251010404:
            pqname = "金勾海贼王";
            break;
        case 910000000:
            pqname = "bspq";
            break;


    }
    return pqname;
}