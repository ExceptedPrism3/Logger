// me/prism3/logger/utils/enums/LogType.java
package me.prism3.logger.utils.enums;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * All supported log‑types, each knows its config path and whether it’s
 * player‑side vs server‑side.
 */
public enum LogType {

    // Staff
    STAFF("Staff", "Staff.Enabled", "Discord.Staff"),

    // Player events
    PLAYER_CHAT("Player Chat", "Log-Player.Chat", "Player.Chat", "Discord.Player-Chat"),
    PLAYER_COMMAND("Player Command", "Log-Player.Command", "Player.Command", "Discord.Player-Command"),
    PLAYER_COMMAND_WHITELISTED("Player Command Whitelisted", "Log-Player.Command", "Player.Command-Whitelisted",
            "Discord.Player-Command-Whitelisted"),
    PLAYER_SIGN_INTERACTION("Player Sign Interaction", "Log-Player.Sign-Interaction", "Player.Sign-Interaction",
            "Discord.Player-Sign-Text"),
    PLAYER_JOIN("Player Join", "Log-Player.Join", "Player.Join", "Discord.Player-Join"),
    PLAYER_LEAVE("Player Leave", "Log-Player.Leave", "Player.Leave", "Discord.Player-Leave"),
    PLAYER_KICK("Player Kick", "Log-Player.Kick", "Player.Kick", "Discord.Player-Kick"),
    PLAYER_DEATH("Player Death", "Log-Player.Death", "Player.Death", "Discord.Player-Death"),
    PLAYER_TELEPORT("Player Teleport", "Log-Player.Teleport", "Player.Teleport", "Discord.Player-Teleport"),
    PLAYER_LEVEL("Player Level", "Log-Player.Level", "Player.Level", "Discord.Player-Level"),
    PLAYER_BLOCK_PLACE("Player Block Place", "Log-Player.Block-Place", "Player.Block-Place", "Discord.Block-Place"),
    PLAYER_BLOCK_BREAK("Player Block Break", "Log-Player.Block-Break", "Player.Block-Break", "Discord.Block-Break"),
    PLAYER_BUCKET_FILL("Player Bucket Fill", "Log-Player.Bucket-Fill", "Player.Bucket-Fill", "Discord.Bucket-Fill"),
    PLAYER_BUCKET_EMPTY("Player Bucket Empty", "Log-Player.Bucket-Empty", "Player.Bucket-Empty",
            "Discord.Bucket-Empty"),
    PLAYER_PRIME_TNT("Player Prime TNT", "Log-Player.Prime-TNT", "Player.Prime-TNT", "Discord.Primed-TNT"),
    PLAYER_ANVIL_INTERACTION("Player Anvil Interaction", "Log-Player.Anvil-Interaction", "Player.Anvil-Interaction",
            "Discord.Anvil"),
    PLAYER_ITEM_PICKUP("Player Item Pickup", "Log-Player.Item-Pickup", "Player.Item-Pickup", "Discord.Item-Pickup"),
    PLAYER_ITEM_DROP("Player Item Drop", "Log-Player.Item-Drop", "Player.Item-Drop", "Discord.Item-Drop"),
    PLAYER_ITEM_ENCHANTING("Player Item Enchant", "Log-Player.Item-Enchant", "Player.Item-Enchant",
            "Discord.Enchanting"),
    PLAYER_BOOK_INTERACTION("Player Book Interaction", "Log-Player.Book-Interaction", "Player.Book-Interaction",
            "Discord.Book-Editing"),
    PLAYER_FURNACE_INTERACTION("Player Furnace Interaction", "Log-Player.Furnace-Interaction",
            "Player.Furnace-Interaction", "Discord.Furnace"),
    PLAYER_GAME_MODE("Player GameMode Change", "Log-Player.Game-Mode", "Player.Game-Mode", "Discord.Game-Mode"),
    PLAYER_ITEM_CRAFT("Player Item Craft", "Log-Player.Item-Craft", "Player.Item-Craft", "Discord.Craft"),
    PLAYER_ENTITY_DEATH("Player Entity Death", "Log-Player.Entity-Death", "Player.Entity-Death",
            "Discord.Entity-Death"),
    PLAYER_PLAYER_SPAWN_EGG("Player Spawn Egg", "Log-Player.Spawn-Egg", "Player.Spawn-Egg", "Discord.Spawn-Egg"),
    PLAYER_PORTAL_CREATION("Player Portal Creation", "Log-Player.Portal-Creation", "Player.Portal-Creation",
            "Discord.Portal-Creation"),
    PLAYER_ADVANCEMENTS("Player Advancement Unlock", "Log-Player.Advancement", "Player.Advancement",
            "Discord.Advancement"),
    PLAYER_CONTAINER_INTERACTION("Player Container Interaction", "Log-Player.Container-Interaction",
            "Player.Container-Interaction", "Discord.Chest-Interaction"),
    PLAYER_REGISTRATION("Player Registration", "Log-Player.Registration", "Player.Registration",
            "Discord.Registration"),
    PLAYER_TOTEM_OF_UNDYING("Player Totem of Undying", "Log-Player.Totem-of-Undying", "Player.Totem-of-Undying",
            "Discord.Version-Exceptions.Totem-of-Undying"),
    PLAYER_WOOD_STRIP("Player Wood Strip", "Log-Player.Wood-Strip", "Player.Wood-Stripping",
            "Discord.Version-Exceptions.Wood-Stripping"),
    PLAYER_LEVER_INTERACTION("Player Lever Interaction", "Log-Player.Lever-Interaction", "Player.Lever-Interaction",
            "Discord.Lever-Interaction"),
    PLAYER_VILLAGER_TRADE("Player Villager Trade", "Log-Player.Villager-Trade", "Player.Villager-Trade",
            "Discord.Villager-Trade"),
    PLAYER_PIGLIN_BARTER("Player Piglin Barter", "Log-Player.Piglin-Barter", "Player.Piglin-Barter",
            "Discord.Piglin-Barter"),
    PLAYER_RESPAWN_ANCHOR("Player Respawn Anchor", "Log-Player.Respawn-Anchor", "Player.Respawn-Anchor",
            "Discord.Respawn-Anchor"),
    PLAYER_CRAFTER_CRAFT("Player Crafter Craft", "Log-Player.Crafter-Craft", "Player.Crafter-Craft",
            "Discord.Crafter-Craft"),
    PLAYER_SCULK_SHRIEKER("Player Sculk Shrieker", "Log-Player.Sculk-Shrieker", "Player.Sculk-Shrieker",
            "Discord.Sculk-Shrieker"),

