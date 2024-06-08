package net.vlands.survival.quests.quests;

import net.vlands.survival.quests.internal.Objective;
import net.vlands.survival.quests.internal.Quest;
import net.vlands.survival.quests.rewards.ItemReward;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class PlaceTest extends Quest {
    public PlaceTest() {
        super(
                13,
                "q13_placetest",
                "&7Place Test",
                0,
                new Objective(
                        Objective.Type.PLACE,
                        100,
                        Material.TORCH
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
