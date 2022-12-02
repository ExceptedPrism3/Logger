package me.prism3.loggercore.database.entity;

import java.time.Instant;

public class Ram {

    private Long id;

    private String serverName;

    private Instant date;

    private Integer totalMemory;

    private Integer usedMemory;

    private Integer freeMemory;

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

    public Integer getTotalMemory() {
        return this.totalMemory;
    }

    public void setTotalMemory(Integer totalMemory) {
        this.totalMemory = totalMemory;
    }

    public Integer getUsedMemory() {
        return this.usedMemory;
    }

    public void setUsedMemory(Integer usedMemory) {
        this.usedMemory = usedMemory;
    }

    public Integer getFreeMemory() {
        return this.freeMemory;
    }

    public void setFreeMemory(Integer freeMemory) {
        this.freeMemory = freeMemory;
    }
}