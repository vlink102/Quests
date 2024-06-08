package net.vlands.survival.quests.quests.die;

import net.vlands.survival.quests.internal.Objective;
import net.vlands.survival.quests.internal.Quest;

public class VoidTest extends Quest {
    public VoidTest() {
        super(
                53,
                "q53_die_voidtest",
                "&4Die &8- &evoid",
                0,
                new Objective(
                        Objective.Type.DIE,
                        1,
                        Objective.DieType.VOID
                )
        );
    }
}
