package net.vlands.survival.quests.quests.loot;

import net.vlands.survival.quests.internal.Loot;
import net.vlands.survival.quests.rewards.ItemReward;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class LootChestTest extends Loot {
    public LootChestTest() {
        super(
                6,
                0,
                "l1",
                new ItemReward(
                        new ItemStack(
                                Material.NETHERITE_INGOT,
                                5
                        ),
                        5
                )
        );
    }
}
