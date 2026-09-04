package me.prism3.logger.events.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.managers.PermissionManager;
import me.prism3.logger.managers.PlayerManager;
import me.prism3.logger.utils.enums.LogType;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

import java.util.Map;


/**
 * Listens for player game mode changes and logs them if the new game mode matches the configured target.
 */
public class GameModeListener implements Listener {

    private final LoggerAPI plugin;
    private final GameMode target;  // only log if new mode == this

    public GameModeListener(final LoggerAPI plugin) {

        this.plugin = plugin;
        // read from config, default CREATIVE
        GameMode gm;

        try {
            gm = GameMode.valueOf(this.plugin.getData().getGameModeToLog().toUpperCase());
        } catch (final IllegalArgumentException ex) {
            gm = GameMode.CREATIVE;
        }

        this.target = gm;
    }

    @EventHandler
    public void onGameModeChange(final PlayerGameModeChangeEvent event) {

        if (event.isCancelled())
            return;

        final Player player = event.getPlayer();

        if (PermissionManager.isExempt(player))
            return;

        // only log if newGameMode == target
        if (event.getNewGameMode() != target)
            return;

        final Map<String, String> placeholders = PlayerManager.getPlayerManager(player).populatePlaceholders(player);
        placeholders.put("game-mode", event.getNewGameMode().name());

        this.plugin.getLoggerManager().logEvent(LogType.PLAYER_GAME_MODE, player, placeholders);
    }
}
