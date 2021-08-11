var maps = Array(104010200, 102020500, 103030400, 101040300, 102040600, 120030500, 200080000, 211040000, 200010302, 220011000, 222010310, 220040200, 221040400, 260010500, 261010003, 261020500, 250010504, 251010500, 240010200, 240010600, 240010500, 240020200, 240010901, 211040500, 240040401, 230020100);

function action(mode, type, selection) {
    for (var i = 0; i < maps.length; i++) {
	if (cm.getPlayer().getMapId() == maps[i]) {
	    var toMap = (i == 25 ? 806240100 : (806000000 + (i * 10000))); //seruf is weird lol
	    if (i == 14 && cm.isQuestActive(20696)) {
		toMap = 806140100;
	    } else if (i == 14 && cm.isQuestActive(20697)) {
		toMap = 806140200;
	    }
	    var marr = cm.getQuestRecord(122600 + i);
	    if (marr.getCustomData() == null) {
		marr.setCustomData("0");
	    }
	    var time = parseInt(marr.getCustomData());
	    if (time + (10 * 60000) > cm.getCurrentTime()) { //10 mins lewl
		cm.sendOk("You can enter in " + (((time + (30 * 60000) - cm.getCurrentTime()) / 60000) | 0) + " min.");
	    } else if (cm.getMap(toMap).getCharactersThreadsafe().size() == 0 && (cm.getMap(toMap + 1) == null || cm.getMap(toMap + 1).getCharactersThreadsafe().size() == 0)) {
		cm.getMap(toMap).resetFully();
		cm.warp(toMap, 0);
		marr.setCustomData(cm.getCurrentTime() + "");
	    } else {
		cm.sendOk("Please try again later, there exists some characters fighting the boss.");
	    }
	    cm.dispose();
	    return;
	}
    }
    cm.dispose();
}