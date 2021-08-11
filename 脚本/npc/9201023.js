/*
	NPC Name: 		Hera
	Map(s): 		Towns
	Description: 		Wedding Village Entrance
*/

var status = -1;

function start() {
    cm.sendSimple("啊~今天真是个好日子！这世界太美好了~！你不觉得这世界充满了爱吗？满溢婚礼村的爱意都流淌到这里来了~！ \n\r #b#L0# 我想要去结婚小镇.#l");
}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
		cm.sendOk("你居然要放弃这么好的机会？那里真的很美~。你不会是还没遇到心爱的人吧？没错，如果你有心爱的人，怎么会对这么浪漫的消息听而不闻呢！！");
        cm.dispose();
        return;
    }
    if (status == 0) {
		cm.sendNext("哦！多么美好的一天！这个世界是多么的美好～！这个世界似乎是充满爱的，不是吗？我可以从这里感受到爱的精神填补了婚礼!");
    } else if (status == 1) {
        cm.sendYesNo("你曾经去过的婚礼村庄？这是一个了不起的地方，爱情是无极限的。恩爱夫妻可以结婚还有，如何浪漫它是什么？如果你想在那里，我会告诉你的方式.");
    } else if (status == 2) {
        cm.sendNext("你做了一个正确的决定！你可以感受到爱的精神在婚礼村发挥到淋漓尽致。当你想回来，你的目的地将在这里，所以不要担心.");
    } else if (status == 3) {
	   cm.saveLocation("AMORIA");
	   cm.warp(680000000, 0);
       cm.dispose();
		}
    }