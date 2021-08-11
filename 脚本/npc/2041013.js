/* Gina
	Ludibrium Skin Change.
*/
var status = -1;
var skin = Array(0, 1, 2, 3, 4);

function action(mode, type, selection) {
    if (mode == 0) {
	cm.dispose();
	return;
    } else {
	status++;
    }
    if (status == 0) {
	cm.sendNext("喔，嗨! 欢迎来到玩具城护肤中心! 你想要变性感吗?? 多么美丽, 雪白的皮肤?? 如果你有 #b#t5153002##k, 你可以跟我们谈谈你想要变得怎么样~");
    } else if (status == 1) {
	cm.askAvatar("选择一个想要的。",5153002, skin);
    } else if (status == 2){
	if (cm.setAvatar(5153002, skin[selection]) == 1) {
	    cm.sendOk("享受!");
	} else {
	    cm.sendOk("痾貌似没有#t5153002#");
	}
	cm.safeDispose();
    }
}