package com.carpour.loggercore.database.entity;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "tps")
public class Tp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "server_name", length = 30)
    private String serverName;
    @Column(name = "date", nullable = false)
    private Instant date;
    @Column(name = "tps")
    private Integer tps;

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

    public Integer getTps() {
        return this.tps;
    }

    public void setTps(Integer tps) {
        this.tps = tps;
    }

}