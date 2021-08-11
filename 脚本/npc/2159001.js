var status = -1;
function action(mode, type, selection) {
    if (mode == 1) 
        status++;
    else 
	status--;
    if (status == 0) {
    	cm.sendNextS("There you are, #h0#! You''re late. Get over here.", 8);
    } else if (status == 1) {
	cm.sendNextPrevS("What was the hold up? You scared or something?", 4, 2159002);
    } else if (status == 2) {
	cm.sendNextPrevS("Don''t be ridiculous.", 2);
    } else if (status == 3) {
	cm.sendNextPrevS("You''re not s-s-scared at all? I am, a little b-b-bit... The grown-ups warned us never to venture into the #bVerne Mines#k... Plus, there are all those #rBlack Wings#k around, watching us, I just know it.", 4, 2159000);
    } else if (status == 4) {
	cm.sendNextPrevS("We snuck here, Jun. No one saw us. No one''s watching us, okay? Come on, when else would we have ever gotten the chance to leave #bEdelstein#k? Don''t be a chicken.", 4, 2159002);
    } else if (status == 5) {
	cm.sendNextPrevS("But what if we get in trouble?", 4, 2159000);
    } else if (status == 6) {
	cm.sendNextPrevS("Jun, we''re already here. If we''re going to get in trouble, let''s at least have some fun first. Let''s play hide-and-seek!", 8);
    } else if (status == 7) {
	cm.sendNextPrevS("Hide and seek?", 2);
    } else if (status == 8) {
	cm.sendNextPrevS("Ugh, la-ame.", 4, 2159002);
    } else if (status == 9) {
	cm.sendNextPrevS("Don''t be a brat, Von. What? Are you scared to hide all by yourself in these big, bad caves? *snicker* \r\n#h0#, since you were late, you''re it. Count to 10 and then come find us. No peeking.", 8);
    } else if (status == 10) {
	cm.warp(931000001, 1);
    	cm.dispose();
    }
}