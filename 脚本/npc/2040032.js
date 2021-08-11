/*
	Weaver - Ludibrium : Ludibrium Pet Walkway (220000006)
**/

var status = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (status == 0 && mode == 0) {
	cm.dispose();
	return;
    } else if (status >= 1 && mode == 0) {
	cm.sendNext("需要的时候可以来找我。");
	cm.dispose();
	return;
    }
    if (mode == 1)
	status++;
    else
	status--;
    if (status == 0) {
	cm.sendYesNo("这是在路上，你可以去与你的宠物散步。你可以走动的，或者你可以训练你的宠物要经过这里的障碍。如果你不是太密切的与您的宠物然而，这可能会出现问题，他不会听从你的命令一样多......那么，你有什么感想？想培养你的宠物？");
    } else if (status == 1) {
	if (cm.haveItem(4031128)) {
	    cm.sendNext("拿到这一封信，跳跃过那些障碍把这封信给我弟弟他会给你奖励...");
	    cm.dispose();
	} else {
	    cm.gainItem(4031128, 1);
	    cm.sendOk("好运!");
	    cm.dispose();
	}
    }
}