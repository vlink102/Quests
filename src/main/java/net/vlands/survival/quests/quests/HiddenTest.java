package net.vlands.survival.quests.quests;

import net.vlands.survival.quests.internal.Objective;
import net.vlands.survival.quests.internal.Quest;
import net.vlands.survival.quests.rewards.ExpReward;
import org.bukkit.Material;

public class HiddenTest extends Quest {
    public HiddenTest() {
        super(
                15,
                "q15_hiddentest",
                "&dYou should see this after 1 quest",
                1,
                new Objective(
                        Objective.Type.MINE,
                        5,
                        Material.DIRT
                ),
                new ExpReward(
                        ExpReward.Type.SINGLE,
                        69
                )
        );
    }
}
