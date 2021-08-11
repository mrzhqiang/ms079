var itemList = new Array(5010000, 2022468, 5010002, 2022468, 2022468, 2022468, 2022468, 5121008, 5121009, 5121010, 5010038, 5010039, 2022468, 5010041, 2022468, 5010043, 2022468, 1702100, 2022468, 5120012, 2022468, 5121007, 2022468, 5010050, 2022468, 5120014, 5010054, 5010052, 5010053, 2022468, 2022468, 5010051, 1702209, 2022468, 2022468, 5150028, 1702210, 1702166, 2022468, 1702166, 2022468, 2022468, 1452062, 1492030, 2022468, 2022468, 1472077, 1482029, 2022468, 1462056, 2022468, 1322065, 1442071, 5021012, 2022468, 5021013, 5021014, 1432050, 2022468, 2022468, 2022468, 2022468, 2022468, 2022468, 1402053, 1422039, 2022468, 2022468, 5021010, 2022468, 5120005, 2022468, 2022468, 5021011, 2022468, 2022468, 1412035, 1382062, 2022468, 2022468, 2022468, 2022468, 1372046, 1332081, 2022468, 2022468, 5160011, 2022468, 2022468, 1332032, 5120002, 2022468, 1052078, 1051131, 5120006, 2022468, 1051049, 1050119, 2022468, 1702088, 2022468, 1050019, 2022468, 2022468, 5160012, 2022468, 5160000, 2022468, 2022468, 5160013, 2022468, 5160003, 2022468, 5160004, 5160002, 1000026, 2022468, 1001036, 1002714, 1002876, 5110000, 5110000, 2022468, 5110000, 1002871, 1002872, 2022468, 2022468, 1002873, 1002874, 2022468, 1002720, 2022468, 2022465, 2022468, 2022466, 2022467, 2022468, 5021000, 2022468, 5160001, 5160005, 2022436, 2022437, 2022468, 2022438, 5010035, 5010034, 5160007, 5160006, 2022468, 2022468, 5010027, 5010021, 5010021, 5010022, 2022468, 5160010, 5010023, 5160009, 5010024, 5160008, 5010025, 2022119, 2022468, 2022122, 1302105, 1312039, 1002368, 2022428, 5160014, 5021015, 5010033, 5110000, 2022468, 2022468, 2022468);
var randNum = Math.floor(Math.random()*(itemList.length));
var randItem = itemList[randNum];
var status = -1;

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 0) {
	    cm.dispose();
		return;
	}
	status--;
    }
    switch(cm.getPlayer().getMapId()) {
	case 104040000:
		cm.saveLocation("CHRISTMAS");
		cm.warp(889100100);
		cm.dispose();
	    break;
	case 889100100:
    	if (status == 0) {
	        cm.sendSimple("安安 听说雪人被雪精灵中出了 请各位强大的中路保护雪人#b\r\n\r\n#L0#保护雪人 - 简单 (等级 10 以上 30等以下)#l\r\n#L1#保护雪人 - 中等 (等级 30 以上 70等以下)#l\r\n#L2#保护雪人 - 困难 (等级 70 以上)#l");
    	} else if (status == 1) {
                var level = cm.getPlayerStat("LVL");
	        if (selection == 0 && level <= 10 || level <= 30) {
		    cm.warp(889100000,0); //exit map lobby
		    cm.dispose();
		} else if (selection == 1 && level <= 30 || level <= 70) {
		    cm.warp(889100010,0); //exit map lobby
		    cm.dispose();
		} else if (selection == 2 && level >= 70) {
		    cm.warp(889100020,0); //exit map lobby
		    cm.dispose();
                } else {
		    cm.sendOk("你的等级不够或是超过");
		    cm.dispose();
                }
        }
	    break;
	case 889100000:
	case 889100010:
	case 889100020:
    	    if (status == 0) {
	        cm.sendSimple("勇士安安阿!#b\r\n\r\n#L0#我的巨型魔棒带好了 我要前往保护雪人#l");
    	    } else if (status == 1) {
			var s = ((cm.getMapId() % 100) / 10) | 0;
   		    var em = cm.getEventManager("Christmas");
    		    if (em == null) {
			cm.sendOk("Please try again later.");
			cm.dispose();
			return;
    		    }
		    if (cm.getPlayer().getParty() == null || !cm.isLeader()) {
			cm.sendOk("请队长来找我对话");
		    } else {
			var party = cm.getPlayer().getParty().getMembers();
			var mapId = cm.getPlayer().getMapId();
			var next = true;
			var size = 0;
			var it = party.iterator();
			while (it.hasNext()) {
				var cPlayer = it.next();
				var ccPlayer = cm.getPlayer().getMap().getCharacterById(cPlayer.getId());
				if (ccPlayer == null || ccPlayer.getLevel() < (s == 0 ? 10 : (s == 1 ? 30 : 70))) {
					next = false;
					break;
				}
				size++;
			}	
			if (next && size >= 4) {
		    		if (em.getInstance("Christmas" + s) == null) {
					em.startInstance_Party("" + s, cm.getPlayer());
		    		} else {
					cm.sendOk("请确认你的其他队员有没有在这边");
		    		}
			} else {
				cm.sendOk("必须要四人(含)以上");
			}
		    }
	        cm.dispose();
            }
	    break;
	case 889100001:
	case 889100011:
	case 889100021:
		if (cm.getPlayer().getEventInstance() == null) {
			cm.sendOk("中路最后希望 拜托你们了!");
		} else {
			if (!cm.getPlayer().getEventInstance().getProperty("stage").equals("1")) {
				cm.sendOk("中路最后希望 拜托你们了!");
			} else if (cm.getPlayer().getMap().countMonsterById(9400319) > 0 || cm.getPlayer().getMap().countMonsterById(9400320) > 0 || cm.getPlayer().getMap().countMonsterById(9400321) > 0) {
				cm.sendOk("中路最后希望 拜托你们了!");
			} else {
                        	cm.showEffect(false, "quest/party/clear");
                        	cm.playSound(false, "Party1/Clear");
				var s = ((cm.getMapId() % 100) / 10) | 0;
				cm.warp(cm.getMapId() + 1);
                        	cm.gainItem(randItem, 1);
				cm.gainExp((s == 0 ? 2500 : (s == 1 ? 7500 : 20000)));
			}
		}
		cm.dispose();
		break;
    }
}