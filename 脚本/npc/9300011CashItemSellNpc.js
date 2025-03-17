//importPackage(Packages.tools);
//importPackage(Packages.constants);


var HighSmega = 15000000;
var ItemSmega = 15000000;
var Amulet = 15000000;
var Sp = 15000000;



var picked = 0;
var status = -1;
var itemid = -1;
var state = -1;


function start() {
    status = -1;
    action(1, 0, 0);
}


function action(mode, type, selection) {
    if (mode == 1)
        status++;
    else {
        cm.sendOk("谢谢光临，欢迎下次再来.");
        cm.dispose();
        return;
    }
    if (status == 0) {
        cm.sendSimple("嗨欢迎来到这里!~\r\n我是点数商品NPC\r\n你目前有 #r[" + cm.getMeso() + "]#k  金币\r\n#l请问您需要什么服务??#k\r\n#l\r\n#L101##b广播项#l\r\n#L102##b杂项类#l");
    } else if (status == 1) {
        state = selection;
        if (state == 101) {
            cm.sendSimple("你目前有 #r[" + cm.getMeso() + "]#k 金币#k \r 你想要什么东西呢??#k \r #L0##i5072000#高效能喇叭 - 1500万金币 \r\n#L1##i5076000#道具喇叭 - 1500万金币");
        } else if (state == 102) {
            cm.sendSimple("你目前有 #r[" + cm.getMeso() + "]#k 金币#k \r #L5##i5130000#护身符 - 1500万金币\r\n#L6##i5050001#1转技能重置卷轴 - 1500万金币\r\n#L7##i5050002#2转技能重置卷轴 - 1500万金币\r\n#L8##i5050003#3转技能重置卷轴 - 1500万金币\r\n#L9##i5050004#4转技能重置卷轴 - 1500万金币");
        }
    } else if (status == 2) {
        if (state == 101) {
            picked = selection;
            cm.sendGetNumber("请问您要买多少个呢??", 1, 1, 100);
        } else if (state == 102) {
            picked = selection;
            cm.sendGetNumber("请问您要买多少个呢??", 1, 1, 100);
        }
    } else if (status == 3) {
        tw = selection;
        if (state == 101) {
            if (tw * 0 != -0) {
                cm.sendOk("我只接受0以上的数字!");
                cm.dispose();
                return;
            }
            if (picked == 0) {
                cm.sendYesNo("这些#i5072000# 花您 " + tw * HighSmega + " 金币, 请问您确定要购买吗??");
            }
            if (picked == 1) {
                cm.sendYesNo("这些#i5076000# 花您 " + tw * ItemSmega + " 金币, 请问您确定要购买吗??");
            }
            if (picked == 2) {
                cm.sendYesNo("这些#i5072000# 花您 " + tw * HighSmega + " 金币, 请问您确定要购买吗??");
            }
            if (picked == 3) {
                cm.sendYesNo("这些方块将花您 " + tw * RMC + " 金币, 请问您确定要购买吗??");
            }
            if (picked == 4) {
                cm.sendYesNo("这些方块将花您 " + tw * EMC + " 金币, 请问您确定要购买吗??");
            }
        }
        if (state == 102) {
            if (tw * 0 != 0) {
                cm.sendOk("我只接受0以上的数字!");
                cm.dispose();
                return;
            }
            if (picked == 5) {
                cm.sendYesNo("这些#i5130000# 花您 " + tw * Amulet + " 金币, 请问您确定要购买吗??");
            }
            if (picked == 6) {
                cm.sendYesNo("这些#i5050001# 花您 " + tw * Sp + " 金币, 请问您确定要购买吗??");
            }
            if (picked == 7) {
                cm.sendYesNo("这些#i5050002# 花您 " + tw * Sp + " 金币, 请问您确定要购买吗??");
            }
            if (picked == 8) {
                cm.sendYesNo("这些#i5050003# 花您 " + tw * Sp + " 金币, 请问您确定要购买吗??");
            }
            if (picked == 9) {
                cm.sendYesNo("这些#i5050004# 花您 " + tw * Sp + " 金币, 请问您确定要购买吗??");
            }
        }
    } else if (status == 4) {
        if (state == 101) {
            if (picked == 0) {
                if (cm.getMeso() >= tw * HighSmega) {
                    cm.gainItem(5072000, tw);
                    cm.gainMeso(-(tw * HighSmega));
                    cm.sendOk("感谢你购买了 #i5072000# 花您 " + tw * HighSmega + " 金币，谢谢惠顾欢迎下次再来~~");
                    cm.dispose();
                } else {
                    cm.sendOk("您没有足够的金币!");
                    cm.dispose();
                }
            }
            if (picked == 1) {
                if (cm.getMeso() >= tw * ItemSmega) {
                    cm.gainItem(5076000, tw);
                    cm.gainMeso(-(tw * ItemSmega));
                    cm.sendOk("感谢你购买了 #i5076000# 花您 " + tw * ItemSmega + " 金币，谢谢惠顾欢迎下次再来~~");
                    cm.dispose();
                } else {
                    cm.sendOk("您没有足够的金币!");
                    cm.dispose();
                }
            }
            if (picked == 2) {
                if (cm.getPlayer().getCSPoints(1) >= tw * SMC) {
                    cm.gainItem(5062002, tw);
                    cm.getPlayer().modifyCSPoints(1, -tw * SMC);
                    cm.dispose();
                } else {
                    cm.sendOk("您没有足够的点数!");
                    cm.dispose();
                }
            }
            if (picked == 3) {
                if (cm.getPlayer().getCSPoints(1) >= tw * RMC) {
                    cm.gainItem(5062003, tw);
                    cm.getPlayer().modifyCSPoints(1, -tw * RMC);
                    cm.dispose();
                } else {
                    cm.sendOk("您没有足够的点数!");
                    cm.dispose();
                }
            }
            if (picked == 4) {
                if (cm.getPlayer().getCSPoints(1) >= tw * EMC) {
                    cm.gainItem(5062005, tw);
                    cm.getPlayer().modifyCSPoints(1, -tw * EMC);
                    cm.dispose();
                } else {
                    cm.sendOk("您没有足够的点数!");
                    cm.dispose();
                }
            }
        }
        if (state == 102) {
            if (picked == 5) {
                if (cm.getMeso() >= tw * Amulet) {
                    cm.gainItem(5130000, tw);
                    cm.gainMeso(-(tw * Amulet));
                    cm.sendOk("感谢你购买了 #i5130000# 花您 " + tw * Amulet + " 金币，谢谢惠顾欢迎下次再来~~");
                    cm.dispose();
                } else {
                    cm.sendOk("您没有足够的金币!");
                    cm.dispose();
                }
            }
            if (picked == 6) {
                if (cm.getMeso() >= tw * Sp) {
                    cm.gainItem(5050001, tw);
                    cm.gainMeso(-(tw * Sp));
                    cm.sendOk("感谢你购买了 #i5050001# 花您 " + tw * Sp + " 金币，谢谢惠顾欢迎下次再来~~");
                    cm.dispose();
                } else {
                    cm.sendOk("您没有足够的金币!");
                    cm.dispose();
                }
            }
            if (picked == 7) {
                if (cm.getMeso() >= tw * Sp) {
                    cm.gainItem(5050002, tw);
                    cm.gainMeso(-(tw * Sp));
                    cm.sendOk("感谢你购买了 #i5050002# 花您 " + tw * Sp + " 金币，谢谢惠顾欢迎下次再来~~");
                    cm.dispose();
                } else {
                    cm.sendOk("您没有足够的金币!");
                    cm.dispose();
                }
            }
            if (picked == 8) {
                if (cm.getMeso() >= tw * Sp) {
                    cm.gainItem(5050003, tw);
                    cm.gainMeso(-(tw * Sp));
                    cm.sendOk("感谢你购买了 #i5050003# 花您 " + tw * Sp + " 金币，谢谢惠顾欢迎下次再来~~");
                    cm.dispose();
                } else {
                    cm.sendOk("您没有足够的金币!");
                    cm.dispose();
                }
            }
            if (picked == 9) {
                if (cm.getMeso() >= tw * Sp) {
                    cm.gainItem(5050004, tw);
                    cm.gainMeso(-(tw * Sp));
                    cm.sendOk("感谢你购买了 #i5050004# 花您 " + tw * Sp + " 金币，谢谢惠顾欢迎下次再来~~");
                    cm.dispose();
                } else {
                    cm.sendOk("您没有足够的金币!");
                    cm.dispose();
                }

            }
        }
    }
}
