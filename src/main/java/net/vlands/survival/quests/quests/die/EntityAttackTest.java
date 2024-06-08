package net.vlands.survival.quests.quests.die;

import net.vlands.survival.quests.internal.Objective;
import net.vlands.survival.quests.internal.Quest;

public class EntityAttackTest extends Quest {
    public EntityAttackTest() {
        super(
                34,
                "q34_die_entityattacktest",
                "&4Die &8- &eentityattack",
                0,
                new Objective(
                        Objective.Type.DIE,
                        2,
                        Objective.DieType.ENTITY_ATTACK
                )
        );
    }
}
