/*
 * 
 * @大王
 * @法海老头
 */
function start() {
    status = -1;
    action(1, 0, 0);
}
var 冒险币 = 5000;
function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    }
    else {
        if (status >= 0 && mode == 0) {
            cm.sendOk("你想结婚了的时候再来看看！");
            cm.dispose();
            return;
        }
        if (mode == 1) {
            status++;
        }
        else {
            status--;
        }
        if (status == 0) {
            cm.sendSimple("#r你想结婚了吗？\r\n<结婚分为以下几种类型>\r\n\r\n#d#L0#中式婚礼#l\r\n\r\n");
        } else if (status == 1) {
            if (selection == 0) {//副本传送
            cm.warp(700000000,0);
            } else if (selection == 1) {//副本兑换奖励
              cm.openNpc(1002006,110);
            }else if(selection == 2){
                cm.openNpc(1002006,120);
        }
        }
    }
}


