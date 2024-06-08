package net.vlands.survival.quests.quests.die;

import net.vlands.survival.quests.internal.Objective;
import net.vlands.survival.quests.internal.Quest;

public class LightningTest extends Quest {
    public LightningTest() {
        super(
                44,
                "q44_die_lightningtest",
                "&4Die &8- &elightning",
                0,
                new Objective(
                        Objective.Type.DIE,
                        1,
                        Objective.DieType.LIGHTNING
                )
        );
    }
}
