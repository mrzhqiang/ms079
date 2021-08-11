var status = 0

function start(){
	action(1, 0, 0);
}

function action(mode, type ,selection){//1050122 1051130
	if(mode == 1) {
		status++;
	} else if(mode == 0) {
		status--;
	} else {
		cm.dispose();
		return;
	}
	if(status == 1){
		cm.openShop(102);
		cm.dispose();
	} else {
		cm.dispose();
	}
}