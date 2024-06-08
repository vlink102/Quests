package net.vlands.survival.quests.quests.die;

import net.vlands.survival.quests.internal.Objective;
import net.vlands.survival.quests.internal.Quest;
import org.bukkit.entity.EntityType;

public class ProjectileTest extends Quest {
    public ProjectileTest() {
        super(
                47,
                "q47_die_projectiletest",
                "&4Die &8- &eprojectile",
                0,
                new Objective(
                        Objective.Type.DIE,
                        1,
                        Objective.DieType.PROJECTILE,
                        Objective.ProjectileType.ARROW,
                        EntityType.SKELETON
                )
        );
    }
}
