var cost = 20000;

function start() {
    status = -1;
    em = cm.getEventManager("AirPlane");
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if(mode == 0 && status == 0) {
	cm.dispose();
	return;
    }
    if (mode == 1) {
	status++;
    }
    if (mode == 0 && menu == 0) {
	cm.sendNext("我在这里很长一段时间。请改变主意再来跟我说话.");
	cm.dispose();
    }
    if (mode == 0 && menu == 1) {
	cm.sendOk("我在这里很长一段时间。请改变主意再来跟我说话..");
	cm.dispose();
    }
    if (status == 0) {
	cm.sendSimple("嗨~ 我是 #p"+cm.getNpc()+"# 来自新加坡机场. 我会帮助你回去 #m103000000# 立刻! 需要帮忙??\r\n#L0##b我想购买飞机票到 #m103000000##k#l\r\n#L1##b出发.#k#l");
    } else if(status == 1) {
	menu = selection;
	if (menu == 0) {
	    cm.sendYesNo("飞机票两万金币是否要购买??");
	} else if (menu == 1) {
	    cm.sendYesNo("你是否要走了?? 一旦走了你将失去一张飞机票, \r\n感谢您选择Wizet航空公司!");
	}
  } else if(status == 2) {
	if(menu == 0) {
	    if(!cm.canHold(4031732) || cm.getMeso() < cost) {
		cm.sendOk("你确定你有 #b"+cost+" 金币#k? 如果是这样的话，我劝您检查其他栏，看看是否满了!.");
	    } else {
		cm.gainMeso(-cost);
		cm.gainItem(4031732, 1);
	    }
	    cm.dispose();
	} else if(menu == 1) {
	  if(em == null) {
		cm.sendNext("脚本错误请回报GM!");
		cm.dispose();
	  } else if(!cm.haveItem(4031732)) {
		cm.sendNext("请先购买飞机票谢谢~");
		cm.dispose();
	} else if (em.getProperty("entry") != null && em.getProperty("entry").equals("true")) {
		cm.sendYesNo("是否要搭飞机??");
		} else if( em.getProperty("entry") != null && em.getProperty("entry").equals("false") && em.getProperty("docked") != null && em.getProperty("docked").equals("true")) {
		cm.sendNext("这架飞机正准备起飞。我很抱歉，但你必须得在接下来的旅程。乘坐时间表可通过在迎来售票展台");
		cm.dispose();
	    } else {
		cm.sendNext("请耐心等待几分钟，正在整理里面中！");
		cm.dispose();
	    }
	}
  } else if (status == 3) {
		cm.gainItem(4031732,-1);
		cm.warp(540010001);
		cm.dispose();
	    }
	}
	
