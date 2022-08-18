package com.carpour.loggercore.database.entity.enums;

public enum BucketActionType {

    BUCKET_FILL("bucket_fill"),
    BUCKET_EMPTY("bucket_empty");

    private final String action;

    BucketActionType(String action) {this.action = action;}

    public String rawAction() {return this.action;}

}