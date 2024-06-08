package net.vlands.survival.quests.quests.die;

import net.vlands.survival.quests.internal.Objective;
import net.vlands.survival.quests.internal.Quest;

public class CrammingTest extends Quest {
    public CrammingTest() {
        super(
                31,
                "q31_die_crammingtest",
                "&4Die &8- &ecramming",
                0,
                new Objective(
                        Objective.Type.DIE,
                        1,
                        Objective.DieType.CRAMMING
                )
        );
    }
}
