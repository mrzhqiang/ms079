/* 	Kelvin
	SingaPore VIP Face changer
*/
var status = 0;
var beauty = 0;
var mface = Array(20109, 20110, 20106, 20108, 20112, 20013);
var fface = Array(21021, 21009, 21010, 21006, 21008, 21012);
var facenew = Array();

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == -1) {
		cm.dispose();
	} else {
		if (mode == 0 && status == 0) {
			cm.dispose();
			return;
		}
		if (mode == 1)
			status++;
		else
			status--;
		if (status == 0) {
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
				cm.sendStyle("选择一个你想要的。", facenew);
			}
		else if (status == 1){
			cm.dispose();
			if (cm.haveItem(5152038) == true){
				cm.gainItem(5152038, -1);
				cm.setFace(facenew[selection]);
				cm.sendOk("享受！");
			} else {
				cm.sendNext("痾.... 貌似没有#t5152038#。");
			}
		}
	}
}