/*
package com.carpour.logger.Commands.onCommands;

import com.carpour.logger.Commands.SubCommands;
import com.carpour.logger.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class CommandSpy extends SubCommands {

    List<UUID> players = new ArrayList<>();

    private final Main main = Main.getInstance();

    @Override
    public String getName() {
        return "cmdspy";
    }

    @Override
    public String getDescription() {
        return "Ability to view player's commands";
    }

    @Override
    public String getSyntax() {
        return "Logger cmdspy";
    }

    @Override
    public void perform(Player player, String[] args) {

        if (player.isOp() || player.hasPermission("logger.cmdspy")) {

            if (main.getConfig().getBoolean("Player-Commands.Commands-Spy.Enable")) {

                if (!players.contains(player.getUniqueId())) {

                    players.add(player.getUniqueId());

                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&7[&bLogger&7] &f| &eCommandSpy has been enabled"));

                    return;
                }

                players.remove(player.getUniqueId());
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&7[&bLogger&7] &f| &eCommandSpy has been disabled"));

            } else {

                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bLogger&7] &f| &cCommandSpy is disabled"));

            }
        }else{

            player.sendMessage(Objects.requireNonNull(main.getConfig().getString("Messages.No-Permission")).replaceAll("&", "§"));

        }
    }
}
*/
