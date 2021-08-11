/* ===========================================================
			Resonance
	NPC Name: 		Minister of Home Affairs
	Map(s): 		Mushroom Castle: Corner of Mushroom Forest(106020000)
	Description: 	Quest -  Over the Castle Wall (3)
=============================================================
Version 1.0 - Script Done.(18/7/2010)
=============================================================
*/

importPackage(Packages.client);

var status = -1;

function start(mode, type, selection) {
    status++;
	if (mode != 1) {
	    if(type == 1 && mode == 0)
		    status -= 2;
		else{
			qm.sendOk("This will be the only way for you to enter the castle. Please think it through");
			qm.dispose();
			return;
		}
	}
	if (status == 0)
		qm.sendNext("Ah! There might be a way... if you can utilize the spine vine that we have grown for the protection of our castle, then you just might be able to enter the premise!");
	if (status == 1)
		qm.sendAcceptDecline("If you can somehow eliminate the spines from the spine vine, then you'll be able to climb over the castle wall using the vine. Of course, that'll also require a Vine Remover...");
	if (status == 2)
		qm.sendOk("The #bSpine Remover#k is created out of extracts from mysterious herbs at the highlands of El Naths. King Pepe used these herbs to intoxicate the pigs and take over the Mushroom Forest. #bIntoxicated Pig Tail#k is where you'll find the extracts of the herb. Please gather up #b100 Intoxicated Pig Tails#k and take them over to #bMinister of Magic.#k");
	if (status == 3){
		//qm.forceStartQuest();
		//qm.forceStartQuest(2324, "1");
		qm.gainExp(11000);
		qm.sendOk("Good job navigating through the area.");
		qm.forceCompleteQuest();
		qm.dispose();
	}
}

function end(mode, type, selection) {
    status++;
	if (mode != 1) {
	    if(type == 1 && mode == 0)
		    status -= 2;
		else{
			qm.dispose();
			return;
		}
	}
	if (status == 0)
		qm.sendOk("Hmmm I see... so they have completely shut off the entrance and everything.");
	if (status == 1){
		qm.gainExp(11000);
		qm.sendOk("Good job navigating through the area.");
		qm.forceCompleteQuest();
		qm.dispose();
	}
}
	