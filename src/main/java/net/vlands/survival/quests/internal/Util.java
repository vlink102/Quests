package net.vlands.survival.quests.internal;

import com.google.common.base.Strings;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;

public class Util {
    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    public static Component colorizeComponent(String message) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(colorize(message));
    }
    public static String capitalize(String str){
        String[] words = str.split("\\s");
        StringBuilder capitalizeWord= new StringBuilder();
        for(String w : words){
            String first = w.substring(0,1);
            String afterFirst = w.substring(1);
            capitalizeWord.append(first.toUpperCase()).append(afterFirst).append(" ");
        }
        return capitalizeWord.toString().trim();
    }
    public static String getProgressBar(int current, int max, int totalBars, char symbol) {
        float percent = (float) current / max;
        int progressBars = (int) (totalBars * percent);

        return Strings.repeat("" + colorPercent(percent) + symbol, progressBars)
                + Strings.repeat("" + "&8" + symbol, totalBars - progressBars);
    }
    public static String colorPercent(double percent) {
        return ((percent <= 25.0) ? "&c" : ((percent <= 50.0) ? "&e" : ((percent <= 75.0) ? "&e" : "&a")));
    }
}
