package net.vlands.survival.quests.quests.die;

import net.vlands.survival.quests.internal.Objective;
import net.vlands.survival.quests.internal.Quest;

public class HotFloorTest extends Quest {
    public HotFloorTest() {
        super(
                42,
                "q42_die_hotfloortest",
                "&4Die &8- &ehotfloor",
                0,
                new Objective(
                        Objective.Type.DIE,
                        1,
                        Objective.DieType.HOT_FLOOR
                )
        );
    }
}
