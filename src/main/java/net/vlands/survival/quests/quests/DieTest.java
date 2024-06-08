package net.vlands.survival.quests.quests;

import net.vlands.survival.quests.internal.Objective;
import net.vlands.survival.quests.internal.Quest;
import net.vlands.survival.quests.rewards.ItemReward;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class DieTest extends Quest {
    public DieTest() {
        super(
                8,
                "q8_dietest",
                "&4Die Test",
                0,
                new Objective(
                        Objective.Type.DIE,
                        2,
                        Objective.DieType.ANY
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
