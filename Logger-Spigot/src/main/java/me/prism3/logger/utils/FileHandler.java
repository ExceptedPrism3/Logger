package me.prism3.logger.utils;

import me.prism3.logger.Main;
import me.prism3.logger.hooks.*;
import me.prism3.logger.utils.enums.NmsVersions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static me.prism3.logger.utils.Data.fileDeletion;
import static me.prism3.logger.utils.Data.version;

public class FileHandler {

    // Folders Part
    // Player Side
    private static File chatLogFolder;
    private static File playerCommandLogFolder;
    private static File signLogFolder;
    private static File playerJoinLogFolder;
    private static File playerLeaveLogFolder;
    private static File playerDeathLogFolder;
    private static File playerDeathBackupLogFolder;
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
    private static File registrationFolder;
    private static File primedTNTFolder;
    private static File chestInteractionFolder;
    private static File entityDeathFolder;
    private static File itemFramePlaceFolder;
    private static File itemFrameBreakFolder;
    private static File armorStandPlaceFolder;
    private static File armorStandBreakFolder;
    private static File armorStandInteractionFolder;
    private static File endCrystalPlaceFolder;
    private static File endCrystalBreakFolder;
    private static File leverInteractionFolder;
    private static File spawnEggFolder;

    // Server Side
    private static File serverStartFolder;
    private static File serverStopFolder;
    private static File consoleLogFolder;
    private static File ramFolder;
    private static File tpsFolder;
    private static File portalCreateFolder;
    private static File rConFolder;
    private static File commandBlockFolder;
    private static File playerCountFolder;
    private static File serverAddressFolder;
    private static File advancementFolder;

    // Extras Side Part
    private static File afkFolder;
    private static File wrongPasswordFolder;
    private static File vaultFolder;
    private static File liteBansFolder;
    private static File advancedBanFolder;
    private static File viaVersionFolder;
    private static File worldGuardFolder;

    // Version Exception Part
    private static File woodStrippingFolder;
    private static File totemUndyingFolder;

    // Custom Part
    private static File manualFolder;

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
    private static File registrationFile;
    private static File primedTNTFile;
    private static File chestInteractionFile;
    private static File entityDeathFile;
    private static File itemFramePlaceFile;
    private static File itemFrameBreakFile;
    private static File armorStandPlaceFile;
    private static File armorStandBreakFile;
    private static File armorStandInteractionFile;
    private static File endCrystalPlaceFile;
    private static File endCrystalBreakFile;
    private static File leverInteractionFile;
    private static File spawnEggFile;

    // Server Side
    private static File serverStartFile;
    private static File serverStopFile;
    private static File consoleLogFile;
    private static File ramFile;
    private static File tpsFile;
    private static File portalCreateFile;
    private static File rConFile;
    private static File commandBlockFile;
    private static File playerCountFile;
    private static File serverAddressFile;
    private static File advancementFile;

    // Extras Side
    private static File afkFile;
    private static File wrongPasswordFile;
    private static File vaultFile;
    private static File liteBansFile;
    private static File advancedBanFile;
    private static File viaVersionFile;
    private static File worldGuardFile;

    // Version Exception Part
    private static File woodStrippingFile;
    private static File totemUndyingFile;

    // Custom Part
    private static File manualFile;

