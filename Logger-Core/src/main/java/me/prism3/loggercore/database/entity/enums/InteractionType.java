package me.prism3.loggercore.database.entity.enums;

public enum InteractionType {

    BLOCK_PLACE("placed"),
    BLOCK_BREAK("broke"),
    WOOD_STRIPPING("stripped");

    private final String interaction;

    InteractionType(String interaction) {
        this.interaction = interaction;
    }

    public String rawInteraction() {
        return this.interaction;
    }

}
