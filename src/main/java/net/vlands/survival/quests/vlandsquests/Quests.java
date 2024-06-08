package net.vlands.survival.quests.vlandsquests;

import net.vlands.survival.quests.Main;
import net.vlands.survival.quests.gui.RandomCreator;
import net.vlands.survival.quests.internal.Loot;
import net.vlands.survival.quests.internal.Objective;
import net.vlands.survival.quests.internal.Quest;
import net.vlands.survival.quests.internal.Util;
import net.vlands.survival.quests.rewards.ExpReward;
import net.vlands.survival.quests.rewards.ItemReward;
import net.vlands.survival.quests.rewards.PermissionReward;
import net.vlands.survival.quests.rewards.PotionEffectReward;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class Quests {
    public static class CustomItems {
        public static ItemStack getEnchantedBook(Enchantment enchant, int lvl) {
            ItemStack stack = new ItemStack(Material.ENCHANTED_BOOK);
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) stack.getItemMeta();
            meta.addEnchant(enchant, lvl, true);
            stack.setItemMeta(meta);
            return stack;
        }

        public static ItemStack getGearFragment() {
            ItemStack stack = new ItemStack(Material.POLISHED_BLACKSTONE_BUTTON);
            ItemMeta meta = stack.getItemMeta();
            meta.addEnchant(Enchantment.LUCK, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.setDisplayName(Util.colorize("&8Gear Fragment"));
            List<String> lore = new ArrayList<>();
            lore.add(Util.colorize("&7A dusty shard of a gear,"));
            lore.add(Util.colorize("&7once part of weaponry beyond"));
            lore.add(Util.colorize("&7imagination."));

            meta.setLore(lore);
            meta.getPersistentDataContainer().set(new NamespacedKey(Main.getPlugin(Main.class), "vlands"), PersistentDataType.INTEGER, 1);
            stack.setItemMeta(meta);
            return stack;
        }

        public static ItemStack getRotorAxle() {
            ItemStack stack = new ItemStack(Material.STICK);
            ItemMeta meta = stack.getItemMeta();
            meta.addEnchant(Enchantment.LUCK, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.setDisplayName(Util.colorize("&2Rotor Axle"));
            List<String> lore = new ArrayList<>();
            lore.add(Util.colorize("&7I wonder if this can be used,"));
            lore.add(Util.colorize("&7it looks quite strong."));

            meta.setLore(lore);
            meta.getPersistentDataContainer().set(new NamespacedKey(Main.getPlugin(Main.class), "vlands"), PersistentDataType.INTEGER, 1);
            stack.setItemMeta(meta);
            return stack;
        }

        public static ItemStack getShieldPanel() {
            ItemStack stack = new ItemStack(Material.REINFORCED_DEEPSLATE);
            ItemMeta meta = stack.getItemMeta();
            meta.addEnchant(Enchantment.LUCK, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.setDisplayName(Util.colorize("&5Shield Panel"));
            List<String> lore = new ArrayList<>();
            lore.add(Util.colorize("&7The citizens who acquired"));
            lore.add(Util.colorize("&7these items must've had"));
            lore.add(Util.colorize("&7incredibly powerful machinery."));
            meta.setLore(lore);
            meta.getPersistentDataContainer().set(new NamespacedKey(Main.getPlugin(Main.class), "vlands"), PersistentDataType.INTEGER, 1);
            stack.setItemMeta(meta);
            return stack;
        }

        public static ItemStack getBeamRefractor() {
            ItemStack stack = new ItemStack(Material.TINTED_GLASS);
            ItemMeta meta = stack.getItemMeta();
            meta.addEnchant(Enchantment.LUCK, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.setDisplayName(Util.colorize("&3Beam Refractor"));
            List<String> lore = new ArrayList<>();
            lore.add(Util.colorize("&7A module that can harness the"));
            lore.add(Util.colorize("&7power of energy beams rivaling the"));
            lore.add(Util.colorize("&7strength of elder guardians..."));
            meta.setLore(lore);
            meta.getPersistentDataContainer().set(new NamespacedKey(Main.getPlugin(Main.class), "vlands"), PersistentDataType.INTEGER, 1);
            stack.setItemMeta(meta);
            return stack;
        }
    }

    public static class EnchantedMelons extends Quest {
        public EnchantedMelons() {
            super(
                    2, "", "&aMelons", 0,
                    new Objective(Objective.Type.EAT, 120, Material.MELON_SLICE),
                    new ItemReward(new ItemStack(Material.MELON_SEEDS), 50),
                    new ItemReward(new ItemStack(Material.GOLD_INGOT), 6),
                    new ItemReward(CustomItems.getGearFragment(), 5),
                    new ItemReward(new ItemStack(Material.IRON_HOE), 1),
                    new ExpReward(ExpReward.Type.LEVEL, 2)
            );
        }
    }

    public static class GoldDigger extends Quest {
        public GoldDigger() {
            super(
                    1, "q1_golddigger", "&6Gold Digger", 0,
                    new Objective(Objective.Type.MINE, 150, Material.GOLD_ORE),
                    new ExpReward(ExpReward.Type.LEVEL, 4),
                    new ItemReward(new ItemStack(Material.EMERALD), 16),
                    new ItemReward(new ItemStack(Material.GOLD_BLOCK), 3),
                    new ItemReward(new ItemStack(Material.IRON_PICKAXE), 1),
                    new PotionEffectReward(PotionEffectType.FAST_DIGGING, 1, 1800)
            );
        }
    }

    public static class RockBottom extends Quest {
        public RockBottom() {
            super(
                    3, "", "&8Rock Bottom", 0,
                    new Objective(Objective.Type.PLACE, 32, Material.COBBLED_DEEPSLATE, Material.BEDROCK),
                    new ItemReward(CustomItems.getShieldPanel(), 1),
                    new ItemReward(new ItemStack(Material.IRON_PICKAXE), 3),
                    new ItemReward(new ItemStack(Material.ANCIENT_DEBRIS), 2),
                    new ExpReward(ExpReward.Type.LEVEL, 3),
                    new PotionEffectReward(PotionEffectType.INCREASE_DAMAGE, 1, 600)
            );
        }
    }

    public static class SightSeer extends Quest {
        public SightSeer() {
            super(
                    4, "", "&bSight-Seer", 0,
                    new Objective(Objective.Type.TRAVEL, 1000, Objective.TravelType.AVIATE),
                    new ItemReward(new ItemStack(Material.PHANTOM_MEMBRANE), 9),
                    new ItemReward(new ItemStack(Material.MAP), 3),
                    new ItemReward(new ItemStack(Material.DRAGON_BREATH), 1),
                    new PotionEffectReward(PotionEffectType.SPEED, 1, 1200),
                    new ExpReward(ExpReward.Type.LEVEL, 6)
            );
        }
    }

    public static class Poseidon extends Quest {
        public Poseidon() {
            super(
                    5, "", "&3Poseidon", 0,
                    new Objective(Objective.Type.DIE, 3, Objective.DieType.PROJECTILE, Objective.ProjectileType.TRIDENT, EntityType.DROWNED),
                    new PotionEffectReward(PotionEffectType.WATER_BREATHING, 1, 3600),
                    new ItemReward(CustomItems.getBeamRefractor(), 1),
                    new ItemReward(new ItemStack(Material.DIAMOND_CHESTPLATE), 1),
                    new ItemReward(new ItemStack(Material.FISHING_ROD), 1),
                    new ItemReward(new ItemStack(Material.TROPICAL_FISH_BUCKET), 1),
                    new ItemReward(new ItemStack(Material.COD), 28)
            );
        }
    }

    public static class MilestoneOne extends Loot {
        public MilestoneOne() {
            super(
                    6, 4, "m1",
                    new ItemReward(CustomItems.getRotorAxle(), 3),
                    new ItemReward(new ItemStack(Material.NETHERITE_INGOT), 1),
                    new ItemReward(new ItemStack(Material.EXPERIENCE_BOTTLE), 35),
                    new PotionEffectReward(PotionEffectType.REGENERATION, 1, 2400),
                    new ExpReward(ExpReward.Type.LEVEL, 10),
                    new PermissionReward(
                            "aqua.command.nick",
                            "permanent",
                            "&7Access to &a/nick &6(&bPERMANENT&6)"
                    )
            );
        }
    }

    public static class JacketPotato extends Quest {
        public JacketPotato() {
            super(
                    7, "", "&eJacket Potato", 2,
                    new Objective(Objective.Type.COOK, 500, Material.BAKED_POTATO),
                    new ItemReward(new ItemStack(Material.ANCIENT_DEBRIS), 3),
                    new ItemReward(new ItemStack(Material.DIAMOND_LEGGINGS), 1),
                    new ItemReward(new ItemStack(Material.BLAZE_POWDER), 15),
                    new ExpReward(ExpReward.Type.LEVEL, 5),
                    new PotionEffectReward(PotionEffectType.SATURATION, 1, 1200)
            );
        }
    }

    public static class Prickly extends Quest {
        public Prickly() {
            super(
                    8, "", "&2Prickly", 2,
                    new Objective(Objective.Type.DIE, 20, Objective.DieType.CONTACT, Objective.ContactType.CACTUS),
                    new ItemReward(CustomItems.getEnchantedBook(Enchantment.MENDING, 1), 2),
                    new ItemReward(CustomItems.getEnchantedBook(Enchantment.DURABILITY, 3), 3),
                    new ItemReward(CustomItems.getBeamRefractor(), 1),
                    new PotionEffectReward(PotionEffectType.DAMAGE_RESISTANCE, 1, 600),
                    new ExpReward(ExpReward.Type.LEVEL, 3)
            );
        }
    }

    public static class Pain extends Quest {
        public Pain() {
            super(
                    9, "", "&4Pain", 2,
                    new Objective(Objective.Type.TRAVEL, 500, Objective.TravelType.CROUCH),
                    new ItemReward(CustomItems.getEnchantedBook(Enchantment.SOUL_SPEED, 3), 2),
                    new ItemReward(new ItemStack(Material.DIAMOND_BOOTS), 1),
                    new PotionEffectReward(PotionEffectType.SPEED, 2, 1200),
                    new ExpReward(ExpReward.Type.LEVEL, 2)
            );
        }
    }

    public static class SlaughterHouse extends Quest {
        public SlaughterHouse() {
            super(
                    10, "", "&cSlaughter House", 2,
                    new Objective(Objective.Type.KILL, 100, EntityType.COW, Material.WOODEN_SWORD),
                    new ItemReward(CustomItems.getEnchantedBook(Enchantment.FIRE_ASPECT, 2), 1),
                    new ItemReward(new ItemStack(Material.WHITE_CARPET, 128), 128),
                    new PotionEffectReward(PotionEffectType.HEALTH_BOOST, 3, 3600),
                    new ExpReward(ExpReward.Type.LEVEL, 6),
                    new ItemReward(CustomItems.getEnchantedBook(Enchantment.DAMAGE_ALL, 4), 4),
                    new PermissionReward(
                            "aqua.command.skull",
                            "permanent",
                            "&7Get a player's head with &a/skull &6(&bPERMANENT&6)"
                    )
            );
        }
    }

    public static class Assassin extends Quest {
        public Assassin() {
            super(
                    11, "", "&5Assassin", 2,
                    new Objective(Objective.Type.KILL, 20, EntityType.PLAYER),
                    new ItemReward(CustomItems.getEnchantedBook(Enchantment.KNOCKBACK, 3), 1),
                    new ItemReward(CustomItems.getEnchantedBook(Enchantment.SWEEPING_EDGE,3), 1),
                    new ExpReward(ExpReward.Type.LEVEL, 10),
                    new PotionEffectReward(PotionEffectType.INCREASE_DAMAGE, 2, 450)
            );
        }
    }

    public static class MilestoneTwo extends Loot {
        public MilestoneTwo() {
            super(
                    12, 8, "m2",
                    new ItemReward(CustomItems.getGearFragment(), 6),
                    new ItemReward(CustomItems.getShieldPanel(), 1),
                    new ItemReward(CustomItems.getEnchantedBook(Enchantment.ARROW_INFINITE, 1), 2),
                    new ItemReward(CustomItems.getEnchantedBook(Enchantment.RIPTIDE, 3), 1),
                    new ItemReward(CustomItems.getEnchantedBook(Enchantment.DAMAGE_ALL, 6), 1),
                    new ExpReward(ExpReward.Type.LEVEL, 15),
                    new PermissionReward(
                            "aqua.command.day",
                            "permanent",
                            "&7Switch the time to &bDay &b(&a/day&b)"
                    ),
                    new PermissionReward(
                            "aqua.command.night",
                            "permanent",
                            "&7Switch the time to &3Night &b(&a/night&b)"
                    ),
                    new PotionEffectReward(PotionEffectType.INVISIBILITY, 1, 1800)
            );
        }
    }
}
