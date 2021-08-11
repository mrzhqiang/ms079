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
		cm.sendNextNoESC("Are all the Commanders here? Good, let's start.");
    } else if (status == 1) {
		cm.sendNextNoESC("Until the mighty Black Mage finishes his plan, we must not relax even for a moment! We are still vulnerable. Now, #h0#, I heard you uncovered interesting information.", 2159308);
    } else if (status == 2) {
		cm.sendPlayerToNpc("Yes..I have discovered a resistance group has formed in secret and is building a force to move against us.");
	} else if (status == 3) {
		cm.sendNextNoESC("Resistance? Ha! There's no one left in this world that can resist us. I've even heard some of the rabble calling them #rHeroes#. Isn't that precious?", 2159308);
	} else if (status == 4) {
		cm.sendNextNoESC("I'm a little excited to see them scramble around in their panic. They certainly didn't put up much resistance when we took Ereve or when I eliminated the Castellan.", 2159339);
	} else if (status == 5) {
		cm.sendNextNoESC("The battle at Ereve was easy because of the Black Mage, not you, Orchid.", 2159308);
	} else if (status == 6) {
		cm.sendNextNoESC("Well, I didn't have to use my full power. So there.", 2159339);
	} else if (status == 7) {
		cm.sendPlayerToNpc("What are you doing here, Orchid? Are you not working with Lotus?");
	} else if (status == 8) {
		cm.sendNextNoESC("Lotus is busy because she is always looking for more to do! You don't have to bug me about it.", 2159339);
	} else if (status == 9) {
		cm.sendNextNoESC("This meeting is going nowhere.");
	} else if (status == 10) {
		cm.sendNextNoESC("Whenever Orchid talks, our meetings grind to a halt! As for the Heroes, I'm sure #h0# has a plan to deal with them. I'm sure these pathetic 'Heroes' will be no match for him.", 2159308);
	} else if (status == 11) {
		cm.sendPlayerToNpc("Unlike most foes, the Heroes fight for others, not themselves... they are special, because they protect the world. That makes them dangerous. Also, I only stunned the Goddess. The Black Mage was the one to defeat her.");
	} else if (status == 12) {
		cm.sendNextNoESC("How modest of you! How you are the Black Mage's favorite... My, my, my...", 2159308);
	} else if (status == 13) {
		cm.sendDirectionInfo("Effect/Direction6.img/effect/tuto/balloonMsg0/10");
		cm.sendNextNoESC("Enough! Both of you.");
	} else if (status == 14) {
		cm.sendNextNoESC("Why? I find it quite amusing.", 2159339);
	} else if (status == 15) {
		cm.sendNextNoESC("And I am complimenting the true HERO of our forces, the MIGHTY #h0#! Ha ha ha...", 2159308);
	} else if (status == 16) {
		cm.sendDirectionInfo("Effect/Direction6.img/effect/tuto/balloonMsg0/10");
		cm.sendNextNoESC("Enough! Accept that #h0# stunned the Goddess, allowing for our victory. Therefore his contribution was the most important. Besides, you are credited for blinding the Goddess. What more do you want?");
	} else if (status == 17) {
		cm.sendNextNoESC("Ah, what of the remaining resistance group then, if the Heroes have been taken care of? We must move along with the meeting.", 2159308);
	} else if (status == 18) {
		cm.sendNextNoESC("As commanded, they have been completely eliminated.");
	} else if (status == 19) {
		cm.sendNextNoESC("Oh, I have a question. Why did the Black Mage tell us to destroy everything? If there's nothing left, there's nothing to rule over.", 2159339);
	} else if (status == 20) {
		cm.sendDirectionInfo("Effect/Direction6.img/effect/tuto/balloonMsg1/18");
		cm.sendPlayerToNpc("What? When did the Black Mage order this? I never heard of this.");
	} else if (status == 21) {
		cm.sendNextNoESC("Ah yes. I nearly forgot to mention the new orders to you. The Black Mage ordered all of us, except you, to eliminate EVERYTHING.", 2159308);
	} else if (status == 22) {
		cm.sendNextNoESC("Yes. For example, Leafre just burned to cinders..");
	} else if (status == 23) {
		cm.sendDirectionInfo("Effect/Direction6.img/effect/tuto/balloonMsg1/3");
		cm.sendPlayerToNpc("(Leafre? That's near my family...!)");
	} else if (status == 24) {
		cm.sendNextNoESC("I think we did well. Only a few dragon servants remain for the price of resistance.", 2159308);
	} else if (status == 25) {
		cm.sendPlayerToNpc("Did the Black Mage not promise to attack Leafre? What sections were destroyed?");
	} else if (status == 26) {
		cm.sendNextNoESC("Sections? All of them, of course! What does it matter to you?", 2159308);
	} else if (status == 27) {
		cm.sendDirectionInfo("Effect/Direction6.img/effect/tuto/balloonMsg0/11");
		cm.sendPlayerToNpc("Please excuse me. There is something I must attend to.");
	} else if (status == 28) {
		cm.sendNextNoESC("Remain seated! No one has dismissed you yet.", 2159308);
	} else if (status == 29) {
		cm.sendDirectionStatus(3, 2);
		cm.sendDirectionStatus(4, 0);
		cm.warp(924020010,0);
		cm.dispose();
	}
}