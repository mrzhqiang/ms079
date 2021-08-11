function action(mode, type, selection) {
    cm.sendNext("Haha! FOOLS! I have betrayed you and have unsealed Rex, the Hoblin King!");
	if (cm.isLeader()) {
	    if (cm.getPlayer().getMap().getReactorByName("bossLex") != null) {
		cm.getPlayer().getMap().getReactorByName("bossLex").forceStartReactor(cm.getClient());
	    } else {
		cm.mapMessage("Rex has been summoned.");
		cm.spawnMonster(9300281,1);
	    }
	}
    cm.dispose();
}