package com.carpour.logger.Events.onCommands;

import com.carpour.logger.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import java.util.List;
import java.util.Objects;

public class onCommandSpy implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCmdSpy(PlayerCommandPreprocessEvent event) {

        Player player = event.getPlayer();

        //Stop Adding Message to Log if the Player has the correct Permissions
        if (player.hasPermission("logger.exempt")) return;

        //Command Spy
        if (main.getConfig().getBoolean("Log.Player-Commands") && main.getConfig().getBoolean("Player-Commands.Commands-Spy.Enable")) {

            List<String> blackListCommands = main.getConfig().getStringList("Player-Commands.Blacklist-Commands");

            for (String command : blackListCommands) {

                if (event.getMessage().split(" ")[0].replaceFirst("/", "").equalsIgnoreCase(command)) return;

            }

            for (Player players : Bukkit.getOnlinePlayers()) {

                if (players.hasPermission("logger.cmdspy")) {

                    players.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            Objects.requireNonNull(main.getConfig().getString("Player-Commands.Commands-Spy.Message")).
                                    replace("%player%", event.getPlayer().getName()).
                                    replace("%cmd%", event.getMessage())));

                }
            }
        }
    }
}
