// me/prism3/logger/managers/EventManager.java
package me.prism3.logger.managers;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.events.player.*;
import me.prism3.logger.events.server.*;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.VersionUtil;
import me.prism3.logger.utils.enums.LogType;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import me.prism3.logger.utils.SchedulerAdapter;

/**
 * Registers all listeners according to Data#isEnabled(LogType).
 */
public final class EventManager {

    private EventManager() {
        /* static only */ }

    public static void registerEvents(final LoggerAPI plugin) {
        final Data data = plugin.getData();

        // --- Player‑side events ---
        if (data.isEnabled(LogType.PLAYER_CHAT))
            register(new ChatListener(plugin), plugin);
        if (data.isEnabled(LogType.PLAYER_COMMAND))
            register(new CommandListener(plugin), plugin);
        if (data.isEnabled(LogType.PLAYER_DEATH))
            register(new DeathListener(plugin), plugin);
        if (data.isEnabled(LogType.PLAYER_JOIN))
            register(new JoinListener(plugin), plugin);
        if (data.isEnabled(LogType.PLAYER_LEAVE))
            register(new LeaveListener(plugin), plugin);
        if (data.isEnabled(LogType.PLAYER_KICK))
            register(new KickListener(plugin), plugin);
        if (data.isEnabled(LogType.PLAYER_BLOCK_PLACE))
            register(new BlockPlaceListener(plugin), plugin);
        if (data.isEnabled(LogType.PLAYER_BLOCK_BREAK))
            register(new BlockBreakListener(plugin), plugin);
        if (data.isEnabled(LogType.PLAYER_BUCKET_FILL))
            register(new BucketFillListener(plugin), plugin);
        if (data.isEnabled(LogType.PLAYER_BUCKET_EMPTY))
            register(new BucketEmptyListener(plugin), plugin);
        if (data.isEnabled(LogType.PLAYER_ANVIL_INTERACTION))
            register(new AnvilInteractionListener(plugin), plugin);
        if (data.isEnabled(LogType.PLAYER_ITEM_PICKUP))
            register(new ItemPickupListener(plugin), plugin);
        if (data.isEnabled(LogType.PLAYER_ITEM_DROP))
            register(new ItemDropListener(plugin), plugin);
        if (data.isEnabled(LogType.PLAYER_ITEM_ENCHANTING))
            register(new ItemEnchantListener(plugin), plugin);
        if (data.isEnabled(LogType.PLAYER_BOOK_INTERACTION))
            register(new BookInteractionListener(plugin), plugin);
        if (data.isEnabled(LogType.PLAYER_FURNACE_INTERACTION))
            register(new FurnaceInteractionListener(plugin), plugin);
        if (data.isEnabled(LogType.PLAYER_GAME_MODE))
            register(new GameModeListener(plugin), plugin);
        if (data.isEnabled(LogType.PLAYER_ITEM_CRAFT))
            register(new ItemCraftListener(plugin), plugin);
        if (data.isEnabled(LogType.PLAYER_PRIME_TNT))
            register(new PrimeTNTListener(plugin), plugin);
        if (data.isEnabled(LogType.PLAYER_CONTAINER_INTERACTION))
            register(new ContainerInteractionListener(plugin), plugin);
        if (data.isEnabled(LogType.PLAYER_ENTITY_DEATH))
            register(new EntityDeathListener(plugin), plugin);
        if (data.isEnabled(LogType.PLAYER_LEVER_INTERACTION))
            register(new LeverInteractionListener(plugin), plugin);
        if (data.isEnabled(LogType.PLAYER_PLAYER_SPAWN_EGG))
            register(new SpawnEggListener(plugin), plugin);
        if (data.isEnabled(LogType.PLAYER_PORTAL_CREATION))
            register(new PortalCreationListener(plugin), plugin);
        if (data.isEnabled(LogType.PLAYER_ADVANCEMENTS))
            register(new AdvancementUnlockListener(plugin), plugin);
        if (VersionUtil.CURRENT.isLegacy() && data.isEnabled(LogType.PLAYER_TOTEM_OF_UNDYING))
            register(new TotemUseListener(plugin), plugin);
        if (VersionUtil.CURRENT.isModern() && data.isEnabled(LogType.PLAYER_WOOD_STRIP))
            register(new WoodStripListener(plugin), plugin);
        if (data.isEnabled(LogType.PLAYER_VILLAGER_TRADE)) {
            try {
                Class.forName("io.papermc.paper.event.player.PlayerTradeEvent");
                register(new VillagerTradeListener(plugin), plugin);
            } catch (ClassNotFoundException | NoClassDefFoundError e) {
                // plugin.getLogger().warning("VillagerTradeListener skipped: Paper PlayerTradeEvent not found.");
            }
        }

        if (data.isEnabled(LogType.PLAYER_RESPAWN_ANCHOR)) {
            try {
                Class.forName("org.bukkit.block.data.type.RespawnAnchor");
                register(new RespawnAnchorListener(plugin), plugin);
            } catch (ClassNotFoundException | NoClassDefFoundError e) {
                // Skipped on older versions
            }
        }

        if (data.isEnabled(LogType.PLAYER_PIGLIN_BARTER)) {
            try {
                Class.forName("org.bukkit.event.entity.PiglinBarterEvent");
                register(new PiglinBarterListener(plugin), plugin);
            } catch (ClassNotFoundException | NoClassDefFoundError e) {
                // Skipped on older versions
            }
        }

        // --- Server‑side events ---
        if (data.isEnabled(LogType.PLAYER_SIGN_INTERACTION))
            register(new SignInteractionListener(plugin), plugin);

        // --- Server‑side events ---
        // StopListener is handled in Logger#onDisable()

        // Delay server start event registration to allow Discord addon to initialize
        if (data.isEnabled(LogType.SERVER_START)) {
            SchedulerAdapter.runLater(plugin, () -> new StartListener(plugin), 100L);
        }

        if (data.isEnabled(LogType.SERVER_CONSOLE_COMMAND))
            register(new ConsoleCommandListener(plugin), plugin);

        if (data.isEnabled(LogType.SERVER_RAM))
            SchedulerAdapter.runTimer(plugin, new RAMListener(plugin), 100L, plugin.getData().getCheckerIntervalRAM());

        if (data.isEnabled(LogType.SERVER_TPS))
            SchedulerAdapter.runTimer(plugin, new TPSListener(plugin, plugin.getData().getCheckerIntervalTPS()), 100L, 1L);

        if (data.isEnabled(LogType.SERVER_RCON_COMMAND))
            register(new RconCommandListener(plugin), plugin);

        if (data.isEnabled(LogType.SERVER_COMMAND_BLOCK))
            register(new CommandBlockListener(plugin), plugin);

        if (data.isEnabled(LogType.SERVER_PLAYER_COUNT))
            SchedulerAdapter.runTimer(plugin, new PlayerCountListener(plugin), 100L,
                    plugin.getData().getCheckerIntervalPlayerCount());
    }

    private static void register(final Listener listener, final LoggerAPI plugin) {
        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }
}
