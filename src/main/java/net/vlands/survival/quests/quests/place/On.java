package net.vlands.survival.quests.quests.place;

import net.vlands.survival.quests.internal.Objective;
import net.vlands.survival.quests.internal.Quest;
import org.bukkit.Material;

public class On extends Quest {
    public On() {
        super(
                51,
                "",
                "&7Rock Bottom",
                0,
                new Objective(
                        Objective.Type.PLACE,
                        1,
                        Material.STONE,
                        Material.BEDROCK
                )
        );
    }
}
