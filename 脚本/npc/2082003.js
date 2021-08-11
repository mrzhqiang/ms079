/*
	NPC Name: 		Cobra - Retired dragon trainer
	Map(s): 		Leafre : Cabin
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
	cm.sendSimple("如果你有翅膀，我敢肯定，你可以去那里。但是，这本身并没有足够的。如果你想要飞，虽然风这比刀片锋利，你需要坚韧的尺度为好。我是唯一半身左边那个知道回来的路上......如果你想去那里，我可以改变你。不管你是什么，这一刻，你会成为一个 #b龙#k...\r\n #L0##b我想变成一只龙.#k#l");
    } else if (status == 1) {
	cm.useItem(2210016);
	cm.warp(200090500, 0);
	cm.dispose();
    }
}