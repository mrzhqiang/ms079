package server;

import client.inventory.Equip;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import tools.Triple;

public class ItemInformation {

    public List<Integer> scrollReqs = null, questItems = null, incSkill = null;
    public int slotMax, itemMakeLevel;
    public Equip eq = null;
    public Map<String, Integer> equipStats;
    public BigDecimal price = BigDecimal.ZERO;
    public int itemId, wholePrice, monsterBook, stateChange, meso, questId, totalprob, replaceItem, mob, cardSet, create, flag, npc;
    public String name, desc, msg, replaceMsg, afterImage, script;
    public int karmaEnabled;
    public List<StructRewardItem> rewardItems = null;
    public List<Triple<String, String, String>> equipAdditions = null;
    public Map<Integer, Map<String, Integer>> equipIncs = null;
}
