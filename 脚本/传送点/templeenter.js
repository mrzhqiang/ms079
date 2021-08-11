/*
 * Portal to get back to leafre
 */

function enter(pi) {
    pi.cancelItem(2210016);
    pi.playPortalSE();
    pi.warp(270000100, "out00");
    return true;
}