package net.vlands.survival.quests.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import net.vlands.survival.quests.Main;
import net.vlands.survival.quests.internal.*;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.List;

public class DailyCreator {
    private final Main plugin;

    public DailyCreator(Main plugin) {
        this.plugin = plugin;
    }

    public class DailyRewards {
        private boolean claimed = false;
        private final List<PotionEffect> effects = new ArrayList<>();
        private final HashMap<ItemStack, ItemRarity> itemStacks = new HashMap<>();
        private int expLevels;

        public DailyRewards(Player player) {
            randomizeAll(player);
        }

        public void setExpLevels(int expLevels) {
            this.expLevels = expLevels;
        }
        public int getExpLevels() {
            return expLevels;
        }

        public HashMap<ItemStack, ItemRarity> getItemStacks() {
            return itemStacks;
        }

        public List<PotionEffect> getEffects() {
            return effects;
        }

        public void setClaimed(boolean claimed) {
            this.claimed = claimed;
        }

        public boolean isClaimed() {
            return claimed;
        }

        public void addItem(int completed) {
            ItemStack stack = new ItemStack(Material.values()[random.nextInt(Material.values().length)]);
            ItemRarity rarity = getRarity(stack.getType());
            switch (rarity) {
                case COMMON -> stack.setAmount(completed <= 5 ? random(10,20) : random(15,30));
                case UNCOMMON -> stack.setAmount(completed <= 10 ? random(3,7) : random(5,12));
                case RARE -> stack.setAmount(completed <= 15 ? random(1,2) : random(2,4));
                case LEGENDARY -> stack.setAmount(completed <= 20 ? 1 : random(1,2));
                case ILLEGAL -> {
                    addItem(completed);
                    return;
                }
            }
            itemStacks.put(stack, rarity);
        }

        public void addEffect(GoodEffect effect, int completed) {
            int amplifier = (completed <= 10 ? 0 : 1);
            int duration = (completed <= 10 ? random(1200, 3600) : random(3600, 7200));
            effects.add(new PotionEffect(effect.getType(), duration * 20, amplifier));
        }

        public void randomizeExp(Player player) {
            expLevels = 0;
            int completed = player.getCompletedQuestsVal();
            if (completed <= 5) {
                setExpLevels(random(1, 4));
            } else if (completed <= 10) {
                setExpLevels(random(3, 6));
            } else if (completed <= 15) {
                setExpLevels(random(5, 8));
            } else {
                setExpLevels(random(7, 10));
            }
        }

        public void randomizeEffects(Player player) {
            effects.clear();
            int completed = player.getCompletedQuestsVal();
            if (completed <= 15) {
                addEffect(GoodEffect.values()[random.nextInt(GoodEffect.values().length)], completed);
            } else {
                addEffect(GoodEffect.values()[random.nextInt(GoodEffect.values().length)], completed);
                addEffect(GoodEffect.values()[random.nextInt(GoodEffect.values().length)], completed);
            }
        }

        public void randomizeItems(Player player) {
            itemStacks.clear();
            int completed = player.getCompletedQuestsVal();
            for (int i = 0; i < (Math.max(completed / 5, 1)); i++) {
                addItem(completed);
            }
        }

        public void randomizeAll(Player player) {
            randomizeExp(player);
            randomizeEffects(player);
            randomizeItems(player);
        }
    }

    public enum ItemRarity {
        COMMON,
        UNCOMMON,
        RARE,
        LEGENDARY,
        ILLEGAL
    }

