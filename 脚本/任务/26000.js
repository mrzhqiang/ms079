var status = -1;

function start(mode, type, selection) {
	if (mode == 1) {
	    status++;
	} else {
	    qm.dispose();
	    return;
	}
	if (qm.getGuild().getLevel() < 1 || !qm.getGuild().hasSkill(910000006)) {
	    qm.dispose();
	    return;
	}
	if (status == 0) {
	    qm.sendYesNo("The Guild supplies have arrived. Here, take them, and don't complain about the amount. You can always get more when your Guild level goes up.");
        } else {
	    if (!qm.canHold(2002037, qm.getGuild().getLevel() * 20)) {
		qm.sendOk("请保留一些空间！");
	    } else {
		qm.gainItemPeriod(2002037, qm.getGuild().getLevel() * 20, 7);
	        qm.forceCompleteQuest();
	    }
	    qm.dispose();
        }
}
function end(mode, type, selection) {
	if (mode == 1) {
	    status++;
	} else {
	    qm.dispose();
	    return;
	}
	if (qm.getGuild().getLevel() < 1 || !qm.getGuild().hasSkill(910000006)) {
	    qm.dispose();
	    return;
	}
	if (status == 0) {
	    qm.sendYesNo("The Guild supplies have arrived. Here, take them, and don't complain about the amount. You can always get more when your Guild level goes up.");
        } else {
	    if (!qm.canHold(2002037, qm.getGuild().getLevel() * 20)) {
		qm.sendOk("Please make some space");
	    } else {
		qm.gainItemPeriod(2002037, qm.getGuild().getLevel() * 20, 7);
	        qm.forceCompleteQuest();
	    }
	    qm.dispose();
        }
}
