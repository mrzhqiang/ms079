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
                text += "";//
            }
			text += "\t\t\t  #e欢迎来到#bMapleStory #k!#n\r\n"
			text += "\t\t\t  #e您当前点卷为：#b"+cm.getPlayer().getCSPoints(1)+"#k!#n\r\n"
            text += "#L1##e#d100点卷兑换1个交易币#l\r\n\r\n"//3
            text += "#L2##d500点卷兑换5个交易币#l\r\n\r\n"//3
            text += "#L3##d1000点卷兑换10个交易币#l\r\n\r\n"//3
            text += "#L4##d5000点卷兑换50个交易币#l\r\n\r\n"//3
            text += "#L5##d10000点卷兑换100个交易币#l\r\n\r\n"//3
            text += "#L6##r1个交易币兑换100点卷#l\r\n\r\n"//3
            text += "#L7##r5个交易币兑换500点卷#l\r\n\r\n"//3
            text += "#L8##r10个交易币兑换1000点卷#l\r\n\r\n"//3
            text += "#L9##r50个交易币兑换5000点卷#l\r\n\r\n"//3
            text += "#L10##r100个交易币兑换10000点卷#l\r\n\r\n"//3
            cm.sendSimple(text);
        } else if (selection == 1) {
                if (cm.getPlayer().getCSPoints(1) >= 100) {
					item = cm.gainGachaponItem(4000463, 1);
					if (item != -1) {
						cm.sendOk("你获得了 #b#t" + item + "##k " + 1 + "个。");
						cm.getPlayer().modifyCSPoints(1, -100);
					} else {
						cm.sendOk("请你确认在背包的装备，消耗，其他窗口中是否有一格以上的空间。");
					}
                    cm.dispose();
                    
                } else {
                    cm.sendOk("#b您没有足够的点卷进行购买,请充值.");
                    cm.dispose();
                }
        } else if (selection == 2) {
                if (cm.getPlayer().getCSPoints(1) >= 500) {
					item = cm.gainGachaponItem(4000463, 5);
					if (item != -1) {
						cm.sendOk("你获得了 #b#t" + item + "##k " + 5 + "个。");
						cm.getPlayer().modifyCSPoints(1, -500);
					} else {
						cm.sendOk("请你确认在背包的装备，消耗，其他窗口中是否有一格以上的空间。");
					}
                    cm.dispose();
                    
                } else {
                    cm.sendOk("#b您没有足够的点卷进行购买,请充值.");
                    cm.dispose();
                }
        } else if (selection == 3) {
                if (cm.getPlayer().getCSPoints(1) >= 1000) {
					item = cm.gainGachaponItem(4000463, 10);
					if (item != -1) {
						cm.sendOk("你获得了 #b#t" + item + "##k " + 10 + "个。");
						cm.getPlayer().modifyCSPoints(1, -1000);
					} else {
						cm.sendOk("请你确认在背包的装备，消耗，其他窗口中是否有一格以上的空间。");
					}
                    cm.dispose();
                    
                } else {
                    cm.sendOk("#b您没有足够的点卷进行购买,请充值.");
                    cm.dispose();
                }
        } else if (selection == 4) {
                if (cm.getPlayer().getCSPoints(1) >= 5000) {
					item = cm.gainGachaponItem(4000463, 50);
					if (item != -1) {
						cm.sendOk("你获得了 #b#t" + item + "##k " + 50 + "个。");
						cm.getPlayer().modifyCSPoints(1, -5000);
					} else {
						cm.sendOk("请你确认在背包的装备，消耗，其他窗口中是否有一格以上的空间。");
					}
                    cm.dispose();
                    
                } else {
                    cm.sendOk("#b您没有足够的点卷进行购买,请充值.");
                    cm.dispose();
                }
        } else if (selection == 5) {
                if (cm.getPlayer().getCSPoints(1) >= 10000) {
					item = cm.gainGachaponItem(4000463, 100);
					if (item != -1) {
						cm.sendOk("你获得了 #b#t" + item + "##k " + 100 + "个。");
						cm.getPlayer().modifyCSPoints(1, -10000);
					} else {
						cm.sendOk("请你确认在背包的装备，消耗，其他窗口中是否有一格以上的空间。");
					}
                    cm.dispose();
                    
                } else {
                    cm.sendOk("#b您没有足够的点卷进行购买,请充值.");
                    cm.dispose();
                }
        } else if (selection == 6) {
        if (cm.haveItem(4000463, 1)) {
			cm.gainItem(4000463,-1);
			cm.getPlayer().modifyCSPoints(1, 100);
			cm.sendOk("你获得了100点卷！。");
		}else{
            cm.sendOk("#b您没有足够的中介币无法兑换！.");
		}
                    cm.dispose();
        } else if (selection == 7) {
        if (cm.haveItem(4000463, 5)) {
			cm.gainItem(4000463,-5);
			cm.getPlayer().modifyCSPoints(1, 500);
			cm.sendOk("你获得了500点卷！。");
		}else{
            cm.sendOk("#b您没有足够的中介币无法兑换！.");
		}
                    cm.dispose();
        } else if (selection == 8) {
        if (cm.haveItem(4000463, 10)) {
			cm.gainItem(4000463,-10);
			cm.getPlayer().modifyCSPoints(1, 1000);
			cm.sendOk("你获得了1000点卷！。");
		}else{
            cm.sendOk("#b您没有足够的中介币无法兑换！.");
		}
                    cm.dispose();
        } else if (selection == 9) {
        if (cm.haveItem(4000463, 50)) {
			cm.gainItem(4000463,-50);
			cm.getPlayer().modifyCSPoints(1, 5000);
			cm.sendOk("你获得了5000点卷！。");
		}else{
            cm.sendOk("#b您没有足够的中介币无法兑换！.");
		}
                    cm.dispose();
        } else if (selection == 10) {
        if (cm.haveItem(4000463, 100)) {
			cm.gainItem(4000463,-100);
			cm.getPlayer().modifyCSPoints(1, 10000);
			cm.sendOk("你获得了10000点卷！。");
		}else{
            cm.sendOk("#b您没有足够的中介币无法兑换！.");
		}
                    cm.dispose();
		}
    }
}


