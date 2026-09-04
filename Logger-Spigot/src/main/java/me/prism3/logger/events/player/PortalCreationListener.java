package me.prism3.logger.events.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.managers.PermissionManager;
import me.prism3.logger.managers.PlayerManager;
import me.prism3.logger.utils.enums.LogType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.PortalCreateEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Listens for player portal creation events and logs them.
 * <p>
 * This class implements the Listener interface and handles events related to portal creation.
 */
public class PortalCreationListener implements Listener {

    private final LoggerAPI plugin;

    public PortalCreationListener(final LoggerAPI plugin) {
        this.plugin = plugin;
    }

    // Time window (in milliseconds) to consider a player's interaction as related to a portal creation.
    private static final long INTERACTION_THRESHOLD_MS = 5000;
    // Use a ConcurrentHashMap to allow safe access from multiple threads.
    private final Map<Player, Long> playerInteractions = new ConcurrentHashMap<>();

    /**
     * Handles player interactions and logs them.
     * <p>
     * This method is called when a player interacts with a block or places a block.
     *
     * @param event The PlayerInteractEvent to handle.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteract(final PlayerInteractEvent event) {

        if (event.isCancelled())
            return;

        final Player player = event.getPlayer();

        this.playerInteractions.put(player, System.currentTimeMillis());
    }

    /**
     * Handles block placement events and logs them.
     * <p>
     * This method is called when a player places a block.
     *
     * @param event The BlockPlaceEvent to handle.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(final BlockPlaceEvent event) {

        if (event.isCancelled())
            return;

        final Player player = event.getPlayer();

        this.playerInteractions.put(player, System.currentTimeMillis());
    }

    /**
     * Handles portal creation events and logs them.
     * <p>
     * This method is called when a portal is created in the world.
     *
     * @param event The PortalCreateEvent to handle.
     */
    @EventHandler
    public void onPortalCreate(final PortalCreateEvent event) {

        if (event.isCancelled())
            return;

        // Capture the time at which the portal is created.
        final long portalCreationTime = System.currentTimeMillis();

        // Remove any interactions that are older than the threshold.
        this.playerInteractions.entrySet().removeIf(entry -> portalCreationTime - entry.getValue() > INTERACTION_THRESHOLD_MS);

        // Log any player's interaction that is within the threshold.
        this.playerInteractions.forEach((player, interactionTime) -> {

            // Check if the interaction time is within the threshold.
            if (Math.abs(interactionTime - portalCreationTime) < INTERACTION_THRESHOLD_MS) {

                if (PermissionManager.isExempt(player))
                    return;

                final Map<String, String> placeholders = PlayerManager.getPlayerManager(player).populatePlaceholders(player);

                this.plugin.getLoggerManager().logEvent(LogType.PLAYER_PORTAL_CREATION, player, placeholders);
            }
        });
    }
}
