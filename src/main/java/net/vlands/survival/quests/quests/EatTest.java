package net.vlands.survival.quests.quests;

import net.vlands.survival.quests.internal.Objective;
import net.vlands.survival.quests.internal.Quest;
import net.vlands.survival.quests.rewards.ItemReward;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class EatTest extends Quest {
    public EatTest() {
        super(
                3,
                "q3_eattest",
                "&2Eat Test",
                0,
                new Objective(
                        Objective.Type.EAT,
                        16,
                        Material.COOKED_BEEF
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
