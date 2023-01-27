package me.prism3.logger.utils.enums;

public enum DiscordChannels {

    STAFF("DiscordManager.Staff.Channel-ID"),
    PLAYER_CHAT("DiscordManager.Player-Chat.Channel-ID"),
    PLAYER_COMMANDS("DiscordManager.Player-Commands.Channel-ID"),
    PLAYER_SIGN_TEXT("DiscordManager.Player-Sign-Text.Channel-ID"),
    PLAYER_JOIN("DiscordManager.Player-Join.Channel-ID"),
    PLAYER_LEAVE("DiscordManager.Player-Leave.Channel-ID"),
    PLAYER_KICK("DiscordManager.Player-Kick.Channel-ID"),
    PLAYER_DEATH("DiscordManager.Player-Death.Channel-ID"),
    PLAYER_TELEPORT("DiscordManager.Player-Teleport.Channel-ID"),
    PLAYER_LEVEL("DiscordManager.Player-Level.Channel-ID"),
    BLOCK_PLACE("DiscordManager.Block-Place.Channel-ID"),
    BLOCK_BREAK("DiscordManager.Block-Break.Channel-ID"),
    BUCKET_FILL("DiscordManager.Bucket-Fill.Channel-ID"),
    BUCKET_EMPTY("DiscordManager.Bucket-Empty.Channel-ID"),
    ANVIL("DiscordManager.Anvil.Channel-ID"),
    ITEM_PICKUP("DiscordManager.Item-Pickup.Channel-ID"),
    ITEM_DROP("DiscordManager.Item-Drop.Channel-ID"),
    ENCHANTING("DiscordManager.Enchanting.Channel-ID"),
    BOOK_EDITING("DiscordManager.Book-Editing.Channel-ID"),
    FURNACE("DiscordManager.Furnace.Channel-ID"),
    GAME_MODE("DiscordManager.Game-Mode.Channel-ID"),
    CRAFTING("DiscordManager.Craft.Channel-ID"),
    REGISTRATION("DiscordManager.Registration.Channel-ID"),
    PRIMED_TNT("DiscordManager.Primed-TNT.Channel-ID"),
    CHEST_INTERACTION("DiscordManager.Chest-Interaction.Channel-ID"),
    ENTITY_DEATH("DiscordManager.Entity-Death.Channel-ID"),
    ITEM_FRAME_PLACE("DiscordManager.Item-Frame-Place.Channel-ID"),
    ITEM_FRAME_BREAK("DiscordManager.Item-Frame-Break.Channel-ID"),
    ARMOR_STAND_PLACE("DiscordManager.ArmorStand-Place.Channel-ID"),
    ARMOR_STAND_BREAK("DiscordManager.ArmorStand-Break.Channel-ID"),
    ARMOR_STAND_INTERACTION("DiscordManager.ArmorStand-Interaction.Channel-ID"),
    LEVER_INTERACTION("DiscordManager.Lever-Interaction.Channel-ID"),
    SPAWN_EGG("DiscordManager.Spawn-Egg.Channel-ID"),
    END_CRYSTAL_PLACE("DiscordManager.EndCrystal-Place.Channel-ID"),
    END_CRYSTAL_BREAK("DiscordManager.EndCrystal-Break.Channel-ID"),

    // Server Side
    SERVER_START("DiscordManager.Server-Side.Start.Channel-ID"),
    SERVER_STOP("DiscordManager.Server-Side.Stop.Channel-ID"),
    CONSOLE("DiscordManager.Server-Side.Console-Commands.Channel-ID"),
    RAM("DiscordManager.Server-Side.RAM.Channel-ID"),
    TPS("DiscordManager.Server-Side.TPS.Channel-ID"),
    PORTAL_CREATION("DiscordManager.Server-Side.Portal-Creation.Channel-ID"),
    RCON("DiscordManager.Server-Side.RCON.Channel-ID"),
    COMMAND_BLOCK("DiscordManager.Server-Side.Command-Block.Channel-ID"),
    PLAYER_COUNT("DiscordManager.Server-Side.Player-Count.Channel-ID"),
    SERVER_ADDRESS("DiscordManager.Server-Side.Server-Address.Channel-ID"),
    ADVANCEMENT("DiscordManager.Server-Side.Advancement.Channel-ID"),

    // Extra Side
    AFK("DiscordManager.Extras.AFK.Channel-ID"),
    WRONG_PASSWORD("DiscordManager.Extras.Wrong-Password.Channel-ID"),
    VAULT("DiscordManager.Extras.Vault.Channel-ID"),
    LITE_BANS("DiscordManager.Extras.LiteBans.Channel-ID"),
    ADVANCED_BAN("DiscordManager.Extras.AdvancedBan.Channel-ID"),
    VIA_VERSION("DiscordManager.Extras.ViaVersion.Channel-ID"),
    WORLD_GUARD("DiscordManager.Extras.WorldGuard.Channel-ID"),

    // Version Exception Side
    WOOD_STRIPPING("DiscordManager.Version-Exceptions.Wood-Stripping.Channel-ID"),
    TOTEM_OF_UNDYING("DiscordManager.Version-Exceptions.Totem-of-Undying.Channel-ID"),

    // Custom Side
    MANUAL("DiscordManager.Custom.Manual.Channel-ID");

    private final String key;

    DiscordChannels(String key) { this.key = key; }

    public String getKey() { return this.key; }
}
