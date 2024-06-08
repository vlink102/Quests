package net.vlands.survival.quests.quests.die;

import net.vlands.survival.quests.internal.Objective;
import net.vlands.survival.quests.internal.Quest;

public class MagicTest extends Quest {
    public MagicTest() {
        super(
                45,
                "q45_die_magictest",
                "&4Die &8- &emagic",
                0,
                new Objective(
                        Objective.Type.DIE,
                        1,
                        Objective.DieType.MAGIC
                )
        );
    }
}
