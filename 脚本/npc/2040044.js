/* ==================
 脚本类型:  NPC	    
 脚本作者：故事丶     
 联系方式：840645183  
 =====================
 */
//importPackage(net.sf.cheryy.tools);
//importPackage(net.sf.cherry.server.life);
//importPackage(java.awt);
//任务道具清除 以免玩家刷道具
var PQItems = new Array(4001022, 4001023);
var status;

var exp = 80000;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else if (status == -1 && cm.isLeader()) {
        var eim = cm.getPlayer().getEventInstance();
        var leaderPreamble = eim.getProperty("crackLeaderPreamble");
        if (leaderPreamble == null) {
            eim.setProperty("crackLeaderPreamble", "done");
            cm.sendNext("欢迎来到 玩具之城 - (#r组队任务#k) #bBoss#k阶段\r\n\r\n请你和你的队员一起打败#r阿丽莎乐#k获得#b#z4001023##k然后交给我……知道阿丽莎乐怎么召唤吗?看到空中哪个老鼠没？消灭它#r阿丽莎乐#k就会出现……");
            cm.dispose();
        } else {
            if (cm.haveItem(4001023) && cm.isLeader()) {
                var eim = cm.getPlayer().getEventInstance();
                if (eim == null) {
                    cm.removeAll(PQItems[i]);
                } else if (cm.isLeader()) {
                    eim.disbandParty();
                    cm.getEventManager("LudiPQ").setProperty("LPQOpen", "true");
                } else {
                    eim.leftParty(cm.getPlayer());
                }
				cm.worldMessage(6,"玩家：["+cm.getName()+"]带领他的队伍完成了玩具组队副本！");
				cm.givePartyItems(4170005,1);
				if(cm.getPlayer().getmrfbrw() == 2){
							cm.givePartyFb(1);
					}
                cm.warpParty(922011100);
                cm.dispose();


//status = 0;
                //cm.sendSimple("感谢你们给我带来了#r#z4001023##k。我现在可以将你和你的队员传送到奖励地图,但是在这之前你必须做一个选择。那么告诉我……你和你的队友想做什么？\r\n\r\n#b#L0#领取奖励 (返回村庄)#l\r\n");
            } else {
                cm.sendNext("你确定给我带来了#r#z4001023##k？请检查一下自己的背包。如果你带来了#r#z4001023##k我可以将你和你的队员一起传送到奖励地图。怎么样？");
                cm.dispose();
            }
        }
    } else if (status == -1 && !cm.isLeader()) {
        cm.sendNext("欢迎来到 玩具之城 - (#r组队任务#k) #bBoss#k阶段\r\n\r\n请你和你的队员一起打败#r阿丽莎乐#k获得#b#z4001023#k#k然后交给我……知道阿丽莎乐怎么召唤吗?看到空中哪个老鼠没？消灭它#r阿丽莎乐#k就会出现……");
        cm.dispose();
         } else if (status == 0 && cm.isLeader()) {
        /*
         var eim = cm.getPlayer().getEventInstance();
         var em = cm.getEventManager("LudiPQ");
         clear(9, eim, cm);
         cm.gainItem(4001023, -1);
         var party = eim.getPlayers();
         cm.givePartyExp(exp, party);
         em.setProperty("entryPossible", "true");
         var endTime = new java.util.Date().getTime();
         var startTime = cm.getPlayer().getEventInstance().getProperty("startTime");
         var startTime2 = cm.getPlayer().getEventInstance().getProperty("s9start");
         
         var get30 = false;
         if (((endTime - startTime) < 600000))
         get30 = true;
         
         var get34 = false;
         if (((endTime - startTime2) < 90000))
         get34 = true;
         
         var giveNx = eim.getProperty("smugglers") != "true";
         eim.dispose();
         */

        //if (selection == 0) {
        //cm.warpParty(922010000, 0);
        //cm.dispose();
        /*
         bem = cm.getEventManager("LudiPQBonus");
         if (bem == null) {
         bem.startInstance(cm.getParty(), cm.getPlayer().getMap());
         cm.dispose();
         } else {
         cm.sendNext("配置文件没有开启。请联系管理员。");
         cm.dispose();
         }
         */

        //} 
    }
}

 function clear(stage, eim, cm) {
 eim.setProperty("stage" + stage.toString() + "status", "clear");
    cm.showEffect(true, "quest/party/clear");
    cm.playSound(true, "Party1/Clear");
    cm.environmentChange(true, "gate");
 var map = eim.getMapInstance(cm.getPlayer().getMapId());
 //map.broadcastMessage(packetglow);
 // stage 9 does not have a door, might be cause of DC error
 }
 
 function warpToBonus(eim, player, bonusMapId, giveNx) {
 if (giveNx) {
 //player.finishAchievement(28);
 //player.finishAchievement(36);
 }
 
 var bonusMap = cm.getPlayer().getClient().getChannelServer().getMapFactory().getMap(bonusMapId);
 eim.unregisterPlayer(player);
 player.changeMap(bonusMap, bonusMap.getPortal(0));
 }
 