package me.prism3.loggercore.database.entity.enums;

public enum PlayerConnectionType {

    PLAYER_JOIN("joined"),
    PLAYER_LEAVE("left");

    private final String action;

    PlayerConnectionType(String action) { this.action = action; }

    public String rawAction() { return this.action; }
}
