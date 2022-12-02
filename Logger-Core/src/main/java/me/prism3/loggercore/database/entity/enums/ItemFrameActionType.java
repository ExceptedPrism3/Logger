package me.prism3.loggercore.database.entity.enums;

public enum ItemFrameActionType {

    ITEMFRAME_PLACE("placed"),
    ITEMFRAME_BREAK("broke");

    private final String action;

    ItemFrameActionType(String action) {
        this.action = action;
    }

    public String rawAction() {
        return this.action;
    }
}
