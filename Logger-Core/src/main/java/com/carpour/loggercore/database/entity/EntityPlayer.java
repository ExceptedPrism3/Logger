package com.carpour.loggercore.database.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "logger_users", uniqueConstraints = {@UniqueConstraint(columnNames = {"player_name", "player_unique_id"})})
public class EntityPlayer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "player_name", length = 30)
    private String playerName;
    @Column(name = "player_unique_id")
    private String playerUniqueID;
    @Column(name = "is_staff")
    private boolean isStaff;

    @Column(name = "created_at")
    private Instant createdAt;

    public Instant getCreatedAt() {
        return createdAt;
    }

    public EntityPlayer() {
    }

    public EntityPlayer(String playerName, String playerUniqueID, boolean isStaff) {
        this.playerName = playerName;
        this.playerUniqueID = playerUniqueID;
        this.isStaff = isStaff;

    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerUniqueID() {
        return playerUniqueID;
    }

    public void setPlayerUniqueID(String playerUniqueID) {
        this.playerUniqueID = playerUniqueID;
    }

    public boolean isStaff() {
        return isStaff;
    }

    public void isStaff(boolean staff) {
        isStaff = staff;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @PostPersist
    public void postPersist() {
    this.createdAt = Instant.now();
    }
}
