package me.prism3.loggercore.database.entity;

public class Anvil extends AbstractAction {


    private String newName;


    private Boolean isStaff;


    public String getNewName() {
        return this.newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    public Boolean isStaff() {
        return this.isStaff;
    }

    public void isStaff(Boolean staff) {
        this.isStaff = staff;
    }

    @Override
    public String getAction() {
        return this.entityPlayer.getPlayerName() + " anvil";
    }
}