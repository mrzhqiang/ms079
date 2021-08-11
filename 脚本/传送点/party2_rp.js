function enter(pi) {
 try {
    var em = pi.getEventManager("LudiPQ");
    if (em != null && em.getProperty("stage6_" + (pi.getPortal().getName().substring(2, 4)) + "").equals("1")) {
	pi.warpS(pi.getMapId(),(pi.getPortal().getName().startsWith("pt9") ? 14 : (pi.getPortal().getId() + 3)));
	pi.playerMessage(-1, "Correct combination!");
	pi.getMap().changeEnvironment("an" + pi.getPortal().getName().substring(2, 4), 2);
    } else {
	pi.warpS(pi.getMapId(), (pi.getPortal().getName().startsWith("pt0") ? 13 : (pi.getPortal().getId() - 3)));
	pi.playerMessage(-1, "Incorrect combination.");
    }
 } catch (e) {
    pi.getPlayer().dropMessage(5, "Error: " + e);
 }
}