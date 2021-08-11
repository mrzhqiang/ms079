var status = -1;

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
	cm.sendOk("You can get professions from the various NPCs in Ardentmill.\r\n\r\n1. Herbalism + Alchemy\r\n2. Mining + Smithing\r\n3. Mining + Accessory Crafting");
	cm.safeDispose();
    }
}