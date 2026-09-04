package me.prism3.logger_bungee.listeners.server;

import me.prism3.logger_bungee.LoggerBungee;
import me.prism3.logger_bungee.listeners.BaseListener;
import me.prism3.logger_bungee.utils.Constants;
import net.md_5.bungee.api.event.ProxyReloadEvent;
import net.md_5.bungee.event.EventHandler;

public class ServerReloadListener extends BaseListener {

    public ServerReloadListener(LoggerBungee plugin) {
        super(plugin);
    }

    @EventHandler
    public void onServerReload(ProxyReloadEvent event) {
        logServerEvent(Constants.Events.SERVER_RELOAD, null);
    }
} 