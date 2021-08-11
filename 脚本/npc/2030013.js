/*
	NPC 名字: 		Adobis
	所在地图: 		扎昆的祭台入口
	脚本名字: 		扎昆远征队
*/

var status = 0;

function action(mode, type, selection) {
    if (cm.getPlayer().getMapId() == 211042200) { //艰苦洞穴Ⅲ
        if (selection < 100) {
            cm.sendSimple("#r#L100#扎昆#l\r\n#L101#进阶扎昆#l");
        } else {
            if (selection == 100) {
                cm.warp(211042300, 0); //扎昆入口
            } else if (selection == 101) {
                cm.warp(211042301, 0); //进阶扎昆入口
            }
            cm.dispose();
        }
        return;
    } else if (cm.getPlayer().getMapId() == 211042401) { //进阶扎昆的祭台入口
        switch (status) {
        case 0:
            if (cm.getPlayer().getLevel() < 100) {
                cm.sendOk("你的等级小于 100 级，无法挑战进阶扎昆。");
                cm.dispose();
                return;
            }
            if (cm.getPlayer().getClient().getChannel() != 3) {
                cm.sendOk("进阶扎昆只能在 3 频道挑战。");
                cm.dispose();
                return;
            }
            var em = cm.getEventManager("ChaosZakum");
            if (em == null) {
                cm.sendOk("配置清单为空，请联系管理员。");
                cm.safeDispose();
                return;
            }
            var prop = em.getProperty("state");
            var marr = cm.getQuestRecord(160102);
            var data = marr.getCustomData();
            if (data == null) {
                marr.setCustomData("0");
                data = "0";
            }
            var time = parseInt(data);
            if (prop == null || prop.equals("0")) {
                var squadAvailability = cm.getSquadAvailability("ChaosZak");
                if (squadAvailability == -1) {
                    status = 1;
                    if (time + (12 * 3600000) >= cm.getCurrentTime() && !cm.getPlayer().isGM()) {
                        cm.sendOk("You have already went to Chaos Zakum in the past 12 hours. Time left: " + cm.getReadableMillis(cm.getCurrentTime(), time + (12 * 3600000)));
                        cm.dispose();
                        return;
                    }
                    cm.sendYesNo("现在可以申请远征队，你想成为远征队队长吗？");
                } else if (squadAvailability == 1) {
                    if (time + (12 * 3600000) >= cm.getCurrentTime() && !cm.getPlayer().isGM()) {
                        cm.sendOk("You have already went to Chaos Zakum in the past 12 hours. Time left: " + cm.getReadableMillis(cm.getCurrentTime(), time + (12 * 3600000)));
                        cm.dispose();
                        return;
                    }
                    // -1 = Cancelled, 0 = not, 1 = true
                    var type = cm.isSquadLeader("ChaosZak");
                    if (type == -1) {
                        cm.sendOk("已经结束了申请。");
                        cm.safeDispose();
                    } else if (type == 0) {
                        var memberType = cm.isSquadMember("ChaosZak");
                        if (memberType == 2) {
                            cm.sendOk("在远征队的制裁名单。");
                            cm.safeDispose();
                        } else if (memberType == 1) {
                            status = 5;
                            cm.sendSimple("你现在想做什么？\r\n#b#L0#查看远征队成员。#l \r\n#b#L1#加入远征队。#l \r\n#b#L2#退出远征队。#l");
                        } else if (memberType == -1) {
                            cm.sendOk("远征队员已经达到30名，请稍后再试。");
                            cm.safeDispose();
                        } else {
                            status = 5;
                            cm.sendSimple("你现在想做什么？ \r\n#b#L0#查看远征队成员。#l \r\n#b#L1#加入远征队。#l \r\n#b#L2#退出远征队。#l");
                        }
                    } else { // Is leader
                        status = 10;
                        cm.sendSimple("你现在想做什么？\r\n#b#L0#查看远征队成员。#l \r\n#b#L1#管理远征队成员。#l \r\n#b#L2#编辑限制列表。#l \r\n#r#L3#进入地图。#l");
                        // TODO viewing!
                    }
                } else {
                    var eim = cm.getDisconnected("ChaosZakum");
                    if (eim == null) {
                        var squd = cm.getSquad("ChaosZak");
                        if (squd != null) {
                            if (time + (12 * 3600000) >= cm.getCurrentTime() && !cm.getPlayer().isGM()) {
                                cm.sendOk("You have already went to Chaos Zakum in the past 12 hours. Time left: " + cm.getReadableMillis(cm.getCurrentTime(), time + (12 * 3600000)));
                                cm.dispose();
                                return;
                            }
                            cm.sendYesNo("The squad's battle against the boss has already begun.\r\n" + squd.getNextPlayer());
                            status = 3;
                        } else {
                            cm.sendOk("The squad's battle against the boss has already begun.");
                            cm.safeDispose();
                        }
                    } else {
                        cm.sendYesNo("Ah, you have returned. Would you like to join your squad in the fight again?");
                        status = 2;
                    }
                }
            } else {
                var eim = cm.getDisconnected("ChaosZakum");
                if (eim == null) {
                    var squd = cm.getSquad("ChaosZak");
                    if (squd != null) {
                        if (time + (12 * 3600000) >= cm.getCurrentTime() && !cm.getPlayer().isGM()) {
                            cm.sendOk("You have already went to Chaos Zakum in the past 12 hours. Time left: " + cm.getReadableMillis(cm.getCurrentTime(), time + (12 * 3600000)));
                            cm.dispose();
                            return;
                        }
                        cm.sendYesNo("The squad's battle against the boss has already begun.\r\n" + squd.getNextPlayer());
                        status = 3;
                    } else {
                        cm.sendOk("The squad's battle against the boss has already begun.");
                        cm.safeDispose();
                    }
                } else {
                    cm.sendYesNo("Ah, you have returned. Would you like to join your squad in the fight again?");
                    status = 2;
                }
            }
            break;
        case 1:
            if (mode == 1) {
                if (cm.registerSquad("ChaosZak", 5, " 已经成为了远征队队长。如果你想加入远征队，请重新打开对话申请加入远征队。")) {
                    cm.sendOk("你已经成为了远征队队长。接下来的5分钟，请等待队员们的申请。");
                } else {
                    cm.sendOk("An error has occurred adding your squad.");
                }
            } else {
                cm.sendOk("如果你想申请远征队的话，那么就来找我吧。")
            }
            cm.safeDispose();
            break;
        case 2:
            if (!cm.reAdd("ChaosZakum", "ChaosZak")) {
                cm.sendOk("由于未知的错误，操作失败。");
            }
            cm.dispose();
            break;
        case 3:
            if (mode == 1) {
                var squd = cm.getSquad("ChaosZak");
                if (squd != null && !squd.getAllNextPlayer().contains(cm.getPlayer().getName())) {
                    squd.setNextPlayer(cm.getPlayer().getName());
                    cm.sendOk("You have reserved the spot.");
                }
            }
            cm.dispose();
            break;
        case 5:
            if (selection == 0) {
                if (!cm.getSquadList("ChaosZak", 0)) {
                    cm.sendOk("由于未知的错误，操作失败。");
                    cm.safeDispose();
                } else {
                    cm.dispose();
                }
            } else if (selection == 1) { // join
                var ba = cm.addMember("ChaosZak", true);
                if (ba == 2) {
                    cm.sendOk("远征队员已经达到30名，请稍后再试。");
                    cm.safeDispose();
                } else if (ba == 1) {
                    cm.sendOk("申请加入远征队成功，请等候队长指示。");
                    cm.safeDispose();
                } else {
                    cm.sendOk("你已经参加了远征队，请等候队长指示。");
                    cm.safeDispose();
                }
            } else { // withdraw
                var baa = cm.addMember("ChaosZak", false);
                if (baa == 1) {
                    cm.sendOk("制裁指定的成员成功。");
                    cm.safeDispose();
                } else {
                    cm.sendOk("你没有参加远征队。");
                    cm.safeDispose();
                }
            }
            break;
        case 10:
            if (selection == 0) {
                if (!cm.getSquadList("ChaosZak", 0)) {
                    cm.sendOk("由于未知的错误，操作失败。");
                }
                cm.safeDispose();
            } else if (selection == 1) {
                status = 11;
                if (!cm.getSquadList("ChaosZak", 1)) {
                    cm.sendOk("由于未知的错误，操作失败。");
                    cm.safeDispose();
                }

            } else if (selection == 2) {
                status = 12;
                if (!cm.getSquadList("ChaosZak", 2)) {
                    cm.sendOk("由于未知的错误，操作失败。");
                    cm.safeDispose();
                }

            } else if (selection == 3) { // get insode
                if (cm.getSquad("ChaosZak") != null) {
                    var dd = cm.getEventManager("ChaosZakum");
                    dd.startInstance(cm.getSquad("ChaosZak"), cm.getMap(), 160102);
                    cm.dispose();
                } else {
                    cm.sendOk("由于未知的错误，操作失败。");
                    cm.safeDispose();
                }
            }
            break;
        case 11:
            cm.banMember("ChaosZak", selection);
            cm.dispose();
            break;
        case 12:
            if (selection != -1) {
                cm.acceptMember("ChaosZak", selection);
            }
            cm.dispose();
            break;
        }
    } else {
        switch (status) {
        case 0:
            if (cm.getPlayer().getLevel() < 50) {
                cm.sendOk("你的等级小于 50 级，无法挑战进阶扎昆。");
                cm.dispose();
                return;
            }
            if (cm.getPlayer().getClient().getChannel() != 2) {
                cm.sendOk("扎昆大怪只能在 2 频道召唤。");
                cm.dispose();
                return;
            }
            var em = cm.getEventManager("ZakumBattle");
            if (em == null) {
                cm.sendOk("配置清单为空，请联系管理员。");
                cm.safeDispose();
                return;
            }
            var prop = em.getProperty("state");
            var marr = cm.getQuestRecord(160101);
            var data = marr.getCustomData();
            if (data == null) {
                marr.setCustomData("0");
                data = "0";
            }
            var time = parseInt(data);
            if (prop == null || prop.equals("0")) {
                var squadAvailability = cm.getSquadAvailability("ZAK");
                if (squadAvailability == -1) {
                    status = 1;
                    if (time + (6 * 3600000) >= cm.getCurrentTime() && !cm.getPlayer().isGM()) {
                        cm.sendOk("You have already went to Zakum in the past 6 hours. Time left: " + cm.getReadableMillis(cm.getCurrentTime(), time + (6 * 360000)));
                        cm.dispose();
                        return;
                    }
                    cm.sendYesNo("现在可以申请远征队，你想成为远征队队长吗？");
                } else if (squadAvailability == 1) {
                    if (time + (6 * 3600000) >= cm.getCurrentTime() && !cm.getPlayer().isGM()) {
                        cm.sendOk("You have already went to Zakum in the past 6 hours. Time left: " + cm.getReadableMillis(cm.getCurrentTime(), time + (6 * 360000)));
                        cm.dispose();
                        return;
                    }
                    // -1 = Cancelled, 0 = not, 1 = true
                    var type = cm.isSquadLeader("ZAK");
                    if (type == -1) {
                        cm.sendOk("已经结束了申请。");
                        cm.safeDispose();
                    } else if (type == 0) {
                        var memberType = cm.isSquadMember("ZAK");
                        if (memberType == 2) {
                            cm.sendOk("在远征队的制裁名单。");
                            cm.safeDispose();
                        } else if (memberType == 1) {
                            status = 5;
                            cm.sendSimple("你现在想做什么？\r\n#b#L0#查看远征队成员。#l \r\n#b#L1#加入远征队。#l \r\n#b#L2#退出远征队。#l");
                        } else if (memberType == -1) {
                            cm.sendOk("远征队员已经达到30名，请稍后再试。");
                            cm.safeDispose();
                        } else {
                            status = 5;
                            cm.sendSimple("你现在想做什么？\r\n#b#L0#查看远征队成员。#l \r\n#b#L1#加入远征队。#l \r\n#b#L2#退出远征队。#l");
                        }
                    } else { // Is leader
                        status = 10;
                        cm.sendSimple("你现在想做什么？\r\n#b#L0#查看远征队成员。#l \r\n#b#L1#管理远征队成员。#l \r\n#b#L2#编辑限制列表。#l \r\n#r#L3#进入地图。#l");
                        // TODO viewing!
                    }
                } else {
                    var eim = cm.getDisconnected("ZakumBattle");
                    if (eim == null) {
                        var squd = cm.getSquad("ZAK");
                        if (squd != null) {
                            if (time + (6 * 3600000) >= cm.getCurrentTime() && !cm.getPlayer().isGM()) {
                                cm.sendOk("You have already went to Zakum in the past 6 hours. Time left: " + cm.getReadableMillis(cm.getCurrentTime(), time + (6 * 360000)));
                                cm.dispose();
                                return;
                            }
                            cm.sendYesNo("The squad's battle against the boss has already begun.\r\n" + squd.getNextPlayer());
                            status = 3;
                        } else {
                            cm.sendOk("The squad's battle against the boss has already begun.");
                            cm.safeDispose();
                        }
                    } else {
                        cm.sendYesNo("Ah, you have returned. Would you like to join your squad in the fight again?");
                        status = 1;
                    }
                }
            } else {
                var eim = cm.getDisconnected("ZakumBattle");
                if (eim == null) {
                    var squd = cm.getSquad("ZAK");
                    if (squd != null) {
                        if (time + (6 * 3600000) >= cm.getCurrentTime() && !cm.getPlayer().isGM()) {
                            cm.sendOk("You have already went to Zakum in the past 6 hours. Time left: " + cm.getReadableMillis(cm.getCurrentTime(), time + (6 * 360000)));
                            cm.dispose();
                            return;
                        }
                        cm.sendYesNo("The squad's battle against the boss has already begun.\r\n" + squd.getNextPlayer());
                        status = 3;
                    } else {
                        cm.sendOk("The squad's battle against the boss has already begun.");
                        cm.safeDispose();
                    }
                } else {
                    cm.sendYesNo("Ah, you have returned. Would you like to join your squad in the fight again?");
                    status = 1;
                }
            }
            break;
        case 1:
            if (mode == 1) {
                if (cm.registerSquad("ZAK", 5, " 已经成为了远征队队长。如果你想加入远征队，请重新打开对话申请加入远征队。")) {
                    cm.sendOk("你已经成为了远征队队长。接下来的5分钟，请等待队员们的申请。");
                } else {
                    cm.sendOk("An error has occurred adding your squad.");
                }
            } else {
                cm.sendOk("如果你想申请远征队的话，那么就来找我吧。")
            }
            cm.safeDispose();
            break;
        case 2:
            if (!cm.reAdd("ZakumBattle", "ZAK")) {
                cm.sendOk("由于未知的错误，操作失败。");
            }
            cm.safeDispose();
            break;
        case 3:
            if (mode == 1) {
                var squd = cm.getSquad("ZAK");
                if (squd != null && !squd.getAllNextPlayer().contains(cm.getPlayer().getName())) {
                    squd.setNextPlayer(cm.getPlayer().getName());
                    cm.sendOk("You have reserved the spot.");
                }
            }
            cm.dispose();
            break;
        case 5:
            if (selection == 0) {
                if (!cm.getSquadList("ZAK", 0)) {
                    cm.sendOk("由于未知的错误，操作失败。");
                    cm.safeDispose();
                } else {
                    cm.dispose();
                }
            } else if (selection == 1) { // join
                var ba = cm.addMember("ZAK", true);
                if (ba == 2) {
                    cm.sendOk("远征队员已经达到30名，请稍后再试。");
                    cm.safeDispose();
                } else if (ba == 1) {
                    cm.sendOk("申请加入远征队成功，请等候队长指示。");
                    cm.safeDispose();
                } else {
                    cm.sendOk("你已经参加了远征队，请等候队长指示。");
                    cm.safeDispose();
                }
            } else { // withdraw
                var baa = cm.addMember("ZAK", false);
                if (baa == 1) {
                    cm.sendOk("制裁指定的成员成功。");
                    cm.safeDispose();
                } else {
                    cm.sendOk("你没有参加远征队。");
                    cm.safeDispose();
                }
            }
            break;
        case 10:
            if (selection == 0) {
                if (!cm.getSquadList("ZAK", 0)) {
                    cm.sendOk("由于未知的错误，操作失败。");
                }
                cm.safeDispose();
            } else if (selection == 1) {
                status = 11;
                if (!cm.getSquadList("ZAK", 1)) {
                    cm.sendOk("由于未知的错误，操作失败。");
                    cm.safeDispose();
                }
            } else if (selection == 2) {
                status = 12;
                if (!cm.getSquadList("ZAK", 2)) {
                    cm.sendOk("由于未知的错误，操作失败。");
                    cm.safeDispose();
                }
            } else if (selection == 3) { // get insode
                if (cm.getSquad("ZAK") != null) {
                    var dd = cm.getEventManager("ZakumBattle");
                    dd.startInstance(cm.getSquad("ZAK"), cm.getMap(), 160101);
                    cm.dispose();
                } else {
                    cm.sendOk("由于未知的错误，操作失败。");
                    cm.safeDispose();
                }
            }
            break;
        case 11:
            cm.banMember("ZAK", selection);
            cm.dispose();
            break;
        case 12:
            if (selection != -1) {
                cm.acceptMember("ZAK", selection);
            }
            cm.dispose();
            break;
        }
    }
}