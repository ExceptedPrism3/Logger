package com.carpour.loggercore.database.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "enchanting")
public class Enchanting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "server_name", length = 30)
    private String serverName;

    @Column(name = "date", nullable = false)
    private Instant date;

    @Column(name = "world", length = 100)
    private String world;

    @Column(name = "player_name", length = 100)
    private String playerName;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getZ() {
        return z;
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

    public Integer getEnchantmentLevel() {
        return enchantmentLevel;
    }

    public void setEnchantmentLevel(Integer enchantmentLevel) {
        this.enchantmentLevel = enchantmentLevel;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Boolean getIsStaff() {
        return isStaff;
    }

    public void setIsStaff(Boolean isStaff) {
        this.isStaff = isStaff;
    }

}