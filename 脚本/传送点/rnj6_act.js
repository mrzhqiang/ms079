function enter(pi) {
    var em = pi.getEventManager("Romeo");
    if (em != null && em.getProperty("stage5").equals("0")) {
//	pi.spawnMonster(9300142, 10);
//	pi.spawnMonster(9300143, 10);
//	pi.spawnMonster(9300144, 10);
//	pi.spawnMonster(9300145, 10);
//	pi.spawnMonster(9300146, 10);
	em.setProperty("stage5", "1");
    }
}