function enter(pi) {
    var em = pi.getEventManager("HorntailBattle");
    if (em != null) {
        var map = pi.getMapId();
        if (map == 240060000) {
            if (em.getProperty("state").equals("2")) {
                em.warpAllPlayer(240060000, 240060100);
            } else {
                pi.playerMessage("這個門還沒開起。");
            }
        } else if (map == 240060100) {
            if (em.getProperty("state").equals("3")) {
                em.warpAllPlayer(240060100, 240060200);
            } else {
                pi.playerMessage("暗黑龍王吼了一聲，你必須殺死暗黑龍王的左頭顱，才能進入下一關。.");
            }
        }
    }
}