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
            text += "#L1##r#v4170010#领取100元首充礼包！#l\r\n\r\n"//3
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
			}else if(!cm.beibao(2,1)){
            cm.sendOk("消耗栏空余不足1个空格！");
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
			}else */if(cm.haveItem(4170010,1)){
				cm.gainItem(4170010, -1);
				cm.gainItem(2040807, 2);//手套卷
				cm.gainItem(2340000, 5);//祝福
				cm.gainItem(2049100, 5);//混沌 
				cm.gainItem(3015332, 1);//baby品克缤
				cm.gainItem(1142792,5,5,5,5,200,200,5,5,50,50,50,50,15,15);////调查兵团勋章
				cm.gainItem(1112421,8,8,8,8,200,200,8,8,50,50,50,50,5,5);////戒指
				cm.gainMeso(1000000);
                                cm.gainNX(20000); //点卷
            cm.sendOk("换购成功！");
			cm.worldMessage(6,"玩家：["+cm.getName()+"]领取了缘梦 100元首充答谢礼包！感谢您的支持！");
            cm.dispose();
			}else{
            cm.sendOk("你还没有进行首充！");
            cm.dispose();
			}
		}
    }
}


