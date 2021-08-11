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
			cm.sendSimple("#b欢迎玩家 #r#h ##k ,欢迎参与端午节活动,下面是兑换物品奖励噢！#b兑换前请确认背包格子,否则后果自负！！" +
            "#k\r\n#L101##r粽子#i2022034##bx1#r换#b屈原变身药 #i 2210015#只能兑换一次\r\n#L102##r粽子#i2022034##bx3#r换#b火红玫瑰#i5121009#x1\r\n#L103##r粽子#i2022034##bx15#r换#b盾牌攻击卷60%#i2040914#x1\r\n#L104##r粽子#i2022034##bx20#r换#b工作人员随机卷100%#i2049104#x1\r\n#L105##r粽子#i2022034##bx30#r换#b盾牌攻击卷30%#i2040917#x1\r\n#L106##r粽子#i2022034##bx100#r换#b#v1142005##z1142005#x1");
        } else if (status == 1) {
            
            if (selection == 101) {
                if (cm.haveItem(2022034, 1) ) {
                    cm.gainItem(2022034, -1);
                    cm.gainItem(2210015, 1);
					cm.gainItem(4031456,1)
                    cm.sendOk("获得#i2210015##i4031456#");
                    cm.dispose();
                } else {
                    cm.sendOk("您身上没有足够的#i2210015#,请在次确认");
                    cm.dispose();
                }
            } else if (selection == 102) {
                if (cm.haveItem(2022034, 3) ) {
                    cm.gainItem(2022034, -3);
                    cm.gainItem(5121009, 1);
					cm.gainItem(4031456, 3);
                    cm.sendOk("获得#i5121009#x1#i4031456#x3");
                    cm.dispose();
                } else {
                    cm.sendOk("您身上没有#i2022034#,请在次确认");
                    cm.dispose();
				}
			 }else if (selection == 103) {
                if (cm.haveItem(2022034, 15) ) {
                    cm.gainItem(2022034, -15);
                    cm.gainItem(2040914, 1);
					cm.gainItem(4031456, 15);
                    cm.sendOk("获得#i2040914#x1#i4031456#x15");
                    cm.dispose();
                } else {
                    cm.sendOk("您身上没有#i4032226#,请在次确认");
                    cm.dispose();
				}
			 }else if (selection == 104) {
                if (cm.haveItem(2022034, 20) ) {
                    cm.gainItem(2022034, -20);
                    cm.gainItem(2049104, 1);
					cm.gainItem(4031456,20)
                    cm.sendOk("获得#i2049104#x1#i4031456#x20");
                    cm.dispose();
                } else {
                    cm.sendOk("您身上没有#i2022034#,请在次确认");
                    cm.dispose();
				}
			 }else if (selection == 105) {
                if (cm.haveItem(2022034, 30) ) {
                    cm.gainItem(2022034, -30);
                    cm.gainItem(2040917, 1);
					cm.gainItem(4031456,30)
                    cm.sendOk("获得#i2040917#x1#i4031456#x30");
                    cm.dispose();
                } else {
                    cm.sendOk("您身上没有#i2022034#,请在次确认");
                    cm.dispose();
				}
			 }else if (selection == 106) {
                if (cm.haveItem(2022034, 100) ) {
                    cm.gainItem(2022034, -100);
                    cm.gainItem(1142005, 1);
					cm.gainItem(4031456,100)
                    cm.sendOk("获得#i1142005#x1#i4031456#x100");
                    cm.dispose();
                } else {
                    cm.sendOk("您身上没有#i2022034#,请在次确认");
                    cm.dispose();
				}
			 }else if (selection == 107) {
                if (cm.haveItem(4032226, 1) ) {
                    cm.gainItem(4032226, -1);
                    cm.gainItem(2022488, 1);
                    cm.sendOk("获得#i2022488#x1");
                    cm.dispose();
                } else {
                    cm.sendOk("您身上没有#i4032226#,请在次确认");
                    cm.dispose();
				}
			 }else if (selection == 108) {
                if (cm.haveItem(4032226, 20) ) {
                    cm.gainItem(4032226, -20);
                    cm.gainItem(2022489, 1);
                    cm.sendOk("获得#i2022489#x1");
                    cm.dispose();
                } else {
                    cm.sendOk("您身上没有#i4032226#,请在次确认");
                    cm.dispose();
				}
			 }else if (selection == 109) {
                if (cm.haveItem(4032226, 20) ) {
                    cm.gainItem(4032226, -20);
                    cm.gainItem(2022490, 1);
                    cm.sendOk("获得#i2022490#x1");
                    cm.dispose();
                } else {
                    cm.sendOk("您身上没有#i4032226#,请在次确认");
                    cm.dispose();
				}
			 }
        }
    }
}

	