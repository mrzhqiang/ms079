/* guild emblem npc */
var status = 0;
var sel;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 0 && status == 0) {
	cm.dispose();
	return;
    }
    if (mode == 1)
	status++;
    else
	status--;

    if (status == 0)
	cm.sendSimple("你想要做什么？\r\n#b#L0#创建/更改公会徽章#l#k");
    else if (status == 1) {
	sel = selection;
	if (selection == 0) {
	    if (cm.getPlayerStat("GRANK") == 1)
		cm.sendYesNo("重新打造一个徽章需要 #b5,000,000金币#k，你确定要继续吗？");
	    else
		cm.sendOk("打造公会徽章需要公会长来找我才行喔，请你们的公会长来找我吧~");
	}
				
    } else if (status == 2) {
	if (sel == 0) {
	    cm.genericGuildMessage(17);
	    cm.dispose();
	}
    }
}
