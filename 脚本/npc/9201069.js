/* V. Isage
	NLC VIP Eye Change.
*/
var status = -1;
var facetype;

function action(mode, type, selection) {
    if (mode == 0) {
	cm.dispose();
	return;
    } else {
	status++;
    }

    if (status == 0) {
	cm.sendNext("嗨，我是#p9201069# 如果您有#r#t5252034##k 那么我可以帮助您~");
    } else if (status == 1) {
	var face = cm.getPlayerStat("FACE");

	if (cm.getPlayerStat("GENDER") == 0) {
	    facetype = [20000, 20001, 20002, 20003, 20004, 20005, 20006, 20007, 20008, 20012];
	} else {
	    facetype = [21001, 21002, 21003, 21004, 21005, 21006, 21008, 21012, 21014, 21016];
	}
	for (var i = 0; i < facetype.length; i++) {
	    facetype[i] = facetype[i] + face % 1000 - (face % 100);
	}
	cm.askAvatar("选择一个喜欢的", facetype);

    } else if (status == 2){
	if (cm.setAvatar(5152034, facetype[selection]) == 1) {
	    cm.sendOk("享受！");
	} else {
	    cm.sendOk("痾...貌似没有#b#t5152034##k");
	}
	cm.safeDispose();
    }
}