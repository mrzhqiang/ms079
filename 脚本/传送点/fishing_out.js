function enter(pi) {
    pi.playPortalSE();
    pi.warp(pi.getSavedLocation("FISHING"), 0);
    pi.clearSavedLocation("FISHING");
}