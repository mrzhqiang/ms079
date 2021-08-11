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
            text += "#L1##r#v4170012#领取600元累计充值礼包！#l\r\n\r\n"//3
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
			}else */if(cm.haveItem(4170012,1)){
				cm.gainItem(4170012, -1);
				cm.gainItem(2040807, 2);//手套卷
				cm.gainItem(2340000, 15);//祝福
				cm.gainItem(2049100, 15);//混沌
				cm.gainItem(1402014, 1);//温度计
				cm.gainItem(1122017, 1);//吊坠
				cm.gainItem(1112423,8,8,8,8,200,200,8,8,50,50,50,50,5,5);////戒指
				cm.gainItem(3010070, 1);//巨无霸
				cm.gainItem(1142794,15,15,15,15,700,700,15,15,100,100,50,50,15,15);//冒险岛收藏家
				cm.gainItem(1012171, 1);//鬼娃100
				cm.gainMeso(6000000);
                                cm.gainNX(60000); //点卷
            cm.sendOk("换购成功！");
			cm.worldMessage(6,"玩家：["+cm.getName()+"]领取了缘梦 600元累计充值答谢礼包！感谢您的支持！");
            cm.dispose();
			}else{
            cm.sendOk("道具不足无法换购！");
            cm.dispose();
			}
		}
    }
}


