package com.carpour.loggercore.database.entity;

import com.carpour.loggercore.database.data.Coordinates;

import javax.persistence.*;

@Entity
@Table(name = "player_sign_text")
public class PlayerSignText extends AbstractAction {

    @Column(name = "world", length = 100)
    private String world;

    @Column(name = "x")
    private Integer x;

    @Column(name = "y")
    private Integer y;

    @Column(name = "z")
    private Integer z;

    @Column(name = "line", length = 60)
    private String line;

    @Column(name = "is_staff")
    private Boolean isStaff;


    public String getWorld() {
        return this.world;
    }

    public void setWorld(String world) {
        this.world = world;
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

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public void setCoordinates(Coordinates c) {
        this.x = c.getX();
        this.y = c.getY();
        this.z = c.getZ();
        this.world = c.getWorldName();
    }

    public Boolean isStaff() {
        return this.isStaff;
    }

    public void isStaff(Boolean staff) {
        this.isStaff = staff;
    }

    @Override
    public String getAction() { return this.entityPlayer.getPlayerName() + " playersigntesxt"; }
}