var status = -1;
function action(mode, type, selection) {
    status++;
    if (status == 0) {
    	cm.sendNext("If Jun''s too chicken, let''s leave him here. But why''s it have to be hide-and-seek? Let''s play something cool...");
    } else if (status == 1) {
	cm.sendNext("That''s not what I said...");
    	cm.dispose();
    }
}