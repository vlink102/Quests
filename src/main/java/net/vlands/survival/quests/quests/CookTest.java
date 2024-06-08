package net.vlands.survival.quests.quests;

import net.vlands.survival.quests.internal.Objective;
import net.vlands.survival.quests.internal.Quest;
import net.vlands.survival.quests.rewards.ItemReward;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CookTest extends Quest {
    public CookTest() {
        super(
                7,
                "q7_cooktest",
                "&cCook Test",
                0,
                new Objective(
                        Objective.Type.COOK,
                        5,
                        Material.COOKED_PORKCHOP
                ),
                new ItemReward(
                        new ItemStack(
                                Material.DIRT,
                                1
                        ),
                        1
                )
        );
    }
}
