package me.prism3.loggercore.database.entity;

import me.prism3.loggercore.database.data.Coordinates;


public class EntityDeath extends AbstractAction {

    private String world;

    private String mob;
    private Integer x;
    private Integer y;
    private Integer z;

    private Boolean isStaff;

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

    public String getWorld() {
        return this.world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public String getMob() {
        return this.mob;
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