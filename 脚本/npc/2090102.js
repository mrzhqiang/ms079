/* Romi
	Orbis Skin Change.
*/
var status = -1;
var skin = [0, 1, 2, 3, 4];

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
	cm.sendNext("嗨，我是#p2090102# 如果你有 #b#t5153006##k 我可以帮助你");
    } else if (status == 1) {
	cm.askAvatar("选择一个想要的。", skin);
    } else if (status == 2){
	if (cm.setAvatar(5153006, skin[selection]) == 1) {
	    cm.sendOk("享受!");
	} else {
	    cm.sendOk("你好像没有#b#t5153006##k");
	}
	cm.dispose();
    }
}