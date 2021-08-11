/** Author: nejevoli
 NPC Name: 		NimaKin
 Map(s): 		Victoria Road : Ellinia (180000000)
 Description: 		Maxes out your stats and able to modify your equipment stats
 */
importPackage(java.lang);

var status = 0;
var slot = Array();
var stats = Array("力量", "敏捷", "智力", "幸运", "HP", "MP", "物理攻击", "魔法攻击", "物理防御", "魔法防御", "命中率", "回避率", "灵敏度", "移动速度", "跳跃力", "卷轴数", "黄金铁锤使用数", "使用卷轴数", "星星数", "浅能 1", "浅能 2", "浅能 3", "装备名字");
var selected;
var statsSel;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (status >= 0 && mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1)
        status++;
    else
        status--;

    if (status == 0) {
        if (cm.getPlayerStat("ADMIN") == 1) {
            cm.sendSimple("亲爱的#h \r\n管理员我能为您做什么呢？？#b\r\n#L0#帮我能力值加到全满！！#l\r\n#L1#帮我技能加到全满！！#l\r\n#L2#帮我修改装备数值！！#l\r\n#L4#帮我初始化AP/SP！#l#k");
        } else if (cm.getPlayerStat("GM") == 1) {
            cm.sendSimple("亲爱的#h \r\n管理员我能为您做什么呢？？#b\r\n#L0#帮我能力值加到全满！！#l\r\n#L1#帮我技能加到全满！！#l\r\n#L2#帮我修改装备数值！！#l\r\n#L4#帮我初始化AP/SP！#l#k");
        } else {
            cm.dispose();
        }
    } else if (status == 1) {
        if (selection == 0) {
            if (cm.getPlayerStat("GM") == 1) {
                cm.maxStats();
                cm.sendOk("已经帮您加满了！！");
            }
            cm.dispose();
        } else if (selection == 1) {
            //Beginner
            if (cm.getPlayerStat("GM") == 1) {
                cm.maxAllSkills();
            }
            cm.dispose();
        } else if (selection == 2 && cm.getPlayerStat("ADMIN") == 1) {
            var avail = "";
            for (var i = -1; i > -199; i--) {
                if (cm.getInventory(-1).getItem(i) != null) {
                    avail += "#L" + Math.abs(i) + "##t" + cm.getInventory(-1).getItem(i).getItemId() + "##l\r\n";
                }
                slot.push(i);
            }
            cm.sendSimple("想要修改哪一件装备能力值呢？？\r\n#b" + avail);
        } else if (selection == 3 && cm.getPlayerStat("ADMIN") == 1) {
            var eek = cm.getAllPotentialInfo();
            var avail = "";
            for (var ii = 0; ii < eek.size(); ii++) {
                avail += "#L" + eek.get(ii) + "#浅能 ID " + eek.get(ii) + "#l\r\n";
            }
            cm.sendSimple("请问想了解？？\r\n#b" + avail);
            status = 9;
        } else if (selection == 4) {
            cm.getPlayer().resetAPSP();
            cm.sendNext("完成，请换频道or重新登入。");
            cm.dispose();
        } else {
            cm.dispose();
        }
    } else if (status == 2 && cm.getPlayerStat("ADMIN") == 1) {
        selected = selection - 1;
        var text = "";
        for (var i = 0; i < stats.length; i++) {
            text += "#L" + i + "#" + stats[i] + "#l\r\n";
        }
        cm.sendSimple("你想要修改你的 #b#t" + cm.getInventory(-1).getItem(slot[selected]).getItemId() + "##k.\r\n想修改哪个能力值？？\r\n#b" + text);
    } else if (status == 3 && cm.getPlayerStat("ADMIN") == 1) {
        statsSel = selection;
        if (selection == 22) {
            cm.sendGetText("请问你想设置多少 #b#t" + cm.getInventory(-1).getItem(slot[selected]).getItemId() + "##k's " + stats[statsSel] + " 能力值?");
        } else {
            cm.sendGetNumber("请问你想设置 #b#t" + cm.getInventory(-1).getItem(slot[selected]).getItemId() + "##k's " + stats[statsSel] + " 多少能力值?", 0, 0, 32767);
        }
    } else if (status == 4 && cm.getPlayerStat("ADMIN") == 1) {
        cm.changeStat(slot[selected], statsSel, selection);
        cm.sendOk("你的 #b#t" + cm.getInventory(-1).getItem(slot[selected]).getItemId() + "##k's " + stats[statsSel] + " 已被设置为 " + selection + ".");
        cm.dispose();
        cm.getPlayer().fakeRelog();
    } else if (status == 10 && cm.getPlayerStat("ADMIN") == 1) {
        cm.sendSimple("#L3#" + cm.getPotentialInfo(selection) + "#l");
        status = 0;
    } else {
        cm.dispose();
    }
}