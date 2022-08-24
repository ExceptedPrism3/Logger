package me.prism3.loggercore.database.entity;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.time.Instant;

@MappedSuperclass
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class AbstractAction implements ActionInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    protected Long id;
    @ManyToOne(cascade = CascadeType.MERGE, optional = false, targetEntity = EntityPlayer.class)
    @JoinColumn(name = "entity_player_id", nullable = false, updatable = false)
    protected EntityPlayer entityPlayer;
    @Column(name = "server_name", length = 30)
    protected String serverName;
    @Column(name = "date", nullable = false)
    protected Instant date;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EntityPlayer getEntityPlayer() {
        return this.entityPlayer;
    }

    public void setEntityPlayer(EntityPlayer entityPlayer) {
        this.entityPlayer = entityPlayer;
    }

    public String getServerName() {
        return this.serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    @Override
    public String getAction() {
        return null;
    }

    public Instant getDate() {
        return this.date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }
}

