package com.carpour.logger.Commands;

import org.bukkit.entity.Player;

public abstract class SubCommands {

    public abstract String getName();

    public abstract String getDescription();

    public abstract String getSyntax();

    public abstract void perform(Player player, String[] args);

}
