var 聊天 = "#fUI/StatusBar/BtChat/normal/0#";
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
			text += "#e#r下面我将为你介绍跑商送货商人的位置：#k!#n\r\n\r\n"
            text += "#r1、跑商第1轮位置：#b每个主城市的npc杜宜\r\n"//3
            text += "#r2、跑商第2轮位置：#b射手村豆豆屋\r\n"//3
            text += "#r3、跑商第3轮位置：#b魔法密林\r\n"//3
            text += "#r4、跑商第4轮位置：#b勇士部落-仓库老板-王先生\r\n"//3
            text += "#r5、跑商第5轮位置：#b废弃都市-网吧管理员-马龙\r\n"//3
            text += "#r6、跑商第6轮位置：#b林中之城-仓库管理员-吴先生\r\n"//3
            text += "#r7、跑商第7轮位置：#b天空之城-仓库管理员-小刘\r\n"//3
            text += "#r8、跑商第8轮位置：#b冰封雪域-仓库管理员-武先生\r\n"//3
            text += "#r9、跑商第9轮位置：#b玩具城-仓库管理员-舍璧\r\n"//3
            text += "#r10、跑商第10轮位置：#b神木村-仓库管理员-寇斯裤\r\n"//3
            cm.sendOk(text);
		    cm.dispose();
		}
    }
}


