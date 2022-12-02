package me.prism3.loggercore.database.entity;

public class GameMode extends AbstractAction {

    private String world;

    private String gameMode;

    private Boolean isStaff;

    public String getWorld() {
        return this.world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public String getGameMode() {
        return this.gameMode;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public Boolean isStaff() {
        return this.isStaff;
    }

    public void isStaff(Boolean staff) {
        this.isStaff = staff;
    }

    @Override
    public String getAction() {
        return this.entityPlayer.getPlayerName() + " gamemode";
    }
}