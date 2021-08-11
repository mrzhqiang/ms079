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
            text += "#e#d制作#v1312184#需要#v1312037#x1.#v4000460#x15.#v4000461#x15.#v4000462#x15.#v4021009#x15.#v4011007#x15.#v4031891#2000万.搜集好道具我就可以为您制作了.#l\r\n全属性+15.攻击130.永恒武器为材料.合成后不计算加卷数量.\r\n"//3
            text += "#L1##r制作伽耶汉武器#l\r\n\r\n"//3
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
			}else */if(cm.haveItem(1312037,1) && cm.haveItem(4000460,15) && cm.haveItem(4000461,15) && cm.haveItem(4000462,15) && cm.haveItem(4021009,15) && cm.haveItem(4011007,15) && cm.getMeso() > 20000000){
				cm.gainItem(1312037, -1);
				cm.gainItem(4000460, -15);
				cm.gainItem(4000461, -15);
				cm.gainItem(4000462, -15);
				cm.gainItem(4021009, -15);
				cm.gainItem(4011007, -15);
				cm.gainItem(1312184,15,15,15,15,0,0,130,0,0,0,50,50,0,0);
				cm.gainMeso(-20000000);
            cm.sendOk("制作成功！");
			cm.worldMessage(6,"玩家：["+cm.getName()+"]制作了[伽耶汉斧(单手斧)]，神器降临！可喜可贺！");
            cm.dispose();
			}else{
            cm.sendOk("您的材料不足！");
            cm.dispose();
			}
		}
    }
}


