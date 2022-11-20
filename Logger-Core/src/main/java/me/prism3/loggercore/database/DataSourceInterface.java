package me.prism3.loggercore.database;

import me.prism3.loggercore.database.entity.PlayerChat;
import me.prism3.loggercore.database.queue.QueueManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public interface DataSourceInterface {

    Connection getConnection() throws SQLException;

    PreparedStatement getPlayerChatStsm(Connection connection) throws SQLException;

    PreparedStatement getPlayerCommandStsm(Connection connection) throws SQLException;

    PreparedStatement getPlayerSignTextStsm(Connection connection) throws SQLException;

    PreparedStatement getPlayerDeathStsm(Connection connection) throws SQLException;

    PreparedStatement getPlayerTeleportStsm(Connection connection) throws SQLException;

    PreparedStatement getPlayerConnectionStsm(Connection connection) throws SQLException;

    PreparedStatement getTpsStsm(Connection connection) throws SQLException;

    PreparedStatement getRAMStsm(Connection connection) throws SQLException;

    PreparedStatement getPlayerKickStsm(Connection connection) throws SQLException;

    PreparedStatement getPortalCreateStsm(Connection connection) throws SQLException;

    PreparedStatement getLevelChangeStsm(Connection connection) throws SQLException;

    PreparedStatement getAnvilStsm(Connection connection) throws SQLException;

    void insertServerStart(String serverName);

    void insertServerStop(String serverName);

    PreparedStatement getItemActionStsm(Connection connection) throws SQLException;

    PreparedStatement getEnchantStsm(Connection connection) throws SQLException;

    PreparedStatement getBookEditingStsm(Connection connection) throws SQLException;

    PreparedStatement getAfkStsm(Connection connection) throws SQLException;

    PreparedStatement getWrongPasswordStsm(Connection connection) throws SQLException;


    PreparedStatement getFurnaceStsm(Connection connection) throws SQLException;

    void insertRCON(String serverName, String command);

    PreparedStatement getGamemodeStsm(Connection connection) throws SQLException;

    PreparedStatement getPlayerCraftStsm(Connection connection) throws SQLException;

    PreparedStatement getVaultStsm(Connection connection) throws SQLException;

    void insertPlayerRegistration(String serverName, String playerName, String playerUUID,
                                  String joinDate);

    PreparedStatement getPrimedTntStsm(Connection connection) throws SQLException;

    PreparedStatement getLiteBansStsm(Connection connection) throws SQLException;

    PreparedStatement getAdvancedDataStsm(Connection connection) throws SQLException;

    PreparedStatement getCommandBlockStsm(Connection connection) throws SQLException;

    PreparedStatement getWoodStrippingStsm(Connection connection) throws SQLException;

    PreparedStatement getChestInteractionStsm(Connection connection) throws SQLException;

    PreparedStatement getEntityDeathStsm(Connection connection) throws SQLException;

    PreparedStatement getConsoleCommandStsm(Connection connection) throws SQLException;

    void insertServerReload(String serverName, String playerName, boolean isStaff);

    PreparedStatement getPlayerLoginStsm(Connection connection) throws SQLException;


    PreparedStatement getServerSwitchStsm(Connection connection) throws SQLException;

    PreparedStatement getPAFFriendMessageStsm(Connection connection) throws SQLException;


    PreparedStatement getPAFPartyMessageStsm(Connection connection) throws SQLException;


    PreparedStatement getLeverInteractionStsm(Connection connection) throws SQLException;

    PreparedStatement getSpawnEggStsm(Connection connection) throws SQLException;

    PreparedStatement getWorldGuardStsm(Connection connection) throws SQLException;

    PreparedStatement getPlayerCountStsm(Connection connection) throws SQLException;

    void disconnect();

    List<PlayerChat> getPlayerChatByPlayerName(String playerName, int offset, int limit);

    Long countByTable(String action);

    PreparedStatement getBlockInteractionStsm(Connection connection) throws SQLException;

    PreparedStatement getBucketActionStsm(Connection connection) throws SQLException;

    void setQueueManager(QueueManager queueManager);
}
