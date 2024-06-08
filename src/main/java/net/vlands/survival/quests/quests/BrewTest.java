package net.vlands.survival.quests.quests;

import net.vlands.survival.quests.internal.Objective;
import net.vlands.survival.quests.internal.Quest;
import net.vlands.survival.quests.rewards.ItemReward;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;

public class BrewTest extends Quest {
    public BrewTest() {
        super(
                14,
                "q14_brewtest",
                "&5Brew Test",
                0,
                new Objective(
                        Objective.Type.BREW,
                        1,
                        PotionType.FIRE_RESISTANCE
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
