package me.prism3.loggercore.database.entity;

import me.prism3.loggercore.database.entity.enums.ArmorStandActionType;


public class StandCrystal extends AbstractAction {

    private String world;
    private Integer x;
    private Integer y;
    private Integer z;
    private Boolean isStaff;
    private String block;
    public String getWorld() {
        return this.world;
    }
    private ArmorStandActionType armorStandActionType;

    public ArmorStandActionType getArmorStandActionType() {
        return armorStandActionType;
    }

    public void setArmorStandActionType(ArmorStandActionType armorStandActionType) {
        this.armorStandActionType = armorStandActionType;
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

    public Boolean isStaff() {
        return this.isStaff;
    }

    public void isStaff(Boolean staff) {
        this.isStaff = staff;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }
}