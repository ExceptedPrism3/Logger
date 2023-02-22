package me.prism3.loggercore.database.entity;

public class PlayerLevel extends AbstractAction {

    private Boolean isStaff;

    private Integer playerLevel;

    public Boolean isStaff() {
        return this.isStaff;
    }

    public void isStaff(Boolean staff) {
        this.isStaff = staff;
    }

    public void setPlayerLevel(Integer playerLevel){
        this.playerLevel = playerLevel;
    }
    public Integer getPlayerLevel(){
        return this.playerLevel;
    }
    @Override
    public String getAction() {
        return this.entityPlayer.getPlayerName() + " playerlevel";
    }
}