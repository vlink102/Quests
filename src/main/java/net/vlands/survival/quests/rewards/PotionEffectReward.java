package net.vlands.survival.quests.rewards;

import net.vlands.survival.quests.gui.DailyCreator;
import net.vlands.survival.quests.internal.RomanNumber;
import org.bukkit.potion.PotionEffectType;

public class PotionEffectReward {
    private final PotionEffectType type;
    private final int amplifier;
    private final int duration;

    public PotionEffectReward(PotionEffectType type, int amplifier, int duration) {
        this.type = type;
        this.amplifier = amplifier;
        this.duration = duration * 20;
    }

    public PotionEffectType getPotionType() {
        return type;
    }
    public int getAmplifier(boolean readable) {
        return (readable ? amplifier : amplifier - 1);
    }

    public int getDuration(boolean inSeconds) {
        return (inSeconds ? duration / 20 : duration);
    }

    public String getFormattedDuration() {
        return DailyCreator.formatTime((duration / 20) * 1000L);
    }

    public String getFormattedAmplifier() {
        return RomanNumber.toRoman(getAmplifier(true));
    }
}

