package com.carpour.loggercore.database.entity.enums;

public enum PlayerConnectionType {
    PLAYER_JOIN("player_join"),
    PLAYER_LEAVE("player_leave");

    private final String action;

    PlayerConnectionType(String action) {this.action = action;}

    public String rawAction() {return this.action;}
}
