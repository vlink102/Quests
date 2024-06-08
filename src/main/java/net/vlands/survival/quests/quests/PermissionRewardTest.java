package net.vlands.survival.quests.quests;

import net.vlands.survival.quests.internal.Objective;
import net.vlands.survival.quests.internal.Quest;
import net.vlands.survival.quests.rewards.ExpReward;
import net.vlands.survival.quests.rewards.ItemReward;
import net.vlands.survival.quests.rewards.PermissionReward;
import net.vlands.survival.quests.rewards.PotionEffectReward;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

public class PermissionRewardTest extends Quest {
    public PermissionRewardTest() {
        super(
                46,
                "",
                "&5Permission test",
                1,
                new Objective(
                        Objective.Type.KILL,
                        3,
                        EntityType.SHEEP,
                        Material.NETHERITE_SHOVEL
                ),
                new ItemReward(
                        new ItemStack(
                                Material.RED_WOOL,
                                15
                        ), 15
                ),
                new ExpReward(
                        ExpReward.Type.LEVEL,
                        69
                ),
                new PotionEffectReward(
                        PotionEffectType.SPEED,
                        2,
                        90
                ),
                new PermissionReward(
                        "aqua.command.nick",
                        "permanent",
                        "&7Access to &a/nick &6(&bPERMANENT&6)"
                )
        );
    }
}
