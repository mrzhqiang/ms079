package server.life;

public enum Element {

    NEUTRAL, PHYSICAL, FIRE, ICE, LIGHTING, POISON, HOLY, DARKNESS;

    public static Element getFromChar(char c) {
        switch (Character.toUpperCase(c)) {
            case 'F':
                return FIRE;
            case 'I':
                return ICE;
            case 'L':
                return LIGHTING;
            case 'S':
                return POISON;
            case 'H':
                return HOLY;
            case 'P':
                return NEUTRAL;
            //  return PHYSICAL;
           /* case 'D': // Added on v.92 MSEA
                return DARKNESS;*/
            case 'G':
            case 'J':
            case 'K':
            case 'M':
            case 'N':
            case 'O':
            case 'Q':
            case 'R':
        }
        throw new IllegalArgumentException("unknown elemnt char " + c);
    }
}
