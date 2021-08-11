function enter(pi) {
    pi.playPortalSE();
    pi.warp(pi.getSavedLocation("AMORIA"), 0);
    pi.clearSavedLocation("AMORIA");
}