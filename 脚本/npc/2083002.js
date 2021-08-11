/*
 Crystal of Roots - Leafre Cave of life
 */

var status = -1;

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (status < 1 && mode == 0) {
            cm.sendOk("好，需要的时候再来找我。");
            cm.dispose();
            return;
        }
        if (mode == 1) {
            status++;
        } else {
            status--;
        }
        if (status == 0) {
            if (cm.getMapId() == 240050400) {
                cm.sendYesNo("你想离开这里到 #m240040700# 吗?");
            } else {
                cm.sendYesNo("你想离开这里到 #m240040700# 吗?");
            }
        } else if (status == 1) {
            if (cm.getMapId() == 240050400) {
                //cm.刷新地图();
                cm.warp(240040700, 0);
                cm.dispose();
            } else {
                cm.刷新地图();
                cm.deleteboss();
                cm.warpParty(240040700);
                cm.dispose();
            }
            cm.dispose();
        }
    }
}