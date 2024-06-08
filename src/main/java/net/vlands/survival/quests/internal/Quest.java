package net.vlands.survival.quests.internal;

import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.StringJoiner;

public class Quest {
    private final int id;
    private final String internalName;
    private final String prettyName;
    private final int unlockCount;
    private final Objective objective;
    private final Object[] rewards;

    public Quest(int id, String internalName, String prettyName, int unlockCount, Objective objective, Object... rewards) {
        this.id = id;
        this.prettyName = prettyName;
        this.internalName = ChatColor.stripColor(Util.colorize(prettyName)).toLowerCase().replaceAll("[^a-zA-Z\\d]", "");
        this.unlockCount = unlockCount;
        this.objective = objective;
        this.rewards = rewards;
    }

    public int getUnlockCount() {
        return unlockCount;
    }

    public int getId() {
        return id;
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
    public Object[] getRewards() {
        return rewards;
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(",");
        Arrays.stream(rewards).forEach(reward -> joiner.add(reward.toString()));
        return "{" + internalName + ", " + prettyName + ", " + objective.toString() + ", " + joiner + "}";
    }
}
