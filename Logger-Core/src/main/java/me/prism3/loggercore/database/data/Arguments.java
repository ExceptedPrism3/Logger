package me.prism3.loggercore.database.data;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;

public class Arguments {
    private String username;
    private String time;
    private String action;
    private Integer page = 1;
    private Integer height = 10;
    private Long totalCount = 0L;
    private String inputCommand;

    public Arguments(String[] args) {
        Arrays.stream(args).filter(argument -> argument.startsWith("user:")).findFirst()
              .ifPresent(usernameParam -> this.username = usernameParam.replace("user:", ""));

        Arrays.stream(args).filter(argument -> argument.startsWith("action:")).findFirst()
              .ifPresent(actionParam -> this.action = actionParam.replace("action:", ""));

        Arrays.stream(args).filter(argument -> argument.startsWith("time:")).findFirst()
              .ifPresent(timeParam -> this.time = timeParam.replace("time:", ""));

        Arrays.stream(args).filter(argument -> argument.startsWith("page:")).findFirst()
              .ifPresent(pageParam -> this.page = Integer.parseUnsignedInt(pageParam.replace("page:", "")));
    }

    public Long getTotalCount() {
        return this.totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public String getInputCommand() {
        return this.inputCommand;
    }

    public void setInputCommand(String inputCommand) {
        this.inputCommand = inputCommand;
    }

    public Integer getHeight() {
        return this.height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return "Arguments{" +
                "username='" + this.username + '\'' +
                ", time='" + this.time + '\'' +
                ", action='" + this.action + '\'' +
                ", page=" + this.page +
                ", height=" + this.height +
                ", totalCount=" + this.totalCount +
                ", inputCommand='" + this.inputCommand + '\'' +
                '}';
    }

    public Integer getPage() {
        return this.page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getAction() {
        return this.action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getMinimumDate() {

        String day = null;
        String week = null;
        String min = null;
        String hour = null;

        if (this.getTime().endsWith("d"))
            day = this.getTime().replace("d", "");

        if (this.getTime().endsWith("w"))
            week = this.getTime().replace("w", "");

        if (this.getTime().endsWith("m"))
            min = this.getTime().replace("m", "");

        if (this.getTime().endsWith("h"))
            hour = this.getTime().replace("h", "");

        SimpleDateFormat s = new SimpleDateFormat();
        s.applyPattern("yyyy-MM-dd HH:mm:ss");

        if (min != null)
            return s.format(Date.from(Instant.now().minusSeconds(Long.parseLong(min) * 60)));

        if (hour != null)
            return s.format(Date.from(Instant.now().minusSeconds(Long.parseLong(hour) * 3600)));

        if (day != null)
            return s.format(Date.from(Instant.now().minusSeconds(Long.parseLong(day) * 86400)));

        if (week != null)
            return s.format(Date.from(Instant.now().minusSeconds(Long.parseLong(week) * 604800)));

        return null;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String forNextPage() {
        return returnCommand() + "page:" + (this.page + 1);
    }

    public String returnCommand() {

        String a = "/loggerget ";

        if (this.action != null)
            a += "action:" + this.action + " ";

        if (this.time != null)
            a += "time:" + this.time + " ";

        if (this.getUsername() != null)
            a += "user:" + this.username + " ";

        return a;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String forPreviousPage() {
        if (this.page == 0 || this.page == 1)
            return returnCommand() + "page:" + (this.page);

        return returnCommand() + "page:" + (this.page - 1);
    }

    public boolean isValidTime() {

        if (this.time == null || this.time.isEmpty())
            return true;

        return this.time.matches("^[1-9]\\d+[h|w|m|d]$");
    }
}
