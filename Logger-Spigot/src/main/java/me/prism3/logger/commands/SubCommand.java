package me.prism3.logger.commands;

import org.bukkit.command.CommandSender;

import java.io.IOException;
import java.util.List;


/**
 * SubCommand interface defines the structure for all subcommands in the plugin.
 * Each subcommand must implement this interface to ensure consistency and functionality.
 */
public interface SubCommand {

    String getName();

    String getDescription();

    String getSyntax();

    void perform(final CommandSender commandSender, final String[] args) throws IOException;

    List<String> getSubCommandsArgs(final CommandSender commandSender, final String[] args);

    String getPermission();
}
