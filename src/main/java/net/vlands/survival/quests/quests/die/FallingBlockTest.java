package net.vlands.survival.quests.quests.die;

import net.vlands.survival.quests.internal.Objective;
import net.vlands.survival.quests.internal.Quest;

public class FallingBlockTest extends Quest {
    public FallingBlockTest() {
        super(
                36,
                "q36_die_fallingblocktest",
                "&4Die &8- &efallingblock",
                0,
                new Objective(
                        Objective.Type.DIE,
                        1,
                        Objective.DieType.FALLING_BLOCK
                )
        );
    }
}
