package net.vlands.survival.quests.quests;

import net.vlands.survival.quests.internal.Objective;
import net.vlands.survival.quests.internal.Quest;
import net.vlands.survival.quests.rewards.ItemReward;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CraftTest extends Quest {
    public CraftTest() {
        super(
                2,
                "q2_crafttest",
                "&6Craft Test",
                0,

                new Objective(
                        Objective.Type.CRAFT,
                        1,
                        Material.BEACON
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
