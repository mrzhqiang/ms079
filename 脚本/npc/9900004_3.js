/* global cm */

var 正在进行中 = "#fUI/UIWindow/Quest/Tab/enabled/1#";
var 完成 = "#fUI/UIWindow/Quest/Tab/enabled/2#";
var 正在进行中蓝 = "#fUI/UIWindow/MonsterCarnival/icon1#";
var 完成红 = "#fUI/UIWindow/MonsterCarnival/icon0#";
function start() {
    status = -1;

    action(1, 0, 0);
}
function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (status >= 0 && mode == 0) {
            cm.sendOk("感谢你的光临！");
            cm.dispose();
            return;
        }
        if (mode == 1) {
            status++;
        } else {
            status--;
        }
        if (status == 0) {
            var tex2 = "";
            var text = "";
            for (i = 0; i < 10; i++) {
                text += "";
            }
            text += "\t\t\t  #e#d欢迎来到#b" + cm.ms() + "#e#d\r\n\t注意事项：系统记录外挂请不要报着侥幸心理开。\r\n\r\n"

            if (cm.getPlayer().getLevel() >= 20 && cm.getPlayer().getdjjl() == 0) {
                text += "#L1##r" + 完成红 + "等级达到20级！" + 完成 + "抵用卷x1000 限时：1天#l\r\n\r\n\r\n"
            } else if (cm.getPlayer().getLevel() >= 20 && cm.getPlayer().getdjjl() > 0) {
                text += "" + 完成红 + "#r等级达到20级！#l" + 完成 + "\r\n\r\n"
            } else {
                text += "" + 正在进行中蓝 + "#r等级达到20级！#l" + 正在进行中 + "\r\n"
            }
            
            if (cm.getPlayer().getLevel() >= 30 && cm.getPlayer().getdjjl() == 1) {
                text += "#L2##r" + 完成红 + "等级达到30级！" + 完成 + "抵用卷x1000 限时：1天#l\r\n\r\n\r\n"
            } else if (cm.getPlayer().getLevel() >= 30 && cm.getPlayer().getdjjl() > 1) {
                text += "" + 完成红 + "#r等级达到30级！#l" + 完成 + "\r\n\r\n"
            } else {
                text += "" + 正在进行中蓝 + "#r等级达到30级！#l" + 正在进行中 + "\r\n"
            }
            
            if (cm.getPlayer().getLevel() >= 50 && cm.getPlayer().getdjjl() == 2) {
                text += "#L3##r" + 完成红 + "等级达到50级！" + 完成 + "抵用卷x1000 限时：1天#l\r\n\r\n\r\n"
            } else if (cm.getPlayer().getLevel() >= 50 && cm.getPlayer().getdjjl() > 2) {
                text += "" + 完成红 + "#r等级达到50级！#l" + 完成 + "\r\n\r\n"
            } else {
                text += "" + 正在进行中蓝 + "#r等级达到50级！#l" + 正在进行中 + "\r\n"
            }
            
            if (cm.getPlayer().getLevel() >= 70 && cm.getPlayer().getdjjl() == 3) {
                text += "#L4##r" + 完成红 + "等级达到70级！" + 完成 + "抵用卷x1000 限时：1天#l\r\n\r\n\r\n"
            } else if (cm.getPlayer().getLevel() >= 70 && cm.getPlayer().getdjjl() > 3) {
                text += "" + 完成红 + "#r等级达到70级！#l" + 完成 + "\r\n\r\n"
            } else {
                text += "" + 正在进行中蓝 + "#r等级达到70级！#l" + 正在进行中 + "\r\n"
            }
            
            if (cm.getPlayer().getLevel() >= 100 && cm.getPlayer().getdjjl() == 4) {
                text += "#L5##r" + 完成红 + "等级达到100级！" + 完成 + "抵用卷x1000 限时：1天#l\r\n\r\n\r\n"
            } else if (cm.getPlayer().getLevel() >= 100 && cm.getPlayer().getdjjl() > 4) {
                text += "" + 完成红 + "#r等级达到100级！#l" + 完成 + "\r\n\r\n"
            } else {
                text += "" + 正在进行中蓝 + "#r等级达到100级！#l" + 正在进行中 + "\r\n"
            }
            
            if (cm.getPlayer().getLevel() >= 120 && cm.getPlayer().getdjjl() == 5) {
                text += "#L6##r" + 完成红 + "等级达到120级！" + 完成 + "抵用卷x1000 限时：1天#l\r\n\r\n\r\n"
            } else if (cm.getPlayer().getLevel() >= 120 && cm.getPlayer().getdjjl() > 5) {
                text += "" + 完成红 + "#r等级达到120级！#l" + 完成 + "\r\n\r\n"
            } else {
                text += "" + 正在进行中蓝 + "#r等级达到120级！#l" + 正在进行中 + "\r\n"
            }
            
            if (cm.getPlayer().getLevel() >= 150 && cm.getPlayer().getdjjl() == 6) {
                text += "#L7##r" + 完成红 + "等级达到150级！" + 完成 + "抵用卷x1000 限时：1天#l\r\n\r\n\r\n"
            } else if (cm.getPlayer().getLevel() >= 150 && cm.getPlayer().getdjjl() > 6) {
                text += "" + 完成红 + "#r等级达到150级！#l" + 完成 + "\r\n\r\n"
            } else {
                text += "" + 正在进行中蓝 + "#r等级达到150级！#l" + 正在进行中 + "\r\n"
            }
            
            if (cm.getPlayer().getLevel() >= 170 && cm.getPlayer().getdjjl() == 7) {
                text += "#L8##r" + 完成红 + "等级达到170级！" + 完成 + "抵用卷x1000 限时：1天#l\r\n\r\n\r\n"
            } else if (cm.getPlayer().getLevel() >= 170 && cm.getPlayer().getdjjl() > 7) {
                text += "" + 完成红 + "#r等级达到170级！#l" + 完成 + "\r\n\r\n"
            } else {
                text += "" + 正在进行中蓝 + "#r等级达到170级！#l" + 正在进行中 + "\r\n"
            }
            
            if (cm.getPlayer().getLevel() >= 180 && cm.getPlayer().getdjjl() == 8) {
                text += "#L9##r" + 完成红 + "等级达到180级！" + 完成 + "抵用卷x1000 限时：1天#l\r\n\r\n\r\n"
            } else if (cm.getPlayer().getLevel() >= 180 && cm.getPlayer().getdjjl() > 8) {
                text += "" + 完成红 + "#r等级达到180级！#l" + 完成 + "\r\n\r\n"
            } else {
                text += "" + 正在进行中蓝 + "#r等级达到180级！#l" + 正在进行中 + "\r\n"
            }
            
            if (cm.getPlayer().getLevel() >= 190 && cm.getPlayer().getdjjl() == 9) {
                text += "#L10##r" + 完成红 + "等级达到190级！" + 完成 + "抵用卷x1000 限时：1天#l\r\n\r\n\r\n"
            } else if (cm.getPlayer().getLevel() >= 190 && cm.getPlayer().getdjjl() > 9) {
                text += "" + 完成红 + "#r等级达到190级！#l" + 完成 + "\r\n\r\n"
            } else {
                text += "" + 正在进行中蓝 + "#r等级达到190级！#l" + 正在进行中 + "\r\n"
            }
            
            if (cm.getPlayer().getLevel() >= 199 && cm.getPlayer().getdjjl() == 10) {
                text += "#L11##r" + 完成红 + "等级达到199级！" + 完成 + "抵用卷x1000 限时：1天#l\r\n\r\n\r\n"
            } else if (cm.getPlayer().getLevel() >= 199 && cm.getPlayer().getdjjl() > 10) {
                text += "" + 完成红 + "#r等级达到199级！#l" + 完成 + "\r\n\r\n"
            } else {
                text += "" + 正在进行中蓝 + "#r等级达到199级！#l" + 正在进行中 + "\r\n"
            }
            
            if (cm.getPlayer().getLevel() >= 200 && cm.getPlayer().getdjjl() == 11) {
                text += "#L12##r" + 完成红 + "等级达到200级！" + 完成 + "抵用卷x1000 限时：1天#l\r\n\r\n\r\n"
            } else if (cm.getPlayer().getLevel() >= 200 && cm.getPlayer().getdjjl() > 11) {
                text += "" + 完成红 + "#r等级达到200级！#l" + 完成 + "\r\n\r\n"
            } else {
                text += "" + 正在进行中蓝 + "#r等级达到200级！#l" + 正在进行中 + "\r\n"
            }
            
            cm.sendSimple(text);
        } else if (selection == 1) {
	if(cm.getPlayer().getLevel() >= 20 && cm.getPlayer().getdjjl() == 0){
	    cm.gainDY(1000);
            cm.setdjjl(1);
            cm.sendOk("领取奖励成功！");
            cm.喇叭(1, "玩家：" + cm.getPlayer().getName() + "，等级达到20级，领取了抵用卷x1000点哦~!");
            cm.dispose();
	}else{
	    cm.sendOk("你已经领取过了");
	}
        } else if (selection == 2) {
	if(cm.getPlayer().getLevel() >= 30 && cm.getPlayer().getdjjl() == 1){
	    cm.gainDY(1000);
            cm.setdjjl(2);
            cm.sendOk("领取奖励成功！");
            cm.喇叭(1, "玩家：" + cm.getPlayer().getName() + "，等级达到30级，领取了抵用卷x1000点哦~!");
            cm.dispose();
	}else{
	    cm.sendOk("你已经领取过了");
	}
        } else if (selection == 3) {
	if(cm.getPlayer().getLevel() >= 50 && cm.getPlayer().getdjjl() == 2){
	    cm.gainDY(1000);
            cm.setdjjl(3);
            cm.sendOk("领取奖励成功！");
            cm.喇叭(1, "玩家：" + cm.getPlayer().getName() + "，等级达到50级，领取了抵用卷x1000点哦~!");
            cm.dispose();
	}else{
	    cm.sendOk("你已经领取过了");
	}
        } else if (selection == 4) {
	if(cm.getPlayer().getLevel() >= 70 && cm.getPlayer().getdjjl() == 3){
	    cm.gainDY(1000);
            cm.setdjjl(4);
            cm.sendOk("领取奖励成功！");
            cm.喇叭(1, "玩家：" + cm.getPlayer().getName() + "，等级达到70级，领取了抵用卷x1000点哦~!");
            cm.dispose();
	}else{
	    cm.sendOk("你已经领取过了");
	}
        } else if (selection == 5) {
	if(cm.getPlayer().getLevel() >= 100 && cm.getPlayer().getdjjl() == 4){
	    cm.gainDY(1000);
            cm.setdjjl(5);
            cm.sendOk("领取奖励成功！");
            cm.喇叭(1, "玩家：" + cm.getPlayer().getName() + "，等级达到100级，领取了抵用卷x1000点哦~!");
            cm.dispose();
	}else{
	    cm.sendOk("你已经领取过了");
	}
        } else if (selection == 6) {
	if(cm.getPlayer().getLevel() >= 120 && cm.getPlayer().getdjjl() == 5){
	    cm.gainDY(1000);
            cm.setdjjl(6);
            cm.sendOk("领取奖励成功！");
            cm.喇叭(1, "玩家：" + cm.getPlayer().getName() + "，等级达到120级，领取了抵用卷x1000点哦~！");
            cm.dispose();
	}else{
	    cm.sendOk("你已经领取过了");
	}
        } else if (selection == 7) {
	if(cm.getPlayer().getLevel() >= 150 && cm.getPlayer().getdjjl() == 6){
            cm.gainDY(1000);
            cm.setdjjl(7);
            cm.sendOk("领取奖励成功！");
            cm.喇叭(1, "玩家：" + cm.getPlayer().getName() + "，等级达到150级，领取了抵用卷x1000点哦~！");
            cm.dispose();
	}else{
	    cm.sendOk("你已经领取过了");
	}
        } else if (selection == 8) {
	if(cm.getPlayer().getLevel() >= 170 && cm.getPlayer().getdjjl() == 7){
            cm.gainDY(1000);
            cm.setdjjl(8);
            cm.sendOk("领取奖励成功！");
            cm.喇叭(1, "玩家：" + cm.getPlayer().getName() + "，等级达到170级，领取了抵用卷x1000点哦~！");
            cm.dispose();
	}else{
	    cm.sendOk("你已经领取过了");
	}
        } else if (selection == 9) {
	if(cm.getPlayer().getLevel() >= 180 && cm.getPlayer().getdjjl() == 8){
	    cm.gainDY(1000);
            cm.setdjjl(9);
            cm.sendOk("领取奖励成功！");
            cm.喇叭(1, "玩家：" + cm.getPlayer().getName() + "，等级达到180级，领取了抵用卷x1000点哦~！");
            cm.dispose();
	}else{
	    cm.sendOk("你已经领取过了");
	}
        } else if (selection == 10) {
	if(cm.getPlayer().getLevel() >= 190 && cm.getPlayer().getdjjl() == 9){
	    cm.gainDY(1000);
            cm.setdjjl(10);
            cm.sendOk("领取奖励成功！");
            cm.喇叭(1, "玩家：" + cm.getPlayer().getName() + "，等级达到190级，领取了抵用卷x1000点哦~！");
            cm.dispose();
	}else{
	    cm.sendOk("你已经领取过了");
	}
        } else if (selection == 11) {
	if(cm.getPlayer().getLevel() >= 199 && cm.getPlayer().getdjjl() == 10){
	    cm.gainDY(1000);
            cm.setdjjl(11);
            cm.sendOk("领取奖励成功！");
            cm.喇叭(1, "玩家：" + cm.getPlayer().getName() + "，等级达到199级，领取了抵用卷x1000点哦~！");
            cm.dispose();
	}else{
	    cm.sendOk("你已经领取过了");
	}
        } else if (selection == 12) {
	if(cm.getPlayer().getLevel() == 200 && cm.getPlayer().getdjjl() == 11){
            cm.gainDY(10000);
            cm.setdjjl(12);
            cm.sendOk("领取奖励成功！");
            cm.喇叭(1, "玩家：" + cm.getPlayer().getName() + "，等级达到200级，领取了抵用卷1万点！");
            cm.dispose();
	}else{
	    cm.sendOk("你已经领取过了");
	}
         
        }
    }
}


