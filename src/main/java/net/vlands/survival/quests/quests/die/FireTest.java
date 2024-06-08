package net.vlands.survival.quests.quests.die;

import net.vlands.survival.quests.internal.Objective;
import net.vlands.survival.quests.internal.Quest;

public class FireTest extends Quest {
    public FireTest() {
        super(
                38,
                "q38_die_firetest",
                "&4Die &8- &efire",
                0,
                new Objective(
                        Objective.Type.DIE,
                        1,
                        Objective.DieType.FIRE
                )
        );
    }
}
