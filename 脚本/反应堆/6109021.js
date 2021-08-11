function act() { //flame1, top center?
	rm.mapMessage(6, "The top center switch has been toggled.");
	var flames = Array("a3", "a4", "a5", "b3", "b4", "b5", "c3", "c4", "c5");
	for (var i = 0; i < flames.length; i++) {
		rm.getMap().toggleEnvironment(flames[i]);
	}
}