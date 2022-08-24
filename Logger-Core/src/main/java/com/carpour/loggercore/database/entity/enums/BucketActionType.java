package me.prism3.loggercore.database.entity.enums;

public enum BucketActionType {

    BUCKET_FILL("filled"),
    BUCKET_EMPTY("emptied");

    private final String action;

    BucketActionType(String action) { this.action = action; }

    public String rawAction() { return this.action; }

}