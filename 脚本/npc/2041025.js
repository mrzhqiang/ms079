/*
 Machine Apparatus - Origin of Clocktower(220080001)
 */

var status = -1;

function start() {
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        status--;
    }
    if (status == 0) {
        cm.sendYesNo("嘟...嘟...你想要离开吗？？");
    } else if (status == 1) {
        cm.刷新地图();
        cm.deleteboss();
        cm.warpParty(220080000);
        if (cm.getPlayerCount(220080001) == 0) {
            cm.getMap(220080000).resetReactors();
        }
        cm.dispose();
    } else {
        cm.dispose();
    }
}