    public FileHandler(File dataFolder) {

        dataFolder.mkdir();

        final File logsFolder = new File(dataFolder, "Logs");
        logsFolder.mkdirs();

        final Date date = new Date();
        final SimpleDateFormat filenameDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        // Player Side Part
        staffFolder = new File(logsFolder, "Staff");
        staffFile = new File(staffFolder, filenameDateFormat.format(date) + ".log");

        chatLogFolder = new File(logsFolder, "Player Chat");
        chatLogFile = new File(chatLogFolder, filenameDateFormat.format(date) + ".log");

        playerCommandLogFolder = new File(logsFolder, "Player Commands");
        commandLogFile = new File(playerCommandLogFolder, filenameDateFormat.format(date) + ".log");

        signLogFolder = new File(logsFolder, "Player Sign Text");
        signLogFile = new File(signLogFolder, filenameDateFormat.format(date) + ".log");

        playerJoinLogFolder = new File(logsFolder, "Player Join");
        playerJoinLogFile = new File(playerJoinLogFolder, filenameDateFormat.format(date) + ".log");

        playerLeaveLogFolder = new File(logsFolder, "Player Leave");
        playerLeaveLogFile = new File(playerLeaveLogFolder, filenameDateFormat.format(date) + ".log");

        playerDeathLogFolder = new File(logsFolder, "Player Death");
        playerDeathLogFile = new File(playerDeathLogFolder, filenameDateFormat.format(date) + ".log");

        playerDeathBackupLogFolder = new File(playerDeathLogFolder, "Backups");

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

        registrationFolder = new File(logsFolder, "Registration");
        registrationFile = new File(registrationFolder, filenameDateFormat.format(date) + ".log");

        final File registrationDataFolder = new File(registrationFolder, "Registration_Data");

        primedTNTFolder = new File(logsFolder, "Primed TNT");
        primedTNTFile = new File(primedTNTFolder, filenameDateFormat.format(date) + ".log");

        chestInteractionFolder = new File(logsFolder, "Chest Interaction");
        chestInteractionFile = new File(chestInteractionFolder, filenameDateFormat.format(date) + ".log");

        entityDeathFolder = new File(logsFolder, "Entity Death");
        entityDeathFile = new File(entityDeathFolder, filenameDateFormat.format(date) + ".log");

        itemFramePlaceFolder = new File(logsFolder, "ItemFrame Place");
        itemFramePlaceFile = new File(itemFramePlaceFolder, filenameDateFormat.format(date) + ".log");

        itemFrameBreakFolder = new File(logsFolder, "ItemFrame Break");
        itemFrameBreakFile = new File(itemFrameBreakFolder, filenameDateFormat.format(date) + ".log");

        armorStandPlaceFolder = new File(logsFolder, "ArmorStand Place");
        armorStandPlaceFile = new File(armorStandPlaceFolder, filenameDateFormat.format(date) + ".log");

        armorStandBreakFolder = new File(logsFolder, "ArmorStand Break");
        armorStandBreakFile = new File(armorStandBreakFolder, filenameDateFormat.format(date) + ".log");

        endCrystalPlaceFolder = new File(logsFolder, "EndCrystal Place");
        endCrystalPlaceFile = new File(endCrystalPlaceFolder, filenameDateFormat.format(date) + ".log");

        endCrystalBreakFolder = new File(logsFolder, "EndCrystal Break");
        endCrystalBreakFile = new File(endCrystalBreakFolder, filenameDateFormat.format(date) + ".log");

        armorStandInteractionFolder = new File(logsFolder, "ArmorStand Interaction");
        armorStandInteractionFile = new File(armorStandInteractionFolder, filenameDateFormat.format(date) + ".log");

        leverInteractionFolder = new File(logsFolder, "Lever Interaction");
        leverInteractionFile = new File(leverInteractionFolder, filenameDateFormat.format(date) + ".log");

        spawnEggFolder = new File(logsFolder, "Spawn Egg");
        spawnEggFile = new File(spawnEggFolder, filenameDateFormat.format(date) + ".log");

        // Server Side Part
        serverStartFolder = new File(logsFolder, "Server Start");
        serverStartFile = new File(serverStartFolder, filenameDateFormat.format(date) + ".log");

        serverStopFolder = new File(logsFolder, "Server Stop");
        serverStopFile = new File(serverStopFolder, filenameDateFormat.format(date) + ".log");

        consoleLogFolder = new File(logsFolder, "Console Commands");
        consoleLogFile = new File(consoleLogFolder, filenameDateFormat.format(date) + ".log");

        ramFolder = new File(logsFolder, "RAM");
        ramFile = new File(ramFolder, filenameDateFormat.format(date) + ".log");

        tpsFolder = new File(logsFolder, "TPS");
        tpsFile = new File(tpsFolder, filenameDateFormat.format(date) + ".log");

        portalCreateFolder = new File(logsFolder, "Portal Creation");
        portalCreateFile = new File(portalCreateFolder, filenameDateFormat.format(date) + ".log");

        rConFolder = new File(logsFolder, "RCON");
        rConFile = new File(rConFolder, filenameDateFormat.format(date) + ".log");

        commandBlockFolder = new File(logsFolder, "Command Block");
        commandBlockFile = new File(commandBlockFolder, filenameDateFormat.format(date) + ".log");

        playerCountFolder = new File(logsFolder, "Player Count");
        playerCountFile = new File(playerCountFolder, filenameDateFormat.format(date) + ".log");

        serverAddressFolder = new File(logsFolder, "Server Address");
        serverAddressFile = new File(serverAddressFolder, filenameDateFormat.format(date) + ".log");

        advancementFolder = new File(logsFolder, "Advancement");
        advancementFile = new File(advancementFolder, filenameDateFormat.format(date) + ".log");

        // Extras Side Part
        afkFolder = new File(logsFolder, "AFK");
        afkFile = new File(afkFolder, filenameDateFormat.format(date) + ".log");

        wrongPasswordFolder = new File(logsFolder, "Wrong Password");
        wrongPasswordFile = new File(wrongPasswordFolder, filenameDateFormat.format(date) + ".log");

        vaultFolder = new File(logsFolder, "Player Balance");
        vaultFile = new File(vaultFolder, filenameDateFormat.format(date) + ".log");

        liteBansFolder = new File(logsFolder, "LiteBans");
        liteBansFile = new File(liteBansFolder, filenameDateFormat.format(date) + ".log");

        advancedBanFolder = new File(logsFolder, "AdvancedBan");
        advancedBanFile = new File(advancedBanFolder, filenameDateFormat.format(date) + ".log");

        viaVersionFolder = new File(logsFolder, "ViaVersion");
        viaVersionFile = new File(viaVersionFolder, filenameDateFormat.format(date) + ".log");

        worldGuardFolder = new File(logsFolder, "World Guard");
        worldGuardFile = new File(worldGuardFolder, filenameDateFormat.format(date) + ".log");

        // Version Exception Part
        woodStrippingFolder = new File(logsFolder, "Wood Stripping");
        woodStrippingFile = new File(woodStrippingFolder, filenameDateFormat.format(date) + ".log");

        totemUndyingFolder = new File(logsFolder, "Totem of Undying");
        totemUndyingFile = new File(totemUndyingFolder, filenameDateFormat.format(date) + ".log");

        // Custom Part
        manualFolder = new File(logsFolder, "Manual");
        manualFile = new File(manualFolder, filenameDateFormat.format(date) + ".log");

        try {

            // Folder Handling
            // Player Side Part
            if (Data.isStaffEnabled) staffFolder.mkdir();

            chatLogFolder.mkdir();

            playerCommandLogFolder.mkdir();

            signLogFolder.mkdir();

            playerJoinLogFolder.mkdir();

            playerLeaveLogFolder.mkdir();

            playerDeathLogFolder.mkdir();

            playerDeathBackupLogFolder.mkdir();

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

            registrationFolder.mkdir();
            registrationDataFolder.mkdir();

            primedTNTFolder.mkdir();

            chestInteractionFolder.mkdir();

            entityDeathFolder.mkdir();

            itemFramePlaceFolder.mkdir();

            itemFrameBreakFolder.mkdir();

            armorStandPlaceFolder.mkdir();

            armorStandBreakFolder.mkdir();

            armorStandInteractionFolder.mkdir();

            endCrystalPlaceFolder.mkdir();

            endCrystalBreakFolder.mkdir();

            playerLevelFolder.mkdir();

            leverInteractionFolder.mkdir();

            spawnEggFolder.mkdir();

            // Server Side Part
            serverStartFolder.mkdir();

            serverStopFolder.mkdir();

            consoleLogFolder.mkdir();

            ramFolder.mkdir();

            tpsFolder.mkdir();

            portalCreateFolder.mkdir();

            rConFolder.mkdir();

            commandBlockFolder.mkdir();

            playerCountFolder.mkdir();

            serverAddressFolder.mkdir();

            advancementFolder.mkdir();

            // Extras Side Part
            if (EssentialsUtil.isAllowed) afkFolder.mkdir();

            if (AuthMeUtil.isAllowed) wrongPasswordFolder.mkdir();

            if (VaultUtil.isAllowed && VaultUtil.getVaultEcon() != null) vaultFolder.mkdir();

            if (LiteBanUtil.isAllowed) liteBansFolder.mkdir();

            if (AdvancedBanUtil.isAllowed) advancedBanFolder.mkdir();

            if (ViaVersionUtil.isAllowed) viaVersionFolder.mkdir();

//            if (WorldGuardUtil.isAllowed) worldGuardFolder.mkdir();

            // Version Exception Part
            if (version.isAtLeast(NmsVersions.v1_13_R1)) woodStrippingFolder.mkdir();
            if (version.isAtLeast(NmsVersions.v1_11_R1)) totemUndyingFolder.mkdir();

            // Custom Part
            manualFolder.mkdir();


            // Files Handling
            // Player Side
            if (Data.isStaffEnabled) staffFile.createNewFile();

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

            registrationFile.createNewFile();

            primedTNTFile.createNewFile();

            chestInteractionFile.createNewFile();

            entityDeathFile.createNewFile();

            itemFramePlaceFile.createNewFile();

            itemFrameBreakFile.createNewFile();

            armorStandPlaceFile.createNewFile();

            armorStandBreakFile.createNewFile();

            endCrystalPlaceFile.createNewFile();

            endCrystalBreakFile.createNewFile();

            armorStandInteractionFile.createNewFile();

            leverInteractionFile.createNewFile();

            spawnEggFile.createNewFile();

            // Server Side
            serverStartFile.createNewFile();

            serverStartFile.createNewFile();

            consoleLogFile.createNewFile();

            ramFile.createNewFile();

            tpsFile.createNewFile();

            portalCreateFile.createNewFile();

            rConFile.createNewFile();

            commandBlockFile.createNewFile();

            playerCountFile.createNewFile();

            serverAddressFile.createNewFile();

            advancementFile.createNewFile();

            // Extras Side
            if (EssentialsUtil.isAllowed) afkFile.createNewFile();

            if (AuthMeUtil.isAllowed) wrongPasswordFile.createNewFile();

            if (VaultUtil.isAllowed && VaultUtil.getVaultEcon() != null) vaultFile.createNewFile();

            if (LiteBanUtil.isAllowed) liteBansFile.createNewFile();

            if (AdvancedBanUtil.isAllowed) advancedBanFile.createNewFile();

            if (ViaVersionUtil.isAllowed) viaVersionFile.createNewFile();

//            if (WorldGuardUtil.isAllowed) worldGuardFile.createNewFile();

            // Version Exception Part
            if (version.isAtLeast(NmsVersions.v1_13_R1)) woodStrippingFile.createNewFile();
            if (version.isAtLeast(NmsVersions.v1_11_R1)) totemUndyingFile.createNewFile();

            // Custom Part
            manualFile.createNewFile();

        } catch (final IOException e) { e.printStackTrace(); }
    }

