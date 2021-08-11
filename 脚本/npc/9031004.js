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
	cm.sendSimple("#b#L0#Learn/Unlearn Accessory Crafting#l");
    } else if (status == 1) {
	    if (cm.getPlayer().getProfessionLevel(92030000) > 0) {
		cm.sendYesNo("Are you sure you wish to unlearn Accessory Crafting? You will lose all your EXP/levels in Accessory Crafting.");
	    } else if (cm.getPlayer().getProfessionLevel(92020000) > 0 || cm.getPlayer().getProfessionLevel(92040000) > 0 || cm.getPlayer().getProfessionLevel(92010000) <= 0) {
		cm.sendOk("You cannot learn or unlearn Accessory Crafting because you already have Smithing or Alchemy or you don't have Mining..");
		cm.dispose();
	    } else {
		cm.sendYesNo("Would you like to learn Accessory Crafting?");
	    }
    } else if (status == 2) {
	    if (cm.getPlayer().getProfessionLevel(92030000) > 0) {
		cm.sendOk("You have unlearned Accessory Crafting.");
		cm.teachSkill(92030000, 0, 0);
	    } else {
		cm.sendOk("You have learned Accessory Crafting.");
		cm.teachSkill(92030000, 0x1000000, 0); //00 00 00 01
	    }
	    cm.dispose();
    }
}