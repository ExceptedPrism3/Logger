package me.prism3.loggercore.database.utils;

import java.net.InetSocketAddress;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataBaseUtils {

    private DataBaseUtils() {}

    public static String formatInstant(Instant instant) {

        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withLocale(Locale.getDefault())
                        .withZone(ZoneId.systemDefault());

        return formatter.format(instant);
    }
    public static Long convertIpToLong(InetSocketAddress ip){
        if(ip == null) return (long) -1;
        return ((long) ip.getAddress().getAddress()[0] << 24) |
                ((long) ip.getAddress().getAddress()[1] << 16) |
                ((long) ip.getAddress().getAddress()[2] << 8) |
                ((long) ip.getAddress().getAddress()[3]);
    }
    public static String convertLongToIp(long ip) {
        return ((ip >> 24) & 0xFF) +
                "." + ((ip >> 16) & 0xFF) +
                "." + ((ip >> 8) & 0xFF) +
                "." + (ip & 0xFF);
    }
}
