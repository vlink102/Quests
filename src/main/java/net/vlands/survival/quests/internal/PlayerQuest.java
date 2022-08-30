package net.vlands.survival.quests.internal;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerQuest {
    public enum Status {
        COMPLETED(Material.LIME_STAINED_GLASS_PANE),
        UNLOCKED(Material.YELLOW_STAINED_GLASS_PANE),
        HIDDEN(Material.RED_STAINED_GLASS_PANE),
        UNRELEASED(Material.BLACK_STAINED_GLASS_PANE);

        private final Material material;

        Status(Material material) {
            this.material = material;
        }

        public Material getMaterial() {
            return material;
        }
    }

    private final Quest quest;
    private final UUID bind;
    private Status status;
    private int value;
    private final int max;

    private List<Component> lore;

    public void setLore(List<Component> lore) {
        this.lore = lore;
    }

    public PlayerQuest(Quest quest, UUID bind, Status status, int value) {
        this.quest = quest;
        this.bind = bind;
        this.status = status;
        this.value = value;
        this.max = this.quest.getObjective().getMaxValue();
        setLore(generateQuestLore());
    }

    public Quest getQuest() {
        return quest;
    }
    public UUID getBind() {
        return bind;
    }
    public Status getStatus() {
        return status;
    }
    public int getValue() {
        return value;
    }
    public int getMax() {
        return max;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    public void setValue(int value) {
        this.value = value;
    }
    public void addValue(int value) {
        this.value += value;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        lore.forEach(component -> sb.append(((TextComponent) component).content()));
        return "{" + quest.toString() + ", " + bind + ", " + status.toString() + ", " + value + ", " + max + sb + "}";
    }

    public ItemStack toItemStack() {
        ItemStack quest = new ItemStack(getStatus().getMaterial());
        quest.setAmount(1);

        ItemMeta meta = quest.getItemMeta();
        meta.displayName(Util.colorizeComponent(this.quest.getPrettyName()));
        meta.lore(this.lore);

        quest.setItemMeta(meta);

        return quest;
    }

    private List<Component> generateQuestLore() {
        List<Component> lore = new ArrayList<>();
        Objective objective = getQuest().getObjective();
        lore.add(Util.colorizeComponent("&7" + Util.capitalize(objective.getType().toString().toLowerCase()) + "&a " + objective.getMaxValue() + "&7 " + Util.capitalize(objective.getRelative()[0].toString().replace("_", " ").toLowerCase())));
        lore.add(Util.colorizeComponent("&7"));

        lore.add(Util.colorizeComponent("&eRewards:"));
        for (Reward reward : this.getQuest().getRewards()) {
            switch (reward.getType()) {
                case EXP:
                    lore.add(Util.colorizeComponent("&b - &a" + reward.getNumber() + "&7 &7" + (reward.getExpType() == Reward.ExpType.LEVEL ? "Exp Level(s)" : "Exp")));
                    break;
                case ITEM:
                    lore.add(Util.colorizeComponent("&7 - &ax" + reward.getAmount() + "&7 &7" + Util.capitalize(reward.getItemStack().getType().toString().toLowerCase())));
                    break;
                case POTIONEFFECT:
                    lore.add(Util.colorizeComponent("&6 - " + EffectColor.valueOf(reward.getPotionEffect().getType().getName()).getColor() + EffectColor.valueOf(reward.getPotionEffect().getType().getName()).getReadableName() + " " + reward.getPotionEffect().getAmplifier() + " &7(&a" + reward.getPotionEffect().getDuration() + "&as&7)"));
                    break;
            }
        }

        lore.add(Util.colorizeComponent("&7"));
        double percent = Math.round(((double) this.value / (double) this.max) * 100);
        double roundedPercent = Math.round(percent * 10.0) / 10.0;

        lore.add(Util.colorizeComponent("&7Progress: " + Util.colorPercent(percent) + value + "&7/&a" + max + " &8(" + Util.colorPercent(percent) + roundedPercent + "%&8)"));
        lore.add(Util.colorizeComponent("&6[" + Util.getProgressBar(value, max, 50, '|') + "&6]"));

        return lore;
    }
}