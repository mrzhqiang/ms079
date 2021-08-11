function start() {
    status = -1;

    action(1, 0, 0);
}
function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    }
    else {
        if (status >= 0 && mode == 0) {

            cm.sendOk("感谢你的光临！");
            cm.dispose();
            return;
        }
        if (mode == 1) {
            status++;
        }
        else {
            status--;
        }
        if (status == 0) {
			if(cm.getJob() >= 0 && cm.getJob()<= 522 && cm.hasSkill(1017) == false){
			cm.teachSkill(1017,1,1);
			}else if(cm.getJob() >=1000 && cm.getJob() <= 1512 && cm.hasSkill(10001019) == false){
			cm.teachSkill(10001019,1,1);
			}else if(cm.getJob() >=2000 && cm.getJob() <= 2112 && cm.hasSkill(20001019) == false){
			cm.teachSkill(20001019,1,1);
			}
            var tex2 = "";
            var text = "";
            for (i = 0; i < 10; i++) {
                text += "";
            }
			//显示物品ID图片用的代码是  #v这里写入ID#
            text += "#e#d皇家骑宠抽奖NPC欢迎您的光临！\r\n使用方法：抽取之后，请点击自己技能栏，新手技能栏有白雪人头像技能，使用技能查看抽取坐骑外貌，再次兑换变换其中，随机到自己喜欢的，就不必随机了，永久使用，下骑宠右键点击BUFF图标即可。皇家骑宠一共有300多种不一样的坐骑，总有一种是你喜欢的。#l\r\n\r\n"//3
            text += "#L1##r抽取皇家坐骑 需求：#v4001215x1##l\r\n"//3
            text += "#L2##r购买#v4001215#1张   2000点卷#l\r\n"//3
            text += "#L3##r购买#v4001215#10张  19000点卷#l\r\n"//3
            text += "#L4##r购买#v4001215#100张 180000点卷#l\r\n\r\n"//3
            cm.sendSimple(text);
        } else if (selection == 1) {
			if(cm.haveItem(4001215,1)){
            var 随机 = Math.ceil(Math.random()* 316);
			if(随机 == 0 || 随机 == 222 || 随机 == 258 || 随机 == 216){
				随机 = 1;
			}
			cm.setskillzq(随机);
            cm.sendOk("抽取成功！\r\n请你打开你的技能面板的新手技能使用！");
            cm.gainItem(4001215, -1);
			cm.worldMessage(6,"玩家：["+cm.getName()+"]在豆豆屋NPC处抽取[皇家骑宠]会是什么精美骑宠呢？你也来试试吧，百种坐骑，完爆你眼球！");
            cm.dispose();
			}else{
            cm.sendOk("您的材料不足！");
            cm.dispose();
			}
        } else if (selection == 2) {
			 if (cm.canHold(4001215,1) && cm.getPlayer().getCSPoints(1) < 2000) { // Not Party Leader
                    cm.sendOk("你的点卷不足2000点，请足够后再来");
                    cm.dispose();
                } else {
			cm.gainNX(-2000);
            cm.gainItem(4001215, 1);
            cm.sendOk("购买成功！");
			cm.worldMessage(6,"玩家：["+cm.getName()+"]1张购买了皇家骑宠抽奖卷！");
            cm.dispose();
			}
        } else if (selection == 3) {
			 if (cm.canHold(4001215,10) && cm.getPlayer().getCSPoints(1) < 19000) { // Not Party Leader
                    cm.sendOk("你的点卷不足19000点，请足够后再来");
                    cm.dispose();
                } else {
			cm.gainNX(-19000);
            cm.gainItem(4001215, 10);
            cm.sendOk("购买成功！");
			cm.worldMessage(6,"玩家：["+cm.getName()+"]10张购买了皇家骑宠抽奖卷！");
            cm.dispose();
			}
        } else if (selection == 4) {
			 if (cm.canHold(4001215,100) && cm.getPlayer().getCSPoints(1) < 180000) { // Not Party Leader
                    cm.sendOk("你的点卷不足180000点，请足够后再来");
                    cm.dispose();
                } else {
			cm.gainNX(-180000);
            cm.gainItem(4001215, 100);
            cm.sendOk("购买成功！");
			cm.worldMessage(6,"玩家：["+cm.getName()+"]100张购买了皇家骑宠抽奖卷！");
            cm.dispose();
			}
		}
    }
}


