var status = -1;

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        if (status == 0) {
            cm.dispose();
        }
        status--;
    }
    cm.removeAll(4031595);
    cm.removeAll(4031594);
    cm.removeAll(4031597);
    if (status == 0) {
        cm.sendSimple("#b#L0#离开这里 (没有退费)#l\r\n#L1#我的队伍已经准备好，我们要挑战了！#l#k");
    } else if (status == 1) {
        if (selection == 0) {
            cm.warp(670010000, 0);
        } else {
            if (cm.getPlayer().getParty() == null || !cm.isLeader()) {
                cm.sendOk("必须要有队长在这里。");
            } else {
                var party = cm.getPlayer().getParty().getMembers();
                var mapId = cm.getPlayer().getMapId();
                var next = true;
                var size = 0;
                var it = party.iterator();
                while (it.hasNext()) {
                    var cPlayer = it.next();
                    var ccPlayer = cm.getPlayer().getMap().getCharacterById(cPlayer.getId());
                    if (ccPlayer == null || ccPlayer.getLevel() < 40) {
                        next = false;
                        break;
                    }
                    size += (ccPlayer.isGM() ? 6 : 1);
                }
                if (next && size >= 6) {
                    var em = cm.getEventManager("Amoria");
                    if (em == null) {
                        cm.sendOk("请稍后再尝试。");
                    } else {
                        em.startInstance(cm.getPlayer().getParty(), cm.getPlayer().getMap());
                    }
                } else {
                    cm.sendOk("队伍成员必须有6个以上都在这里。");
                }
            }
        }
        cm.dispose();
    }
}
