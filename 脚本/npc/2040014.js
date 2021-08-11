var status = -1;
var s = 0;
var t = 0;

function start() {
    action(1, 0, 0);
}

function action(mode, type, selection) {
    var c = cm.getPlayer();
	if(!c.isVip()){
	cm.sendNext("欢迎来到屁屁股v113");
	cm.dispose();
	return
	}
    var VipMedal = c.getVipMedal() ? "#r显示" : "#b未显示";
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) {
            cm.sendSimple("#b本NPC提供选择您脚下显示的东西\r\n#rPS: 别人看您也如此显示\r\n" +
                    "#g#L1#VIP勋章	  : " + VipMedal + "\r\n" +
					"#d#L4#设定完成");
        } else if (status == 1) {
            s = selection;
            if (s == 1) {
                t = c.getVipMedal();
                if (t == true) {
                    t = false;
                } else {
                    t = true;
                }
                c.setVipMedat(t);
				cm.dispose();
                cm.openNpc(cm.getNpc());
            } else {
                c.fakeRelog();
                cm.dispose();
            }
        }
    }
}
