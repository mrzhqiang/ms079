var status = -1;

function start(mode, type, selection) {
	qm.dispose();
}
function end(mode, type, selection) {
	status++;
	if (status == 0) {
		qm.sendNext("I''d love nothing more than to rub what we''ve done in #p2154009#''s face, but things could get hairy if he gathers all his minions. Let''s get out of here. Use the Underground Base #t4032740# on my count. One... two... three!");
	} else {
		qm.warp(310010000);
		qm.forceCompleteQuest();
		qm.dispose();
	}
}
