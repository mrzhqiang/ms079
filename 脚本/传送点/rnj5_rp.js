function enter(pi) {
    var em = pi.getEventManager("Romeo");
    //pi.playerMessage(5, "stage6_" + (((pi.getMapId() % 10) | 0) - 1) + "_" + (pi.getPortal().getName().substring(2, 3)) + "_" + (pi.getPortal().getName().substring(3, 4)) + "");
    //pi.playerMessage(5, em.getProperty("stage6_" + (((pi.getMapId() % 10) | 0) - 1) + "_" + (pi.getPortal().getName().substring(2, 3)) + "_" + (pi.getPortal().getName().substring(3, 4)) + ""));
    if (em != null && em.getProperty("stage6_" + (((pi.getMapId() % 10) | 0) - 1) + "_" + (pi.getPortal().getName().substring(2, 3)) + "_" + (pi.getPortal().getName().substring(3, 4)) + "").equals("1")) {
	pi.warpS(pi.getMapId(),(pi.getPortal().getId() >= 51 ? 13 : (pi.getPortal().getId() + 4)));
    } else {
	pi.warpS(pi.getMapId(), 0);
    }
}