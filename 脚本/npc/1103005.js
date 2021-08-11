/*
	NPC Name: 		Nineheart
	Map(s): 		-Erev Cygnus Intro-
	Description: 		warpout, accept cygnus help request
*/

function start() {
    cm.askAcceptDecline("Becoming a Knight of Cygnus requires talent, faith, courage, and will power... and it looks like you are more than qualified to become a Knight of Cygnus. What do you think? If you wish to become one right this minute, I'll take you straight to Erev. Would you like to head over to Erev right now?");
}

function action(mode, type, selection) {
    var returnmap = cm.getSavedLocation("CYGNUSINTRO");

    if (returnmap == null) {
        cm.warp(130000000, 0);
    } else {
        if (mode == 1) {
            cm.warp(returnmap != -1 ? returnmap : 130000000, 0);
        } else {
            cm.warp(130000000, 0);
        }
	cm.clearSavedLocation("CYGNUSINTRO");
    }
    cm.dispose();
}