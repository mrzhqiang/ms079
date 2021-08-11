/*
	闹鬼宅邸 触发-美伊德 (229010000)
**/

function start() {

    if (cm.getParty() == null) { // No Party
	cm.sendOk("请组队再来找我");
    } else if (!cm.isLeader()) { // Not Party Leader
	cm.sendOk("请叫你的队长来找我!");
    } else {
	// Check if all party members are within Levels 50-200
	var party = cm.getParty().getMembers();
	var mapId = cm.getMapId();
	var next = true;
	var levelValid = 0;
	var inMap = 0;

	var it = party.iterator();
	while (it.hasNext()) {

	    var cPlayer = it.next();
	    if ((cPlayer.getLevel() >= 50 && cPlayer.getLevel() <= 200) || cPlayer.getJobId() == 900) {
		levelValid += 1;
	    } else {
		next = false;
	    }
	    if (cPlayer.getMapid() == mapId) {
		inMap += (cPlayer.getJobId() == 900 ? 4 : 1);
	    }
	}
	if (party.size() > 1 || inMap < 1) {
	    next = false;
	}
	if (next && cm.haveItem(4001337, 1)) {
	    var em = cm.getEventManager("QiajiPQ");
	    if (em == null) {
			cm.sendOk("找不到脚本，请联系GM！");
			cm.dispose();
			return;		
	    } else {
		var prop = em.getProperty("state");
		if (prop == null || prop.equals("0")) {
		    em.startInstance(cm.getParty(),cm.getMap());
		} else {
		    cm.sendOk("已经有队伍在里面挑战了。");
			cm.dispose();
			return;
			}
	    }
	} else {
	    cm.sendOk("你的队伍需要一个人,等级必须在50-200之间,请确认你的队友有没有都在这里,或是里面已经有人了,或你的钥匙不足!");
		cm.dispose();
		return;
		}
		cm.gainItem(4001337, -1)
    }
    cm.dispose();
}

function action(mode, type, selection) {
}