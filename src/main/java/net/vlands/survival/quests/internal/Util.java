package net.vlands.survival.quests.internal;

import com.google.common.base.Strings;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.vlands.survival.quests.Main;
import org.bukkit.ChatColor;

import java.math.BigInteger;
import java.time.Instant;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.TimeZone;

public class Util {
    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static Component colorizeComponent(String message) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(colorize(message));
    }

    public static String displayNextTime() {
        TemporalAccessor ta = Instant.ofEpochMilli(timeTillMidnightInMs());
        return DateTimeFormatter.ISO_TIME.format(ta);
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

        return Strings.repeat(colorPercent(percent * 100) + symbol, progressBars)
                + Strings.repeat("&8" + symbol, totalBars - progressBars);
    }
    public static String colorPercent(double percent) {
        return ((percent <= 25.0) ? "&c" : ((percent <= 50.0) ? "&6" : ((percent <= 75.0) ? "&e" : "&a")));
    }

    public static long timeTillMidnightInMs() {
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone(ZoneId.of("Europe/London")));
        c.add(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return (c.getTimeInMillis()-System.currentTimeMillis());
    }

    public static String vowelModify(String str) {
        return (str.startsWith("A") ||
                str.startsWith("E") ||
                str.startsWith("I") ||
                str.startsWith("O") ||
                str.startsWith("U") ||
                str.startsWith("a") ||
                str.startsWith("e") ||
                str.startsWith("i") ||
                str.startsWith("o") ||
                str.startsWith("u")) ? "an " + str : "a " + str;
    }

    public static void incrementQuest(Main plugin, PlayerQuest quest) {
        quest.addValue(1);
        quest.updateStatus(plugin, quest.getBind());
    }

    public static void incrementQuest(Main plugin, PlayerQuest quest, int amount) {
        quest.addValue(amount);
        quest.updateStatus(plugin, quest.getBind());
    }

    public static void setQuestVal(Main plugin, PlayerQuest quest, int val) {
        quest.setValue(val);
        quest.updateStatus(plugin, quest.getBind());
    }

    public static String toPlural(String str, int amount) {
        if (amount == 1) {
            return str;
        } else {
            if (str.endsWith("y")) return (str.substring(0, str.length() - 1) + "ies");
            if (str.endsWith("o") || str.endsWith("s")) return (str + "es");
            return str + "s";
        }
    }

}
