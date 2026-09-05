package me.prism3.loggervelocity.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        final File logsFolder = new File(dataFolder, "Logs");

        final Date date = new Date();
        final SimpleDateFormat filenameDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        final String dateStr = filenameDateFormat.format(date);

        staffLogFolder = new File(logsFolder, "Staff");
        staffLogFile = new File(staffLogFolder, dateStr + ".log");

        chatLogFolder = new File(logsFolder, "Player Chat");
        chatLogFile = new File(chatLogFolder, dateStr + ".log");

        playerCommandLogFolder = new File(logsFolder, "Player Command");
        playerCommandLogFile = new File(playerCommandLogFolder, dateStr + ".log");

        loginLogFolder = new File(logsFolder, "Player Login");
        loginLogFile = new File(loginLogFolder, dateStr + ".log");

        leaveLogFolder = new File(logsFolder, "Player Leave");
        leaveLogFile = new File(leaveLogFolder, dateStr + ".log");

        consoleCommandLogFolder = new File(logsFolder, "Server Commands");
        consoleCommandLogFile = new File(consoleCommandLogFolder, dateStr + ".log");

        serverStartLogFolder = new File(logsFolder, "Server Start");
        serverStartLogFile = new File(serverStartLogFolder, dateStr + ".log");

        serverStopLogFolder = new File(logsFolder, "Server Stop");
        serverStopLogFile = new File(serverStopLogFolder, dateStr + ".log");

        ramLogFolder = new File(logsFolder, "RAM");
        ramLogFile = new File(ramLogFolder, dateStr + ".log");
    }

    public static File getStaffLogFile() {
        return staffLogFile;
    }

    public static File getChatLogFile() {
        return chatLogFile;
    }

    public static File getPlayerCommandLogFile() {
        return playerCommandLogFile;
    }

    public static File getLoginLogFile() {
        return loginLogFile;
    }

    public static File getLeaveLogFile() {
        return leaveLogFile;
    }

    public static File getConsoleCommandLogFile() {
        return consoleCommandLogFile;
    }

    public static File getServerStartLogFile() {
        return serverStartLogFile;
    }

    public static File getServerStopLogFile() {
        return serverStopLogFile;
    }

    public static File getRamLogFile() {
        return ramLogFile;
    }

    private void deleteFile(File file) {
        if (fileDeletion <= 0 || file == null || !file.exists()) {
            return;
        }

        try {
            FileTime creationTime = (FileTime) Files.getAttribute(file.toPath(), "creationTime");
            if (creationTime != null) {
                final long offset = System.currentTimeMillis() - creationTime.toMillis();
                final long maxAge = TimeUnit.DAYS.toMillis(fileDeletion);
                if (offset > maxAge) {
                    file.delete();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteFilesInFolder(File folder) {
        if (folder != null && folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File f : files) {
                    this.deleteFile(f);
                }
            }
        }
    }

    public void deleteFiles() {
        if (fileDeletion <= 0)
            return;

        if (isStaffEnabled) {
            deleteFilesInFolder(staffLogFolder);
        }

        deleteFilesInFolder(chatLogFolder);
        deleteFilesInFolder(playerCommandLogFolder);
        deleteFilesInFolder(loginLogFolder);
        deleteFilesInFolder(leaveLogFolder);
        deleteFilesInFolder(consoleCommandLogFolder);
        deleteFilesInFolder(serverStartLogFolder);
        deleteFilesInFolder(serverStopLogFolder);
        deleteFilesInFolder(ramLogFolder);
    }

    public static void logToFile(String eventType, String message) {
        if (!Data.isLogToFiles) {
            return;
        }

        File file;
        switch (eventType) {
            case "Player-Chat":
                file = chatLogFile;
                break;
            case "Player-Chat-Staff":
                file = staffLogFile;
                break;
            case "Player-Command":
                file = playerCommandLogFile;
                break;
            case "Player-Command-Staff":
                file = staffLogFile;
                break;
            case "Player-Login":
                file = loginLogFile;
                break;
            case "Player-Login-Staff":
                file = staffLogFile;
                break;
            case "Player-Leave":
                file = leaveLogFile;
                break;
            case "Player-Leave-Staff":
                file = staffLogFile;
                break;
            case "Server-Side.Console-Commands":
            case "Server-Side.Manual-Log":
                file = consoleCommandLogFile;
                break;
            case "Server-Side.Start":
                file = serverStartLogFile;
                break;
            case "Server-Side.Stop":
                file = serverStopLogFile;
                break;
            case "Server-Side.RAM":
                file = ramLogFile;
                break;
            default:
                return;
        }

        if (file == null)
            return;

        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        try (java.io.BufferedWriter out = new java.io.BufferedWriter(new java.io.FileWriter(file, true))) {
            out.write(message);
            out.newLine();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}