    public ItemRarity getRarity(Material material) {
        if (material.isAir()) return ItemRarity.ILLEGAL;
        if (!material.isItem()) return ItemRarity.ILLEGAL;

        if (uno.contains(material)) return ItemRarity.ILLEGAL;
        if (unc.contains(material)) return ItemRarity.UNCOMMON;
        if (rar.contains(material)) return ItemRarity.RARE;
        if (leg.contains(material)) return ItemRarity.LEGENDARY;

        if (material.isEdible()) return ItemRarity.UNCOMMON;
        if (material.isRecord()) return ItemRarity.RARE;
        if (material.isInteractable()) {
            if (material == Material.ENDER_CHEST || material == Material.ENCHANTING_TABLE) {
                return ItemRarity.RARE;
            } else {
                return ItemRarity.UNCOMMON;
            }
        }
        String mS = material.toString();
        if (mS.contains("SPAWN_EGG")) return ItemRarity.ILLEGAL;
        if (mS.contains("NETHERITE")) return ItemRarity.LEGENDARY;
        if (mS.contains("DIAMOND")) return ItemRarity.RARE;
        if (mS.contains("IRON")) return ItemRarity.UNCOMMON;
        if (mS.contains("GOLD")) return ItemRarity.RARE;
        if (mS.contains("EMERALD")) return ItemRarity.UNCOMMON;
        if (mS.contains("GOAT")) return ItemRarity.UNCOMMON;
        if (mS.contains("PATTERN")) return ItemRarity.RARE;

        return ItemRarity.COMMON;
    }

    public Material[] uncommon = new Material[] {
            Material.COPPER_INGOT,
            Material.COPPER_ORE,
            Material.DEEPSLATE_COPPER_ORE,
            Material.GOLD_INGOT,
            Material.GOLD_ORE,
            Material.DEEPSLATE_GOLD_ORE,
            Material.IRON_INGOT,
            Material.IRON_ORE,
            Material.DEEPSLATE_IRON_ORE,
            Material.GOLD_ORE,
            Material.GOLD_INGOT,
            Material.DEEPSLATE_GOLD_ORE,
            Material.NETHER_GOLD_ORE,
            Material.EMERALD,
            Material.EMERALD_ORE,
            Material.SADDLE,
            Material.LEAD,
    };

    public Material[] rare = new Material[] {
            Material.COPPER_BLOCK,
            Material.RAW_GOLD_BLOCK,
            Material.RAW_IRON_BLOCK,
            Material.RAW_COPPER_BLOCK,
            Material.IRON_BLOCK,
            Material.GOLD_BLOCK,
            Material.EMERALD_BLOCK,
            Material.SPECTRAL_ARROW,
            Material.BOW,
            Material.TURTLE_EGG,
            Material.TURTLE_HELMET,
            Material.EXPERIENCE_BOTTLE,
            Material.BLAZE_POWDER,
            Material.BLAZE_ROD,
            Material.OBSIDIAN,
            Material.CRYING_OBSIDIAN,
            Material.GHAST_TEAR,
            Material.PHANTOM_MEMBRANE,
            Material.ENDER_PEARL,
            Material.ENDER_EYE
    };

    public Material[] legendary = new Material[] {
            Material.TRIDENT,
            Material.TOTEM_OF_UNDYING,
            Material.DIAMOND_BLOCK,
            Material.NETHERITE_BLOCK,
            Material.END_CRYSTAL,
            Material.DRAGON_BREATH,
            Material.LODESTONE,
            Material.RECOVERY_COMPASS,
            Material.RESPAWN_ANCHOR,
            Material.ELYTRA,
            Material.NETHER_STAR
    };

