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
            text += "#e#d您好！您在海盗副本进化的#v1113167#，可以通过天空副本蛋得到进一步的提升哦！\r\n\r\n#v1113168#Lv150级佩戴.全属性+10,HP/MP+1000,防御/魔防+50,命中/回避+15,移动/跳跃+5,攻击/魔法+10\r\n所需材料:#v4170006#x15个+#v1113167#x1.搜集完毕就可以找我进行进化了.#l\r\n\r\n"//3
            text += "#L1##r进化至 大师赏金猎人戒指#l\r\n\r\n"//3
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
			}else */if(cm.haveItem(4170006,15) && cm.haveItem(1113167,1)){
				cm.gainItem(4170006, -15);
				cm.gainItem(1113167, -1);
				cm.gainItem(1113168,10,10,10,10,1000,1000,10,10,50,50,15,15,5,5);
				cm.gainMeso(100000);
            cm.sendOk("兑换成功！");
			cm.worldMessage(6,"玩家：["+cm.getName()+"]用15个[天空副本蛋]+[大师赏金猎人戒指]进化出[传说赏金猎人戒指]，恭喜赏金戒指毕业！");
            cm.dispose();
			}else{
            cm.sendOk("您的材料不足！");
            cm.dispose();
			}
		}
    }
}


