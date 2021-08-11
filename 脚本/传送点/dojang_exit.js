function enter(pi) {
    pi.playPortalSE();
    pi.warp(pi.getSavedLocation("MULUNG_TC"), 0);
    pi.clearSavedLocation("MULUNG_TC");
}