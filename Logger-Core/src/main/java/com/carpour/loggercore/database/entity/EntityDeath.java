package com.carpour.loggercore.database.entity;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "entity_death")
public class EntityDeath {
    public EntityDeath() {
    }

    public EntityDeath(String serverName, Coordinates coords, String mob, EntityPlayer entityPlayer, Boolean isStaff) {
        this.serverName = serverName;
        this.world = coords.getWorldName();
        this.mob = mob;
        this.x = coords.getX();
        this.y = coords.getY();
        this.z = coords.getZ();
        this.entityPlayer = entityPlayer;
        this.isStaff = isStaff;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "server_name", length = 30)
    private String serverName;

    @Column(name = "date", nullable = false)
    @CreationTimestamp
    private Instant date;

    @Column(name = "world", length = 100)
    private String world;


    @Column(name = "mob", length = 50)
    private String mob;

    @Column(name = "x")
    private Integer x;

    @Column(name = "y")
    private Integer y;

    @Column(name = "z")
    private Integer z;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "entity_player_id", nullable = false)
    private EntityPlayer entityPlayer;
    @Column(name = "is_staff")
    private Boolean isStaff;

    public EntityPlayer getEntityPlayer() {
        return entityPlayer;
    }

    public void setEntityPlayer(EntityPlayer entityPlayer) {
        this.entityPlayer = entityPlayer;
    }

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

    public String getMob() {
        return mob;
    }

    public void setMob(String mob) {
        this.mob = mob;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getZ() {
        return z;
    }

    public void setZ(Integer z) {
        this.z = z;
    }

    public Boolean isStaff() {
        return isStaff;
    }

    public void isStaff(Boolean staff) {
        isStaff = staff;
    }

}