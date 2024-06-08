package net.vlands.survival.quests.quests;

import net.vlands.survival.quests.internal.Objective;
import net.vlands.survival.quests.internal.Quest;
import net.vlands.survival.quests.rewards.ItemReward;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class TravelTest extends Quest {
    public TravelTest() {
        super(
                5,
                "q5_traveltest",
                "&9Travel Test",
                0,
                new Objective(
                        Objective.Type.TRAVEL,
                        1000,
                        Objective.TravelType.ALL
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
