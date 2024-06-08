package net.vlands.survival.quests.quests.die;

import net.vlands.survival.quests.internal.Objective;
import net.vlands.survival.quests.internal.Quest;

public class FreezeTest extends Quest {
    public FreezeTest() {
        super(
                41,
                "q41_die_freezetest",
                "&4Die &8- &efreeze",
                0,
                new Objective(
                        Objective.Type.DIE,
                        1,
                        Objective.DieType.FREEZE
                )
        );
    }
}
