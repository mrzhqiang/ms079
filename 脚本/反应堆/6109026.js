function act() {
	rm.mapMessage(6, "The bottom center switch has been toggled.");
	var flames = Array("g3", "g4", "g5", "h3", "h4", "h5", "i3", "i4", "i5");
	for (var i = 0; i < flames.length; i++) {
		rm.getMap().toggleEnvironment(flames[i]);
	}
}