    public static File getChatLogFile() { return chatLogFile; }

    public static File getPlayerCommandLogFile() { return commandLogFile; }

    public static File getSignLogFile() { return signLogFile; }

    public static File getPlayerJoinLogFile() { return playerJoinLogFile; }

    public static File getPlayerLeaveLogFile() { return playerLeaveLogFile; }

    public static File getPlayerDeathLogFile() { return playerDeathLogFile; }

    public static File getPlayerDeathBackupLogFolder() { return playerDeathBackupLogFolder; }

    public static File getPlayerTeleportLogFile() { return playerTeleportLogFile; }

    public static File getBlockPlaceLogFile() { return blockPlaceLogFile; }

    public static File getBlockBreakLogFile() { return blockBreakLogFile; }

    public static File getPlayerKickLogFile() { return playerKickLogFile; }

    public static File getPlayerLevelFile() { return playerLevelFile; }

    public static File getBucketFillFile() { return bucketFillFile; }

    public static File getBucketEmptyFile() { return bucketEmptyFile; }

    public static File getAnvilFile() { return anvilFile; }

    public static File getStaffFile() { return staffFile; }

    public static File getItemDropFile() { return itemDropFile; }

    public static File getEnchantFile() { return enchantFile; }

    public static File getBookEditingFile() { return bookEditingFile; }

