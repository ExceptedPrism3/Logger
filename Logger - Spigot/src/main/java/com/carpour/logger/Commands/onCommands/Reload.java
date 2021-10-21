package com.carpour.logger.Commands.onCommands;

import com.carpour.logger.Discord.DiscordFile;
import com.carpour.logger.Main;
import com.carpour.logger.Commands.SubCommands;
import com.carpour.logger.Utils.Messages;
import org.bukkit.entity.Player;

import java.util.Objects;

public class Reload extends SubCommands {

    private final Main main = Main.getInstance();

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "Reload the plugin";
    }

    @Override
    public String getSyntax() {
        return "/logger reload";
    }

    @Override
    public void perform(Player player, String[] args) {

        if (player.hasPermission("logger.reload")){

            main.reloadConfig();
            DiscordFile.reload();
            player.sendMessage(Objects.requireNonNull(Messages.get().getString("General.Reload")).replaceAll("&", "§"));

        } else {

            player.sendMessage(Objects.requireNonNull(Messages.get().getString("General.No-Permission")).replaceAll("&", "§"));

        }
    }
}
