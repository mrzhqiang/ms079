var status = -1;
var need = 0;

function start() {
 action(1, 0, 0);
}
 
function action(mode, type, selection){
	 if(mode == 1){
		 status++;
	 } else {
		 cm.dispose();
		 returm;
	 }
	 if(status == 0){
		 cm.sendYesNo("你们的婚礼进行完了啊？好的我将送你回去!");
	 } else if(status == 1){
		 if(cm.getPlayer().getMeso() < need){
			 cm.sendNext("~ ");
			 cm.dispose();
			 return;
		 }
		 cm.gainMeso(-need);
		if (cm.getMapId() == 700000200) {
			cm.warp(100000000, 0);
		    cm.dispose();
		}
 }   }

