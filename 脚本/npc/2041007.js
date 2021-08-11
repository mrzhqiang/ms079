/* Miyu
	Ludibrium VIP Hair/Hair Color Change.
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
	cm.sendSimple("欢迎~欢迎 来到玩具城美发店! 如果你有 #b#t5150007##k 或者 #b#t5151007##k? 我就可以帮你改变 请选择要哪种服务: \r\n#L0#使用: #i5150007##t5150007##l\r\n#L1#使用: #i5151007##t5151007##l");
    } else if (status == 1) {
	if (selection == 0) {
	    var hair = cm.getPlayerStat("HAIR");
	    hair_Colo_new = [];
	    beauty = 1;

	    if (cm.getPlayerStat("GENDER") == 0) {
		hair_Colo_new = [30030, 30020, 30000, 30250, 30190, 30150, 30050, 30280, 30240, 30300, 30160];
	    } else {
		hair_Colo_new = [31040, 31000, 31150, 31280, 31160, 31120, 31290, 31270, 31030, 31230, 31010];
	    }
	    for (var i = 0; i < hair_Colo_new.length; i++) {
		hair_Colo_new[i] = hair_Colo_new[i] + (hair % 10);
	    }
	    cm.askAvatar("选择一个你喜欢的",5150007, hair_Colo_new);
	} else if (selection == 1) {
	    var currenthaircolo = Math.floor((cm.getPlayerStat("HAIR") / 10)) * 10;
	    hair_Colo_new = [];
	    beauty = 2;

	    for (var i = 0; i < 8; i++) {
		hair_Colo_new[i] = currenthaircolo + i;
	    }
	    cm.askAvatar("选择一个你喜欢的",5150007, hair_Colo_new);
	}
    } else if (status == 2){
	if (beauty == 1){
	    if (cm.setAvatar(5150007, hair_Colo_new[selection]) == 1) {
		cm.sendOk("享受!");
	    } else {
		cm.sendOk("痾..貌似没有#t5150007#");
	    }
	} else {
	    if (cm.setAvatar(5151007, hair_Colo_new[selection]) == 1) {
		cm.sendOk("享受!");
	    } else {
		cm.sendOk("痾..貌似没有#t5151007#");
	    }
	}
	cm.dispose();
    }
}