package me.prism3.logger_discord_addon.events;

import me.prism3.logger_discord_addon.utils.enums.DiscordChannels;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Event called when a message is sent to Discord.
 */
public class DiscordMessageEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final DiscordChannels channel;
    private final String message;
    private final boolean success;

    /**
     * Constructs a new DiscordMessageEvent.
     *
     * @param channel The Discord channel the message was sent to.
     * @param message The message content.
     * @param success Whether the message was sent successfully.
     */
    public DiscordMessageEvent(DiscordChannels channel, String message, boolean success) {
        this.channel = channel;
        this.message = message;
        this.success = success;
    }

    /**
     * Returns the Discord channel the message was sent to.
     *
     * @return The DiscordChannels enum value.
     */
    public DiscordChannels getChannel() {
        return this.channel;
    }

    /**
     * Returns the message content.
     *
     * @return The message string.
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Returns whether the message was sent successfully.
     *
     * @return True if the message was sent successfully, false otherwise.
     */
    public boolean isSuccess() {
        return this.success;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
} 