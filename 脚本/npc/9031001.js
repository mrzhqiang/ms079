var status = -1;
var sel = -1;

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 0) {
	    cm.dispose();
	}
	status--;
    }
    if (status == 0) {
	cm.sendSimple("#b#L0#Learn/Unlearn Herbalism#l\r\n#L1#Trade Herb Roots#l");
    } else if (status == 1) {
	sel = selection;
	if (sel == 0) {
	    if (cm.getPlayer().getProfessionLevel(92020000) > 0 || cm.getPlayer().getProfessionLevel(92030000) > 0 || cm.getPlayer().getProfessionLevel(92040000) > 0) {
		cm.sendOk("Please unlearn Smithing/Accessory Crafting/Alchemy first.");
		cm.dispose();
		return;
	    }
	    if (cm.getPlayer().getProfessionLevel(92000000) > 0) {
		cm.sendYesNo("Are you sure you wish to unlearn Herbalism? You will lose all your EXP/levels in Herbalism.");
	    } else if (cm.getPlayer().getProfessionLevel(92010000) > 0) {
		cm.sendOk("You cannot learn or unlearn Herbalism because you already have Mining.");
		cm.dispose();
	    } else {
		cm.sendYesNo("Would you like to learn Herbalism?");
	    }
	} else if (sel == 1) {
	    if (!cm.haveItem(4022023, 100)) {
		cm.sendOk("You need 100 Herb Roots.");
 	    } else if (!cm.canHold(2028066, 1)) {
		cm.sendOk("Please make some USE space.");
	    } else {
		cm.sendOk("Thank you.");
		cm.gainItem(2028066, 1);
		cm.gainItem(4022023, -100);
	    } 
	    cm.dispose();
	}
    } else if (status == 2) {
	if (sel == 0) {
	    if (cm.getPlayer().getProfessionLevel(92000000) > 0) {
		cm.sendOk("You have unlearned Herbalism.");
		cm.teachSkill(92000000, 0, 0);
	    } else {
		cm.sendOk("You have learned Herbalism.");
		cm.teachSkill(92000000, 0x1000000, 0); //00 00 00 01
		if (cm.canHold(1502000,1)) {
			cm.gainItem(1502000,1);
		}
	    }
	    cm.dispose();
	}
    }
}