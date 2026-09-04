package me.prism3.loggervelocity.events.oncommands;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.proxy.Player;
import me.prism3.loggervelocity.Logger;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static me.prism3.loggervelocity.utils.Data.*;

public class OnCommandWhitelist {

    @Subscribe
    public void onWhitelistedCommand(final CommandExecuteEvent event) {

        final Logger main = Logger.getInstance();
        final Player player = (Player) event.getCommandSource();

        final String command = event.getCommand().replace("\\", "\\\\");
        final String server = player.getCurrentServer().get().getServerInfo().getName();
        final List<String> commandParts = Arrays.asList(command.split("\\s+"));

        for (String m : commandsToLog) {

            if (commandParts.contains(m)) {

                Map<String, String> placeholders = new HashMap<>();
                placeholders.put("player", player.getUsername());
                placeholders.put("server", server);
                placeholders.put("command", command);
                placeholders.put("time", dateTimeFormatter.format(ZonedDateTime.now()));

                boolean isStaff = isStaffEnabled && player.hasPermission(loggerStaffLog);

                main.getLogManager().logPlayerEvent("Player-Command", player, placeholders, isStaff);
                return;
            }
        }
    }
}
