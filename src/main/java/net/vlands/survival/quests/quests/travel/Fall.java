package net.vlands.survival.quests.quests.travel;

import net.vlands.survival.quests.internal.Objective;
import net.vlands.survival.quests.internal.Quest;
import net.vlands.survival.quests.rewards.ItemReward;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Fall extends Quest {
    public Fall() {
        super(
                16,
                "q16_travel_falltest",
                "&9Travel Test &8- &eFall",
                0,
                new Objective(
                        Objective.Type.TRAVEL,
                        10,
                        Objective.TravelType.FALL
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
