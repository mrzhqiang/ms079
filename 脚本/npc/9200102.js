/* Dr. Bosch
	Ludibrium Random/VIP Eye Color Change.
*/
var status = -1;
var beauty = 0;
var hair_Colo_new;

function action(mode, type, selection) {
    if (mode == 0) {
	cm.dispose();
	return;
    } else {
	status++;
    }

    if (status == 0) {
	cm.sendSimple("Um... hi, I'm Dr. Bosch, and I am a cosmetic lens expert here at the Ludibrium Plastic Surgery Shop. I believe your eyes are the most important feature in your body, and with #b#t5152012##k or #b#t5152015##k, I can prescribe the right kind of cosmetic lenses for you. Now, what would you like to use? \r\n#L0#Cosmetic Lenses: #i5152012##t5152012##l\r\n#L1#Cosmetic Lenses: #i5152015##t5152015##l");
    } else if (status == 1) {
	hair_Colo_new = [];

	var teye = cm.getPlayerStat("FACE") % 100;

	if (cm.getPlayerStat("GENDER") == 0) {
	    teye += 20000;
	} else {
	    teye += 21000;
	}
	hair_Colo_new[0] = teye + 100;
	hair_Colo_new[1] = teye + 200;
	hair_Colo_new[2] = teye + 300;
	hair_Colo_new[3] = teye + 400;
	hair_Colo_new[4] = teye + 500;
	hair_Colo_new[5] = teye + 600;
	hair_Colo_new[6] = teye + 700;

	if (selection == 0) {
	    beauty = 1;
	    cm.sendYesNo("If you use the regular coupon, you'll be awarded a random pair of cosmetic lenses. Are you going to use a #b#t5152012##k and really make the change to your eyes?");
	} else if (selection == 1) {
	    beauty = 2;
	    cm.askAvatar("With our new computer program, you can see yourself after the treatment in advance. What kind of lens would you like to wear? Please choose the style of your liking.", hair_Colo_new);
	}
    } else if (status == 2){
	if (beauty == 1){
	    if (cm.setRandomAvatar(5152012, hair_Colo_new) == 1) {
		cm.sendOk("Enjoy your new and improved cosmetic lenses!");
	    } else {
		cm.sendOk("I'm sorry, but I don't think you have our cosmetic lens coupon with you right now. Without the coupon, I'm afraid I can't do it for you..");
	    }
	} else {
	    if (cm.setAvatar(5152015, hair_Colo_new[selection]) == 1) {
		cm.sendOk("Enjoy your new and improved cosmetic lenses!");
	    } else {
		cm.sendOk("I'm sorry, but I don't think you have our cosmetic lens coupon with you right now. Without the coupon, I'm afraid I can't do it for you..");
	    }
	}
	cm.safeDispose();
    }
}