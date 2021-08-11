function enter(pi) {
    pi.playPortalSE();
    pi.warp(pi.getSavedLocation("EVENT"), 0);
    pi.clearSavedLocation("EVENT");
}