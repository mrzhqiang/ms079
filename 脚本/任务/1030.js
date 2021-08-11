var status = -1;

function start(mode, type, selection) {
    if (mode == -1) {
        qm.dispose();
    } else {
        if (mode == 1) {
            status++;
        } else {
            status--;
        }
        if (status == 0) {
            qm.sendOk("是位新的旅行者吧？还很陌生吧？我是玛丽亚，打开(快捷键W)就可以查看所有枫之谷世界的所有地图");
            qm.gainExp(11);
	    qm.forceCompleteQuest();
            qm.dispose();
        }
    }
}