package me.prism3.loggervelocity.utils;

import me.prism3.loggervelocity.api.LiteBansUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static me.prism3.loggervelocity.utils.Data.fileDeletion;
import static me.prism3.loggervelocity.utils.Data.isStaffEnabled;

public class FileHandler {

    private static File staffLogFolder;
    private static File chatLogFolder;
    private static File playerCommandLogFolder;
    private static File loginLogFolder;
    private static File leaveLogFolder;
    private static File consoleCommandLogFolder;
    private static File serverStartLogFolder;
    private static File serverStopLogFolder;
    private static File ramLogFolder;
    private static File liteBansLogFolder;

    private static File staffLogFile;
    private static File chatLogFile;
    private static File playerCommandLogFile;
    private static File loginLogFile;
    private static File leaveLogFile;
    private static File consoleCommandLogFile;
    private static File serverStartLogFile;
    private static File serverStopLogFile;
    private static File ramLogFile;
    private static File liteBansBansLogFile;
    private static File liteBansMuteLogFile;
    private static File liteBansKickLogFile;

    public FileHandler(File dataFolder) {

        dataFolder.mkdir();

        final File logsFolder = new File(dataFolder, "Logs");
        logsFolder.mkdirs();

        final Date date = new Date();
        final SimpleDateFormat filenameDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        staffLogFolder = new File(logsFolder, "Staff");
        staffLogFile = new File(staffLogFolder, filenameDateFormat.format(date) + ".log");

        chatLogFolder = new File(logsFolder, "Player Chat");
        chatLogFile = new File(chatLogFolder, filenameDateFormat.format(date) + ".log");

        playerCommandLogFolder = new File(logsFolder, "Player Commands");
        playerCommandLogFile = new File(playerCommandLogFolder, filenameDateFormat.format(date) + ".log");

        loginLogFolder = new File(logsFolder, "Player Login");
        loginLogFile = new File(loginLogFolder, filenameDateFormat.format(date) + ".log");

        leaveLogFolder = new File(logsFolder, "Player Leave");
        leaveLogFile = new File(leaveLogFolder, filenameDateFormat.format(date) + ".log");

        consoleCommandLogFolder = new File(logsFolder, "Console Commands");
        consoleCommandLogFile = new File(consoleCommandLogFolder, filenameDateFormat.format(date) + ".log");

        serverStartLogFolder = new File(logsFolder, "Server Start");
        serverStartLogFile = new File(serverStartLogFolder, filenameDateFormat.format(date) + ".log");

        serverStopLogFolder = new File(logsFolder, "Server Stop");
        serverStopLogFile = new File(serverStopLogFolder, filenameDateFormat.format(date) + ".log");

        ramLogFolder = new File(logsFolder, "RAM");
        ramLogFile = new File(ramLogFolder, filenameDateFormat.format(date) + ".log");

        liteBansLogFolder = new File(logsFolder, "LiteBans");

        final File liteBansBansLogFolder = new File(liteBansLogFolder, "Bans");
        liteBansBansLogFile = new File(liteBansBansLogFolder, filenameDateFormat.format(date) + ".log");

        final File liteBansMuteLogFolder = new File(liteBansLogFolder, "Mutes");
        liteBansMuteLogFile = new File(liteBansMuteLogFolder, filenameDateFormat.format(date) + ".log");

        final File liteBansKickLogFolder = new File(liteBansLogFolder, "Kick");
        liteBansKickLogFile = new File(liteBansKickLogFolder, filenameDateFormat.format(date) + ".log");

        try {

            if (isStaffEnabled) staffLogFolder.mkdir();
            chatLogFolder.mkdir();
            playerCommandLogFolder.mkdir();
            loginLogFolder.mkdir();
            leaveLogFolder.mkdir();
            consoleCommandLogFolder.mkdir();
            serverStartLogFolder.mkdir();
            serverStopLogFolder.mkdir();
            ramLogFolder.mkdir();
            if (LiteBansUtil.getLiteBansAPI().isPresent()) {

                liteBansLogFolder.mkdir();
                liteBansBansLogFolder.mkdir();
                liteBansMuteLogFolder.mkdir();
                liteBansKickLogFolder.mkdir();
            }

            if (isStaffEnabled) staffLogFile.createNewFile();
            chatLogFile.createNewFile();
            playerCommandLogFile.createNewFile();
            loginLogFile.createNewFile();
            leaveLogFile.createNewFile();
            consoleCommandLogFile.createNewFile();
            serverStartLogFile.createNewFile();
            serverStopLogFile.createNewFile();
            ramLogFile.createNewFile();
            if (LiteBansUtil.getLiteBansAPI().isPresent()) {

                liteBansBansLogFile.createNewFile();
                liteBansMuteLogFile.createNewFile();
                liteBansKickLogFile.createNewFile();
            }

        } catch (IOException e) { e.printStackTrace(); }
    }

