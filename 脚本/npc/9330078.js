
/*
星缘NPC
枫叶换取点卷
*/

//importPackage(net.sf.odinms.client);

function start() {
	status = -1;
	
	action(1, 0, 0);
}

function action(mode, type, selection) {
            if (mode == -1) {
                cm.dispose();
            }
            else {
                if (status >= 0 && mode == 0) {
                
			cm.sendOk("我是土豪，我不和你交朋友！");
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
				cm.sendSimple("你有什么稀奇古怪的好玩的材料吗。。。都可以找我回收哦！我开的价格是点卷哦！\r\n#L1##b2个#b动物皮#r换购1点卷#n#L2##b20个动物皮#r换购10点卷#k\r\n#L3##b2个粗羽毛#r换购1点卷#k#L4##b20个粗羽毛#r换购10点卷#l\r\n#L80##b200个动物皮#r换购100点卷#k#L81##b200个粗羽毛#r换购100点卷#l\r\n\r\n#k以下是BOSS换购区(每个可换购#b50#k点卷)：\rk#L5##r#z4001083# #L6##z4001084# #L7##z4001085#");
			} else if (status == 1) { //使用50枫叶换取50点卷
			if (selection == 1) { //动物皮
			if (cm.haveItem(4000021, 2)) {
                        cm.gainItem(4000021,-2);
			cm.gainNX(1);
			cm.sendOk("成功兑换");
                  	cm.dispose();
                   	} else {
	           	cm.sendOk("不足~");
			cm.dispose(); 
			}
			} else if (selection == 80) { //动物皮
			if (cm.haveItem(4000021, 200)) {
                        cm.gainItem(4000021,-200);
			cm.gainNX(100);
			cm.sendOk("成功兑换");
                  	cm.dispose();
                   	} else {
	           	cm.sendOk("不足~");
			cm.dispose(); }
//------------------------------高级鱼饵兑换----------------------------------
            } else if (selection == 81) { //粗羽毛
			if (cm.haveItem(4003004, 200)) {
                        cm.gainItem(4003004,-200);
			cm.gainNX(100);
			cm.sendOk("成功兑换");
                  	cm.dispose();
                   	} else {
	           	cm.sendOk("不足~");
			cm.dispose(); }
//-------------------------------激活四转技能-----------------------------
			} else if  (selection == 2) { //使用500枫叶500点卷
			if (cm.haveItem(4000021, 20)) {
                        cm.gainItem(4000021,-20);
			cm.gainNX(10);
			cm.sendOk("成功兑换");
                  	cm.dispose();
                   	} else {
	           	cm.sendOk("不足~");
			cm.dispose(); }
//------------------------------高级鱼饵兑换----------------------------------
            } else if (selection == 3) { //粗羽毛
			if (cm.haveItem(4003004, 2)) {
                        cm.gainItem(4003004,-2);
			cm.gainNX(1);
			cm.sendOk("成功兑换");
                  	cm.dispose();
                   	} else {
	           	cm.sendOk("不足~");
			cm.dispose(); }
//--------------------------------鱼饵兑换------------------------------------
            } else if (selection == 4) {
			if (cm.haveItem(4003004, 20)) {
                        cm.gainItem(4003004,-20);
			cm.gainNX(10);
			cm.sendOk("成功兑换");
                  	cm.dispose();
                   	} else {
	           	cm.sendOk("不足~");
			cm.dispose(); }
//-------------------------------BOSS兑换------------------------------------4001083
	                 } else if (selection == 5) { 
			if (cm.haveItem(4001083, 1)) {
                        cm.gainItem(4001083,-1);
			cm.gainNX(50);
			cm.sendOk("成功兑换");
                  	cm.dispose();
                   	} else {
	           	cm.sendOk("不足~");
			cm.dispose(); }
	                 } else if (selection == 6) {
			if (cm.haveItem(4001084, 1)) {
                        cm.gainItem(4001084,-1);
			cm.gainNX(50);
			cm.sendOk("成功兑换");
                  	cm.dispose();
                   	} else {
	           	cm.sendOk("不足~");
			cm.dispose(); } //OK 4001085
	                 } else if (selection == 7) {
			if (cm.haveItem(4001085, 1)) {
                        cm.gainItem(4001085,-1);
			cm.gainNX(50);
			cm.sendOk("成功兑换");
                  	cm.dispose();
                   	} else {
	           	cm.sendOk("不足~");
			cm.dispose(); } //ok
	                 } else if (selection == 8) {
			if (cm.haveItem(4000040, 1)) {
                        cm.gainItem(4000040,-1);
			cm.gainNX(50);
			cm.sendOk("成功兑换");
                  	cm.dispose();
                   	} else {
	           	cm.sendOk("不足~");
			cm.dispose(); }
	                 } else if (selection == 9) {
			if ((cm.getNX() >= 2000)) { 
			cm.gainNX(-2000);
		   	cm.gainItem(1332099,1);
			cm.sendOk("成功兑换了装备!");
                  	cm.dispose();
                   	} else {
	           	cm.sendOk("点卷不足~");
			cm.dispose(); }
	                 } else if (selection == 10) {
			if ((cm.getNX() >= 2000)) { 
			cm.gainNX(-2000);
		   	cm.gainItem(1472100,1);
			cm.sendOk("成功兑换了装备!");
                  	cm.dispose();
                   	} else {
	           	cm.sendOk("点卷不足~");
			cm.dispose(); }
	                 } else if (selection == 11) {
			if ((cm.getNX() >= 2000)) { 
			cm.gainNX(-2000);
		   	cm.gainItem(1482046,1);
			cm.sendOk("成功兑换了装备!");
                  	cm.dispose();
                   	} else {
	           	cm.sendOk("点卷不足~");
			cm.dispose(); }
	                 } else if (selection == 12) {
			if ((cm.getNX() >= 2000)) { 
			cm.gainNX(-2000);
		   	cm.gainItem(1492048,1);
			cm.sendOk("成功兑换了装备!");
                  	cm.dispose();
                   	} else {
	           	cm.sendOk("点卷不足~");
			cm.dispose(); }


}}
	}
}