/* Spiruna
 Orbis : Old Man's House (200050001)
 
 Refining NPC: 
 * Dark Crystal - Half Price compared to Vogen, but must complete quest 
 */

var status = 0;
var selectedItem = -1;

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
        if (cm.getQuestStatus(3034) == 2) {
            var selStr = "You've been so much of a help to me... If you have any Dark Crystal Ore, I can refine it for you for only #b500000 meso#k each."

            cm.sendYesNo(selStr);
        } else {
            cm.sendOk("Go away, I'm trying to meditate.");
            cm.dispose();
        }
    } else if (status == 1 && mode == 1) {
        cm.sendGetNumber("Okay, so how many do you want me to make?", 1, 1, 100);
    } else if (status == 2 && mode == 1) {
        var complete = true;
        var itemID = 4005004;
        var matID = 4004004;
        var matQty = 10;
        var cost = 500000;

        if (cm.getMeso() < cost * selection) {
            cm.sendOk("I'm sorry, but I am NOT doing this for free.")
        } else {
            if (!cm.haveItem(matID, matQty * selection)) {
                complete = false;
            }
        }

        if (!complete)
            cm.sendOk("I need that ore to refine the Crystal. No exceptions..");
        else {
            cm.gainItem(matID, -matQty * selection);
            cm.gainMeso(-cost * selection);
            cm.gainItem(itemID, selection);
            cm.sendOk("Use it wisely.");
        }
        cm.dispose();
    }
}