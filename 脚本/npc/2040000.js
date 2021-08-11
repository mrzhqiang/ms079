/*
	Mel - Ludibrium Ticketing Place(220000100)
*/

var cost = 6000;
var status = 0;

function start() {
    cm.sendYesNo("你好,我是码头服务员乔伊。你想离开玩具之城到天空之城吗? 从这站到艾纳斯大陆的#b天空之城#k的船只\r需要花费#b"+cost+" 金币#k 购买#b#t4031045##k 才可以启航.");
}

function action(mode, type, selection) {
    if(mode == -1)
        cm.dispose();
    else {
        if(mode == 1)
            status++;
        if(mode == 0) {
            cm.sendNext("你有一些经济的负担而无法搭船对吧?");
            cm.dispose();
            return;
        }
        if(status == 1) {
            if(cm.getMeso() >= cost && cm.canHold(4031045)) {
                cm.gainItem(4031045,1);
                cm.gainMeso(-cost);
            } else
                cm.sendOk("请问你有 #b"+cost+" 金币#k? 如果有的话,我劝您检查下身上其他栏位看是否有没有满了.");
            cm.dispose();
        }
    }
}
