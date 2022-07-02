package me.prism3.logger.commands.subcommands;

import me.prism3.logger.commands.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

import static me.prism3.logger.utils.Data.discordSupportServer;

public class Discord implements SubCommand {

    @Override
    public String getName() { return "discord"; }

    @Override
    public String getDescription() { return "Display the discord support server link"; }

    @Override
    public String getSyntax() { return "/logger discord"; }

    @Override
    public void perform(Player player, String[] args) {

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "Discord Support Server: &b" + discordSupportServer));

    }

    @Override
    public List<String> getSubCommandsArgs(Player player, String[] args) {
        return Collections.emptyList();
    }
}