    public Material[] unobtainable = new Material[] {
            Material.BARRIER,
            Material.BEDROCK,
            Material.CHAIN_COMMAND_BLOCK,
            Material.COMMAND_BLOCK,
            Material.COMMAND_BLOCK_MINECART,
            Material.REPEATING_COMMAND_BLOCK,
            Material.DEBUG_STICK,
            Material.END_PORTAL,
            Material.NETHER_PORTAL,
            Material.END_PORTAL_FRAME,
            Material.FARMLAND,
            Material.DIRT_PATH,
            Material.ENCHANTED_BOOK,
            Material.POTION,
            Material.SPLASH_POTION,
            Material.LINGERING_POTION,
            Material.FILLED_MAP,
            Material.CREEPER_HEAD,
            Material.PLAYER_HEAD,
            Material.ZOMBIE_HEAD,
            Material.INFESTED_CHISELED_STONE_BRICKS,
            Material.INFESTED_COBBLESTONE,
            Material.INFESTED_CRACKED_STONE_BRICKS,
            Material.INFESTED_DEEPSLATE,
            Material.INFESTED_MOSSY_STONE_BRICKS,
            Material.INFESTED_STONE,
            Material.INFESTED_STONE_BRICKS,
            Material.FROSTED_ICE,
            Material.JIGSAW,
            Material.KNOWLEDGE_BOOK,
            Material.SPAWNER,
            Material.PETRIFIED_OAK_SLAB,
            Material.WRITTEN_BOOK,
            Material.REINFORCED_DEEPSLATE,
            Material.DRAGON_EGG
    };

    private final List<Material> uno = List.of(unobtainable);
    private final List<Material> unc = List.of(uncommon);
    private final List<Material> rar = List.of(rare);
    private final List<Material> leg = List.of(legendary);

    public static final Random random = new Random(System.nanoTime());
    static public int random(int start, int end) {
        return start + random.nextInt(end - start + 1);
    }

    private final HashMap<UUID, DailyRewards> dailyRewards = new HashMap<>();

    public enum GoodEffect {
        ABSORPTION(PotionEffectType.ABSORPTION),
        CONDUIT_POWER(PotionEffectType.CONDUIT_POWER),
        DAMAGE_RESISTANCE(PotionEffectType.DAMAGE_RESISTANCE),
        DOLPHINS_GRACE(PotionEffectType.DOLPHINS_GRACE),
        FAST_DIGGING(PotionEffectType.FAST_DIGGING),
        FIRE_RESISTANCE(PotionEffectType.FIRE_RESISTANCE),
        GLOWING(PotionEffectType.GLOWING),
        HEAL(PotionEffectType.HEAL),
        HEALTH_BOOST(PotionEffectType.HEALTH_BOOST),
        HERO_OF_THE_VILLAGE(PotionEffectType.HERO_OF_THE_VILLAGE),
        INCREASE_DAMAGE(PotionEffectType.INCREASE_DAMAGE),
        INVISIBILITY(PotionEffectType.INVISIBILITY),
        JUMP(PotionEffectType.JUMP),
        LEVITATION(PotionEffectType.LEVITATION),
        LUCK(PotionEffectType.LUCK),
        NIGHT_VISION(PotionEffectType.NIGHT_VISION),
        REGENERATION(PotionEffectType.REGENERATION),
        SATURATION(PotionEffectType.SATURATION),
        SLOW_FALLING(PotionEffectType.SLOW_FALLING),
        SPEED(PotionEffectType.SPEED),
        WATER_BREATHING(PotionEffectType.WATER_BREATHING);

        private final PotionEffectType type;

        GoodEffect(PotionEffectType type) {
            this.type = type;
        }

        public PotionEffectType getType() {
            return type;
        }
    }

    public void openDaily(org.bukkit.entity.Player player) {
        Gui gui = preloadDaily(player.getUniqueId());
        if (gui != null) gui.open(player);
    }

    private void addItemAction(Gui gui, Player vPlayer, org.bukkit.entity.Player player) {
        gui.addSlotAction(2, 2, event -> {
            double hours = (Util.timeTillMidnightInMs()) / 1000.0 / 60.0 / 60.0;
            if (vPlayer.getRerollCount() == 0) {
                player.sendMessage(Util.colorize("&cYou've already used all your rerolls!"));
                player.sendMessage(Util.colorize("&cGet more in &e" + Util.displayNextTime() + "&c."));
                Util.playWolfHowl(player);
            } else {
                vPlayer.decreaseReroll();
                dailyRewards.get(player.getUniqueId()).randomizeItems(vPlayer);
                updateAllItems(gui, vPlayer);
                Util.playAnvilHit(player);
                player.sendMessage(Util.colorize("&aYou rerolled your daily items! You have &6" + vPlayer.getRerollCount() + "&a rerolls left."));
            }
        });
    }

