
function action(mode, type, selection) {
	var returnMap = cm.getSavedLocation("MULUNG_TC");
	if (returnMap < 0) {
		returnMap = 230000000; // to fix people who entered the fm trough an unconventional way
	}
	cm.clearSavedLocation("MULUNG_TC");
	cm.warp(returnMap,0);
	cm.dispose();
}