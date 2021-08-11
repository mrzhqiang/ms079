var data = new Date();
var hour = data.getHours();
var map;

function enter(pi) {
	if(hour < 6) {// 00:00 ~ 5:59
		map = 910100000;
	} else if(hour < 12) {// 06:00 ~ 11:59
		map = 910100001;
	} else if(hour < 18) {// 12:00 ~ 17:59
		map = 910100000;
	} else if(hour < 24) {// 18:00 ~ 11:59
		map = 910100001;
	}
    pi.warp(map, 0); //or 910100001
}