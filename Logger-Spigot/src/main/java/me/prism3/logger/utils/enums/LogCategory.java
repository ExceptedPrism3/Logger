package me.prism3.logger.utils.enums;


public enum LogCategory {

    PLAYER_CHAT("Player Chat"),
    PLAYER_COMMANDS("Player Commands"),
    PLAYER_SIGN_TEXT("Player Sign Text"),
    PLAYER_JOIN("Player Join"),
    PLAYER_LEAVE("Player Leave"),
    PLAYER_DEATH("Player Death"),
    PLAYER_TELEPORT("Player Teleport"),
    BLOCK_PLACE("Block Place"),
    BLOCK_BREAK("Block Break"),
    PLAYER_KICK("Player Kick"),
    PLAYER_LEVEL("Player Level"),
    BUCKET_FILL("Bucket Fill"),
    BUCKET_EMPTY("Bucket Empty"),
    ANVIL("Anvil"),
    STAFF("Staff"),
    ITEM_PICKUP("Item Pickup"),
    ITEM_DROP("Item Drop"),
    ENCHANTING("Enchanting"),
    BOOK_EDITING("Book Editing"),
    FURNACE("Furnace"),
    GAME_MODE("Game Mode"),
    CRAFTING("Crafting"),
    REGISTRATION("Registration"),
    PRIMED_TNT("Primed TNT"),
    CHEST_INTERACTION("Chest Interaction"),
    ENTITY_DEATH("Entity Death"),
    ITEM_FRAME_PLACE("ItemFrame Place"),
    ITEM_FRAME_BREAK("ItemFrame Break"),
    ARMOR_STAND_PLACE("ArmorStand Place"),
    ARMOR_STAND_BREAK("ArmorStand Break"),
    ARMOR_STAND_INTERACTION("ArmorStand Interaction"),
    END_CRYSTAL_PLACE("EndCrystal Place"),
    END_CRYSTAL_BREAK("EndCrystal Break"),
    LEVER_INTERACTION("Lever Interaction"),
    SPAWN_EGG("Spawn Egg"),
    SERVER_START("Server Start"),
    SERVER_STOP("Server Stop"),
    CONSOLE_COMMANDS("Console Commands"),
    RAM("RAM"),
    TPS("TPS"),
    PORTAL_CREATION("Portal Creation"),
    RCON("RCON"),
    COMMAND_BLOCK("Command Block"),
    PLAYER_COUNT("Player Count"),
    SERVER_ADDRESS("Server Address"),
    ADVANCEMENTS("Advancement"),
    AFK("AFK"),
    WRONG_PASSWORD("Wrong Password"),
    VAULT("Player Balance"),
    LITEBANS("LiteBans"),
    ADVANCEDBAN("AdvancedBan"),
    VIAVERSION("ViaVersion"),
    WORLDGUARD("World Guard"),
    WOOD_STRIPPING("Wood Stripping"),
    TOTEM_UNDYING("Totem of Undying"),
    MANUAL("Manual"),
    DATABASE("Database");

    private final String folderName;

    LogCategory(final String displayName) { this.folderName = displayName; }

    public String getFolderName() { return this.folderName; }
}
