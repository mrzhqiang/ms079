function start() {

if (cm.getChar().getMapId() != 209000015){
    cm.sendSimple ("#b查看哪种排名？\r\n#L1#杀怪排行榜#l#r#L2#家族排行榜#l\r\n\r\n#L12#等级排行榜#L11#人气排行榜");//#L10#金币排行榜
    } else {
    cm.sendOk("不要再这个地图使用我")
    }
}
function action(mode, type, selection) {
cm.dispose();
if (selection == 0) { //人气排行
       cm.openNpc(2101017,0);
} else if (selection == 1) {
	//Level
        cm.获取杀怪排行榜();
} else if (selection == 2) {
        //MapGui
        cm.获取家族排行榜();
	cm.dispose(); 
}  else if (selection == 10) {
        //MapGui
        cm.获取金币排行榜();
	cm.dispose(); 
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