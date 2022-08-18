package com.carpour.loggercore.database.entity;

import com.carpour.loggercore.database.entity.enums.BucketActionType;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "bucket_action")
public class BucketAction extends AbstractAction {


    @Column(name = "world", length = 100)
    private String world;

    @Column(name = "bucket", length = 40)
    private String bucket;

    @Column(name = "x")
    private Integer x;

    @Column(name = "y")
    private Integer y;

    @Column(name = "z")
    private Integer z;
    @Column(name = "is_staff")
    private Boolean isStaff;

    @Enumerated(EnumType.STRING)
    @Column(name = "bucket_action_type", nullable = false)
    private BucketActionType bucketActionType;

    public BucketActionType getBucketActionType() {return bucketActionType;}

    public void setBucketActionType(
            BucketActionType bucketActionType) {this.bucketActionType = bucketActionType;}


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

    @PrePersist
    public void prePersist() {
        this.date = Instant.now();
    }

    @Override
    public String getAction() {
        return this.entityPlayer.getPlayerName() + " " + this.bucketActionType.rawAction() + " " + this.bucket;
    }

}