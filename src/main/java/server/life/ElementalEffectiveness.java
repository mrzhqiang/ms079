package server.life;

public enum ElementalEffectiveness {

    正常, 免疫, 增强, 虚弱, NEUTRAL;
//NORMAL, IMMUNE, STRONG, WEAK, NEUTRAL;
    public static ElementalEffectiveness getByNumber(int num) {
        switch (num) {
            case 1:
                return 免疫;
            case 2:
                return 正常;
            case 3:
                return 虚弱;
            case 4:
                return NEUTRAL;
            default:
                throw new IllegalArgumentException("Unkown effectiveness: " + num);
        }
    }
}