    // Server events
    SERVER_START("Server Start", "Log-Server.Start", "Server.Start", "Discord.Server-Side.Start"),
    SERVER_STOP("Server Stop", "Log-Server.Stop", "Server.Stop", "Discord.Server-Side.Stop"),
    SERVER_CONSOLE_COMMAND("Server Console Command", "Log-Server.Console-Command", "Server.Console-Command",
            "Discord.Server-Side.Console-Command"),
    SERVER_RAM("Server RAM", "Log-Server.RAM", "Server.RAM", "Discord.Server-Side.RAM"),
    SERVER_TPS("Server TPS", "Log-Server.TPS", "Server.TPS", "Discord.Server-Side.TPS"),
    SERVER_RCON_COMMAND("Server RCON Command", "Log-Server.RCON", "Server.RCON", "Discord.Server-Side.RCON"),
    SERVER_COMMAND_BLOCK("Server Command Block", "Log-Server.Command-Block", "Server.Command-Block",
            "Discord.Server-Side.Command-Block"),
    SERVER_PLAYER_COUNT("Server Player Count", "Log-Server.Player-Count", "Server.Player-Count",
            "Discord.Server-Side.Player-Count"),
    SERVER_MANUAL_LOG("Server Manual Log", "Log-Server.Manual-Log", "Server.Manual-Log", "Discord.Custom.Manual");

    private final String folderName;
    private final String configPath;
    private String messagePath;
    private final String discordPath;

    LogType(String folderName, String configPath, String messagePath, String discordPath) {
        this.folderName = folderName;
        this.configPath = configPath;
        this.messagePath = messagePath;
        this.discordPath = discordPath;
    }

    LogType(String folderName, String configPath, String discordPath) {
        this.folderName = folderName;
        this.configPath = configPath;
        this.discordPath = discordPath;
    }

    public String getDiscordPath() {
        return discordPath;
    }

    public String getFolderName() {
        return folderName;
    }

    public String getConfigPath() {
        return configPath;
    }

    public String getMessagePath(final boolean isStaff) {
        return isStaff ? messagePath + "-Staff" : messagePath; // Append "-Staff" dynamically
    }

    /**
     * True if this type’s enabled‑flag in config is on.
     */
    public boolean isEnabled(FileConfiguration cfg) {
        return cfg.getBoolean(configPath, false);
    }

    /** Is this a player‑side event? */
    public boolean isPlayerSide() {
        return name().startsWith("PLAYER_");
    }

    /** Is this a server‑side event? */
    public boolean isServerSide() {
        return name().startsWith("SERVER_");
    }
}