    private void addExpAction(Gui gui, Player vPlayer, org.bukkit.entity.Player player) {
        gui.addSlotAction(2, 4, event -> {
            double hours = (Util.timeTillMidnightInMs()) / 1000.0 / 60.0 / 60.0;
            if (vPlayer.getRerollCount() == 0) {
                player.sendMessage(Util.colorize("&cYou've already used all your rerolls!"));
                player.sendMessage(Util.colorize("&cGet more in &e" + Util.displayNextTime() + "&c."));
                player.getWorld().playSound(player, Sound.ENTITY_WOLF_HOWL, 1, 1);
            } else {
                vPlayer.decreaseReroll();
                dailyRewards.get(player.getUniqueId()).randomizeExp(vPlayer);
                updateAllItems(gui, vPlayer);
                Util.playAnvilHit(player);
                player.sendMessage(Util.colorize("&aYou rerolled your daily exp! You have &6" + vPlayer.getRerollCount() + "&a rerolls left."));
            }
        });
    }

    private void addPotionEffectAction(Gui gui, Player vPlayer, org.bukkit.entity.Player player) {
        gui.addSlotAction(2, 3, event -> {
            double hours = (Util.timeTillMidnightInMs()) / 1000.0 / 60.0 / 60.0;
            if (vPlayer.getRerollCount() == 0) {
                player.sendMessage(Util.colorize("&cYou've already used all your rerolls!"));
                player.sendMessage(Util.colorize("&cGet more in &e" + Util.displayNextTime() + "&c."));
                player.getWorld().playSound(player, Sound.ENTITY_WOLF_HOWL, 1, 1);
            } else {
                vPlayer.decreaseReroll();
                dailyRewards.get(player.getUniqueId()).randomizeEffects(vPlayer);
                updateAllItems(gui, vPlayer);
                Util.playAnvilHit(player);
                player.sendMessage(Util.colorize("&aYou rerolled your daily potion effects! You have &6" + vPlayer.getRerollCount() + "&a rerolls left."));
            }
        });
    }

    private void addAllRerollButton(Gui gui, Player vPlayer, org.bukkit.entity.Player player) {
        gui.addSlotAction(2, 7, event -> {
            double hours = (Util.timeTillMidnightInMs()) / 1000.0 / 60.0 / 60.0;
            if (vPlayer.getRerollCount() <= 1) {
                player.sendMessage(Util.colorize("&cYou dont have enough rerolls!"));
                player.sendMessage(Util.colorize("&cGet more in &e" + Util.displayNextTime() + "&c."));
                player.getWorld().playSound(player, Sound.ENTITY_WOLF_HOWL, 1, 1);
            } else {
                vPlayer.decreaseReroll();
                vPlayer.decreaseReroll();
                dailyRewards.get(player.getUniqueId()).randomizeAll(vPlayer);
                updateAllItems(gui, vPlayer);
                Util.playAnvilHit(player);
                player.sendMessage(Util.colorize("&aYou rerolled all options! You have &6" + vPlayer.getRerollCount() + "&a rerolls left."));
            }
        });
    }

    private void updateAllItems(Gui gui, Player vPlayer) {
        gui.updateItem(2,2, generateItemButton(vPlayer.getBind(), vPlayer.getRerollCount()));
        gui.updateItem(2,4, generateExpButton(vPlayer.getBind(), vPlayer.getRerollCount()));
        gui.updateItem(2,3, generatePotionEffectsButton(vPlayer.getBind(), vPlayer.getRerollCount()));
        gui.updateItem(2,7, generateAllRerollItem(vPlayer.getRerollCount()));
        gui.updateItem(2,8, generateClaimButton(vPlayer.getBind()));
    }

