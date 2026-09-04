package me.prism3.logger_discord_addon.utils.enums;

/**
 * Enum representing various Discord channels used for different events.
 * Each enum constant corresponds to a key in the configuration file ("discord.yml")
 * that holds the channel ID for the specific event.
 */
public enum DiscordChannels {

    // Staff
    STAFF("Discord.Staff.Channel-ID"),

    // Player events
    PLAYER_CHAT("Discord.Player-Chat.Channel-ID"),
    PLAYER_COMMAND("Discord.Player-Command.Channel-ID"),
    PLAYER_COMMAND_WHITELISTED("Discord.Player-Command-Whitelisted.Channel-ID"),
    PLAYER_SIGN_INTERACTION("Discord.Player-Sign-Text.Channel-ID"),
    PLAYER_JOIN("Discord.Player-Join.Channel-ID"),
    PLAYER_LEAVE("Discord.Player-Leave.Channel-ID"),
    PLAYER_KICK("Discord.Player-Kick.Channel-ID"),
    PLAYER_DEATH("Discord.Player-Death.Channel-ID"),
    PLAYER_TELEPORT("Discord.Player-Teleport.Channel-ID"),
    PLAYER_LEVEL("Discord.Player-Level.Channel-ID"),
    PLAYER_BLOCK_PLACE("Discord.Block-Place.Channel-ID"),
    PLAYER_BLOCK_BREAK("Discord.Block-Break.Channel-ID"),
    PLAYER_BUCKET_FILL("Discord.Bucket-Fill.Channel-ID"),
    PLAYER_BUCKET_EMPTY("Discord.Bucket-Empty.Channel-ID"),
    PLAYER_PRIME_TNT("Discord.Primed-TNT.Channel-ID"),
    PLAYER_ANVIL_INTERACTION("Discord.Anvil.Channel-ID"),
    PLAYER_ITEM_PICKUP("Discord.Item-Pickup.Channel-ID"),
    PLAYER_ITEM_DROP("Discord.Item-Drop.Channel-ID"),
    PLAYER_ITEM_ENCHANTING("Discord.Enchanting.Channel-ID"),
    PLAYER_BOOK_INTERACTION("Discord.Book-Editing.Channel-ID"),
    PLAYER_FURNACE_INTERACTION("Discord.Furnace.Channel-ID"),
    PLAYER_GAME_MODE("Discord.Game-Mode.Channel-ID"),
    PLAYER_ITEM_CRAFT("Discord.Craft.Channel-ID"),
    PLAYER_ENTITY_DEATH("Discord.Entity-Death.Channel-ID"),
    PLAYER_PLAYER_SPAWN_EGG("Discord.Spawn-Egg.Channel-ID"),
    PLAYER_PORTAL_CREATION("Discord.Portal-Creation.Channel-ID"),
    PLAYER_ADVANCEMENTS("Discord.Advancement.Channel-ID"),
    PLAYER_CONTAINER_INTERACTION("Discord.Chest-Interaction.Channel-ID"),
    PLAYER_REGISTRATION("Discord.Registration.Channel-ID"),
    PLAYER_TOTEM_OF_UNDYING("Discord.Version-Exceptions.Totem-of-Undying.Channel-ID"),
    PLAYER_WOOD_STRIP("Discord.Version-Exceptions.Wood-Stripping.Channel-ID"),
    PLAYER_LEVER_INTERACTION("Discord.Lever-Interaction.Channel-ID"),
    PLAYER_VILLAGER_TRADE("Discord.Villager-Trade.Channel-ID"),
    PLAYER_PIGLIN_BARTER("Discord.Piglin-Barter.Channel-ID"),
    PLAYER_RESPAWN_ANCHOR("Discord.Respawn-Anchor.Channel-ID"),
    PLAYER_CRAFTER_CRAFT("Discord.Crafter-Craft.Channel-ID"),
    PLAYER_SCULK_SHRIEKER("Discord.Sculk-Shrieker.Channel-ID"),

    // Server events
    SERVER_START("Discord.Server-Side.Start.Channel-ID"),
    SERVER_STOP("Discord.Server-Side.Stop.Channel-ID"),
    SERVER_CONSOLE_COMMAND("Discord.Server-Side.Console-Command.Channel-ID"),
    SERVER_RAM("Discord.Server-Side.RAM.Channel-ID"),
    SERVER_TPS("Discord.Server-Side.TPS.Channel-ID"),
    SERVER_RCON_COMMAND("Discord.Server-Side.RCON.Channel-ID"),
    SERVER_COMMAND_BLOCK("Discord.Server-Side.Command-Block.Channel-ID"),
    SERVER_PLAYER_COUNT("Discord.Server-Side.Player-Count.Channel-ID"),
    SERVER_MANUAL_LOG("Discord.Custom.Manual.Channel-ID");

    private final String key;

    /**
     * Constructor for the enum constants.
     *
     * @param key The key in the configuration file that holds the channel ID.
     */
    DiscordChannels(final String key) {
        this.key = key;
    }

    /**
     * Returns the key associated with this enum constant.
     *
     * @return The key in the configuration file that corresponds to this channel.
     */
    public String getKey() {
        return this.key;
    }
}
