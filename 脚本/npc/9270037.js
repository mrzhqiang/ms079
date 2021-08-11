/* 	Jimmy
	Singa Random Hair/Color Changer
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
	cm.sendSimple("嗨，我是#p9270037# 如果您有 #b#t5150032##k 或者 #b#t5151027##k 我可以随机变一个好看的~~\r\n#L0#使用:#i5150032##t5150032##l\r\n#L1#使用:#i5151027##t5151027##l");
    } else if (status == 1) {
	if (selection == 0) {
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
	    cm.sendYesNo("确定是否要使用了??");
	} else if (selection == 1) {
	    var currenthaircolo = Math.floor((cm.getPlayerStat("HAIR") / 10)) * 10;
	    hair_Colo_new = [];
	    beauty = 2;

	    for (var i = 0; i < 8; i++) {
		hair_Colo_new[i] = currenthaircolo + i;
	    }
	    cm.sendYesNo("确定是否要使用了??");
	}
    } else if (status == 2){
	if (beauty == 1){
	    if (cm.setRandomAvatar(5150032, hair_Colo_new) == 1) {
		cm.sendOk("享受你新的造型吧！");
	    } else {
		cm.sendOk("由于没有#b#t5150032##k 所以我不能帮忙。");
	    }
	} else {
	    if (cm.setRandomAvatar(5151027, hair_Colo_new) == 1) {
		cm.sendOk("享受你新的造型吧！");
	    } else {
		cm.sendOk("由于没有#b#t5151027##k 所以我不能帮忙。");
	    }
	}
	cm.dispose();
    }
}