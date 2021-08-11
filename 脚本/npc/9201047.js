var status = -1;

function action(mode, type, selection) {
    if (mode == 1) {
		status++;
    } else {
	if (status == 0) {
	    cm.dispose();
	}
	status--;
    }
    if (cm.getPlayer().getMapId() == 670011000) {
		//cm.gainNX(2000);
		cm.warp(670010000,0);
		cm.dispose();
	return;
    }
    var em = cm.getEventManager("Amoria");
    if (em == null || !cm.isLeader()) {
		cm.dispose();
		return;
    }
    if (em.getProperty("apq1").equals("0")) {
	if (cm.getMap().getAllMonstersThreadsafe().size() == 0) {
	    em.setProperty("apq1", "1");
	    cm.mapMessage(5, "某种东西已经在地图上某个地方生成了。");
	    cm.spawnMonster(9400518,1);
	} else {
	    cm.sendOk("请问你可以帮我消除地图上所有的怪物吗？？");
	}
    } else if (em.getProperty("apq1").equals("1")) {
	if (cm.haveItem(4031595)) {
	    cm.gainItem(4031595, -1);
    	    cm.showEffect(true, "quest/party/clear");
    	    cm.playSound(true, "Party1/Clear");
	    em.setProperty("apq1", "2");
	} else {
	    cm.sendOk("请告诉我使用永锤从轮渡打破镜子的证明。");
		}
    }
    cm.dispose();
}