function start() {

if (cm.getChar().getMapId() != 209000015){
    cm.sendSimple ("#b这里是韩国整容中心，你想改变哪里呢？\r\n#L1#皇家发型（随机）#l#r#L2#皇家发型（自选）#l\r\n\r\n#L3#普通发型+染色（自选）#l\r\n\r\n#L4#皇家脸型（自选）#l#L5#眼睛换色 \r\n\r\n#L6#换肤色（自选）#l   ");
    } else {
    cm.sendOk("请不要在这个地图使用")
    }
}
function action(mode, type, selection) {
cm.dispose();
if (selection == 0) { //人气排行
       cm.openNpc(2101017,0);
} else if (selection == 1) {
 cm.openNpc(1012117);

} else if (selection == 2) {
 cm.openNpc(9105006);

}  else if (selection == 3) {
 cm.openNpc(1012103);

}   else if (selection == 4) {
 cm.openNpc(1052004);

}   else if (selection == 5) {
 cm.openNpc(1052005);

}   else if (selection == 6) {
 cm.openNpc(1012105);
}  
}