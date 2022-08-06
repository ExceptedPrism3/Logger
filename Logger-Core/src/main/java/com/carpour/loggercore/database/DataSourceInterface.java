package com.carpour.loggercore.database;

import com.carpour.loggercore.database.entity.Coordinates;
import com.carpour.loggercore.database.entity.EntityPlayer;

import java.net.InetSocketAddress;
import java.util.List;

public interface DataSourceInterface {


    void insertPlayerChat(String serverName, EntityPlayer player, String worldName, String msg);

    void insertPlayerCommands(String serverName, EntityPlayer player, String worldName, String command);

    void insertPlayerSignText(String serverName, EntityPlayer player, Coordinates coords, String lines);

    void insertPlayerDeath(String serverName, EntityPlayer player, int level, String cause, String who, Coordinates coordinates);

    void insertPlayerTeleport(String serverName, EntityPlayer player, Coordinates oldCoords, Coordinates newCoords);

    void insertPlayerJoin(String serverName, EntityPlayer player, Coordinates coords, InetSocketAddress ip);

    void insertPlayerLeave(String serverName, EntityPlayer player, Coordinates coords);

    void insertBlockPlace(String serverName, EntityPlayer player, String block, Coordinates coords);

    void insertBlockBreak(String serverName, EntityPlayer player, String blockName, Coordinates coords);

    void insertTps(String serverName, double tpss);

    void insertRam(String serverName, long tm, long um, long fm);

    void insertPlayerKick(String serverName, EntityPlayer player, Coordinates coords, String reason);

    void insertPortalCreate(String serverName, String worldName, String by);

    void insertLevelChange(String serverName, String playerName, boolean isStaff);

    void insertBucketFill(String serverName, EntityPlayer player, String bucket, Coordinates coords);

    void insertBucketEmpty(String serverName, EntityPlayer player, String bucket, Coordinates coords);

    void insertAnvil(String serverName, String playerName, String newName, boolean isStaff);

    void insertServerStart(String serverName);

    void insertServerStop(String serverName);

    void insertItemDrop(String serverName, EntityPlayer player, String item, int amount, Coordinates coords, List<String> enchantment, String changedName);

    void insertEnchant(String serverName, EntityPlayer player, List<String> enchantment, int enchantmentLevel,
                       String item, int cost, Coordinates coordinates);

    void insertBookEditing(String serverName, EntityPlayer player, String worldName, int pages, List<String> content, String signedBy);

    void insertAfk(String serverName, EntityPlayer player, Coordinates coords);

    void insertWrongPassword(String serverName, EntityPlayer player, String worldName);

    void insertItemPickup(String serverName, EntityPlayer player, String item, int amount, Coordinates coords, String changedName);

    void insertFurnace(String serverName, EntityPlayer player, String item, int amount, Coordinates coords);

    void insertRCON(String serverName, String ip, String command);

    void insertGameMode(String serverName, EntityPlayer player, String theGameMode, String worldName);

    void insertPlayerCraft(String serverName, EntityPlayer player, String item, int amount, Coordinates coordinates);

    void insertVault(String serverName, EntityPlayer player, double oldBal, double newBal);

    void insertPlayerRegistration(String serverName, EntityPlayer player, String joinDate);

    void insertPrimedTnt(String serverName, EntityPlayer player, Coordinates coords);

    void insertLiteBans(String serverName, String executor, String command, String onWho, String duration, String reason, boolean isSilent);

    void insertAdvanceBanData(String serverName, String type, String executor, String executedOn, String reason, long expirationDate);

    void insertCommandBlock(String serverName, String msg);

    void insertWoodStripping(String serverName, EntityPlayer player, String logName, Coordinates coords);

    void insertChestInteraction(String serverName, EntityPlayer player, Coordinates coords, String[] items);

    void insertEntityDeath(String serverName, EntityPlayer player, String mob, Coordinates coords);

    void insertConsoleCommand(String serverName, String msg);
}
