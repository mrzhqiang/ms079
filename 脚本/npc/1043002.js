/* Author: aaroncsn (MapleSea Like)
	NPC Name: 		Mike
	Map(s): 		Warning Street: Perion Dungeon Entrance(106000300)
	Description: 		Unknown
*/

function start(){
	if (cm.getQuestStatus(2358) == 1) { //too lazy
		cm.forceCompleteQuest(2358);
	}
	cm.dispose();
}