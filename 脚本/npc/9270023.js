/* 	Noel
	Singapore Random Face Changer
	Credits to aaron and cody
	Side note by aaron [If there is something wrong PM me on fMS]
*/
var status = 0;
var beauty = 0;
var mface = Array(20000, 20001, 20002, 20003, 20004, 20005, 20006, 20007, 20008, 20012);
var fface = Array(21001, 21002, 21003, 21004, 21005, 21006, 21008, 21012, 21014, 21016);
var facenew = Array();

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 0 && status == 0) {
	cm.dispose();
	return;
    }
    if (mode == 1)
	status++;
    else
	status--;
    if (status == 0) {
	cm.sendSimple("使用#b#t5152037##k的话，你只能随机更换脸型喔...你想要使用 #b#t5152037##k来整形嘛？ 不过别忘记喔，这是是随机的\r\n\#L2#来吧！！#l");
    } else if (selection == 2) {
	facenew = Array();
	if (cm.getChar().getGender() == 0) {
	    for(var i = 0; i < mface.length; i++) {
		facenew.push(mface[i] + cm.getChar().getFace() % 1000 - (cm.getChar().getFace() % 100));
	    }
	}
	if (cm.getChar().getGender() == 1) {
	    for(var i = 0; i < fface.length; i++) {
		facenew.push(fface[i] + cm.getChar().getFace() % 1000 - (cm.getChar().getFace() % 100));
	    }
	}
	cm.sendYesNo("如果使用一般的会员卡，你只能随机更换脸型，请问你还是想要使用 #b#t5152037##k 来更换脸型嘛？");
    } else if (status == 2) {
	if (cm.setAvatar(5152037, facenew[Math.floor(Math.random() * facenew.length)]) == 1){
	    cm.sendOk("享受你新的造型吧！");
	} else {
	    cm.sendOk("嗯 ... 你好像没有这里的整形会员卡欸？很抱歉，如果你没有会员卡，我不能够为你服务喔！");
	}
    }
}	
