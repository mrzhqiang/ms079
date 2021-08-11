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
            var tex2 = "";
            var text = "";
            for (i = 0; i < 10; i++) {
                text += "";
            }
			//显示物品ID图片用的代码是  #v这里写入ID#
            text += "#e#d制作5000血衣#v1052461#需要#v4011006#x100.#v4021007#x100.#v4021008#x100.#v4011002#x100.#v4011003#x100.#v4011005#x100.#v4021000#x100.#v4021002#x100.#v4021004#x100.#v4170005#x200.#v4170013#x200.#v4170002#x200.#v4011007#x30.#v4021009#x30.#v1050018#x1.#v4170016#x90.#v4031891#5000万.搜集好道具我就可以为您制作了.#l\r\n全属性+38.攻击38.魔法力50.桑拿为材料.合成后不计算加卷数量.\r\n"//3
            text += "#L1##r制作5000血衣#l\r\n\r\n"//3
            cm.sendSimple(text);
        } else if (selection == 1) {
			//1
			//2
			//3
			//4
			//5
			/*if(!cm.beibao(1,3)){
            cm.sendOk("装备栏空余不足3个空格！");
            cm.dispose();
			}else if(!cm.beibao(2,2)){
            cm.sendOk("消耗栏空余不足2个空格！");
            cm.dispose();
			}else if(!cm.beibao(3,1)){
            cm.sendOk("设置栏空余不足1个空格！");
            cm.dispose();
			}else if(!cm.beibao(4,1)){
            cm.sendOk("其他栏空余不足1个空格！");
            cm.dispose();
			}else if(!cm.beibao(5,1)){
            cm.sendOk("现金栏空余不足1个空格！");
            cm.dispose();
			}else */if(cm.haveItem(1050018,1) && cm.haveItem(4011006,100) && cm.haveItem(4021007,100) && cm.haveItem(4021008,100) && cm.haveItem(4011002,100) && cm.haveItem(4011003,100) && cm.haveItem(4011005,100) 
&& cm.haveItem(4021000,100) && cm.haveItem(4021002,100) && cm.haveItem(4021004,100) && cm.haveItem(4170005,200) && cm.haveItem(4170013,200) && cm.haveItem(4170002,200) && cm.haveItem(4011007,100) && cm.haveItem(4021009,100) && cm.haveItem(4170016,90) && cm.getMeso() > 50000000){
				cm.gainItem(1050018, -1);
				cm.gainItem(4011006, -100);
				cm.gainItem(4021007, -100);
				cm.gainItem(4021008, -100);
				cm.gainItem(4011002, -100);
				cm.gainItem(4011003, -100);
				cm.gainItem(4011005, -100);
				cm.gainItem(4021000, -200);
				cm.gainItem(4021002, -200);
				cm.gainItem(4021004, -200);
				cm.gainItem(4170005, -100);
				cm.gainItem(4170013, -30);
				cm.gainItem(4170002, -100);
				cm.gainItem(4011007, -20);
				cm.gainItem(4021009, -20);
				cm.gainItem(4170016, -90);
				cm.gainItem(1052461,38,38,38,38,5000,0,38,50,20,20,50,50,0,0);
				cm.gainMeso(-50000000);
            cm.sendOk("制作成功！");
			cm.worldMessage(6,"玩家：["+cm.getName()+"]制作了[终极5000血衣)]，神装在手！天下我有！");
            cm.dispose();
			}else{
            cm.sendOk("您的材料不足！");
            cm.dispose();
			}
		}
    }
}


