function enter(pi) {
    pi.playPortalSE();
    pi.warp(pi.getSavedLocation("RICHIE"), 0);
    pi.clearSavedLocation("RICHIE");
}