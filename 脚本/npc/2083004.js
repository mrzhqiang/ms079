function start() {
	if (cm.getPlayer().getClient().getChannel() == 3) {
        cm.dispose();
        cm.openNpc(2083004, 1);
    } else {
        cm.sendOk("只有在3频道才可以参加黑龙远征队");
        cm.dispose();
    }
}