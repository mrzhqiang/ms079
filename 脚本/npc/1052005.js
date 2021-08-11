/* Dr. Feeble
	Henesys Random Eye Change.
*/
var status = 0;
var beauty = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 0 && status == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    } else {
        status--;
    }
    if (status == 0) {
        cm.sendNext("想换换长相吗? 看来是想以新的容貌重新出生的样子? 好~ 我帮你如愿以偿.\r\n#L0##b 整形(使用射手村整形手术普通会员卡)#l\r\n\r\n\r\n#r 换眼睛颜色只是随机的哦【选中不一定代表就是那个颜色】#l\r\n#L1##b进行眼晴换色手术(#k使用#b射手村整形手术高级会员卡#k)#l");
    } else if (status == 1) {
		if (selection == 0) {
        var face = cm.getPlayerStat("FACE");
        var facetype;
        if (cm.getPlayerStat("GENDER") == 0) {
            facetype = [20000, 20001, 20002, 20003, 20004, 20005, 20006, 20007, 20008, 20012, 20014];
        } else {
            facetype = [21000, 21001, 21002, 21003, 21004, 21005, 21006, 21007, 21008, 21012, 21014];
        }
        for (var i = 0; i < facetype.length; i++) {
            facetype[i] = facetype[i] + face % 1000 - (face % 100);
        }
        if (cm.setRandomAvatar(5152001, facetype) == 1) {
            cm.sendOk("好了,你的朋友们一定认不出你了!");
        } else {
            cm.sendOk("呃...你没有我们医院射手村整形手术普通会员卡...不好意思无法帮你做整形手术噢。");
        }
		}else if(selection == 1){
        var face1 = cm.getPlayerStat("FACE");
				beauty = 1;
            var hair_Colo_new = [];
				if (cm.getChar().getGender() == 0 && face1 < 21000) {
					var current = face1 % 100 + 20000;
				}else if(cm.getChar().getGender() == 0){
					var current = face1 % 100 + 23000;
				}else if (cm.getChar().getGender() == 1 && face1 >= 21000 && face1 < 23000) {
					var current = face1 % 100 + 21000;
				}else if(cm.getChar().getGender() == 1){
					var current = face1 % 100 + 24000;
				}
				colors = Array();
           // for (var i = 0; i < 7; i++) {
            //    colors = Array(current + (i * 100));
            //}
				colors = Array(current, current + 100, current + 200, current + 300, current + 400, current + 500, current + 600, current + 700);
				//cm.sendStyle("请选择你喜欢的颜色.", colors,5152001);
			    cm.askAvatar("请选择你喜欢的颜色", 5152001, colors);
        }
		}else if (status == 2){		
		if(beauty ==1 ){
        if (cm.setRandomAvatar(5152001, colors) == 1) {
            cm.sendOk("好了,你的朋友们一定认不出你了!");
        } else {
            cm.sendOk("呃...你没有我们医院射手村整形手术普通会员卡...不好意思无法帮你做整形手术噢。");
        }
        cm.dispose();
			}
    }
}