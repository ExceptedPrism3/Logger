package me.prism3.loggercore.database.entity;

import javax.persistence.*;

@Entity
@Table(name = "player_death")
public class PlayerDeath extends AbstractAction {

    @Column(name = "world", length = 100)
    private String world;

    @Column(name = "player_level")
    private Integer playerLevel;

    @Column(name = "x")
    private Integer x;

    @Column(name = "y")
    private Integer y;

    @Column(name = "z")
    private Integer z;

    @Column(name = "cause", length = 40)
    private String cause;

    @Column(name = "by_who", length = 30)
    private String byWho;

    @Column(name = "is_staff")
    private Boolean isStaff;


    public String getWorld() {
        return this.world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public Integer getPlayerLevel() {
        return playerLevel;
    }

    public void setPlayerLevel(Integer playerLevel) {
        this.playerLevel = playerLevel;
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

    public String getCause() {
        return this.cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getByWho() {
        return this.byWho;
    }

    public void setByWho(String byWho) {
        this.byWho = byWho;
    }

    public Boolean isStaff() {
        return this.isStaff;
    }

    public void isStaff(Boolean staff) {
        this.isStaff = staff;
    }

    @Override
    public String getAction() { return this.entityPlayer.getPlayerName() + " playerdeath"; }
}