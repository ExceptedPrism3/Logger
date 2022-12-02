package me.prism3.loggercore.database.entity.enums;

public enum ItemActionType {

    ITEM_DROP("dropped"),
    ITEM_PICKUP("picked-up");

    private final String action;

    ItemActionType(String action) {
        this.action = action;
    }

    public String rawInteraction() {
        return this.action;
    }
}