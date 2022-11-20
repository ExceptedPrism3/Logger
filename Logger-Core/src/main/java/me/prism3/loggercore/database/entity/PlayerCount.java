package me.prism3.loggercore.database.entity;

import java.time.Instant;

public class PlayerCount {
    private String serverName;
    private Integer count;
    private Instant date;

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }
}