    public static File getStaffLogFile() { return staffLogFile; }

    public static File getChatLogFile() { return chatLogFile; }

    public static File getPlayerCommandLogFile() { return playerCommandLogFile; }

    public static File getLoginLogFile() { return loginLogFile; }

    public static File getLeaveLogFile() { return leaveLogFile; }

    public static File getConsoleCommandLogFile() { return consoleCommandLogFile; }

    public static File getServerStartLogFile() { return serverStartLogFile; }

    public static File getServerStopLogFile() { return serverStopLogFile; }

    public static File getRamLogFile() { return ramLogFile; }

    public static File getLiteBansBansLogFile() { return liteBansBansLogFile; }

    public static File getLiteBansMuteLogFile() { return liteBansMuteLogFile; }

    public static File getLiteBansKickLogFile() { return liteBansKickLogFile; }


    public void deleteFile(File file) {

        if (fileDeletion <= 0 ) { return; }

        FileTime creationTime = null;

        try {

            creationTime = (FileTime) Files.getAttribute(file.toPath(), "creationTime");

        } catch (IOException e) { e.printStackTrace(); }

        assert creationTime != null;
        final long offset = System.currentTimeMillis() - creationTime.toMillis();
        final long fileDeletionDays = fileDeletion;
        final long maxAge = TimeUnit.DAYS.toMillis(fileDeletionDays);

        if (offset > maxAge) file.delete();

    }

    public void deleteFiles() {

        if (fileDeletion <= 0 ) return;

        if (isStaffEnabled) {

            for (File staffLog : Objects.requireNonNull(staffLogFolder.listFiles())) {

                deleteFile(staffLog);

            }
        }

        for (File chatLog : Objects.requireNonNull(chatLogFolder.listFiles()))
        {

            deleteFile(chatLog);

        }

        for (File commandsLog : Objects.requireNonNull(playerCommandLogFolder.listFiles()))
        {

            deleteFile(commandsLog);

        }

        for (File loginLog : Objects.requireNonNull(loginLogFolder.listFiles()))
        {

            deleteFile(loginLog);

        }

        for (File leaveLog : Objects.requireNonNull(leaveLogFolder.listFiles()))
        {

            deleteFile(leaveLog);

        }

        for (File consoleCommandsLog : Objects.requireNonNull(consoleCommandLogFolder.listFiles()))
        {

            deleteFile(consoleCommandsLog);

        }

        for (File serverStartLog : Objects.requireNonNull(serverStartLogFolder.listFiles()))
        {

            deleteFile(serverStartLog);

        }

        for (File serverStopLog : Objects.requireNonNull(serverStopLogFolder.listFiles()))
        {

            deleteFile(serverStopLog);

        }

        for (File ramLog : Objects.requireNonNull(ramLogFolder.listFiles()))
        {

            deleteFile(ramLog);

        }

        if (LiteBansUtil.getLiteBansAPI().isPresent()) {

            for (File liteBansLog : Objects.requireNonNull(liteBansLogFolder.listFiles()))
            {

                deleteFile(liteBansLog);

            }
        }
    }
}
