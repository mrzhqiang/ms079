var status = -1;
var yaoshi = 2;

function start() {
    if (cm.getPlayer().getMapId() == 551030200) {
        cm.sendYesNo("你要离开了吗?");
        status = 1;
        return;
    }
    if (cm.getPlayer().getLevel() < 90) {
        cm.sendOk("你的等级尚未达到90....");
        cm.dispose();
        return;
    } else if (!cm.haveItem(4032246)) {
        cm.sendOk("你没有意念滚吧!");
        cm.dispose();
        return;
    }
    if ( !cm.getPlayer().isGM() &&  cm.getPlayer().getClient().getChannel() != 5 && cm.getPlayer().getClient().getChannel() != 6) {
        cm.sendOk("熊狮只能在5,6频挑战.");
        cm.dispose();
        return;
    }
    if (cm.getBossLog("熊狮王次数") >= yaoshi) {
        cm.sendOk("很抱歉每天只能打两次..");
        cm.dispose();
        return;
    }
    var em = cm.getEventManager("ScarTarBattle");

    if (em == null) {
        cm.sendOk("本活动尚未开放.");
        cm.dispose();
        return;
    }
    var eim_status = em.getProperty("state");
    var marr = cm.getQuestRecord(160108);
    var data = marr.getCustomData();
    if (data == null) {
        marr.setCustomData("0");
        data = "0";
    }
    var time = parseInt(data);
    var dat = parseInt(marr.getCustomData());
    if (eim_status == null || eim_status.equals("0")) {
        var squadAvailability = cm.getSquadAvailability("ScarTar");
        if (squadAvailability == -1) {
            status = 0;
            cm.sendYesNo("现在有人正在挑战当中，您可以先申请远征队排队，你想成为远征队队长吗？");
            if (cm.getBossLog("熊狮王次数") >= yaoshi) {
                cm.sendOk("很抱歉每天只能打两次..");
                cm.dispose();
                return;
            }
        } else if (squadAvailability == 1) {
            // -1 = Cancelled, 0 = not, 1 = true
            var type = cm.isSquadLeader("ScarTar");
            if (type == -1) {
                cm.sendOk("已经结束了申请。");
                cm.dispose();
            } else if (type == 0) {
                var memberType = cm.isSquadMember("ScarTar");
                if (memberType == 2) {
                    cm.sendOk("在远征队的制裁名单。");
                    cm.dispose();
                } else if (memberType == 1) {
                    status = 5;
                    cm.sendSimple("你要做什么? \r\n#b#L0#加入远征队#l \r\n#b#L1#退出远征队#l \r\n#b#L2#查看远征队名单#l");
                } else if (memberType == -1) {
                    cm.sendOk("远征队员已经达到30名，请稍后再试。");
                    cm.dispose();
                } else {
                    status = 5;
                    cm.sendSimple("你要做什么? \r\n#b#L0#加入远征队#l \r\n#b#L1#退出远征队#l \r\n#b#L2#查看远征队名单#l");
                }
            } else { // Is leader
                status = 10;
                cm.sendSimple("你现在想做什么？\r\n#b#L0#查看远征队成员。#l \r\n#b#L1#管理远征队成员。#l \r\n#b#L2#编辑限制列表。#l \r\n#r#L3#进入地图。#l");
                // TODO viewing!
            }
        } else {
            var eim = cm.getDisconnected("ScarTarBattle");
            if (eim == null) {
                var squd = cm.getSquad("ScarTar");
                if (squd != null) {
                    cm.sendYesNo("已经远征队正在进行挑战了.\r\n" + squd.getNextPlayer());
                    status = 3;
                } else {
                    cm.sendOk("远征队的挑战已经开始.");
                    cm.safeDispose();
                }
            } else {
                cm.sendYesNo("你要继续进行远征任务吗?");
                status = 2;
            }
        }
    } else {
        var eim = cm.getDisconnected("ScarTarBattle");
        if (eim == null) {
            var squd = cm.getSquad("ScarTar");
            if (squd != null) {
                cm.sendYesNo("已经远征队正在进行挑战了.\r\n" + squd.getNextPlayer());
                status = 3;
            } else {
                cm.sendOk("远征队的挑战已经开始.");
                cm.safeDispose();
            }
        } else {
            cm.sendYesNo("你要继续进行远征任务吗？");
            status = 2;
        }
    }
}

