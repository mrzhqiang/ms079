function enter(pi) {
    if ( pi.getPlayer().getCarnivalParty().getTeam() == 0 ) {
	pi.warp( pi.getMapId() - 100, "red_revive" );
    } else {
	pi.warp( pi.getMapId() - 100, "blue_revive" );
    }
}