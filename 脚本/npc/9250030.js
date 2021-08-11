/* Author: aaroncsn (MapleSea Like)
	NPC Name: 		Limbo
	Map(s): 		Thailand:Floating Market(500000000)
	Description: 		Thailand Hair Salon
*/

var status = 0;
var beauty = 0;
var mhair = Array(30030, 30020, 30000, 30270, 30230, 30260, 30280, 30240, 30290, 30340, 30370, 30630, 30530, 30760);
var fhair = Array(31040, 31000, 31250, 31220, 31260, 31240, 31110, 31270, 31030, 31230, 31530, 31710, 31320, 31650, 31630);
var hairnew = Array();

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == -1) {
		cm.dispose();
	} else {
		if (mode == 0 && status == 1) {
			cm.sendNext("I understand...think about it, and if you still feel like changing come talk to me..");
			cm.dispose();
			return;
		}
		if (mode == 1)
			status++;
		else
			status--;
		if (status == 0) {
			cm.sendSimple("I'm a hair assistant Limbo in Floating Market. My title might only be assistant, but don't worry! I've got skills! If you have #bFloating Market hair style coupon(REG)#k by any chance, then how about letting me change your hairdo? \r\n#L0##b Haircut(REG coupon)#k#l \r\n#L1##bDye your hair(REG coupon)#k#l");
		} else if (status == 1) {
			if (selection == 0) {
				beauty = 1;
				hairnew = Array();
				if (cm.getChar().getGender() == 0) {
					for(var i = 0; i < mhair.length; i++) {
						hairnew.push(mhair[i] + parseInt(cm.getChar().getHair()
 % 10));
					}
				} 
				if (cm.getChar().getGender() == 1) {
					for(var i = 0; i < fhair.length; i++) {
						hairnew.push(fhair[i] + parseInt(cm.getChar().getHair()
 % 10));
					}
				}
				cm.sendYesNo("If you use a regular coupon your hair style will change RANDOMLY. Do you still want to use #b#t5150023##k and change it up?");
			} else if (selection == 1) {
				beauty = 2;
				haircolor = Array();
				var current = parseInt(cm.getChar().getHair()
/10)*10;
				for(var i = 0; i < 8; i++) {
					haircolor.push(current + i);
				}
				cm.sendYesNo("If you use a regular coupon your hair color will change RANDOMLY. Do you still want to use #b#t5151018##k and change it up?");
			}
		}
		else if (status == 2){
			cm.dispose();
			if (beauty == 1){
				if (cm.haveItem(5150023) == true){
					cm.gainItem(5150023, -1);
					cm.setHair(hairnew[Math.floor(Math.random() * hairnew.length)]);
					cm.sendOk("Enjoy your new and improved hairstyle!");
				} else {
					cm.sendNext("Hmmm...it looks like you don't have our designated coupon.I'm afraid I can't give you a haircut without it. I'm sorry.");
				}
			}
			if (beauty == 2){
				if (cm.haveItem(5151018) == true){
					cm.gainItem(5151018, -1);
					cm.setHair(haircolor[Math.floor(Math.random() * haircolor.length)]);
					cm.sendOk("Enjoy your new and improved haircolor!");
				} else {
					cm.sendNext("Hmmm...it looks like you don't have our designated coupon.I'm afraid I can't dye your hair without it. I'm sorry.");
				}
			}
		}
	}
}