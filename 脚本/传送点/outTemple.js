/*
 * Get out of Time Temple
 */

function enter(pi) {
    pi.useItem(2210016);
    pi.playPortalSE();
    pi.warp(200090510, 0);
    return true;
}