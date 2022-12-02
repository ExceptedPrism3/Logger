package me.prism3.loggercore.database.utils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateUtils {

    private DateUtils() {}

    public static String formatInstant(Instant instant) {

        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withLocale(Locale.getDefault())
                        .withZone(ZoneId.systemDefault());

        return formatter.format(instant);
    }
}
