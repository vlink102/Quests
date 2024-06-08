package net.vlands.survival.quests.quests.die;

import net.vlands.survival.quests.internal.Objective;
import net.vlands.survival.quests.internal.Quest;

public class DrowningTest extends Quest {
    public DrowningTest() {
        super(
                33,
                "q33_die_drowningtest",
                "&4Die &8- &edrowning",
                0,
                new Objective(
                        Objective.Type.DIE,
                        1,
                        Objective.DieType.DROWNING
                )
        );
    }
}
