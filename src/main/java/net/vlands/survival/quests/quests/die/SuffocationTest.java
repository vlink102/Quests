package net.vlands.survival.quests.quests.die;

import net.vlands.survival.quests.internal.Objective;
import net.vlands.survival.quests.internal.Quest;

public class SuffocationTest extends Quest {
    public SuffocationTest() {
        super(
                50,
                "q50_die_suffocationtest",
                "&4Die &8- &esuffocation",
                0,
                new Objective(
                        Objective.Type.DIE,
                        1,
                        Objective.DieType.SUFFOCATION
                )
        );
    }
}
