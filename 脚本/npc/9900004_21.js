var status = 0;
var selectedType = -1;
var selectedItem = -1;

function start() {
status = -1;
action(1, 0, 0);
}

function action(mode, type, selection) {
if (mode == -1 || mode == 0) {
cm.dispose();
} else {
status++;
var map = 910000000;
if (status == 0) {
var selStr = "你想去单挑房间吗?";
var pvproom = new Array(
"\r\n"+
cm.getPvpRoom(map+01, 01),
cm.getPvpRoom(map+02, 02)+"\r\n",
cm.getPvpRoom(map+03, 03),
cm.getPvpRoom(map+04, 04)+"\r\n",
cm.getPvpRoom(map+05, 05),
cm.getPvpRoom(map+06, 06)+"\r\n",
cm.getPvpRoom(map+07, 07),
cm.getPvpRoom(map+08, 08)+"\r\n",
cm.getPvpRoom(map+09, 09),
cm.getPvpRoom(map+10, 10)+"\r\n",
cm.getPvpRoom(map+11, 11),
cm.getPvpRoom(map+12, 12)+"\r\n",
cm.getPvpRoom(map+13, 13),
cm.getPvpRoom(map+14, 14)+"\r\n",
cm.getPvpRoom(map+15, 15),
cm.getPvpRoom(map+16, 16)+"\r\n",
cm.getPvpRoom(map+17, 17),
cm.getPvpRoom(map+18, 18)+"\r\n",
cm.getPvpRoom(map+19, 19),
cm.getPvpRoom(map+20, 20)+"\r\n",
cm.getPvpRoom(map+21, 21),
cm.getPvpRoom(map+22, 22));
for (var i = 0; i < pvproom.length; i++) {
selStr += "" + pvproom + "";
}
cm.sendSimple(selStr);
} else if (status == 1) {
selectedroom = selection;
var pvproom2 = new Array(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17, 18,19,20,21,22);
if (cm.getCharQuantity(map+pvproom2[selectedroom]) == 0) {
cm.warp(map+pvproom2[selectedroom]);
cm.Charnotice(1, "成功创建房间");
cm.dispose();
} else if (cm.getCharQuantity(map+pvproom2[selectedroom]) == 2) {
cm.sendOk("该房间已经在游戏了");
cm.dispose();
} else {
cm.warp(map+pvproom2[selectedroom]);
cm.dispose();
}
}
}
} 
