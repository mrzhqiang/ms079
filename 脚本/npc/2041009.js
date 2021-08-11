/* Mini
	Ludibrium Random Hair/Hair Color Change.
*/
var status = -1;
var beauty = 0;
var hair_Colo_new;

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
    if (status == 0) {
	cm.sendSimple("嗨，我是#p2041009# 如果你有 #b#t5150012##k 或 #b#t5151006##k 我就可以帮你改变 请选择要哪种服务: \r\n#L0#使用: #i5150006##t5150006##l\r\n#L1#使用: #i5151006##t5151006##l");
    } else if (status == 1) {
	if (selection == 0) {
	    var hair = cm.getPlayerStat("HAIR");
	    hair_Colo_new = [];
	    beauty = 1;

	    if (cm.getPlayerStat("GENDER") == 0) {
		hair_Colo_new = [30250, 30190, 30150, 30050, 30280, 30240, 30300, 30160, 30650, 30540, 30640, 30680];
	    } else {
		hair_Colo_new = [31150, 31280, 31160, 31120, 31290, 31270, 31030, 31230, 31010, 31640, 31540, 31680, 31600];
	    }
	    for (var i = 0; i < hair_Colo_new.length; i++) {
		hair_Colo_new[i] = hair_Colo_new[i] + (hair % 10);
	    }
	    cm.sendYesNo("是否确定要使用注意:他是#r随机#k!");
	} else if (selection == 1) {
	    var currenthaircolo = Math.floor((cm.getPlayerStat("HAIR") / 10)) * 10;
	    hair_Colo_new = [];
	    beauty = 2;

	    for (var i = 0; i < 8; i++) {
		hair_Colo_new[i] = currenthaircolo + i;
	    }
	    cm.sendYesNo("是否确定要使用注意:他是#r随机#k!");
	}
    } else if (status == 2){
	if (beauty == 1){
	    if (cm.setRandomAvatar(5150006, hair_Colo_new) == 1) {
		cm.sendOk("享受!");
	    } else {
		cm.sendOk("痾..貌似没有#t5150006#");
	    }
	} else {
	    if (cm.setRandomAvatar(5151006, hair_Colo_new) == 1) {
		cm.sendOk("享受!");
	    } else {
		cm.sendOk("痾..貌似没有#t5151006#");
	    }
	}
	cm.dispose();
    }
}