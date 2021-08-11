var status = -1;
var job = 0;
var type = -1;
var skill = [[1004],[10001004],[20001004]];
var skill2 = [[8],[10000018],[20000024]];

function start(){
	action(1, 0, 0);
}

function action(mode, type ,selection) {
	if(mode == 0 && status == 0) {
		status --;
	} else if(mode == 1) {
		status ++;
	} else {
		cm.dispose();
		return;
	}
	
	if (status == 0) {//-锻造#s1007#-皇家骑宠技能#s1013#
		cm.sendOk("到达等级30，在我这里可以帮你一键学会骑宠和群宠技能哦！\r\n#L1#我想学习骑宠技能(需要10000000金币)\r\n\r\n#L2#我想学习群宠技能(需要10000000金币)");
	} else if (selection == 1){
		if(cm.getPlayer().getLevel() < 30){
			cm.sendNext("你的等级没有达到30级");
			cm.dispose();
		} else if(cm.getMeso() < 10000000){
			cm.sendNext("你的金币不足1千万\r\n你当前拥有"+ cm.getMeso() +"金币");
			cm.dispose();
			return;
		}
		cm.gainMeso(-10000000);
		job = cm.getPlayer().getJob();
		if (job < 1000){// Adv(0 ~ 522)
			type = 0;
		} else if (job < 2000) {// Cy(1000 ~ 1512)
			type = 1;
		} else if (job < 3000) {// Aran(2000 ~ 2112)
			type = 2;
		} else {
			cm.dispose();
			return;
		}
		for(var i = 0; i < skill[type].length;i++){
			var level = 1;
			if(i == 2) {
				level = 3;
			}
			cm.teachSkill(skill[type][i], level);
		}
		cm.sendNext("骑兽技能已经学习成功了！");
		cm.dispose();
	} else if (selection == 2){
		if(cm.getPlayer().getLevel() < 30){
			cm.sendNext("你的等级没有达到30级");
			cm.dispose();
			return;
		}
		if(cm.getMeso() < 10000000){
			cm.sendNext("你的金币不足1千万\r\n你当前拥有"+ cm.getMeso() +"金币");
			cm.dispose();
			return;
		}
		cm.gainMeso(-10000000);
		job = cm.getPlayer().getJob();
		if (job < 1000){// Adv(0 ~ 522)
			type = 0;
		} else if (job < 2000) {// Cy(1000 ~ 1512)
			type = 1;
		} else if (job < 3000) {// Aran(2000 ~ 2112)
			type = 2;
		} else {
			cm.dispose();
			return;
		}
		for(var i = 0; i < skill2[type].length;i++){
			var level = 1;
			if(i == 2) {
				level = 3;
			}
			cm.teachSkill(skill2[type][i], level);
		}
		cm.sendNext("群宠技能已经学习成功了！");
		cm.dispose();

	} else {
		cm.dispose();
	}
}