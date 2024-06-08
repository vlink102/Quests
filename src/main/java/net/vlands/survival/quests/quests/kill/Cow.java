package net.vlands.survival.quests.quests.kill;

import net.vlands.survival.quests.internal.Objective;
import net.vlands.survival.quests.internal.Quest;
import org.bukkit.entity.EntityType;

public class Cow extends Quest {
    public Cow() {
        super(
                39,
                "",
                "&4Kill &8- &eCow",
                0,
                new Objective(
                        Objective.Type.KILL,
                        3,
                        EntityType.PLAYER
                )
        );
    }
}
