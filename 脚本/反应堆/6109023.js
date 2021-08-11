function act() {
	rm.mapMessage(6, "The middle left switch has been toggled.");
	var flames = Array("d1", "d2", "e1", "e2", "f1", "f2");
	for (var i = 0; i < flames.length; i++) {
		rm.getMap().toggleEnvironment(flames[i]);
	}
}