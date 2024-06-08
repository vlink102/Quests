package net.vlands.survival.quests.quests;

import net.vlands.survival.quests.internal.Objective;
import net.vlands.survival.quests.internal.Quest;
import org.bukkit.entity.EntityType;

public class BreedTest extends Quest {
    public BreedTest() {
        super(
                11,
                "",
                "&dBreed Test",
                0,
                new Objective(
                        Objective.Type.BREED,
                        1,
                        EntityType.COW
                )
        );
    }
}
