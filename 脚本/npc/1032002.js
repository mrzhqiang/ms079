/* Francois
 Victoria Island: Ellinia (101000000)
 
 Refining NPC: (易德)
 * Gloves
 * Glove Upgrades
 * Hats
 * Wand
 * Staff
 */

var status = 0;
var selectedType = -1;
var selectedItem = -1;
var item;
var mats;
var matQty;
var cost;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1)
        status++;
    else
        cm.dispose();
    if (status == 0 && mode == 1) {
        var selStr = "欢迎来到我的生态安全精炼馆，今天你想做什么？？#b"
        var options = new Array("做手套", "升级手套", "升级帽子", "做短杖", "做长杖");
        for (var i = 0; i < options.length; i++) {
            selStr += "\r\n#L" + i + "# " + options[i] + "#l";
        }

        cm.sendSimple(selStr);
    } else if (status == 1 && mode == 1) {
        selectedType = selection;
        if (selectedType == 0) { //glove refine
            var selStr = "所以，今天你想做什么？？#b";
            var items = new Array("#t1082019##k - 法师 等级. 15#b", "#t1082020##k - 法师 等级. 20#b", "#t1082026##k - 法师 等级. 25#b", "#t1082051##k - 法师 等级. 30#b", "#t1082054##k - 法师 等级. 35#b", "#t1082062##k - 法师 等级. 40#b",
                    "#t1082081##k - 法师 等级. 50#b", "#t1082086##k - 法师 等级. 60#b");
            for (var i = 0; i < items.length; i++) {
                selStr += "\r\n#L" + i + "# " + items[i] + "#l";
            }
            cm.sendSimple(selStr);
        } else if (selectedType == 1) { //glove upgrade
            var selStr = "所以，今天你想做什么？？#b";
            var items = new Array("#t1082021##k - 法师 等级. 20#b", "#t1082022##k - 法师 等级. 20#b", "#t1082027##k - 法师 等级. 25#b", "#t1082028##k - 法师 等级. 25#b", "#t1082052##k - 法师 等级. 30#b", "#t1082053##k - 法师 等级. 30#b",
                    "#t1082055##k - 法师 等级. 35#b", "#t1082056##k - 法师 等级. 35#b", "#t1082063##k - 法师 等级. 40#b", "#t1082064##k - 法师 等级. 40#b", "#t1082082##k - 法师 等级. 50#b", "#t1082080##k - 法师 等级. 50#b",
                    "#t1082087##k - 法师 等级. 60#b", "#t1082088##k - 法师 等级. 60#b");
            for (var i = 0; i < items.length; i++) {
                selStr += "\r\n#L" + i + "# " + items[i] + "#l";
            }
            cm.sendSimple(selStr);
        } else if (selectedType == 2) { //hat upgrade
            var selStr = "所以，今天你想做什么？？#b";
            var items = new Array("#t1002065##k - 法师 等级. 30#b", "#t1002013##k - 法师 等级. 30#b");
            for (var i = 0; i < items.length; i++) {
                selStr += "\r\n#L" + i + "# " + items[i] + "#l";
            }
            cm.sendSimple(selStr);
        } else if (selectedType == 3) { //wand refine
            var selStr = "所以，今天你想做什么？？#b";
            var items = new Array("#t1372005##k - 等级. 8#b", "#t1372006##k - 等级. 13#b", "#t1372002##k - 等级. 18#b", "#t1372004##k - 法师 等级. 23#b", "#t1372003##k - 法师 等级. 28#b",
                    "#t1372001##k - 法师 等级. 33#b", "#t1372000##k - 法师 等级. 38#b", "#t1372007##k - 法师 等级. 48#b");
            for (var i = 0; i < items.length; i++) {
                selStr += "\r\n#L" + i + "# " + items[i] + "#l";
            }
            cm.sendSimple(selStr);
        } else if (selectedType == 4) { //staff refine
            var selStr = "所以，今天你想做什么？？#b";
            var items = new Array("#t1382000##k - 法师 等级. 10#b", "#t1382003##k - 法师 等级. 15#b", "#t1382005##k - 法师 等级. 15#b", "#t1382004##k - 法师 等级. 20#b", "#t1382002##k - 法师 等级. 25#b",
                    "#t1382001##k - 法师 等级. 45#b");
            for (var i = 0; i < items.length; i++) {
                selStr += "\r\n#L" + i + "# " + items[i] + "#l";
            }
            cm.sendSimple(selStr);
        } else if (selectedType == 5) { //wand refine
                cm.openNpc(1032002,2);
        } else if (selectedType == 6) { //wand refine
            var selStr = "所以，今天你想做什么？？#b";
            var items = new Array("#t1372005##k - 等级. 8#b", "#t1372006##k - 等级. 13#b", "#t1372002##k - 等级. 18#b", "#t1372004##k - 法师 等级. 23#b", "#t1372003##k - 法师 等级. 28#b",
                    "#t1372001##k - 法师 等级. 33#b", "#t1372000##k - 法师 等级. 38#b", "#t1372007##k - 法师 等级. 48#b");
            for (var i = 0; i < items.length; i++) {
                selStr += "\r\n#L" + i + "# " + items[i] + "#l";
            }
            cm.sendSimple(selStr);
        }
    } else if (status == 2 && mode == 1) {
        selectedItem = selection;

        if (selectedType == 0) { //glove refine
            var itemSet = new Array(1082019, 1082020, 1082026, 1082051, 1082054, 1082062, 1082081, 1082086);
            var matSet = new Array(4000021, new Array(4000021, 4011001), new Array(4000021, 4011006), new Array(4000021, 4021006, 4021000), new Array(4000021, 4011006, 4011001, 4021000),
                    new Array(4000021, 4021000, 4021006, 4003000), new Array(4021000, 4011006, 4000030, 4003000), new Array(4011007, 4011001, 4021007, 4000030, 4003000));
            var matQtySet = new Array(15, new Array(30, 1), new Array(50, 2), new Array(60, 1, 2), new Array(70, 1, 3, 2), new Array(80, 3, 3, 30), new Array(3, 2, 35, 40), new Array(1, 8, 1, 50, 50));
            var costSet = new Array(7000, 15000, 20000, 25000, 30000, 40000, 50000, 70000);
            item = itemSet[selectedItem];
            mats = matSet[selectedItem];
            matQty = matQtySet[selectedItem];
            cost = costSet[selectedItem];
        } else if (selectedType == 1) { //glove upgrade
            var itemSet = new Array(1082021, 1082022, 1082027, 1082028, 1082052, 1082053, 1082055, 1082056, 1082063, 1082064, 1082082, 1082080, 1082087, 1082088);
            var matSet = new Array(new Array(1082020, 4011001), new Array(1082020, 4021001), new Array(1082026, 4021000), new Array(1082026, 4021008), new Array(1082051, 4021005),
                    new Array(1082051, 4021008), new Array(1082054, 4021005), new Array(1082054, 4021008), new Array(1082062, 4021002), new Array(1082062, 4021008),
                    new Array(1082081, 4021002), new Array(1082081, 4021008), new Array(1082086, 4011004, 4011006), new Array(1082086, 4021008, 4011006));
            var matQtySet = new Array(new Array(1, 1), new Array(1, 2), new Array(1, 3), new Array(1, 1), new Array(1, 3), new Array(1, 1), new Array(1, 3), new Array(1, 1), new Array(1, 4),
                    new Array(1, 2), new Array(1, 5), new Array(1, 3), new Array(1, 3, 5), new Array(1, 2, 3));
            var costSet = new Array(20000, 25000, 30000, 40000, 35000, 40000, 40000, 45000, 45000, 50000, 55000, 60000, 70000, 80000);
            item = itemSet[selectedItem];
            mats = matSet[selectedItem];
            matQty = matQtySet[selectedItem];
            cost = costSet[selectedItem];
        } else if (selectedType == 2) { //hat upgrade
            var itemSet = new Array(1002065, 1002013);
            var matSet = new Array(new Array(1002064, 4011001), new Array(1002064, 4011006));
            var matQtySet = new Array(new Array(1, 3), new Array(1, 3));
            var costSet = new Array(40000, 50000);
            item = itemSet[selectedItem];
            mats = matSet[selectedItem];
            matQty = matQtySet[selectedItem];
            cost = costSet[selectedItem];
        } else if (selectedType == 3) { //wand refine
            var itemSet = new Array(1372005, 1372006, 1372002, 1372004, 1372003, 1372001, 1372000, 1372007);
            var matSet = new Array(4003001, new Array(4003001, 4000001), new Array(4011001, 4000009, 4003000), new Array(4011002, 4003002, 4003000), new Array(4011002, 4021002, 4003000),
                    new Array(4021006, 4011002, 4011001, 4003000), new Array(4021006, 4021005, 4021007, 4003003, 4003000), new Array(4011006, 4021003, 4021007, 4021002, 4003002, 4003000));
            var matQtySet = new Array(5, new Array(10, 50), new Array(1, 30, 5), new Array(2, 1, 10), new Array(3, 1, 10), new Array(5, 3, 1, 15), new Array(5, 5, 1, 1, 20), new Array(4, 3, 2, 1, 1, 30));
            var costSet = new Array(1000, 3000, 5000, 12000, 30000, 60000, 120000, 200000);
            item = itemSet[selectedItem];
            mats = matSet[selectedItem];
            matQty = matQtySet[selectedItem];
            cost = costSet[selectedItem];
        } else if (selectedType == 4) { //staff refine
            var itemSet = new Array(1382000, 1382003, 1382005, 1382004, 1382002, 1382001);
            var matSet = new Array(4003001, new Array(4021005, 4011001, 4003000), new Array(4021003, 4011001, 4003000), new Array(4003001, 4011001, 4003000),
                    new Array(4021006, 4021001, 4011001, 4003000), new Array(4011001, 4021006, 4021001, 4021005, 4003000, 4000010, 4003003));
            var matQtySet = new Array(5, new Array(1, 1, 5), new Array(1, 1, 5), new Array(50, 1, 10), new Array(2, 1, 1, 15), new Array(8, 5, 5, 5, 30, 50, 1));
            var costSet = new Array(2000, 2000, 2000, 5000, 12000, 180000);
            item = itemSet[selectedItem];
            mats = matSet[selectedItem];
            matQty = matQtySet[selectedItem];
            cost = costSet[selectedItem];
        }

        prompt = "你想要做一双 #t" + item + "#? 在这种情况下, 我为了要做出最棒的品质，我建议你确保装备栏空间足够。#b";

        if (mats instanceof Array) {
            for (var i = 0; i < mats.length; i++) {
                prompt += "\r\n#i" + mats[i] + "# " + matQty[i] + " #t" + mats[i] + "#";
            }
        } else {
            prompt += "\r\n#i" + mats + "# " + matQty + " #t" + mats + "#";
        }
        if (cost > 0) {
            prompt += "\r\n#i4031138# " + cost + " 金币";
        }
        cm.sendYesNo(prompt);
    } else if (status == 3 && mode == 1) {
        var complete = true;
        if (cm.getMeso() < cost) {
            cm.sendNext("抱歉我只接受金币.");
            cm.dispose();
            return;
        } else {
            if (mats instanceof Array) {

                for (var i = 0; complete && i < mats.length; i++)
                {
                    if (!cm.haveItem(mats[i], matQty[i]))
                    {
                        complete = false;
                    }
                }
            } else {
                if (!cm.haveItem(mats, matQty))
                {
                    complete = false;
                }
            }
        }

        if (!complete)
            cm.sendOk("很抱歉由于你的材料不足，所以我不想帮你做了。");
        else {
            if (mats instanceof Array) {
                for (var i = 0; i < mats.length; i++) {
                    cm.gainItem(mats[i], -matQty[i]);
                }
            } else
                cm.gainItem(mats, -matQty);

            if (cost > 0)
                cm.gainMeso(-cost);

            cm.gainItem(item, 1);
            cm.sendOk("制作完毕。");
        }
        cm.dispose();
    }
}
