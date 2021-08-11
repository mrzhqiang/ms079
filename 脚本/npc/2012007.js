/* Rinz the assistant
	Orbis Random Hair/Hair Color Change.
*/
var status = -1;
var beauty = 0;
var hair_Colo_new;

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
	cm.sendSimple("我是利纳斯助手. 请问你有 #b#t5150004##k or #b#t5151004##k 嘛? 如果是这样，您考虑让我如何把你的头发护理？ 你想要怎样护理你的头发? \r\n#L0#使用: #i5150004##t5150004##l \r\n#L1#使用: #i5151004##t5151004##l");
    } else if (status == 1) {
	if (selection == 0) {
	    var hair = cm.getPlayerStat("HAIR");
	    hair_Colo_new = [];
	    beauty = 1;

	    if (cm.getPlayerStat("GENDER") == 0) {
		hair_Colo_new = [30030, 30020, 30000, 30270, 30230, 30260, 30280, 30240, 30290, 30340, 30370, 30630, 30530];
	    } else {
		hair_Colo_new = [31040, 31000, 31250, 31220, 31260, 31240, 31110, 31270, 31030, 31230, 31530, 31710, 31320, 31650, 31630];
	    }
	    for (var i = 0; i < hair_Colo_new.length; i++) {
		hair_Colo_new[i] = hair_Colo_new[i] + (hair % 10);
	    }
	    cm.sendYesNo("这是随机是否要使用 #b#t5150004##k ？？");
	} else if (selection == 1) {
	    var currenthaircolo = Math.floor((cm.getPlayerStat("HAIR") / 10)) * 10;
	    hair_Colo_new = [];
	    beauty = 2;

	    for (var i = 0; i < 8; i++) {
		hair_Colo_new[i] = currenthaircolo + i;
	    }
	    cm.sendYesNo("这是随机是否要使用 #b#t5151004##k ？？");
	}
    } else if (status == 2){
	if (beauty == 1) {
	    if (cm.setRandomAvatar(5150004, hair_Colo_new) == 1) {
		cm.sendOk("享受!");
	    } else {
		cm.sendOk("您貌似没有#b#t5150004##k..");
	    }
	} else {
	    if (cm.setRandomAvatar(5151004, hair_Colo_new) == 1) {
		cm.sendOk("享受!");
	    } else {
		cm.sendOk("您貌似没有#b#t5151004##k..");
	    }
	}
	cm.dispose();
    }
}
