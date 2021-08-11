/* Miranda
	NLC Skin Change.
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
	cm.sendNext("嗨，我是#p9201065# 如果您有5153009 那我可以帮助您~");
    } else if (status == 1) {
	cm.askAvatar("选择一个喜欢的", skin);
    } else if (status == 2){
	if (cm.setAvatar(5153009, skin[selection]) == 1) {
	    cm.sendOk("享受！");
	} else {
	    cm.sendOk("痾...貌似没有#b#t5153009##k");
	}

	cm.dispose();
    }
}