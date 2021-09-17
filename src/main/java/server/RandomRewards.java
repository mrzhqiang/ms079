package server;

import com.google.common.collect.Lists;
import constants.GameConstants;

import java.util.Collections;
import java.util.List;

public class RandomRewards {

    private static final RandomRewards instance = new RandomRewards();

    private final List<Integer> compiledGold = Lists.newArrayList();
    private final List<Integer> compiledSilver = Lists.newArrayList();
    private final List<Integer> compiledFishing = Lists.newArrayList();
    private final List<Integer> compiledEvent = Lists.newArrayList();
    private final List<Integer> compiledEventC = Lists.newArrayList();
    private final List<Integer> compiledEventB = Lists.newArrayList();
    private final List<Integer> compiledEventA = Lists.newArrayList();

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void init() {
        getInstance();
    }

    public static RandomRewards getInstance() {
        return instance;
    }

    private RandomRewards() {
        processRewards(compiledGold, GameConstants.goldrewards);
        processRewards(compiledSilver, GameConstants.silverrewards);
        processRewards(compiledFishing, GameConstants.fishingReward);
        processRewards(compiledEventC, GameConstants.eventCommonReward);
        processRewards(compiledEventB, GameConstants.eventUncommonReward);
        processRewards(compiledEventA, GameConstants.eventRareReward);
        processRewards(compiledEvent, GameConstants.eventSuperReward);
    }

    private static void processRewards(List<Integer> returnArray, int[] list) {
        int lastitem = 0;
        for (int i = 0; i < list.length; i++) {
            if (i % 2 == 0) { // 偶数==物品id
                lastitem = list[i];
            } else { // 奇数==物品数量
                for (int j = 0; j < list[i]; j++) {
                    returnArray.add(lastitem);
                }
            }
        }
        Collections.shuffle(returnArray);
    }

    public int getGoldBoxReward() {
        return compiledGold.get(Randomizer.nextInt(compiledGold.size()));
    }

    public int getSilverBoxReward() {
        return compiledSilver.get(Randomizer.nextInt(compiledSilver.size()));
    }

    public int getFishingReward() {
        return compiledFishing.get(Randomizer.nextInt(compiledFishing.size()));
    }

    public int getEventReward() {
        final int chance = Randomizer.nextInt(100);
        if (chance < 50) {
            return compiledEventC.get(Randomizer.nextInt(compiledEventC.size()));
        } else if (chance < 80) {
            return compiledEventB.get(Randomizer.nextInt(compiledEventB.size()));
        } else if (chance < 95) {
            return compiledEventA.get(Randomizer.nextInt(compiledEventA.size()));
        } else {
            return compiledEvent.get(Randomizer.nextInt(compiledEvent.size()));
        }
    }
}
