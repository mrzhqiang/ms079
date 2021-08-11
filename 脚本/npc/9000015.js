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
            text += "#e#d#l这里是[寻找宝物]活动集合地.上下两层为 东郊/南郊 当然除了地图不同其他都一样.在地图中寻找箱子，在箱子中找到#v4000313#就可以找我抽取奖励了，奖励:卷轴.玩具.装备.各职业技能书.各种各样椅子.你会抽到什么宝物呢？！\r\n\r\n"//3
            text += "#L2##e#r#v4000313#消耗1个.打开#v04031504#礼物盒.#l\r\n\r\n"//3
            text += "#L3##e#r用#v4000313#x3.打开#v04031505#礼物盒.( 各职业技能书.)#l\r\n"//3
            text += "#L4##e#r用#v4000313#x3.打开#v04031506#礼物盒.( 各种各样椅子.)#l\r\n\r\n"//3
            text += "#L1##e#r#v4000212# 离开此活动地图#l\r\n"//3
            cm.sendSimple(text);
        } else if (selection == 1) {
		cm.openNpc(9900004, 1);
        } else if (selection == 6) {
		cm.openNpc(9310059, 5);
        } else if (selection == 5) {
		cm.openNpc(9000015, 4);
        } else if (selection == 2) {
		cm.openNpc(9000015, 1);
        } else if (selection == 3) {
		cm.openNpc(9000015, 2);
        } else if (selection == 4) {
		cm.openNpc(9000015, 3);
	}
    }
}


