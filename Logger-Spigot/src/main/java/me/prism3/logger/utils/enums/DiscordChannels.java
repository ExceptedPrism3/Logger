package me.prism3.logger.utils.enums;

public enum DiscordChannels {

    STAFF("Discord.Staff.Channel-ID"),
    PLAYER_CHAT("Discord.Player-Chat.Channel-ID"),
    PLAYER_COMMANDS("Discord.Player-Commands.Channel-ID"),
    PLAYER_SIGN_TEXT("Discord.Player-Sign-Text.Channel-ID"),
    PLAYER_JOIN("Discord.Player-Join.Channel-ID"),
    PLAYER_LEAVE("Discord.Player-Leave.Channel-ID"),
    PLAYER_KICK("Discord.Player-Kick.Channel-ID"),
    PLAYER_DEATH("Discord.Player-Death.Channel-ID"),
    PLAYER_TELEPORT("Discord.Player-Teleport.Channel-ID"),
    PLAYER_LEVEL("Discord.Player-Level.Channel-ID"),
    BLOCK_PLACE("Discord.Block-Place.Channel-ID"),
    BLOCK_BREAK("Discord.Block-Break.Channel-ID"),
    BUCKET_FILL("Discord.Bucket-Fill.Channel-ID"),
    BUCKET_EMPTY("Discord.Bucket-Empty.Channel-ID"),
    ANVIL("Discord.Anvil.Channel-ID"),
    ITEM_PICKUP("Discord.Item-Pickup.Channel-ID"),
    ITEM_DROP("Discord.Item-Drop.Channel-ID"),
    ENCHANTING("Discord.Enchanting.Channel-ID"),
    BOOK_EDITING("Discord.Book-Editing.Channel-ID"),
    FURNACE("Discord.Furnace.Channel-ID"),
    GAME_MODE("Discord.Game-Mode.Channel-ID"),
    CRAFTING("Discord.Craft.Channel-ID"),
    REGISTRATION("Discord.Registration.Channel-ID"),
    PRIMED_TNT("Discord.Primed-TNT.Channel-ID"),
    CHEST_INTERACTION("Discord.Chest-Interaction.Channel-ID"),
    ENTITY_DEATH("Discord.Entity-Death.Channel-ID"),
    ITEM_FRAME_PLACE("Discord.Item-Frame-Place.Channel-ID"),
    ITEM_FRAME_BREAK("Discord.Item-Frame-Break.Channel-ID"),
    ARMOR_STAND_PLACE("Discord.ArmorStand-Place.Channel-ID"),
    ARMOR_STAND_BREAK("Discord.ArmorStand-Break.Channel-ID"),
    ARMOR_STAND_INTERACTION("Discord.ArmorStand-Interaction.Channel-ID"),
    LEVER_INTERACTION("Discord.Lever-Interaction.Channel-ID"),
    SPAWN_EGG("Discord.Spawn-Egg.Channel-ID"),
    END_CRYSTAL_PLACE("Discord.EndCrystal-Place.Channel-ID"),
    END_CRYSTAL_BREAK("Discord.EndCrystal-Break.Channel-ID"),

    // Server Side
    SERVER_START("Discord.Server-Side.Start.Channel-ID"),
    SERVER_STOP("Discord.Server-Side.Stop.Channel-ID"),
    CONSOLE("Discord.Server-Side.Console-Commands.Channel-ID"),
    RAM("Discord.Server-Side.RAM.Channel-ID"),
    TPS("Discord.Server-Side.TPS.Channel-ID"),
    PORTAL_CREATION("Discord.Server-Side.Portal-Creation.Channel-ID"),
    RCON("Discord.Server-Side.RCON.Channel-ID"),
    COMMAND_BLOCK("Discord.Server-Side.Command-Block.Channel-ID"),
    PLAYER_COUNT("Discord.Server-Side.Player-Count.Channel-ID"),
    SERVER_ADDRESS("Discord.Server-Side.Server-Address.Channel-ID"),
    ADVANCEMENT("Discord.Server-Side.Advancement.Channel-ID"),

    // Extra Side
    AFK("Discord.Extras.AFK.Channel-ID"),
    WRONG_PASSWORD("Discord.Extras.Wrong-Password.Channel-ID"),
    VAULT("Discord.Extras.Vault.Channel-ID"),
    LITE_BANS("Discord.Extras.LiteBans.Channel-ID"),
    ADVANCED_BAN("Discord.Extras.AdvancedBan.Channel-ID"),
    VIA_VERSION("Discord.Extras.ViaVersion.Channel-ID"),
    WORLD_GUARD("Discord.Extras.WorldGuard.Channel-ID"),

    // Version Exception Side
    WOOD_STRIPPING("Discord.Version-Exceptions.Wood-Stripping.Channel-ID"),
    TOTEM_OF_UNDYING("Discord.Version-Exceptions.Totem-of-Undying.Channel-ID"),

    // Custom Side
    MANUAL("Discord.Custom.Manual.Channel-ID");

    private final String key;

    DiscordChannels(String key) { this.key = key; }

    public String getKey() { return this.key; }
}
