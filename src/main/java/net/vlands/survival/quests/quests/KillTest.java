package net.vlands.survival.quests.quests;

import net.vlands.survival.quests.internal.Objective;
import net.vlands.survival.quests.internal.Quest;
import net.vlands.survival.quests.rewards.ItemReward;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public class KillTest extends Quest {
    public KillTest() {
        super(
                9,
                "q9_killtest",
                "&4Kill Test",
                0,
                new Objective(
                        Objective.Type.KILL,
                        5,
                        EntityType.SHEEP
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
