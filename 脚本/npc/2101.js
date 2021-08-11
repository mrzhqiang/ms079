/* Author: Xterminator
	NPC Name: 		Heena
	Map(s): 		Maple Road : Lower level of the Training Camp (2)
	Description: 		Takes you outside of Training Camp
*/
var status = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (status >= 0 && mode == 0) {
	cm.sendOk("没完成新手训验嘛? 如果想要离开这里, 请不要吝啬的告诉我。.");
	cm.dispose();
	return;
    }
    if (mode == 1)
	status++;
    else
	status--;
    if (status == 0) {
	cm.sendYesNo("你完成你的训练了嘛? 如果你想要离开的话，我可以带你离开。");
    } else if (status == 1) {
	cm.sendNext("那我要带你离开这里， 加油！");
    } else if (status == 2) {
	cm.warp(3, 0);
	cm.dispose();
    }
}