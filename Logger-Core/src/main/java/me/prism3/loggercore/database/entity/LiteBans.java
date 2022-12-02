package me.prism3.loggercore.database.entity;

import java.time.Instant;

public class LiteBans {

    private String command;
    private String sender;
    private Instant date;
    private String reason;
    private String onWho;
    private String duration;
    private String serverName;

    private Boolean isSilent;

    public Boolean getSilent() {
        return this.isSilent;
    }

    public void setSilent(Boolean silent) {
        this.isSilent = silent;
    }

    public String getCommand() {
        return this.command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getSender() {
        return this.sender;
    }

    public void setSender(String executor) {
        this.sender = executor;
    }

    public Instant getDate() {
        return this.date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getReason() {
        return this.reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getOnWho() {
        return this.onWho;
    }

    public void setOnWho(String onWho) {
        this.onWho = onWho;
    }

    public String getDuration() {
        return this.duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getServerName() {
        return this.serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }
}
