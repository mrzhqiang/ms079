var status = 0;
var random = java.lang.Math.floor(Math.random() * 4);
function start() {
    status = -1;
    action(1, 0, 0);
}
function action(mode, type, selection) {
    if (status == 0 && mode == 0) {
        cm.sendOk("感谢支持#r冒险岛#k我们有独立开发的技术以及最前线的保障。谢谢各位支持！\r\n#b小赌怡情，打赌伤身，强赌灰飞烟灭~！");
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    } else {
        status--;
    }
    if (cm.getMapId() == 180000001) {
        cm.sendOk("很遗憾，您因为违反用户守则被禁止游戏活动，如有异议请联系管理员.")
        cm.dispose();
    }
    else if (status == 0) {
cm.teachSkill(1004,1,1);
        var selStr = "\t\t\t  你好~欢迎来到#b冒险岛#k.\r\n\t温馨提示: #r小赌怡情，大赌伤身，强赌灰飞烟灭~！。#n\r\n";
        selStr += "  #b#L0#老虎机100点卷[#rNEW#b]#l\r\n\r\n";
        selStr += "  #b#L1#老虎机1000点卷[#rNEW#b]#l\r\n\r\n"
        selStr += "  #b#L2#老虎机10000点卷[#rNEW#b]#l\r\n\r\n"
        cm.sendSimple(selStr);
    } else if (status == 1) {
        switch (selection) {
            case 0:
		if(cm.getNX(1) < 100){
        	cm.sendOk("你没有100点劵")
		}else{
                cm.getlfj(100,0);
		}

                cm.dispose();
                break;
            case 1:
		if(cm.getNX(1) < 1000){
        	cm.sendOk("你没有1000点劵")
		}else{
                cm.getlfj(1000,1);
		}
				cm.dispose();
                break;
            case 2:
		if(cm.getNX(1) < 10000){
        	cm.sendOk("你没有10000点劵")
		}else{
                cm.getlfj(10000,2);
		}
				cm.dispose();
                break;
        }
    }
}
