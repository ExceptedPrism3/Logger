package me.prism3.loggercore.database.entity;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Table(name = "furnace")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Furnace extends AbstractAction {

    @Column(name = "world", length = 100)
    private String world;

    @Column(name = "item", length = 250)
    private String item;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "x")
    private Integer x;

    @Column(name = "y")
    private Integer y;

    @Column(name = "z")
    private Integer z;

    @Column(name = "is_staff")
    private Boolean isStaff;

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

    @Override
    public String getAction() { return this.entityPlayer.getPlayerName() + " furnace"; }
}