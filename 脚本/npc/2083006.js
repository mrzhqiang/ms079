/*
	Crysta; - Kamuma (Neo Tokyo Teleporter)
*/

function start() {
	    cm.sendSimple("\r #b#L0#Year 2021 - Average Town Entrance#l \r #L1#Year 2099 - Midnight Harbor Entrance#l \r #L2#Year 2215 - Bombed City Center Retail District#l \r #L3#Year 2216 - Ruined City Intersection#l \r #L4#Year 2230 - Dangerous Tower Lobby#l \r #L5#Year 2503 - Air Battleship Bow#l  \r #L6#Year 2227 - Dangerous City Intersection#l");
}

function action(mode, type, selection) {
	var mapid = 0;

	switch (selection) {
	    case 0:
		mapid = 240070100;
		break;
	    case 1:
		mapid = 240070200;
		break;
	    case 2:
		mapid = 240070300;
		break;
	    case 3:
		mapid = 240070400;
		break;
	    case 4:
		mapid = 240070500;
		break;
	    case 5:
		mapid = 240070600;
		break;
	    case 6:
		mapid = 683070400;
		break;
	}
	if (mapid > 0) {
	    cm.warp(mapid, 0);
	} else {
	    cm.sendOk("Complete your mission first");
	}
    cm.dispose();
}