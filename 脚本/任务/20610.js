/*
 * Cygnus Skill -
 */

var status = -1;

function start(mode, type, selection) {
    status++;

    if (status == 0) {
	qm.askAcceptDecline("你有没有熟练你的技能了呢？我相信你已经掌握了所有技能使用的方法，接下来我将再传授一招#b最终技能#k给你。");
    } else if (status == 1) {
	if (mode == 0) {
	    qm.sendOk("好吧，你在做什么，现在不会使你看起来像有人说的谦虚。你只要看看由自满这样做，这是从来没有一个好东西。");
	} else {
	    qm.forceStartQuest();
	}
	qm.dispose();
    }
}

function end(mode, type, selection) {
    qm.dispose();
}