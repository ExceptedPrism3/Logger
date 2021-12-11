package com.carpour.logger.Discord;

import org.bukkit.configuration.file.FileConfiguration;
import org.carour.loggercore.discord.DiscordOptions;


public class SpigotDiscordOptions extends DiscordOptions {

    FileConfiguration fileConfiguration;

    public SpigotDiscordOptions(FileConfiguration fileConfiguration) {
        this.fileConfiguration = fileConfiguration;
        construct();
    }

    @Override
    protected DiscordOptions construct() {
        return construct(fileConfiguration);
    }

    private DiscordOptions construct(FileConfiguration fileConfiguration) {
        this.setStaffEnabled(fileConfiguration.getBoolean("Staff.Enabled"));
        this.setPlayerChatEnabled(fileConfiguration.getBoolean("Log.Player-Chat"));
        this.setPlayerCommandsEnabled(fileConfiguration.getBoolean("Log.Player-Commands"));
        this.setPlayerCommandsEnabled(fileConfiguration.getBoolean("Log.Player-Commands"));
        this.setConsoleCommandsEnabled(fileConfiguration.getBoolean("Log.Console-Commands"));
        this.setPlayerSignTextEnabled(fileConfiguration.getBoolean("Log.Player-Sign-Text"));
        this.setPlayerJoinEnabled(fileConfiguration.getBoolean("Log.Player-Join"));
        this.setPlayerLeaveEnabled(fileConfiguration.getBoolean("Log.Player-Leave"));
        this.setPlayerKickEnabled(fileConfiguration.getBoolean("Log.Player-Kick"));
        this.setPlayerDeathEnabled(fileConfiguration.getBoolean("Log.Player-Death"));
        this.setPlayerTeleportEnabled(fileConfiguration.getBoolean("Log.Player-Teleport"));
        this.setPlayerLevelEnabled(fileConfiguration.getBoolean("Log.Player-Level"));
        this.setBlockPlaceEnabled(fileConfiguration.getBoolean("Log.Block-Place"));
        this.setBlockBreakEnabled(fileConfiguration.getBoolean("Log.Block-Break"));
        this.setPortalCreateEnabled(fileConfiguration.getBoolean("Log.Portal-Creation"));
        this.setBucketPlaceEnabled(fileConfiguration.getBoolean("Log.Bucket-Place"));
        this.setAnvilUseEnabled(fileConfiguration.getBoolean("Log.Anvil"));
        this.setTpsEnabled(fileConfiguration.getBoolean("Log.TPS"));
        this.setRamEnabled(fileConfiguration.getBoolean("Log.RAM"));
        this.setServerStartEnabled(fileConfiguration.getBoolean("Log.Server-Start"));
        this.setServerStopEnabled(fileConfiguration.getBoolean("Log.Server-Stop"));
        this.setItemDropEnabled(fileConfiguration.getBoolean("Log.Item-Drop"));
        this.setEnchantEnabled(fileConfiguration.getBoolean("Log.Enchant"));
        this.setBookEditEnabled(fileConfiguration.getBoolean("Log.Book-Editing"));
        this.setAfkEnabled(fileConfiguration.getBoolean("Log.AFK"));
        this.setItemPickupEnabled(fileConfiguration.getBoolean("Log.Item-Pickup"));
        this.setFurnaceEnabled(fileConfiguration.getBoolean("Log.Furnace"));

        return this;
    }
}
