package net.vlands.survival.quests.quests.die;

import net.vlands.survival.quests.internal.Objective;
import net.vlands.survival.quests.internal.Quest;

public class FallTest extends Quest {
    public FallTest() {
        super(
                37,
                "q37_die_falltest",
                "&4Die &8- &efall",
                0,
                new Objective(
                        Objective.Type.DIE,
                        1,
                        Objective.DieType.FALL
                )
        );
    }
}
