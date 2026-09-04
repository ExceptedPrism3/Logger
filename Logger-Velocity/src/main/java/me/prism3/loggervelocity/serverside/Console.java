package me.prism3.loggervelocity.serverside;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.proxy.Player;
import me.prism3.loggervelocity.Logger;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import static me.prism3.loggervelocity.utils.Data.*;

public class Console {

    @Subscribe
    public void onConsole(final CommandExecuteEvent event) {
        if (event.getCommandSource() instanceof Player) {
            return; // Ignore player commands here (handled by OnCommand)
        }

        final Logger main = Logger.getInstance();

        if (main.getConfig() != null && (main.getConfig().getBoolean("Log-Server.Server-Commands") || main.getConfig().getBoolean("Log-Server.Console-Commands"))) {

            final String command = event.getCommand().replace("\\", "\\\\");

            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("time", dateTimeFormatter.format(ZonedDateTime.now()));
            placeholders.put("command", command);
            placeholders.put("log", command);
            placeholders.put("server", serverName);

            main.getLogManager().logServerEvent("Server-Side.Server-Commands", placeholders);
        }
    }
}
