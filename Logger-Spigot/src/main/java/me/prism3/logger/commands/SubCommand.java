package me.prism3.logger.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.List;

public interface SubCommand {

    String getName();

    String getDescription();

    String getSyntax();

    void perform(CommandSender commandSender, String[] args) throws IOException;

    List<String> getSubCommandsArgs(CommandSender commandSender, String[] args);
}
