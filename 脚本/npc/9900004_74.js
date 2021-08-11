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
            text += "#L1##r#v4001245#领取1000元累计充值礼包！#l\r\n\r\n"//3
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
			}else */if(cm.haveItem(4001245,1)){
				cm.gainItem(4001245, -1);
				cm.gainItem(2340000, 20);//祝福
				cm.gainItem(2049100, 20);//混沌
				cm.gainItem(1112424,8,8,8,8,200,200,8,8,50,50,50,50,5,5);////戒指
				cm.gainItem(3015051, 1);//巨无霸飞机
				cm.gainItem(1022224, 1);//独眼巨人之眼Lv.1
				cm.gainItem(1032206, 1);//神话耳环复原第1阶段
				cm.gainItem(2040807, 3);//手套攻击
				cm.gainItem(1012172, 1);//鬼娃娃150
				cm.gainItem(1142907,20,20,20,20,1000,1000,20,20,100,100,50,50,15,15);//冒险岛收藏家
				cm.gainMeso(10000000);
                                cm.gainNX(80000); //点卷
            cm.sendOk("换购成功！");
			cm.worldMessage(6,"玩家：["+cm.getName()+"]领取了缘梦 1000元累计充值答谢礼包！感谢您的支持！");
            cm.dispose();
			}else{
            cm.sendOk("道具不足无法换购！");
            cm.dispose();
			}
		}
    }
}


