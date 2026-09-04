package me.prism3.logger_core.discord;

import me.prism3.logger_core.objects.LogPlayer;

/**
 * Interface representing the Discord manager subsystem.
 * Implemented by LoggerDiscordAddon.
 */
public interface DiscordManager {

    void sendMessage(String type, String message);

    void sendMessage(String type, String message, LogPlayer player, String logType);

    void shutdown();

    void reload();

    boolean isEnabled();
}
