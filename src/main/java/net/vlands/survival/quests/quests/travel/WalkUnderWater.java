package net.vlands.survival.quests.quests.travel;

import net.vlands.survival.quests.internal.Objective;
import net.vlands.survival.quests.internal.Quest;
import net.vlands.survival.quests.rewards.ItemReward;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class WalkUnderWater extends Quest {
    public WalkUnderWater() {
        super(
                29,
                "q29_travel_walkunderwatertest",
                "&9Travel Test &8- &eWalk on Water",
                0,
                new Objective(
                        Objective.Type.TRAVEL,
                        10,
                        Objective.TravelType.WALK_UNDER_WATER
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
