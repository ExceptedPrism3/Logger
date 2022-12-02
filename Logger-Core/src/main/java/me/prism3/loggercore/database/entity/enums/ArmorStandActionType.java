package me.prism3.loggercore.database.entity.enums;

public enum ArmorStandActionType {

    ARMORSTAND_PLACE("placed"),
    ARMORSTAND_BREAK("broke");

    private final String action;

    ArmorStandActionType(String action) {
        this.action = action;
    }

    public String rawAction() {
        return this.action;
    }
}
