package me.prism3.loggercore.database.entity;

import me.prism3.loggercore.database.entity.enums.ItemActionType;


public class ItemAction extends AbstractAction {

    private String world;

    private String item;

    private Integer amount;

    private Integer x;

    private Integer y;

    private Integer z;

    private String enchantment;

    private String changedName;

    private Boolean isStaff;

    private ItemActionType itemActionType;

    public ItemActionType getItemActionType() {
        return this.itemActionType;
    }

    public void setItemActionType(ItemActionType itemActionType) {
        this.itemActionType = itemActionType;
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

    public String getEnchantment() {
        return this.enchantment;
    }

    public void setEnchantment(String enchantment) {
        this.enchantment = enchantment;
    }

    public String getChangedName() {
        return this.changedName;
    }

    public void setChangedName(String changedName) {
        this.changedName = changedName;
    }

    public Boolean isStaff() {
        return this.isStaff;
    }

    public void isStaff(Boolean staff) {
        this.isStaff = staff;
    }

    @Override
    public String getAction() {
        return this.entityPlayer.getPlayerName() + " itemaction";
    }
}