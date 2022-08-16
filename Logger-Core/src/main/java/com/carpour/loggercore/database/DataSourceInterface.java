package com.carpour.loggercore.database;

import com.carpour.loggercore.database.entity.Coordinates;
import com.carpour.loggercore.database.entity.PlayerChat;

import java.net.InetSocketAddress;
import java.util.List;

public interface DataSourceInterface {

    void insertPlayerChat(String serverName, String playerName, String playerUUID, String worldName, String msg, boolean isStaff);

    void insertPlayerCommands(String serverName, String playerName, String playerUUID, String worldName, String command, boolean isStaff);

    void insertPlayerSignText(String serverName, String playerName, String playerUUID, Coordinates coords, String lines, boolean isStaff);

    void insertPlayerDeath(String serverName, String playerName, String playerUUID, int level, String cause, String who, Coordinates coordinates, boolean isStaff);

    void insertPlayerTeleport(String serverName, String playerName, String playerUUID, Coordinates oldCoords, Coordinates newCoords, boolean isStaff);

    void insertPlayerJoin(String serverName, String playerName, String playerUUID, Coordinates coords, InetSocketAddress ip, boolean isStaff);

    void insertPlayerLeave(String serverName, String playerName, String playerUUID, Coordinates coords, boolean isStaff);

    void insertBlockPlace(String serverName, String playerName, String playerUUID, String block, Coordinates coords, boolean isStaff);

    void insertBlockBreak(String serverName, String playerName, String playerUUID, String blockName, Coordinates coords, boolean isStaff);

    void insertTps(String serverName, double tpss);

    void insertRam(String serverName, long tm, long um, long fm);

    void insertPlayerKick(String serverName, String playerName, String playerUUID, Coordinates coords, String reason, boolean isStaff);

    void insertPortalCreate(String serverName, String worldName, String by);

    void insertLevelChange(String serverName, String playerName, String playerUUID, boolean isStaff);

    void insertBucketFill(String serverName, String playerName, String playerUUID, String bucket, Coordinates coords, boolean isStaff);

    void insertBucketEmpty(String serverName, String playerName, String playerUUID, String bucket, Coordinates coords, boolean isStaff);

    void insertAnvil(String serverName, String playerName, String playerUUID, String newName, boolean isStaff);

    void insertServerStart(String serverName);

    void insertServerStop(String serverName);

    void insertItemDrop(String serverName, String playerName, String playerUUID, String item, int amount, Coordinates coords,
                        List<String> enchantment, String changedName, boolean isStaff);

    void insertEnchant(String serverName, String playerName, String playerUUID, List<String> enchantment, int enchantmentLevel,
                       String item, int cost, Coordinates coordinates, boolean isStaff);

    void insertBookEditing(String serverName, String playerName, String playerUUID, String worldName, int pages, List<String> content,
                           String signedBy, boolean isStaff);

    void insertAfk(String serverName, String playerName, String playerUUID, Coordinates coords, boolean isStaff);

    void insertWrongPassword(String serverName, String playerName, String playerUUID, String worldName, boolean isStaff);

    void insertItemPickup(String serverName, String playerName, String playerUUID, String item, int amount, Coordinates coords, String changedName, boolean isStaff);

    void insertFurnace(String serverName, String playerName, String playerUUID, String item, int amount, Coordinates coords, boolean isStaff);

    void insertRCON(String serverName, String ip, String command);

    void insertGameMode(String serverName, String playerName, String playerUUID, String theGameMode, String worldName, boolean isStaff);

    void insertPlayerCraft(String serverName, String playerName, String playerUUID, String item, int amount, Coordinates coordinates, boolean isStaff);

    void insertVault(String serverName, String playerName, String playerUUID, double oldBal, double newBal, boolean isStaff);

    void insertPlayerRegistration(String serverName, String playerName, String playerUUID, String joinDate);

    void insertPrimedTnt(String serverName, String playerName, String playerUUID, Coordinates coords, boolean isStaff);

    void insertLiteBans(String serverName, String executor, String command, String onWho, String duration, String reason, boolean isSilent);

    void insertAdvanceBanData(String serverName, String type, String executor, String executedOn, String reason, long expirationDate);

    void insertCommandBlock(String serverName, String msg);

    void insertWoodStripping(String serverName, String playerName, String playerUUID, String logName, Coordinates coords, boolean isStaff);

    void insertChestInteraction(String serverName, String playerName, String playerUUID,
                                Coordinates coords, String[] items, boolean isStaff);

    void insertEntityDeath(String serverName, String playerName, String playerUUID, String mob,
                           Coordinates coords, boolean isStaff);

    void insertConsoleCommand(String serverName, String msg);

    void insertServerReload(String serverName, String playerName, boolean isStaff);

    void insertPlayerLogin(String serverName, String playerName, String toString,
                           InetSocketAddress playerIP, boolean hasPermission);

    void disconnect();

    List<PlayerChat> getPlayerChatByPlayerName(String playerName, int offset, int limit);

    Long getPlayerChatCount(String playerName);

}
