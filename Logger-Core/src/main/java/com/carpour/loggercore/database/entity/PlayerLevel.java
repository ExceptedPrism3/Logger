package com.carpour.loggercore.database.entity;

import javax.persistence.*;

@Entity
@Table(name = "player_level")
public class PlayerLevel extends AbstractAction {

    @Column(name = "is_staff")
    private Boolean isStaff;


    public Boolean isStaff() {
        return this.isStaff;
    }

    public void isStaff(Boolean staff) { this.isStaff = staff; }

    @Override
    public String getAction() { return this.entityPlayer.getPlayerName() + " playerleve"; }
}