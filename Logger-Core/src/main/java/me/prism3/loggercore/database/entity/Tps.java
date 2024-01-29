package me.prism3.loggercore.database.entity;

import java.time.Instant;


public class Tps {

    private Long id;

    private String serverName;

    private Instant date;

    private Integer tps;

    public void prePersist() {
        this.date = Instant.now();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServerName() {
        return this.serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public Instant getDate() {
        return this.date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Integer getTps() {
        return this.tps;
    }

    public void setTps(Integer tps) {
        this.tps = tps;
    }
}