package com.carpour.loggercore.database.entity;

public enum InteractionType {

    BLOCK_PLACE("placed"),
    BLOCK_BREAK("broke"),
    WOOD_STRIPPING("stripped");

    private final String interaction;

    InteractionType(String interaction) {this.interaction = interaction;}

    public String rawInteraction() {return this.interaction;}

}
