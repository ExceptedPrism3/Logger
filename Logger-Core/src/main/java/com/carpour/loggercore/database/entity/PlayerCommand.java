package me.prism3.loggercore.database.entity;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Table(name = "player_commands")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class PlayerCommand extends AbstractAction {

    @Column(name = "world", length = 100)
    private String world;

    @Column(name = "command", length = 256)
    private String command;

    @Column(name = "is_staff")
    private Boolean isStaff;


    public String getWorld() {
        return this.world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Boolean isStaff() {
        return this.isStaff;
    }

    public void isStaff(Boolean staff) {
        this.isStaff = staff;
    }

    @Override
    public String getAction() { return this.entityPlayer.getPlayerName() + " playercommand"; }
}