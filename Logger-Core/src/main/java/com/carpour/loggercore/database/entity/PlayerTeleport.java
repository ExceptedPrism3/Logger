package me.prism3.loggercore.database.entity;

import javax.persistence.*;

@Entity
@Table(name = "player_teleport")
public class PlayerTeleport extends AbstractAction {

    @Column(name = "world", length = 100)
    private String world;

    @Column(name = "from_x")
    private Integer fromX;

    @Column(name = "from_y")
    private Integer fromY;

    @Column(name = "from_z")
    private Integer fromZ;

    @Column(name = "to_x")
    private Integer toX;

    @Column(name = "to_y")
    private Integer toY;

    @Column(name = "to_z")
    private Integer toZ;

    @Column(name = "is_staff")
    private Boolean isStaff;


    public String getWorld() {
        return this.world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public Integer getFromX() {
        return this.fromX;
    }

    public void setFromX(Integer fromX) {
        this.fromX = fromX;
    }

    public Integer getFromY() {
        return this.fromY;
    }

    public void setFromY(Integer fromY) {
        this.fromY = fromY;
    }

    public Integer getFromZ() {
        return this.fromZ;
    }

    public void setFromZ(Integer fromZ) {
        this.fromZ = fromZ;
    }

    public Integer getToX() {
        return this.toX;
    }

    public void setToX(Integer toX) {
        this.toX = toX;
    }

    public Integer getToY() {
        return this.toY;
    }

    public void setToY(Integer toY) {
        this.toY = toY;
    }

    public Integer getToZ() {
        return this.toZ;
    }

    public void setToZ(Integer toZ) {
        this.toZ = toZ;
    }

    public Boolean isStaff() {
        return this.isStaff;
    }

    public void isStaff(Boolean staff) {
        this.isStaff = staff;
    }

    @Override
    public String getAction() { return this.entityPlayer.getPlayerName() + " playerteleport"; }
}