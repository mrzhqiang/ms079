function action(mode, type, selection) {
    var em = cm.getEventManager("Juliet");
    if (em == null) {
	cm.sendOk("找不到脚本，请联系GM！");
	cm.dispose();
	return;
    }
    switch(cm.getPlayer().getMapId()) {
	case 261000021:
	    cm.removeAll(4001130);
	    cm.removeAll(4001131);
	    cm.removeAll(4001132);
	    cm.removeAll(4001133);
	    cm.removeAll(4001134);
	    cm.removeAll(4001135);
	    if (cm.getPlayer().getParty() == null || !cm.isLeader()) {
		cm.sendOk("请找队长来和我谈。");
	    } else {
		var party = cm.getPlayer().getParty().getMembers();
		var mapId = cm.getPlayer().getMapId();
		var next = true;
		var size = 0;
		var it = party.iterator();
		while (it.hasNext()) {
			var cPlayer = it.next();
			var ccPlayer = cm.getPlayer().getMap().getCharacterById(cPlayer.getId());
			if (ccPlayer == null || ccPlayer.getLevel() < 70 || ccPlayer.getLevel() > 120) {
				next = false;
				break;
			}
			size += (ccPlayer.isGM() ? 4 : 1);
		}	
		if (next && (cm.getPlayer().isGM() || size >= 4)) {
			em.startInstance(cm.getPlayer().getParty(), cm.getPlayer().getMap());
		} else {
			cm.sendOk("请你的队员大于4个人(含)都要70等以上(含)120等以下(120也可)都要在这张地图再来找我");
		}
	    }
	    break;
	case 926110000:
	    cm.sendOk("你应该尝试在这里调查各地。看看库中的文件，直到你可以找到入口实验室.");
	    break;
	case 926110001:
	    cm.sendOk("请消除所有的怪物。");
	    break;
	case 926110100:
	    cm.sendOk("请把烧杯里的溢体装满。");
	    break;
	case 926110200:
	    if (cm.haveItem(4001131,1)) {
		cm.sendOk("哦，我的信找到了，谢谢！");
		cm.gainItem(4001131,-1);
		em.setProperty("stage", "1");
	    } else if (cm.haveItem(4001134,1)) {
		cm.gainItem(4001134,-1);
		cm.sendOk("谢谢你，现在帮我找#t4001135#.");
		em.setProperty("stage4", "1");
	    } else if (cm.haveItem(4001135,1) && em.getProperty("stage4").equals("1")) {
		cm.gainItem(4001135,-1);
		cm.sendOk("谢谢你，已经过关了。.");
		em.setProperty("stage4", "2");
		cm.getMap().getReactorByName("jnr3_out3").hitReactor(cm.getClient());
	    } else {
	    	cm.sendOk("现在我们必须停止冲突，请帮我找出#t4001134# 和 #t4001135#。");
	    }
	    break;
	case 926100300:
	    cm.sendOk("我们一定要到实验室的顶部.");
	    break;
	case 926100400:
	    cm.sendOk("当你准备好了，我们要快去救救我的爱人.");
	    break;
	case 926110401:
	    cm.warpParty(926110500); //urete
	    break;
    }
    cm.dispose();
}