    public static File getItemPickupFile() { return itemPickupFile; }

    public static File getFurnaceFile() { return furnaceFile; }

    public static File getGameModeFile() { return gameModeFile; }

    public static File getCraftFile() { return craftFile; }

    public static File getRegistrationFile() { return registrationFile; }

    public static File getPrimedTNTFile() { return primedTNTFile; }

    public static File getChestInteractionFile() { return chestInteractionFile; }

    public static File getEntityDeathFile() { return entityDeathFile; }

    public static File getItemFramePlaceFile() { return itemFramePlaceFile; }

    public static File getItemFrameBreakFile() { return itemFrameBreakFile; }

    public static File getArmorStandPlaceFile() { return armorStandPlaceFile; }

    public static File getArmorStandBreakFile() { return armorStandBreakFile; }

    public static File getArmorStandInteractionFile() { return armorStandInteractionFile; }

    public static File getEndCrystalPlaceFile() { return endCrystalPlaceFile; }

    public static File getEndCrystalBreakFile() { return endCrystalBreakFile; }

    public static File getLeverInteractionFile() { return leverInteractionFile; }

    public static File getSpawnEggFile() { return spawnEggFile; }

    // Server Side Part
    public static File getServerStartFile() { return serverStartFile; }

    public static File getServerStopFile() { return serverStopFile; }

