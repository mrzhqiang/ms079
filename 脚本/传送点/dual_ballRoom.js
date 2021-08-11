function enter(pi) {
    if (pi.getQuestStatus(2363) == 1 && !pi.haveItem(4032616)) { //too lazy to do the map shit
	pi.gainItem(4032616,1);
    }
}