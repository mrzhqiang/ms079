function start() {
    status = -1;

    action(1, 0, 0);
}
function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (status >= 0 && mode == 0) {

            cm.sendOk("感谢你的光临！");
            cm.dispose();
            return;
        }
        if (mode == 1) {
            status++;
        } else {
            status--;
        }
        if (status == 0) {
            var tex2 = "";
            var text = "";
            for (i = 0; i < 10; i++) {
                text += "";
            }
            text += "#L9##dLv10.英语学院副本#l\r\n\r\n"//3
            text += "#L1##dLv10.月妙组队副本#l\r\n\r\n"//3
            text += "#L700##dLv20.阿里安特副本#l\r\n\r\n"//3
            text += "#L2##dLv21.废弃组队副本#l\r\n\r\n"//3
            text += "#L10##dLv30.怪物嘉年华#l\r\n\r\n"//3
            text += "#L3##dLv35.玩具组队副本#l\r\n\r\n"//3
            text += "#L4##dLv51.天空组队副本#l\r\n\r\n"//3
            text += "#L6##dLv55.海盗组队副本#l\r\n\r\n"//3
            text += "#L5##dLv70.毒物组队副本#l\r\n\r\n"//3
            text += "#L7##dLv100.罗密欧与朱丽叶#l\r\n\r\n"//3
            text += "#L11##dLv120.千年树精王遗迹Ⅱ#l\r\n\r\n"//3
            text += "#L12##dLv130.人偶师BOSS挑战#l\r\n\r\n"//3
            text += "#L8##d遗址公会对抗战#l\r\n\r\n"//3
	    text += "#L13##d二十七宫挑战#l\r\n\r\n"//3
            cm.sendSimple(text);
        } else if (selection == 1) { //月妙组队副本
            cm.warp(100000200);
            cm.dispose();
            //cm.openNpc(1012112, 0);
        } else if (selection == 2) {  //废弃组队副本
            cm.warp(103000000);
            cm.dispose();
            //cm.openNpc(9020000, 0);
        } else if (selection == 3) { //玩具组队副本
            cm.warp(221024500);
            cm.dispose();
            //cm.openNpc(2040034, 0);
        } else if (selection == 4) {//天空组队副本
            cm.warp(200080101);
            cm.dispose();
            //cm.openNpc(2013000, 0);
        } else if (selection == 5) {//毒物组队副本
            cm.warp(300030100);
            cm.dispose();
            //cm.openNpc(2133000, 0);
        } else if (selection == 6) {//海盗组队副本
            cm.warp(251010404);
            cm.dispose();
            //cm.openNpc(2094000, 0);
        } else if (selection == 7) {//罗密欧与朱丽叶组队副本
            cm.warp(261000011);
            cm.dispose();
        } else if (selection == 8) {//遗址公会对抗战
            cm.warp(101030104);
            cm.dispose();
        } else if (selection == 9) {//英语学院副本
            cm.warp(702090400);
            cm.dispose();
            //cm.openNpc(9310057, 0);
        } else if (selection == 11) {//英语学院副本
            cm.warp(541020700);
            cm.dispose();
            //cm.openNpc(9310057, 0);
        } else if (selection == 12) {//英语学院副本
            cm.warp(910510001);
            cm.dispose();
            //cm.openNpc(9310057, 0);
        } else if (selection == 13) {//英语学院副本
            cm.warp(970030020);
            cm.dispose();
            //cm.openNpc(9310057, 0);
        } else if (selection == 10) {//英语学院副本
            cm.warp(980000000);
            cm.dispose();
            //cm.openNpc(9310057, 0);
        } else if (selection == 700) {//阿里
            //cm.warp(980000000);
            cm.openNpc(2101018,0);

        }
    }
}


