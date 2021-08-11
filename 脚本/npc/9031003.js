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
	cm.sendSimple("#b#L0#Learn/Unlearn Smithing#l");
    } else if (status == 1) {
	    if (cm.getPlayer().getProfessionLevel(92020000) > 0) {
		cm.sendYesNo("Are you sure you wish to unlearn Smithing? You will lose all your EXP/levels in Smithing.");
	    } else if (cm.getPlayer().getProfessionLevel(92030000) > 0 || cm.getPlayer().getProfessionLevel(92040000) > 0 || cm.getPlayer().getProfessionLevel(92010000) <= 0) {
		cm.sendOk("You cannot learn or unlearn Smithing because you already have Accessory Crafting or Alchemy or you don't have Mining.");
		cm.dispose();
	    } else {
		cm.sendYesNo("Would you like to learn Smithing?");
	    }
    } else if (status == 2) {
	    if (cm.getPlayer().getProfessionLevel(92020000) > 0) {
		cm.sendOk("You have unlearned Smithing.");
		cm.teachSkill(92020000, 0, 0);
	    } else {
		cm.sendOk("You have learned Smithing.");
		cm.teachSkill(92020000, 0x1000000, 0); //00 00 00 01
	    }
	    cm.dispose();
    }
}