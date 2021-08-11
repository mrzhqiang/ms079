/*
Sharen III's Grave Exit - Guild Quest

@Author Lerk
*/

function enter(pi) {
    if (pi.getMap().getReactorByName("ghostgate").getState() == 1 || (pi.getPlayer().getEventInstance() != null && pi.getPlayer().getEventInstance().getProperty("stage4clear") != null && pi.getPlayer().getEventInstance().getProperty("stage4clear").equals("true"))) {
	pi.warp(990000800, 0);
	return true;
    } else {
	pi.playerMessage("This way forward is not open yet.");
	return false;
    }
}