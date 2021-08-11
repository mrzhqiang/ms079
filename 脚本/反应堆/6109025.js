function act() {
	rm.mapMessage(6, "The bottom left switch has been toggled.");
	var flames = Array("g1", "g2", "h1", "h2", "i1", "i2");
	for (var i = 0; i < flames.length; i++) {
		rm.getMap().toggleEnvironment(flames[i]);
	}
}