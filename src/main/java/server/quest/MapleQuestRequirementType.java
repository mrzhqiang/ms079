package server.quest;


public enum MapleQuestRequirementType {
    UNDEFINED(-1),
    JOB(0),
    ITEM(1),
    QUEST(2),
    LV_MIN(3),
    LV_MAX(4),
    END(5),
    MOB(6),
    NPC(7),
    FIELD_ENTER(8),
    INTERVAL(9),
    START_SCRIPT(10),
    END_SCRIPT(10),
    PET(11),
    PET_TAMENESS_MIN(12),
    MB_MIN(13),
    QUEST_COMPLETE(14),
    POP(15),
    SKILL(16),
    MB_CARD(17),
    SUB_JOB_FLAGS(18),
    DAY_BY_DAY(19),
    NORMAL_AUTO_START(20);

    final byte type;

    MapleQuestRequirementType(int type) {
        this.type = (byte) type;
    }

    public byte getType() {
        return type;
    }

    public static MapleQuestRequirementType getByType(byte type) {
        for (MapleQuestRequirementType requirementType : MapleQuestRequirementType.values()) {
            if (requirementType.getType() == type) {
                return requirementType;
            }
        }
        return null;
    }

    public static MapleQuestRequirementType getByWZName(String name) {
        try {
            return valueOf(name.toUpperCase());
        } catch (IllegalArgumentException ex) {
            return UNDEFINED;
        }
    }
}
