/* Romi
	Orbis Skin Change.
*/
var status = -1;
var skin = [0, 1, 2, 3, 4];

function action(mode, type, selection) {
    if (mode == 0) {
	cm.dispose();
	return;
    } else {
	status++;
    }

    if (status == 0) {
	cm.sendNext("嗨, 我是#p2012008# 如果你有一张 #b#t5153001##k, 我可以帮你美容皮肤！");
    } else if (status == 1) {
	cm.askAvatar("选择一个你想要美容的皮肤~",5153001, skin);
    } else if (status == 2){
	if (cm.setAvatar(5153001, skin[selection]) == 1) {
	    cm.sendOk("享受!");
	} else {
	    cm.sendOk("您貌似没有#b#t5153001##k..");
	}
	cm.safeDispose();
    }
}