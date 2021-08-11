function start() {
	//cm.sendSimple("<3 <3");
	cm.sendSimple("#b你好 #k#h  ##e  #b我是排名系统.#k\r\n#L0##r家族排名\n\#l\r\n#L1##g玩家排名\n\#l\r\n#L2##b金币排名#l");//\r\n#L2##b金币排名#l
}

function action(mode, type, selection) {
	cm.dispose();
	if (selection == 0) {	
	cm.displayGuildRanks();
	cm.dispose();
	}
	else if (selection == 1) {
	cm.showlvl();
	cm.dispose();
	}
	else if (selection == 2) {
	cm.showmeso();
	cm.dispose();
	}
}
