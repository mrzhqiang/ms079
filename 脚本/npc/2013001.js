function action(mode, type, selection) {
    if (cm.getPlayer().getMapId() == 920011200) { //exit
    for (var i = 4001044; i < 4001064; i++) {
        cm.removeAll(i); //holy
    }
    cm.warp(200080101);
    cm.dispose();
    return;
    }
    var em = cm.getEventManager("OrbisPQ");
    if (em == null) {
    cm.sendOk("脚本出错，请联系管理员。");
    cm.dispose();
    return;
    }
    if (!cm.isLeader()) {
    cm.sendOk("我只能跟你的队长说话。");
    cm.dispose();
    return;
    }
     if (em.getProperty("pre").equals("0")) {
		if(cm.getPlayer().haveItem(4001063, 20) && cm.getPlayer().getMapId() == 920010000) {
			cm.givePartyExp(6000);
			cm.gainItem(4001063, -20);
			em.setProperty("pre", "1");
		} else{
			cm.sendNext("我被远古精灵困在这座塔，快收集材料让我出去。");
			cm.dispose();
			return;
		}
    }
    switch(cm.getPlayer().getMapId()) {
    case 920010000:
            clear();
        cm.warpParty(920010000, 2);
        break;
    case 920010100:
        if (em.getProperty("stage").equals("6")) {
        if (em.getProperty("finished").equals("0")) {
            cm.warpParty(920010800); //GARDEN.  
        } else {
            cm.sendOk("谢谢你救了我们，请您找女神说话。");
			cm.dispose();
        }
        } else {
        cm.sendOk("请收集六个女神雕像的碎片，然后来找我谈话获得最后一块。");
		cm.dispose();
        }
        break;
    case 920010200: //walkway
        if (!cm.haveItem(4001050,30)) {
		cm.sendOk("我需要#b#t4001050# 30个#k，目前有#c4001050#个。");
		cm.dispose();
        } else {
        cm.removeAll(4001050);
        cm.gainItem(4001044,1); //first piece
        cm.givePartyExp(9000);
        clear();
        }
        break;
    case 920010300: //storage
        if (!cm.haveItem(4001051,15)) {
        cm.sendOk("我需要#b#t4001051# 15个#k，目前有#c4001051#个。");
		cm.dispose();
        } else {
        cm.removeAll(4001051);
        cm.gainItem(4001045,1); //second piece
        cm.givePartyExp(12000);
        clear();
        }
        break;
    case 920010400: //lobby
        if (em.getProperty("stage3").equals("0")) {
        cm.sendOk("请找到今天的唱片，并把它放入音乐盒拨放\r\n#v4001056#星期日\r\n#v4001057#星期一\r\n#v4001058#星期二\r\n#v4001059#星期三\r\n#v4001060#星期四\r\n#v4001061#星期五\r\n#v4001062#星期六\r\n");
        } else if (em.getProperty("stage3").equals("1")) {
        if (cm.canHold(4001046,1)) {
            cm.gainItem(4001046,1); //third piece
            //cm.givePartyExp(7500);
            clear();
            em.setProperty("stage3", "2");
        } else {
            cm.sendOk("请清出一些空间。");
        }
        } else {
        cm.sendOk("谢谢你。");
        }
        break;
    case 920010500: //sealed
        if (em.getProperty("stage4").equals("0")) {
        var players = Array();
        var total = 0;
        for (var i = 0; i < 3; i++) {
            var z = cm.getMap().getNumPlayersInArea(i);
            players.push(z);
            total += z;
        }
        if (total < 5) {
            cm.sendOk("需要5个玩家站在平台上。");
			cm.dispose();
        } else {
            var num_correct = 0;
            for (var i = 0; i < 3; i++) {
            if (em.getProperty("stage4_" + i).equals("" + players[i])) {
                num_correct++;
            }
            }
            if (num_correct == 3) {
            if (cm.canHold(4001047,1)) {
				clear();
                cm.gainItem(4001047,1); //fourth
                cm.givePartyExp(40000);
				em.setProperty("stage4", "1");
            } else {
                cm.sendOk("请清出一些空间。");
            }
            } else {
				cm.showEffect(true, "quest/party/wrong_kor");
				cm.playSound(true, "Party1/Failed");
            if (num_correct > 0) {
                cm.sendOk("一个平台是正确的。");
				cm.dispose();
            } else {
                cm.sendOk("所有平台都是错的。");
				cm.dispose();
            }
            }
        }
        } else {
        cm.sendOk("这么门已经开了！");
		cm.dispose();
        }
        cm.dispose();
        break;
    case 920010600: //lounge
        if (!cm.haveItem(4001052,40)) {
		cm.sendOk("我需要#b#t4001052# 40个#k，目前有#c4001052#个。");
		cm.dispose();
        } else {
        cm.removeAll(4001052);
        cm.gainItem(4001048,1); //fifth piece
        //cm.givePartyExp(7500);
        clear();
        }
        break;
    case 920010700: //on the way up
        if (em.getProperty("stage6").equals("0")) {
        var react = Array();
        var total = 0;
            for(var i = 0; i < 5; i++) {
            if (cm.getMap().getReactorByName("" + (i + 1)).getState() > 0) {
            react.push("1");
            total += 1;
            } else {
            react.push("0");
            }
            }
        if (total != 2) {
            cm.sendOk("需要有两个人在顶部回答题目。");
			cm.dispose();
        } else {
            var num_correct = 0;
            for (var i = 0; i < 5; i++) {
            if (em.getProperty("stage62_" + i).equals("" + react[i])) {
                num_correct++;
            }
            }
            if (num_correct == 5) {
            if (cm.canHold(4001049,1)) {
                    clear();
                cm.gainItem(4001049,1); //sixth
                cm.givePartyExp(80000);
                    em.setProperty("stage6", "1");
            } else {
                cm.sendOk("请清出一些空间。");
				cm.dispose();
            }
            } else {
                    cm.showEffect(true, "quest/party/wrong_kor");
                    cm.playSound(true, "Party1/Failed");
            if (num_correct >= 3) {
                cm.sendOk("一个杠杆是正确的。");
				cm.dispose();
            } else {
                cm.sendOk("两个杠杆都是错误的。");
				cm.dispose();
            }
            }
        }
        } else {
        cm.sendOk("谢谢你。");
        }
        break;
    case 920010800:
        cm.warpParty(920010100);
        break;
    case 920010900:
        cm.sendNext("这是塔的监狱。你可能会发现一些好吃的东西在这里，但除此之外，我不认为我们有什么在这里件。"); 
        break;
    case 920011000:
        cm.sendNext("这是隐藏的房间塔。你可能会发现一些好吃的东西在这里，但除此之外，我不认为我们有什么在这里件。"); 
        break;
    }
    cm.dispose();
}

function clear() {
    cm.showEffect(true, "quest/party/clear");
    cm.playSound(true, "Party1/Clear");
}