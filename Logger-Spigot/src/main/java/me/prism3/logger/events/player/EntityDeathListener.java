package me.prism3.logger.events.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.managers.PermissionManager;
import me.prism3.logger.managers.PlayerManager;
import me.prism3.logger.utils.enums.LogType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Map;


/**
 * Listens for entity death events and logs them if the killer is a player and not exempt.
 * Only logs entities that are configured in the plugin's data.
 */
public class EntityDeathListener implements Listener {

    private final LoggerAPI plugin;

    public EntityDeathListener(final LoggerAPI plugin) { this.plugin = plugin; }

    /**
     * Handles entity death events.
     * Logs the event if the killer is a player and not exempt, and if the entity type is configured.
     *
     * @param event the EntityDeathEvent to handle
     */
    @EventHandler(ignoreCancelled = true)
    public void onEntityDeath(final EntityDeathEvent event) {

        final Player killer = event.getEntity().getKiller();

        // only player kills
        if (killer == null) return;

        if (PermissionManager.isExempt(killer))
            return;

        final String entityType = event.getEntityType().name();

        // only types configured
        if (!this.plugin.getData().getEntitiesToLog().contains(entityType))
            return;

        final Map<String,String> placeholders = PlayerManager.getPlayerManager(killer).populatePlaceholders(killer);

        placeholders.put("entity_type", entityType);
        placeholders.put("entity_x", String.valueOf(event.getEntity().getLocation().getBlockX()));
        placeholders.put("entity_y", String.valueOf(event.getEntity().getLocation().getBlockY()));
        placeholders.put("entity_z", String.valueOf(event.getEntity().getLocation().getBlockZ()));
        this.plugin.getLoggerManager().logEvent(LogType.PLAYER_ENTITY_DEATH, killer, placeholders);
    }
}
