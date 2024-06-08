package net.vlands.survival.quests.internal;

import lombok.Getter;
import lombok.Setter;
import me.activated.core.plugin.AquaCoreAPI;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.context.ContextSet;
import net.luckperms.api.context.DefaultContextKeys;
import net.luckperms.api.model.data.DataMutateResult;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.vlands.survival.quests.Main;
import net.vlands.survival.quests.rewards.ExpReward;
import net.vlands.survival.quests.rewards.ItemReward;
import net.vlands.survival.quests.rewards.PermissionReward;
import net.vlands.survival.quests.rewards.PotionEffectReward;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;

import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAmount;
import java.util.*;

public class PlayerQuest {
    @Getter
    public enum Status {
        COMPLETED(Material.LIME_STAINED_GLASS_PANE),
        CLAIMED(Material.GREEN_STAINED_GLASS_PANE),
        VIEWABLE(Material.GRAY_STAINED_GLASS_PANE),
        HIDDEN(Material.RED_STAINED_GLASS_PANE),
        UNRELEASED(Material.BLACK_STAINED_GLASS_PANE),
        IN_PROGRESS(Material.YELLOW_STAINED_GLASS_PANE);

        private final Material material;

        Status(Material material) {
            this.material = material;
        }

    }

    @Getter
    private final Quest quest;
    @Setter
    @Getter
    private Status status;
    @Getter
    private int value;
    @Getter
    private final UUID bind;
    private String description;
    
    private List<String> lore = new ArrayList<>();

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public void updateStatus(Main plugin, UUID uuid) {
        net.vlands.survival.quests.internal.Player thisPlayer = plugin.getRegisteredPlayers().get(uuid);
        Player player = Bukkit.getPlayer(bind);
        if (thisPlayer.claimedQuests.contains(this)) {
            status = Status.CLAIMED;
            return;
        }
        if (thisPlayer.completedQuests.contains(this)) {
            status = Status.COMPLETED;
            return;
        }
        if (value >= quest.getObjective().getMaxValue()) {
            status = Status.COMPLETED;
            thisPlayer.addCompletedQuest(this);
            if (player != null) {
                if (player.isOnline()) {
                    player.sendMessage(Util.colorize("&b ◇ &aQuest Complete: &8'" + description + "&8'&a."));
                    player.getWorld().playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                } else {
                    thisPlayer.addJoinQuest(this);
                }
                updateHidden(plugin, thisPlayer);
            }
            return;
        }
        if (thisPlayer.getCompletedQuestsVal() >= quest.getUnlockCount()) {
            if (status == Status.HIDDEN) {
                status = Status.VIEWABLE;
                generateQuestLore(bind);
                if (player != null) {
                    if (player.isOnline()) {
                        player.sendMessage(Util.colorize("&d ◇ &bQuest Unlocked: &8'" + description + "&8'&a."));
                        if (quest.getUnlockCount() != 0) {
                            player.getWorld().playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                        }
                    } else {
                        thisPlayer.addJoinQuest(this);
                    }
                }
            } else {
                if (thisPlayer.getActiveQuests().contains(this)) {
                    //int maxValue = this.quest.getObjective().getMaxValue();
                    status = (value >= 0 ? Status.IN_PROGRESS : Status.VIEWABLE);
                } else {
                    plugin.log("&4Player has value on hidden quest, setting to viewable");
                    status = Status.VIEWABLE;
                }
            }
        } else {
            status = Status.HIDDEN;
        }
    }

    public static void updateHidden(Main plugin, net.vlands.survival.quests.internal.Player thisPlayer) {
        for (PlayerQuest playerQuest : thisPlayer.getQuestList()) {
            if (playerQuest.getStatus() == Status.HIDDEN) {
                playerQuest.updateStatus(plugin, playerQuest.getBind());
            }
        }
        for (PlayerLoot playerLoot : thisPlayer.getLootList()) {
            if (playerLoot.getStatus() == PlayerLoot.Status.HIDDEN) {
                playerLoot.updateStatus(plugin, playerLoot.getBind());
            }
        }
    }

