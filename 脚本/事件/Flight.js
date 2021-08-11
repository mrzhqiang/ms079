importPackage(Packages.tools);

var Orbis_btf;
var Boat_to_Orbis;
var Orbis_docked;
var leafre_btf;
var Boat_to_leafre;
var leafre_docked;

function init() {
    Orbis_btf = em.getChannelServer().getMapFactory().getMap(200000132);
    leafre_btf = em.getChannelServer().getMapFactory().getMap(240000111);
    Boat_to_Orbis = em.getChannelServer().getMapFactory().getMap(200090110);
    Boat_to_leafre = em.getChannelServer().getMapFactory().getMap(200090200);
    Orbis_docked = em.getChannelServer().getMapFactory().getMap(200000100);
    leafre_docked = em.getChannelServer().getMapFactory().getMap(240000100);
    Orbis_Station = em.getChannelServer().getMapFactory().getMap(200000131);
    leafre_Station = em.getChannelServer().getMapFactory().getMap(240000110);
    scheduleNew();
}

function scheduleNew() {
    leafre_Station.setDocked(true);
    Orbis_Station.setDocked(true);
    leafre_Station.broadcastMessage(MaplePacketCreator.boatPacket(true));
    Orbis_Station.broadcastMessage(MaplePacketCreator.boatPacket(true));
    em.setProperty("docked", "true");
    em.setProperty("entry", "true");
    em.schedule("stopEntry", 240000);
    em.schedule("takeoff", 300000);
}

function stopEntry() {
    em.setProperty("entry","false");
}

function takeoff() {
    leafre_Station.setDocked(false);
    Orbis_Station.setDocked(false);
    leafre_Station.broadcastMessage(MaplePacketCreator.boatPacket(false));
    Orbis_Station.broadcastMessage(MaplePacketCreator.boatPacket(false));
    em.setProperty("docked","false");
    var temp1 = Orbis_btf.getCharacters().iterator();
    while(temp1.hasNext())
        temp1.next().changeMap(Boat_to_leafre, Boat_to_leafre.getPortal(0));
    var temp2 = leafre_btf.getCharacters().iterator();
    while(temp2.hasNext())
        temp2.next().changeMap(Boat_to_Orbis, Boat_to_Orbis.getPortal(0));
    em.schedule("arrived", 600000);
}

function arrived() {
    var temp1 = Boat_to_Orbis.getCharacters().iterator();
    while(temp1.hasNext())
        temp1.next().changeMap(Orbis_docked, Orbis_docked.getPortal(0));
    var temp2 = Boat_to_leafre.getCharacters().iterator();
    while(temp2.hasNext())
        temp2.next().changeMap(leafre_docked, leafre_docked.getPortal(0));
    scheduleNew();
}

function cancelSchedule() {
}