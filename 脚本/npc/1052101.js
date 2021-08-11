/* Andre
	Kerning Random Hair/Hair Color Change.
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
		cm.sendSimple("我是安德里亚. 如果你有 #b#t5151002##k \r\n或者 #b#t5150002##k 任何机会，\r\n那么怎么样让我改变你的发型颜色?\r\n#L0#使用 #b#t5150002##k\r\n#L1#使用 #b#t5151002##k");
    } else if (status == 1) {
	if (selection == 0) {
	    var hair = cm.getPlayerStat("HAIR");
	    hair_Colo_new = [];
	    beauty = 1;

	    if (cm.getPlayerStat("GENDER") == 0) {
		hair_Colo_new = [30130, 30350, 30190, 30110, 30180, 30050, 30040, 30160, 30770, 30620, 30550, 30520];
	    } else {
		hair_Colo_new = [31060, 31090, 31020, 31130, 31120, 31140, 31330, 31010, 31520, 31440, 31750, 31620];
	    }
	    for (var i = 0; i < hair_Colo_new.length; i++) {
		hair_Colo_new[i] = hair_Colo_new[i] + (hair % 10);
	    }
	    cm.sendYesNo("注意!这是随机，请问是否要使用 #b#t5150002##k?");
	} else if (selection == 1) {
	    var currenthaircolo = Math.floor((cm.getPlayerStat("HAIR") / 10)) * 10;
	    hair_Colo_new = [];
	    beauty = 2;

	    for (var i = 0; i < 8; i++) {
		hair_Colo_new[i] = currenthaircolo + i;
	    }
	    cm.sendYesNo("注意!这是随机，请问是否要使用 #b#t5151002##k?");
	}
    } else if (status == 2){
	if (beauty == 1){
	    if (cm.setRandomAvatar(5150002, hair_Colo_new) == 1) {
		cm.sendOk("享受!");
	    } else {
		cm.sendOk("您貌似没有#b#t5150002##k..");
	    }
	} else {
	    if (cm.setRandomAvatar(5151002, hair_Colo_new) == 1) {
		cm.sendOk("享受!");
	    } else {
		cm.sendOk("您貌似没有#b#t5151002##k..");
	    }
	}
	cm.dispose();
    }
}
