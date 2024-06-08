package net.vlands.survival.quests.quests.die;

import net.vlands.survival.quests.internal.Objective;
import net.vlands.survival.quests.internal.Quest;

public class ContactTest extends Quest {
    public ContactTest() {
        super(
                30,
                "q30_die_contacttest",
                "&4Die &8- &eContact",
                0,
                new Objective(
                        Objective.Type.DIE,
                        1,
                        Objective.DieType.CONTACT,
                        Objective.ContactType.CACTUS
                )
        );
    }
}
