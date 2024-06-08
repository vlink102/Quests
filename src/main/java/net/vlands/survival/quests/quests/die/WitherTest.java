package net.vlands.survival.quests.quests.die;

import net.vlands.survival.quests.internal.Objective;
import net.vlands.survival.quests.internal.Quest;

public class WitherTest extends Quest {
    public WitherTest() {
        super(
                54,
                "q54_die_withertest",
                "&4Die &8- &ewither",
                0,
                new Objective(
                        Objective.Type.DIE,
                        1,
                        Objective.DieType.WITHER
                )
        );
    }
}
