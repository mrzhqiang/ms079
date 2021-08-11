importPackage(Packages.tools);

var Orbis_btf;
var Train_to_Orbis;
var Orbis_docked;
var Ludibrium_btf;
var Train_to_Ludibrium;
var Ludibrium_docked;

function init() {
    Orbis_btf = em.getChannelServer().getMapFactory().getMap(200000122);
    Ludibrium_btf = em.getChannelServer().getMapFactory().getMap(220000111);
    Train_to_Orbis = em.getChannelServer().getMapFactory().getMap(200090110);
    Train_to_Ludibrium = em.getChannelServer().getMapFactory().getMap(200090100);
    Orbis_docked = em.getChannelServer().getMapFactory().getMap(200000100);
    Ludibrium_docked = em.getChannelServer().getMapFactory().getMap(220000100);
    Orbis_Station = em.getChannelServer().getMapFactory().getMap(200000121);
    Ludibrium_Station = em.getChannelServer().getMapFactory().getMap(220000110);
    scheduleNew();
}

function scheduleNew() {
    Ludibrium_Station.setDocked(true);
    Orbis_Station.setDocked(true);
    Ludibrium_Station.broadcastMessage(MaplePacketCreator.boatPacket(true));
    Orbis_Station.broadcastMessage(MaplePacketCreator.boatPacket(true));
    em.setProperty("docked", "true");
    em.setProperty("entry", "true");
    em.schedule("stopEntry", 240000);
    em.schedule("takeoff", 10 * 60 * 1000);//300000 乘船时间
}

function stopEntry() {
    em.setProperty("entry","false");
}

function takeoff() {
    Ludibrium_Station.setDocked(false);
    Orbis_Station.setDocked(false);
    Ludibrium_Station.broadcastMessage(MaplePacketCreator.boatPacket(false));
    Orbis_Station.broadcastMessage(MaplePacketCreator.boatPacket(false));
    em.setProperty("docked","false");
    var temp1 = Orbis_btf.getCharacters().iterator();
    while(temp1.hasNext())
        temp1.next().changeMap(Train_to_Ludibrium, Train_to_Ludibrium.getPortal(0));
    var temp2 = Ludibrium_btf.getCharacters().iterator();
    while(temp2.hasNext())
        temp2.next().changeMap(Train_to_Orbis, Train_to_Orbis.getPortal(0));
    em.schedule("arrived", 10 * 60 * 1000);//600000 乘船时间
}

function arrived() {
    var temp1 = Train_to_Orbis.getCharacters().iterator();
    while(temp1.hasNext())
        temp1.next().changeMap(Orbis_docked, Orbis_docked.getPortal(0));
    var temp2 = Train_to_Ludibrium.getCharacters().iterator();
    while(temp2.hasNext())
        temp2.next().changeMap(Ludibrium_docked, Ludibrium_docked.getPortal(0));
    scheduleNew();
}

function cancelSchedule() {
}