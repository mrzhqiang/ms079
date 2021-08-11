function enter(pi) {
    if (pi.getQuestStatus(6110) == 1) {
	 if (pi.getParty() != null) {
	     if (!pi.isLeader()) {
		 pi.playerMessage("Party leader consisting of two Warriors can decide to enter." );
	     } else {
		 if (pi.getParty().getMembers().size < 2) {
		    pi.playerMessage("You can make a quest when you have a party with two. Please make your party with two members." );
		 } else {
		      if (!pi.isAllPartyMembersAllowedJob(1)) {
			  pi.playerMessage("You can't enter. Your party member's job is not Warrior or Your party doesn't consist of two members.");
		      } else {
			  var em = pi.getEventManager("4jrush");
			  if (em == null) {
			      pi.playerMessage("You're not allowed to enter with unknown reason. Try again." );
			  } else {
			      em.startInstance(pi.getParty(), pi.getMap());
			      return true;
			  }
		      }
		 }
	     }
	 } else {
	     pi.playerMessage(5, "You don...t have a  party. You can challenge with party.");
	 }
    } else {
	pi.playerMessage("You can't enter sealed place.");
    }
    return false;
}