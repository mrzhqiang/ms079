/* 	Eric
	Singapore VIP Hair/Color Changer
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
	cm.sendSimple("欢迎来到新加坡 如果你有 #b#t5150033##k 或者 #b#t5151028 ##k? 我就可以剪你专属的发型\r\n#L1#使用#i5150033##t5150033##l\r\n#L2#使用#i5151028##t5151028##l");
    } else if (status == 1) {
	if (selection == 1) {
	    var hair = cm.getPlayerStat("HAIR");
	    hair_Colo_new = [];
	    beauty = 1;

	    if (cm.getPlayerStat("GENDER") == 0) {
		hair_Colo_new = [30110, 30290, 30230, 30260, 30320, 30190, 30240, 30350, 30270, 30180];
	    } else {
		hair_Colo_new = [31260, 31090, 31220, 31250, 31140, 31160, 31100, 31120, 31030, 31270, 31810];
	    }
	    for (var i = 0; i < hair_Colo_new.length; i++) {
		hair_Colo_new[i] = hair_Colo_new[i] + (hair % 10);
	    }
	    cm.askAvatar("选择一个喜欢的", hair_Colo_new);
	} else if (selection == 2) {
	    var currenthaircolo = Math.floor((cm.getPlayerStat("HAIR") / 10)) * 10;
	    hair_Colo_new = [];
	    beauty = 2;

	    for (var i = 0; i < 8; i++) {
		hair_Colo_new[i] = currenthaircolo + i;
	    }
	    cm.askAvatar("选择一个喜欢的", hair_Colo_new);
	}
    } else if (status == 2){
	if (beauty == 1){
	    if (cm.setAvatar(5150033, hair_Colo_new[selection]) == 1) {
		cm.sendOk("享受！！");
	    } else {
		cm.sendOk("由于没有#b#t5150033##k 所以我不能帮忙。");
	    }
	} else {
	    if (cm.setAvatar(5151028, hair_Colo_new[selection]) == 1) {
		cm.sendOk("享受！！");
	    } else {
		cm.sendOk("由于没有#b#t5151028##k 所以我不能帮忙。");
	    }
	}
	cm.dispose();
    }
}