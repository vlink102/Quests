package net.vlands.survival.quests.internal;

import org.bukkit.Effect;

public enum EffectColor {
    ABSORPTION("&e", "Absorption"),
    BAD_OMEN("&8", "Bad Omen"),
    BLINDNESS("&8", "Blindness"),
    CONDUIT_POWER("&3", "Conduit Power"),
    CONFUSION("&2", "Confusion"),
    DAMAGE_RESISTANCE("&7", "Resistance"),
    DARKNESS("&8", "Darkness"),
    DOLPHINS_GRACE("&b", "Dolphin's Grace"),
    FAST_DIGGING("&e", "Haste"),
    FIRE_RESISTANCE("&6", "Fire Resistance"),
    GLOWING("&e", "Glowing"),
    HARM("&4", "Instant Damage"),
    HEAL("&c", "Instant Health"),
    HEALTH_BOOST("&c", "Health Boost"),
    HERO_OF_THE_VILLAGE("&d", "Hero of the Village"),
    HUNGER("&2", "Hunger"),
    INCREASE_DAMAGE("&5", "Strength"),
    INVISIBILITY("&7", "Invisibility"),
    JUMP("&a", "Jump Boost"),
    LEVITATION("&7", "Levitation"),
    LUCK("&a", "Luck"),
    NIGHT_VISION("&9", "Night Vision"),
    POISON("&2", "Poison"),
    REGENERATION("&c", "Regeneration"),
    SATURATION("&6", "Saturation"),
    SLOW("&8", "Slowness"),
    SLOW_DIGGING("&8", "Mining Fatigue"),
    SLOW_FALLING("&b", "Slow Falling"),
    SPEED("&b", "Speed"),
    UNLUCK("&2", "Unlucky"),
    WATER_BREATHING("&3", "Water Breathing"),
    WEAKNESS("&8", "Weakness"),
    WITHER("&8", "Wither");

    private final String color;
    private final String readableName;

    EffectColor(String color, String readableName) {
        this.color = color;
        this.readableName =readableName;
    }

    public String getReadableName() {
        return readableName;
    }

    public String getColor() {
        return color;
    }
}
