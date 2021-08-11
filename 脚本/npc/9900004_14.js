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
			cm.sendSimple("#b欢迎你 #r#h ##k ,这里是带新人信件兑换专区，等级达到120级即可开始，最低带2个新人完成副本最后关领取奖励后会自动获得信件喔，每个副本条件不同\r\n\r\n月杪副本：最低带2个新人-新人等级要求（10级—40级）\r\n\r\n废弃副本：最低带2个新人-新人等级要求（21级—60级）\r\n\r\n玩具副本：最低带2个新人-新人等级要求（35级—70级）\r\n\r\n天空副本：最低带2个新人-新人等级要求（51—100）\r\n\r\n海盗副本：最低带2个新人-新人等级要求（55—100）\r\n\r\n#r注：只有队长领奖或者进入传送门才有信件不要想着当混子！\r\n\r\n#b兑换前请确认背包格子,否则后果自负！！" +           
 

"#k\r\n#L101##r信件#i4032327##bx10#r兑换#b混沌卷轴#i2049100#x1\r\n#L102##r信件#i4032327##bx20#r兑换#b祝福卷轴#i2340000#x2\r\n#L103##r信件#i4032327##bx40#r兑换 #k4000点卷x1\r\n#L104##r信件#i4032327##bx60#r兑换#b天玑#i4031568#x1\r\n#L105##r信件#i4032327##bx100#r兑换#b#v1102489##z1102489#x1\r\n");
        } else if (status == 1) {
            
            if (selection == 101) {
                if (cm.haveItem(4032327, 10) ) {
                    cm.gainItem(4032327, -10);
                    cm.gainItem(2049100, 1);
                    cm.sendOk("恭喜您获得#i2049100#，感谢你的勤劳付出。");
                    cm.dispose();
                } else {
                    cm.sendOk("您身上没有10个#i4032327#,请在次确认");
                    cm.dispose();
                }
            } else if (selection == 102) {
                if (cm.haveItem(4032327, 20) ) {
                    cm.gainItem(4032327, -20);
                    cm.gainItem(2340000, 2);
                    cm.sendOk("恭喜您获得#i2340000#x2，感谢你你勤劳付出。");
                    cm.dispose();
                } else {
                    cm.sendOk("您身上没有20个#i4032327#,请在次确认");
                    cm.dispose();
				}
			 }else if (selection == 103) {
                if (cm.haveItem(4032327, 40) ) {
                    cm.gainItem(4032327, -40);
                    cm.gainNX(4000);
                    cm.sendOk("恭喜您获得4000点卷，感谢你的勤劳付出");
                    cm.dispose();
                } else {
                    cm.sendOk("您身上没有40个#i4032327#,请在次确认");
                    cm.dispose();
				}
			 }else if (selection == 104) {
                if (cm.haveItem(4032327, 60) ) {
                    cm.gainItem(4032327, -60);
                    cm.gainItem(4031568, 1);
                    cm.sendOk("恭喜您获得七星之一 #i4031568#x1 收集齐七星即可注入装备大幅增加属性喔~感谢你的勤劳付出。");
                    cm.dispose();
                } else {
                    cm.sendOk("您身上没有60个#i4032327#,请在次确认");
                    cm.dispose();
				}
			 }else if (selection == 105) {
                if (cm.haveItem(4032327, 100) ) {
                    cm.gainItem(4032327, -100);
                    cm.gainItem(1102489, 1);
                    cm.sendOk("恭喜您获得属性强大的 #i1102489#x1 感谢你的勤劳付出~");
                    cm.dispose();
                } else {
                    cm.sendOk("您身上没有100个#i4032327#,请在次确认");
                    cm.dispose();
				}
			 }else if (selection == 106) {
                if (cm.haveItem(4032327, 200) ) {
                    cm.gainItem(4032327, -200);
                    cm.gainItem(1142288, 1);
                    cm.sendOk("恭喜您获得属性超级强大的 #i1142288#x1 感谢你的付出。Tos再次感谢！");
                    cm.dispose();
                } else {
                    cm.sendOk("您身上没有200个#i4032327#,请在次确认");
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

	