    public void claimRewards(Main plugin, Player player) {
        for (Object object : quest.getRewards()) {
            if (object instanceof ItemReward && player.getInventory().firstEmpty() == -1) {
                player.sendMessage(Util.colorize("&cYour inventory is full!"));
                player.getWorld().playSound(player, Sound.ENTITY_VILLAGER_NO, 1, 1);
                return;
            }
        }

        status = PlayerQuest.Status.CLAIMED;
        plugin.getRegisteredPlayers().get(player.getUniqueId()).addClaimedQuest(this);
        plugin.getRegisteredPlayers().get(player.getUniqueId()).removeCompletedQuest(this);
        updateStatus(plugin, player.getUniqueId());
        int rewardCount = quest.getRewards().length;

        player.sendMessage(Util.colorize("&9&m---------------------------------"));
        player.sendMessage(Util.colorize("&aQuest Rewards:"));
        if (rewardCount == 0) {
            player.sendMessage(Util.colorize("&6 - &dBragging rights"));
            player.getWorld().playSound(player, Sound.ENTITY_WOLF_HOWL, 1, 1);
        } else {
            for (Object object : quest.getRewards()) {
                if (object instanceof ItemReward itemReward) {
                    player.getInventory().addItem(itemReward.getItemStack());
                    player.sendMessage(Util.colorize(getRewardString(itemReward)));
                }
                if (object instanceof ExpReward expReward) {
                    switch (expReward.getExpType()) {
                        case SINGLE -> player.giveExp(expReward.getAmount());
                        case LEVEL -> player.giveExpLevels(expReward.getAmount());
                    }
                    player.sendMessage(Util.colorize(getRewardString(expReward)));
                }
                if (object instanceof PotionEffectReward potionEffectReward) {
                    if (player.getPotionEffect(potionEffectReward.getPotionType()) == null) {
                        player.addPotionEffect(new PotionEffect(potionEffectReward.getPotionType(), potionEffectReward.getDuration(false), potionEffectReward.getAmplifier(false)));
                        player.sendMessage(Util.colorize(getRewardString(potionEffectReward)));
                    } else {
                        int existingAmplifier = player.getPotionEffect(potionEffectReward.getPotionType()).getAmplifier();
                        int duration = Objects.requireNonNull(player.getPotionEffect(potionEffectReward.getPotionType())).getDuration();
                        int level = Math.max(existingAmplifier, potionEffectReward.getAmplifier(false));

                        player.removePotionEffect(potionEffectReward.getPotionType());
                        player.addPotionEffect(new PotionEffect(potionEffectReward.getPotionType(), potionEffectReward.getDuration(false) + duration, level));
                        player.sendMessage(Util.colorize(getRewardString(potionEffectReward) + "&8 (+" + potionEffectReward.getFormattedDuration() + ")" + (level > existingAmplifier ? "&8 (+" + (level - existingAmplifier) + " " + Util.toPlural("level", (level - existingAmplifier)) + ")" : "")));
                    }
                }
                if (object instanceof PermissionReward permissionReward) {
                    if ((!plugin.isLuckPerms() && !plugin.isAquaCore())) {
                        plugin.log("&4No permissions manager found, ignoring...");
                    } else {
                        if (plugin.isAquaCore()) {
                            ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                            String command = permissionReward.getCommand().replace("%player%", player.getName());
                            Bukkit.dispatchCommand(console, command);
                            if (AquaCoreAPI.INSTANCE.getGlobalPlayer(player.getUniqueId()).hasPermission(permissionReward.getPermission())) {
                                player.sendMessage(Util.colorize(PlayerQuest.getRewardString(permissionReward)));
                                player.getWorld().playSound(player, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, 1, 1);
                            } else {
                                player.sendMessage(Util.colorize("&cSadly this reward will not work on this server."));
                                player.getWorld().playSound(player, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE_FAR, 1, 1);
                            }
                        } else if (plugin.isLuckPerms()) {
                            LuckPerms perms = plugin.getLuckPermsInstance();
                            User user = perms.getPlayerAdapter(Player.class).getUser(player);
                            Node node = Node.builder(permissionReward.getPermission())
                                    .withContext(DefaultContextKeys.SERVER_KEY, Main.SERVER_ID)
                                    .build();
                            DataMutateResult result = user.data().add(node);
                            perms.getUserManager().saveUser(user);
                            if (result.wasSuccessful()) {
                                plugin.log("&aSuccessfully applied permission '" + permissionReward.getPermission() + "' to " + player.getName() + "!");
                            } else {
                                plugin.log("&4Failed to apply permission '" + permissionReward.getPermission() + "' to " + player.getName());
                            }
                        }
                    }
                }
            }
            player.getWorld().playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
        }
        player.sendMessage(Util.colorize("&9&m---------------------------------"));
    }

