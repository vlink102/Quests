package net.vlands.survival.quests.quests.travel;

import net.vlands.survival.quests.internal.Objective;
import net.vlands.survival.quests.internal.Quest;
import net.vlands.survival.quests.rewards.ItemReward;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Climb extends Quest {
    public Climb() {
        super(
                21,
                "q21_travel_climbtest",
                "&9Travel Test &8- &eClimb",
                0,
                new Objective(
                        Objective.Type.TRAVEL,
                        10,
                        Objective.TravelType.CLIMB
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
