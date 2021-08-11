/*
	Computer - Premium road : Kerning City Internet Cafe
*/

var maps = Array(190000000, 191000000, 192000000, 195000000, 196000000, 197000000
);

function start() {
    var selStr = "Select your desired premium map exclusively for you!#b";
    for (var i = 0; i < maps.length; i++) {
	selStr += "\r\n#L" + i + "##m" + maps[i] + "# #l";
    }
    cm.sendSimple(selStr);
}

function action(mode, type, selection) {
    if (mode == 1) {
	cm.warp(maps[selection], 0);
    }
    cm.dispose();
}