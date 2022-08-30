package net.vlands.survival.quests.internal;

import java.util.Arrays;
import java.util.StringJoiner;

public abstract class Quest {
    private final String internalName;
    private final String prettyName;
    private final Objective objective;
    private final Reward[] rewards;

    public Quest(String internalName, String prettyName, Objective objective, Reward... rewards) {
        this.internalName = internalName;
        this.prettyName = prettyName;
        this.objective = objective;
        this.rewards = rewards;
    }

    public String getInternalName() {
        return internalName;
    }
    public String getPrettyName() {
        return prettyName;
    }
    public Objective getObjective() {
        return objective;
    }
    public Reward[] getRewards() {
        return rewards;
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(",");
        Arrays.stream(rewards).forEach(reward -> joiner.add(reward.toString()));
        return "{" + internalName + ", " + prettyName + ", " + objective.toString() + ", " + joiner + "}";
    }
}
