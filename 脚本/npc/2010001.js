/* Don Giovanni
	Kerning VIP Hair/Hair Color Change.
*/
var status = -1;
var beauty = 0;
var hair_Colo_new;

function action(mode, type, selection) {
    if (mode == 0) {
	cm.dispose();
	return;
    } else {
	status++;
    }

    if (status == 0) {
	cm.sendSimple("我是米努. 如果你有 #b#t5150005##k \r\n或者 #b#t5151005##k 任何机会，\r\n那么怎么样让我改变你的发型颜色?\r\n#L0#使用 #b#t5150005##k\r\n#L1#使用 #b#t5151005##k");
    } else if (status == 1) {
	if (selection == 0) {
	    var hair = cm.getPlayerStat("HAIR");
	    hair_Colo_new = [];
	    beauty = 1;

	    if (cm.getPlayerStat("GENDER") == 0) {
		hair_Colo_new = [30030, 30020, 30000, 30270, 30230, 30260, 30280, 30240, 30290, 30340];
	    } else {
		hair_Colo_new = [31040, 31000, 31250, 31220, 31260, 31240, 31110, 31270, 31030, 31230];
	    }
	    for (var i = 0; i < hair_Colo_new.length; i++) {
		hair_Colo_new[i] = hair_Colo_new[i] + (hair % 10);
	    }
	    cm.askAvatar("选择一个喜欢的~",5150005, hair_Colo_new);
	} else if (selection == 1) {
	    var currenthaircolo = Math.floor((cm.getPlayerStat("HAIR") / 10)) * 10;
	    hair_Colo_new = [];
	    beauty = 2;

	    for (var i = 0; i < 8; i++) {
		hair_Colo_new[i] = currenthaircolo + i;
	    }
	    cm.askAvatar("选择一个喜欢的~",5150005, hair_Colo_new);
	}
    } else if (status == 2){
	if (beauty == 1){
	    if (cm.setAvatar(5150005, hair_Colo_new[selection]) == 1) {
		cm.sendOk("享受!");
	    } else {
		cm.sendOk("您貌似没有#b#t5150005##k..");
	    }
	} else {
	    if (cm.setAvatar(5151005, hair_Colo_new[selection]) == 1) {
		cm.sendOk("享受!");
	    } else {
		cm.sendOk("您貌似没有#b#t5151005##k..");
	    }
	}
	cm.safeDispose();
    }
}
