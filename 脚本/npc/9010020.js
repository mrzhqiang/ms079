function action(mode, type, selection) {
    if (cm.haveItem(1002971,1) && cm.canHold(1052202,1) && !cm.haveItem(1052202,1)) {
	cm.gainItem(1052202,1);
    } else {
    	cm.sendOk ("如果你给我的粉红色豆帽子和一个空槽，你将获得的粉红豆总体，如果你不已经拥有了它。");
    }
    cm.safeDispose();
}