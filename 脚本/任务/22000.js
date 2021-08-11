var status = -1;

function start(mode, type, selection) {
    status++;
	if (mode != 1) {
	    if(type == 1 && mode == 0)
		    status -= 2;
		else{
			qm.sendNext("Hm? Don't you want to tell Utah? You have to be nice to your brother, dear.");
			qm.dispose();
			return;
		}
	}
	if (status == 0)
		qm.sendNext("Did you sleep well,Evan?");
	else if (status == 1)
		qm.PlayerToNpc("#bYes,what about you,Mom?#k");
	else if (status == 2)
		qm.sendNextPrev("I did as well. But you seem so tired. Are you sure you slept okay? Did the thunder and lightning last night keep you up?");
	else if (status == 3) 
		qm.PlayerToNpc("#bOh, no. It's not that, Mom. I just had a strange dream last night.#k");
	else if (status == 4)
		qm.sendNextPrev("A strange dream? What kind of strange dream?");
	else if (status == 5)
		qm.PlayerToNpc("#bWell...#k");
	else if (status == 6)
		qm.PlayerToNpc("#b(You explain that you met a dragon in your dream.)");
	else if (status == 7)
		qm.sendAcceptDecline("Hahaha, a dragon? That's incredible. I'm glad he didn't swallow you whole! You should tell Utah about your dream. I'm sure he'll enjoy it.");
	else if (status == 8){
		qm.forceStartQuest();
		qm.sendNext("#bUtah#k went to the #bFront Porch#k to feed the Bull Dog. You'll see him right outside.");
   }else if (status == 9){
		qm.evanTutorial("UI/tutorial/evan/1/0", 1);
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
		qm.sendNext("Hey, Evan. You up? What's with the dark circles under your eyes? Didn't sleep well? Huh? A strange dream? What was it about? Whoa? A dream about a dragon?");
	if (status == 1)
		qm.sendNextPrev("Muahahahahaha, a dragon? Are you serious? I don't know how to interpret dreams, but that sounds like a good one! Did you see a dog in your dream, too? Hahaha!\r\n\r\n#fUI/UIWindow.img/QuestIcon/8/0# 20 exp");
	if (status == 2){
		qm.gainExp(20);
		qm.evanTutorial("UI/tutorial/evan/2/0", 1);
		qm.forceCompleteQuest();
		qm.dispose();	
		}
	}