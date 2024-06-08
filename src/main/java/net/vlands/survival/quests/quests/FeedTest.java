package net.vlands.survival.quests.quests;

import net.vlands.survival.quests.internal.Objective;
import net.vlands.survival.quests.internal.Quest;
import net.vlands.survival.quests.rewards.ItemReward;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public class FeedTest extends Quest {
    public FeedTest() {
        super(
                12,
                "q12_feedtest",
                "&bFeed Test",
                0,
                new Objective(
                        Objective.Type.FEED,
                        1,
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
