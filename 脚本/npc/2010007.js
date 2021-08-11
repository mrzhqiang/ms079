/* guild creation npc */
var status = -1;
var sel;

function start() {
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 0 && status == 0) {
	cm.dispose();
	return;
    }
    if (mode == 1)
	status++;
    else
	status--;

    if (status == 0)
	cm.sendSimple("你想要做什么？\r\n#b#L0#创建公会#l\r\n#L1#解散公会#l\r\n#L2#扩充公会人数#l#k");
    else if (status == 1) {
	sel = selection;
	if (selection == 0) {
	    if (cm.getPlayerStat("GID") > 0) {
		cm.sendOk("你不能创建一个新的工会.");
		cm.dispose();
	    } else
		cm.sendYesNo("创建公会需要 #b15000000 金币#k, 你确定要创建公会吗?");
	} else if (selection == 1) {
	    if (cm.getPlayerStat("GID") <= 0 || cm.getPlayerStat("GRANK") != 1) {
		cm.sendOk("你不是公会会长所以不能解散公会");
		cm.dispose();
	    } else
		cm.sendYesNo("你确定要解散你的公会?你将无法恢复并且GP消失.");
	} else if (selection == 2) {
	    if (cm.getPlayerStat("GID") <= 0 || cm.getPlayerStat("GRANK") != 1) {
		cm.sendOk("你不是公会会长所以不能扩充人数");
		cm.dispose();
	    } else
		cm.sendYesNo("扩充公会人数 #b5#k 要 #b2500000 金币#k, 你确定要扩充吗?");
	}
    } else if (status == 2) {
	if (sel == 0 && cm.getPlayerStat("GID") <= 0) {
	    cm.genericGuildMessage(1);
	    cm.dispose();
	} else if (sel == 1 && cm.getPlayerStat("GID") > 0 && cm.getPlayerStat("GRANK") == 1) {
	    cm.disbandGuild();
	    cm.dispose();
	} else if (sel == 2 && cm.getPlayerStat("GID") > 0 && cm.getPlayerStat("GRANK") == 1) {
	    cm.increaseGuildCapacity();
	    cm.dispose();
	}
    }
}