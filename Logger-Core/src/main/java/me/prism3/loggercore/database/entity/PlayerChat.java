package me.prism3.loggercore.database.entity;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Table(name = "player_chat")
@NamedQueries({
        @NamedQuery(name = "playerchat.findAllByName", query = "select e from PlayerChat e where e.entityPlayer.playerName = :playerName")
})
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class PlayerChat extends AbstractAction {

    @Column(name = "world", length = 100)
    private String world;

    @Column(name = "message", length = 200)
    private String message;

    @Column(name = "is_staff")
    private Boolean isStaff;

    public String getWorld() {
        return this.world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean isStaff() {
        return this.isStaff;
    }

    public void isStaff(Boolean staff) {
        this.isStaff = staff;
    }

    @Override
    public String getAction() {
        return this.entityPlayer.getPlayerName() + " said " + this.getMessage();
    }
}