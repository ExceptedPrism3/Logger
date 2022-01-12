package com.carpour.loggervelocity.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class FileHandler {

    private final ConfigManager config = new ConfigManager();

    private static File staffLogFolder;
    private static File chatLogFolder;
    private static File playerCommandLogFolder;
    private static File loginLogFolder;
    private static File leaveLogFolder;
    private static File consoleCommandLogFolder;
    private static File serverStartLogFolder;
    private static File serverStopLogFolder;
    private static File ramLogFolder;

    private static File staffLogFile;
    private static File chatLogFile;
    private static File playerCommandLogFile;
    private static File loginLogFile;
    private static File leaveLogFile;
    private static File consoleCommandLogFile;
    private static File serverStartLogFile;
    private static File serverStopLogFile;
    private static File ramLogFile;

    public FileHandler(File dataFolder) {

        dataFolder.mkdir();

        File logsFolder = new File(dataFolder, "Logs");
        logsFolder.mkdirs();

        Date date = new Date();
        SimpleDateFormat filenameDateFormat = new SimpleDateFormat("dd-MM-yyyy");


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

        try {

            if (config.getBoolean("Staff.Enabled")) staffLogFolder.mkdir();
            chatLogFolder.mkdir();
            playerCommandLogFolder.mkdir();
            loginLogFolder.mkdir();
            leaveLogFolder.mkdir();
            consoleCommandLogFolder.mkdir();
            serverStartLogFolder.mkdir();
            serverStopLogFolder.mkdir();
            ramLogFolder.mkdir();

            if (config.getBoolean("Staff.Enabled")) staffLogFile.createNewFile();
            chatLogFile.createNewFile();
            playerCommandLogFile.createNewFile();
            loginLogFile.createNewFile();
            leaveLogFile.createNewFile();
            consoleCommandLogFile.createNewFile();
            serverStartLogFile.createNewFile();
            serverStopLogFile.createNewFile();
            ramLogFile.createNewFile();

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


    public void deleteFile(File file) {

        if (config.getInt("File-Deletion") <= 0 ){ return; }

        FileTime creationTime = null;

        try {

            creationTime = (FileTime) Files.getAttribute(file.toPath(), "creationTime");

        } catch (IOException e) { e.printStackTrace(); }

        assert creationTime != null;
        long offset = System.currentTimeMillis() - creationTime.toMillis();
        long fileDeletionDays = config.getInt("File-Deletion");
        long maxAge = TimeUnit.DAYS.toMillis(fileDeletionDays);

        if(offset > maxAge) file.delete();

    }

    public void deleteFiles(){

        if (config.getInt("File-Deletion") <= 0 ) return;

        if (config.getBoolean("Staff.Enabled")) {

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

        for (File consolecommandsLog : Objects.requireNonNull(consoleCommandLogFolder.listFiles()))
        {

            deleteFile(consolecommandsLog);

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
    }
}
