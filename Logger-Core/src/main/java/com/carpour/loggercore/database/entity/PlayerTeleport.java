package com.carpour.loggercore.database.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "player_teleport")
public class PlayerTeleport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "server_name", length = 30)
    private String serverName;

    @Column(name = "date", nullable = false)
    private Instant date;

    @Column(name = "world", length = 100)
    private String world;

    @Column(name = "player_name", length = 100)
    private String playerName;

    @Column(name = "from_x")
    private Integer fromX;

    @Column(name = "from_y")
    private Integer fromY;

    @Column(name = "from_z")
    private Integer fromZ;

    @Column(name = "to_x")
    private Integer toX;

    @Column(name = "to_y")
    private Integer toY;

    @Column(name = "to_z")
    private Integer toZ;

    @Column(name = "is_staff")
    private Boolean isStaff;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Integer getFromX() {
        return fromX;
    }

    public void setFromX(Integer fromX) {
        this.fromX = fromX;
    }

    public Integer getFromY() {
        return fromY;
    }

    public void setFromY(Integer fromY) {
        this.fromY = fromY;
    }

    public Integer getFromZ() {
        return fromZ;
    }

    public void setFromZ(Integer fromZ) {
        this.fromZ = fromZ;
    }

    public Integer getToX() {
        return toX;
    }

    public void setToX(Integer toX) {
        this.toX = toX;
    }

    public Integer getToY() {
        return toY;
    }

    public void setToY(Integer toY) {
        this.toY = toY;
    }

    public Integer getToZ() {
        return toZ;
    }

    public void setToZ(Integer toZ) {
        this.toZ = toZ;
    }

    public Boolean getIsStaff() {
        return isStaff;
    }

    public void setIsStaff(Boolean isStaff) {
        this.isStaff = isStaff;
    }

}