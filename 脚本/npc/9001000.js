var points;

function start() {
    var record = cm.getQuestRecord(150001);
    points = record.getCustomData() == null ? "0" : record.getCustomData();

    cm.sendSimple("Would you like to have a taste of a relentless boss battle? If so you must definitely try this! Which of these difficulty levels do you want to take on?.... \n\r #b[TIP : The points will be saved on every defeat of bosses!]#k \n\r #b#L3#Current points#l#k \n\r\n\r\n #b#L0# #v03994115##l #L1# #v03994116##l #L2# #v03994117##l #L28# #v03994118##l  \n\r\r\n\r\n#fUI/UIWindow.img/QuestIcon/4/0# \n\r \n\r #b#L4##i1492023:#Trade 120,000 points (Timeless Blindness)#l#k \n\r #b#L5##i1472068:#Trade 120,000 points (Timeless Lampion)#l#k \n\r #b#L6##i1462050:#Trade 120,000 points (Timeless Black Beauty)#l#k \n\r #b#L7##i1452057:#Trade 120,000 points (Timeless Engaw)#l#k \n\r #b#L8##i1432047:#Trade 120,000 points (Timeless Alchupiz)#l#k \n\r #b#L9##i1382057:#Trade 120,000 points (Timeless Aeas Hand)#l#k \n\r #b#L10##i1372044:#Trade 120,000 points (Timeless Enreal Tear)#l#k \n\r #b#L11##i1332074:#Trade 120,000 points (Timeless Killic)#l#k \n\r #b#L12##i1332073:#Trade 120,000 points (Timeless Pescas)#l#k \n\r #b#L13##i1482023:#Trade 120,000 points (Timeless Equinox)#l#k \n\r #b#L14##i1442063:#Trade 120,000 points (Timeless Diesra)#l#k \n\r #b#L15##i1422037:#Trade 120,000 points (Timeless Bellocce)#l#k \n\r #b#L16##i1412033:#Trade 120,000 points (Timeless Tabarzin)#l#k \n\r #b#L17##i1402046:#Trade 120,000 points (Timeless Nibleheim)#l#k \n\r #b#L18##i1322060:#Trade 120,000 points (Timeless Allargando)#l#k \n\r #b#L19##i1312037:#Trade 120,000 points (Timeless Bardiche)#l#k \n\r #b#L20##i1302081:#Trade 120,000 points (Timeless Executioners)#l#k \n\r #b#L31##i1342011:#Trade 120,000 points (Timeless Katara)#l#k \n\r #b#L21##i2070018:#Trade 200,000 points (Balanced Fury)#l#k \n\r #b#L22# #i1122017:#Trade 10,000 points (Fairy Pendant, lasts 1 day)#l#k \n\r #b#L23# #i2022459:#Trade 2,000 points (Cassandra Reward 1)#l#k \n\r #b#L24# #i2022460:#Trade 5,000 points (Cassandra Reward 2)#l#k \n\r #b#L25# #i2022461:#Trade 5,000 points (Cassandra Reward 3)#l#k \n\r #b#L26# #i2022462:#Trade 5,000 points (Cassandra Reward 4)#l#k \n\r #b#L27# #i2022463:#Trade 5,000 points (Cassandra Reward 5)#l#k \n\r #b#L28##i2340000:#Trade 100,000 points (White Scroll)#l#k \n\r #b#L29##i5490001:#Trade 20,000 points (Silver Key)#l#k \n\r #b#L30##i5490000:#Trade 30,000 points (Gold Key)#l#k");

}

