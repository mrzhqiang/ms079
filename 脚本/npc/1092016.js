/* Author: aaroncsn (MapleSea Like)
	NPC Name: 		Glittering Stone
	Map(s): 		Nautilus:Power Source Chamber(120000301)
	Description: 		Unknown
*/

function start() {
    if(cm.getQuestStatus(2166) == 1) {
        cm.sendNext("这是一个美丽的，闪亮的岩石。我能感觉到它周围的神秘力量。");
		cm.forceCompleteQuest(2166);
    } else
        cm.sendNext("我用我的手摸了摸发亮的岩石，我感到一种神秘的力量流进我的身体。");
    cm.dispose();
}