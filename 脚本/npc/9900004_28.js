/* global cm, text */

function start() {
    status = -1;

    action(1, 0, 0);
}
function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (status >= 0 && mode == 0) {

            cm.sendOk("#e#b需要征集队伍吗？你可以找我！");
            cm.dispose();
            return;
        }
        if (mode == 1) {
            status++;
        } else {
            status--;
        }
        if (status == 0) {
            var tex2 = "";
            var text = "";
            for (i = 0; i < 10; i++) {
                text += "";
            }
            text += "#e#r使用我的功能，可以为你发一次征求组队的喇叭！\r\n";
            text += "#L1##b英语学院副本<100000冒险币>#l\r\n\r\n";
            text += "#L2##b月妙组队副本<100000冒险币>#l\r\n\r\n";
            text += "#L3##b废弃都市组队副本<100000冒险币>#l\r\n\r\n";
            text += "#L4##b怪物嘉年华副本<100000冒险币>#l\r\n\r\n";
            text += "#L5##b玩具塔组队副本<100000冒险币>#l\r\n\r\n";
            text += "#L6##b天空女神塔组队副本<100000冒险币>#l\r\n\r\n";
            text += "#L7##b海盗组队副本<100000冒险币>#l\r\n\r\n";
            text += "#L8##b毒雾森林组队副本<100000冒险币>#l\r\n\r\n";
            text += "#L9##b罗密欧与朱丽叶副本<100000冒险币>#l\r\n\r\n";
            text += "#L10##b千年树精王遗迹Ⅱ<100000冒险币>#l\r\n\r\n";
            text += "#L11##b人偶师BOSS挑战<100000冒险币>#l\r\n\r\n";
            text += "#L12##b遗址公会对抗战<100000冒险币>#l\r\n\r\n";
            cm.sendSimple(text);
            
            //cm.sendSimple("\r\n\r\n#L1##k废弃都市征集喇叭<20000冒险币>#l#r\r\n\r\n#L3##b玩具城征集喇叭<30000冒险币>#l\r\n\r\n#L4##r#d天空女神塔征集喇叭<50000冒险币>\r\n\r\n#L5##d绯红征集喇叭<100000冒险币>")
        } else if (status == 1) {
            if (selection == 1) {
                if (cm.getPlayer().getLevel() >= 10 && cm.getPlayer().getMeso() >= 100000) {
                    cm.gainMeso(-100000);
                    cm.组队征集喇叭(1, "频道：" + cm.getC().getChannel() + ",玩家：" + cm.getPlayer().getName() + "，需要几名勇士一起完成[英语学院副本].");
                    cm.dispose();
                } else {
                    cm.sendOk("征集令发送失败了！\r\n可能的原因1：你的等级不足英语村副本最低等级10级的要求\r\n可能的原因2：你的冒险币不足十万，无法刷出征集令！");
                    cm.dispose();
                }
            } else if (selection == 2) {
                if (cm.getPlayer().getLevel() >= 10 && cm.getPlayer().getMeso() >= 100000) {
                    cm.gainMeso(-100000);
                    cm.组队征集喇叭(1, "频道：" + cm.getC().getChannel() + ",玩家：" + cm.getPlayer().getName() + "，需要几名勇士一起完成[月妙组队副本].");
                    cm.dispose();
                } else {
                    cm.sendOk("征集令发送失败了！\r\n可能的原因1：你的等级不足月妙组队副本最低等级10级的要求\r\n可能的原因2：你的冒险币不足十万，无法刷出征集令！");
                    cm.dispose();
                }
            } else if (selection == 3) {
                if (cm.getPlayer().getLevel() >= 21 && cm.getPlayer().getMeso() >= 100000) {
                    cm.gainMeso(-100000);
                    cm.组队征集喇叭(1, "频道：" + cm.getC().getChannel() + ",玩家：" + cm.getPlayer().getName() + "，需要几名勇士一起完成[废弃都市组队副本].");
                    cm.dispose();
                } else {
                    cm.sendOk("征集令发送失败了！\r\n可能的原因1：你的等级不足废弃都市组队副本最低等级21级的要求\r\n可能的原因2：你的冒险币不足十万，无法刷出征集令！");
                    cm.dispose();
                }
            } else if (selection == 4) {
                if (cm.getPlayer().getLevel() >= 30 && cm.getPlayer().getMeso() >= 100000) {
                    cm.gainMeso(-100000);
                    cm.组队征集喇叭(1, "频道：" + cm.getC().getChannel() + ",玩家：" + cm.getPlayer().getName() + "，需要几名勇士进行对抗比赛[怪物嘉年华副本].");
                    cm.dispose();
                } else {
                    cm.sendOk("征集令发送失败了！\r\n可能的原因1：你的等级不足怪物嘉年华副本最低等级30级的要求\r\n可能的原因2：你的冒险币不足十万，无法刷出征集令！");
                    cm.dispose();
                }
            } else if (selection == 5) {
                if (cm.getPlayer().getLevel() >= 35 && cm.getPlayer().getMeso() >= 100000) {
                    cm.gainMeso(-100000);
                    cm.组队征集喇叭(1, "频道：" + cm.getC().getChannel() + ",玩家：" + cm.getPlayer().getName() + "，需要几名勇士进行对抗比赛[玩具101组队副本].");
                    cm.dispose();
                } else {
                    cm.sendOk("征集令发送失败了！\r\n可能的原因1：你的等级不足玩具101组队副本最低等级35级的要求\r\n可能的原因2：你的冒险币不足十万，无法刷出征集令！");
                    cm.dispose();
                }
            } else if (selection == 6) {
                if (cm.getPlayer().getLevel() >= 51 && cm.getPlayer().getMeso() >= 100000) {
                    cm.gainMeso(-100000);
                    cm.组队征集喇叭(1, "频道：" + cm.getC().getChannel() + ",玩家：" + cm.getPlayer().getName() + "，需要几名勇士进行对抗比赛[天空女神塔组队副本].");
                    cm.dispose();
                } else {
                    cm.sendOk("征集令发送失败了！\r\n可能的原因1：你的等级不足天空女神塔组队副本最低等级51级的要求\r\n可能的原因2：你的冒险币不足十万，无法刷出征集令！");
                    cm.dispose();
                }
            } else if (selection == 7) {
                if (cm.getPlayer().getLevel() >= 55 && cm.getPlayer().getMeso() >= 100000) {
                    cm.gainMeso(-100000);
                    cm.组队征集喇叭(1, "频道：" + cm.getC().getChannel() + ",玩家：" + cm.getPlayer().getName() + "，需要几名勇士进行对抗比赛[海盗组队副本].");
                    cm.dispose();
                } else {
                    cm.sendOk("征集令发送失败了！\r\n可能的原因1：你的等级不足海盗组队副本最低等级55级的要求\r\n可能的原因2：你的冒险币不足十万，无法刷出征集令！");
                    cm.dispose();
                }
            } else if (selection == 8) {
                if (cm.getPlayer().getLevel() >= 70 && cm.getPlayer().getMeso() >= 100000) {
                    cm.gainMeso(-100000);
                    cm.组队征集喇叭(1, "频道：" + cm.getC().getChannel() + ",玩家：" + cm.getPlayer().getName() + "，需要几名勇士进行对抗比赛[毒物森林组队副本].");
                    cm.dispose();
                } else {
                    cm.sendOk("征集令发送失败了！\r\n可能的原因1：你的等级不足毒物森林组队副本最低等级70级的要求\r\n可能的原因2：你的冒险币不足十万，无法刷出征集令！");
                    cm.dispose();
                }
            } else if (selection == 9) {
                if (cm.getPlayer().getLevel() >= 100 && cm.getPlayer().getMeso() >= 100000) {
                    cm.gainMeso(-100000);
                    cm.组队征集喇叭(1, "频道：" + cm.getC().getChannel() + ",玩家：" + cm.getPlayer().getName() + "，需要几名勇士进行对抗比赛[罗密欧与朱丽叶副本].");
                    cm.dispose();
                } else {
                    cm.sendOk("征集令发送失败了！\r\n可能的原因1：你的等级不足罗密欧与朱丽叶副本最低等级100级的要求\r\n可能的原因2：你的冒险币不足十万，无法刷出征集令！");
                    cm.dispose();
                }
            } else if (selection == 10) {
                if (cm.getPlayer().getLevel() >= 120 && cm.getPlayer().getMeso() >= 100000) {
                    cm.gainMeso(-100000);
                    cm.组队征集喇叭(1, "频道：" + cm.getC().getChannel() + ",玩家：" + cm.getPlayer().getName() + "，需要几名勇士进行对抗比赛[千年树精王遗迹Ⅱ].");
                    cm.dispose();
                } else {
                    cm.sendOk("征集令发送失败了！\r\n可能的原因1：你的等级不足千年树精王遗迹Ⅱ副本最低等级120级的要求\r\n可能的原因2：你的冒险币不足十万，无法刷出征集令！");
                    cm.dispose();
                }
            } else if (selection == 11) {
                if (cm.getPlayer().getLevel() >= 130 && cm.getPlayer().getMeso() >= 100000) {
                    cm.gainMeso(-100000);
                    cm.组队征集喇叭(1, "频道：" + cm.getC().getChannel() + ",玩家：" + cm.getPlayer().getName() + "，需要几名勇士进行对抗比赛[人偶师BOSS挑战副本].");
                    cm.dispose();
                } else {
                    cm.sendOk("征集令发送失败了！\r\n可能的原因1：你的等级不足人偶师BOSS挑战副本最低等级130级的要求\r\n可能的原因2：你的冒险币不足十万，无法刷出征集令！");
                    cm.dispose();
                }
            } else if (selection == 12) {
                if (cm.getPlayer().getMeso() >= 100000) {
                    cm.gainMeso(-100000);
                    cm.组队征集喇叭(1, "频道：" + cm.getC().getChannel() + ",玩家：" + cm.getPlayer().getName() + "，需要几名勇士进行对抗比赛[遗址公会对抗联赛].");
                    cm.dispose();
                } else {
                    cm.sendOk("征集令发送失败了！\r\n可能的原因1：你的冒险币不足十万，无法刷出征集令！");
                    cm.dispose();
                }
            }
        }
    }
}


