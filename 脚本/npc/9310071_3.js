importPackage(net.sf.cherry.tools);
importPackage(net.sf.cherry.client);

var status = 0;
var ttt ="#fUI/UIWindow.img/Quest/icon9/0#";
var xxx ="#fUI/UIWindow.img/Quest/icon8/0#";
var sss ="#fUI/UIWindow.img/QuestIcon/3/0#";

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

	    var textz = "\r\n你好，我是大姐大，请选择你需要租的装备 \r\n所有装备出租的只有一天权\r\n";

		textz += "#r#L0##v1302126##z1302126#    战士 - 单手剑#l\r\n\r\n";

		textz += "#r#L1##v1402068##z1402068#    战士 - 双手剑#l\r\n\r\n";

		textz += "#r#L2##v1332095##z1332095#      飞侠 - 短  刀#l\r\n\r\n";

		textz += "#r#L3##v1382076##z1382076#   法师 - 长  杖#l\r\n\r\n";

		textz += "#r#L4##v1432059##z1432059#   战士 - 长  枪#l\r\n\r\n";

		textz += "#r#L5##v1442084##z1442084#   战士 - 长  矛#l\r\n\r\n";

		textz += "#r#L6##v1452079##z1452079#   弓手 - 远  弓#l\r\n\r\n";

		textz += "#r#L7##v1462071##z1462071#   弩手 - 远  弩#l\r\n\r\n";

		textz += "#r#L8##v1472095##z1472095#   飞侠 - 拳  套#l\r\n\r\n";

		textz += "#r#L9##v1482043##z1482043#   海盗 - 拳  甲#l\r\n\r\n";

		textz += "#r#L10##v1492044##z1492044#    海盗 - 手  枪#l\r\n\r\n";

		cm.sendSimple (textz);  

	}else if (status == 1) {

	if (selection == 0){
		if (cm.getMeso() < 0) {
 			cm.sendOk("请带来#r1亿#k金币#k");
      			cm.dispose();
		} else if (!cm.haveItem(5220007,1)) {
 			cm.sendOk("#v5220007##z5220007#您没有该物品 需要在商城购买后在来吧");
      			cm.dispose();
		} else if (cm.getPlayer().getInventory(net.sf.cherry.client.MapleInventoryType.getByType(1)).isFull(3)){
			cm.sendOk("#b请保证装备栏位至少有3个空格,否则无法兑换.");
			cm.dispose();
		} else{
         var ii = net.sf.cherry.server.MapleItemInformationProvider.getInstance();		                
            var type = ii.getInventoryType(1302126); //获得装备的类形/////////////////////////////////////////////////////////////////////
            var toDrop = ii.randomizeStats(ii.getEquipById(1302126)).copy(); // 生成一个Equip类
            var temptime = new java.sql.Timestamp(java.lang.System.currentTimeMillis() + 1 * 4 * 60 * 60 * 1000*1); //时间
toDrop.setExpiration(temptime); 
cm.getPlayer().getInventory(type).addItem(toDrop);//将这个装备放入包中
cm.getC().getSession().write(net.sf.cherry.tools.MaplePacketCreator.addInventorySlot(type, toDrop)); //刷新背包	
cm.getChar().saveToDB(true);
			cm.gainItem(5220007,-1);
			cm.sendOk("兑换成功!")
      			cm.dispose();
			}

	}else if (selection == 1){
		if (cm.getMeso() < 0) {
 			cm.sendOk("请带来#r5000W#k金币#k");
      			cm.dispose();
		} else if (!cm.haveItem(5220007,1)) {
 			cm.sendOk("#v5220007##z5220007#您没有该物品 需要在商城购买后在来吧#k");
      			cm.dispose();
		} else if (cm.getPlayer().getInventory(net.sf.cherry.client.MapleInventoryType.getByType(1)).isFull(3)){
			cm.sendOk("#b请保证装备栏位至少有3个空格,否则无法兑换.");
			cm.dispose();
		} else{
         var ii = net.sf.cherry.server.MapleItemInformationProvider.getInstance();		                
            var type = ii.getInventoryType(1402068); //获得装备的类形/////////////////////////////////////////////////////////////////////
            var toDrop = ii.randomizeStats(ii.getEquipById(1402068)).copy(); // 生成一个Equip类
            var temptime = new java.sql.Timestamp(java.lang.System.currentTimeMillis() + 1 * 4 * 60 * 60 * 1000*1); //时间
toDrop.setExpiration(temptime); 
cm.getPlayer().getInventory(type).addItem(toDrop);//将这个装备放入包中
cm.getC().getSession().write(net.sf.cherry.tools.MaplePacketCreator.addInventorySlot(type, toDrop)); //刷新背包	
cm.getChar().saveToDB(true);
			cm.gainItem(5220007,-1);
			cm.sendOk("兑换成功!")
      			cm.dispose();
			}




	}else if (selection == 2){
		if (cm.getMeso() < 0) {
 			cm.sendOk("请带来#r5000W#k金币#k");
      			cm.dispose();
		} else if (!cm.haveItem(5220007,1)) {
 			cm.sendOk("#v5220007##z5220007#您没有该物品 需要在商城购买后在来吧#k");
      			cm.dispose();
		} else if (cm.getPlayer().getInventory(net.sf.cherry.client.MapleInventoryType.getByType(1)).isFull(3)){
			cm.sendOk("#b请保证装备栏位至少有3个空格,否则无法兑换.");
			cm.dispose();
		} else{
         var ii = net.sf.cherry.server.MapleItemInformationProvider.getInstance();		                
            var type = ii.getInventoryType(1332095); //获得装备的类形/////////////////////////////////////////////////////////////////////
            var toDrop = ii.randomizeStats(ii.getEquipById(1332095)).copy(); // 生成一个Equip类
            var temptime = new java.sql.Timestamp(java.lang.System.currentTimeMillis() + 1 * 4 * 60 * 60 * 1000*1); //时间
toDrop.setExpiration(temptime); 
cm.getPlayer().getInventory(type).addItem(toDrop);//将这个装备放入包中
cm.getC().getSession().write(net.sf.cherry.tools.MaplePacketCreator.addInventorySlot(type, toDrop)); //刷新背包	
cm.getChar().saveToDB(true);
			cm.gainItem(5220007,-1);
			cm.sendOk("兑换成功!")
      			cm.dispose();
			}

	}else if (selection == 3){
		if (cm.getMeso() < 0) {
 			cm.sendOk("请带来#r5000W#k金币#k");
      			cm.dispose();
		} else if (!cm.haveItem(5220007,1)) {
 			cm.sendOk("#v5220007##z5220007#您没有该物品 需要在商城购买后在来吧#k");
      			cm.dispose();
		} else if (cm.getPlayer().getInventory(net.sf.cherry.client.MapleInventoryType.getByType(1)).isFull(3)){
			cm.sendOk("#b请保证装备栏位至少有3个空格,否则无法兑换.");
			cm.dispose();
		} else{
         var ii = net.sf.cherry.server.MapleItemInformationProvider.getInstance();		                
            var type = ii.getInventoryType(1382076); //获得装备的类形/////////////////////////////////////////////////////////////////////
            var toDrop = ii.randomizeStats(ii.getEquipById(1382076)).copy(); // 生成一个Equip类
            var temptime = new java.sql.Timestamp(java.lang.System.currentTimeMillis() + 1 * 4 * 60 * 60 * 1000*1); //时间
toDrop.setExpiration(temptime); 
cm.getPlayer().getInventory(type).addItem(toDrop);//将这个装备放入包中
cm.getC().getSession().write(net.sf.cherry.tools.MaplePacketCreator.addInventorySlot(type, toDrop)); //刷新背包	
cm.getChar().saveToDB(true);
			cm.gainItem(5220007,-1);
			cm.sendOk("兑换成功!")
      			cm.dispose();
			}

	}else if (selection == 4){
		if (cm.getMeso() < 0) {
 			cm.sendOk("请带来#r5000W#k金币#k");
      			cm.dispose();
		} else if (!cm.haveItem(5220007,1)) {
 			cm.sendOk("#v5220007##z5220007#您没有该物品 需要在商城购买后在来吧#k");
      			cm.dispose();
		} else if (cm.getPlayer().getInventory(net.sf.cherry.client.MapleInventoryType.getByType(1)).isFull(3)){
			cm.sendOk("#b请保证装备栏位至少有3个空格,否则无法兑换.");
			cm.dispose();
		} else{
         var ii = net.sf.cherry.server.MapleItemInformationProvider.getInstance();		                
            var type = ii.getInventoryType(1432059); //获得装备的类形/////////////////////////////////////////////////////////////////////
            var toDrop = ii.randomizeStats(ii.getEquipById(1432059)).copy(); // 生成一个Equip类
            var temptime = new java.sql.Timestamp(java.lang.System.currentTimeMillis() + 1 * 4 * 60 * 60 * 1000*1); //时间
toDrop.setExpiration(temptime); 
cm.getPlayer().getInventory(type).addItem(toDrop);//将这个装备放入包中
cm.getC().getSession().write(net.sf.cherry.tools.MaplePacketCreator.addInventorySlot(type, toDrop)); //刷新背包	
cm.getChar().saveToDB(true);
			cm.gainItem(5220007,-1);
			cm.sendOk("兑换成功!")
      			cm.dispose();
			}

	}else if (selection == 5){
		if (cm.getMeso() < 0) {
 			cm.sendOk("请带来#r5000W#k金币#k");
      			cm.dispose();
		} else if (!cm.haveItem(5220007,1)) {
 			cm.sendOk("#v5220007##z5220007#您没有该物品 需要在商城购买后在来吧#k");
      			cm.dispose();
		} else if (cm.getPlayer().getInventory(net.sf.cherry.client.MapleInventoryType.getByType(1)).isFull(3)){
			cm.sendOk("#b请保证装备栏位至少有3个空格,否则无法兑换.");
			cm.dispose();
		} else{
         var ii = net.sf.cherry.server.MapleItemInformationProvider.getInstance();		                
            var type = ii.getInventoryType(1442084); //获得装备的类形/////////////////////////////////////////////////////////////////////
            var toDrop = ii.randomizeStats(ii.getEquipById(1442084)).copy(); // 生成一个Equip类
            var temptime = new java.sql.Timestamp(java.lang.System.currentTimeMillis() + 1 * 4 * 60 * 60 * 1000*1); //时间
toDrop.setExpiration(temptime); 
cm.getPlayer().getInventory(type).addItem(toDrop);//将这个装备放入包中
cm.getC().getSession().write(net.sf.cherry.tools.MaplePacketCreator.addInventorySlot(type, toDrop)); //刷新背包	
cm.getChar().saveToDB(true);
			cm.gainItem(5220007,-1);
			cm.sendOk("兑换成功!")
      			cm.dispose();
			}






	}else if (selection == 6){
		if (cm.getMeso() < 0) {
 			cm.sendOk("请带来#r5000W#k金币#k");
      			cm.dispose();
		} else if (!cm.haveItem(5220007,1)) {
 			cm.sendOk("#v5220007##z5220007#您没有该物品 需要在商城购买后在来吧#k");
      			cm.dispose();
		} else if (cm.getPlayer().getInventory(net.sf.cherry.client.MapleInventoryType.getByType(1)).isFull(3)){
			cm.sendOk("#b请保证装备栏位至少有3个空格,否则无法兑换.");
			cm.dispose();
		} else{
         var ii = net.sf.cherry.server.MapleItemInformationProvider.getInstance();		                
            var type = ii.getInventoryType(1452079); //获得装备的类形/////////////////////////////////////////////////////////////////////
            var toDrop = ii.randomizeStats(ii.getEquipById(1452079)).copy(); // 生成一个Equip类
            var temptime = new java.sql.Timestamp(java.lang.System.currentTimeMillis() + 1 * 4 * 60 * 60 * 1000*1); //时间
toDrop.setExpiration(temptime); 
cm.getPlayer().getInventory(type).addItem(toDrop);//将这个装备放入包中
cm.getC().getSession().write(net.sf.cherry.tools.MaplePacketCreator.addInventorySlot(type, toDrop)); //刷新背包	
cm.getChar().saveToDB(true);
			cm.gainItem(5220007,-1);
			cm.sendOk("兑换成功!")
      			cm.dispose();
			}

	}else if (selection == 7){
		if (cm.getMeso() < 0) {
 			cm.sendOk("请带来#r5000W#k金币#k");
      			cm.dispose();
		} else if (!cm.haveItem(5220007,1)) {
 			cm.sendOk("#v5220007##z5220007#您没有该物品 需要在商城购买后在来吧#k");
      			cm.dispose();
		} else if (cm.getPlayer().getInventory(net.sf.cherry.client.MapleInventoryType.getByType(1)).isFull(3)){
			cm.sendOk("#b请保证装备栏位至少有3个空格,否则无法兑换.");
			cm.dispose();
		} else{
         var ii = net.sf.cherry.server.MapleItemInformationProvider.getInstance();		                
            var type = ii.getInventoryType(1462071); //获得装备的类形/////////////////////////////////////////////////////////////////////
            var toDrop = ii.randomizeStats(ii.getEquipById(1462071)).copy(); // 生成一个Equip类
            var temptime = new java.sql.Timestamp(java.lang.System.currentTimeMillis() + 1 * 4 * 60 * 60 * 1000*1); //时间
toDrop.setExpiration(temptime); 
cm.getPlayer().getInventory(type).addItem(toDrop);//将这个装备放入包中
cm.getC().getSession().write(net.sf.cherry.tools.MaplePacketCreator.addInventorySlot(type, toDrop)); //刷新背包	
cm.getChar().saveToDB(true);
			cm.gainItem(5220007,-1);
			cm.sendOk("兑换成功!")
      			cm.dispose();
			}

	}else if (selection == 8){
		if (cm.getMeso() < 0) {
 			cm.sendOk("请带来#r5000W#k金币#k");
      			cm.dispose();
		} else if (!cm.haveItem(5220007,1)) {
 			cm.sendOk("#v5220007##z5220007#您没有该物品 需要在商城购买后在来吧#k");
      			cm.dispose();
		} else if (cm.getPlayer().getInventory(net.sf.cherry.client.MapleInventoryType.getByType(1)).isFull(3)){
			cm.sendOk("#b请保证装备栏位至少有3个空格,否则无法兑换.");
			cm.dispose();
		} else{
         var ii = net.sf.cherry.server.MapleItemInformationProvider.getInstance();		                
            var type = ii.getInventoryType(1472095); //获得装备的类形/////////////////////////////////////////////////////////////////////
            var toDrop = ii.randomizeStats(ii.getEquipById(1472095)).copy(); // 生成一个Equip类
            var temptime = new java.sql.Timestamp(java.lang.System.currentTimeMillis() + 1 * 4 * 60 * 60 * 1000*1); //时间
toDrop.setExpiration(temptime); 
cm.getPlayer().getInventory(type).addItem(toDrop);//将这个装备放入包中
cm.getC().getSession().write(net.sf.cherry.tools.MaplePacketCreator.addInventorySlot(type, toDrop)); //刷新背包	
cm.getChar().saveToDB(true);
			cm.gainItem(5220007,-1);
			cm.sendOk("兑换成功!")
      			cm.dispose();
			}

	}else if (selection == 9){
		if (cm.getMeso() < 0) {
 			cm.sendOk("请带来#r5000W#k金币#k");
      			cm.dispose();
		} else if (!cm.haveItem(5220007,1)) {
 			cm.sendOk("#v5220007##z5220007#您没有该物品 需要在商城购买后在来吧#k");
      			cm.dispose();
		} else if (cm.getPlayer().getInventory(net.sf.cherry.client.MapleInventoryType.getByType(1)).isFull(3)){
			cm.sendOk("#b请保证装备栏位至少有3个空格,否则无法兑换.");
			cm.dispose();
		} else{
         var ii = net.sf.cherry.server.MapleItemInformationProvider.getInstance();		                
            var type = ii.getInventoryType(1482043); //获得装备的类形/////////////////////////////////////////////////////////////////////
            var toDrop = ii.randomizeStats(ii.getEquipById(1482043)).copy(); // 生成一个Equip类
            var temptime = new java.sql.Timestamp(java.lang.System.currentTimeMillis() + 1 * 4 * 60 * 60 * 1000*1); //时间
toDrop.setExpiration(temptime); 
cm.getPlayer().getInventory(type).addItem(toDrop);//将这个装备放入包中
cm.getC().getSession().write(net.sf.cherry.tools.MaplePacketCreator.addInventorySlot(type, toDrop)); //刷新背包	
cm.getChar().saveToDB(true);
			cm.gainItem(5220007,-1);
			cm.sendOk("兑换成功!")
      			cm.dispose();
			}
	}else if (selection == 10){
		if (cm.getMeso() < 0) {
 			cm.sendOk("请带来#r5000W#k金币#k");
      			cm.dispose();
		} else if (!cm.haveItem(5220007,1)) {
 			cm.sendOk("#v5220007##z5220007#您没有该物品 需要在商城购买后在来吧#k");
      			cm.dispose();
		} else if (cm.getPlayer().getInventory(net.sf.cherry.client.MapleInventoryType.getByType(1)).isFull(3)){
			cm.sendOk("#b请保证装备栏位至少有3个空格,否则无法兑换.");
			cm.dispose();
		} else{
         var ii = net.sf.cherry.server.MapleItemInformationProvider.getInstance();		                
            var type = ii.getInventoryType(1492044); //获得装备的类形/////////////////////////////////////////////////////////////////////
            var toDrop = ii.randomizeStats(ii.getEquipById(1492044)).copy(); // 生成一个Equip类
            var temptime = new java.sql.Timestamp(java.lang.System.currentTimeMillis() + 1 * 4 * 60 * 60 * 1000*1); //时间
toDrop.setExpiration(temptime); 
cm.getPlayer().getInventory(type).addItem(toDrop);//将这个装备放入包中
cm.getC().getSession().write(net.sf.cherry.tools.MaplePacketCreator.addInventorySlot(type, toDrop)); //刷新背包	
cm.getChar().saveToDB(true);
			cm.gainItem(5220007,-1);
			cm.sendOk("兑换成功!")
      			cm.dispose();
			}


}
}
}
}
