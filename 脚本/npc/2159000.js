var status = -1;
function action(mode, type, selection) {
    status++;
    if (status == 0) {
    	cm.sendNext("I''m glad you made it. Safety in numbers, right? I feel like we''re being watched... Shouldn''t we think about heading back? The grown-ups in town say the mines aren''t safe...");
    } else if (status == 1) {
	cm.sendNext("Sheesh, why are you such a scaredy cat? We''ve come all this way! We should at least do something before we go back.", 2159002);
    	cm.dispose();
    }
}