var status = -1;
function action(mode, type, selection) {
    status++;
    if (status == 0) {
    	cm.sendNext("Aww, you found me. I thought I found a great spot, too.");
    } else if (status == 1) {
    	cm.dispose();
    }
}