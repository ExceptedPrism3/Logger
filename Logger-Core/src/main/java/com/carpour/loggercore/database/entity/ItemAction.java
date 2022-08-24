package me.prism3.loggercore.database.entity;

import me.prism3.loggercore.database.entity.enums.ItemActionType;

import javax.persistence.*;

@Entity
@Table(name = "item_action")
public class ItemAction extends AbstractAction {

    @Column(name = "world", length = 100)
    private String world;

    @Column(name = "item", length = 50)
    private String item;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "x")
    private Integer x;

    @Column(name = "y")
    private Integer y;

    @Column(name = "z")
    private Integer z;

    @Column(name = "enchantment", length = 50)
    private String enchantment;

    @Column(name = "changed_name", length = 50)
    private String changedName;

    @Column(name = "is_staff")
    private Boolean isStaff;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_action_type", nullable = false)
    private ItemActionType itemActionType;

    public ItemActionType getItemActionType() { return itemActionType; }

    public void setItemActionType(ItemActionType itemActionType) { this.itemActionType = itemActionType; }

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
        return enchantment;
    }

    public void setEnchantment(String enchantment) {
        this.enchantment = enchantment;
    }

    public String getChangedName() {
        return changedName;
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
    public String getAction() { return this.entityPlayer.getPlayerName() + " itemactoin"; }
}