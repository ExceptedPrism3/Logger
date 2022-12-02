package me.prism3.loggercore.database.entity;

public class PlayerLevel extends AbstractAction {

    private Boolean isStaff;

    public Boolean isStaff() {
        return this.isStaff;
    }

    public void isStaff(Boolean staff) {
        this.isStaff = staff;
    }

    @Override
    public String getAction() {
        return this.entityPlayer.getPlayerName() + " playerlevel";
    }
}