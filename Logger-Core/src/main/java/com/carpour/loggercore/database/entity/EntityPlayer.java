package com.carpour.loggercore.database.entity;

import org.hibernate.annotations.CacheConcurrencyStrategy;

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
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class EntityPlayer implements Serializable {

    @Id
    @Column(name = "player_name")
    private String playerName;
    @Column(name = "player_unique_id")
    private String playerUniqueID;

    @Column(name = "created_at")
    private Instant createdAt;

    public EntityPlayer() {}

    public EntityPlayer(String playerName, String playerUniqueID) {
        this.playerName = playerName;
        this.playerUniqueID = playerUniqueID;
    }

    public Instant getCreatedAt() { return this.createdAt; }

    public String getPlayerName() { return this.playerName; }

    public void setPlayerName(String playerName) { this.playerName = playerName; }

    public String getPlayerUniqueID() { return this.playerUniqueID; }

    public void setPlayerUniqueID(String playerUniqueID) { this.playerUniqueID = playerUniqueID; }

    @PrePersist
    public void prePersist() { this.createdAt = Instant.now(); }


}
