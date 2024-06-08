package net.vlands.survival.quests.quests.die;

import net.vlands.survival.quests.internal.Objective;
import net.vlands.survival.quests.internal.Quest;

public class ThornsTest extends Quest {
    public ThornsTest() {
        super(
                52,
                "q52_die_thornstest",
                "&4Die &8- &ethorns",
                0,
                new Objective(
                        Objective.Type.DIE,
                        1,
                        Objective.DieType.THORNS
                )
        );
    }
}
