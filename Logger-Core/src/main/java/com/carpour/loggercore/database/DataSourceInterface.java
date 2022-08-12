package com.carpour.loggercore.database;

import com.carpour.loggercore.database.entity.Coordinates;
import com.carpour.loggercore.database.entity.EntityPlayer;

import java.net.InetSocketAddress;
import java.util.List;

public interface DataSourceInterface {

    void insertPlayerChat(String serverName, EntityPlayer player, String worldName, String msg, boolean isStaff);

    void insertPlayerCommands(String serverName, EntityPlayer player, String worldName, String command, boolean isStaff);

    void insertPlayerSignText(String serverName, EntityPlayer player, Coordinates coords, String lines, boolean isStaff);

    void insertPlayerDeath(String serverName, EntityPlayer player, int level, String cause, String who, Coordinates coordinates, boolean isStaff);

    void insertPlayerTeleport(String serverName, EntityPlayer player, Coordinates oldCoords, Coordinates newCoords, boolean isStaff);

    void insertPlayerJoin(String serverName, EntityPlayer player, Coordinates coords, InetSocketAddress ip, boolean isStaff);

    void insertPlayerLeave(String serverName, EntityPlayer player, Coordinates coords, boolean isStaff);

    void insertBlockPlace(String serverName, EntityPlayer player, String block, Coordinates coords , boolean isStaff);

    void insertBlockBreak(String serverName, EntityPlayer player, String blockName, Coordinates coords, boolean isStaff);

    void insertTps(String serverName, double tpss);

    void insertRam(String serverName, long tm, long um, long fm);

    void insertPlayerKick(String serverName, EntityPlayer player, Coordinates coords, String reason, boolean isStaff);

    void insertPortalCreate(String serverName, String worldName, String by);

    void insertLevelChange(String serverName, EntityPlayer player, boolean isStaff);

    void insertBucketFill(String serverName, EntityPlayer player, String bucket, Coordinates coords, boolean isStaff);

    void insertBucketEmpty(String serverName, EntityPlayer player, String bucket, Coordinates coords, boolean isStaff);

    void insertAnvil(String serverName, EntityPlayer player, String newName, boolean isStaff);

    void insertServerStart(String serverName);

    void insertServerStop(String serverName);

    void insertItemDrop(String serverName, EntityPlayer player, String item, int amount, Coordinates coords,
                        List<String> enchantment, String changedName, boolean isStaff);

    void insertEnchant(String serverName, EntityPlayer player, List<String> enchantment, int enchantmentLevel,
                       String item, int cost, Coordinates coordinates, boolean isStaff);

    void insertBookEditing(String serverName, EntityPlayer player, String worldName, int pages, List<String> content,
                           String signedBy, boolean isStaff);

    void insertAfk(String serverName, EntityPlayer player, Coordinates coords, boolean isStaff);

    void insertWrongPassword(String serverName, EntityPlayer player, String worldName, boolean isStaff);

    void insertItemPickup(String serverName, EntityPlayer player, String item, int amount, Coordinates coords, String changedName, boolean isStaff);

    void insertFurnace(String serverName, EntityPlayer player, String item, int amount, Coordinates coords, boolean isStaff);

    void insertRCON(String serverName, String ip, String command);

    void insertGameMode(String serverName, EntityPlayer player, String theGameMode, String worldName, boolean isStaff);

    void insertPlayerCraft(String serverName, EntityPlayer player, String item, int amount, Coordinates coordinates, boolean isStaff);

    void insertVault(String serverName, EntityPlayer player, double oldBal, double newBal, boolean isStaff);

    void insertPlayerRegistration(String serverName, EntityPlayer player, String joinDate);

    void insertPrimedTnt(String serverName, EntityPlayer player, Coordinates coords, boolean isStaff);

    void insertLiteBans(String serverName, String executor, String command, String onWho, String duration, String reason, boolean isSilent);

    void insertAdvanceBanData(String serverName, String type, String executor, String executedOn, String reason, long expirationDate);

    void insertCommandBlock(String serverName, String msg);

    void insertWoodStripping(String serverName, EntityPlayer player, String logName, Coordinates coords, boolean isStaff);

    void insertChestInteraction(String serverName, EntityPlayer player, Coordinates coords, String[] items, boolean isStaff);

    void insertEntityDeath(String serverName, EntityPlayer player, String mob, Coordinates coords, boolean isStaff);

    void insertConsoleCommand(String serverName, String msg);
}
