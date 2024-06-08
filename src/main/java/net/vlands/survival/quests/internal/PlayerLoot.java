package net.vlands.survival.quests.internal;

import me.activated.core.plugin.AquaCoreAPI;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
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
import org.bukkit.Sound;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class PlayerLoot {
    public enum Status {
        HIDDEN(Material.BARRIER),
        COMPLETE(Material.CHEST_MINECART),
        CLAIMED(Material.MINECART);

        private final Material material;

        Status(Material material) {
            this.material = material;
        }

        public Material getMaterial() {
            return material;
        }
    }
    private Status status;
    private final Loot loot;
    private final UUID bind;

    private List<String> lore = new ArrayList<>();

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public void updateLore() {
        setLore(generateLootLore(bind));
    }

    public PlayerLoot(Loot loot, UUID bind, Status status) {
        this.loot = loot;
        this.bind = bind;
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Loot getLoot() {
        return loot;
    }

    public UUID getBind() {
        return bind;
    }

    public void updateStatus(Main plugin, UUID uuid) {
        net.vlands.survival.quests.internal.Player thisPlayer = plugin.getRegisteredPlayers().get(uuid);
        Player player = Bukkit.getPlayer(uuid);
        if (thisPlayer.claimedLoot.contains(this)) {
            status = Status.CLAIMED;
            return;
        }
        if (thisPlayer.completedLoot.contains(this)) {
            status = Status.COMPLETE;
            return;
        }
        if (thisPlayer.getCompletedQuestsVal() >= loot.getQuestsTillUnlocked()) {
            status = Status.COMPLETE;
            thisPlayer.addCompleteLoot(this);
            if (player != null) {
                if (player.isOnline()) {
                    player.sendMessage(Util.colorize("&d ◇ &bLoot Milestone Unlocked!"));
                    Util.playExperienceOrbSound(player);
                } else {
                    thisPlayer.addJoinLoot(this);
                }
            }
            return;
        }
        status = Status.HIDDEN;
    }

    public void claimRewards(Main plugin, Player player) {
        for (Object object : loot.getRewards()) {
            if (object instanceof ItemReward && player.getInventory().firstEmpty() == -1) {
                player.sendMessage(Util.colorize("&cYour inventory is full!"));
                player.getWorld().playSound(player, Sound.ENTITY_VILLAGER_NO, 1, 1);
                return;
            }
        }

        status = Status.CLAIMED;
        plugin.getRegisteredPlayers().get(player.getUniqueId()).addClaimedLoot(this);
        plugin.getRegisteredPlayers().get(player.getUniqueId()).removeCompletedLoot(this);
        updateStatus(plugin, player.getUniqueId());
        int rewardCount = loot.getRewards().length;

        player.sendMessage(Util.colorize("&6&m---------------------------------"));
        player.sendMessage(Util.colorize("&bLoot Chest Contents:"));
        if (rewardCount == 0) {
            player.sendMessage(Util.colorize("&6 - &8Nothing? Probably raided by admins."));
            player.getWorld().playSound(player, Sound.ENTITY_WOLF_HOWL, 1, 1);
        } else {
            for (Object object : loot.getRewards()) {
                if (object instanceof ItemReward itemReward) {
                    player.getInventory().addItem(itemReward.getItemStack());
                    player.sendMessage(Util.colorize(PlayerQuest.getRewardString(itemReward)));
                }
                if (object instanceof ExpReward expReward) {
                    switch (expReward.getExpType()) {
                        case SINGLE -> player.giveExp(expReward.getAmount());
                        case LEVEL -> player.giveExpLevels(expReward.getAmount());
                    }
                    player.sendMessage(Util.colorize(PlayerQuest.getRewardString(expReward)));
                }
                if (object instanceof PotionEffectReward potionEffectReward) {
                    if (player.getPotionEffect(potionEffectReward.getPotionType()) == null) {
                        player.addPotionEffect(new PotionEffect(potionEffectReward.getPotionType(), potionEffectReward.getDuration(false), potionEffectReward.getAmplifier(false)));
                        player.sendMessage(Util.colorize(PlayerQuest.getRewardString(potionEffectReward)));
                    } else {
                        int existingAmplifier = player.getPotionEffect(potionEffectReward.getPotionType()).getAmplifier();
                        int duration = Objects.requireNonNull(player.getPotionEffect(potionEffectReward.getPotionType())).getDuration();
                        int level = Math.max(existingAmplifier, potionEffectReward.getAmplifier(false));

                        player.removePotionEffect(potionEffectReward.getPotionType());
                        player.addPotionEffect(new PotionEffect(potionEffectReward.getPotionType(), (potionEffectReward.getDuration(false)) + duration, level));
                        player.sendMessage(Util.colorize(PlayerQuest.getRewardString(potionEffectReward) + "&8 (+" + potionEffectReward.getFormattedDuration() + ")" + (level > existingAmplifier ? "&8 (+" + (level - existingAmplifier) + " " + Util.toPlural("level", (level - existingAmplifier)) + ")" : "")));
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
                            LuckPerms perms = LuckPermsProvider.get();
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
            Util.playLevelupSound(player);
        }
        player.sendMessage(Util.colorize("&6&m---------------------------------"));
    }

    public ItemStack toItemStack(Main plugin) {
        updateStatus(plugin, getBind());
        ItemStack loot = new ItemStack(getStatus().getMaterial());
        loot.setAmount(1);
        ItemMeta meta = loot.getItemMeta();
        Objects.requireNonNull(meta).setDisplayName(Util.colorize("&6&lLoot Chest"));
        updateLore();
        meta.setLore(this.lore);
        if (status == Status.COMPLETE) {
            meta.addEnchant(Enchantment.LUCK, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        loot.setItemMeta(meta);
        return loot;
    }

    private List<String> generateLootLore(UUID bind) {
        List<String> lore = new ArrayList<>();
        if (status == Status.HIDDEN) {
            lore.add(Util.colorize("&8Loot Milestone!"));
        } else {
            lore.add(Util.colorize("&7You've reached a milestone!"));
        }
        lore.add(Util.colorize("&7"));
        lore.add(Util.colorize("&bContents:"));
        for (Object reward : this.getLoot().getRewards()) {
            if (status == Status.HIDDEN) {
                lore.add(Util.colorize("&7 - &8???"));
            } else {
                lore.add(Util.colorize(PlayerQuest.getRewardString(reward)));
            }
        }

        lore.add(Util.colorize("&7"));

        switch (status) {
            case HIDDEN -> lore.add(Util.colorize("&8Complete &a" + (loot.getQuestsTillUnlocked() - Main.getPlugin(Main.class).getRegisteredPlayers().get(bind).getCompletedQuestsVal()) + " &8more quests."));
            case COMPLETE -> lore.add(Util.colorize("&eClick to claim contents!"));
            case CLAIMED -> lore.add(Util.colorize("&aLoot claimed!"));
        }
        return lore;
    }
}
