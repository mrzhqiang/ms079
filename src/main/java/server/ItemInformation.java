package server;

import client.inventory.Equip;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.mrzhqiang.maplestory.domain.DWzItemData;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import constants.GameConstants;
import tools.Triple;

public class ItemInformation {

    public final List<Integer> scrollReqs = Lists.newArrayList();
    public final List<Integer> questItems = Lists.newArrayList();
    public final List<Integer> incSkill = Lists.newArrayList();
    public final List<Triple<String, String, String>> equipAdditions = Lists.newArrayList();
    public final List<StructRewardItem> rewardItems = Lists.newArrayList();

    public final Map<String, Integer> equipStats = Maps.newHashMap();
    public final Map<Integer, Map<String, Integer>> equipIncs = Maps.newHashMap();

    public Equip eq = null;
    public int mob, cardSet, npc;
    public String script;

    public final DWzItemData data;

    public ItemInformation(DWzItemData data) {
        this.data = data;
        int slotMax = GameConstants.getSlotMax(data.id);
        data.slotMax = slotMax > 0 ? slotMax : data.slotMax;
        String scrollRq = data.scrollReqs;
        if (scrollRq != null && !scrollRq.isEmpty()) {
            for (String rq : Splitter.on(',').split(scrollRq)) {
                if (rq.length() > 1) {
                    scrollReqs.add(Integer.parseInt(rq));
                }
            }
        }
        String consumeItem = data.consumeItem;
        if (consumeItem != null && !consumeItem.isEmpty()) {
            for (String item : Splitter.on(',').split(consumeItem)) {
                if (item.length() > 1) {
                    questItems.add(Integer.parseInt(item));
                }
            }
        }
        String incRq = data.incSkill;
        if (incRq != null && !incRq.isEmpty()) {
            for (String rq : Splitter.on(',').split(incRq)) {
                if (rq.length() > 1) {
                    incSkill.add(Integer.parseInt(rq));
                }
            }
        }
    }
}
