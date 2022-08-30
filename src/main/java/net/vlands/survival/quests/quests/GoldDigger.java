package net.vlands.survival.quests.quests;

import net.vlands.survival.quests.internal.Objective;
import net.vlands.survival.quests.internal.Quest;
import net.vlands.survival.quests.internal.Reward;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GoldDigger extends Quest {
    public GoldDigger() {
        super(
                "q1_golddigger",
                "&6Gold Digger",

                new Objective(Objective.Type.MINE, 30, Material.GOLD_ORE),
                new Reward(
                        Reward.Type.EXP,
                        Reward.ExpType.LEVEL,
                        1
                ),
                new Reward(
                        Reward.Type.ITEM,
                        new ItemStack(Material.EMERALD),
                        1
                ),
                new Reward(
                        Reward.Type.POTIONEFFECT,
                        new PotionEffect(
                                PotionEffectType.FAST_DIGGING,
                                600,
                                1
                        )
                )
        );
    }
}
