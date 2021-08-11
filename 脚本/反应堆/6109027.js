function act() {
	rm.mapMessage(6, "The bottom right switch has been toggled.");
	var flames = Array("g6", "g7", "h6", "h7", "i6", "i7");
	for (var i = 0; i < flames.length; i++) {
		rm.getMap().toggleEnvironment(flames[i]);
	}
}