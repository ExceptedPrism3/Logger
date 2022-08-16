package com.carpour.loggercore.database.entity;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "ram")
public class Ram {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "server_name", length = 30)
    private String serverName;
    @Column(name = "date", nullable = false)
    private Instant date;
    @Column(name = "total_memory")
    private Integer totalMemory;
    @Column(name = "used_memory")
    private Integer usedMemory;
    @Column(name = "free_memory")
    private Integer freeMemory;

    @PrePersist
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