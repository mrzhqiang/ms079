/* Tepei
	Showa VIP Hair/Hair Color Change.
*/
var status = -1;
var beauty = 0;
var hair_Colo_new;

function start() {
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 0) {
	cm.dispose();
	return;
    } else {
	status++;
    }

    if (status == 0) {
	cm.sendSimple("嗨，我是#p9120100# 如果你有 #b#t5150009##k, 或者有 #b#t5151009##k, 我就可以帮助你~ \r\n#L0#使用: #i5150009##t5150009##l\r\n#L1#使用: #i5151009##t5151009##l");
    } else if (status == 1) {
	if (selection == 0) {
	    var hair = cm.getPlayerStat("HAIR");
	    hair_Colo_new = [];
	    beauty = 1;

	    if (cm.getPlayerStat("GENDER") == 0) {
		hair_Colo_new = [30230, 30030, 30260, 30280, 30240, 30290, 30020, 30270, 30340, 30710, 30810];
	    } else {
		hair_Colo_new = [31310, 31300, 31050, 31040, 31160, 31100, 31410, 31030, 31790, 31550];
	    }
	    for (var i = 0; i < hair_Colo_new.length; i++) {
		hair_Colo_new[i] = hair_Colo_new[i] + (hair % 10);
	    }
	    cm.askAvatar("选择一个你喜欢的。",5150009, hair_Colo_new);
	} else if (selection == 1) {
	    var currenthaircolo = Math.floor((cm.getPlayerStat("HAIR") / 10)) * 10;
	    hair_Colo_new = [];
	    beauty = 2;

	    for (var i = 0; i < 8; i++) {
		hair_Colo_new[i] = currenthaircolo + i;
	    }
	    cm.askAvatar("选择一个你喜欢的。",5150009, hair_Colo_new);
	}
    } else if (status == 2) {
	if (beauty == 1) {
	    if (cm.setAvatar(5150009, hair_Colo_new[selection]) == 1) {
		cm.sendOk("享受！");
	    } else {
		cm.sendOk("痾...你好像没有#t5150009#。");
	    }
	} else {
	    if (cm.setAvatar(5151009, hair_Colo_new[selection]) == 1) {
		cm.sendOk("享受！");
	    } else {
		cm.sendOk("痾...你好像没有#t5151009#。");
	    }
	}
	cm.dispose();
    }
}
