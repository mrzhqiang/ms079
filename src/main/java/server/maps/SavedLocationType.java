package server.maps;

public enum SavedLocationType {

    FREE_MARKET(0),
    MULUNG_TC(1),
    WORLDTOUR(2),
    FLORINA(3),
    FISHING(4),
    RICHIE(5),
    DONGDONGCHIANG(6),
    EVENT(7),
    AMORIA(8),
    CHRISTMAS(9),
    MONSTER_CARNIVAL(10),
    PVP(11),
    HOTEL(12),
    PACH(13),
    Pachinko_port(14),
    DOJO(15),
    CYGNUSINTRO(16),
    ENGLISH(17), 
    SLEEP(18),
    ARIANT(19),
    ARIANT_PQ(20),
    WEDDING(21);
    private int index;

    private SavedLocationType(int index) {
        this.index = index;
    }

    public int getValue() {
        return index;
    }

    public static SavedLocationType fromString(String Str) {
        return valueOf(Str);
    }
}
