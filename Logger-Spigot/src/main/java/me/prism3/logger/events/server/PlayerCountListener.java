package me.prism3.logger.events.server;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.utils.enums.LogType;


/*
 * This class is responsible for checking the player count on the server
 */
public class PlayerCountListener implements Runnable {

    private final LoggerAPI plugin;

    public PlayerCountListener(LoggerAPI plugin) { this.plugin = plugin; }

    /**
     * This method is called every X minutes to check the player count on the server.
     * If the player count exceeds the configured threshold, it logs an event.
     */
    @Override
    public void run() {

        final int count = this.plugin.getServer().getOnlinePlayers().size();

        if (count <= this.plugin.getData().getPlayerCountToLog())
            return;

        this.plugin.getLoggerManager().logEvent(LogType.SERVER_PLAYER_COUNT, null, null);
    }
}
