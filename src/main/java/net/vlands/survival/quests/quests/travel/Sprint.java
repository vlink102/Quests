package net.vlands.survival.quests.quests.travel;

import net.vlands.survival.quests.internal.Objective;
import net.vlands.survival.quests.internal.Quest;
import net.vlands.survival.quests.rewards.ItemReward;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Sprint extends Quest {
    public Sprint() {
        super(
                17,
                "q16_travel_sprinttest",
                "&9Travel Test &8- &eSprint",
                0,
                new Objective(
                        Objective.Type.TRAVEL,
                        10,
                        Objective.TravelType.SPRINT
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
