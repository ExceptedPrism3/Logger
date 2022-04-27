package me.prism3.logger.Commands.SubCommands;

import me.prism3.logger.Commands.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

import static me.prism3.logger.Utils.Data.discordSupportServer;

public class DiscordCommand extends SubCommand {

    @Override
    public String getName() { return "discord"; }

    @Override
    public String getDescription() { return "Display the discord support server link"; }

    @Override
    public String getSyntax() { return "/logger discord"; }

    @Override
    public void perform(Player player, String[] args) {

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "Discord Support Server: " + discordSupportServer));

    }

    @Override
    public List<String> getSubCommandsArgs(Player player, String[] args) {
        return null;
    }
}
