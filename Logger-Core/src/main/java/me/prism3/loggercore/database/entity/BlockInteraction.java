package me.prism3.loggercore.database.entity;

import me.prism3.loggercore.database.entity.enums.InteractionType;


public class BlockInteraction extends AbstractAction {

    private String world;

    private String block;

    private Integer x;
    private Integer y;
    private Integer z;
    private Boolean isStaff;

    private InteractionType interactionType;

    public String getWorld() {
        return this.world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public String getBlock() {
        return this.block;
    }

    public void setBlock(String block) {
        this.block = block;
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

    public InteractionType getInteractionType() {
        return this.interactionType;
    }

    public void setInteractionType(InteractionType interactionType) {
        this.interactionType = interactionType;
    }

    @Override
    public String getAction() {
        return this.entityPlayer.getPlayerName() + " " + this.interactionType.rawInteraction() + " " + this.block.toLowerCase();
    }
}