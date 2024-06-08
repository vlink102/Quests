package net.vlands.survival.quests.internal;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public enum TimeUnit {
        BEST,
        SECONDS,
        MINUTES,
        HOURS,
        DAYS
    }

    public static String format(long unixTime) {
        String time = "";

        int seconds = (int) (unixTime / 1000) % 60;
        int minutes = (int) ((unixTime / (1000 * 60)) % 60);
        int hours = (int) ((unixTime / (1000 * 60 * 60)) % 24);
        int days = (int) ((unixTime / (1000 * 60 * 60 * 24)));

        if (days != 0) time = days + " day" + (days == 1 ? "" : "s");
        if (hours != 0) time = time + (time.isEmpty() ? "" : " ") + hours + " hour" + (hours == 1 ? "" : "s");
        if (minutes != 0) time = time + (time.isEmpty() ? "" : " ") + minutes + " minute" + (minutes == 1 ? "" : "s");
        if (seconds != 0) time = time + (time.isEmpty() ? "" : " ") + seconds + " second" + (seconds == 1 ? "" : "s");
        return time;
    }

    public static String toString(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static float nanosToSeconds(float nanos) {
        return (float) (Math.round((nanos / 1000000000) * 100.0) / 100.0);
    }

    public static float millisToSeconds(float millis) {
        return (float) (Math.round((millis / 1000) * 100.0) / 100.0);
    }

    public static String toString(long seconds, String format) {
        Date date = new Date(seconds);
        return toString(date, format);
    }

    public static int[] splitToHMS(BigDecimal biggy) {
        long longVal = biggy.longValue();
        int hours = (int) longVal / 3600;
        int remainder = (int) longVal - hours * 3600;
        int mins = remainder / 60;
        remainder = remainder - mins * 60;
        int secs = remainder;

        return new int[]{ hours, mins, secs };
    }

    public static int[] splitToMS(BigDecimal biggy) {
        long longVal = biggy.longValue();
        int mins = (int) (longVal / 60);
        int secs = (int) (longVal - (mins * 60));

        return new int[]{ mins, secs };
    }


}