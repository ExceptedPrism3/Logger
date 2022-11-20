package me.prism3.loggercore.database.entity;

import java.time.Instant;

public class ServerAddress {
    private String serverName;
    private String playerName;
    private String playerUUID;
    private String domaineName;
    private Instant date;

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerUUID() {
        return playerUUID;
    }

    public void setPlayerUUID(String playerUUID) {
        this.playerUUID = playerUUID;
    }

    public String getDomaineName() {
        return domaineName;
    }

    public void setDomaineName(String domaineName) {
        this.domaineName = domaineName;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }
}
