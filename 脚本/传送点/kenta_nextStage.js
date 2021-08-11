function enter(pi) {
        if (pi.getPlayer().getParty() != null) {
                //pi.warpParty_Instanced(921120600);
				var cleared = false;
				switch(pi.getMapId() / 10) {
					case 92304010:
						cleared = pi.getMap().getAllMonstersThreadsafe().size() == 0;
						break;
					case 92304020:
						cleared = pi.haveItem(2430364, 20);
						break;
					case 92304030:
						cleared = pi.getPlayer().getEventInstance() != null && pi.getPlayer().getEventInstance().getProperty("kentaSaving") != null && pi.getPlayer().getEventInstance().getProperty("kentaSaving").equals("0");
						break;
				}
				if (cleared) {
					pi.removeAll(2430364);
					pi.warpParty(pi.getMapId() + 100);
					pi.playPortalSE();
				} else {
					pi.playerMessage(5,"This portal is not available yet.");
				}
        } else {
                pi.playerMessage(5,"This portal is not available.");
        }
}