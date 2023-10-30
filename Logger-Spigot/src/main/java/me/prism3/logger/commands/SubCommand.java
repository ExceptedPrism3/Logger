package me.prism3.logger.commands;

import org.bukkit.command.CommandSender;

import java.io.IOException;
import java.util.List;


public interface SubCommand {

    String getName();

    String getDescription();

    String getSyntax();

    void perform(final CommandSender commandSender, final String[] args) throws IOException;

    List<String> getSubCommandsArgs(final CommandSender commandSender, final String[] args);
}
