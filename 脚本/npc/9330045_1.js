
var status = 0;
var zones = 0;

		
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
            var text = "";
            for (i = 0; i < 10; i++) {
                text += "";
            }
            sl = cm.getPlayer().getItemQuantity(4031627, false);
            slA = cm.getPlayer().getItemQuantity(4031628, false);
            slB = cm.getPlayer().getItemQuantity(4031630, false);
            slC = cm.getPlayer().getItemQuantity(4031631, false);
            slD = cm.getPlayer().getItemQuantity(4031632, false);
            slE = cm.getPlayer().getItemQuantity(4031633, false);
            slF = cm.getPlayer().getItemQuantity(4031634, false);
            slG = cm.getPlayer().getItemQuantity(4031635, false);
            slH = cm.getPlayer().getItemQuantity(4031636, false);
            slI = cm.getPlayer().getItemQuantity(4031637, false);
            slJ = cm.getPlayer().getItemQuantity(4031638, false);
            slK = cm.getPlayer().getItemQuantity(4031639, false);
            slL = cm.getPlayer().getItemQuantity(4031640, false);
            slM = cm.getPlayer().getItemQuantity(4031641, false);
            slN = cm.getPlayer().getItemQuantity(4031642, false);
            slO = cm.getPlayer().getItemQuantity(4031643, false);
            slP = cm.getPlayer().getItemQuantity(4031644, false);
            slQ = cm.getPlayer().getItemQuantity(4031645, false);
            slR = cm.getPlayer().getItemQuantity(4031646, false);
            slS = cm.getPlayer().getItemQuantity(4031647, false);
            slT = cm.getPlayer().getItemQuantity(4031648, false);
            slU = cm.getPlayer().getItemQuantity(4031629, false);
			var jifen = sl + slA + slB + slC + slD + slE + slF + slG + slH + slI + slJ + slK + slL + slM + slN + slO + slP + slQ + slR + slS + slT + slU;
            
			text += "您当前拥有：" + cm.getFishingJF() + "钓鱼积分!\r\n\r\n";
			text += "#L1##b#n你当前可以兑换:" + jifen + " 钓鱼积分!\r\n\r\n";
			text += "#L2##r钓鱼积分兑换物品列表";
            cm.sendSimple(text);
            zones == 0;

        } else if (selection == 1) {
					var jifenA = sl + slA + slB + slC + slD + slE + slF + slG + slH + slI + slJ + slK + slL + slM + slN + slO + slP + slQ + slR + slS + slT + slU;
					cm.gainFishingJF(jifenA);
                    cm.sendOk("兑换成功:" + jifenA + "钓鱼积分!");
					cm.removeAll(4031627);
					cm.removeAll(4031628);
					cm.removeAll(4031630);
					cm.removeAll(4031631);
					cm.removeAll(4031632);
					cm.removeAll(4031633);
					cm.removeAll(4031634);
					cm.removeAll(4031635);
					cm.removeAll(4031636);
					cm.removeAll(4031637);
					cm.removeAll(4031638);
					cm.removeAll(4031639);
					cm.removeAll(4031640);
					cm.removeAll(4031641);
					cm.removeAll(4031642);
					cm.removeAll(4031643);
					cm.removeAll(4031644);
					cm.removeAll(4031645);
					cm.removeAll(4031646);
					cm.removeAll(4031647);
					cm.removeAll(4031648);
					cm.removeAll(4031629);
                    cm.dispose();
        } else if (selection == 2) {
			cm.openNpc(9330045, 2);
                    //cm.sendOk("暂未开放！");
                    //cm.dispose();
        }
    }
}	