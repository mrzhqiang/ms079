function enter(pi) {
        pi.sendDirectionStatus(4, 2159308);
		pi.sendDirectionStatus(3, 2);
		pi.sendDirectionStatus(1, 10);
		pi.EnableUI(1);
		pi.DisableUI(true);
		pi.openNpc(2159309);
}