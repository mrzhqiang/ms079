function act() {
	var em = rm.getEventManager("Pirate");
	if (em != null) {
		rm.mapMessage(6, "成功的阻止一波海賊亂入了。");
		em.setProperty("stage4", parseInt(em.getProperty("stage4")) + 1);
		if (em.getProperty("stage4").equals("4")) { //all 5 done
			rm.mapMessage(6, "過關，請往下一個門走！");
		}
	}
}