    public static File getConsoleLogFile() { return consoleLogFile; }

    public static File getRAMLogFile() { return ramFile; }

    public static File getTPSLogFile() { return tpsFile; }

    public static File getPortalCreateFile() { return portalCreateFile; }

    public static File getRconFile() { return rConFile; }

    public static File getCommandBlockFile() { return commandBlockFile; }

    public static File getPlayerCountFile() { return playerCountFile; }

    public static File getServerAddressFile() { return serverAddressFile; }

    public static File getAdvancementFile() { return advancementFile; }

    // Extras Side Part
    public static File getAfkFile() { return afkFile; }

    public static File getWrongPasswordFile() { return wrongPasswordFile; }

    public static File getVaultFile() { return vaultFile; }

    public static File getLiteBansFile() { return liteBansFile; }

    public static File getAdvancedBanFile() { return advancedBanFile; }

    public static File getViaVersionFile() { return viaVersionFile; }

    public static File getWorldGuardFile() { return worldGuardFile; }

    // Version Exception Part
    public static File getWoodStrippingFile() { return woodStrippingFile; }

    public static File getTotemUndyingFile() { return totemUndyingFile; }

    // Custom Part
    public static File getManualFile() { return manualFile; }

    public static void handleFileLog(String messagePath, Map<String, String> placeholders, File logFile) {

        String message = Main.getInstance().getMessagesFile().get().getString(messagePath);

        if (message == null) {
            Log.severe("The following path " + messagePath + " could not be found, make sure it's not renamed or deleted." +
                    " If the issue persists, re-create the messages file");
            return;
        }

        for (Map.Entry<String, String> placeholder : placeholders.entrySet())
            message = message.replace(placeholder.getKey(), placeholder.getValue());

        try (final BufferedWriter out = new BufferedWriter(new FileWriter(logFile, true))) {
            out.append(message).append(System.lineSeparator());
        } catch (final IOException e) {
            Log.warning("An error occurred while logging into the appropriate file.");
            Log.warning(e.getMessage());
        }
    }

    public void deleteFiles(final File dataFolder) {

        if (fileDeletion <= 0) return;

        final long deadLine = System.currentTimeMillis() - (fileDeletion * 24 * 60 * 60 * 1000);
        final File logsFolder = new File(dataFolder, "Logs");

        try (final Stream<Path> files = Files.walk(Paths.get(logsFolder.getPath()))) {
            files.filter(path -> {
                try {
                    return Files.isRegularFile(path) && Files.getLastModifiedTime(path).to(TimeUnit.MILLISECONDS) < deadLine;
                } catch (final IOException ex) {
                    Log.severe("An error occurred while checking file modification time.", ex);
                    return false;
                }
            }).forEach(path -> {
                try {
                    Files.deleteIfExists(path);
                } catch (final IOException ex) {
                    Log.severe("An error occurred while deleting files.", ex);
                }
            });
        } catch (final IOException e) {
            Log.severe("An error occurred while searching for files to delete.", e);
        }
    }
}
