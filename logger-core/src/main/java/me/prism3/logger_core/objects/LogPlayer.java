package me.prism3.logger_core.objects;

import java.util.UUID;

public class LogPlayer {
    private final String name;
    private final UUID uuid;
    private final String serverName; // Bungee server or Spigot server
    
    public LogPlayer(String name, UUID uuid, String serverName) {
        this.name = name;
        this.uuid = uuid;
        this.serverName = serverName;
    }

    public String getName() { return name; }
    public UUID getUniqueId() { return uuid; }
    public String getServerName() { return serverName; }
}
