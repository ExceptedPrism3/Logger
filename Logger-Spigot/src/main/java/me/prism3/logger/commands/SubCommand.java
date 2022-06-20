package me.prism3.logger.commands;

import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.List;

public interface SubCommand {

    String getName();

    String getDescription();

    String getSyntax();

    void perform(Player player, String[] args) throws IOException;

    List<String> getSubCommandsArgs(Player player, String[] args);
}
