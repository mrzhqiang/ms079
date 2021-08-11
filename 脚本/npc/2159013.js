var status = -1;
function action(mode, type, selection) {
    status++;
    if (status == 0) {
    	cm.sendNext("My heart is pounding, but this is kind of exciting. We''re going to get in so much trouble if we''re caught, though.");
    } else if (status == 1) {
    	cm.dispose();
    }
}