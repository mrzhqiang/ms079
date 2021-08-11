function act() { //flame0, im assuming this is topleft
	rm.mapMessage(6, "The top left switch has been toggled.");
	var flames = Array("a1", "a2", "b1", "b2", "c1", "c2");
	for (var i = 0; i < flames.length; i++) {
		rm.getMap().toggleEnvironment(flames[i]);
	}
	//a1, a2
	//b1, b2
	//c1, c2
	
}