package me.prism3.logger_discord_addon.api;

import me.prism3.logger_discord_addon.LoggerDiscordAddon;
import me.prism3.logger_discord_addon.utils.enums.DiscordChannels;

/**
 * API class for the Logger Discord Addon.
 * Provides methods for other plugins to interact with the Discord integration.
 */
public class LoggerDiscordAPI {
    private final LoggerDiscordAddon addon;

    /**
     * Constructs a new LoggerDiscordAPI instance.
     *
     * @param addon The LoggerDiscordAddon instance.
     */
    public LoggerDiscordAPI(LoggerDiscordAddon addon) {
        this.addon = addon;
    }

    /**
     * Sends a message to a specified Discord channel.
     *
     * @param channel The Discord channel to send the message to.
     * @param message The message content to send.
     * @return True if the message was sent successfully, false otherwise.
     */
    public boolean sendMessage(DiscordChannels channel, String message) {
        if (!this.addon.getDiscordManager().isEnabled()) {
            return false;
        }
        
        this.addon.getDiscordManager().sendMessage(channel, message);
        return true;
    }

    /**
     * Checks if the Discord integration is enabled.
     *
     * @return True if the Discord integration is enabled, false otherwise.
     */
    public boolean isEnabled() {
        return this.addon.getDiscordManager().isEnabled();
    }

    /**
     * Reloads the Discord configuration.
     */
    public void reload() {
        this.addon.reloadConfig();
    }
} 
