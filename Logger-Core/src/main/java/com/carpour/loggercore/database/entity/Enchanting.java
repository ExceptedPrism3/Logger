package me.prism3.loggercore.database.entity;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Table(name = "enchanting")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Enchanting extends AbstractAction {

    @Column(name = "world", length = 100)
    private String world;

    @Column(name = "x")
    private Integer x;

    @Column(name = "y")
    private Integer y;

    @Column(name = "z")
    private Integer z;

    @Column(name = "enchantment", length = 50)
    private String enchantment;

    @Column(name = "enchantment_level")
    private Integer enchantmentLevel;

    @Column(name = "item", length = 50)
    private String item;

    @Column(name = "cost")
    private Integer cost;

    @Column(name = "is_staff")
    private Boolean isStaff;

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

    public String getEnchantment() {
        return this.enchantment;
    }

    public void setEnchantment(String enchantment) {
        this.enchantment = enchantment;
    }

    public Integer getEnchantmentLevel() {
        return this.enchantmentLevel;
    }

    public void setEnchantmentLevel(Integer enchantmentLevel) {
        this.enchantmentLevel = enchantmentLevel;
    }

    public String getItem() {
        return this.item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Integer getCost() {
        return this.cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Boolean isStaff() {
        return this.isStaff;
    }

    public void isStaff(Boolean staff) {
        this.isStaff = staff;
    }

    @Override
    public String getAction() { return this.entityPlayer.getPlayerName() + " enchanting"; }
}