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
			cm.sendSimple("#b欢迎你 #r#h ##k ,这里是马币兑换平台\r\n\r\n#r注：马币只有限时答题才能有几率得到喔~！\r\n\r\n#b兑换前请确认背包格子,否则后果自负！！" +           
"#k\r\n#L101##r马币#i4310030##bx30#r兑换#b混沌卷轴#i2049100#x1\r\n#L102##r马币#i4310030##bx50#r兑换#b祝福卷轴#i2340000#x1\r\n#L103##r马币#i4310030##bx100#r兑换 #k3000点卷x1\r\n#L104##r马币#i4310030##bx200#r兑换#b开阳#i4032170#x1\r\n#L105##r马币#i4310030##bx300#r兑换#b#v1142617#x1\r\n");

        } else if (status == 1) {
            
            if (selection == 101) {
                if (cm.haveItem(4310030, 30) ) {
                    cm.gainItem(4310030, -30);
                    cm.gainItem(2049100, 1);
                    cm.sendOk("恭喜您获得#i2049100#，感谢你的勤劳付出。");
                    cm.dispose();
                } else {
                    cm.sendOk("您身上没有30个#i4310030#,请在次确认");
                    cm.dispose();
                }
            } else if (selection == 102) {
                if (cm.haveItem(4310030, 50) ) {
                    cm.gainItem(4310030, -50);
                    cm.gainItem(2340000, 1);
                    cm.sendOk("恭喜您获得#i2340000#x1，感谢你你勤劳付出。");
                    cm.dispose();
                } else {
                    cm.sendOk("您身上没有50个#i4310030#,请在次确认");
                    cm.dispose();
				}
			 }else if (selection == 103) {
                if (cm.haveItem(4310030, 100) ) {
                    cm.gainItem(4310030, -100);
                    cm.gainNX(3000);
                    cm.sendOk("恭喜您获得3000点卷，感谢你的勤劳付出");
                    cm.dispose();
                } else {
                    cm.sendOk("您身上没有100个#i4310030#,请在次确认");
                    cm.dispose();
				}
			 }else if (selection == 104) {
                if (cm.haveItem(4310030, 200) ) {
                    cm.gainItem(4310030, -200);
                    cm.gainItem(4032170, 1);
                    cm.sendOk("恭喜您获得七星之一 #i4032170#x1 收集齐七星即可注入装备大幅增加属性喔~感谢你的勤劳付出。");
                    cm.dispose();
                } else {
                    cm.sendOk("您身上没有200个#i4310030#,请在次确认");
                    cm.dispose();
				}
			 }else if (selection == 105) {
                if (cm.haveItem(4310030, 300) ) {
                    cm.gainItem(4310030, -300);
	        cm.gainItem(1142617,9,9,9,9,1000,1000,8,8,50,50,15,15,5,5,0,0,0);
                    cm.sendOk("恭喜您获得属性强大的 #i1142617#x1 感谢你的勤劳付出~");
                    cm.dispose();
                } else {
                    cm.sendOk("您身上没有300个#i4310030#,请在次确认");
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

	