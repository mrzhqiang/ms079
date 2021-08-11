/* 
    Return from 
    Ariant Coliseum : Lobby 
*/ 

function enter(pi) { 
    var returnMap = pi.getSavedLocation("ARIANT_PQ"); 
    if (returnMap < 0) { 
        returnMap = 100000000; 
    } 
    var target = pi.getClient().getChannelServer().getMapFactory().getMap(returnMap); 
    var targetPortal; 
    if (returnMap == 100000000) { 
        targetPortal = target.getPortal(4); 
    } else { 
        targetPortal = target.getPortal(0); 
    } 
    if (targetPortal == null) { 
        targetPortal = target.getPortal(0); 
    } 
    pi.clearSavedLocation("ARIANT_PQ"); 
    pi.getPlayer().changeMap(target, targetPortal); 
    return true; 
}  