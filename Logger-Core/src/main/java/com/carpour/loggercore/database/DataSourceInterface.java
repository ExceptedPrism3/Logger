package com.carpour.loggercore.database;

import com.carpour.loggercore.database.entity.EntityPlayer;

import java.net.InetSocketAddress;
import java.util.List;

public interface DataSourceInterface {

    void insertConsoleCommand(String serverName, String msg);


    void addPlayerTime(String playerName, int playtime);

    int getPlayerTime(String playerName);

    void pushPlayerTimesToDatabase();

    void pushSinglePTimeToDatabase(String playerName);

    void insertPlayerChat(String serverName, EntityPlayer player, String msg, boolean staff);

    void insertPlayerCommands(String serverName, EntityPlayer player, String msg, boolean staff);

    void insertPlayerSignText(String serverName, EntityPlayer player, int x, int y, int z, String lines, boolean staff);

    void insertPlayerDeath(String serverName, EntityPlayer player, int level, String cause, String who, boolean staff);

    void insertPlayerTeleport(String serverName, EntityPlayer player, int tx, int ty, int tz, boolean staff);

    void insertPlayerJoin(String serverName, EntityPlayer player, int x, int y, int z, InetSocketAddress ip, boolean staff);

    void insertPlayerLeave(String serverName, EntityPlayer player,  boolean staff);

    void insertBlockPlace(String serverName, EntityPlayer player, String block, int x, int y, int z, boolean staff);

    void insertBlockBreak(String serverName, EntityPlayer player, String blockName, int x, int y, int z, boolean staff);

    void insertTps(String serverName, double tpss);

    void insertRam(String serverName, long tm, long um, long fm);

    void insertPlayerKick(String serverName, EntityPlayer player, int x, int y, int z, String reason, boolean staff);

    void insertPortalCreate(String serverName, String worldName, String by);

    void insertLevelChange(String serverName, String playerName, boolean staff);

    void insertBucketFill(String serverName, EntityPlayer player, String bucket, int x, int y, int z, boolean staff);

    void insertBucketEmpty(String serverName, EntityPlayer player, String bucket, int x, int y, int z, boolean staff);

    void insertAnvil(String serverName, String playerName, String newName, boolean staff);

    void insertServerStart(String serverName);

    void insertServerStop(String serverName);

    void insertItemDrop(String serverName, EntityPlayer player, String item, int amount, int x, int y, int z, List<String> enchantment, String changedName, boolean staff);

    void insertEnchant(String serverName, EntityPlayer player, List<String> enchantment, int enchantmentLevel, String item, int cost, boolean staff);

    void insertBookEditing(String serverName, EntityPlayer player, int pages, List<String> content, String signedBy, boolean staff);

    void insertAfk(String serverName, EntityPlayer player, int x, int y, int z, boolean staff);

    void insertWrongPassword(String serverName, EntityPlayer player, boolean staff);

    void insertItemPickup(String serverName, EntityPlayer player, String item, int amount, int x, int y, int z, String changedName, boolean staff);

    void insertFurnace(String serverName, EntityPlayer player, String item, int amount, int x, int y, int z, boolean staff);

    void insertRCON(String serverName, String ip, String command);

    void insertGamemode(String serverName, EntityPlayer player, String theGameMode, boolean staff);

    void insertPlayerCraft(String serverName, EntityPlayer player, String item, int amount);

    void insertVault(String serverName, EntityPlayer player, double oldBal, double newBal, boolean staff);

    void insertPlayerRegistration(String serverName, EntityPlayer player, String joinDate);

    void insertPrimedtnt(String serverName, EntityPlayer player, int x, int y, int z, boolean staff);

    void insertLitebans(String serverName, String executor, String command, String onWho, String duration, String reason, boolean isSilent);

    void insertAdvanceBanData(String serverName, String type, String executor, String executedOn, String reason, long expirationDate);

    void insertCommandBlock(String serverName, String msg);

    void insertWoodStripping(String serverName, EntityPlayer player, String logName, int x, int y, int z, boolean staff);

    void insertChestInteraction(String serverName, EntityPlayer player, int x, int y, int z, String[] items, boolean staff);

    void insertEntityDeath(String serverName, EntityPlayer player, String mob, int x, int y, int z, boolean staff);



}
