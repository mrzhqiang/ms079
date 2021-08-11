var baseid = 240040511;
var dungeonid = 240040800;
var dungeons = 19;

function enter(pi) {
    if (pi.getMapId() == baseid) {
        if (pi.getPlayer().getFame() < 10) {
            pi.playerMessage(5, "您需要10點名聲以上。");
            return;
        }
        if (pi.getParty() != null) {
            if (pi.isLeader()) {
                for (var i = 0; i < dungeons; i++) {
                    if (pi.getPlayerCount(dungeonid + i) == 0) {
                        pi.warpParty(dungeonid + i);
                        return;
                    }
                }
            } else {
                pi.playerMessage(5, "你不是隊長。");
            }
        } else {
            for (var i = 0; i < dungeons; i++) {
                if (pi.getPlayerCount(dungeonid + i) == 0) {
                    pi.warp(dungeonid + i);
                    return;
                }
            }
        }
        pi.playerMessage(5, "目前所有地下城都在使用，請稍後在嘗試。");
    } else {
        pi.playPortalSE();
        pi.warp(baseid, "MD00");
    }
}
