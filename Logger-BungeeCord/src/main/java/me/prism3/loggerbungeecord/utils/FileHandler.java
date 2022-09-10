package me.prism3.loggerbungeecord.utils;

import me.prism3.loggerbungeecord.Main;
import me.prism3.loggerbungeecord.hooks.LiteBanUtil;
import me.prism3.loggerbungeecord.hooks.PartyAndFriendsUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static me.prism3.loggerbungeecord.utils.Data.fileDeletion;

public class FileHandler {

    private static File staffLogFolder;
    private static File chatLogFolder;
    private static File commandLogFolder;
    private static File loginLogFolder;
    private static File leaveLogFolder;
    private static File serverSwitchLogFolder;

    private static File reloadLogFolder;
    private static File serverStartLogFolder;
    private static File serverStopLogFolder;
    private static File ramLogFolder;

    private static File liteBansLogFolder;
    private static File pafFriendMessageLogFolder;
    private static File pafPartyMessageLogFolder;


    private static File staffLogFile;
    private static File chatLogFile;
    private static File commandLogFile;
    private static File loginLogFile;
    private static File leaveLogFile;
    private static File serverSwitchLogFile;

    private static File reloadLogFile;
    private static File serverStartLogFile;
    private static File serverStopLogFile;
    private static File ramLogFile;

    private static File liteBansBansLogFile;
    private static File liteBansMuteLogFile;
    private static File liteBansKickLogFile;
    private static File pafFriendMessageLogFile;
    private static File pafPartyMessageLogFile;

    public FileHandler(File dataFolder) {

        dataFolder.mkdir();

        final File logsFolder = new File(dataFolder, "Logs");
        logsFolder.mkdir();

        final Date date = new Date();
        final SimpleDateFormat filenameDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        staffLogFolder = new File(logsFolder, "Staff");
        staffLogFile = new File(staffLogFolder, filenameDateFormat.format(date) + ".log");

        chatLogFolder = new File(logsFolder, "Player Chat");
        chatLogFile = new File(chatLogFolder, filenameDateFormat.format(date) + ".log");

        commandLogFolder = new File(logsFolder, "Player Commands");
        commandLogFile = new File(commandLogFolder, filenameDateFormat.format(date) + ".log");

        loginLogFolder = new File(logsFolder, "Player Login");
        loginLogFile = new File(loginLogFolder, filenameDateFormat.format(date) + ".log");

        leaveLogFolder = new File(logsFolder, "Player Leave");
        leaveLogFile = new File(leaveLogFolder, filenameDateFormat.format(date) + ".log");

        reloadLogFolder = new File(logsFolder, "Reload");
        reloadLogFile = new File(reloadLogFolder, filenameDateFormat.format(date) + ".log");

        serverStartLogFolder = new File(logsFolder, "Server Start");
        serverStartLogFile = new File(serverStartLogFolder, filenameDateFormat.format(date) + ".log");

        serverStopLogFolder = new File(logsFolder, "Server Stop");
        serverStopLogFile = new File(serverStopLogFolder, filenameDateFormat.format(date) + ".log");

        ramLogFolder = new File(logsFolder, "RAM");
        ramLogFile = new File(ramLogFolder, filenameDateFormat.format(date) + ".log");

        serverSwitchLogFolder = new File(logsFolder, "Server Switch");
        serverSwitchLogFile = new File(serverSwitchLogFolder, filenameDateFormat.format(date) + ".log");

        liteBansLogFolder = new File(logsFolder, "LiteBans");

        final File liteBansBansLogFolder = new File(liteBansLogFolder, "Bans");
        liteBansBansLogFile = new File(liteBansBansLogFolder, filenameDateFormat.format(date) + ".log");

        final File liteBansMuteLogFolder = new File(liteBansLogFolder, "Mutes");
        liteBansMuteLogFile = new File(liteBansMuteLogFolder, filenameDateFormat.format(date) + ".log");

        final File liteBansKickLogFolder = new File(liteBansLogFolder, "Kick");
        liteBansKickLogFile = new File(liteBansKickLogFolder, filenameDateFormat.format(date) + ".log");

        final File pafFriendMessageLogFolder = new File(logsFolder, "PAF Friend Message");
        pafFriendMessageLogFile = new File(pafFriendMessageLogFolder, filenameDateFormat.format(date) + ".log");

        final File pafPartyMessageLogFolder = new File(logsFolder, "PAF Party Message");
        pafPartyMessageLogFile = new File(pafPartyMessageLogFolder, filenameDateFormat.format(date) + ".log");

        try {

            if (Data.isStaffEnabled) staffLogFolder.mkdir();
            chatLogFolder.mkdir();
            commandLogFolder.mkdir();
            loginLogFolder.mkdir();
            leaveLogFolder.mkdir();
            reloadLogFolder.mkdir();
            serverStartLogFolder.mkdir();
            serverStopLogFolder.mkdir();
            ramLogFolder.mkdir();
            serverSwitchLogFolder.mkdir();
            if (LiteBanUtil.getLiteBansAPI() != null) {
                liteBansLogFolder.mkdir();
                liteBansBansLogFolder.mkdir();
                liteBansMuteLogFolder.mkdir();
                liteBansKickLogFolder.mkdir();
            }
            if (PartyAndFriendsUtil.getPartyAndFriendsAPI() != null) {
                pafFriendMessageLogFolder.mkdir();
                pafPartyMessageLogFolder.mkdir();
            }

            if (Data.isStaffEnabled) staffLogFile.createNewFile();
            chatLogFile.createNewFile();
            commandLogFile.createNewFile();
            loginLogFile.createNewFile();
            leaveLogFile.createNewFile();
            reloadLogFile.createNewFile();
            serverStartLogFile.createNewFile();
            serverStopLogFile.createNewFile();
            ramLogFile.createNewFile();
            serverSwitchLogFile.createNewFile();
            if (LiteBanUtil.getLiteBansAPI() != null) {
                liteBansBansLogFile.createNewFile();
                liteBansMuteLogFile.createNewFile();
                liteBansKickLogFile.createNewFile();
            }
            if (PartyAndFriendsUtil.getPartyAndFriendsAPI() != null) {
                pafFriendMessageLogFile.createNewFile();
                pafPartyMessageLogFile.createNewFile();
            }

        } catch (final IOException e) { e.printStackTrace(); }
    }

