package net.vlands.survival.quests.quests.travel;

import net.vlands.survival.quests.internal.Objective;
import net.vlands.survival.quests.internal.Quest;
import net.vlands.survival.quests.rewards.ItemReward;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Minecart extends Quest {
    public Minecart() {
        super(
                24,
                "q24_travel_minecarttest",
                "&9Travel Test &8- &eMinecart",
                0,
                new Objective(
                        Objective.Type.TRAVEL,
                        10,
                        Objective.TravelType.MINECART
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
