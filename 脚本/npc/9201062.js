/* J.J.
	NLC VIP Eye Color Change.
*/
var status = -1;
var hair_Colo_new;

function action(mode, type, selection) {
    if (mode == 0) {
	cm.dispose();
	return;
    } else {
	status++;
    }

    if (status == 0) {
	cm.sendNext("Hey, there~! I'm J.J.! I'm in charge of the cosmetic lenses here at NLC Shop! If you have a #b#t5152036##k, I can get you the best cosmetic lenses you have ever had! Now, what would you like to do?");
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
	    
	cm.askAvatar("With our specialized machine, you can see yourself after the treatment in advance. What kind of lens would you like to wear? Choose the style of your liking.", hair_Colo_new);

    } else if (status == 2){
	if (cm.setAvatar(5152036, hair_Colo_new[selection]) == 1) {
	    cm.sendOk("Enjoy your new and improved cosmetic lenses!");
	} else {
	    cm.sendOk("I'm sorry, but I don't think you have our cosmetic lens coupon with you right now. Without the coupon, I'm afraid I can't do it for you..");
	}
	cm.safeDispose();
    }
}