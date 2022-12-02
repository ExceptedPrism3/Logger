package me.prism3.loggercore.database.entity;

import java.io.Serializable;
import java.time.Instant;


public class EntityPlayer implements Serializable {

    private String playerName;

    private String playerUniqueID;

    private Instant createdAt;

    public EntityPlayer(String playerName, String playerUniqueID) {
        this.playerName = playerName;
        this.playerUniqueID = playerUniqueID;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerUniqueID() {
        return this.playerUniqueID;
    }

    public void setPlayerUniqueID(String playerUniqueID) {
        this.playerUniqueID = playerUniqueID;
    }

    public void prePersist() {
        this.createdAt = Instant.now();
    }
}
