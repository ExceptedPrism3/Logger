package me.prism3.loggercore.database.entity;

import java.time.Instant;

public class Litebans {

    private String command;
    private String sender;
    private Instant date;
    private String reason;
    private String onWho;
    private String duration;
    private String serverName;

    private Boolean isSilent;

    public Boolean getSilent() {
        return isSilent;
    }

    public void setSilent(Boolean silent) {
        isSilent = silent;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String executor) {
        this.sender = executor;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getOnWho() {
        return onWho;
    }

    public void setOnWho(String onWho) {
        this.onWho = onWho;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }
}
