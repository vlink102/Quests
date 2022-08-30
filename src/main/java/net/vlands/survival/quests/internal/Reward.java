package net.vlands.survival.quests.internal;

import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class Reward {
    public enum Type {
        EXP,
        ITEM,
        POTIONEFFECT
    }
    public enum ExpType {
        LEVEL,
        SINGLE
    }

    private final Type type;

    private ItemStack itemStack;
    private int amount;

    private ExpType expType;
    private int number;

    private PotionEffect potionEffect;

    public Reward(Type type, ItemStack itemStack, int amount) {
        this.type = type;
        this.itemStack = itemStack;
        this.amount = amount;
    }
    public Reward(Type type, ExpType expType, int number) {
        this.type = type;
        this.expType = expType;
        this.number = number;
    }
    public Reward(Type type, PotionEffect potionEffect) {
        this.type = type;
        this.potionEffect = potionEffect;
    }

    public Type getType() {
        return type;
    }
    public ItemStack getItemStack() {
        return itemStack;
    }
    public int getAmount() {
        return amount;
    }
    public ExpType getExpType() {
        return expType;
    }
    public int getNumber() {
        return number;
    }
    public PotionEffect getPotionEffect() {
        return potionEffect;
    }

    @Override
    public String toString() {
        return "{" + type.toString() + ", {" + (itemStack == null ? "null" : itemStack.toString()) + "}, " + amount + ", " + (expType == null ? "null" : expType.toString()) + ", " + number + ", " + (potionEffect == null ? "null" : potionEffect.toString()) + "}";
    }
}
