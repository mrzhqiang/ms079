function start() {

if (cm.getChar().getMapId() != 209000015){
    cm.sendSimple ("#b你想创建家族么？\r\n#L1#创建家族#l#r#L2#创建家族图标#l#L10#创建家族联盟#l");
    } else {
    cm.sendOk("不要再这个地图使用我")
    }
}
function action(mode, type, selection) {
cm.dispose();
if (selection == 0) { //人气排行
       cm.openNpc(2101017,0);
} else if (selection == 1) {
         cm.openNpc(2010007);
} else if (selection == 2) {
         cm.openNpc(2010008);
}  else if (selection == 10) {
         cm.openNpc(2010009);
}   else if (selection == 11) {
        cm.获取人气排行榜();
	cm.dispose(); 
}   else if (selection == 12) {
        cm.获取等级排行榜();
}   else if (selection == 110) {
        cm.人气排行榜();
	cm.dispose(); 
}  
}