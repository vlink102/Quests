package net.vlands.survival.quests.quests.die.entityattack;

import net.vlands.survival.quests.internal.Objective;
import net.vlands.survival.quests.internal.Quest;
import net.vlands.survival.quests.rewards.ItemReward;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public class Zombie extends Quest {
    public Zombie() {
        super(
               32,
               "q32_die_eattack_zombie",
               "&7Die to a &2Zombie",
               0,
                new Objective(
                       Objective.Type.DIE,
                       1,
                       Objective.DieType.ENTITY_ATTACK,
                       EntityType.ZOMBIE
                ),
                new ItemReward(
                        new ItemStack(
                                Material.NETHERITE_BLOCK,
                                5
                        ),
                        5
                )
        );
    }
}
