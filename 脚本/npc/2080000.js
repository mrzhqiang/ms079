/* Mos
 Leafre : Leafre (240000000)
 
 Refining NPC: 
 * Level 110 weapons - Stimulator allowed
 */

        var status = 0;
var selectedType = -1;
var selectedItem = -1;
var stimulator = false;
var bustedDagger = false;
var item;
var mats;
var matQty;
var cost;
var stimID;

function start() {
    status = -2;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        cm.dispose();
        return;
    }
    if (status == -1) {
        cm.sendSimple("你好~如果您有兴趣升级或修理你的武器，你肯定会来对地方了！我在这个伟大的神木村最好武器制造商。好了，你怎么想的武器，是充满了龙的不可思议的力量？你有兴趣吗？\r\n#L0# 做一个龙武器。\r\n#L100# 修理装备。#l");
    } else if (status == 0) {
        if (selection == 0) {
            if (cm.haveItem(4001079)) {
                bustedDagger = true;
                cm.sendNext("这是什么？在破获匕首你似乎老了，我需要 #i" + 4011001 + "# 和 #i" + 4011002 + "#.");
            } else {
                var selStr = "龙的力量是不可低估的。如果你愿意，我可以添加自己的权力你的武器之一。但是，武器必须足够强大，以保持其潜在的...#b";
                var options = new Array("什么是催化剂?", "做一个剑士武器", "做一个弓箭手武器", "做一个法师武器", "做一个盗贼武器", "做一个海盗武器",
                        "做一个剑士武器使用催化剂", "做一个弓箭手武器使用催化剂", "做一个法师武器使用催化剂", "做一个盗贼武器使用催化剂", "做一个海盗武器使用催化剂");
                for (var i = 0; i < options.length; i++) {
                    selStr += "\r\n#L" + i + "# " + options[i] + "#l";
                }
                cm.sendSimple(selStr);
            }
        } else {
            cm.sendYesNo("太好了！我会告诉你我的能力的。大家都知道，这取决于耐久性的物品的等级和数量的物品的丢失，所以服务费各不相同？您想立即修复你的装备？");
            status = 99;
        }

    } else if (status == 1) {
        if (bustedDagger) {
            if (cm.haveItem(4011001) && cm.haveItem(4011002) && cm.haveItem(4001079)) {
                cm.gainItem(4011001, -1);
                cm.gainItem(4011002, -1);
                cm.gainItem(4001079, -1);
                cm.gainItem(4001078, 1);
            } else {
                cm.sendOk("你没有足够的材料。");
            }
            cm.dispose();
        } else {
            selectedType = selection;
            if (selectedType > 5) {
                stimulator = true;
                selectedType -= 5;
            } else
                stimulator = false;
            if (selectedType == 0) { //What's a stim?
                cm.sendNext("催化剂是一种特殊的药水，我可以加入到创建某些项目的进程。它给它统计中，就好像从一个怪物下降。然而，它可能有没有变化，而且也有可能为项低于平均水平。还有没有得到任何项目使用刺激的时候，所以请明智的选择有10％的机会。")
                cm.dispose();
            } else if (selectedType == 1) { //warrior weapon
                var selStr = "很好，那么你想做哪一个？？#b";
                var weapon = new Array("龙泉剑#k - 等级. 110 单手剑#b", "战龙斧#k - 等级. 110 单手斧#b", "龙头锤#k - 等级. 110 单手锤#b", "狂龙剑#k - 等级. 110 双手剑#b", "龙王之斧#k - 等级. 110 双手斧#b", "龙之焰#k - 等级. 110 双手锤#b",
                        "幻龙长枪#k - 等级. 110 火枪#b", "赤龙长矛#k - 等级. 110 矛#b");
                for (var i = 0; i < weapon.length; i++) {
                    selStr += "\r\n#L" + i + "# " + weapon[i] + "#l";
                }
                cm.sendSimple(selStr);
            } else if (selectedType == 2) { //bowman weapon
                var selStr = "很好，那么你想做哪一个？？#b";
                var weapon = new Array("龙形之弓#k - 等级. 110 弓#b", "飞龙弩#k - 等级. 110 弩#b");
                for (var i = 0; i < weapon.length; i++) {
                    selStr += "\r\n#L" + i + "# " + weapon[i] + "#l";
                }
                cm.sendSimple(selStr);
            } else if (selectedType == 3) { //magician weapon
                var selStr = "很好，那么你想做哪一个？？#b";
                var weapon = new Array("圣龙短杖#k - 等级. 108 短杖#b", "龙骨长杖#k - 等级. 110 长杖#b");
                for (var i = 0; i < weapon.length; i++) {
                    selStr += "\r\n#L" + i + "# " + weapon[i] + "#l";
                }
                cm.sendSimple(selStr);
            } else if (selectedType == 4) { //thief weapon
                var selStr = "很好，那么你想做哪一个？？#b";
                var weapon = new Array("阿拉伯弯刀#k - 等级. 110 力量短刀#b", "烈风短刃#k - 等级. 110 幸运短刀#b", "绿色龙牙拳刃#k - 等级. 110 拳套#b");
                for (var i = 0; i < weapon.length; i++) {
                    selStr += "\r\n#L" + i + "# " + weapon[i] + "#l";
                }
                cm.sendSimple(selStr);
            } else if (selectedType == 5) { //pirate weapon
                var selStr = "很好，那么你想做哪一个？？#b";
                var weapon = new Array("龙王之爪#k - 等级. 110 指虎#b", "圣龙金枪#k - 等级. 110 枪#b");
                for (var i = 0; i < weapon.length; i++) {
                    selStr += "\r\n#L" + i + "# " + weapon[i] + "#l";
                }
                cm.sendSimple(selStr);
            }
        }
    } else if (status == 2) {
        selectedItem = selection;
        if (selectedType == 1) { //warrior weapon
            var itemSet = new Array(1302059, 1312031, 1322052, 1402036, 1412026, 1422028, 1432038, 1442045);
            var matSet = new Array(new Array(1302056, 4000244, 4000245, 4005000), new Array(1312030, 4000244, 4000245, 4005000), new Array(1322045, 4000244, 4000245, 4005000), new Array(1402035, 4000244, 4000245, 4005000),
                    new Array(1412021, 4000244, 4000245, 4005000), new Array(1422027, 4000244, 4000245, 4005000), new Array(1432030, 4000244, 4000245, 4005000), new Array(1442044, 4000244, 4000245, 4005000));
            var matQtySet = new Array(new Array(1, 20, 25, 8), new Array(1, 20, 25, 8), new Array(1, 20, 25, 8), new Array(1, 20, 25, 8), new Array(1, 20, 25, 8), new Array(1, 20, 25, 8), new Array(1, 20, 25, 8), new Array(1, 20, 25, 8));
            var costSet = new Array(120000, 120000, 120000, 120000, 120000, 120000, 120000, 120000);
            item = itemSet[selectedItem];
            mats = matSet[selectedItem];
            matQty = matQtySet[selectedItem];
            cost = costSet[selectedItem];
        } else if (selectedType == 2) { //bowman weapon
            var itemSet = new Array(1452044, 1462039);
            var matSet = new Array(new Array(1452019, 4000244, 4000245, 4005000, 4005002), new Array(1462015, 4000244, 4000245, 4005000, 4005002));
            var matQtySet = new Array(new Array(1, 20, 25, 3, 5), new Array(1, 20, 25, 5, 3));
            var costSet = new Array(120000, 120000);
            item = itemSet[selectedItem];
            mats = matSet[selectedItem];
            matQty = matQtySet[selectedItem];
            cost = costSet[selectedItem];
        } else if (selectedType == 3) { //magician weapon
            var itemSet = new Array(1372032, 1382036);
            var matSet = new Array(new Array(1372010, 4000244, 4000245, 4005001, 4005003), new Array(1382035, 4000244, 4000245, 4005001, 4005003));
            var matQtySet = new Array(new Array(1, 20, 25, 6, 2), new Array(1, 20, 25, 6, 2));
            var costSet = new Array(120000, 120000);
            item = itemSet[selectedItem];
            mats = matSet[selectedItem];
            matQty = matQtySet[selectedItem];
            cost = costSet[selectedItem];
        } else if (selectedType == 4) { //thief weapon
            var itemSet = new Array(1332049, 1332050, 1472051);
            var matSet = new Array(new Array(1332051, 4000244, 4000245, 4005000, 4005002), new Array(1332052, 4000244, 4000245, 4005002, 4005003), new Array(1472053, 4000244, 4000245, 4005002, 4005003));
            var matQtySet = new Array(new Array(1, 20, 25, 5, 3), new Array(1, 20, 25, 3, 5), new Array(1, 20, 25, 2, 6));
            var costSet = new Array(120000, 120000, 120000);
            item = itemSet[selectedItem];
            mats = matSet[selectedItem];
            matQty = matQtySet[selectedItem];
            cost = costSet[selectedItem];
        } else if (selectedType == 5) { //pirate weapon
            var itemSet = new Array(1482013, 1492013);
            var matSet = new Array(new Array(1482012, 4000244, 4000245, 4005000, 4005002), new Array(1492012, 4000244, 4000245, 4005000, 4005002));
            var matQtySet = new Array(new Array(1, 20, 25, 5, 3), new Array(1, 20, 25, 3, 5));
            var costSet = new Array(120000, 120000);
            item = itemSet[selectedItem];
            mats = matSet[selectedItem];
            matQty = matQtySet[selectedItem];
            cost = costSet[selectedItem];
        }

        var prompt = "你想要做一个 #t" + item + "#? 在这种情况下，为了要做出好品质的装备。请确保您有空间在您的装备栏！#b";

        if (stimulator) {
            stimID = getStimID(item);
            prompt += "\r\n#i" + stimID + "# 1 #t" + stimID + "#";
        }

        if (mats instanceof Array) {
            for (var i = 0; i < mats.length; i++) {
                prompt += "\r\n#i" + mats[i] + "# " + matQty[i] + " #t" + mats[i] + "#";
            }
        } else {
            prompt += "\r\n#i" + mats + "# " + matQty + " #t" + mats + "#";
        }

        if (cost > 0)
            prompt += "\r\n#i4031138# " + cost + " 金币";

        cm.sendYesNo(prompt);
    } else if (status == 3 && mode == 1) {
        var complete = true;

        if (cm.getMeso() < cost) {
            cm.sendOk("糟糕...你的钱好像不够哦...")
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

        if (stimulator) { //check for stimulator
            if (!cm.haveItem(stimID)) {
                complete = false;
            }
        }

        if (!complete)
            cm.sendOk("由于你没有足够的材料，所以我不帮忙做了。");
        else {
            if (mats instanceof Array) {
                for (var i = 0; i < mats.length; i++) {
                    cm.gainItem(mats[i], -matQty[i]);
                }
            } else
                cm.gainItem(mats, -matQty);

            cm.gainMeso(-cost);
            if (stimulator) { //check for stimulator
                cm.gainItem(stimID, -1);
                var deleted = Math.floor(Math.random() * 10);
                if (deleted != 0) {
                    cm.gainItem(item, 1, true)
                    cm.sendOk("完成。善待好你的武器，免得你使龙的愤怒.");
                } else {
                    cm.sendOk("不幸的是，龙的精髓...抵触你的武器。我很抱歉是我的疏失.....");
                }
            } else { //just give basic item
                cm.gainItem(item, 1);
                cm.sendOk("完成。善待好你的武器，免得你使龙的愤怒.");
            }
        }
        cm.dispose();
    } else if (status == 100) {
        cm.sendRepairWindow();
        cm.dispose();
    }
}

function getStimID(equipID) {
    var cat = Math.floor(equipID / 10000);
    var stimBase = 4130002; //stim for 1h sword

    switch (cat) {
        case 130: //1h sword, do nothing
            break;
        case 131: //1h axe
            stimBase++;
            break;
        case 132: //1h bw
            stimBase += 2;
            break;
        case 140: //2h sword
            stimBase += 3;
            break;
        case 141: //2h axe
            stimBase += 4;
            break;
        case 142: //2h bw
            stimBase += 5;
            break;
        case 143: //spear
            stimBase += 6;
            break;
        case 144: //polearm
            stimBase += 7;
            break;
        case 137: //wand
            stimBase += 8;
            break;
        case 138: //staff
            stimBase += 9;
            break;
        case 145: //bow
            stimBase += 10;
            break;
        case 146: //xbow
            stimBase += 11;
            break;
        case 133: //dagger
            stimBase += 12;
            break;
        case 147: //claw
            stimBase += 13;
            break;
        case 148: //knuckle
            stimBase += 14;
            break;
        case 149: //gun
            stimBase += 15;
            break;
    }

    return stimBase;
}
