package net.vlands.survival.quests.rewards;

public class ExpReward {
    private final Type expType;
    private final int amount;

    public enum Type {
        LEVEL,
        SINGLE
    }

    public ExpReward(Type type, int amount) {
        this.expType = type;
        this.amount = amount;
    }

    public Type getExpType() {
        return expType;
    }
    public int getAmount() {
        return amount;
    }
}
