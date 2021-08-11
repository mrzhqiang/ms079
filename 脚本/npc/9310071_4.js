var status = 0;
var pet = null;
var theitems = Array();

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0) {
            cm.sendOk("需要的时候再来找我。");
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) {
            cm.sendSimple("您好，我是#p1032102# 有什么我可以帮您的吗？？ \r\n#r#L0#我要升级宝贝龙宠物#l#k\r\n#g#L1#我要升级我的机器人#l#k\r\n#b#L2#我要复活我的宠物#l#k\r\n#L3#我想要得到群宠技能#l#k\r\n#L4##d我要解坎特的提议#l#k");
        } else if (status == 1) {
            if (selection == 0) {
                var currentpet = null;
                for (var i = 0; i < 3; i++) {
                    currentpet = cm.getPlayer().getPet(i);
                    if (currentpet != null && pet == null) {
                        if (currentpet.getSummoned() && currentpet.getPetItemId() > 5000028 && currentpet.getPetItemId() < 5000034 && currentpet.getLevel() >= 15) {
                            pet = currentpet;
                            break;
                        }
                    }
                }
                if (pet == null || !cm.haveItem(5380000, 1)) {
                    cm.sendOk("您没有 #i5380000##t5380000#, 然后，宠物等级15级后我再告诉您怎么做。");
                    cm.dispose();
                } else {
                    var id = pet.getPetItemId();
                    var name = pet.getName();
                    var level = pet.getLevel();
                    var closeness = pet.getCloseness();
                    var fullness = pet.getFullness();
                    var slot = pet.getInventoryPosition();
                    var flag = pet.getFlags();
                    var rand = 0;
                    var after = id;
                    while (after == id) {
                        rand = 1 + Math.floor(Math.random() * 10);
                        if (rand >= 1 && rand <= 3) {
                            after = 5000030;
                        } else if (rand >= 4 && rand <= 6) {
                            after = 5000031;
                        } else if (rand >= 7 && rand <= 9) {
                            after = 5000032;
                        } else if (rand == 10) {
                            after = 5000033;
                        }
                    }
                    if (name.equals(cm.getItemName(id))) {
                        name = cm.getItemName(after);
                    }
                    cm.getPlayer().unequipPet(pet, false);
                    cm.gainItem(5380000, -1);
                    cm.removeSlot(5, slot, 1);
                    cm.gainPet(after, name, level, closeness, fullness, flag);
                    cm.getPlayer().spawnPet(slot);
                    cm.sendOk("您的龙已经进化了！它曾经是一只 #i" + id + "##t" + id + "#, 而现在是一只 #i" + after + "##t" + after + "#!");
                    cm.dispose();
                }
            } else if (selection == 1) {
                var currentpet = null;
                for (var i = 0; i < 3; i++) {
                    currentpet = cm.getPlayer().getPet(i);
                    if (currentpet != null && pet == null) {
                        if (currentpet.getSummoned() && currentpet.getPetItemId() > 5000047 && currentpet.getPetItemId() < 5000054 && currentpet.getLevel() >= 15) {
                            pet = currentpet;
                            break;
                        }
                    }
                }
                if (pet == null || !cm.haveItem(5380000, 1)) {
                    cm.sendOk("您没有 #i5380000##t5380000#, 然后，宠物等级15级后我再告诉您怎么做。");
                    cm.dispose();
                } else {
                    var id = pet.getPetItemId();
                    var name = pet.getName();
                    var level = pet.getLevel();
                    var closeness = pet.getCloseness();
                    var fullness = pet.getFullness();
                    var slot = pet.getInventoryPosition();
                    var flag = pet.getFlags();
                    var rand = 0;
                    var after = id;
                    while (after == id) {
                        rand = 1 + Math.floor(Math.random() * 9);
                        if (rand >= 1 && rand <= 2) {
                            after = 5000049;
                        } else if (rand >= 3 && rand <= 4) {
                            after = 5000050;
                        } else if (rand >= 5 && rand <= 6) {
                            after = 5000051;
                        } else if (rand >= 7 && rand <= 8) {
                            after = 5000052;
                        } else if (rand == 9) {
                            after = 5000053;
                        }
                    }
                    if (name.equals(cm.getItemName(id))) {
                        name = cm.getItemName(after);
                    }
                    cm.getPlayer().unequipPet(pet, false);
                    cm.gainItem(5380000, -1);
                    cm.removeSlot(5, slot, 1);
                    cm.gainPet(after, name, level, closeness, fullness, flag);
                    cm.getPlayer().spawnPet(slot);
                    cm.sendOk("您的龙已经进化了！它曾经是一只 #i" + id + "##t" + id + "#, 而现在是一只 #i" + after + "##t" + after + "#!");
                    cm.dispose();
                }
            } else if (selection == 3) {
                if (cm.haveItem(5380000, 15)) {
					if (cm.getPlayer().isKOC()) {
						cm.teachSkill(10000018, 1, 1);
						cm.gainItem(5380000, -15);
						cm.sendOk("已经学会啰！！");
						cm.dispose();
					} else if (cm.getPlayer().isAran()) {
						cm.teachSkill(20000024, 1, 1);
						cm.gainItem(5380000, -15);
						cm.sendOk("已经学会啰！！");
						cm.dispose();	
					} else {
						cm.teachSkill(0000008, 1, 1);
						cm.gainItem(5380000, -15);
						cm.sendOk("已经学会啰！！");
						cm.dispose();		
					}
                } else {
                    cm.sendOk("很抱歉我需要#t5380000# 15个。");
                    cm.dispose();
                }
			} else if (selection == 4) {
				if (cm.getQuestStatus(3096) == 2) {
					cm.sendOk("你已经完成过任务了。");
					cm.dispose();
				}
				if (cm.haveItem(4031274,1) && cm.haveItem(4031275,1) && cm.haveItem(4031276,1) && cm.haveItem(4031277,1)&& cm.haveItem(4031278,1)) {
					cm.removeAll(4031274);
					cm.removeAll(4031275);
					cm.removeAll(4031276);
					cm.removeAll(4031277);
					cm.removeAll(4031278);
					cm.forceCompleteQuest(3096);
					cm.gainExp(3000);
					cm.sendOk("完成坎特的提议。");
					cm.dispose();
				} else {
					cm.sendOk("我需要#i4031274# x1、#i4031275# x1 、 #i4031276# x1 、 #i4031277# x1 、 #i4031278# 麻烦准备好再来找我。");
					cm.dispose();
				}
            } else if (selection == 2) { //revive	
                var inv = cm.getInventory(5);
                var pets = cm.getPlayer().getPets(); //includes non-summon
                for (var i = 0; i <= inv.getSlotLimit(); i++) {
                    var it = inv.getItem(i);
                    if (it != null && it.getItemId() >= 5000000 && it.getItemId() < 5010000 && it.getExpiration() > 0 && it.getExpiration() < cm.getCurrentTime()) {
                        theitems.push(it);

                    }
                }
                if (theitems.length <= 0) {
                    cm.sendOk("你没有死掉的宠物");
                    cm.dispose();
                } else {
                    var selStr = "请选择一个想复活的宠物。#b\r\n";
                    for (var i = 0; i < theitems.length; i++) {
                        selStr += "\r\n#L" + i + "##v" + theitems[i].getItemId() + "##i" + theitems[i].getItemId() + "##l";
                    }
                    cm.sendSimple(selStr);
                }
            }
        } else if (status == 2) {
            if (theitems.length <= 0) {
                cm.sendOk("没有死亡的宠物。");
            } else if (!cm.haveItem(5180000, 1)) {
                cm.sendOk("我需要 #v5180000##i5180000#.");
            } else {
                theitems[selection].setExpiration(cm.getCurrentTime() + (45 * 24 * 60 * 60 * 1000));
                cm.getPlayer().fakeRelog();
                cm.sendOk("你的宠物已延长45天寿命 请好好对待他");
                cm.gainItem(5180000, -1);
            }
            cm.dispose();
        }
    }
}