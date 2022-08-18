package com.carpour.loggercore.database.entity;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Cacheable
@Proxy(lazy = false)
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class AbstractAction implements ActionInterface {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id", nullable = false)
    protected Long id;
    @ManyToOne(cascade = CascadeType.PERSIST, optional = false, targetEntity = EntityPlayer.class)
    @JoinColumn(name = "entity_player_id", nullable = false, updatable = false)
    protected EntityPlayer entityPlayer;
    @Column(name = "server_name", length = 30)
    protected String serverName;
    @Column(name = "date", nullable = false)
    protected Instant date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EntityPlayer getEntityPlayer() {
        return entityPlayer;
    }

    public void setEntityPlayer(EntityPlayer entityPlayer) {
        this.entityPlayer = entityPlayer;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }


    @Override
    public String getAction() {
        return null;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    @PrePersist
    public void prePersist() {
        this.date = Instant.now();
    }


}

