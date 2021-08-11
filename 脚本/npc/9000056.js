/* global cm, Packages, MapleItemInformationProvider, World, MaplePacketCreator */

importPackage(Packages.tools);
importPackage(Packages.handling.world);
importPackage(Packages.server);

var status = -1;
var selected = 0;
var bag = -1;
var Cash = 0;
var itemId = 0;
var maxtimesperday = 5;
var neededCash = 10;
var s = 0;
var h = 0;

var msg = "";

var edit = false;

var inv = null;
var statsSel = null;

var slot = Array();
var bosslog = ["礼拜日", "礼拜一", "礼拜二", "礼拜三", "礼拜四", "礼拜五", "礼拜六"];

var d = new Date();
var day = d.getDay();

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
        if (cm.getPlayer().getAcLog(bosslog[day] + "_广播") >= maxtimesperday) {
            msg = "一天最多使用次数为#r" + maxtimesperday;
            cm.sendNext(msg);
            cm.dispose();
            return;
        }
        msg = "本NPC可以提供给您广播功能，使用一次为" + neededCash + "点点数(可以选择点数/枫叶点数)\r\n" +
                "请选择所使用的点数：\r\n \r\n#r" +
                "#L1#一般点数\r\n" +
                "#L2#枫叶点数";
        cm.sendSimple(msg);
    } else if (status == 1) {
        if (mode == 1) {
            Cash = selection;
        }
        msg = "请选择要贩卖的道具种类#r\r\n" +
                "#L1#装备栏\r\n" +
                "#L2#消耗栏\r\n" +
                "#L3#装饰栏\r\n" +
                "#L4#其他栏\r\n" +
                "#L5#特殊栏";
        cm.sendSimple(msg);
    } else if (status == 2) {
        msg = "请选择要广播的道具\r\n";
        if (mode == 1) {
            bag = selection;
            inv = cm.getInventory(bag);
        }
        for (var i = 1; i <= inv.getSlotLimit(); i++) {
            slot.push(i);
            var it = inv.getItem(i);
            if (it != null) {
                var itemid = it.getItemId();
                msg += "#L" + i + "##v" + itemid + "##t" + itemid + "##l\r\n";
            }
        }
        cm.sendSimple(msg);
    } else if (status == 3) {
        selected = selection - 1;
        if (selected >= inv.getSlotLimit()) {
            msg = "错误，请稍后再试。";
            cm.sendNext(msg);
            cm.dispose();
            return;
        }
        try {
            statsSel = inv.getItem(slot[selected]);
        } catch (err) {

        }
        if (statsSel == null) {
            msg = "错误，请稍后再试。";
            cm.sendNext(msg);
            cm.dispose();
            return;
        }
        itemId = statsSel.getItemId();
        msg = "请确认以下事项是否正确：\r\n" +
                "\t一、使用点数类型为：" + (Cash == 1 ? "普通点数" : Cash == 2 ? "枫叶点数" : "发生错误") + "\r\n" +
                "\t二、选择道具栏位为：" + (bag == 1 ? "装备" : bag == 2 ? "消耗" : bag == 3 ? "装饰" : bag == 4 ? "其他" : bag == 5 ? "特殊" : "发生错误") + "栏\r\n" +
                "\t三、所选择的道具为：#v" + itemId + "##t" + itemId + "#\r\n";
        cm.sendYesNo(msg);
    } else if (status == 4) {
        // 旧版功能
        // msg = "请输入要广播的讯息";
        // cm.sendGetText(msg);
        msg = "请选择要广播出来的频道数值( 频道-洞数 的频道)\r\n" +
                "#L1#1\r\n" +
                "#L2#2\r\n" +
                "#L3#3\r\n" +
                "#L4#4\r\n" +
                "#L5#5\r\n" +
                "#L6#6\r\n" +
                "#L7#7\r\n" +
                "#L8#8\r\n" +
                "#L9#9\r\n";
        cm.sendSimple(msg);
    } else if (status == 5) {
        s = selection;
        msg = "请选择要广播出来的洞数数值( 频道-洞数 的洞数)\r\n" +
                "#L1#1\r\n" +
                "#L2#2\r\n" +
                "#L3#3\r\n" +
                "#L4#4\r\n" +
                "#L5#5\r\n" +
                "#L6#6\r\n" +
                "#L7#7\r\n" +
                "#L8#8\r\n" +
                "#L9#9\r\n";
        cm.sendSimple(msg);
    } else if (status == 6) {
        h = selection;
        if (cm.getPlayer().getCSPoints(Cash) < neededCash) {
            msg = "您的点数不足。";
            cm.sendNext(msg);
            cm.dispose();
            return;
        }
        cm.getPlayer().setAcLog(bosslog[day]);
        cm.getPlayer().modifyCSPoints(Cash, -neededCash, false);
        // var text = cm.getText();
        var text = "      " + s + " - " + h + " 快来这边找我买吧!";
        World.Broadcast.broadcastMessage(MaplePacketCreator.getGachaponMega(cm.getPlayer().getName(), " : " + text, statsSel, 0).getBytes());
        cm.dispose();
    } else {
        cm.dispose();
    }
}