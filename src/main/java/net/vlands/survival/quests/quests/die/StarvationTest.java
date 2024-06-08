package net.vlands.survival.quests.quests.die;

import net.vlands.survival.quests.internal.Objective;
import net.vlands.survival.quests.internal.Quest;

public class StarvationTest extends Quest {
    public StarvationTest() {
        super(
                49,
                "q49_die_starvationtest",
                "&4Die &8- &estarvation",
                0,
                new Objective(
                        Objective.Type.DIE,
                        1,
                        Objective.DieType.STARVATION
                )
        );
    }
}
