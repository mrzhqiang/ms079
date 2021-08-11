var status = 0;


function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == -1 || mode == 0) {
		cm.dispose();
	} else {
		status++;
		if (status == 0) {
			cm.sendSimple("嗨，我是#p9000003#，需要什么帮忙吗？？\r\n#L0#我拿到宝箱了。");
		} else if (status == 1) {
			if(cm.haveItem(4031017)){
			cm.removeAll(4031017);
    		cm.warp(109050000, 0);
			cm.dispose();
			} else {
			cm.sendOk("你没有#b#t4031017##k，你来找我做什么？？");
			cm.dispose();
			}
			cm.dispose();
		}
	}
}

function getEimForGuild(em, id) {
        var stringId = "" + id;
        return em.getInstance(stringId);
}