/* Noma
	Mu Lung Random/VIP Eye Change.
*/
var status = -1;
var beauty = 0;
var facetype;

function action(mode, type, selection) {
    if (mode == 0) {
	cm.dispose();
	return;
    } else {
	status++;
    }

    if (status == 0) {
	cm.sendSimple("嗨，我是#p2090104# 我在#b桃花仙境#k 这个地方是很有名的整形师，甚至比我师父还厉害！ 如果你有 #b#t5152027##k或者 #b#t5152028##k, 我可以帮你改变你想要的脸型。 \r\n#L0#使用: #i5152027##t5152027##l\r\n#L1#使用: #i5152028##t5152028##l");
    } else if (status == 1) {
	var face = cm.getPlayerStat("FACE");
	facetype = [];
	beauty = 1;

	if (cm.getPlayerStat("GENDER") == 0) {
	    facetype = [20000, 20001, 20002, 20003, 20004, 20005, 20006, 20007, 20008, 20012, 20014, 20009, 20010];
	} else {
	    facetype = [21000, 21001, 21002, 21003, 21004, 21005, 21006, 21007, 21008, 21012, 21014, 21009, 21011];
	}
	for (var i = 0; i < facetype.length; i++) {
	    facetype[i] = facetype[i] + face % 1000 - (face % 100);
	}

	if (selection == 0) {
	    beauty = 1;
	    cm.sendYesNo("是否要使用 #b#t5152027##k #r注意:他是随机#k?");
	} else if (selection == 1) {
	    beauty = 2;
	    cm.askAvatar("选择一个你喜欢的。", facetype);
	}
    } else if (status == 2){
	if (beauty == 1){
	    if (cm.setRandomAvatar(5152027, facetype) == 1) {
		cm.sendOk("享受!");
	    } else {
		cm.sendOk("痾...貌似没有#b#t5152027##k。");
	    }
	} else {
	    if (cm.setAvatar(5152028, facetype[selection]) == 1) {
		cm.sendOk("享受!");
	    } else {
		cm.sendOk("痾...貌似没有#b#t5152028##k。");
	    }
	}
	cm.safeDispose();
    }
}