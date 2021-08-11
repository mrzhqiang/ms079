var status = -1;

function action(mode, type, selection) {
    if (mode == 1)
	status++;
    else
	status--;
    if (status == 0) {
        cm.sendOk("是人类吗？？没事的话赶紧离开这里吧！");
	cm.dispose();
    }
}