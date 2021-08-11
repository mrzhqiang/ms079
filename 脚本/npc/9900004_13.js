function start() {

if (cm.getChar().getMapId() != 209000015){
    cm.sendSimple ("#b嗨~这里是同步商城，我们2有个商场喔，东西随便看~可能会有重复~\r\n#L1#同步商城（新）#l#r#L2#同步商城（旧）#l ");//\r\n\r\n #r#L3#补偿领取（每人限领一次）#l 
    } else {
    cm.sendOk("不要再这个地图使用我")
    }
}
function action(mode, type, selection) {
cm.dispose();
if (selection == 0) { //人气排行
       cm.openNpc(2101017,0);
} else if (selection == 1) {
 cm.openNpc(9900004,9);

} else if (selection == 2) {
 cm.openNpc(9900004,10);

}  else if (selection == 3) {
 cm.openNpc(9900004,8888);

}   else if (selection == 4) {
 cm.openNpc(1052004);

}   else if (selection == 5) {
 cm.openNpc(1052005);

}   else if (selection == 6) {
 cm.openNpc(1012105);
}  
}