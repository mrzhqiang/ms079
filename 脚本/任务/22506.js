/*
 Description: 	Quest - Tasty Milk 3
 */

var status = -1;

function start(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        if (status == 1) {
            qm.sendOk("#b(You're too afraid to get closer. Come back later for the milk.)");
            qm.dispose();
            return;
        }
        status--;
    }
    if (status == 0) {
        qm.sendNext("#b(You ask the Milk Cow to give you some milk.");
    } else if (status == 1) {
        qm.askAcceptDecline("Moo...");
    } else if (status == 2) {
        qm.sendOk("#b(The Milk Cow gives you some milk. Go feed the milk to Mir.)#k");
        qm.gainItem(4032454, 1);
        qm.forceStartQuest();
        qm.dispose();
    }
}

function end(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        status--;
    }
    if (status == 0) {
        qm.sendOk("I'm so hungry, I have no strength left... Master, I'm so hungry I might shrivel up and really become a lizard. What's this? Water? You want me to fill my stomach with water? If you say so, master...\r\n\r\n#fUI/UIWindow.img/QuestIcon/4/0#\r\n#fUI/UIWindow.img/QuestIcon/10/0# 1 sp\r\n\r\n#fUI/UIWindow.img/QuestIcon/8/0#1420 exp");
    } else if (status == 1) {
        qm.forceCompleteQuest();
        qm.gainExp(1420);
        qm.getPlayer().gainSP(1, 0);
        qm.gainItem(4032454, -1);
        qm.sendNext("(Gulp, gulp, gulp)");
    } else if (status == 2) {
        qm.sendNextPrev("Wow, this is so good! What is this water called? Milk? Yum! I feel sooo strong now!");
    } else if (status == 3) {
        qm.sendPrev("Hey, it looks like you've become stronger too, master. Your HP and MP is much higher than when I first saw you.");
        qm.dispose();
    }
}