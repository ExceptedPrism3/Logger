package com.carpour.loggercore.database.entity;

import com.carpour.loggercore.database.data.Coordinates;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "entity_death")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class EntityDeath extends AbstractAction {

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
    @Column(name = "is_staff")
    private Boolean isStaff;

    public EntityDeath() {}

    public EntityDeath(String serverName, Coordinates coords, String mob, EntityPlayer entityPlayer,
                       Boolean isStaff) {
        this.serverName = serverName;
        this.world = coords.getWorldName();
        this.mob = mob;
        this.x = coords.getX();
        this.y = coords.getY();
        this.z = coords.getZ();
        this.entityPlayer = entityPlayer;
        this.isStaff = isStaff;
    }

    public String getWorld() {
        return this.world;
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
        return this.x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return this.y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getZ() {
        return this.z;
    }

    public void setZ(Integer z) {
        this.z = z;
    }

    public Boolean isStaff() {
        return this.isStaff;
    }

    public void isStaff(Boolean staff) {
        this.isStaff = staff;
    }

    @Override
    public String getAction() {
        return this.entityPlayer.getPlayerName() + " killed " + this.mob;
    }

}