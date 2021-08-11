/* Author: Xterminator
	NPC Name: 		Trainer Frod
	Map(s): 		Victoria Road : Pet-Walking Road (100000202)
	Description: 		Pet Trainer
*/
var status = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (status >= 0 && mode == 0) {
	cm.dispose();
	return;
    }
    if (mode == 1)
	status++;
    else
	status--;
    if (status == 0) {
	if (cm.haveItem(4031035)) {
	    cm.sendNext("嗯，这是我哥哥的信！也许骂我想我不工作和东西......嗯？唉唉......你跟着我哥哥的意见和训练有素的宠物，站起身来这里，是吧？不错！既然你辛辛苦苦来到这里，我会提高你的亲密水平与您的宠物。");
	} else {
	    cm.sendOk("我哥哥告诉我，照顾宠物的障碍，当然，但是......既然我这么远从他身上，我不禁想游手好闲......呵呵，因为我没有看到他在眼前，还不如干脆放松几分钟.");
	    cm.dispose();
	}
    } else if (status == 1) {
	if (cm.getPlayer().getPet(0) == null) {
	    cm.sendNextPrev("嗯...你真的没有带宠物来？快离开这儿！");
	} else {
	    cm.gainItem(4031035, -1);
	    cm.gainClosenessAll(2);
	    cm.sendNextPrev("你怎么看？难道你不认为你已经得到了你的宠物更接近？如果你有时间，再训练你的宠物在这个障碍......当然当然，我的兄弟的许可。");
	}
	cm.dispose();
    }
}