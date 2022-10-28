package me.prism3.loggercore.database.entity;

import java.time.Instant;


public class ConsoleCommand implements ActionInterface {


    private Long id;

    private String serverName;


    private Instant date;

    private String command;

    public ConsoleCommand() {
    }

    public ConsoleCommand(String serverName, String command) {
        this.serverName = serverName;
        this.command = command;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServerName() {
        return this.serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    @Override
    public String getAction() {
        return "Console ran " + this.command;
    }

    public Instant getDate() {
        return this.date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getCommand() {
        return this.command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}