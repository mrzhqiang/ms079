/*
	Flo - Crossroad of Time(220040200)
**/

function start() {
    if (cm.getQuestStatus(6225) == 1 || cm.getQuestStatus(6315) == 1) {
	var ret = checkJob();
	if (ret == -1) {
	    cm.sendOk("请再组成一个队伍，再跟我说一次.");
	} else if (ret == 0) {
	    cm.sendOk("请确保组队人数为2.");
	} else if (ret == 1) {
	    cm.sendOk("你的一个队员没有资格进入另一个世界.");
	} else if (ret == 2) {
	    cm.sendOk("你的一个队员的水平是没有资格进入另一个世界.");
	} else {
	    var dd = cm.getEventManager("ElementThanatos");
	    if (dd != null) {
		dd.startInstance(cm.getParty(), cm.getMap());
	    } else {
		cm.sendOk("发生未知错误.");
	    }
	}
    } else {
	cm.sendOk("你似乎没有理由满足我.");
    }
    cm.dispose();
}

function checkJob() {
    var party = cm.getParty();

    if (party == null) {
	return -1;
    }
    if (party.getMembers().size() != 2) {
	return 0;
    }
    var it = party.getMembers().iterator();

    while (it.hasNext()) {
	var cPlayer = it.next();

	if (cPlayer.getJobId() == 212 || cPlayer.getJobId() == 222 || cPlayer.getJobId() == 900) {
	    if (cPlayer.getLevel() < 120) {
		return 2;
	    }
	} else {
	    return 1;
	}
    }
    return 3;
}