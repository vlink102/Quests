package net.vlands.survival.quests.quests.die;

import net.vlands.survival.quests.internal.Objective;
import net.vlands.survival.quests.internal.Quest;

public class ExplosionTest extends Quest {
    public ExplosionTest() {
        super(
                35,
                "q35_die_explosiontest",
                "&4Die &8- &eexplosion",
                0,
                new Objective(
                        Objective.Type.DIE,
                        1,
                        Objective.DieType.EXPLOSION
                )
        );
    }
}
