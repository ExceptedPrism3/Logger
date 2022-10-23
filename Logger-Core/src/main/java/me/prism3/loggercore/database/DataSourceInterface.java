package me.prism3.loggercore.database;

import me.prism3.loggercore.database.data.Coordinates;
import me.prism3.loggercore.database.entity.PlayerChat;

import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public interface DataSourceInterface  {
    Connection getConnection() throws SQLException;
    void insertPlayerChat(String serverName, String playerName, String playerUUID, String worldName,
                          String msg, boolean isStaff);

    void insertPlayerCommands(String serverName, String playerName, String playerUUID,
                              String worldName, String command, boolean isStaff);

    void insertPlayerSignText(String serverName, String playerName, String playerUUID,
                              Coordinates coords, String lines, boolean isStaff);

    void insertPlayerDeath(String serverName, String playerName, String playerUUID, int level,
                           String cause, String who, Coordinates coordinates, boolean isStaff);

    void insertPlayerTeleport(String serverName, String playerName, String playerUUID,
                              Coordinates oldCoords, Coordinates newCoords, boolean isStaff);

    void insertPlayerJoin(String serverName, String playerName, String playerUUID,
                          Coordinates coords, InetSocketAddress ip, boolean isStaff);

    void insertPlayerLeave(String serverName, String playerName, String playerUUID,
                           Coordinates coords, boolean isStaff);

    void insertTps(String serverName, double tpss);

    void insertRam(String serverName, long tm, long um, long fm);

    void insertPlayerKick(String serverName, String playerName, String playerUUID,
                          Coordinates coords, String reason, boolean isStaff);

    void insertPortalCreate(String serverName, String worldName, String by);
//TODO remove insertStatements and ADD getPreparedStatement methods
    void insertLevelChange(String serverName, String playerName, String playerUUID,
                           boolean isStaff);


    void insertAnvil(String serverName, String playerName, String playerUUID, String newName,
                     boolean isStaff);

    void insertServerStart(String serverName);

    void insertServerStop(String serverName);

    void insertItemDrop(String serverName, String playerName, String playerUUID, String item,
                        int amount, Coordinates coords,
                        List<String> enchantment, String changedName, boolean isStaff);

    void insertEnchant(String serverName, String playerName, String playerUUID,
                       List<String> enchantment, int enchantmentLevel,
                       String item, int cost, Coordinates coordinates, boolean isStaff);

    void insertBookEditing(String serverName, String playerName, String playerUUID,
                           String worldName, int pages, List<String> content,
                           String signedBy, boolean isStaff);

    void insertAfk(String serverName, String playerName, String playerUUID, Coordinates coords,
                   boolean isStaff);

    void insertWrongPassword(String serverName, String playerName, String playerUUID,
                             String worldName, boolean isStaff);

    void insertItemPickup(String serverName, String playerName, String playerUUID, String item,
                          int amount, Coordinates coords, String changedName, boolean isStaff);

    void insertFurnace(String serverName, String playerName, String playerUUID, String item,
                       int amount, Coordinates coords, boolean isStaff);

    void insertRCON(String serverName, String command);

    void insertGameMode(String serverName, String playerName, String playerUUID, String theGameMode,
                        String worldName, boolean isStaff);

    void insertPlayerCraft(String serverName, String playerName, String playerUUID, String item,
                           int amount, Coordinates coordinates, boolean isStaff);

    void insertVault(String serverName, String playerName, String playerUUID, double oldBal,
                     double newBal, boolean isStaff);

    void insertPlayerRegistration(String serverName, String playerName, String playerUUID,
                                  String joinDate);

    void insertPrimedTnt(String serverName, String playerName, String playerUUID,
                         Coordinates coords, boolean isStaff);

    void insertLiteBans(String serverName, String executor, String command, String onWho,
                        String duration, String reason, boolean isSilent);

    void insertAdvanceBanData(String serverName, String type, String executor, String executedOn,
                              String reason, long expirationDate);

    void insertCommandBlock(String serverName, String msg);

    void insertWoodStripping(String serverName, String playerName, String playerUUID,
                             String logName, Coordinates coords, boolean isStaff);

    void insertChestInteraction(String serverName, String playerName, String playerUUID,
                                Coordinates coords, String[] items, boolean isStaff);

    void insertEntityDeath(String serverName, String playerName, String playerUUID, String mob,
                           Coordinates coords, boolean isStaff);

    void insertConsoleCommand(String serverName, String msg);

    void insertServerReload(String serverName, String playerName, boolean isStaff);

    void insertPlayerLogin(String serverName, String playerName, String toString,
                           InetSocketAddress playerIP, boolean hasPermission);

    void insertItemFramePlace(String serverName, String playerName, String playerUUID,
                              Coordinates coords, boolean isStaff);

    void insertItemFrameBreak(String serverName, String playerName, String playerUUID,
                              Coordinates coords, boolean isStaff);

    void insertServerSwitch(String serverName, String playerUUID, String playerName, String from,
                            String destination, boolean isStaff);

    void insertPAFFriendMessage(String serverName, String playerUUID, String playerName,
                                String message, String receiver, boolean isStaff);

    void insertPAFPartyMessage(String serverName, String playerUUID, String playerName,
                               String message, String leader, List<String> partyMembers, boolean isStaff);

    void insertLeverInteraction(String serverName, String playerUUID, String worldName, String playerName,
                               int x, int y, int z, boolean isStaff);

    void insertSpawnEgg(String serverName, String playerUUID, String worldName, String playerName,
                                int x, int y, int z, String entity, boolean isStaff);

    void insertWorldGuard(String serverName, String playerUUID, String worldName, String playerName,
                        String regionName, boolean isStaff);

    void insertPlayerCount(String serverName, int playerCount);

    void disconnect();

    List<PlayerChat> getPlayerChatByPlayerName(String playerName, int offset, int limit);

    Long countByTable(String action);
    PreparedStatement getBlockInteractionStsm(Connection connection) throws SQLException;
    PreparedStatement getBucketActionStsm(Connection connection) throws SQLException;


}
