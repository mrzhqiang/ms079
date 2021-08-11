var status = 0;
var minLevel = 55;
var maxLevel = 200;
var minPartySize = 2;
var maxPartySize = 6;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0 && status == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
        if (cm.getPlayer().getClient().getChannel() == 1 || cm.getPlayer().getClient().getChannel() == 2 || cm.getPlayer().getClient().getChannel() == 3) {
            if (status == 0) {
                cm.removeAll(4001117);
                cm.removeAll(4031437);
                cm.removeAll(4001120);
                cm.removeAll(4001121);
                cm.removeAll(4001122);
                cm.removeAll(4001260);
                cm.sendSimple("亲爱的#r#h ##k您好，海盗船组队任务:\r\n#r1,2,3线可挑战。#r所属队长与我对话执行。\r\n组队员等级必须要在" + minLevel + "级以上。\r\n#组队员必须要" + minPartySize + "人以上，" + maxPartySize + "人以下。#b\r\n\r\n#L0#海盗船#l");
            } else if (status == 1) {
                if (selection == 0) {
                    if (cm.getParty() == null) { // 没有组队
                        cm.sendOk("请组队后和我谈话。");
                        cm.dispose();
                    } else if (!cm.isLeader()) { // 不是队长
                        cm.sendOk("队长必须在这里。请让他和我说话。");
                        cm.dispose();
                    } else {
                        var party = cm.getParty().getMembers();
                        var mapId = cm.getPlayer().getMapId();
                        var next = true;
                        var levelValid = 0;
                        var inMap = 0;
                        var it = party.iterator();
                        while (it.hasNext()) {
                            var cPlayer = it.next();
                            if ((cPlayer.getLevel() >= minLevel) && (cPlayer.getLevel() <= maxLevel)) {
                                levelValid += 1;
                            } else {
                                next = false;
                            }
                            if (cPlayer.getMapid() == mapId) {
                                inMap += 1;
                            }
                        }
                        if (party.size() < minPartySize || party.size() > maxPartySize || inMap < minPartySize) {
                            next = false;
                        }
                        if (next) {
                            var em = cm.getEventManager("Pirate");
                            if (em == null) {
                                cm.sendOk("此任务正在建设当中。");
                            } else {
                                var prop = em.getProperty("state");
                                if (prop.equals("0") || prop == null) {
                                    em.startInstance(cm.getParty(), cm.getMap());
                                    cm.dispose();
                                    return;
                                } else {
                                    cm.sendOk("[日常]抢占海盗船任务里面已经有人了，请稍等！");
                                }
                            }
                            cm.dispose();
                        } else {
                            cm.sendOk("请确认你的组队员：\r\n\r\n#b1、组队员必须要" + minPartySize + "人以上，" + maxPartySize + "人以下。\r\n2、组队员等级必须要在" + minLevel + "级以上。\r\n\r\n（#r如果仍然错误, 重新下线,再登陆 或者请重新组队。#k#b）");
                            cm.dispose();
                        }
                    } //判断组队
                } else if (selection == 1) {
                    cm.sendOk("请确认你的组队员：\r\n\r\n#b1、组队员必须要" + minPartySize + "人以上，" + maxPartySize + "人以下。\r\n2、组队员等级必须要在" + minLevel + "级以上。\r\n\r\n（#r如果仍然错误, 重新下线,再登陆 或者请重新组队。#k#b）");
                    cm.dispose();
                }
            }
        } else {
            cm.dispose();
            cm.sendOk("只有在1,2,3频道才可以参加海盗船任务。");
        }
    }
}