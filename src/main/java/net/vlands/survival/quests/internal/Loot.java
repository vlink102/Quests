package net.vlands.survival.quests.internal;

public class Loot {
    private final int id;
    private final int unlockRequirement;
    private final String internalName;
    private final Object[] rewards;

    public Loot(int id, int unlockRequirement, String internalName, Object... rewards) {
        this.id = id;
        this.unlockRequirement = unlockRequirement;
        this.internalName = internalName;
        this.rewards = rewards;
    }

    public int getId() {
        return id;
    }

    public String getInternalName() {
        return internalName;
    }

    public int getQuestsTillUnlocked() {
        return unlockRequirement;
    }

    public Object[] getRewards() {
        return rewards;
    }
}
