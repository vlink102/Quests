package net.vlands.survival.quests.internal;

import java.util.Arrays;

public class Objective {
    public enum Type {
        MINE,
        CRAFT,
        EAT,
        DRINK,
        TRAVEL,
        BREW,
        COOK,
        DIE,
        KILL,
        TAME,
        RIDE,
        BREED,
        PLACE
    }

    private final Type type;
    private final int maxValue;
    private final Object[] relative;

    public Objective(Type type, int maxValue, Object... relative) {
        this.type = type;
        this.maxValue = maxValue;
        this.relative = relative;
    }

    public Type getType() {
        return type;
    }
    public int getMaxValue() {
        return maxValue;
    }
    public Object[] getRelative() {
        return relative;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Arrays.stream(relative).forEach(rel -> sb.append(rel.toString()));
        return "{" + type.toString() + ", " + maxValue + ", {" + sb + "}}";
    }
}