    public static File getStaffLogFile() { return staffLogFile; }

    public static File getChatLogFile() { return chatLogFile; }

    public static File getCommandLogFile() { return commandLogFile; }

    public static File getLoginLogFile() { return loginLogFile; }

    public static File getLeaveLogFile() { return leaveLogFile; }

    public static File getReloadLogFile() { return reloadLogFile; }

    public static File getServerStartLogFile() { return serverStartLogFile; }

    public static File getServerStopLogFile() { return serverStopLogFile; }

    public static File getRamLogFile() { return ramLogFile; }

    public static File getServerSwitchLogFile() { return serverSwitchLogFile; }

    public static File getLiteBansBansLogFile() { return liteBansBansLogFile; }

    public static File getLiteBansMuteLogFile() { return liteBansMuteLogFile; }

    public static File getLiteBansKickLogFile() { return liteBansKickLogFile; }

    public static File getPafFriendMessageLogFile() { return pafFriendMessageLogFile; }

    public static File getPafPartyMessageLogFile() { return pafPartyMessageLogFile; }

    private void deleteFile(File file) {

        if (fileDeletion <= 0 ) return;

        FileTime creationTime = null;

        try {

            creationTime = (FileTime) Files.getAttribute(file.toPath(), "creationTime");

        } catch (final IOException e) { e.printStackTrace(); }

        assert creationTime != null;
        final long offset = System.currentTimeMillis() - creationTime.toMillis();
        final long fileDeletionDays = fileDeletion;
        final long maxAge = TimeUnit.DAYS.toMillis(fileDeletionDays);

        if (offset > maxAge) file.delete();
    }

    public void deleteFiles() {

        if (fileDeletion<= 0 ) { return; }

        if (Data.isStaffEnabled)
            for (File staffLog : staffLogFolder.listFiles())
                this.deleteFile(staffLog);

        for (File chatLog : chatLogFolder.listFiles())
            this.deleteFile(chatLog);

        for (File commandsLog : commandLogFolder.listFiles())
            this.deleteFile(commandsLog);

        for (File loginLog : loginLogFolder.listFiles())
            this.deleteFile(loginLog);

        for (File leaveLog : leaveLogFolder.listFiles())
            this.deleteFile(leaveLog);

        for (File reloadLog : reloadLogFolder.listFiles())
            this.deleteFile(reloadLog);

        for (File serverStartLog : serverStartLogFolder.listFiles())
            this.deleteFile(serverStartLog);

        for (File serverStopLog : serverStopLogFolder.listFiles())
            this.deleteFile(serverStopLog);

        for (File ramLog : ramLogFolder.listFiles())
            this.deleteFile(ramLog);

        for (File serverSwitchLog : serverSwitchLogFolder.listFiles())
            this.deleteFile(serverSwitchLog);

        if (LiteBanUtil.getLiteBansAPI() != null)
            for (File liteBansLog : liteBansLogFolder.listFiles())
                this.deleteFile(liteBansLog);

        if (PartyAndFriendsUtil.getPartyAndFriendsAPI() != null) {
            for (File pafFriendMessage : pafFriendMessageLogFolder.listFiles())
                this.deleteFile(pafFriendMessage);

            for (File pafPartyMessage : pafPartyMessageLogFolder.listFiles())
                this.deleteFile(pafPartyMessage);
        }
    }
}