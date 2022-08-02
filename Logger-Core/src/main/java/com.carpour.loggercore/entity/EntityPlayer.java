package com.carpour.loggercore.entity;

import lombok.Data;

@Data
public class EntityPlayer {

    private String playerName;

    private String worldName;

    private String playerUniqueID;

    private int x;

    private int y;

    private int z;

    private boolean isStaff;



}
