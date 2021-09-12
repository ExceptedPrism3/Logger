package com.carpour.logger.Utils;

import com.carpour.logger.Main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class FileHandler {


    private static File chatLogFolder;
    private static File commandLogFolder;
    private static File consoleLogFolder;
    private static File signLogFolder;
    private static File playerJoinLogFolder;
    private static File playerLeaveLogFolder;
    private static File playerDeathLogFolder;
    private static File playerTeleportLogFolder;
    private static File blockPlaceLogFolder;
    private static File blockBreakLogFolder;
    private static File TPSLogFolder;
    private static File RAMLogFolder;
    private static File playerKickLogFolder;
    private static File playerLevelFolder;
    private static File portalCreateFolder;
    private static File bucketPlaceFolder;
    private static File anvilFolder;
    private static File staffFolder;
    private static File serverStartFolder;
    private static File serverStopFolder;
    private static File itemDropFolder;
    private static File enchantFolder;
    private static File afkFolder;

    private static File chatLogFile;
    private static File commandLogFile;
    private static File consoleLogFile;
    private static File signLogFile;
    private static File playerJoinLogFile;
    private static File playerLeaveLogFile;
    private static File playerDeathLogFile;
    private static File playerTeleportLogFile;
    private static File blockPlaceLogFile;
    private static File blockBreakLogFile;
    private static File TPSLogFile;
    private static File RAMLogFile;
    private static File playerKickLogFile;
    private static File playerLevelFile;
    private static File portalCreateFile;
    private static File bucketPlaceFile;
    private static File anvilFile;
    private static File StaffFile;
    private static File ServerStartFile;
    private static File ServerStopFile;
    private static File itemDropFile;
    private static File EnchantFile;
    private static File afkFile;


    private final Main main = Main.getInstance();

    public FileHandler(File dataFolder) {

        dataFolder.mkdir();

        File logsFolder = new File(dataFolder, "Logs");
        logsFolder.mkdirs();

        Date date = new Date();
        SimpleDateFormat filenameDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        chatLogFolder = new File(logsFolder, "Player Chat");
        chatLogFile = new File(chatLogFolder, filenameDateFormat.format(date) + ".log");

        commandLogFolder = new File(logsFolder, "Player Commands");
        commandLogFile = new File(commandLogFolder, filenameDateFormat.format(date) + ".log");

        consoleLogFolder = new File(logsFolder, "Console Commands");
        consoleLogFile = new File(consoleLogFolder, filenameDateFormat.format(date) + ".log");

        signLogFolder = new File(logsFolder, "Player Sign Text");
        signLogFile = new File(signLogFolder, filenameDateFormat.format(date) + ".log");

        playerJoinLogFolder = new File(logsFolder, "Player Join");
        playerJoinLogFile = new File(playerJoinLogFolder, filenameDateFormat.format(date) + ".log");

        playerLeaveLogFolder = new File(logsFolder, "Player Leave");
        playerLeaveLogFile = new File(playerLeaveLogFolder, filenameDateFormat.format(date) + ".log");

        playerDeathLogFolder = new File(logsFolder, "Player Death");
        playerDeathLogFile = new File(playerDeathLogFolder, filenameDateFormat.format(date) + ".log");

        playerTeleportLogFolder = new File(logsFolder, "Player Teleport");
        playerTeleportLogFile = new File(playerTeleportLogFolder, filenameDateFormat.format(date) + ".log");

        blockPlaceLogFolder = new File(logsFolder, "Block Place");
        blockPlaceLogFile = new File(blockPlaceLogFolder, filenameDateFormat.format(date) + ".log");

        blockBreakLogFolder = new File(logsFolder, "Block Break");
        blockBreakLogFile = new File(blockBreakLogFolder, filenameDateFormat.format(date) + ".log");

        TPSLogFolder = new File(logsFolder, "TPS");
        TPSLogFile = new File(TPSLogFolder, filenameDateFormat.format(date) + ".log");

        RAMLogFolder = new File(logsFolder, "RAM");
        RAMLogFile = new File(RAMLogFolder, filenameDateFormat.format(date) + ".log");

        playerKickLogFolder = new File(logsFolder, "Player Kick");
        playerKickLogFile = new File(playerKickLogFolder, filenameDateFormat.format(date) + ".log");

        playerLevelFolder = new File(logsFolder, "Player Level");
        playerLevelFile = new File(playerLevelFolder, filenameDateFormat.format(date) + ".log");

        portalCreateFolder = new File(logsFolder, "Portal Creation");
        portalCreateFile = new File(portalCreateFolder, filenameDateFormat.format(date) + ".log");

        bucketPlaceFolder = new File(logsFolder, "Bucket Place");
        bucketPlaceFile = new File(bucketPlaceFolder, filenameDateFormat.format(date) + ".log");

        anvilFolder = new File(logsFolder, "Anvil");
        anvilFile = new File(anvilFolder, filenameDateFormat.format(date) + ".log");

        staffFolder = new File(logsFolder, "Staff");
        StaffFile = new File(staffFolder, filenameDateFormat.format(date) + ".log");

        serverStartFolder = new File(logsFolder, "Server Start");
        ServerStartFile = new File(serverStartFolder, filenameDateFormat.format(date) + ".log");

        serverStopFolder = new File(logsFolder, "Server Stop");
        ServerStopFile = new File(serverStopFolder, filenameDateFormat.format(date) + ".log");

        itemDropFolder = new File(logsFolder, "Item Drop");
        itemDropFile = new File(itemDropFolder, filenameDateFormat.format(date) + ".log");

        enchantFolder = new File(logsFolder, "Enchanting");
        EnchantFile = new File(enchantFolder, filenameDateFormat.format(date) + ".log");

        afkFolder = new File(logsFolder, "AFK");
        afkFile = new File(afkFolder, filenameDateFormat.format(date) + ".log");

        try {

            chatLogFolder.mkdir();

            commandLogFolder.mkdir();

            consoleLogFolder.mkdir();

            signLogFolder.mkdir();

            playerJoinLogFolder.mkdir();

            playerLeaveLogFolder.mkdir();

            playerDeathLogFolder.mkdir();

            playerTeleportLogFolder.mkdir();

            blockPlaceLogFolder.mkdir();

            blockBreakLogFolder.mkdir();

            TPSLogFolder.mkdir();

            RAMLogFolder.mkdir();

            playerKickLogFolder.mkdir();

            playerLevelFolder.mkdir();

            portalCreateFolder.mkdir();

            bucketPlaceFolder.mkdir();

            anvilFolder.mkdir();

            if (main.getConfig().getBoolean("Staff.Enabled")) staffFolder.mkdir();

            serverStartFolder.mkdir();

            serverStopFolder.mkdir();

            itemDropFolder.mkdir();

            enchantFolder.mkdir();

            if (main.getAPI() != null) afkFolder.mkdir();



            chatLogFile.createNewFile();

            commandLogFile.createNewFile();

            consoleLogFile.createNewFile();

            signLogFile.createNewFile();

            playerJoinLogFile.createNewFile();

            playerLeaveLogFile.createNewFile();

            playerDeathLogFile.createNewFile();

            playerTeleportLogFile.createNewFile();

            blockPlaceLogFile.createNewFile();

            blockBreakLogFile.createNewFile();

            TPSLogFile.createNewFile();

            RAMLogFile.createNewFile();

            playerKickLogFile.createNewFile();

            playerLevelFile.createNewFile();

            portalCreateFile.createNewFile();

            anvilFile.createNewFile();

            bucketPlaceFile.createNewFile();

            if (main.getConfig().getBoolean("Staff.Enabled")) StaffFile.createNewFile();

            ServerStartFile.createNewFile();

            ServerStartFile.createNewFile();

            itemDropFile.createNewFile();

            EnchantFile.createNewFile();

            if (main.getAPI() != null) afkFile.createNewFile();



        } catch (IOException e) {

            e.printStackTrace();

        }
    }

    public static File getChatLogFile() { return chatLogFile; }

    public static File getCommandLogFile() { return commandLogFile; }

    public static File getConsoleLogFile() { return consoleLogFile; }

    public static File getSignLogFile() { return signLogFile; }

    public static File getPlayerJoinLogFile() { return playerJoinLogFile; }

    public static File getPlayerLeaveLogFile() { return playerLeaveLogFile; }

    public static File getPlayerDeathLogFile() { return playerDeathLogFile; }

    public static File getPlayerTeleportLogFile() { return playerTeleportLogFile; }

    public static File getBlockPlaceLogFile() { return blockPlaceLogFile; }

    public static File getBlockBreakLogFile() { return blockBreakLogFile; }

    public static File getTPSLogFile() { return TPSLogFile; }

    public static File getRAMLogFile() { return RAMLogFile; }

    public static File getPlayerKickLogFile() { return playerKickLogFile; }

    public static File getPlayerLevelFile() { return playerLevelFile; }

    public static File getPortalCreateFile() { return portalCreateFile; }

    public static File getBucketPlaceFile() { return bucketPlaceFile; }

    public static File getAnvilFile() { return anvilFile; }

    public static File getStaffFile() { return StaffFile; }

    public static File getServerStartFile() { return ServerStartFile; }

    public static File getServerStopFile() { return ServerStopFile; }

    public static File getItemDropFile() { return itemDropFile; }

    public static File getEnchantFile() { return EnchantFile; }

    public static File getAfkFile() { return afkFile; }


    public void deleteFile(File file) {

        if (main.getConfig().getInt("File-Deletion") < 0 ){ return; }

        FileTime creationTime = null;

        try {

            creationTime = (FileTime) Files.getAttribute(file.toPath(), "creationTime");

        } catch (IOException e) {

            e.printStackTrace();
        }

        assert creationTime != null;
        long offset = System.currentTimeMillis() - creationTime.toMillis();
        long fileDeletionDays = main.getConfig().getLong("File-Deletion");
        long maxAge = TimeUnit.DAYS.toMillis(fileDeletionDays);

        if(offset > maxAge) {

            file.delete();

        }
    }

    public void deleteFiles(){

        if (main.getConfig().getInt("File-Deletion") < 0 ){ return; }

        for (File chatLog : Objects.requireNonNull(chatLogFolder.listFiles()))
        {

                deleteFile(chatLog);

        }

        for (File commandLog : Objects.requireNonNull(commandLogFolder.listFiles()))
        {

            deleteFile(commandLog);

        }

        for (File consoleLog : Objects.requireNonNull(consoleLogFolder.listFiles()))
        {

            deleteFile(consoleLog);

        }

        for (File signLog : Objects.requireNonNull(signLogFolder.listFiles()))
        {

            deleteFile(signLog);

        }

        for (File playerJoinLog : Objects.requireNonNull(playerJoinLogFolder.listFiles()))
        {

            deleteFile(playerJoinLog);

        }

        for (File playerLeaveLog : Objects.requireNonNull(playerLeaveLogFolder.listFiles()))
        {

            deleteFile(playerLeaveLog);

        }

        for (File playerDeathLog : Objects.requireNonNull(playerDeathLogFolder.listFiles()))
        {

            deleteFile(playerDeathLog);

        }

        for (File playerTeleportLog : Objects.requireNonNull(playerTeleportLogFolder.listFiles()))
        {

            deleteFile(playerTeleportLog);

        }

        for (File blockPlaceLog : Objects.requireNonNull(blockPlaceLogFolder.listFiles()))
        {

            deleteFile(blockPlaceLog);

        }

        for (File blockBreakLog : Objects.requireNonNull(blockBreakLogFolder.listFiles()))
        {

            deleteFile(blockBreakLog);

        }

        for (File TPSLog : Objects.requireNonNull(TPSLogFolder.listFiles()))
        {

            deleteFile(TPSLog);

        }

        for (File RAMLog : Objects.requireNonNull(RAMLogFolder.listFiles()))
        {

            deleteFile(RAMLog);

        }

        for (File kickLog : Objects.requireNonNull(playerKickLogFolder.listFiles()))
        {

            deleteFile(kickLog);

        }

        for (File playerLevelLog : Objects.requireNonNull(playerLevelFolder.listFiles()))
        {

            deleteFile(playerLevelLog);

        }

        for (File portalCreate : Objects.requireNonNull(portalCreateFolder.listFiles()))
        {

            deleteFile(portalCreate);

        }

        for (File bucketPlace : Objects.requireNonNull(bucketPlaceFolder.listFiles()))
        {

            deleteFile(bucketPlace);

        }

        for (File anvil : Objects.requireNonNull(anvilFolder.listFiles()))
        {

            deleteFile(anvil);

        }


        if (main.getConfig().getBoolean("Staff.Enabled")) {
            for (File Staff : Objects.requireNonNull(staffFolder.listFiles())) {

                deleteFile(Staff);

            }
        }

        for (File serverStart : Objects.requireNonNull(serverStartFolder.listFiles()))
        {

            deleteFile(serverStart);

        }

        for (File serverStop : Objects.requireNonNull(serverStopFolder.listFiles()))
        {

            deleteFile(serverStop);

        }

        for (File itemDrop : Objects.requireNonNull(itemDropFolder.listFiles()))
        {

            deleteFile(itemDrop);

        }

        for (File enchanting : Objects.requireNonNull(enchantFolder.listFiles()))
        {

            deleteFile(enchanting);

        }

        if (main.getAPI() != null) {
            for (File afk : Objects.requireNonNull(afkFolder.listFiles())) {

                deleteFile(afk);

            }
        }
    }
}