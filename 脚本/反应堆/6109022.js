function act() {
	rm.mapMessage(6, "The top right switch has been toggled.");
	var flames = Array("a6", "a7", "b6", "b7", "c6", "c7");
	for (var i = 0; i < flames.length; i++) {
		rm.getMap().toggleEnvironment(flames[i]);
	}
}