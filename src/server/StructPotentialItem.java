package server;

public class StructPotentialItem {

    public byte incSTR, incDEX, incINT, incLUK, incACC, incEVA, incSpeed, incJump,
            incPAD, incMAD, incPDD, incMDD, prop, time, incSTRr, incDEXr, incINTr,
            incLUKr, incMHPr, incMMPr, incACCr, incEVAr, incPADr, incMADr, incPDDr,
            incMDDr, incCr, incDAMr, RecoveryHP, RecoveryMP, HP, MP, level,
            ignoreTargetDEF, ignoreDAM, DAMreflect, mpconReduce, mpRestore,
            incMesoProp, incRewardProp, incAllskill, ignoreDAMr, RecoveryUP;
    public boolean boss;
    public short incMHP, incMMP, attackType, potentialID, skillID;
    public int optionType, reqLevel; //probably the slot
    public String face; //angry, cheers, love, blaze, glitter

    @Override
    public final String toString() {
        final StringBuilder ret = new StringBuilder();
        if (incMesoProp > 0) {
            ret.append("Gives MESO(not coded): ");
            ret.append(incMesoProp);
            ret.append(" ");
        }
        if (incRewardProp > 0) {
            ret.append("Gives ITEM(not coded): ");
            ret.append(incRewardProp);
            ret.append(" ");
        }
        if (prop > 0) {
            ret.append("Probability(not coded): ");
            ret.append(prop);
            ret.append(" ");
        }
        if (time > 0) {
            ret.append("Duration(not coded): ");
            ret.append(time);
            ret.append(" ");
        }
        if (attackType > 0) {
            ret.append("Attack Type(not coded): ");
            ret.append(attackType);
            ret.append(" ");
        }
        if (incAllskill > 0) {
            ret.append("Gives ALL SKILLS: ");
            ret.append(incAllskill);
            ret.append(" ");
        }
        if (skillID > 0) {
            ret.append("Gives SKILL: ");
            ret.append(skillID);
            ret.append(" ");
        }
        if (boss) {
            ret.append("BOSS ONLY, ");
        }
        if (face.length() > 0) {
            ret.append("Face Expression: ");
            ret.append(face);
            ret.append(" ");
        }
        if (RecoveryUP > 0) {
            ret.append("Gives Recovery % on potions: ");
            ret.append(RecoveryUP);
            ret.append(" ");
        }
        if (DAMreflect > 0) {
            ret.append("Reflects Damage when Hit: ");
            ret.append(DAMreflect);
            ret.append(" ");
        }
        if (mpconReduce > 0) {
            ret.append("Reduces MP Needed for skills: ");
            ret.append(mpconReduce);
            ret.append(" ");
        }
        if (ignoreTargetDEF > 0) {
            ret.append("Ignores Monster DEF %: ");
            ret.append(ignoreTargetDEF);
            ret.append(" ");
        }
        if (RecoveryHP > 0) {
            ret.append("Recovers HP: ");
            ret.append(RecoveryHP);
            ret.append(" ");
        }
        if (RecoveryMP > 0) {
            ret.append("Recovers MP: ");
            ret.append(RecoveryMP);
            ret.append(" ");
        }
        if (HP > 0) { //no idea
            ret.append("Recovers HP: ");
            ret.append(HP);
            ret.append(" ");
        }
        if (MP > 0) { //no idea
            ret.append("Recovers MP: ");
            ret.append(MP);
            ret.append(" ");
        }
        if (mpRestore > 0) { //no idea
            ret.append("Recovers MP: ");
            ret.append(mpRestore);
            ret.append(" ");
        }
        if (ignoreDAM > 0) {
            ret.append("Ignores Monster Damage: ");
            ret.append(ignoreDAM);
            ret.append(" ");
        }
        if (ignoreDAMr > 0) {
            ret.append("Ignores Monster Damage %: ");
            ret.append(ignoreDAMr);
            ret.append(" ");
        }
        if (incMHP > 0) {
            ret.append("Gives HP: ");
            ret.append(incMHP);
            ret.append(" ");
        }
        if (incMMP > 0) {
            ret.append("Gives MP: ");
            ret.append(incMMP);
            ret.append(" ");
        }
        if (incMHPr > 0) {
            ret.append("Gives HP %: ");
            ret.append(incMHPr);
            ret.append(" ");
        }
        if (incMMPr > 0) {
            ret.append("Gives MP %: ");
            ret.append(incMMPr);
            ret.append(" ");
        }
        if (incSTR > 0) {
            ret.append("Gives STR: ");
            ret.append(incSTR);
            ret.append(" ");
        }
        if (incDEX > 0) {
            ret.append("Gives DEX: ");
            ret.append(incDEX);
            ret.append(" ");
        }
        if (incINT > 0) {
            ret.append("Gives INT: ");
            ret.append(incINT);
            ret.append(" ");
        }
        if (incLUK > 0) {
            ret.append("Gives LUK: ");
            ret.append(incLUK);
            ret.append(" ");
        }
        if (incACC > 0) {
            ret.append("Gives ACC: ");
            ret.append(incACC);
            ret.append(" ");
        }
        if (incEVA > 0) {
            ret.append("Gives EVA: ");
            ret.append(incEVA);
            ret.append(" ");
        }
        if (incSpeed > 0) {
            ret.append("Gives Speed: ");
            ret.append(incSpeed);
            ret.append(" ");
        }
        if (incJump > 0) {
            ret.append("Gives Jump: ");
            ret.append(incJump);
            ret.append(" ");
        }
        if (incPAD > 0) {
            ret.append("Gives Attack: ");
            ret.append(incPAD);
            ret.append(" ");
        }
        if (incMAD > 0) {
            ret.append("Gives Magic Attack: ");
            ret.append(incMAD);
            ret.append(" ");
        }
        if (incPDD > 0) {
            ret.append("Gives Defense: ");
            ret.append(incPDD);
            ret.append(" ");
        }
        if (incMDD > 0) {
            ret.append("Gives Magic Defense: ");
            ret.append(incMDD);
            ret.append(" ");
        }
        if (incSTRr > 0) {
            ret.append("Gives STR %: ");
            ret.append(incSTRr);
            ret.append(" ");
        }
        if (incDEXr > 0) {
            ret.append("Gives DEX %: ");
            ret.append(incDEXr);
            ret.append(" ");
        }
        if (incINTr > 0) {
            ret.append("Gives INT %: ");
            ret.append(incINTr);
            ret.append(" ");
        }
        if (incLUKr > 0) {
            ret.append("Gives LUK %: ");
            ret.append(incLUKr);
            ret.append(" ");
        }
        if (incACCr > 0) {
            ret.append("Gives ACC %: ");
            ret.append(incACCr);
            ret.append(" ");
        }
        if (incEVAr > 0) {
            ret.append("Gives EVA %: ");
            ret.append(incEVAr);
            ret.append(" ");
        }
        if (incPADr > 0) {
            ret.append("Gives Attack %: ");
            ret.append(incPADr);
            ret.append(" ");
        }
        if (incMADr > 0) {
            ret.append("Gives Magic Attack %: ");
            ret.append(incMADr);
            ret.append(" ");
        }
        if (incPDDr > 0) {
            ret.append("Gives Defense %: ");
            ret.append(incPDDr);
            ret.append(" ");
        }
        if (incMDDr > 0) {
            ret.append("Gives Magic Defense %: ");
            ret.append(incMDDr);
            ret.append(" ");
        }
        if (incCr > 0) {
            ret.append("Gives Critical %: ");
            ret.append(incCr);
            ret.append(" ");
        }
        if (incDAMr > 0) {
            ret.append("Gives Total Damage %: ");
            ret.append(incDAMr);
            ret.append(" ");
        }
        if (level > 0) {
            ret.append("Level: ");
            ret.append(level);
            ret.append(" ");
        }
        return ret.toString();
    }
}
