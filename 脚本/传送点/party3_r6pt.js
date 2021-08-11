function enter(pi) {
 try {
    var em = pi.getEventManager("OrbisPQ");
    if (em != null && em.getProperty("stage6_" + (pi.getPortal().getName().substring(2, 5)) + "").equals("1")) {
	pi.warpS(pi.getMapId(),(pi.getPortal().getName().startsWith("rp16") ? "pt03" : (pi.getPortal().getId() + 4)));
    } else {
	pi.warpS(pi.getMapId(), 0);
    }
 } catch (e) {
    pi.getPlayer().dropMessage(5, "Error: " + e);
 }
}