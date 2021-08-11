package server;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author AlphaEta
 */
public class StructItemOption {

    public static String[] types = {"incSTR", "incDEX", "incINT", "incLUK", "incACC", "incEVA", "incSpeed", "incJump",
        "incPAD", "incMAD", "incPDD", "incMDD", "prop", "time", "incSTRr", "incDEXr", "incINTr",
        "incLUKr", "incMHPr", "incMMPr", "incACCr", "incEVAr", "incPADr", "incMADr", "incPDDr",
        "incMDDr", "incCr", "incDAMr", "RecoveryHP", "RecoveryMP", "HP", "MP", "level",
        "ignoreTargetDEF", "ignoreDAM", "incAllskill", "ignoreDAMr", "RecoveryUP",
        "incCriticaldamageMin", "incCriticaldamageMax", "incTerR", "incAsrR", "DAMreflect",
        "mpconReduce", "reduceCooltime", "incMesoProp", "incRewardProp", "boss", "incMHP", "incMMP", "attackType"};
    public int optionType, reqLevel, opID; // opID = nebulite Id or potential ID
    public String face; // angry, cheers, love, blaze, glitter
    public Map<String, Integer> data = new HashMap<>();

    public int get(final String type) {
        return data.get(type) != null ? data.get(type) : 0;
    }

    @Override
    public final String toString() { // I should read from the "string" value instead.
        return data.toString();
    }
}
