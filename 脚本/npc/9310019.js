/* global cm */

function start() {
    status = -1;

    action(1, 0, 0);
}
function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (status >= 0 && mode == 0) {

            cm.sendOk("谢谢使用！");
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
            //显示物品ID图片用的代码是  #v这里写入ID#
            text += "#e#d BOSS掉线重返！！#l\r\n\r\n"//3
            text += "#L1##r重返闹钟#l #L2##r重返扎昆#l #L3##r重返黑龙#l #L4##r重返鱼王#l \r\n\r\n"//
            text += "#L10##r查看闹钟BOSS状态#l\r\n\r\n"//
            text += "#L11##r查看扎昆BOSS状态#l\r\n\r\n"//
            text += "#L12##r查看黑龙BOSS状态#l\r\n\r\n"//
            text += "#L13##r查看鱼王BOSS状态#l\r\n\r\n"//
            cm.sendSimple(text);
        } else if (selection == 1) {
            if (cm.getMap(220080001).getCharactersSize() > 0) {
                cm.warp(220080001);//闹钟地图
                cm.dispose();
            } else {
                cm.sendOk("闹钟地图已经没人了。无需重返，直接去挑战吧");
                cm.dispose();
            }
        } else if (selection == 2) {
            if (cm.getMap(280030000).getCharactersSize() > 0) {
                cm.warp(280030000);//扎昆地图
                cm.dispose();
            } else {
                cm.sendOk("扎昆地图已经没人了。无需重返，直接去挑战吧！");
                cm.dispose();
            }
        } else if (selection == 3) {
            if (cm.getMap(240060200).getCharactersSize() > 0) {
                cm.warp(240060200);//黑龙地图
                cm.dispose();
            } else {
                cm.sendOk("黑龙地图已经没人了。无需重返，直接去挑战吧！");
                cm.dispose();
            }

        } else if (selection == 4) {
            if (cm.getMap(230040420).getCharactersSize() > 0) {
                cm.warp(230040420);//鱼王地图
                cm.dispose();
            } else {
                cm.sendOk("鱼王地图已经没人了。无需重返，直接去挑战吧！");
                cm.dispose();
            }
            
            
        } else if (selection == 10) {
            cm.sendOk("闹钟BOSS，当前挑战人数为:"+cm.getMap(220080001).getCharactersSize()+"人.");
            cm.dispose();
        } else if (selection == 11) {
            cm.sendOk("扎昆BOSS，当前挑战人数为:"+cm.getMap(280030000).getCharactersSize()+"人.");
            cm.dispose();
        } else if (selection == 12) {
            cm.sendOk("黑龙BOSS，当前挑战人数为:"+cm.getMap(240060200).getCharactersSize()+"人.");
            cm.dispose();
        } else if (selection == 13) {
            cm.sendOk("鱼王BOSS，当前挑战人数为:"+cm.getMap(230040420).getCharactersSize()+"人.");
            cm.dispose();
            }
        }
    }