    private ItemStack generateAllRerollItem(int rerollsLeft) {
        ItemStack stack = new ItemStack(Material.ANVIL);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(Util.colorize("&eReroll All"));
        List<String> lore = new ArrayList<>();
        lore.add(Util.colorize("&7You may reroll all 3 loot"));
        lore.add(Util.colorize("&7options for the price of 2!"));
        lore.add(Util.colorize("&7"));
        lore.add(Util.colorize("&aYou have &a" + rerollsLeft + "&a rerolls left."));
        lore.add(Util.colorize("&eClick to reroll all!"));
        meta.setLore(lore);
        stack.setItemMeta(meta);
        return stack;
    }

    public Gui preloadDaily(UUID uuid) {
        org.bukkit.entity.Player player = Bukkit.getPlayer(uuid);
        if (player == null) return null;

        Player vPlayer = plugin.getRegisteredPlayers().get(uuid);

        if (vPlayer == null) {
            System.out.println("Error, player is not registered (" + player.getName() + ")");
            return null;
        }

        Gui gui = Gui.gui().title(Util.colorizeComponent("&9Daily Rewards")).rows(3).create();
        gui.getFiller().fillBetweenPoints(1, 1, 3,9, ItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE).name(Util.colorizeComponent("&8")).asGuiItem());

        setDefaultClickAction(gui);
        addClosedItem(gui);

        if (!dailyRewards.containsKey(uuid)) {
            dailyRewards.put(uuid, new DailyRewards(vPlayer));
        }

        int rerollCount = vPlayer.getRerollCount();

