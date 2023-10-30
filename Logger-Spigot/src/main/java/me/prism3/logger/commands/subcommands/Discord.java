package me.prism3.logger.commands.subcommands;

import me.prism3.logger.commands.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

import static me.prism3.logger.utils.Data.discordSupportServer;
import static me.prism3.logger.utils.Data.pluginPrefix;


public class Discord implements SubCommand {

    @Override
    public String getName() { return "discord"; }

    @Override
    public String getDescription() { return "Display the discord support server link"; }

    @Override
    public String getSyntax() { return "/logger " + this.getName(); }

    @Override
    public void perform(final CommandSender commandSender, final String[] args) {

        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', pluginPrefix + "Discord Support Server: &b" + discordSupportServer));
    }

    @Override
    public List<String> getSubCommandsArgs(final CommandSender commandSender, final String[] args) { return Collections.emptyList(); }
}
