/*
All Time Temple portal
*/

var quest;
var tomap;
var uncompletedmap;

function enter(pi) {
    switch (pi.getMapId()) {

        // Green area
        case 270010100:
            quest = 3501;
            tomap = 270010110;
            uncompletedmap = 270010000;
            break;
        case 270010200:
            quest = 3502;
            tomap = 270010210;
            uncompletedmap = 270010110;
            break;
        case 270010300:
            quest = 3503;
            tomap = 270010310;
            uncompletedmap = 270010210;
            break;
        case 270010400:
            quest = 3504;
            tomap = 270010410;
            uncompletedmap = 270010310;
            break;
        case 270010500:
            quest = 3507;
            tomap = 270020000;
            uncompletedmap = 270010410;
            break;

        // Blue area
        case 270020100:
            quest = 3508;
            tomap = 270020110;
            uncompletedmap = 270020000;
            break;
        case 270020200:
            quest = 3509;
            tomap = 270020210;
            uncompletedmap = 270020110;
            break;
        case 270020300:
            quest = 3510;
            tomap = 270020310;
            uncompletedmap = 270020210;
            break;
        case 270020400:
            quest = 3511;
            tomap = 270020410;
            uncompletedmap = 270020310;
            break;
        case 270020500:
            quest = 3514;
            tomap = 270030000;
            uncompletedmap = 270020410;
            break;

        // Red zone
        case 270030100:
            quest = 3515;
            tomap = 270030110;
            uncompletedmap = 270030000;
            break;
        case 270030200:
            quest = 3516;
            tomap = 270030210;
            uncompletedmap = 270030110;
            break;
        case 270030300:
            quest = 3517;
            tomap = 270030310;
            uncompletedmap = 270030210;
            break;
        case 270030400:
            quest = 3518;
            tomap = 270030410;
            uncompletedmap = 270030310;
            break;
        case 270030500:
            quest = 3521;
            tomap = 270040000;
            uncompletedmap = 270030410;
            break;

        case 270040000:
            if (pi.haveItem(4032002)) {
		pi.playPortalSE();
                pi.warp(270040100, "out00");
                pi.gainItem(4032002, -1);
                pi.playerMessage("現在移動至神殿內部..");
                return true;
            } else {
                pi.playerMessage("有一股很強的力量阻擋著...");
                return false;
            }
            break;
        default:
            return false;
    }
    if (pi.getQuestStatus(quest) == 2) {
	pi.playPortalSE();
        pi.warp(tomap, "out00");
    } else {
	pi.playPortalSE();
        pi.warp(uncompletedmap, 0);
        pi.playerMessage("有一股很強的力量阻擋著...");
    }
    return true;
}