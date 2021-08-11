 /* 
	NPC Name: 		Divine Bird
	Map(s): 		Erev
	Description: 		Buff
*/

function start() {
    cm.useItem(2022458);
    cm.sendOk("不要停止训练，这个世界需要你来守护。");
}

function action(mode, type, selection) {
    cm.dispose();
}