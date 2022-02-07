package com.carpour.logger.Utils;

import com.carpour.logger.API.AuthMeUtil;
import com.carpour.logger.API.EssentialsUtil;
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

    // Folders Part
    // Player Side
    private static File chatLogFolder;
    private static File commandLogFolder;
    private static File signLogFolder;
    private static File playerJoinLogFolder;
    private static File playerLeaveLogFolder;
    private static File playerDeathLogFolder;
    private static File playerTeleportLogFolder;
    private static File blockPlaceLogFolder;
    private static File blockBreakLogFolder;
    private static File playerKickLogFolder;
    private static File playerLevelFolder;
    private static File bucketFillFolder;
    private static File bucketEmptyFolder;
    private static File anvilFolder;
    private static File staffFolder;
    private static File itemDropFolder;
    private static File enchantFolder;
    private static File bookEditingFolder;
    private static File itemPickupFolder;
    private static File furnaceFolder;
    private static File gameModeFolder;
    private static File craftFolder;

    // Server Side
    private static File serverStartFolder;
    private static File serverStopFolder;
    private static File consoleLogFolder;
    private static File RAMFolder;
    private static File TPSFolder;
    private static File portalCreateFolder;
    private static File rconFolder;

    // Extras Side Part
    private static File afkFolder;
    private static File wrongPasswordFolder;

    // Files Part
    // Player Side
    private static File chatLogFile;
    private static File commandLogFile;
    private static File signLogFile;
    private static File playerJoinLogFile;
    private static File playerLeaveLogFile;
    private static File playerDeathLogFile;
    private static File playerTeleportLogFile;
    private static File blockPlaceLogFile;
    private static File blockBreakLogFile;
    private static File playerKickLogFile;
    private static File playerLevelFile;
    private static File bucketFillFile;
    private static File bucketEmptyFile;
    private static File anvilFile;
    private static File staffFile;
    private static File itemDropFile;
    private static File enchantFile;
    private static File bookEditingFile;
    private static File itemPickupFile;
    private static File furnaceFile;
    private static File gameModeFile;
    private static File craftFile;

    // Server Side
    private static File serverStartFile;
    private static File serverStopFile;
    private static File consoleLogFile;
    private static File RAMFile;
    private static File TPSFile;
    private static File portalCreateFile;
    private static File rconFile;

    // Extras Side
    private static File afkFile;
    private static File wrongPasswordFile;


    private final Main main = Main.getInstance();

    public FileHandler(File dataFolder) {

        dataFolder.mkdir();

        File logsFolder = new File(dataFolder, "Logs");
        logsFolder.mkdirs();

        Date date = new Date();
        SimpleDateFormat filenameDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        // Player Side Part
        staffFolder = new File(logsFolder, "Staff");
        staffFile = new File(staffFolder, filenameDateFormat.format(date) + ".log");

        chatLogFolder = new File(logsFolder, "Player Chat");
        chatLogFile = new File(chatLogFolder, filenameDateFormat.format(date) + ".log");

        commandLogFolder = new File(logsFolder, "Player Commands");
        commandLogFile = new File(commandLogFolder, filenameDateFormat.format(date) + ".log");

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

        playerKickLogFolder = new File(logsFolder, "Player Kick");
        playerKickLogFile = new File(playerKickLogFolder, filenameDateFormat.format(date) + ".log");

        playerLevelFolder = new File(logsFolder, "Player Level");
        playerLevelFile = new File(playerLevelFolder, filenameDateFormat.format(date) + ".log");

        bucketFillFolder = new File(logsFolder, "Bucket Fill");
        bucketFillFile = new File(bucketFillFolder, filenameDateFormat.format(date) + ".log");

        bucketEmptyFolder = new File(logsFolder, "Bucket Empty");
        bucketEmptyFile = new File(bucketEmptyFolder, filenameDateFormat.format(date) + ".log");

        anvilFolder = new File(logsFolder, "Anvil");
        anvilFile = new File(anvilFolder, filenameDateFormat.format(date) + ".log");

        itemDropFolder = new File(logsFolder, "Item Drop");
        itemDropFile = new File(itemDropFolder, filenameDateFormat.format(date) + ".log");

        enchantFolder = new File(logsFolder, "Enchanting");
        enchantFile = new File(enchantFolder, filenameDateFormat.format(date) + ".log");

        bookEditingFolder = new File(logsFolder, "Book Editing");
        bookEditingFile = new File(bookEditingFolder, filenameDateFormat.format(date) + ".log");

        itemPickupFolder = new File(logsFolder, "Item Pickup");
        itemPickupFile = new File(itemPickupFolder, filenameDateFormat.format(date) + ".log");

        furnaceFolder = new File(logsFolder, "Furnace");
        furnaceFile = new File(furnaceFolder, filenameDateFormat.format(date) + ".log");

        gameModeFolder = new File(logsFolder, "Game Mode");
        gameModeFile = new File(gameModeFolder, filenameDateFormat.format(date) + ".log");

        craftFolder = new File(logsFolder, "Crafting");
        craftFile = new File(craftFolder, filenameDateFormat.format(date) + ".log");

        // Server Side Part
        serverStartFolder = new File(logsFolder, "Server Start");
        serverStartFile = new File(serverStartFolder, filenameDateFormat.format(date) + ".log");

        serverStopFolder = new File(logsFolder, "Server Stop");
        serverStopFile = new File(serverStopFolder, filenameDateFormat.format(date) + ".log");

        consoleLogFolder = new File(logsFolder, "Console Commands");
        consoleLogFile = new File(consoleLogFolder, filenameDateFormat.format(date) + ".log");

        RAMFolder = new File(logsFolder, "RAM");
        RAMFile = new File(RAMFolder, filenameDateFormat.format(date) + ".log");

        TPSFolder = new File(logsFolder, "TPS");
        TPSFile = new File(TPSFolder, filenameDateFormat.format(date) + ".log");

        portalCreateFolder = new File(logsFolder, "Portal Creation");
        portalCreateFile = new File(portalCreateFolder, filenameDateFormat.format(date) + ".log");

        rconFolder = new File(logsFolder, "RCON");
        rconFile = new File(rconFolder, filenameDateFormat.format(date) + ".log");

        // Extras Side Part
        afkFolder = new File(logsFolder, "AFK");
        afkFile = new File(afkFolder, filenameDateFormat.format(date) + ".log");

        wrongPasswordFolder = new File(logsFolder, "Wrong Password");
        wrongPasswordFile = new File(wrongPasswordFolder, filenameDateFormat.format(date) + ".log");

        try {

            // Folder Handling
            // Player Side Part
            if (main.getConfig().getBoolean("Staff.Enabled")) staffFolder.mkdir();

            chatLogFolder.mkdir();

            commandLogFolder.mkdir();

            signLogFolder.mkdir();

            playerJoinLogFolder.mkdir();

            playerLeaveLogFolder.mkdir();

            playerDeathLogFolder.mkdir();

            playerTeleportLogFolder.mkdir();

            blockPlaceLogFolder.mkdir();

            blockBreakLogFolder.mkdir();

            playerKickLogFolder.mkdir();

            playerLevelFolder.mkdir();

            bucketFillFolder.mkdir();

            bucketEmptyFolder.mkdir();

            anvilFolder.mkdir();

            itemDropFolder.mkdir();

            enchantFolder.mkdir();

            bookEditingFolder.mkdir();

            itemPickupFolder.mkdir();

            furnaceFolder.mkdir();

            gameModeFolder.mkdir();

            craftFolder.mkdir();

            // Server Side Part
            serverStartFolder.mkdir();

            serverStopFolder.mkdir();

            consoleLogFolder.mkdir();

            RAMFolder.mkdir();

            TPSFolder.mkdir();

            portalCreateFolder.mkdir();

            rconFolder.mkdir();

            // Extras Side Part
            if (EssentialsUtil.getEssentialsAPI() != null) afkFolder.mkdir();

            if (AuthMeUtil.getAuthMeAPI() != null) wrongPasswordFolder.mkdir();


            // Files Handling
            // Player Side
            if (main.getConfig().getBoolean("Staff.Enabled")) staffFile.createNewFile();

            chatLogFile.createNewFile();

            commandLogFile.createNewFile();

            signLogFile.createNewFile();

            playerJoinLogFile.createNewFile();

            playerLeaveLogFile.createNewFile();

            playerDeathLogFile.createNewFile();

            playerTeleportLogFile.createNewFile();

            blockPlaceLogFile.createNewFile();

            blockBreakLogFile.createNewFile();

            playerKickLogFile.createNewFile();

            playerLevelFile.createNewFile();

            anvilFile.createNewFile();

            bucketFillFile.createNewFile();

            bucketEmptyFile.createNewFile();

            itemDropFile.createNewFile();

            enchantFile.createNewFile();

            bookEditingFile.createNewFile();

            itemPickupFile.createNewFile();

            furnaceFile.createNewFile();

            gameModeFile.createNewFile();

            craftFile.createNewFile();

            // Server Side
            serverStartFile.createNewFile();

            serverStartFile.createNewFile();

            consoleLogFile.createNewFile();

            RAMFile.createNewFile();

            TPSFile.createNewFile();

            portalCreateFile.createNewFile();

            rconFile.createNewFile();

            // Extras Side
            if (EssentialsUtil.getEssentialsAPI() != null) afkFile.createNewFile();

            if (AuthMeUtil.getAuthMeAPI() != null) wrongPasswordFile.createNewFile();

        } catch (IOException e) { e.printStackTrace(); }
    }

    public static File getChatLogFile() { return chatLogFile; }

    public static File getCommandLogFile() { return commandLogFile; }

    public static File getSignLogFile() { return signLogFile; }

    public static File getPlayerJoinLogFile() { return playerJoinLogFile; }

    public static File getPlayerLeaveLogFile() { return playerLeaveLogFile; }

    public static File getPlayerDeathLogFile() { return playerDeathLogFile; }

    public static File getPlayerTeleportLogFile() { return playerTeleportLogFile; }

    public static File getBlockPlaceLogFile() { return blockPlaceLogFile; }

    public static File getBlockBreakLogFile() { return blockBreakLogFile; }

    public static File getPlayerKickLogFile() { return playerKickLogFile; }

    public static File getPlayerLevelFile() { return playerLevelFile; }

    public static File getBucketFillFile() { return bucketFillFile; }

    public static File getBucketEmptyFolder() { return bucketEmptyFile; }

    public static File getAnvilFile() { return anvilFile; }

    public static File getstaffFile() { return staffFile; }

    public static File getItemDropFile() { return itemDropFile; }

    public static File getenchantFile() { return enchantFile; }

    public static File getBookEditingFile() { return bookEditingFile; }

    public static File getItemPickupFile() { return itemPickupFile; }

    public static File getFurnaceFile() { return furnaceFile; }

    public static File getGameModeFile() { return gameModeFile; }

    public static File getCraftFile() { return craftFile; }

    // Server Side Part
    public static File getserverStartFile() { return serverStartFile; }

    public static File getserverStopFile() { return serverStopFile; }

    public static File getConsoleLogFile() { return consoleLogFile; }

    public static File getRAMLogFile() { return RAMFile; }

    public static File getTPSLogFile() { return TPSFile; }

    public static File getPortalCreateFile() { return portalCreateFile; }

    public static File getRconFile() { return rconFile; }

    // Extras Side Part
    public static File getAfkFile() { return afkFile; }

    public static File getWrongPasswordFile() { return wrongPasswordFile; }


    public void deleteFile(File file) {

        if (main.getConfig().getInt("File-Deletion") <= 0 ) return;

        FileTime creationTime = null;

        try {

            creationTime = (FileTime) Files.getAttribute(file.toPath(), "creationTime");

        } catch (IOException e) {

            e.printStackTrace();
        }

        assert creationTime != null;
        long offset = System.currentTimeMillis() - creationTime.toMillis();
        long fileDeletionDays = main.getConfig().getInt("File-Deletion");
        long maxAge = TimeUnit.DAYS.toMillis(fileDeletionDays);

        if (offset > maxAge) file.delete();
    }

    public void deleteFiles(){

        if (main.getConfig().getInt("File-Deletion") <= 0 ) return;

        // Player Side
        for (File chatLog : Objects.requireNonNull(chatLogFolder.listFiles()))
        {

            deleteFile(chatLog);

        }

        for (File commandLog : Objects.requireNonNull(commandLogFolder.listFiles()))
        {

            deleteFile(commandLog);

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

        for (File kickLog : Objects.requireNonNull(playerKickLogFolder.listFiles()))
        {

            deleteFile(kickLog);

        }

        for (File playerLevelLog : Objects.requireNonNull(playerLevelFolder.listFiles()))
        {

            deleteFile(playerLevelLog);

        }

        for (File bucketFill : Objects.requireNonNull(bucketFillFolder.listFiles()))
        {

            deleteFile(bucketFill);

        }

        for (File bucketEmpty : Objects.requireNonNull(bucketEmptyFolder.listFiles()))
        {

            deleteFile(bucketEmpty);

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

        for (File itemDrop : Objects.requireNonNull(itemDropFolder.listFiles()))
        {

            deleteFile(itemDrop);

        }

        for (File enchanting : Objects.requireNonNull(enchantFolder.listFiles()))
        {

            deleteFile(enchanting);

        }

        for (File book : Objects.requireNonNull(bookEditingFolder.listFiles()))
        {

            deleteFile(book);

        }

        for (File pickup : Objects.requireNonNull(itemPickupFolder.listFiles()))
        {

            deleteFile(pickup);

        }

        for (File furnace : Objects.requireNonNull(furnaceFolder.listFiles()))
        {

            deleteFile(furnace);

        }

        for (File craft : Objects.requireNonNull(craftFolder.listFiles()))
        {

            deleteFile(craft);

        }

        // Server Side
        for (File serverStart : Objects.requireNonNull(serverStartFolder.listFiles()))
        {

            deleteFile(serverStart);

        }

        for (File serverStop : Objects.requireNonNull(serverStopFolder.listFiles()))
        {

            deleteFile(serverStop);

        }

        for (File consoleLog : Objects.requireNonNull(consoleLogFolder.listFiles()))
        {

            deleteFile(consoleLog);

        }

        for (File RAMLog : Objects.requireNonNull(RAMFolder.listFiles()))
        {

            deleteFile(RAMLog);

        }

        for (File TPSLog : Objects.requireNonNull(TPSFolder.listFiles()))
        {

            deleteFile(TPSLog);

        }

        for (File portalCreate : Objects.requireNonNull(portalCreateFolder.listFiles()))
        {

            deleteFile(portalCreate);

        }

        for (File rcon : Objects.requireNonNull(rconFolder.listFiles()))
        {

            deleteFile(rcon);

        }

        for (File creative : Objects.requireNonNull(gameModeFolder.listFiles()))
        {

            deleteFile(creative);

        }

        // Extra Side
        if (EssentialsUtil.getEssentialsAPI() != null) {
            for (File afk : Objects.requireNonNull(afkFolder.listFiles())) {

                deleteFile(afk);

            }
        }

        if (AuthMeUtil.getAuthMeAPI() != null) {
            for (File password : Objects.requireNonNull(wrongPasswordFolder.listFiles())) {

                deleteFile(password);

            }
        }
    }
}