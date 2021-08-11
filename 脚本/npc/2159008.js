var status = -1;
function action(mode, type, selection) {
    if (mode == 1) 
        status++;
    else 
	status--;
    if (status == 0) {
    	cm.sendNext("Little rats. I say, how DARE you try to escape this place?");
    } else if (status == 1) {
	cm.sendNextPrevS("Shoot, we were spotted!", 2);
    } else if (status == 2) {
	cm.sendNextPrev("Now, now, children. Don''t make this harder than it needs to be. Just walk towards me, nice and easy... Wait, you''re not one of the test subjects. You''re one of the townspeople, aren''t you?");
    } else if (status == 3) {
	cm.sendNextPrevS("That''s right. I''m a resident of Edelstein, not a test subject. You can''t boss ME around.", 2);
    } else if (status == 4) {
	cm.sendNextPrev("Oh my, oh my. I told them to make sure the townspeople kept their kids away from the mines... Alas, it''s too late now. I can''t allow you to tell anyone about this laboratory, so I guess you''ll just have to stay here and...help with the experiments. *snicker*");
    } else if (status == 5) {
	cm.sendNextPrevS("Hmph. Big words, but let''s see if you can catch me first.", 2);
    } else if (status == 6) {
	cm.sendNextPrev("Why, you insolent, little-- Ahem, ahem, ahem. Your words don''t matter. Time for me to pull out the big guns. I do hope you''re ready. If not, you will suffer.");
    } else if (status == 7) {
	cm.sendNextPrev("I say, got any more big words, kiddo? I''ll make sure Gelimer performs some especially atrocious experiments on you. But I''ll be nice if you come with me quiet-like.");
    } else if (status == 8) {
	cm.sendNextPrevS("Hold it right there!", 4, 2159010);
    } else if (status == 9) {
	cm.warp(931000021,1);
    	cm.dispose();
    }
}