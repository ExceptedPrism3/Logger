package com.carpour.loggercore.database.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "player_messages")
public class PlayerMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "player_name", length = 30)
    private String playerName;

    @Column(name = "logged_at", nullable = false)
    private Instant loggedAt;

    @Column(name = "message")
    private String message;

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Instant getLoggedAt() {
        return loggedAt;
    }

    public void setLoggedAt(Instant loggedAt) {
        this.loggedAt = loggedAt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}