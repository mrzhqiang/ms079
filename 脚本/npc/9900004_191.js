importPackage(java.lang);
importPackage(Packages.client);
importPackage(Packages.client.inventory);
importPackage(Packages.server);
importPackage(Packages.constants);
importPackage(Packages.net.channel);
importPackage(Packages.tools);
importPackage(Packages.scripting);
importPackage(Packages.tools.packet);
importPackage(Packages.tools.data);
importPackage(Packages.tools);

var status = -1;
var itemss;
var slot = Array();

function start() {
    action(1, 0, 0);
}

function action(mode, type, selection) {
	
    if (mode == 1) {
        status++;
    } else if (mode == 0 && status != 0) {
        status--;
    } else {
        cm.dispose();
        return;
    }

    if (status == 0) {
		var avail = "";
		for (var i = 0; i < 96; i++) {
			if (cm.getInventory(1).getItem(i) != null) {
				avail += "#L" + cm.getInventory(1).getItem(i).getItemId() + "##i" + cm.getInventory(1).getItem(i).getItemId() + ":##l";
			}
			slot.push(i);
		}
		  for (var i = 0; i < 96; i++) {
			if (cm.getInventory(2).getItem(i) != null) {
				avail += "#L" + cm.getInventory(2).getItem(i).getItemId() + "##i" + cm.getInventory(2).getItem(i).getItemId() + ":##l";
			}
			slot.push(i);
		}
		for (var i = 0; i < 96; i++) {
			if (cm.getInventory(4).getItem(i) != null) {
				avail += "#L" + cm.getInventory(4).getItem(i).getItemId() + "##i" + cm.getInventory(4).getItem(i).getItemId() + ":##l";
			}
			slot.push(i);
		}
		for (var i = 0; i < 96; i++) {
			if (cm.getInventory(5).getItem(i) != null) {
				avail += "#L" + cm.getInventory(5).getItem(i).getItemId() + "##i" + cm.getInventory(5).getItem(i).getItemId() + ":##l";
			}
			slot.push(i);
		}
		cm.sendSimple("请选择你需要清除的物品:\r\n#b" + avail);
	 
    } else if (status == 1) {
        itemss = selection;
		var shul = cm.getPlayer().getItemQuantity(itemss, false);
		cm.removeAll(itemss);
		Ok("我已经将你背包里的 #d#i" + itemss + ":# #t" + itemss + ":# 数量：#e#r" + shul + "#n#b\r\n从你的背包删除！");
		cm.dispose();
    } else {
        cm.dispose();
    }//status
}// function

function Ok(text) {
    cm.sendOk(text);
}



