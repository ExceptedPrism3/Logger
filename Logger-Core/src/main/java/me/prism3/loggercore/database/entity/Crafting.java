package me.prism3.loggercore.database.entity;


public class Crafting extends AbstractAction {

    private String world;

    private String item;

    private Integer amount;

    private Integer x;

    private Integer y;

    private Integer z;

    private Boolean isStaff;

    @Override
    public String getAction() {
        return this.entityPlayer.getPlayerName() + " crafted " + this.item.toLowerCase() + " x " + this.amount;
    }

    public String getWorld() {
        return this.world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public String getItem() {
        return this.item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Integer getAmount() {
        return this.amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
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

    public Boolean isStaff() {
        return this.isStaff;
    }

    public void isStaff(Boolean staff) {
        this.isStaff = staff;
    }
}