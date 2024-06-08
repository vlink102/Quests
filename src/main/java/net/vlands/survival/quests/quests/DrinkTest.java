package net.vlands.survival.quests.quests;

import net.vlands.survival.quests.internal.Objective;
import net.vlands.survival.quests.internal.Quest;
import net.vlands.survival.quests.rewards.ItemReward;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;

public class DrinkTest extends Quest {
    public DrinkTest() {
        super(
                4,
                "q4_drinktest",
                "&dDrink Test",
                0,
                new Objective(
                        Objective.Type.DRINK,
                        1,
                        PotionType.WATER
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
