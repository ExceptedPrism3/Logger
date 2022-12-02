package me.prism3.loggercore.database.entity;

import me.prism3.loggercore.database.entity.enums.BucketActionType;

import java.time.Instant;


public class BucketAction extends AbstractAction {

    private String world;

    private String bucket;

    private Integer x;

    private Integer y;

    private Integer z;

    private Boolean isStaff;

    private BucketActionType bucketActionType;

    public BucketActionType getBucketActionType() {
        return this.bucketActionType;
    }

    public void setBucketActionType(BucketActionType bucketActionType) {
        this.bucketActionType = bucketActionType;
    }

    public String getWorld() {
        return this.world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public String getBucket() {
        return this.bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
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


    public void prePersist() {
        this.date = Instant.now();
    }

    @Override
    public String getAction() {
        return this.entityPlayer.getPlayerName() + " " + this.bucketActionType.rawAction() + " " + this.bucket;
    }
}