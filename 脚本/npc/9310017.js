/* Brittany
	Henesys Random Hair/Hair Color Change.
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
	cm.sendSimple("嗨，我是#p9310017# 如果你有 #b#t5150014##k 或者 #b#t5151010##k, 我就可以免费帮你弄好看的头发。 \r\n#L0#使用: #i5150014##t5150014##l\r\n#L1#使用: #i5151010##t5151010##l");
    } else if (status == 1) {
	if (selection == 0) {
	    var hair = cm.getPlayerStat("HAIR");
	    hair_Colo_new = [];
	    beauty = 1;

	    if (cm.getPlayerStat("GENDER") == 0) {
		hair_Colo_new = [30030, 30040, 30000, 30062, 30110, 30120, 30160, 30260, 30270, 30420, 30550, 30340, 30300];
	    } else {
		hair_Colo_new = [31000, 31420, 31290, 31490, 30420, 31480, 31810, 31080, 31880, 31030, 31850, 31700, 34000];
	    }
	    for (var i = 0; i < hair_Colo_new.length; i++) {
		hair_Colo_new[i] = hair_Colo_new[i] + (hair % 10);
	    }
	    cm.sendYesNo("确定要使用 #b#t5150014##k 随机剪发了？？");

	} else if (selection == 1) {
	    var currenthaircolo = Math.floor((cm.getPlayerStat("HAIR") / 10)) * 10;
	    hair_Colo_new = [];
	    beauty = 2;

	    for (var i = 0; i < 7; i++) {
		hair_Colo_new[i] = currenthaircolo + i;
	    }
	    cm.sendYesNo("确定要使用 #b#t5151010##k 随机染发了？？");
	}
    } else if (status == 2){
	if (beauty == 1){
	    if (cm.setRandomAvatar(5150014, hair_Colo_new) == 1) {
		cm.sendOk("享受！");
	    } else {
		cm.sendOk("痾.... 貌似没有#b#t5150014##k。");
	    }
	} else {
	    if (cm.setRandomAvatar(5151010, hair_Colo_new) == 1) {
		cm.sendOk("享受！");
	    } else {
		cm.sendOk("痾.... 貌似没有#b#t5151010##k。");
	    }
	}
	cm.safeDispose();
    }
}