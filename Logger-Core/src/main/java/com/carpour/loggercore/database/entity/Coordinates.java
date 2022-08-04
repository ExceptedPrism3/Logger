package com.carpour.loggercore.database.entity;

import lombok.Data;

@Data
public class Coordinates {

    private int x;
    private int y;
    private int z;
    private String worldName;

    public Coordinates(int x, int y, int z, String worldName) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.worldName = worldName;
    }
}
