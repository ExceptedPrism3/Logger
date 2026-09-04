package me.prism3.logger.database;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.database.loggers.player.*;
import me.prism3.logger.database.loggers.server.*;
import me.prism3.logger.utils.enums.LogType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Factory class for creating and caching database loggers based on the specified LogType.
 * Reuses singleton instances to prevent GC pressure on high-traffic event logging.
 */
public class DatabaseLoggerFactory {

    private static final Map<LoggerAPI, Map<LogType, Object>> CACHE = new ConcurrentHashMap<>();

    /**
     * Retrieves or creates a cached logger instance based on the specified LogType.
     *
     * @param logType The type of log to create.
     * @param plugin  The LoggerAPI plugin instance.
     * @return An instance of the appropriate logger implementation.
     */
    public static Object createLogger(final LogType logType, final LoggerAPI plugin) {
        Map<LogType, Object> pluginLoggers = CACHE.computeIfAbsent(plugin, k -> new ConcurrentHashMap<>());
        return pluginLoggers.computeIfAbsent(logType, type -> instantiateLogger(type, plugin));
    }

    /**
     * Clears cached logger instances for a plugin during reload or shutdown.
     *
     * @param plugin The LoggerAPI plugin instance.
     */
    public static void clearCache(final LoggerAPI plugin) {
        CACHE.remove(plugin);
    }

    private static Object instantiateLogger(final LogType logType, final LoggerAPI plugin) {
        switch (logType) {
            case PLAYER_CHAT:
                return new ChatLogger(plugin);
            case PLAYER_COMMAND:
                return new CommandLogger(plugin);
            case PLAYER_DEATH:
                return new DeathLogger(plugin);
            case PLAYER_JOIN:
                return new JoinLogger(plugin);
            case PLAYER_ITEM_ENCHANTING:
                return new ItemEnchantLogger(plugin);
            case SERVER_CONSOLE_COMMAND:
                return new ConsoleCommandLogger(plugin);
            case PLAYER_BLOCK_PLACE:
                return new BlockPlaceLogger(plugin);
            case PLAYER_BLOCK_BREAK:
                return new BlockBreakLogger(plugin);
            case PLAYER_PRIME_TNT:
                return new PrimeTNTLogger(plugin);
            case PLAYER_ANVIL_INTERACTION:
                return new AnvilInteractionLogger(plugin);
            case SERVER_RAM:
                return new RAMLogger(plugin);
            case SERVER_START:
                return new StartLogger(plugin);
            case SERVER_STOP:
                return new StopLogger(plugin);
            case SERVER_TPS:
                return new TPSLogger(plugin);
            case PLAYER_PLAYER_SPAWN_EGG:
                return new SpawnEggLogger(plugin);
            case PLAYER_PORTAL_CREATION:
                return new PortalCreationLogger(plugin);
            case PLAYER_LEAVE:
                return new LeaveLogger(plugin);
            case PLAYER_ITEM_PICKUP:
                return new ItemPickupLogger(plugin);
            case PLAYER_ITEM_DROP:
                return new ItemDropLogger(plugin);
            case PLAYER_ADVANCEMENTS:
                return new AdvancementUnlockLogger(plugin);
            case SERVER_RCON_COMMAND:
                return new RconCommandLogger(plugin);
            case SERVER_COMMAND_BLOCK:
                return new CommandBlockLogger(plugin);
            case PLAYER_SIGN_INTERACTION:
                return new SignInteractionLogger(plugin);
            case PLAYER_TELEPORT:
                return new TeleportLogger(plugin);
            case PLAYER_CONTAINER_INTERACTION:
                return new ContainerInteractionLogger(plugin);
            case PLAYER_LEVEL:
                return new LevelLogger(plugin);
            case PLAYER_KICK:
                return new KickLogger(plugin);
            case PLAYER_BUCKET_EMPTY:
                return new BucketEmptyLogger(plugin);
            case PLAYER_BUCKET_FILL:
                return new BucketFillLogger(plugin);
            case PLAYER_FURNACE_INTERACTION:
                return new FurnaceInteractionLogger(plugin);
            case PLAYER_BOOK_INTERACTION:
                return new BookInteractionLogger(plugin);
            case PLAYER_GAME_MODE:
                return new GameModeLogger(plugin);
            case SERVER_PLAYER_COUNT:
                return new PlayerCountLogger(plugin);
            case PLAYER_ITEM_CRAFT:
                return new ItemCraftLogger(plugin);
            case PLAYER_REGISTRATION:
                return new RegistrationLogger(plugin);
            case PLAYER_TOTEM_OF_UNDYING:
                return new TotemUseLogger(plugin);
            case PLAYER_WOOD_STRIP:
                return new WoodStripLogger(plugin);
            case PLAYER_ENTITY_DEATH:
                return new EntityDeathLogger(plugin);
            case PLAYER_LEVER_INTERACTION:
                return new LeverInteractionLogger(plugin);
            case PLAYER_VILLAGER_TRADE:
                return new VillagerTradeLogger(plugin);
            case PLAYER_PIGLIN_BARTER:
                return new PiglinBarterLogger(plugin);
            case PLAYER_RESPAWN_ANCHOR:
                return new RespawnAnchorLogger(plugin);
            case PLAYER_CRAFTER_CRAFT:
                return new CrafterCraftLogger(plugin);
            case PLAYER_SCULK_SHRIEKER:
                return new SculkShriekerLogger(plugin);
            case SERVER_MANUAL_LOG:
                return new ManualLogLogger(plugin);

            default:
                throw new IllegalArgumentException("Unknown LogType: " + logType);
        }
    }
}
