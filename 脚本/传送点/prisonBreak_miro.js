function enter(pi) {
        if (pi.getPlayer().getParty() != null && pi.getPlayer().getMap().getAllMonstersThreadsafe().size() == 0) {
				var cleared = java.lang.Math.random() > 0.9;
				if (cleared) {
					pi.warpParty(921160400);
				} else {
					pi.warp(921160300 + ((Math.floor(java.lang.Math.random() * 6) | 0) * 10), 0);
				}
				pi.playPortalSE();
        } else {
                pi.playerMessage(5,"This portal is not available. Kill all monsters.");
        }
}