        if (!dailyRewards.get(uuid).isClaimed()) {
            ItemStack itemButton = generateItemButton(uuid, rerollCount);
            ItemStack potionEffectsButton = generatePotionEffectsButton(uuid, rerollCount);
            ItemStack expButton = generateExpButton(uuid, rerollCount);
            ItemStack addAllButton = generateAllRerollItem(rerollCount);
            ItemStack claimButton = generateClaimButton(uuid);

            gui.setItem(2, 2, ItemBuilder.from(itemButton).asGuiItem());
            addItemAction(gui, vPlayer, player);

            gui.setItem(2, 3, ItemBuilder.from(potionEffectsButton).asGuiItem());
            addPotionEffectAction(gui, vPlayer, player);

            gui.setItem(2,4, ItemBuilder.from(expButton).asGuiItem());
            addExpAction(gui, vPlayer, player);

            gui.setItem(2,7, ItemBuilder.from(addAllButton).asGuiItem());
            addAllRerollButton(gui, vPlayer, player);

            gui.setItem(2,8, ItemBuilder.from(claimButton).asGuiItem());
            addClaimAction(gui, vPlayer, player);
        } else {
            ItemStack stack = new ItemStack(Material.PAPER);
            ItemMeta meta = stack.getItemMeta();

            List<String> lore = new ArrayList<>();
            lore.add(Util.colorize("&aLoot claimed for today!"));
            lore.add(Util.colorize("&aCome back in " + formatTime(Util.timeTillMidnightInMs()) + "!"));
            meta.setDisplayName(Util.colorize("&bLoot already claimed"));
            meta.setLore(lore);
            stack.setItemMeta(meta);
            gui.setItem(2,5, ItemBuilder.from(stack).asGuiItem());
        }
        return gui;
    }

    public static String formatTime(long millis) {
        String time = "";

        long sec = millis / 1000;
        long min = sec / 60;
        long hour = min / 60;

        if (hour != 0) time = hour + " hour" + (hour == 1 ? "" : "s");
        if (min != 0) time = time + (time.equals("") ? "" : ", ") + (min - (hour * 60)) + " minute" + (min == 1 ? "" : "s");
        if (sec != 0) time = time + (time.equals("") ? "" : ", ") + (sec - (min * 60)) + " second" + (sec == 1 ? "" : "s");

        return time;
    }

    private void addClaimAction(Gui gui, Player vPlayer, org.bukkit.entity.Player player) {
        gui.addSlotAction(2,8, event -> {
            if (player.getInventory().firstEmpty() == -1) {
                player.sendMessage(Util.colorize("&cYour inventory is full!"));
                player.getWorld().playSound(player, Sound.ENTITY_VILLAGER_NO, 1, 1);
                return;
            }
            UUID uuid = player.getUniqueId();

            player.sendMessage(Util.colorize("&b&m---------------------------------"));
            player.sendMessage(Util.colorize("&9Daily Rewards:"));
            for (ItemStack stack : dailyRewards.get(uuid).getItemStacks().keySet()) {
                player.getInventory().addItem(stack);
                player.sendMessage(Util.colorize(getRewardString(stack, uuid)));
            }
            for (PotionEffect potionEffect : dailyRewards.get(uuid).getEffects()) {
                if (player.getPotionEffect(potionEffect.getType()) == null) {
                    player.addPotionEffect(potionEffect);
                    player.sendMessage(Util.colorize(getRewardString(potionEffect, uuid)));
                } else {
                    int existingAmplifier = Objects.requireNonNull(player.getPotionEffect(potionEffect.getType())).getAmplifier();
                    int duration = Objects.requireNonNull(player.getPotionEffect(potionEffect.getType())).getDuration();
                    int level = Math.max(existingAmplifier, potionEffect.getAmplifier());

                    player.removePotionEffect(potionEffect.getType());
                    player.addPotionEffect(new PotionEffect(potionEffect.getType(), potionEffect.getDuration() + duration, level));
                    player.sendMessage(Util.colorize(getRewardString(potionEffect, uuid) + "&8 (+" + formatTime((duration / 20) * 1000L) + ")" + (level > existingAmplifier ? "&8 (+" + (level - existingAmplifier) + " " + Util.toPlural("level", (level - existingAmplifier)) + ")" : "")));
                }
            }
            player.giveExpLevels(dailyRewards.get(uuid).getExpLevels());
            player.sendMessage(Util.colorize("&6 - &a" + dailyRewards.get(uuid).getExpLevels() + "&7 Exp Level(s)"));
            player.sendMessage(Util.colorize("&b&m---------------------------------"));
            dailyRewards.get(player.getUniqueId()).setClaimed(true);
            Util.playLevelupSound(player);

            gui.close(player);
        });
    }

    private ItemStack generateClaimButton(UUID uuid) {
        ItemStack claimButton = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = claimButton.getItemMeta();
        meta.setDisplayName(Util.colorize("&aClaim Daily"));

        List<String> lore = new ArrayList<>();
        lore.add(Util.colorize("&7Items:"));
        for (ItemStack stack : dailyRewards.get(uuid).getItemStacks().keySet()) {
            lore.add(getRewardString(stack, uuid));
        }
        lore.add(Util.colorize("&7"));
        lore.add(Util.colorize("&7Potion Effects:"));
        for (PotionEffect potionEffect : dailyRewards.get(uuid).getEffects()) {
            lore.add(getRewardString(potionEffect, uuid));
        }
        lore.add(Util.colorize("&7"));
        lore.add(Util.colorize("&7Exp Levels: &a" + dailyRewards.get(uuid).getExpLevels()));
        lore.add(Util.colorize("&7"));
        lore.add(Util.colorize("&eClick to claim!"));
        meta.setLore(lore);
        claimButton.setItemMeta(meta);
        return claimButton;
    }

    private String getRewardString(Object object, UUID uuid) {
        if (object instanceof PotionEffect potionEffect) {
            return Util.colorize("&5 - " + EffectColor.valueOf(potionEffect.getType().getName()).getColor() + EffectColor.valueOf(potionEffect.getType().getName()).getReadableName() + " " + RomanNumber.toRoman(potionEffect.getAmplifier() + 1) + "&a (" + formatTime((potionEffect.getDuration() / 20) * 1000L) + ")");
        } else if (object instanceof ItemStack stack) {
            return Util.colorize("&d - &ax" + stack.getAmount()  + " " + getColor(dailyRewards.get(uuid).getItemStacks().get(stack)) + stack.getType().toString().toLowerCase().replace("_", " ") + ".");
        }
        return "";
    }

    private ItemStack generateItemButton(UUID uuid, int rerolls) {
        ItemStack itemButton = new ItemStack(Material.CHEST);
        ItemMeta meta = itemButton.getItemMeta();
        meta.setDisplayName(Util.colorize("&bDaily Items"));

        List<String> lore = new ArrayList<>();
        lore.add(Util.colorize("&7Items:"));
        for (ItemStack stack : dailyRewards.get(uuid).getItemStacks().keySet()) {
            lore.add(getRewardString(stack, uuid));
        }
        lore.add(Util.colorize("&7"));
        lore.add(Util.colorize("&aYou have " + rerolls + "&a rerolls left."));
        lore.add(Util.colorize("&cClick to reroll items"));
        meta.setLore(lore);
        itemButton.setItemMeta(meta);
        return itemButton;
    }

    private ItemStack generatePotionEffectsButton(UUID uuid, int rerolls) {
        ItemStack potionEffectsButton = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) potionEffectsButton.getItemMeta();
        meta.setColor(Color.fromBGR(random(0, 255), random(0, 255), random(0, 255)));
        meta.setDisplayName(Util.colorize("&bDaily Potion Effects"));

        List<String> lore = new ArrayList<>();
        lore.add(Util.colorize("&7Potion Effects:"));
        for (PotionEffect potionEffect : dailyRewards.get(uuid).getEffects()) {
            lore.add(getRewardString(potionEffect, uuid));
        }
        lore.add(Util.colorize("&7"));
        lore.add(Util.colorize("&aYou have " + rerolls + "&a rerolls left."));
        lore.add(Util.colorize("&cClick to reroll potion effects"));
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        potionEffectsButton.setItemMeta(meta);
        return potionEffectsButton;
    }

    private ItemStack generateExpButton(UUID uuid, int rerolls) {
        ItemStack expButton = new ItemStack(Material.EXPERIENCE_BOTTLE);
        ItemMeta meta = expButton.getItemMeta();
        meta.setDisplayName(Util.colorize("&bDaily Exp"));

        List<String> lore = new ArrayList<>();
        lore.add(Util.colorize("&7Levels: &a" + dailyRewards.get(uuid).getExpLevels()));
        lore.add(Util.colorize("&7"));
        lore.add(Util.colorize("&aYou have " + rerolls + "&a rerolls left."));
        lore.add(Util.colorize("&cClick to reroll exp"));
        meta.setLore(lore);
        expButton.setItemMeta(meta);
        return expButton;
    }

    private String getColor(ItemRarity rarity) {
        return switch (rarity) {
            case LEGENDARY -> "&6";
            case RARE -> "&d";
            case UNCOMMON -> "&b";
            case COMMON -> "&a";
            case ILLEGAL -> "&4";
        };
    }

    public static void addClosedItem(Gui gui) {
        gui.setItem(3,5, ItemBuilder.from(Material.BARRIER).name(Util.colorizeComponent("&cClose")).asGuiItem(event -> {
            for (HumanEntity viewer : new ArrayList<>(event.getViewers())) {
                viewer.closeInventory();
                Util.playWoodenDoorClose(viewer);
            }
        }));
    }

    public static void setDefaultClickAction(Gui gui) {
        gui.setDefaultTopClickAction(event -> event.setCancelled(true));
        gui.setOutsideClickAction(event -> {
            for (HumanEntity viewer : new ArrayList<>(event.getViewers())) {
                viewer.closeInventory();
                viewer.getWorld().playSound(viewer, Sound.BLOCK_WOODEN_DOOR_CLOSE, 1, 1);
            }
        });
    }
}
