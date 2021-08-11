/*
 * Cygnus Skill - Training Never ends
 */

var status = -1;

function start(mode, type, selection) {
    status++;

    if (status == 0) {
	qm.askAcceptDecline("#h0#. 你有没有在训练懈怠，因为达到100级？我们都知道你是多么强大，但训练是不完整的。一起来看看这些骑士指挥官。他们训练了一天一夜，准备为自己的黑精灵可能遇到的问题。");
    } else {
	if (mode == 1) {
	    qm.forceStartQuest();
	}
	qm.dispose();
    }
}

function end(mode, type, selection) {
    qm.dispose();
}