/* 
 * Shuang, Victoria Road: Excavation Site<Camp> (101030104)
 * Start of Guild Quest
 */

var status;
var GQItems = Array(1032033, 4001024, 4001025, 4001026, 4001027, 4001028, 4001031, 4001032, 4001033, 4001034, 4001035, 4001037);

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 0 && status == 0) {
	cm.dispose();
	return;
    }
    if (mode == 1) {
	status++;
    } else {
	status--;
    }

    if (status == 0) {
	if (cm.getPlayer().hasEquipped(1032033)) {
		cm.sendOk("请卸下你的保护石.");
		cm.dispose();
	} else {
		cm.sendSimple("你要做什么? #b\r\n#L0#开始一个家族的任务#l\r\n#L1#加入家族的家族#l");
	}
	
    } else if (status == 1) {
	if (selection == 0) { //Start
	    if (cm.getPlayerStat("GID") == 0 || cm.getPlayerStat("GRANK") >= 3) { //no guild or not guild master/jr. master
		cm.sendNext("只有家族族长和副族长才能开始这个任务.");
		cm.dispose();
	    } else {
		var em = cm.getEventManager("GuildQuest");
		if (em == null) {
		    cm.sendOk("副本发生问题.");
		} else {
		    var prop = em.getProperty("started");

		    if ((prop.equals("false") || prop == null) && em.getInstance("GuildQuest") == null) {
    			for (var i = 0; i < GQItems.length; i++) {
				cm.removeAll(GQItems[i]);
    			}
			em.startInstance(cm.getPlayer(), cm.getPlayer().getName());
			em.setProperty("state", "0");
			cm.guildMessage("家族已进入家族的任务。请在通道的挖掘营地报告 " + cm.getClient().getChannel() + ".");
		    } else {
			cm.sendOk("有人已经尝试在家族任务.")
		    }
		}
		cm.dispose();
	    }
	} else if (selection == 1) { //entering existing GQ
	    if (cm.getPlayerStat("GID") == 0) { //no guild or not guild master/jr. master
		cm.sendNext("你必须加入一个家族.");
		cm.dispose();
	    } else {
		var em = cm.getEventManager("GuildQuest");
		if (em == null) {
		    cm.sendOk("副本发生问题.");
		} else {
		    var eim = em.getInstance("GuildQuest");

		    if (eim == null) {
			cm.sendOk("你的家族目前没有注册这个副本.");
		    } else {
//			if (em.getProperty("guildid") != null && !em.getProperty//("guildid").equalsIgnoreCase("" + cm.getPlayerStat("GID"))) {
//			if (cm.getPlayer().isGM()) {
//			    cm.sendOk("This instance is not your guild. Instance Guild: "  + //em.getProperty("guildid") + ", Your Guild: " + cm.getPlayerStat("GID"));
//			} else {
//			    cm.sendOk("This instance is not your guild.");
//			}
//			} else 
if (em.getProperty("started").equals("false")) {
    			for (var i = 0; i < GQItems.length; i++) {
				cm.removeAll(GQItems[i]);
    			}
			    eim.registerPlayer(cm.getPlayer());
			} else {
			    cm.sendOk("I'm sorry, but the guild has gone on without you. Try again later.");
			}
		    }
		}
		cm.dispose();
	    }
	}
    }
}