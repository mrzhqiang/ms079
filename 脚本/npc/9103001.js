/**
	Rolly - Ludibirum Maze PQ
* */

var status = 0;
var minLevel = 51;
var maxLevel = 70;
var minPlayers = 3;
var maxPlayers = 6;
var time = 15;
var open = true;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 0) {
	cm.dispose();
    } else {
	if (mode == 1)
	    status++;
	else
	    status--;
		
	if (status == 0) {
	    cm.sendSimple("This is the entrance to the Ludibrium Maze. Enjoy!\r\n#b#L0#Enter the Lubidrium Maze#l\r\n#L1#What is the Ludibrium Maze?");
	 	
	} else if (status == 1) {
	    var em = cm.getEventManager("LudiMazePQ");
	    if(selection == 0) {//ENTER THE PQ
		if (!hasParty()) {//NO PARTY
		    cm.sendOk("Try taking on the Maze Quest with your party. If you DO decide to tackle it, please have your Party Leader notify me!");
		} else if (!isLeader()) {//NOT LEADER
		    cm.sendOk("Try taking on the Maze Quest with your party. If you DO decide to tackle it, please have your Party Leader notify me!");
		} else if (!checkPartySize()) {//PARTY SIZE WRONG
		    cm.sendOk("Your party needs to consist of at least " + minplayers + " members in order to tackle this maze");
		} else if (!checkPartyLevels()) {//WRONG LEVELS
		    cm.sendOk("One of your party members has not met the level requirements of " + minlvl + "~" + maxlvl + ".");
		} else if (em == null) {//EVENT ERROR
		    cm.sendOk("ERROR IN EVENT");
		} else if (!open){
		    cm.sendOk("The PQ is #rclosed#k for now.");
		} else {
		    cm.sendOk("You may enter");//ENTER PQ
		    em.startInstance(cm.getParty(), cm.getMap());
		    var party = cm.getEventInstance().getPlayers();
		    cm.removeFromParty(4001106, party);
		}
		cm.dispose();
	    } else if(selection == 1) {
		cm.sendOk("This maze is available to all parties of " + minplayers + " or more members, and all participants must be between Level " + minlvl + "~" + maxlvl + ".  You will be given " + time + " minutes to escape the maze.  At the center of the room, there will be a Warp Portal set up to transport you to a different room.  These portals will transport you to other rooms where you'll (hopefully) find the exit.  Pietri will be waiting at the exit, so all you need to do is talk to him, and he'll let you out.  Break all the boxes located in the room, and a monster inside the box will drop a coupon.  After escaping the maze, you will be awarded with EXP based on the coupons collected.  Additionally, if the leader possesses at least 30 coupons, then a special gift will be presented to the party.  If you cannot escape the maze within the allotted " + time +" minutes, you will receive 0 EXP for your time in the maze.  If you decide to log off while you're in the maze, you will be automatically kicked out of the maze.  Even if the members of the party leave in the middle of the quest, the remaining members will be able to continue on with the quest.  If you are in critical condition and unable to hunt down the monsters, you may avoid them to save yourself.  Your fighting spirit and wits will be tested!  Good luck!");
		cm.dispose();
	    }
	}
    }
}
     
function getPartySize(){
    if(cm.getParty() == null){
	return 0;
    }else{
	return (cm.getParty().getMembers().size());
    }
}

function isLeader(){
    return cm.isLeader();
}

function checkPartySize(){
    var size = 0;
    if (cm.getParty() == null){
	size = 0;
    }else{
	size = (cm.getParty().getMembers().size());
    }
    if(size < minplayers || size > maxplayers){
	return false;
    }else{
	return true;
    }
}

function checkPartyLevels(){
    var pass = true;
    var party = cm.getParty().getMembers();
    if(cm.getParty() == null){
	pass = false;
    }else{
	for (var i = 0; i < party.size() && pass; i++) {
	    if ((party.get(i).getLevel() < minlvl) || (party.get(i).getLevel() > maxlvl) || (party.get(i).getMapid() != cm.getMapId())) {
		pass = false;
	    }
	}
    }
    return pass;
}

function hasParty(){
    if(cm.getParty() == null){
	return false;
    }else{
	return true;
    }
}