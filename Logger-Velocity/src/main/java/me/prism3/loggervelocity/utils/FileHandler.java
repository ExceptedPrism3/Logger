package me.prism3.loggervelocity.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
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
    private static File switchLogFolder;
    private static File kickLogFolder;
    private static File consoleCommandLogFolder;
    private static File serverStartLogFolder;
    private static File serverStopLogFolder;
    private static File serverReloadLogFolder;
    private static File manualLogFolder;
    private static File ramLogFolder;

    public FileHandler(File dataFolder) {
        final File logsFolder = new File(dataFolder, "Logs");

        staffLogFolder = new File(logsFolder, "Staff");
        chatLogFolder = new File(logsFolder, "Player Chat");
        playerCommandLogFolder = new File(logsFolder, "Player Command");
        loginLogFolder = new File(logsFolder, "Player Login");
        leaveLogFolder = new File(logsFolder, "Player Leave");
        switchLogFolder = new File(logsFolder, "Player Switch");
        kickLogFolder = new File(logsFolder, "Player Kick");
        consoleCommandLogFolder = new File(logsFolder, "Server Commands");
        serverStartLogFolder = new File(logsFolder, "Server Start");
        serverStopLogFolder = new File(logsFolder, "Server Stop");
        serverReloadLogFolder = new File(logsFolder, "Server Reload");
        manualLogFolder = new File(logsFolder, "Manual Log");
        ramLogFolder = new File(logsFolder, "RAM");
    }

    private static String getTodayDateString() {
        return new SimpleDateFormat("dd-MM-yyyy").format(new Date());
    }

    private static File getDailyFile(File folder) {
        if (folder != null && !folder.exists()) {
            folder.mkdirs();
        }
        return new File(folder, getTodayDateString() + ".log");
    }

    public static File getStaffLogFile() {
        return getDailyFile(staffLogFolder);
    }

    public static File getChatLogFile() {
        return getDailyFile(chatLogFolder);
    }

    public static File getPlayerCommandLogFile() {
        return getDailyFile(playerCommandLogFolder);
    }

    public static File getLoginLogFile() {
        return getDailyFile(loginLogFolder);
    }

    public static File getLeaveLogFile() {
        return getDailyFile(leaveLogFolder);
    }

    public static File getSwitchLogFile() {
        return getDailyFile(switchLogFolder);
    }

    public static File getKickLogFile() {
        return getDailyFile(kickLogFolder);
    }

    public static File getConsoleCommandLogFile() {
        return getDailyFile(consoleCommandLogFolder);
    }

    public static File getServerStartLogFile() {
        return getDailyFile(serverStartLogFolder);
    }

    public static File getServerStopLogFile() {
        return getDailyFile(serverStopLogFolder);
    }

    public static File getServerReloadLogFile() {
        return getDailyFile(serverReloadLogFolder);
    }

    public static File getManualLogFile() {
        return getDailyFile(manualLogFolder);
    }

    public static File getRamLogFile() {
        return getDailyFile(ramLogFolder);
    }

    private void deleteFile(File file) {
        if (fileDeletion <= 0 || file == null || !file.exists()) {
            return;
        }

        final long offset = System.currentTimeMillis() - file.lastModified();
        final long maxAge = TimeUnit.DAYS.toMillis(fileDeletion);
        if (offset > maxAge) {
            file.delete();
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
        deleteFilesInFolder(switchLogFolder);
        deleteFilesInFolder(kickLogFolder);
        deleteFilesInFolder(consoleCommandLogFolder);
        deleteFilesInFolder(serverStartLogFolder);
        deleteFilesInFolder(serverStopLogFolder);
        deleteFilesInFolder(serverReloadLogFolder);
        deleteFilesInFolder(manualLogFolder);
        deleteFilesInFolder(ramLogFolder);
    }

    public static void logToFile(String eventType, String message) {
        if (!Data.isLogToFiles) {
            return;
        }

        File file;
        switch (eventType) {
            case "Player-Chat":
                file = getChatLogFile();
                break;
            case "Player-Chat-Staff":
                file = isStaffEnabled ? getStaffLogFile() : getChatLogFile();
                break;
            case "Player-Command":
                file = getPlayerCommandLogFile();
                break;
            case "Player-Command-Staff":
                file = isStaffEnabled ? getStaffLogFile() : getPlayerCommandLogFile();
                break;
            case "Player-Login":
                file = getLoginLogFile();
                break;
            case "Player-Login-Staff":
                file = isStaffEnabled ? getStaffLogFile() : getLoginLogFile();
                break;
            case "Player-Leave":
                file = getLeaveLogFile();
                break;
            case "Player-Leave-Staff":
                file = isStaffEnabled ? getStaffLogFile() : getLeaveLogFile();
                break;
            case "Player-Switch":
                file = getSwitchLogFile();
                break;
            case "Player-Switch-Staff":
                file = isStaffEnabled ? getStaffLogFile() : getSwitchLogFile();
                break;
            case "Player-Kick":
                file = getKickLogFile();
                break;
            case "Player-Kick-Staff":
                file = isStaffEnabled ? getStaffLogFile() : getKickLogFile();
                break;
            case "Server-Side.Server-Commands":
            case "Server-Side.Console-Commands":
                file = getConsoleCommandLogFile();
                break;
            case "Server-Side.Reload-Console":
                file = getServerReloadLogFile();
                break;
            case "Manual Log":
            case "Server-Side.Manual-Log":
                file = getManualLogFile();
                break;
            case "Server-Side.Start":
                file = getServerStartLogFile();
                break;
            case "Server-Side.Stop":
                file = getServerStopLogFile();
                break;
            case "Server-Side.RAM":
                file = getRamLogFile();
                break;
            default:
                return;
        }

        if (file == null)
            return;

        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), StandardCharsets.UTF_8))) {
            out.write(message);
            out.newLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
