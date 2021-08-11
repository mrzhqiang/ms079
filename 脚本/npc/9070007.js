
function start() {
    cm.sendYesNo("Got minions to train? If so, then go in here ... you'll probably head to a familiar place. What do you think? Do you want to go in?");
}

function action(mode, type, selection) {
    if (mode == 0) {
	cm.sendNext("You must be busy, huh? You should try going in. You may end up in a strange place once inside.");
    } else {
	if (cm.getPlayer().getBattler(0) != null || cm.getPlayer().getBoxed().size() >= 1) {
	    cm.warp(193000000, 0);
	} else {
	    cm.sendNext("Hey, hey ... I don't think you have any minions to train ... Maybe you could get one from my friend if they feel nice... You can talk to my friend by doing the command: #b@pokemon#k");
	}
    }
    cm.dispose();
}