function action(mode, type, selection) {
    if (mode == 1) {
	switch (selection) {
	    case 0:
		if (cm.getParty() != null) {
		if (cm.getDisconnected("BossQuestEASY") != null) {
			cm.getDisconnected("BossQuestEASY").registerPlayer(cm.getPlayer());
		 } else if (cm.isLeader()) {
			var q = cm.getEventManager("BossQuestEASY");
			if (q == null) {
			    cm.sendOk("Unknown error occured");
			} else {
			    q.startInstance(cm.getParty(), cm.getMap());
			}
		    } else {
			cm.sendOk("You are not the leader of the party, please ask your leader to talk to me.");
		    }
		} else {
		    cm.sendOk("Please form a party first.");
		}
		break;
	    case 1:
		if (cm.getParty() != null) {
		if (cm.getDisconnected("BossQuestMed") != null) {
			cm.getDisconnected("BossQuestMed").registerPlayer(cm.getPlayer());
		 } else if (cm.isLeader()) {
			var q = cm.getEventManager("BossQuestMed");
			if (q == null) {
			    cm.sendOk("Unknown error occured");
			} else {
			    q.startInstance(cm.getParty(), cm.getMap());
			}
		    } else {
			cm.sendOk("You are not the leader of the party, please ask your leader to talk to me.");
		    }
		} else {
		    cm.sendOk("Please form a party first.");
		}
		break;
	    case 2:
		if (cm.getParty() != null) {
		if (cm.getDisconnected("BossQuestHARD") != null) {
			cm.getDisconnected("BossQuestHARD").registerPlayer(cm.getPlayer());
		 } else if (cm.isLeader()) {
			var q = cm.getEventManager("BossQuestHARD");
			if (q == null) {
			    cm.sendOk("Unknown error occured");
			} else {
			    q.startInstance(cm.getParty(), cm.getMap());
			}
		    } else {
			cm.sendOk("You are not the leader of the party, please ask your leader to talk to me.");
		    }
		} else {
		    cm.sendOk("Please form a party first.");
		}
		break;
	    case 28:
		if (cm.getParty() != null) {
		if (cm.getDisconnected("BossQuestHELL") != null) {
			cm.getDisconnected("BossQuestHELL").registerPlayer(cm.getPlayer());
		 } else if (cm.isLeader()) {
			var q = cm.getEventManager("BossQuestHELL");
			if (q == null) {
			    cm.sendOk("Unknown error occured");
			} else {
			    q.startInstance(cm.getParty(), cm.getMap());
			}
		    } else {
			cm.sendOk("You are not the leader of the party, please ask your leader to talk to me.");
		    }
		} else {
		    cm.sendOk("Please form a party first.");
		}
		break;
	    case 3:
		cm.sendOk("#bCurrent Points : " + points);
		break;
	    case 4: // Timeless Blindness
		var record = cm.getQuestRecord(150001);
		var intPoints = parseInt(points);

		if (intPoints >= 120000) {
		    if (cm.canHold(1492023)) {
			intPoints -= 120000;
			record.setCustomData(""+intPoints+"");
			cm.gainItem(1492023, 1);
			cm.sendOk("Enjoy your rewards :P");
		    } else {
			cm.sendOk("Please check if you have sufficient inventory slot for it.")
		    }
		} else {
		    cm.sendOk("Please check if you have sufficient points for it, #bCurrent Points : " + points);
		}
		break;
	    case 5: // Timeless Lampion
		var record = cm.getQuestRecord(150001);
		var intPoints = parseInt(points);

		if (intPoints >= 120000) {
		    if (cm.canHold(1472068)) {
			intPoints -= 120000;
			record.setCustomData(""+intPoints+"");
			cm.gainItem(1472068, 1);
			cm.sendOk("Enjoy your rewards :P");
		    } else {
			cm.sendOk("Please check if you have sufficient inventory slot for it.")
		    }
		} else {
		    cm.sendOk("Please check if you have sufficient points for it, #bCurrent Points : " + points);
		}
		break;
	    case 6: // Timeless Black Beauty
		var record = cm.getQuestRecord(150001);
		var intPoints = parseInt(points);

		if (intPoints >= 120000) {
		    if (cm.canHold(1462050)) {
			intPoints -= 120000;
			record.setCustomData(""+intPoints+"");
			cm.gainItem(1462050, 1);
			cm.sendOk("Enjoy your rewards :P");
		    } else {
			cm.sendOk("Please check if you have sufficient inventory slot for it.")
		    }
		} else {
		    cm.sendOk("Please check if you have sufficient points for it, #bCurrent Points : " + points);
		}
		break;
	    case 7: // Timeless Engaw
		var record = cm.getQuestRecord(150001);
		var intPoints = parseInt(points);

		if (intPoints >= 120000) {
		    if (cm.canHold(1452057)) {
			intPoints -= 120000;
			record.setCustomData(""+intPoints+"");
			cm.gainItem(1452057, 1);
			cm.sendOk("Enjoy your rewards :P");
		    } else {
			cm.sendOk("Please check if you have sufficient inventory slot for it.")
		    }
		} else {
		    cm.sendOk("Please check if you have sufficient points for it, #bCurrent Points : " + points);
		}
		break;
	    case 8: // Timeless Alchupiz
		var record = cm.getQuestRecord(150001);
		var intPoints = parseInt(points);

		if (intPoints >= 120000) {
		    if (cm.canHold(1432047)) {
			intPoints -= 120000;
			record.setCustomData(""+intPoints+"");
			cm.gainItem(1432047, 1);
			cm.sendOk("Enjoy your rewards :P");
		    } else {
			cm.sendOk("Please check if you have sufficient inventory slot for it.")
		    }
		} else {
		    cm.sendOk("Please check if you have sufficient points for it, #bCurrent Points : " + points);
		}
		break;
	    case 9: // Timeless Aeas Hand
		var record = cm.getQuestRecord(150001);
		var intPoints = parseInt(points);

		if (intPoints >= 120000) {
		    if (cm.canHold(1382057)) {
			intPoints -= 120000;
			record.setCustomData(""+intPoints+"");
			cm.gainItem(1382057, 1);
			cm.sendOk("Enjoy your rewards :P");
		    } else {
			cm.sendOk("Please check if you have sufficient inventory slot for it.")
		    }
		} else {
		    cm.sendOk("Please check if you have sufficient points for it, #bCurrent Points : " + points);
		}
		break;
    	    case 10: // Timeless Enreal Tear
		var record = cm.getQuestRecord(150001);
		var intPoints = parseInt(points);

		if (intPoints >= 120000) {
		    if (cm.canHold(1372044)) {
			intPoints -= 120000;
			record.setCustomData(""+intPoints+"");
			cm.gainItem(1372044, 1);
			cm.sendOk("Enjoy your rewards :P");
		    } else {
			cm.sendOk("Please check if you have sufficient inventory slot for it.")
		    }
		} else {
		    cm.sendOk("Please check if you have sufficient points for it, #bCurrent Points : " + points);
		}
		break;
  	    case 11: // Timeless Killic
		var record = cm.getQuestRecord(150001);
		var intPoints = parseInt(points);

		if (intPoints >= 120000) {
		    if (cm.canHold(1332074)) {
			intPoints -= 120000;
			record.setCustomData(""+intPoints+"");
			cm.gainItem(1332074, 1);
			cm.sendOk("Enjoy your rewards :P");
		    } else {
			cm.sendOk("Please check if you have sufficient inventory slot for it.")
		    }
		} else {
		    cm.sendOk("Please check if you have sufficient points for it, #bCurrent Points : " + points);
		}
		break;
	    case 12: // Timeless Pescas
		var record = cm.getQuestRecord(150001);
		var intPoints = parseInt(points);

		if (intPoints >= 120000) {
		    if (cm.canHold(1332073)) {
			intPoints -= 120000;
			record.setCustomData(""+intPoints+"");
			cm.gainItem(1332073, 1);
			cm.sendOk("Enjoy your rewards :P");
		    } else {
			cm.sendOk("Please check if you have sufficient inventory slot for it.")
		    }
		} else {
		    cm.sendOk("Please check if you have sufficient points for it, #bCurrent Points : " + points);
		}
		break;
	    case 13: // Timeless Equinox
		var record = cm.getQuestRecord(150001);
		var intPoints = parseInt(points);

		if (intPoints >= 120000) {
		    if (cm.canHold(1482023)) {
			intPoints -= 120000;
			record.setCustomData(""+intPoints+"");
			cm.gainItem(1482023, 1);
			cm.sendOk("Enjoy your rewards :P");
		    } else {
			cm.sendOk("Please check if you have sufficient inventory slot for it.")
		    }
		} else {
		    cm.sendOk("Please check if you have sufficient points for it, #bCurrent Points : " + points);
		}
		break;
	    case 14: // Timeless Diesra
		var record = cm.getQuestRecord(150001);
		var intPoints = parseInt(points);

		if (intPoints >= 120000) {
		    if (cm.canHold(1442063)) {
			intPoints -= 120000;
			record.setCustomData(""+intPoints+"");
			cm.gainItem(1442063, 1);
			cm.sendOk("Enjoy your rewards :P");
		    } else {
			cm.sendOk("Please check if you have sufficient inventory slot for it.")
		    }
		} else {
		    cm.sendOk("Please check if you have sufficient points for it, #bCurrent Points : " + points);
		}
		break;
	    case 15: // Timeless Bellocce
		var record = cm.getQuestRecord(150001);
		var intPoints = parseInt(points);

		if (intPoints >= 120000) {
		    if (cm.canHold(1422037)) {
			intPoints -= 120000;
			record.setCustomData(""+intPoints+"");
			cm.gainItem(1422037, 1);
			cm.sendOk("Enjoy your rewards :P");
		    } else {
			cm.sendOk("Please check if you have sufficient inventory slot for it.")
		    }
		} else {
		    cm.sendOk("Please check if you have sufficient points for it, #bCurrent Points : " + points);
		}
		break;
	    case 16: // Timeless Tabarzin
		var record = cm.getQuestRecord(150001);
		var intPoints = parseInt(points);

		if (intPoints >= 120000) {
		    if (cm.canHold(1412033)) {
			intPoints -= 120000;
			record.setCustomData(""+intPoints+"");
			cm.gainItem(1412033, 1);
			cm.sendOk("Enjoy your rewards :P");
		    } else {
			cm.sendOk("Please check if you have sufficient inventory slot for it.")
		    }
		} else {
		    cm.sendOk("Please check if you have sufficient points for it, #bCurrent Points : " + points);
		}
		break;
	    case 17: // Timeless Nibleheim
		var record = cm.getQuestRecord(150001);
		var intPoints = parseInt(points);

		if (intPoints >= 120000) {
		    if (cm.canHold(1402046)) {
			intPoints -= 120000;
			record.setCustomData(""+intPoints+"");
			cm.gainItem(1402046, 1);
			cm.sendOk("Enjoy your rewards :P");
		    } else {
			cm.sendOk("Please check if you have sufficient inventory slot for it.")
		    }
		} else {
		    cm.sendOk("Please check if you have sufficient points for it, #bCurrent Points : " + points);
		}
		break;
	    case 18: // Timeless Allargando
		var record = cm.getQuestRecord(150001);
		var intPoints = parseInt(points);

		if (intPoints >= 120000) {
		    if (cm.canHold(1322060)) {
			intPoints -= 120000;
			record.setCustomData(""+intPoints+"");
			cm.gainItem(1322060, 1);
			cm.sendOk("Enjoy your rewards :P");
		    } else {
			cm.sendOk("Please check if you have sufficient inventory slot for it.")
		    }
		} else {
		    cm.sendOk("Please check if you have sufficient points for it, #bCurrent Points : " + points);
		}
		break;
	    case 19: // Timeless Bardiche
		var record = cm.getQuestRecord(150001);
		var intPoints = parseInt(points);

		if (intPoints >= 120000) {
		    if (cm.canHold(1312037)) {
			intPoints -= 1312037;
			record.setCustomData(""+intPoints+"");
			cm.gainItem(2049100, 1);
			cm.sendOk("Enjoy your rewards :P");
		    } else {
			cm.sendOk("Please check if you have sufficient inventory slot for it.")
		    }
		} else {
		    cm.sendOk("Please check if you have sufficient points for it, #bCurrent Points : " + points);
		}
		break;
	    case 20: // Timeless Executioners
		var record = cm.getQuestRecord(150001);
		var intPoints = parseInt(points);

		if (intPoints >= 120000) {
		    if (cm.canHold(1302081)) {
			intPoints -= 120000;
			record.setCustomData(""+intPoints+"");
			cm.gainItem(1302081, 1);
			cm.sendOk("Enjoy your rewards :P");
		    } else {
			cm.sendOk("Please check if you have sufficient inventory slot for it.")
		    }
		} else {
		    cm.sendOk("Please check if you have sufficient points for it, #bCurrent Points : " + points);
		}
		break;		
	    case 21: // Balanced Fury
		var record = cm.getQuestRecord(150001);
		var intPoints = parseInt(points);

		if (intPoints >= 150000) {
		    if (cm.canHold(2070018)) {
			intPoints -= 150000;
			record.setCustomData(""+intPoints+"");
			cm.gainItem(2070018, 1);
			cm.sendOk("Enjoy your rewards :P");
		    } else {
			cm.sendOk("Please check if you have sufficient inventory slot for it.")
		    }
		} else {
		    cm.sendOk("Please check if you have sufficient points for it, #bCurrent Points : " + points);
		}
		break;
	    case 22: // Fairy Pendant
		var record = cm.getQuestRecord(150001);
		var intPoints = parseInt(points);

		if (intPoints >= 10000) {
		    if (cm.canHold(1122017)) {
			intPoints -= 10000;
			record.setCustomData(""+intPoints+"");
			cm.gainItemPeriod(1122017, 1, 1);
			cm.sendOk("Enjoy your rewards :P");
		    } else {
			cm.sendOk("Please check if you have sufficient inventory slot for it.")
		    }
		} else {
		    cm.sendOk("Please check if you have sufficient points for it, #bCurrent Points : " + points);
		}
		break;
	    case 23: // Cassandra Reward
		var record = cm.getQuestRecord(150001);
		var intPoints = parseInt(points);

		if (intPoints >= 2000) {
		    if (cm.canHold(2022459)) {
			intPoints -= 2000;
			record.setCustomData(""+intPoints+"");
			cm.gainItem(2022459, 1);
			cm.sendOk("Enjoy your rewards :P");
		    } else {
			cm.sendOk("Please check if you have sufficient inventory slot for it.")
		    }
		} else {
		    cm.sendOk("Please check if you have sufficient points for it, #bCurrent Points : " + points);
		}
		break;
	    case 24: // Cassandra Reward
	    case 25: // Cassandra Reward
	    case 26: // Cassandra Reward
	    case 27: // Cassandra Reward
		var record = cm.getQuestRecord(150001);
		var intPoints = parseInt(points);

		if (intPoints >= 5000) {
		    if (cm.canHold(2022436 + selection)) {
			intPoints -= 5000;
			record.setCustomData(""+intPoints+"");
			cm.gainItem(2022436 + selection, 1);
			cm.sendOk("Enjoy your rewards :P");
		    } else {
			cm.sendOk("Please check if you have sufficient inventory slot for it.")
		    }
		} else {
		    cm.sendOk("Please check if you have sufficient points for it, #bCurrent Points : " + points);
		}
		break;
	    case 28:
		var record = cm.getQuestRecord(150001);
		var intPoints = parseInt(points);

		if (intPoints >= 100000) {
		    if (cm.canHold(2340000)) {
			intPoints -= 100000;
			record.setCustomData(""+intPoints+"");
			cm.gainItem(2340000, 1);
			cm.sendOk("Enjoy your rewards :P");
		    } else {
			cm.sendOk("Please check if you have sufficient inventory slot for it.")
		    }
		} else {
		    cm.sendOk("Please check if you have sufficient points for it, #bCurrent Points : " + points);
		}
		break;	
	    case 29:
		var record = cm.getQuestRecord(150001);
		var intPoints = parseInt(points);

		if (intPoints >= 20000) {
		    if (cm.canHold(5490001)) {
			intPoints -= 20000;
			record.setCustomData(""+intPoints+"");
			cm.gainItem(5490001, 1);
			cm.sendOk("Enjoy your rewards :P");
		    } else {
			cm.sendOk("Please check if you have sufficient inventory slot for it.")
		    }
		} else {
		    cm.sendOk("Please check if you have sufficient points for it, #bCurrent Points : " + points);
		}
		break;	
	    case 30:
		var record = cm.getQuestRecord(150001);
		var intPoints = parseInt(points);

		if (intPoints >= 30000) {
		    if (cm.canHold(5490000)) {
			intPoints -= 30000;
			record.setCustomData(""+intPoints+"");
			cm.gainItem(5490000, 1);
			cm.sendOk("Enjoy your rewards :P");
		    } else {
			cm.sendOk("Please check if you have sufficient inventory slot for it.")
		    }
		} else {
		    cm.sendOk("Please check if you have sufficient points for it, #bCurrent Points : " + points);
		}
		break;	
	    case 31: // Timeless Katara
		var record = cm.getQuestRecord(150001);
		var intPoints = parseInt(points);

		if (intPoints >= 120000) {
		    if (cm.canHold(1342011)) {
			intPoints -= 120000;
			record.setCustomData(""+intPoints+"");
			cm.gainItem(1342011, 1);
			cm.sendOk("Enjoy your rewards :P");
		    } else {
			cm.sendOk("Please check if you have sufficient inventory slot for it.")
		    }
		} else {
		    cm.sendOk("Please check if you have sufficient points for it, #bCurrent Points : " + points);
		}
		break;	
	}
    }
    cm.dispose();
}