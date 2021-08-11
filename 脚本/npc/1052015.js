var status = -1;
var sel = 0;

function action(mode, type, selection) {
	if (mode != 1) {
    		cm.dispose();
	} else {
		status++;
		if (cm.getPlayer().getBattler(0) == null) {
			cm.sendOk("You need a monster first.");
			cm.dispose();	
			return;
		}
		if (cm.getPlayer().getMapId() == 193000000) {
			if (status == 0) {
				cm.sendSimple("I can lead you to Battle Tower, where you will be pit against other monsters...\r\n\r\n#b#L0# #v03994115##l #L1# #v03994116##l #L2# #v03994117##l #L3# #v03994118##l");
			} else if (status == 1) {
				sel = selection;
				var num = 0;
				var averageLevel = 0;
				var battlers = cm.getPlayer().getBattlers();
				for (var i = 0; i < battlers.length; i++) {
					if (battlers[i] != null) {
						if (battlers[i].getLevel() > averageLevel) {
							averageLevel = battlers[i].getLevel();
						}
						num++;
					}
				}	
				averageLevel |= 0;
				var selStr = "#v0" + (3994115 + sel) + "#\r\nCurrently, you have " + num + " monsters with the highest level of " + averageLevel + ".\r\n\r\n#eThis mode will have the following rules and restrictions#n:\r\n- The number of monsters you have right now will be the number of monsters that each player will use.\r\n- You may not run or use balls on the trainer monsters.\r\n- You must have at least 3 monsters in your team.\r\n";
				if (sel == 0) {
					cm.sendNext(selStr + "- The highest level of your monsters must be above level 10.\r\n- You will face monsters anywhere from 10 levels below your highest level to your highest level.\r\n- If any of your monsters exceed level 150, they will be reset to level 150.\r\n- There is no reward in this mode except for EXP for your monsters.\r\n\r\nClick next to start this mode."); 
				} else if (sel == 1) {
					cm.sendNext(selStr + "- The highest level of your monsters must be above level 10.\r\n- You will face monsters anywhere from 5 levels below your highest level to 5 levels above your highest level.\r\n- If any of your monsters exceed level 150, they will be reset to level 150.\r\n- In addition to gaining EXP, you will also gain an item reward after every successful match.\r\n\r\nClick next to start this mode."); 
				} else if (sel == 2) {
					cm.sendNext(selStr + "- The highest level of your monsters must be above level 10.\r\n- You will face monsters anywhere from your highest level to 10 levels above your highest level.\r\n- If any of your monsters exceed level 150, they will be reset to level 150.\r\n- In addition to gaining EXP, you will also gain an item reward after every successful match.\r\n\r\nClick next to start this mode."); 
				} else if (sel == 3) {
					cm.sendNext(selStr + "- The highest level of your monsters must be above level 100.\r\n- You will face only bosses that are below your highest level.\r\n- In addition to gaining EXP, you will also gain an item reward after every successful match.\r\n\r\nClick next to start this mode."); 
				} else {
					cm.dispose();
				}
			} else if (status == 2) {
				cm.warp(925020010 + sel);
				cm.dispose();
			}
		} else if (cm.getPlayer().getMapId() == 925020010 || cm.getPlayer().getMapId() == 925020011 || cm.getPlayer().getMapId() == 925020012 || cm.getPlayer().getMapId() == 925020013) { //easy
			if (status == 0) {
				var num = 0;
				var averageLevel = 0;
				var battlers = cm.getPlayer().getBattlers();
				for (var i = 0; i < battlers.length; i++) {
					if (battlers[i] != null) {
						if (battlers[i].getLevel() > averageLevel) {
							averageLevel = battlers[i].getLevel();
						}
						num++;
					}
				}	
				averageLevel |= 0;
				var selStr = "#v0" + (3994115 + (cm.getPlayer().getMapId() - 925020010)) + "#\r\nCurrently, you have " + num + " monsters with the highest level of " + averageLevel + ".\r\n\r\n#eThis mode will have the following rules and restrictions#n:\r\n- The number of monsters you have right now will be the number of monsters that each player will use.\r\n- You may not run or use balls on the trainer monsters.\r\n- You will need at least 3 monsters to enter.\r\n";
				if (cm.getPlayer().getMapId() == 925020010) {
					cm.sendNext(selStr + "- The highest level of your monsters must be above level 10.\r\n- You will face monsters anywhere from 10 levels below your highest level to your highest level.\r\n- If any of your monsters exceed level 150, they will be reset to level 150.\r\n- There is no reward in this mode except for EXP for your monsters.\r\n\r\nClick next to start this mode."); 
				} else if (cm.getPlayer().getMapId() == 925020011) {
					cm.sendNext(selStr + "- The highest level of your monsters must be above level 10.\r\n- You will face monsters anywhere from 5 levels below your highest level to 5 levels above your highest level.\r\n- If any of your monsters exceed level 150, they will be reset to level 150.\r\n- In addition to gaining EXP, you will also gain an item reward after every successful match.\r\n\r\nClick next to start this mode."); 
				} else if (cm.getPlayer().getMapId() == 925020012) {
					cm.sendNext(selStr + "- The highest level of your monsters must be above level 10.\r\n- You will face monsters anywhere from your highest level to 10 levels above your highest level.\r\n- If any of your monsters exceed level 150, they will be reset to level 150.\r\n- In addition to gaining EXP, you will also gain an item reward after every successful match.\r\n\r\nClick next to start this mode."); 
				} else if (cm.getPlayer().getMapId() == 925020013) {
					cm.sendNext(selStr + "- The highest level of your monsters must be above level 100.\r\n- You will face only bosses that are below your highest level.\r\n- In addition to gaining EXP, you will also gain an item reward after every successful match.\r\n\r\nClick next to start this mode."); 
				} else {
					cm.dispose();
				}
			} else {
				if (cm.getPlayer().getMapId() == 925020010) {
					var npcTeam = cm.makeTeam(-10, 0, 10, 150);
					if (npcTeam == null) {
						cm.sendOk("You did not meet one or more of the requirements. Please check again.");
					} else {
						cm.preparePokemonBattle(npcTeam, 150);
					}
				} else if (cm.getPlayer().getMapId() == 925020011) {
					var npcTeam = cm.makeTeam(-5, 5, 10, 150);
					if (npcTeam == null) {
						cm.sendOk("You did not meet one or more of the requirements. Please check again.");
					} else {
						cm.preparePokemonBattle(npcTeam, 150);
					}
				} else if (cm.getPlayer().getMapId() == 925020012) {
					var npcTeam = cm.makeTeam(0, 10, 10, 150);
					if (!cm.canHold()) {
						cm.sendOk("Please make some inventory space in all inventories.");
					} else if (npcTeam == null) {
						cm.sendOk("You did not meet one or more of the requirements. Please check again.");
					} else {
						cm.preparePokemonBattle(npcTeam, 150);
					}
				} else if (cm.getPlayer().getMapId() == 925020013) {
					var npcTeam = cm.makeTeam(0, 0, 100, 200);
					if (!cm.canHold()) {
						cm.sendOk("Please make some inventory space in all inventories.");
					} else if (npcTeam == null) {
						cm.sendOk("You did not meet one or more of the requirements. Please check again.");
					} else {
						cm.preparePokemonBattle(npcTeam, 200);
					}
				}
				cm.dispose();
			}
		} else {
			cm.dispose();
		}
	}
}