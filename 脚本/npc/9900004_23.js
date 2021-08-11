function start() {

if (cm.getChar().getMapId() != 209000015){
    cm.sendSimple ("#b需要给装备注入哪种力量？请选择\r\n\r\n#L1#七星注力（攻击力魔法力）#l    ");
    } else {
    cm.sendOk("不要再这个地图使用我")
    }
}
function action(mode, type, selection) {
cm.dispose();
if (selection == 0) { //人气排行
       cm.openNpc(2101017,0);
} else if (selection == 1) {
            cm.openNpc(9900004,18);
} else if (selection == 2) {
            cm.openNpc(9900004,19);
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