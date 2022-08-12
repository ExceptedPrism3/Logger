package com.carpour.loggercore.database.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "logger_users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"player_name", "player_unique_id"})
})
@NamedQueries({
        @NamedQuery(name = "EntityPlayer.findOneByName", query = "select e from EntityPlayer e where e.playerName = :playerName")
})

public class EntityPlayer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "player_name", length = 30)
    private String playerName;
    @Column(name = "player_unique_id")
    private String playerUniqueID;

    @Column(name = "created_at")
    private Instant createdAt;

    public EntityPlayer() {
    }

    public EntityPlayer(String playerName, String playerUniqueID) {
        this.playerName = playerName;
        this.playerUniqueID = playerUniqueID;

    }

    public Instant getCreatedAt() {
        return createdAt;
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
