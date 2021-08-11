/*
 By 梓条
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
        if (mode == 0 && status == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) {
		var Editing = false //true=显示;false=开始活动
          if(Editing){
          cm.sendOk("暂停运作");
          cm.dispose();
          return;
        } 
			cm.sendSimple("#b欢迎玩家 #r#h ##k 兑换#r绵羊单人床#i3010054#" +
            "#k\r\n#L101##r黄金猪#i4032226##bx1000#r换#b绵羊单人床 #i3010054#\r\n");
        } else if (status == 1) {
            
            if (selection == 101) {
                if (cm.haveItem(4032226, 1000) ) {
                    cm.gainItem(4032226, -1000);
                    cm.gainItem(3010054, 1);
                    cm.sendOk("获得#i3010054#");
                    cm.dispose();
                } else {
                    cm.sendOk("您身上没有足够的#i4032226#,请在次确认");
                    cm.dispose();
                }
            }
        }
    }
}

	