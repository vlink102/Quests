package net.vlands.survival.quests.rewards;

import org.bukkit.inventory.ItemStack;

public class ItemReward {
    private final ItemStack itemStack;
    private final int amount;

    public ItemReward(ItemStack stack, int amount) {
        stack.setAmount(amount);
        this.itemStack = stack;
        this.amount = amount;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
    public int getAmount() {
        return amount;
    }
}