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

            cm.sendOk("感谢你的光临！");
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
            var tex2 = "";
            var text = "";
            for (i = 0; i < 10; i++) {
                text += "";
            }
			text += "#e欢迎来到#b冒险岛#k！#n\r\n\r\n"
	//		text += "\t#r枫叶兑换点卷开始活动永久折扣1:1!#n\r\n"
            text += "#L1##b200个枫叶兑换1000点卷#l\r\n\r\n"//3
         //   text += "#L2##b5个枫叶兑换5点卷#l\r\n\r\n"//3
         //   text += "#L3##b10个枫叶兑换10点卷#l\r\n\r\n"//3
         //   text += "#L4##b100个枫叶兑换100点卷#l\r\n\r\n"//3
            cm.sendSimple(text);
        } else if (selection == 1) { 
			if(cm.haveItem(4001126,200)){
				cm.gainNX(1000);
				cm.gainItem(4001126,-200);
				cm.sendOk("200个枫叶兑换1000点卷成功！");
	//		    cm.worldMessage(6,"【兑换系统】["+cm.getName()+"]250个枫叶兑换100点卷成功！");
				cm.dispose();
			}else{
				cm.sendOk("你没有200个枫叶无法兑换1000点卷！");
				cm.dispose();
			}
        } else if (selection == 2) {  
			if(cm.haveItem(4001126,500)){
				cm.gainDY(200);
				cm.gainItem(4001126,-500);
				cm.sendOk("500个枫叶兑换200点卷成功！");
			    cm.worldMessage(6,"【兑换系统】["+cm.getName()+"]500个枫叶兑换200点卷成功！");
				cm.dispose();
			}else{
				cm.sendOk("你没有500个枫叶无法兑换200点卷！");
				cm.dispose();
			}
        } else if (selection == 3) { 
			if(cm.haveItem(4001126,1000)){
				cm.gainDY(400);
				cm.gainItem(4001126,-1000);
				cm.sendOk("1000个枫叶兑换400点卷成功！");
			    cm.worldMessage(6,"【兑换系统】["+cm.getName()+"]1000个枫叶兑换400点卷成功！");
				cm.dispose();
			}else{
				cm.sendOk("你没有1000个枫叶无法兑换400点卷！");
				cm.dispose();
			}
        } else if (selection == 4) {
			if(cm.haveItem(4001126,2500)){
				cm.gainDY(1000);
				cm.gainItem(4001126,-2500);
				cm.sendOk("2500个枫叶兑换1000点卷成功！");
			    cm.worldMessage(6,"【兑换系统】["+cm.getName()+"]2500个枫叶兑换1000点卷成功！");
				cm.dispose();
			}else{
				cm.sendOk("你没有2500个枫叶无法兑换1000点卷！");
				cm.dispose();
			}
		}
    }
}