function action(mode, type, selection) {
    switch (status) {
        case 0:
            if (mode == 1) {
                if (cm.registerSquad("ScarTar", 5, " 已经成为了远征队队长。如果你想加入远征队，请重新打开对话申请加入远征队。")) {
                    cm.sendOk("你已经成为了远征队队长。接下来的5分钟，请等待队员们的申请。");
                } else {
                    cm.sendOk("未知错误.");
                }
            }
            cm.dispose();
            break;
        case 1:
            if (mode == 1) {
                cm.warp(551030100, 0);
            }
            cm.dispose();
            break;
        case 2:
            if (!cm.reAdd("ScarTarBattle", "ScarTar")) {
                cm.sendOk("由于未知的错误，操作失败。");
            }
            cm.safeDispose();
            break;
        case 3:
            if (mode == 1) {
                var squd = cm.getSquad("ScarTar");
                if (squd != null && !squd.getAllNextPlayer().contains(cm.getPlayer().getName())) {
                    squd.setNextPlayer(cm.getPlayer().getName());
                    cm.sendOk("你已经成功登记为下一组..");
                }
            }
            cm.dispose();
            break;
        case 5:
            if (selection == 0) { // join
                var ba = cm.addMember("ScarTar", true);
                if (ba == 2) {
                    cm.sendOk("远征队员已经达到30名，请稍后再试。");
                } else if (ba == 1 && !cm.getPlayer().isGM()) {
                    cm.setBossLog("熊狮王次数");
                    cm.sendOk("申请加入远征队成功，请等候队长指示。");
                } else {
                    cm.sendOk("你已经参加了远征队，请等候队长指示。");
                }
            } else if (selection == 1) {// withdraw
                var baa = cm.addMember("ScarTar", false);
                if (baa == 1) {
                    cm.sendOk("成功退出远征队。");
                } else {
                    cm.sendOk("你没有参加远征队。");
                }
            } else if (selection == 2) {
                if (!cm.getSquadList("ScarTar", 0)) {
                    cm.sendOk("由于未知的错误，操作失败。");
                }
            }
            cm.dispose();
            break;
        case 10:
            if (mode == 1) {
                if (selection == 0) {
                    if (!cm.getSquadList("ScarTar", 0)) {
                        cm.sendOk("由于未知的错误，操作失败。");
                    }
                    cm.dispose();
                } else if (selection == 1) {
                    status = 11;
                    if (!cm.getSquadList("ScarTar", 1)) {
                        cm.sendOk("由于未知的错误，操作失败。");
                        cm.dispose();
                    }
                } else if (selection == 2) {
                    status = 12;
                    if (!cm.getSquadList("ScarTar", 2)) {
                        cm.sendOk("由于未知的错误，操作失败。");
                        cm.dispose();
                    }
                } else if (selection == 3) { // get insode
                    if (cm.getSquad("ScarTar") != null) {
                        var dd = cm.getEventManager("ScarTarBattle");
                        dd.startInstance(cm.getSquad("ScarTar"), cm.getMap(), 160108);
                        cm.setBossLog("熊狮王次数");
                    } else {
                        cm.sendOk("由于未知的错误，操作失败。");
                    }
                    cm.dispose();
                }
            } else {
                cm.dispose();
            }
            break;
        case 11:
            cm.banMember("ScarTar", selection);
            cm.dispose();
            break;
        case 12:
            if (selection != -1) {
                cm.acceptMember("ScarTar", selection);
            }
            cm.dispose();
            break;
    }
}
