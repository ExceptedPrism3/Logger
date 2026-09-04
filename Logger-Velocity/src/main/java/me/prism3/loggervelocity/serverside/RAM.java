package me.prism3.loggervelocity.serverside;

import me.prism3.loggervelocity.Logger;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import static me.prism3.loggervelocity.utils.Data.*;

public class RAM implements Runnable {

    final Logger main = Logger.getInstance();

    public void run() {

        if (main.getConfig().getBoolean("Log-Server.RAM")) {

            if (ramPercent <= 0 || ramPercent >= 100) return;

            final long maxMemory = Runtime.getRuntime().maxMemory() / 1048576L;
            final long freeMemory = Runtime.getRuntime().freeMemory() / 1048576L;
            final long usedMemory = maxMemory - freeMemory;
            final double percentUsed = usedMemory * 100.0D / maxMemory;

            if (ramPercent <= percentUsed) {

                Map<String, String> placeholders = new HashMap<>();
                placeholders.put("time", dateTimeFormatter.format(ZonedDateTime.now()));
                placeholders.put("max", String.valueOf(maxMemory));
                placeholders.put("used", String.valueOf(usedMemory));
                placeholders.put("free", String.valueOf(freeMemory));

                main.getLogManager().logServerEvent("Server-Side.RAM", placeholders);
            }
        }
    }
}
