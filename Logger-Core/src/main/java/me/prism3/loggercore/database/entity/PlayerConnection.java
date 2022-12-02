package me.prism3.loggercore.database.entity;

import me.prism3.loggercore.database.entity.enums.PlayerConnectionType;


public class PlayerConnection extends AbstractAction {

    private String world;

    private Integer x;
    private Integer y;
    private Integer z;

    private Long ip;

    private Boolean isStaff;

    private PlayerConnectionType playerConnectionType;

    public PlayerConnectionType getPlayerConnectionType() {
        return this.playerConnectionType;
    }

    public void setPlayerConnectionType(PlayerConnectionType playerConnectionType) { this.playerConnectionType = playerConnectionType; }

    public String getWorld() {
        return this.world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public Integer getX() {
        return this.x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return this.y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getZ() {
        return this.z;
    }

    public void setZ(Integer z) {
        this.z = z;
    }

    public Long getIp() {
        return this.ip;
    }

    public void setIp(Long ip) {
        this.ip = ip;
    }

    public Boolean isStaff() {
        return this.isStaff;
    }

    public void isStaff(Boolean staff) {
        this.isStaff = staff;
    }

    @Override
    public String getAction() {

        return this.entityPlayer.getPlayerName() + " " + this.getPlayerConnectionType()
                .rawAction() + " at " + this.getDate().toString();
    }
}