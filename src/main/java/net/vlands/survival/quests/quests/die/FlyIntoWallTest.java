package net.vlands.survival.quests.quests.die;

import net.vlands.survival.quests.internal.Objective;
import net.vlands.survival.quests.internal.Quest;

public class FlyIntoWallTest extends Quest {
    public FlyIntoWallTest() {
        super(
                40,
                "q40_die_flyintowalltest",
                "&4Die &8- &eflyintowall",
                0,
                new Objective(
                        Objective.Type.DIE,
                        1,
                        Objective.DieType.FLY_INTO_WALL
                )
        );
    }
}
