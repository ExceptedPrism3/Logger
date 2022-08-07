package com.carpour.loggercore.database.entity;

import lombok.Data;

@Data
public class EntityPlayer {

    private String playerName;

    private String playerUniqueID;

    private boolean isStaff;

    public EntityPlayer(String playerName, String playerUniqueID, boolean isStaff) {
        this.playerName = playerName;
        this.playerUniqueID = playerUniqueID;
        this.isStaff = isStaff;

    }
}
