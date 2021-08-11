
/* Chris
	Victoria Road : Kerning City Repair Shop (103000006)
	
	Refining NPC: 
	* Minerals
	* Jewels
	* Special - Iron Hog's Metal Hoof x 100 into Steel Plate
	* Claws
*/

var status = 0;
var selectedType = -1;
var selectedItem = -1;
var item;
var mats;
var matQty;
var cost;
var qty;
var equip;
var last_use; //last item is a use item

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1)
        status++;
    else
        cm.dispose();
    if (status == 0) {
        var selStr = "是的，我退休的盗贼。如果你愿意付出，我可以给你我的一些服务.#b"
        var options = new Array("制作矿石", "提炼宝石矿", "钢铁肥肥的腿兑换中心...", "升级拳套");
        for (var i = 0; i < options.length; i++) {
            selStr += "\r\n#L" + i + "# " + options[i] + "#l";
        }
        cm.sendSimple(selStr);
    } else if (status == 1) {
        selectedType = selection;
        if (selectedType == 0) { //mineral refine
            var selStr = "所以你想要制作什么矿石??#b";
            var minerals = new Array("青铜", "钢铁", "锂矿石", "朱矿石", "银", "紫矿石", "黄金");
            for (var i = 0; i < minerals.length; i++) {
                selStr += "\r\n#L" + i + "# " + minerals[i] + "#l";
            }
            equip = false;
            cm.sendSimple(selStr);
        } else if (selectedType == 1) { //jewel refine
            var selStr = "所以你要我提炼哪种宝石??#b";
            var jewels = new Array("石榴石", "紫水晶", "海蓝宝石", "祖母绿", "蛋白石", "蓝宝石", "黄晶", "钻石", "黑暗水晶");
            for (var i = 0; i < jewels.length; i++) {
                selStr += "\r\n#L" + i + "# " + jewels[i] + "#l";
            }
            equip = false;
            cm.sendSimple(selStr);
        } else if (selectedType == 2) { //foot refine
            var selStr = "你知道吗？很多人不知道，在铁猪的蹄金属的潜力......我可以使它成为一些特别的东西，如果你要我做的话...";
            equip = false;
            cm.sendYesNo(selStr);
        } else if (selectedType == 3) { //claw refine
            var selStr = "啊你想要升级拳套，告诉我要升级哪一个?\r\n#e#r(这边有小BUG例如你选错你要升级的拳套)\r\n(请勿停止对话OR取消因为会断线这点请特别注意!)\r\n(所以请先选好您要升级的拳套)#k#n#b";
            var claws = new Array("赤红战神拳套#k - 盗贼 等级. 60#b", "蓝宝战神拳套#k - 盗贼 等级. 60#b", "黑战神拳套#k - 盗贼 等级. 60#b");
            for (var i = 0; i < claws.length; i++) {
                selStr += "\r\n#L" + i + "# " + claws[i] + "#l";
            }
            equip = true;
            cm.sendSimple(selStr);
        }
        if (equip)
            status++;
    } else if (status == 2 && mode == 1) {
        selectedItem = selection;
        if (selectedType == 0) { //mineral refine
            var itemSet = new Array(4011000, 4011001, 4011002, 4011003, 4011004, 4011005, 4011006);
            var matSet = new Array(4010000, 4010001, 4010002, 4010003, 4010004, 4010005, 4010006);
            var matQtySet = new Array(10, 10, 10, 10, 10, 10, 10);
            var costSet = new Array(300, 300, 300, 500, 500, 500, 800);
            item = itemSet[selectedItem];
            mats = matSet[selectedItem];
            matQty = matQtySet[selectedItem];
            cost = costSet[selectedItem];
        } else if (selectedType == 1) { //jewel refine
            var itemSet = new Array(4021000, 4021001, 4021002, 4021003, 4021004, 4021005, 4021006, 4021007, 4021008);
            var matSet = new Array(4020000, 4020001, 4020002, 4020003, 4020004, 4020005, 4020006, 4020007, 4020008);
            var matQtySet = new Array(10, 10, 10, 10, 10, 10, 10, 10, 10);
            var costSet = new Array(500, 500, 500, 500, 500, 500, 500, 1000, 3000);
            item = itemSet[selectedItem];
            mats = matSet[selectedItem];
            matQty = matQtySet[selectedItem];
            cost = costSet[selectedItem];
        } else if (selectedType == 2) { //special refine
            var itemSet = new Array(4011001, 1);
            var matSet = new Array(4000039, 1);
            var matQtySet = new Array(100, 1);
            var costSet = new Array(1000, 1)
            item = itemSet[0];
            mats = matSet[0];
            matQty = matQtySet[0];
            cost = costSet[0];
        }
        var prompt = "所以，你要我做一些 #t" + item + "#? 在这种情况下，有多少你要我做多少个??";
        cm.sendGetNumber(prompt, 1, 1, 100);

    } else if (status == 3) {
        if (equip) {
            selectedItem = selection;
            qty = 1;
        } else {
            qty = selection;
        }
        last_use = false;

        if (selectedType == 3) { //claw refine
            var itemSet = new Array(1472023, 1472024, 1472025);
            var matSet = new Array(new Array(1472022, 4011007, 4021000, 2012000), new Array(1472022, 4011007, 4021005, 2012002), new Array(1472022, 4011007, 4021008, 4000046));
            var matQtySet = new Array(new Array(1, 1, 8, 10), new Array(1, 1, 8, 10), new Array(1, 1, 3, 5));
            var costSet = new Array(80000, 80000, 100000)
            item = itemSet[selectedItem];
            mats = matSet[selectedItem];
            matQty = matQtySet[selectedItem];
            cost = costSet[selectedItem];
            if (selectedItem != 2)
                last_use = true;
        }

        var prompt = "你要我做 ";
        if (qty == 1)
            prompt += "1个 #t" + item + "#?";
        else
            prompt += qty + " #t" + item + "#?";
        prompt += " 在这种情况下，我要为了做出好的品质。请确保您是否有这么多空间可以放!#b";

        if (mats instanceof Array) {
            for (var i = 0; i < mats.length; i++) {
                prompt += "\r\n#i" + mats[i] + "# " + matQty[i] * qty + " #t" + mats[i] + "#";
            }
        } else {
            prompt += "\r\n#i" + mats + "# " + matQty * qty + " #t" + mats + "#";
        }

        if (cost > 0) {
            prompt += "\r\n#i4031138# " + cost * qty + " 金币";
        }
        cm.sendYesNo(prompt);
    } else if (status == 4) {
        var complete = true;

        if (cm.getMeso() < cost * qty) {
            complete = false;
            cm.sendOk("我只需要现金谢谢。");
			cm.dispose();
			return;
        } else {
            if (mats instanceof Array) {
			for (var i = 0; i < mats.length; i++) {
				complete = cm.haveItem(mats[i], matQty[i] * qty);
		    if (!complete) {
				break;
		    }
			}
            } else {
                complete = cm.haveItem(mats, matQty * qty);
            }
        }

        if (!complete) {
            cm.sendOk("我不能接受的替代品。如果你没有什么我需要的话，我将无法帮你!!");
			cm.dispose();
			return;
	} else if (!cm.canHold()) {
            cm.sendOk("请确认道具栏是否有空间");
			cm.dispose();
			return;
        } else {
            if (mats instanceof Array) {
                for (var i = 0; i < mats.length; i++) {
                    cm.gainItem(mats[i], -matQty[i] * qty);
                }
            } else {
                cm.gainItem(mats, -matQty * qty);
            }
            cm.gainMeso(-cost * qty);
            cm.gainItem(item, qty);
            cm.sendNext("呼......我差点没想到，将工作的第二个......好吧，反正我希望你喜欢它。");
        }
        cm.dispose();
    }
}
