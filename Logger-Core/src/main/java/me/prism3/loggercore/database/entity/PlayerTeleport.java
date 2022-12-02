package me.prism3.loggercore.database.entity;


public class PlayerTeleport extends AbstractAction {

    private String world;

    private Integer fromX;

    private Integer fromY;

    private Integer fromZ;

    private Integer toX;

    private Integer toY;

    private Integer toZ;

    private Boolean isStaff;

    public String getWorld() {
        return this.world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public Integer getFromX() {
        return this.fromX;
    }

    public void setFromX(Integer fromX) {
        this.fromX = fromX;
    }

    public Integer getFromY() {
        return this.fromY;
    }

    public void setFromY(Integer fromY) {
        this.fromY = fromY;
    }

    public Integer getFromZ() {
        return this.fromZ;
    }

    public void setFromZ(Integer fromZ) {
        this.fromZ = fromZ;
    }

    public Integer getToX() {
        return this.toX;
    }

    public void setToX(Integer toX) {
        this.toX = toX;
    }

    public Integer getToY() {
        return this.toY;
    }

    public void setToY(Integer toY) {
        this.toY = toY;
    }

    public Integer getToZ() {
        return this.toZ;
    }

    public void setToZ(Integer toZ) {
        this.toZ = toZ;
    }

    public Boolean isStaff() {
        return this.isStaff;
    }

    public void isStaff(Boolean staff) {
        this.isStaff = staff;
    }

    @Override
    public String getAction() {
        return this.entityPlayer.getPlayerName() + " playerteleport";
    }
}