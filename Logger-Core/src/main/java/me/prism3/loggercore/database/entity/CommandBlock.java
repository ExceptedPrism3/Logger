package me.prism3.loggercore.database.entity;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "command_block")
public class CommandBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "server_name", length = 30)
    private String serverName;

    @Column(name = "date", nullable = false)
    @CreationTimestamp
    private Instant date;

    @Column(name = "command", length = 256)
    private String command;

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

    public String getCommand() {
        return this.command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    @PrePersist
    public void prePersist() {
        this.date = Instant.now();
    }
}