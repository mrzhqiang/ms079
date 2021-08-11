function enter(pi) {
    if (pi.getQuestStatus(20021) == 0) {
	pi.playerSummonHint(true);
	pi.summonMsg("歡迎來到楓之谷的世界! 我的名字是 提酷, 我會是你的指導老師！ 我會在這裡回答你的問題，並指導你直到等級10等，成為騎士團之前如果你有任何疑問，可以點擊我！");
//	pi.forceCompleteQuest(20100);
	pi.forceCompleteQuest(20021);
    }
}