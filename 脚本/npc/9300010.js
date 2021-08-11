/* 
	脚本类型: 		NPC
	所在地图:		孤星殿
	脚本名字:		离婚地图离开NPC
*/

var status = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 1) {
            status++;
        } else {
            status--;
        }
        if (status == -1) {
            cm.dispose();
        } else if (status == 0) {
            cm.sendSimple("离婚可能是一件很鲁莽的决定，你可能决定了，我也不多说什么了。#b\r\n#L0# 我想离开这里。");
        } else if (status == 1) {
            cm.warp(700000000);
            cm.dispose();
        }
    }
}
