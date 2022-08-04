package com.carpour.loggercore.database.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class PlayerTime {

    public static final Map<String, PlayerTime> playersTime = new HashMap<>();
    private LocalDateTime joinTime;
    private final String playerName;
    private final String serverName;

    public PlayerTime(String serverName, String playerName) {
        this.playerName = playerName;
        this.setJoinTimeToNow();
        this.serverName = serverName;
        PlayerTime.playersTime.put(this.playerName, this);
    }

    public void setJoinTimeToNow() {
        this.joinTime = LocalDateTime.now();
    }

    public long getCurrentSessionTime() {
        return Duration.between(this.joinTime, LocalDateTime.now()).getSeconds();
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public String getServerName() {
        return this.serverName;
    }

    @Override
    public String toString() {
        return "PlayerTime{" +
                "joinTime=" + this.joinTime +
                ", playerName='" + this.playerName + '\'' +
                ", serverName='" + this.serverName + '\'' +
                '}';
    }
}
