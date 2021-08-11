importPackage(Packages.tools);

var closeTime = 120000; //The time to close the gate
var beginTime = 120000; //The time to begin the ride
var rideTime = 60000; //The time that require move to destination
var KC_Waiting;
var Subway_to_KC;
var KC_docked;
var NLC_Waiting;
var Subway_to_NLC;
var NLC_docked;

function init() {
    KC_Waiting = em.getChannelServer().getMapFactory().getMap(600010004);
    NLC_Waiting = em.getChannelServer().getMapFactory().getMap(600010002);
    Subway_to_KC = em.getChannelServer().getMapFactory().getMap(600010003);
    Subway_to_NLC = em.getChannelServer().getMapFactory().getMap(600010005);
    KC_docked = em.getChannelServer().getMapFactory().getMap(103000100);
    NLC_docked = em.getChannelServer().getMapFactory().getMap(600010001);
    scheduleNew();
}

function scheduleNew() {
    em.setProperty("docked", "true");
    em.setProperty("entry", "true");
    KC_docked.broadcastMessage(MaplePacketCreator.serverNotice(6, "地鐵列車到站了。"));
    NLC_docked.broadcastMessage(MaplePacketCreator.serverNotice(6, "地鐵列車到站了。"));
    em.schedule("stopEntry", closeTime);
    em.schedule("takeoff", beginTime);
}

function stopEntry() {
    em.setProperty("entry","false");
}

function takeoff() {
    em.setProperty("docked","false");
    var temp1 = KC_Waiting.getCharacters().iterator();
    while(temp1.hasNext()) {
        temp1.next().changeMap(Subway_to_NLC, Subway_to_NLC.getPortal(0));
    }
    var temp2 = NLC_Waiting.getCharacters().iterator();
    while(temp2.hasNext()) {
        temp2.next().changeMap(Subway_to_KC, Subway_to_KC.getPortal(0));
    }
    KC_docked.broadcastMessage(MaplePacketCreator.serverNotice(6, "地鐵列車已經離開了."));
    NLC_docked.broadcastMessage(MaplePacketCreator.serverNotice(6, "地鐵列車已經離開了."));
    var temp = rideTime / 1000;
    Subway_to_KC.broadcastMessage(MaplePacketCreator.getClock(temp));
    Subway_to_NLC.broadcastMessage(MaplePacketCreator.getClock(temp));
    em.schedule("arrived", rideTime);
}

function arrived() {
    var temp1 = Subway_to_KC.getCharacters().iterator();
    while(temp1.hasNext()) {
        temp1.next().changeMap(KC_docked, KC_docked.getPortal(0));
    }
    var temp2 = Subway_to_NLC.getCharacters().iterator();
    while(temp2.hasNext()) {
        temp2.next().changeMap(NLC_docked, NLC_docked.getPortal(0));
    }
    scheduleNew();
}

function cancelSchedule() {
}
