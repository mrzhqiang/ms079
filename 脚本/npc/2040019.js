/* Everton
	Ludibrium Random Eye Change.
*/
var status = -1;
var beauty = 0;
var mface = Array(20000, 20001, 20002, 20003, 20004, 20005, 20006, 20007, 20008, 20012, 20014);
var fface = Array(21000, 21001, 21002, 21003, 21004, 21005, 21006, 21007, 21008, 21012, 21014);
var facenew = Array();

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
	cm.sendNext("嗨，我好无聊喔 如果你有一张 #b#t5152006##k, 我可以随机帮你搞定一张脸");
    } else if (status == 1) {
	cm.sendYesNo("是否想用 #b#t5152006##k?");
    } else if (status == 2){
	var face = cm.getPlayerStat("FACE");
	var facetype;

	if (cm.getPlayerStat("GENDER") == 0) {
	    facetype = [20000, 20001, 20002, 20003, 20004, 20005, 20006, 20007, 20008, 20012, 20014];
	} else {
	    facetype = [21000, 21001, 21002, 21003, 21004, 21005, 21006, 21007, 21008, 21012, 21014];
	}
	for (var i = 0; i < facetype.length; i++) {
	    facetype[i] = facetype[i] + face % 1000 - (face % 100);
	}
	
	if (cm.setRandomAvatar(5152006, facetype) == 1) {
	    cm.sendOk("享受!");
	} else {
	    cm.sendOk("痾..貌似没有#t5152006#");
	}
	cm.dispose();
    }
}
