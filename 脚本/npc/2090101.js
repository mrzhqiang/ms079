/* Lilishu
	Mu Lung Random Hair/Hair Color Change.
*/
var status = 0;
var beauty = 0;
var mhair = Array(30250, 30350, 30270, 30150, 30300, 30600, 30160, 30700, 30720, 30420);
var fhair = Array(31040, 31250, 31310, 31220, 31300, 31680, 31160, 31030, 31230, 31690, 31210, 31170, 31450);
var hairnew = Array();

function start() {
    status = -1;
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
	cm.sendSimple("嗨，我是#p2090101# 如果你有 #b#t5150024##k 或者 #b#t5151019##k 然后就可以来找我谈。 \r\n#L0#使用: #i5150024##t5150024##l\r\n#L1#使用: #i5151019##t5151019##l");
    } else if (status == 1) {
	if (selection == 0) {
	    var hair = cm.getPlayerStat("HAIR");
	    hair_Colo_new = [];
	    beauty = 1;

	    if (cm.getPlayerStat("GENDER") == 0) {
		hair_Colo_new = [30250, 30110, 30230, 30050, 30280, 30410, 30730, 30160, 30200];
	    } else {
		hair_Colo_new = [31150, 31310, 31220, 31300, 31260, 31160, 31730, 31410, 31410];
	    }
	    for (var i = 0; i < hair_Colo_new.length; i++) {
		hair_Colo_new[i] = hair_Colo_new[i] + (hair % 10);
	    }
	    cm.sendYesNo("是否要使用 #b#t5150024##k ??");
	} else if (selection == 1) {
	    var currenthaircolo = Math.floor((cm.getPlayerStat("HAIR") / 10)) * 10;
	    hair_Colo_new = [];
	    beauty = 2;

	    for (var i = 0; i < 8; i++) {
		hair_Colo_new[i] = currenthaircolo + i;
	    }
	    cm.sendYesNo("是否要使用 #b#t5150024##k ??");
	}
    } else if (status == 2){
	if (beauty == 1) {
	    if (cm.setRandomAvatar(5150024, hair_Colo_new) == 1) {
		cm.sendOk("享受！！");
	    } else {
		cm.sendOk("痾...似乎没有#b#t5150024##k");
	    }
	} else {
	    if (cm.setRandomAvatar(5151019, hair_Colo_new) == 1) {
		cm.sendOk("享受！！");
	    } else {
		cm.sendOk("痾...似乎没有#b#t5151019##k");
	    }
	}
	cm.dispose();
    }
}