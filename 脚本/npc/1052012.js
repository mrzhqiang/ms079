/*
	Mong from Kong - Victoria Road : Kerning City (103000000)
*/

function start() {
    cm.sendYesNo("你是不是通过网吧上网？如果是的话，那就去这儿吧…你可能会去一个熟悉的地方。你觉得怎么样？你想进去吗?");
}

function action(mode, type, selection) {
    if (mode == 0) {
	cm.sendNext("你一定很忙吧？但如果你登录的网吧，那么你应该尝试去。你可能会在一个陌生的地方结束.");
    } else {
	if (cm.haveItem(5420007)) {
	    cm.warp(193000000, 0);
	} else {
	    cm.sendNext("嘿，嘿…我不认为你是在专属网吧上网。如果你正在家里上网，你就不能进入这个地方 ...");
	}
    }
    cm.dispose();
}