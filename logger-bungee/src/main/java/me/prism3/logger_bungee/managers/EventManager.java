package me.prism3.logger_bungee.managers;

import me.prism3.logger_bungee.LoggerBungee;
import me.prism3.logger_bungee.listeners.player.*;
import me.prism3.logger_bungee.listeners.server.ConsoleCommandListener;
import me.prism3.logger_bungee.listeners.server.ServerCommandInterceptor;
import me.prism3.logger_bungee.listeners.server.ServerReloadListener;
import me.prism3.logger_bungee.utils.Constants;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;

import java.util.HashMap;
import java.util.Map;

public class EventManager {

    private final LoggerBungee plugin;
    private final Map<Constants.Events, Listener> registeredListeners;
    private ConsoleCommandListener consoleCommandListener;
    private ServerCommandInterceptor serverCommandInterceptor;

    public EventManager(LoggerBungee plugin) {
        this.plugin = plugin;
        this.registeredListeners = new HashMap<>();
        registerListeners();
    }

    private void registerListeners() {
        Configuration config = this.plugin.getConfigManager().getConfig();

        // Player Events
        if (config.getBoolean("Log-Player.Login", true)) {
            registerListener(Constants.Events.PLAYER_LOGIN, new PlayerLoginListener(this.plugin));
        }
        if (config.getBoolean("Log-Player.Leave", true)) {
            registerListener(Constants.Events.PLAYER_LEAVE, new PlayerDisconnectListener(this.plugin));
        }
        if (config.getBoolean("Log-Player.Switch", true)) {
            registerListener(Constants.Events.PLAYER_SWITCH, new PlayerSwitchListener(this.plugin));
        }
        if (config.getBoolean("Log-Player.Chat", true)) {
            registerListener(Constants.Events.PLAYER_CHAT, new ChatListener(this.plugin));
        }
        if (config.getBoolean("Log-Player.Commands", true)) {
            registerListener(Constants.Events.PLAYER_COMMAND, new CommandListener(this.plugin));
        }
        if (config.getBoolean("Log-Player.Kick", true)) {
            registerListener(Constants.Events.PLAYER_KICK, new ServerKickListener(this.plugin));
        }

        // Server Events
        if (config.getBoolean("Log-Server.Reload", true)) {
            registerListener(Constants.Events.SERVER_RELOAD, new ServerReloadListener(this.plugin));
        }

        if (config.getBoolean("Log-Server.Server-Commands", true) || config.getBoolean("Log-Server.Console-Commands", true)) {
            this.serverCommandInterceptor = new ServerCommandInterceptor(this.plugin);
            this.consoleCommandListener = new ConsoleCommandListener(this.plugin);
        }
    }

    private void registerListener(Constants.Events eventType, Listener listener) {
        this.plugin.getProxy().getPluginManager().registerListener(this.plugin, listener);
        this.registeredListeners.put(eventType, listener);
    }

    public void reload() {
        // Unregister all current listeners
        for (Listener listener : this.registeredListeners.values()) {
            this.plugin.getProxy().getPluginManager().unregisterListener(listener);
        }
        this.registeredListeners.clear();

        // Register listeners based on current configuration
        registerListeners();
    }

    public boolean isEventEnabled(Constants.Events eventType) {
        return this.registeredListeners.containsKey(eventType);
    }

    public Listener getListener(Constants.Events eventType) {
        return this.registeredListeners.get(eventType);
    }
}
