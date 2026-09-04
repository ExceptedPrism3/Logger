package me.prism3.loggervelocity.serverside;

import me.prism3.loggervelocity.Logger;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import static me.prism3.loggervelocity.utils.Data.*;

public class Start {

    public void run() {

        final Logger main = Logger.getInstance();

        if (main.getConfig().getBoolean("Log-Server.Start")) {

            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("server", serverName);
            placeholders.put("time", dateTimeFormatter.format(ZonedDateTime.now()));

            main.getLogManager().logServerEvent("Server-Side.Start", placeholders);
        }

        if (isWhitelisted && isBlacklisted) {
            main.getSLF4JLogger().error("Enabling both Whitelist and Blacklist isn't supported. " +
                    "Please disable one of them to continue logging Player Commands");
        }
    }
}
