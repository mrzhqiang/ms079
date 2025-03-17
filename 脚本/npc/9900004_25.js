//importPackage(net.sf.cherry.tools);
//importPackage(net.sf.cherry.client);

var status = 0;

	function start() {
		status = -1;
		action(1, 0, 0);
		}
	function action(mode, type, selection) {
		if (mode == -1) {
		cm.dispose();
		} else {
		if (status >= 0 && mode == 0) {
		cm.dispose();
		return;
		}
		if (mode == 1)
		status++;
		else
		status--;


	if (status == 0) {

	    var textz = "还在担心被BOSS秒杀?快来制作吧.加HP的桑拿服噢!\r\n\r\n";

		textz += "#r#L0#制作+1000HP的桑拿服(男)#l\r\n\r\n";

		textz += "#r#L1#制作+1000HP的桑拿服(女)#l\r\n\r\n";

		textz += "#r#L2#制作+2000HP的桑拿服(男)#l\r\n\r\n";

		textz += "#r#L3#制作+2000HP的桑拿服(女)#l\r\n\r\n";

		textz += "#r#L4#制作+3000HP的桑拿服(男)#l\r\n\r\n";

		textz += "#r#L5#制作+3000HP的桑拿服(女)#l\r\n\r\n";

		cm.sendSimple (textz);  

	}else if (status == 1) {

	if (selection == 0){
		if (!cm.haveItem(1050018)) {
 			cm.sendOk("请带来#v1050018##z1050018#\r\n\#r注:该物品由林中之城桑拿服任务获得#k");
      			cm.dispose();
		} else{
     			cm.gainItem(1050018,-1);
			//var ID = 1050100;//蓝浴巾
                        cm.gainItem(1050100,0,0,0,0,1000,0,0,0,23,24,0,0,0,0,1,0,0);
                        cm.喇叭(1, "玩家：[" + cm.getName() + "]成功制作+1000HP的蓝浴巾(男)!,大家恭喜他!");
      			cm.dispose();
			}

	}else if (selection == 1){
		if (!cm.haveItem(1051017)) {
 			cm.sendOk("请带来#v1051017##z1051017#\r\n\#r注:该物品由林中之城桑拿服任务获得#k");
      			cm.dispose();

		} else{
     			cm.gainItem(1051017,-1);
			//var ID = 1051098;//红浴巾
                        cm.gainItem(1051098,0,0,0,0,1000,0,0,0,23,24,0,0,0,0,1,0,0);
                        cm.喇叭(1, "玩家：[" + cm.getName() + "]成功制作+1000HP的红浴巾(女)!,大家恭喜他!");
      			cm.dispose();
			}

	}else if (selection == 2){
		if (!cm.haveItem(1050100)) {
 			cm.sendOk("请带来#v1050100##z1050100#\r\n5个#v4000040##z4000040#\r\n30个#v4021007##z4021007#\r\n1个#v4011007##z4011007#\r\n1个#v4021009##z4021009#\r\n#r外加1万DQ\r\n#r注:该物品#v1050100##z1050100#由1000血衣获得#k");
      			cm.dispose();
		} else if (!cm.haveItem(4000040,5)) {
 			cm.sendOk("请带来5个#v4000040##z4000040#\r\n\#r注:该物品由击杀蘑菇王获得#k");
      			cm.dispose();
		} else if (!cm.haveItem(4021007,30)) {
 			cm.sendOk("请带来30个#v4021007##z4021007#\r\n\#r注:该物品由野外怪物掉落母矿合成获得#k");
      			cm.dispose();
		} else if (!cm.haveItem(4011007)) {
 			cm.sendOk("请带来1个#v4011007##z4011007#\r\n\#r注:该物品由各种成品矿石合成#k");
      			cm.dispose();
		} else if (!cm.haveItem(4021009)) {
 			cm.sendOk("请带来#v4021009##z4021009#\r\n\#r注:该物品由各种成品矿石合成#k");
      			cm.dispose();
          	} else if (cm.getPlayer().getNX() < 10000) {
 			cm.sendOk("请带来1万点卷");
      			cm.dispose();

		} else{
     			cm.gainItem(4000040,-5);
			cm.gainItem(4021007,-30);
			cm.gainItem(4011007,-1);
			cm.gainItem(4021009,-1);
			cm.gainItem(1050100,-1);
			cm.gainNX(-10000);
			//var ID = 1050127;//浴巾
                        cm.gainItem(1050127,3,3,3,3,2000,0,0,0,23,24,0,0,0,0,1,0,0);
                        cm.喇叭(1, "玩家：[" + cm.getName() + "]成功制作+2000HP的浴巾(男)!,大家恭喜他!");
      			cm.dispose();
			}

	}else if (selection == 3){
		if (!cm.haveItem(1051098)) {
 			cm.sendOk("请带来#v1051098##z1051098#\r\n5个#v4000040##z4000040#\r\n30个#v4021007##z4021007#\r\n1个#v4011007##z4011007#\r\n1个#v4021009##z4021009#\r\n#r外加1万DQ\r\n#r注:该物品#v1051098##z1051098#由1000血衣获得#k");
      			cm.dispose();
		} else if (!cm.haveItem(4000040,5)) {
 			cm.sendOk("请带来5个#v4000040##z4000040#\r\n\#r注:该物品由击杀蘑菇王获得#k");
      			cm.dispose();
		} else if (!cm.haveItem(4021007,30)) {
 			cm.sendOk("请带来30个#v4021007##z4021007#\r\n\#r注:该物品由野外怪物掉落母矿合成获得#k");
      			cm.dispose();
		} else if (!cm.haveItem(4011007)) {
 			cm.sendOk("请带来1个#v4011007##z4011007#\r\n\#r注:该物品由各种成品矿石合成#k");
      			cm.dispose();
		} else if (!cm.haveItem(4021009)) {
 			cm.sendOk("请带来#v4021009##z4021009#\r\n\#r注:该物品由各种成品矿石合成#k");
      			cm.dispose();
          	} else if (cm.getPlayer().getNX() < 10000) {
 			cm.sendOk("请带来1万点卷");
      			cm.dispose();

		} else{
     			cm.gainItem(4000040,-5);
			cm.gainItem(4021007,-30);
			cm.gainItem(4011007,-1);
			cm.gainItem(4021009,-1);
			cm.gainItem(1051098,-1);
			cm.gainNX(-10000);
			//var ID = 1051140;//黄色沐浴巾
                        cm.gainItem(1051140,3,3,3,3,2000,0,0,0,23,24,0,0,0,0,1,0,0);
                        cm.喇叭(1, "玩家：[" + cm.getName() + "]成功制作+2000HP的黄色沐浴巾(女)!,大家恭喜他!"); 
      			cm.dispose();
			}

	}else if (selection == 4){
		if (!cm.haveItem(1050127)) {
 			cm.sendOk("请带来#v1050127##z1050127#\r\n1个#v4001084##z4001084#\r\n1个#v4001083##z4001083#\r\n1个#v4001085##z4001085#\r\n300个#v4310030##z4310030#\r\n#r外加1万DQ\r\n#r注:该物品#v1050127##z1050127#由2000血衣获得#k");
      			cm.dispose();
		} else if (!cm.haveItem(4001084)) {
 			cm.sendOk("请带来#v4001084##z4001084#\r\n\#r注:该物品由击杀帕普拉图斯(闹钟)获得#k");
      			cm.dispose();
		} else if (!cm.haveItem(4001083)) {
 			cm.sendOk("请带来#v4001083##z4001083#\r\n\#r注:该物品由击杀扎昆获得#k");
      			cm.dispose();
		} else if (!cm.haveItem(4001085)) {
 			cm.sendOk("请带来#v4001085##z4001085#\r\n\#r注:该物品由击杀击杀鱼王获得#k");
      			cm.dispose();
		} else if (!cm.haveItem(4310030,300)) {
 			cm.sendOk("请带来300个#v4310030##z4310030#\r\n\#r注:该物品由限时答题获得#k");
      			cm.dispose();
          	} else if (cm.getPlayer().getNX() < 10000) {
 			cm.sendOk("请带来1万点卷");
      			cm.dispose();

		} else{
     			cm.gainItem(4001084,-1);
			cm.gainItem(4310030,-300);
			cm.gainItem(4001083,-1);
			cm.gainItem(4001085,-1);
			cm.gainItem(1050127,-1);
			cm.gainNX(-10000);
			//var ID = 1052358;//未知套服9
                        cm.gainItem(1052358,10,10,10,10,3000,0,5,5,23,24,5,5,0,0,1,0,0);
                        cm.喇叭(1, "玩家：[" + cm.getName() + "]成功制作+3000HP的套服(男)!,卧槽牛X啊~大家恭喜他!"); 
      			cm.dispose();
			}

	}else if (selection == 5){
		if (!cm.haveItem(1051140)) {
 			cm.sendOk("请带来#v1051140##z1051140#\r\n1个#v4001084##z4001084#\r\n1个#v4001083##z4001083#\r\n1个#v4001085##z4001085#\r\n300个#v4310030##z4310030#\r\n#r外加1万DQ\r\n#r注:该物品#v1051140##z1051140#由2000血衣获得#k");
      			cm.dispose();
		} else if (!cm.haveItem(4001084)) {
 			cm.sendOk("请带来#v4001084##z4001084#\r\n\#r注:该物品由击杀帕普拉图斯(闹钟)获得#k");
      			cm.dispose();
		} else if (!cm.haveItem(4001083)) {
 			cm.sendOk("请带来#v4001083##z4001083#\r\n\#r注:该物品由击杀扎昆获得#k");
      			cm.dispose();
		} else if (!cm.haveItem(4001085)) {
 			cm.sendOk("请带来#v4001085##z4001085#\r\n\#r注:该物品由击杀击杀鱼王获得#k");
      			cm.dispose();
		} else if (!cm.haveItem(4310030,300)) {
 			cm.sendOk("请带来300个#v4310030##z4310030#\r\n\#r注:该物品由限时答题获得#k");
      			cm.dispose();
          	} else if (cm.getPlayer().getNX() < 10000) {
 			cm.sendOk("请带来1万点卷");
      			cm.dispose();

		} else{
     			cm.gainItem(4001084,-1);
			cm.gainItem(4310030,-300);
			cm.gainItem(4001083,-1);
			cm.gainItem(4001085,-1);
			cm.gainItem(1051140,-1);
			cm.gainNX(-10000);
			//var ID = 1052358;//未知套服9
	                cm.gainItem(1052358,10,10,10,10,3000,0,5,5,23,24,5,5,0,0,1,0,0);
                        cm.喇叭(1, "玩家：[" + cm.getName() + "]成功制作+3000HP的套服(女)!,卧槽牛X啊~大家恭喜他!"); 
      			cm.dispose();
			}

}
}
}
}
