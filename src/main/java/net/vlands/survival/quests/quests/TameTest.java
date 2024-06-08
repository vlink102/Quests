package net.vlands.survival.quests.quests;

import net.vlands.survival.quests.internal.Objective;
import net.vlands.survival.quests.internal.Quest;
import net.vlands.survival.quests.rewards.ItemReward;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public class TameTest extends Quest {
    public TameTest() {
        super(
                10,
                "q10_tametest",
                "&aTame Test",
                0,
                new Objective(
                        Objective.Type.TAME,
                        1,
                        EntityType.OCELOT
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
