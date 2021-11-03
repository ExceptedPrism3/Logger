package com.carpour.loggerbungeecord.Utils;

import com.carpour.loggerbungeecord.Main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class FileHandler {

    ConfigManager cm = Main.getConfig();

    private static File staffLogFolder;
    private static File chatLogFolder;
    private static File loginLogFolder;
    private static File leaveLogFolder;
//    private static File switchLogFolder;
    private static File reloadLogFolder;
    private static File serverStartLogFolder;
    private static File serverStopLogFolder;

    private static File staffLogFile;
    private static File chatLogFile;
    private static File loginLogFile;
    private static File leaveLogFile;
//    private static File switchLogFile;
    private static File reloadLogFile;
    private static File serverStartLogFile;
    private static File serverStopLogFile;

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

        loginLogFolder = new File(logsFolder, "Player Login");
        loginLogFile = new File(loginLogFolder, filenameDateFormat.format(date) + ".log");

        leaveLogFolder = new File(logsFolder, "Player Leave");
        leaveLogFile = new File(leaveLogFolder, filenameDateFormat.format(date) + ".log");

//        switchLogFolder = new File(logsFolder, "Player Switch");
//        switchLogFile = new File(switchLogFolder, filenameDateFormat.format(date) + ".log");

        reloadLogFolder = new File(logsFolder, "Reload");
        reloadLogFile = new File(reloadLogFolder, filenameDateFormat.format(date) + ".log");

        serverStartLogFolder = new File(logsFolder, "Server Start");
        serverStartLogFile = new File(serverStartLogFolder, filenameDateFormat.format(date) + ".log");

        serverStopLogFolder = new File(logsFolder, "Server Stop");
        serverStopLogFile = new File(serverStopLogFolder, filenameDateFormat.format(date) + ".log");

        try {

            staffLogFolder.mkdir();
            chatLogFolder.mkdir();
            loginLogFolder.mkdir();
            leaveLogFolder.mkdir();
//            switchLogFolder.mkdir();
            reloadLogFolder.mkdir();
            serverStartLogFolder.mkdir();
            serverStopLogFolder.mkdir();

            staffLogFile.createNewFile();
            chatLogFile.createNewFile();
            loginLogFile.createNewFile();
            leaveLogFile.createNewFile();
//            switchLogFile.createNewFile();
            reloadLogFile.createNewFile();
            serverStartLogFile.createNewFile();
            serverStopLogFile.createNewFile();



        } catch (IOException e) {

            e.printStackTrace();

        }
    }

    public static File getStaffLogFile() { return staffLogFile; }

    public static File getChatLogFile() { return chatLogFile; }

    public static File getLoginLogFile() { return loginLogFile; }

    public static File getLeaveLogFile() { return leaveLogFile; }

//    public static File getSwtichLogFile() { return switchLogFile; }

    public static File getReloadLogFile() { return reloadLogFile; }

    public static File getServerStartLogFile() { return serverStartLogFile; }

    public static File getServerStopLogFile() { return serverStopLogFile; }


    public void deleteFile(File file) {

        if (cm.getInt("File-Deletion") <= 0 ){ return; }

        FileTime creationTime = null;

        try {

            creationTime = (FileTime) Files.getAttribute(file.toPath(), "creationTime");

        } catch (IOException e) {

            e.printStackTrace();
        }

        assert creationTime != null;
        long offset = System.currentTimeMillis() - creationTime.toMillis();
        long fileDeletionDays = cm.getLong("File-Deletion");
        long maxAge = TimeUnit.DAYS.toMillis(fileDeletionDays);

        if(offset > maxAge) {

            file.delete();

        }
    }

    public void deleteFiles(){

        if (cm.getInt("File-Deletion") <= 0 ){ return; }


        for (File staffLog : Objects.requireNonNull(staffLogFolder.listFiles()))
        {

            deleteFile(staffLog);

        }

        for (File chatLog : Objects.requireNonNull(chatLogFolder.listFiles()))
        {

            deleteFile(chatLog);

        }

        for (File loginLog : Objects.requireNonNull(loginLogFolder.listFiles()))
        {

            deleteFile(loginLog);

        }

        for (File leaveLog : Objects.requireNonNull(leaveLogFolder.listFiles()))
        {

            deleteFile(leaveLog);

        }

        /*for (File switchLog : Objects.requireNonNull(switchLogFolder.listFiles()))
        {

            deleteFile(switchLog);

        }*/

        for (File reloadLog : Objects.requireNonNull(reloadLogFolder.listFiles()))
        {

            deleteFile(reloadLog);

        }

        for (File serverStartLog : Objects.requireNonNull(serverStartLogFolder.listFiles()))
        {

            deleteFile(serverStartLog);

        }

        for (File serverStopLog : Objects.requireNonNull(serverStopLogFolder.listFiles()))
        {

            deleteFile(serverStopLog);

        }
    }
}