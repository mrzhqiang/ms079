/* Franz the Owner
	Orbis VIP Eye Change.
*/
var status = -1;
var facetype;

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
	cm.sendNext("哇，欢迎来到天空之城整形手术馆 我是#p2010002# 如果你有 #b#t5152005##k, 我可以帮你整想要的手术。");
    } else if (status == 1) {
	var face = cm.getPlayerStat("FACE");

	if (cm.getPlayerStat("GENDER") == 0) {
	    facetype = [20000, 20001, 20002, 20003, 20004, 20005, 20006, 20007, 20008, 20012, 20014];
	} else {
	    facetype = [21000, 21001, 21002, 21003, 21004, 21005, 21006, 21007, 21008, 21012, 21014];
	}
	for (var i = 0; i < facetype.length; i++) {
	    facetype[i] = facetype[i] + face % 1000 - (face % 100);
	}
	cm.askAvatar("来吧~选择你想要的脸型",5152005,facetype);

    } else if (status == 2) {
	if (cm.setAvatar(5152005, facetype[selection]) == 1) {
	    cm.sendOk("享受!");
	} else {
	    cm.sendOk("你好像没有#t5152005#");
	}
	cm.dispose();
    }
}