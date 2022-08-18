package com.carpour.loggercore.database.entity;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Table(name = "block_interaction")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class BlockInteraction extends AbstractAction {


    @Column(name = "world", length = 100)
    private String world;
    @Column(name = "block", length = 40)
    private String block;
    @Column(name = "x")
    private Integer x;
    @Column(name = "y")
    private Integer y;
    @Column(name = "z")
    private Integer z;
    @Column(name = "is_staff")
    private Boolean isStaff;

    @Column(name = "interaction_type")
    @Enumerated(EnumType.STRING)
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