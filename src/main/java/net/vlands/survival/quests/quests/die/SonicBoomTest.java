package net.vlands.survival.quests.quests.die;

import net.vlands.survival.quests.internal.Objective;
import net.vlands.survival.quests.internal.Quest;

public class SonicBoomTest extends Quest {
    public SonicBoomTest() {
        super(
                48,
                "q48_die_sonicboomtest",
                "&4Die &8- &esonicboom",
                0,
                new Objective(
                        Objective.Type.DIE,
                        1,
                        Objective.DieType.SONIC_BOOM
                )
        );
    }
}