    public PlayerQuest(Quest quest, Status status, int value, UUID bind) {
        this.quest = quest;
        this.status = status;
        this.value = value;
        this.bind = bind;
    }

    public void updateLore() {
        setLore(generateQuestLore(bind));
    }

    public void setValue(Integer value) {
        this.value = (value == null ? this.value : value);
    }
    public void addValue(int value) {
        this.value += value;
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(",");
        lore.forEach(joiner::add);
        return "{" + quest.toString() + ", " + status.toString() + ", " + value + ", {" + joiner + "}}";
    }

    public ItemStack toItemStack(Main plugin) {
        updateStatus(plugin, getBind());
        ItemStack quest = new ItemStack(getStatus().getMaterial());
        quest.setAmount(1);
        ItemMeta meta = quest.getItemMeta();
        Objects.requireNonNull(meta).setDisplayName(Util.colorize(this.quest.getPrettyName()));
        updateLore();
        meta.setLore(lore);
        if (status == Status.COMPLETED) {
            meta.addEnchant(Enchantment.LUCK, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        quest.setItemMeta(meta);
        return quest;
    }

    public static String updateDescription(Objective objective, boolean travelType, boolean dieType) {
        if (travelType || dieType || objective.getType() == Objective.Type.KILL || objective.getType() == Objective.Type.MINE || objective.getType() == Objective.Type.PLACE) {
            if (travelType) return (Util.colorize("&7" + ((Objective.TravelType) objective.getRelative()[0]).getPrettyName() + "&a " + objective.getMaxValue() + "&7 blocks."));
            if (dieType) {
                if (objective.getRelative()[0] == Objective.DieType.ENTITY_ATTACK) {
                    if (objective.getRelative().length == 1) {
                        return (Util.colorize("&7" + ((Objective.DieType) objective.getRelative()[0]).getPrettyName().split("###")[0] + " " + Util.vowelModify(Util.capitalize("entity")) + "&a " + objective.getMaxValue() + "&7 " + Util.toPlural("time", objective.getMaxValue()) + "."));
                    } else if (objective.getRelative().length == 2 && objective.getRelative()[1] instanceof EntityType type) {
                        return (Util.colorize("&7" + ((Objective.DieType) objective.getRelative()[0]).getPrettyName().split("###")[0] + " " + Util.vowelModify(Util.capitalize(type.toString().toLowerCase().replace("_", " "))) + "&a " + objective.getMaxValue() + "&7 " + Util.toPlural("time", objective.getMaxValue()) + "."));
                    }
                } else if (objective.getRelative()[0] == Objective.DieType.PROJECTILE) {
                    if (objective.getRelative().length == 3 && objective.getRelative()[1] instanceof Objective.ProjectileType type && objective.getRelative()[2] instanceof EntityType shooter) {
                        String[] stuff = ((Objective.DieType) objective.getRelative()[0]).getPrettyName().split("###");
                        return (Util.colorize("&7" + stuff[0] + " " + Util.vowelModify(type.getType().toString().toLowerCase().replace("_", " ")) + " " + stuff[2] + " " + Util.vowelModify(Util.capitalize(shooter.toString().toLowerCase().replace("_", " "))) + "&a " + objective.getMaxValue() + "&7 " + Util.toPlural("time", objective.getMaxValue()) + "."));
                    } else if (objective.getRelative().length == 2 && objective.getRelative()[1] instanceof Objective.ProjectileType type) {
                        String[] stuff = ((Objective.DieType) objective.getRelative()[0]).getPrettyName().split("###");
                        return (Util.colorize("&7" + stuff[0] + " " + Util.vowelModify(type.getType().toString().toLowerCase().replace("_", " ")) + "&a " + objective.getMaxValue() + "&7 " + Util.toPlural("time", objective.getMaxValue()) + "."));
                    } else if (objective.getRelative().length == 1) {
                        return (Util.colorize("&7" + ((Objective.DieType) objective.getRelative()[0]).getPrettyName().split("###")[0] + " a projectile &a" + objective.getMaxValue() + "&7 " + Util.toPlural("time", objective.getMaxValue()) + "."));
                    }
                } else if (objective.getRelative()[0] == Objective.DieType.CONTACT) {
                    if (objective.getRelative().length == 2 && objective.getRelative()[1] instanceof Objective.ContactType type) {
                        return (Util.colorize("&7" + ((Objective.DieType) objective.getRelative()[0]).getPrettyName().split("###")[0] + " " + Util.vowelModify(type.getMaterial().toString().toLowerCase().replace("_", " ")) + "&a " + objective.getMaxValue() + "&7 " + Util.toPlural("time", objective.getMaxValue()) + "."));
                    } else if (objective.getRelative().length == 1) {
                        return (Util.colorize("&7" + ((Objective.DieType) objective.getRelative()[0]).getPrettyName().split("###")[0] + " a block &a" + objective.getMaxValue() + "&7 " + Util.toPlural("time", objective.getMaxValue()) + "."));
                    }
                } else {
                    return (Util.colorize("&7" + ((Objective.DieType) objective.getRelative()[0]).getPrettyName() + "&a " + objective.getMaxValue() + "&7 " + Util.toPlural("time", objective.getMaxValue()) + "."));
                }
            }
            if (objective.getType() == Objective.Type.KILL) {
                if (objective.getRelative().length >= 1) {
                    if (objective.getRelative().length >= 2) {
                        if (objective.getRelative().length >= 3) {
                            if (objective.getRelative()[0] == EntityType.PLAYER && objective.getRelative()[1] instanceof Material material && objective.getRelative()[2] instanceof Objective.KillPlayerType killPlayerType) {
                                switch (killPlayerType) {
                                    case SINGLE -> {
                                        return Util.colorize("&7Kill &a" + objective.getMaxValue() + "&7 " + Util.toPlural("player", objective.getMaxValue()) + " with " + Util.vowelModify(material.toString().toLowerCase().replace("_", " ")) + ".");
                                    }
                                    case DIFFERENT -> {
                                        return Util.colorize("&7Kill &a" + objective.getMaxValue() + "&7 different " + Util.toPlural("player", objective.getMaxValue()) + " with " + Util.vowelModify(material.toString().toLowerCase().replace("_", " ")) + ".");
                                    }
                                }
                            }
                        } else if (objective.getRelative().length == 2) {
                            if (objective.getRelative()[0] instanceof EntityType entityType && objective.getRelative()[1] instanceof Material material) {
                                if (entityType == EntityType.PLAYER) {
                                    return Util.colorize("&7Kill &a" + objective.getMaxValue() + "&7 " + Util.toPlural("player", objective.getMaxValue()) + " with " + Util.vowelModify(material.toString().toLowerCase().replace("_", " ")) + ".");
                                } else {
                                    return Util.colorize("&7Kill &a" + objective.getMaxValue() + "&7 " + Util.toPlural(entityType.toString().toLowerCase().replace("_", " "), objective.getMaxValue()) + " with " + Util.vowelModify(material.toString().toLowerCase().replace("_", " ")) + ".");
                                }
                            } else if (objective.getRelative()[0] == EntityType.PLAYER && objective.getRelative()[1] instanceof Objective.KillPlayerType killPlayerType) {
                                switch (killPlayerType) {
                                    case DIFFERENT -> {
                                        return Util.colorize("&7Kill &a" + objective.getMaxValue() + "&7 different " + Util.toPlural("player", objective.getMaxValue()) + ".");
                                    }
                                    case SINGLE -> {
                                        return Util.colorize("&7Kill &a" + objective.getMaxValue() + "&7 " + Util.toPlural("player", objective.getMaxValue()) + ".");
                                    }
                                }
                            }
                        }
                    } else if (objective.getRelative().length == 1) {
                        if (objective.getRelative()[0] instanceof EntityType entityType) {
                            return Util.colorize("&7Kill &a" + objective.getMaxValue() + "&7 " + Util.toPlural(entityType.toString().toLowerCase().replace("_", " "), objective.getMaxValue()) + ".");
                        } else if (objective.getRelative()[0] instanceof Material material) {
                            return Util.colorize("&7Kill &a" + objective.getMaxValue() + "&7 " + Util.toPlural("entity", objective.getMaxValue()) + " with " + Util.vowelModify(material.toString().toLowerCase().replace("_", " ")) + ".");
                        }
                    }
                } else if (objective.getRelative().length == 0) {
                    return Util.colorize("&7Kill &a" + objective.getMaxValue() + "&7 " + Util.toPlural("entity", objective.getMaxValue()) + ".");
                }
            }
            if (objective.getType() == Objective.Type.PLACE) {
                if (objective.getRelative().length == 0) {
                    return Util.colorize("&7Place &a" + objective.getMaxValue() + "&7 " + Util.toPlural("block", objective.getMaxValue()));
                } else if (objective.getRelative().length == 1) {
                    if (objective.getRelative()[0] instanceof Material material) {
                        return Util.colorize("&7Place &a" + objective.getMaxValue() + "&7 " + Util.toPlural(material.toString().toLowerCase().replace("_", " "), objective.getMaxValue()));
                    }
                } else if (objective.getRelative().length == 2) {
                    if (objective.getRelative()[0] instanceof Material material && objective.getRelative()[1] instanceof Material material1) {
                        return Util.colorize("&7Place &a" + objective.getMaxValue() + "&7 " + Util.toPlural(material.toString().toLowerCase().replace("_", " "), objective.getMaxValue()) + " on " + Util.vowelModify(material1.toString().toLowerCase().replace("_", " ")));
                    }
                }
            }
            if (objective.getType() == Objective.Type.MINE) {
                if (objective.getRelative().length == 0) {
                    return Util.colorize("&7Mine &a" + objective.getMaxValue() + "&7 " + Util.toPlural("block", objective.getMaxValue()) + ".");
                } else if (objective.getRelative().length == 1) {
                    if (objective.getRelative()[0] instanceof Material material) {
                        return Util.colorize("&7Mine &a" + objective.getMaxValue() + "&7 " + Util.toPlural(material.toString().toLowerCase().replace("_", " "), objective.getMaxValue()));
                    } else if (objective.getRelative()[1] instanceof ItemStack itemStack) {
                        return Util.colorize("&7Mine &a" + objective.getMaxValue() + "&7 " + Util.toPlural("block", objective.getMaxValue()) + " with " + Util.vowelModify(itemStack.getType().toString().toLowerCase().replace("_", " ")));
                    }
                } else if (objective.getRelative().length == 2) {
                    if (objective.getRelative()[0] instanceof ItemStack itemStack && objective.getRelative()[1] instanceof Material material) {
                        return Util.colorize("&7Mine &a" + objective.getMaxValue() + "&7 " + Util.toPlural(material.toString().toLowerCase().replace("_", " "), objective.getMaxValue()) + " with " + Util.vowelModify(itemStack.getType().toString().toLowerCase().replace("_", " ")));
                    }
                }
            }
        } else {
            return (Util.colorize("&7" + Util.capitalize(objective.getType().toString().toLowerCase()) + "&a " + objective.getMaxValue() + "&7 " + Util.capitalize(objective.getRelative()[0].toString().replace("_", " ").toLowerCase())));
        }

        return Util.colorize("&4Please report this (bug)!");
    }

    public static String getRewardString(Object reward) {
        if (reward instanceof ExpReward) {
            return (Util.colorize("&6 - &a" + ((ExpReward) reward).getAmount() + "&7 &7" + (((ExpReward) reward).getExpType() == ExpReward.Type.LEVEL ? "Exp Level(s)" : "Exp")));
        } else if (reward instanceof ItemReward itemReward) {
            if (itemReward.getItemStack().hasItemMeta() && itemReward.getItemStack().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(Main.getPlugin(Main.class), "vlands"), PersistentDataType.INTEGER)) {
                return Util.colorize("&d - &ax" + itemReward.getAmount() + "&7 &7" + itemReward.getItemStack().getItemMeta().getDisplayName());
            } else {
                return (Util.colorize("&6 - &ax" + itemReward.getAmount() + "&7 &7" + Util.capitalize(itemReward.getItemStack().getType().toString().toLowerCase())));
            }
        } else if (reward instanceof PotionEffectReward) {
            return (Util.colorize("&6 - " + EffectColor.valueOf(((PotionEffectReward)reward).getPotionType().getName()).getColor() + EffectColor.valueOf(((PotionEffectReward)reward).getPotionType().getName()).getReadableName() + " " + ((PotionEffectReward)reward).getFormattedAmplifier() + " &7(&a" + ((PotionEffectReward)reward).getFormattedDuration() + "&as&7)"));
        } else if (reward instanceof PermissionReward permissionReward) {
            return Main.getPlugin(Main.class).isAquaCore() ? Util.colorize("&b - " + permissionReward.getDescription()) : Util.colorize("&b - &4Error, reward unavailable.");
        }
        return null;
    }

    private List<String> generateQuestLore(UUID bind) {
        List<String> lore = new ArrayList<>();
        Objective objective = getQuest().getObjective();

        boolean travelType = objective.getRelative().length != 0 && objective.getRelative()[0] instanceof Objective.TravelType;
        boolean dieType = objective.getRelative().length != 0 && objective.getRelative()[0] instanceof Objective.DieType;

        if (status == Status.HIDDEN) {
            lore.add(Util.colorize("&8?????"));
        } else {
            description = updateDescription(objective, travelType, dieType);
            lore.add(description);
        }

        lore.add(Util.colorize("&7"));

        lore.add(Util.colorize("&7Rewards:"));
        if (quest.getRewards().length == 0) {
            lore.add(Util.colorize("&6 - &7No Rewards :("));
        } else {
            for (Object reward : quest.getRewards()) {
                if (status == Status.HIDDEN) {
                    lore.add(Util.colorize("&6 - &8???"));
                } else {
                    lore.add(getRewardString(reward));
                }
            }
        }

        int max = quest.getObjective().getMaxValue();

        lore.add(Util.colorize("&7"));
        double percent = ((float) value / max) * 100.0;
        double roundedPercent = (double) Math.round(percent * 100.0) / 100.0;

        switch (status) {
            case HIDDEN -> lore.add(Util.colorize("&8Complete &a" + (quest.getUnlockCount() - Main.getPlugin(Main.class).getRegisteredPlayers().get(bind).getCompletedQuestsVal()) + " &8more quests."));
            case VIEWABLE -> {
                lore.add(Util.colorize("&7Progress: " + Util.colorPercent(percent) + value + "&7/&a" + max + " &8(" + Util.colorPercent(percent) + roundedPercent + "%&8)"));
                lore.add(Util.colorize("&6[" + Util.getProgressBar(value, max, 50, '|') + "&6]"));
            }
            case COMPLETED -> lore.add(Util.colorize("&eClick to claim rewards!"));
            case CLAIMED -> lore.add(Util.colorize("&aQuest complete!"));
        }

        return lore;
    }
}