package me.prism3.loggervelocity.events.oncommands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.proxy.Player;
import me.prism3.loggervelocity.Logger;
import me.prism3.loggervelocity.serverside.Console;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static me.prism3.loggervelocity.utils.Data.*;

public class OnCommand {

    @Subscribe
    public void onCmd(final CommandExecuteEvent event) {

        final Logger main = Logger.getInstance();

        final CommandSource commandSource = event.getCommandSource();

        if (commandSource instanceof Player) {

            final Player player = (Player) commandSource;

            if (main.getConfig().getBoolean("Log-Player.Commands") && player.getCurrentServer().isPresent()) {

                if (isWhitelisted && isBlacklisted) return;

                if (player.hasPermission(loggerExempt)) return;

                final String command = event.getCommand().replace("\\", "\\\\");
                final List<String> commandParts = Arrays.asList(command.split("\\s+"));

                if (isBlacklisted) {
                    for (String list : commandsToBlock) {
                        if (commandParts.contains(list)) return;
                    }
                }

                // Whitelist Commands
                if (isWhitelisted) {
                    new OnCommandWhitelist().onWhitelistedCommand(event);
                    return;
                }

                final String server = player.getCurrentServer().get().getServerInfo().getName();

                Map<String, String> placeholders = new HashMap<>();
                placeholders.put("player", player.getUsername());
                placeholders.put("server", server);
                placeholders.put("command", command);
                placeholders.put("time", dateTimeFormatter.format(ZonedDateTime.now()));

                boolean isStaff = isStaffEnabled && player.hasPermission(loggerStaffLog);

                main.getLogManager().logPlayerEvent("Player-Command", player, placeholders, isStaff);
            }
        } else {
            new Console().onConsole(event);
        }
    }
}
