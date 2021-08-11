/*
	Gold Richie
*/

function start() {
    if (cm.haveItem(2430008)) {
	cm.sendNext("The Gold Compass is now ready.");
    } else {
	cm.sendNext("Check and see if you have all the materials ready to make the Gold Compass. Making one requires 1 blank compass, along with 20 letter N's, 20 letter E's, 20 letter W's, and 20 letter S's for the compass");
    }
}

function action(mode, type, selection) {
    cm.dispose();
}