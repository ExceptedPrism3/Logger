package me.prism3.loggercore.database.entity;

import me.prism3.loggercore.database.entity.enums.PlayerConnectionType;

import javax.persistence.*;

@Entity
@Table(name = "player_connection")
public class PlayerConnection extends AbstractAction {

    @Column(name = "world", length = 100)
    private String world;

    @Column(name = "x")
    private Integer x;
    @Column(name = "y")
    private Integer y;
    @Column(name = "z")
    private Integer z;
    @Column(name = "ip", columnDefinition = "INT UNSIGNED")
    private Long ip;
    @Column(name = "is_staff")
    private Boolean isStaff;

    @Enumerated(EnumType.STRING)
    @Column(name = "player_connection_type", nullable = false)
    private PlayerConnectionType playerConnectionType;

    public PlayerConnectionType getPlayerConnectionType() { return playerConnectionType; }

    public void setPlayerConnectionType(
            PlayerConnectionType playerConnectionType) { this.playerConnectionType = playerConnectionType; }


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

    public Long getIp() {
        return this.ip;
    }

    public void setIp(Long ip) {
        this.ip = ip;
    }

    public Boolean isStaff() {
        return this.isStaff;
    }

    public void isStaff(Boolean staff) {
        this.isStaff = staff;
    }

    @Override
    public String getAction() {
        return this.entityPlayer.getPlayerName() + " " + this.getPlayerConnectionType()
                .rawAction() + " at " + this.getDate().toString();
    }
}