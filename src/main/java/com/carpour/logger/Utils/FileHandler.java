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
    private static File StaffFolder;
    private static File ServerStartFolder;
    private static File ServerStopFolder;
    private static File itemDropFolder;
    private static File EnchantFolder;

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


    private final Main main = Main.getInstance();

    public FileHandler(File dataFolder) {
        dataFolder.mkdir();

        Date date = new Date();
        SimpleDateFormat filenameDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        chatLogFolder = new File(dataFolder, "Player Chat");
        chatLogFile = new File(chatLogFolder, filenameDateFormat.format(date) + ".log");

        commandLogFolder = new File(dataFolder, "Player Commands");
        commandLogFile = new File(commandLogFolder, filenameDateFormat.format(date) + ".log");

        consoleLogFolder = new File(dataFolder, "Console Commands");
        consoleLogFile = new File(consoleLogFolder, filenameDateFormat.format(date) + ".log");

        signLogFolder = new File(dataFolder, "Player Sign Text");
        signLogFile = new File(signLogFolder, filenameDateFormat.format(date) + ".log");

        playerJoinLogFolder = new File(dataFolder, "Player Join");
        playerJoinLogFile = new File(playerJoinLogFolder, filenameDateFormat.format(date) + ".log");

        playerLeaveLogFolder = new File(dataFolder, "Player Leave");
        playerLeaveLogFile = new File(playerLeaveLogFolder, filenameDateFormat.format(date) + ".log");

        playerDeathLogFolder = new File(dataFolder, "Player Death");
        playerDeathLogFile = new File(playerDeathLogFolder, filenameDateFormat.format(date) + ".log");

        playerTeleportLogFolder = new File(dataFolder, "Player Teleport");
        playerTeleportLogFile = new File(playerTeleportLogFolder, filenameDateFormat.format(date) + ".log");

        blockPlaceLogFolder = new File(dataFolder, "Block Place");
        blockPlaceLogFile = new File(blockPlaceLogFolder, filenameDateFormat.format(date) + ".log");

        blockBreakLogFolder = new File(dataFolder, "Block Break");
        blockBreakLogFile = new File(blockBreakLogFolder, filenameDateFormat.format(date) + ".log");

        TPSLogFolder = new File(dataFolder, "TPS");
        TPSLogFile = new File(TPSLogFolder, filenameDateFormat.format(date) + ".log");

        RAMLogFolder = new File(dataFolder, "RAM");
        RAMLogFile = new File(RAMLogFolder, filenameDateFormat.format(date) + ".log");

        playerKickLogFolder = new File(dataFolder, "Player Kick");
        playerKickLogFile = new File(playerKickLogFolder, filenameDateFormat.format(date) + ".log");

        playerLevelFolder = new File(dataFolder, "Player Level");
        playerLevelFile = new File(playerLevelFolder, filenameDateFormat.format(date) + ".log");

        portalCreateFolder = new File(dataFolder, "Portal Creation");
        portalCreateFile = new File(portalCreateFolder, filenameDateFormat.format(date) + ".log");

        bucketPlaceFolder = new File(dataFolder, "Bucket Place");
        bucketPlaceFile = new File(bucketPlaceFolder, filenameDateFormat.format(date) + ".log");

        anvilFolder = new File(dataFolder, "Anvil");
        anvilFile = new File(anvilFolder, filenameDateFormat.format(date) + ".log");

        StaffFolder = new File(dataFolder, "Staff");
        StaffFile = new File(StaffFolder, filenameDateFormat.format(date) + ".log");

        ServerStartFolder = new File(dataFolder, "Server Start");
        ServerStartFile = new File(ServerStartFolder, filenameDateFormat.format(date) + ".log");

        ServerStopFolder = new File(dataFolder, "Server Stop");
        ServerStopFile = new File(ServerStopFolder, filenameDateFormat.format(date) + ".log");

        itemDropFolder = new File(dataFolder, "Item Drop");
        itemDropFile = new File(itemDropFolder, filenameDateFormat.format(date) + ".log");

        EnchantFolder = new File(dataFolder, "Enchanting");
        EnchantFile = new File(EnchantFolder, filenameDateFormat.format(date) + ".log");

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
            if (main.getConfig().getBoolean("Staff.Enabled")){

                StaffFolder.mkdir();

            }
            ServerStartFolder.mkdir();

            ServerStopFolder.mkdir();

            itemDropFolder.mkdir();

            EnchantFolder.mkdir();



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

            if (main.getConfig().getBoolean("Staff.Enabled")){
                StaffFile.createNewFile();
            }

            ServerStartFile.createNewFile();

            ServerStartFile.createNewFile();

            itemDropFile.createNewFile();

            EnchantFile.createNewFile();



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


    public void deleteFile(File file) {

        if (main.getConfig().getInt("File-Deletion") < 0 ){ return; }

        // CompletableFuture.runAsync(() -> {
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

        // });
    }

    public void deleteFiles(){

        if (main.getConfig().getInt("File-Deletion") < 0 ){ return; }

        for (File ChatLog : Objects.requireNonNull(chatLogFolder.listFiles()))
        {

                deleteFile(ChatLog);

        }

        for (File CommandLog : Objects.requireNonNull(commandLogFolder.listFiles()))
        {

            deleteFile(CommandLog);

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

        for (File BlockPlaceLog : Objects.requireNonNull(blockPlaceLogFolder.listFiles()))
        {

            deleteFile(BlockPlaceLog);

        }

        for (File BlockBreakLog : Objects.requireNonNull(blockBreakLogFolder.listFiles()))
        {

            deleteFile(BlockBreakLog);

        }

        for (File TPSLog : Objects.requireNonNull(TPSLogFolder.listFiles()))
        {

            deleteFile(TPSLog);

        }

        for (File RAMLog : Objects.requireNonNull(RAMLogFolder.listFiles()))
        {

            deleteFile(RAMLog);

        }

        for (File KickLog : Objects.requireNonNull(playerKickLogFolder.listFiles()))
        {

            deleteFile(KickLog);

        }

        for (File PlayerLevelLog : Objects.requireNonNull(playerLevelFolder.listFiles()))
        {

            deleteFile(PlayerLevelLog);

        }

        for (File PortalCreate : Objects.requireNonNull(portalCreateFolder.listFiles()))
        {

            deleteFile(PortalCreate);

        }

        for (File BucketPlace : Objects.requireNonNull(bucketPlaceFolder.listFiles()))
        {

            deleteFile(BucketPlace);

        }

        for (File Anvil : Objects.requireNonNull(anvilFolder.listFiles()))
        {

            deleteFile(Anvil);

        }


        if (main.getConfig().getBoolean("Staff.Enabled")) {
            for (File Staff : Objects.requireNonNull(StaffFolder.listFiles())) {

                deleteFile(Staff);

            }
        }

        for (File ServerStart : Objects.requireNonNull(ServerStartFolder.listFiles()))
        {

            deleteFile(ServerStart);

        }

        for (File ServerStop : Objects.requireNonNull(ServerStopFolder.listFiles()))
        {

            deleteFile(ServerStop);

        }

        for (File ItemDrop : Objects.requireNonNull(itemDropFolder.listFiles()))
        {

            deleteFile(ItemDrop);

        }

        for (File Enchanting : Objects.requireNonNull(EnchantFolder.listFiles()))
        {

            deleteFile(Enchanting);

        }

    }

}