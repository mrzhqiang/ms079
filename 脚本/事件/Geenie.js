importPackage(Packages.tools);

var Orbis_btf;
var Boat_to_Orbis;
var Orbis_docked;
var Geenie_btf;
var Boat_to_Geenie;
var Geenie_docked;

function init() {
    Orbis_btf = em.getChannelServer().getMapFactory().getMap(200000152);
    Geenie_btf = em.getChannelServer().getMapFactory().getMap(260000110);
    Boat_to_Orbis = em.getChannelServer().getMapFactory().getMap(200090410);
    Boat_to_Geenie = em.getChannelServer().getMapFactory().getMap(200090400);
    Orbis_docked = em.getChannelServer().getMapFactory().getMap(200000100);
    Geenie_docked = em.getChannelServer().getMapFactory().getMap(260000100);
    Orbis_Station = em.getChannelServer().getMapFactory().getMap(200000151);
    Geenie_Station = em.getChannelServer().getMapFactory().getMap(260000100);
    scheduleNew();
}

function scheduleNew() {
    Geenie_Station.setDocked(true);
    Orbis_Station.setDocked(true);
    Geenie_Station.broadcastMessage(MaplePacketCreator.boatPacket(true));
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
    Geenie_Station.setDocked(false);
    Orbis_Station.setDocked(false);
    Geenie_Station.broadcastMessage(MaplePacketCreator.boatPacket(false));
    Orbis_Station.broadcastMessage(MaplePacketCreator.boatPacket(false));
    em.setProperty("docked","false");
    var temp1 = Orbis_btf.getCharacters().iterator();
    while(temp1.hasNext())
        temp1.next().changeMap(Boat_to_Geenie, Boat_to_Geenie.getPortal(0));
    var temp2 = Geenie_btf.getCharacters().iterator();
    while(temp2.hasNext())
        temp2.next().changeMap(Boat_to_Orbis, Boat_to_Orbis.getPortal(0));
    em.schedule("arrived", 600000);
}

function arrived() {
    var temp1 = Boat_to_Orbis.getCharacters().iterator();
    while(temp1.hasNext())
        temp1.next().changeMap(Orbis_docked, Orbis_docked.getPortal(0));
    var temp2 = Boat_to_Geenie.getCharacters().iterator();
    while(temp2.hasNext())
        temp2.next().changeMap(Geenie_docked, Geenie_docked.getPortal(0));
    scheduleNew();
}

function cancelSchedule() {
}