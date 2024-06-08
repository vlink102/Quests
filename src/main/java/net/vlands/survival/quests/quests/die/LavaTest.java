package net.vlands.survival.quests.quests.die;

import net.vlands.survival.quests.internal.Objective;
import net.vlands.survival.quests.internal.Quest;

public class LavaTest extends Quest {
    public LavaTest() {
        super(
                43,
                "q43_die_lavatest",
                "&4Die &8- &elava",
                0,
                new Objective(
                        Objective.Type.DIE,
                        1,
                        Objective.DieType.LAVA
                )
        );
    }
}
