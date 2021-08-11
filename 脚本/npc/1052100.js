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
	cm.sendSimple("我是钱老板. 如果你有 #b#t5150003##k \r\n或者 #b#t5151003##k 任何机会，\r\n那么怎么样让我改变你的发型颜色?\r\n#L0#使用 #b#t5150003##k\r\n#L1#使用 #b#t5151003##k");
    } else if (status == 1) {
	if (selection == 0) {
	    var hair = cm.getPlayerStat("HAIR");
	    hair_Colo_new = [];
	    beauty = 1;

	    if (cm.getPlayerStat("GENDER") == 0) {
		hair_Colo_new = [30030, 30020, 30000, 30130, 30350, 30190, 30110, 30180, 30050, 30040, 30160];
	    } else {
		hair_Colo_new = [31050, 31040, 31000, 31060, 31090, 31020, 31130, 31120, 31140, 31330, 31010];
	    }
	    for (var i = 0; i < hair_Colo_new.length; i++) {
		hair_Colo_new[i] = hair_Colo_new[i] + (hair % 10);
	    }
	    cm.askAvatar("选择一个喜欢的~",5151003, hair_Colo_new);
	} else if (selection == 1) {
	    var currenthaircolo = Math.floor((cm.getPlayerStat("HAIR") / 10)) * 10;
	    hair_Colo_new = [];
	    beauty = 2;

	    for (var i = 0; i < 8; i++) {
		hair_Colo_new[i] = currenthaircolo + i;
	    }
	    cm.askAvatar("选择一个喜欢的~",5151003, hair_Colo_new);
	}
    } else if (status == 2){
	if (beauty == 1){
	    if (cm.setAvatar(5150003, hair_Colo_new[selection]) == 1) {
		cm.sendOk("享受!");
	    } else {
		cm.sendOk("您貌似没有#b#t5150003##k..");
	    }
	} else {
	    if (cm.setAvatar(5151003, hair_Colo_new[selection]) == 1) {
		cm.sendOk("享受!");
	    } else {
		cm.sendOk("您貌似没有#b#t5151003##k..");
	    }
	}
	cm.safeDispose();
    }
}
