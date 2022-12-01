package me.prism3.loggercore.database.entity;

import java.time.Instant;

public class PlayerCount {
    private String serverName;
    private Instant date;

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }
}
