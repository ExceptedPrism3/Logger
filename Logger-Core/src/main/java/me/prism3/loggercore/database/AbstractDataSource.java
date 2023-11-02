package me.prism3.loggercore.database;

import me.prism3.loggercore.database.data.Options;
import me.prism3.loggercore.database.entity.PlayerChat;
import me.prism3.loggercore.database.queue.DatabaseQueue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public abstract class AbstractDataSource {

    protected final Logger logger = Logger.getLogger(AbstractDataSource.class.getName());
    protected final Options options;
    private final String className;

    protected final String prefix;

    protected AbstractDataSource(Options options, String className, String prefix) {

        this.className = className;
        this.options = options;
        this.prefix = prefix;
    }

    abstract public DatabaseQueue getDatabaseQueue();

    protected abstract String getJdbcUrl();

    public abstract Connection getConnection() throws SQLException;

    public abstract void createTables();

    public Options getOptions(){
        return this.options;
    }

    public PreparedStatement getPlayerChatStsm(Connection connection) throws SQLException {

        return connection.prepareStatement("INSERT INTO player_chat (server_name, world, player_name, message," +
                " is_staff, date) VALUES(?,?,?,?,?,?)");
    }

    public PreparedStatement getPlayerCommandStsm(Connection connection) throws SQLException {

        return connection.prepareStatement("INSERT INTO player_commands (server_name, world, player_name, command," +
                " is_staff, date) VALUES(?,?,?,?,?,?)");
    }

    public PreparedStatement getPlayerSignTextStsm(Connection connection) throws SQLException {

        return connection.prepareStatement("INSERT INTO player_sign_text (server_name, world, x, y, z," +
                " player_name, line, is_staff, date) VALUES(?,?,?,?,?,?,?,?,?)");
    }

    public PreparedStatement getPlayerDeathStsm(Connection connection) throws SQLException {

        return connection.prepareStatement("INSERT INTO player_death (server_name, world, player_name, player_level," +
                " x, y, z, cause, by_who, is_staff, date) VALUES(?,?,?,?,?,?,?,?,?,?,?)");
    }

    public PreparedStatement getPlayerTeleportStsm(Connection connection) throws SQLException {

        return connection.prepareStatement("INSERT INTO player_teleport (server_name, world, player_name, from_x," +
                " from_y, from_z, to_x, to_y, to_z, is_staff, date) VALUES(?,?,?,?,?,?,?,?,?,?,?)");
    }

    public PreparedStatement getPlayerConnectionStsm(Connection connection) throws SQLException {
//TODO convert ip in java instead of database
        return connection.prepareStatement("INSERT INTO player_connection (server_name, world, player_name, x, y," +
                " z, ip, is_staff, date, player_connection_type) VALUES(?,?,?,?,?,?,?,?,?,?)");
    }

    public PreparedStatement getTpsStsm(Connection connection) throws SQLException {

        return connection.prepareStatement("INSERT INTO tps (server_name, tps, date) VALUES(?,?,?)");
    }

    public PreparedStatement getRAMStsm(Connection connection) throws SQLException {

        return connection.prepareStatement("INSERT INTO ram (server_name, total_memory, used_memory," +
                " free_memory, date) VALUES(?,?,?,?,?)");
    }

    public PreparedStatement getPlayerKickStsm(Connection connection) throws SQLException {

        return connection.prepareStatement("INSERT INTO player_kick (server_name, world, player_name, x, y, z," +
                " reason, is_staff, date) VALUES(?,?,?,?,?,?,?,?,?)");
    }

    public PreparedStatement getPortalCreateStsm(Connection connection) throws SQLException {

        return connection.prepareStatement("INSERT INTO portal_creation (server_name, world, caused_by, date) VALUES(?,?,?,?)");
    }

    public PreparedStatement getBucketActionStsm(Connection connection) throws SQLException {

        return connection.prepareStatement("INSERT INTO bucket_action (server_name, world, player_name, bucket," +
                " x, y, z, is_staff, bucket_action, date ) VALUES(?,?,?,?,?,?,?,?,?,?)");
    }

    public PreparedStatement getAnvilStsm(Connection connection) throws SQLException {

        return connection.prepareStatement("INSERT INTO `anvil` (server_name, new_name, player_name, is_staff," +
                " date) VALUES(?,?,?,?,?)");
    }

    public void insertServerStart(String serverName) {

        try (final Connection connection = this.getConnection();
             final PreparedStatement serverStart = connection.prepareStatement("INSERT INTO server_start (server_name) VALUES(?)")) {

            serverStart.setString(1, serverName);

            serverStart.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }


    public void insertServerStop(String serverName) {

        try (final Connection connection = this.getConnection();
             final PreparedStatement serverStop = connection.prepareStatement("INSERT INTO server_stop (server_name) VALUES(?)")) {

            serverStop.setString(1, serverName);

            serverStop.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    public PreparedStatement getEnchantStsm(Connection connection) throws SQLException {

        return connection.prepareStatement("INSERT INTO enchanting (server_name, world, player_name, x, y, z," +
                " enchantment, enchantment_level, item, cost, is_staff, date) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
    }

    public PreparedStatement getBookEditingStsm(Connection connection) throws SQLException {

        return connection.prepareStatement("INSERT INTO book_editing (server_name, world, player_name, page_count," +
                " page_content, signed_by, is_staff, date) VALUES(?,?,?,?,?,?,?,?)");
    }

    public PreparedStatement getAfkStsm(Connection connection) throws SQLException {

        return connection.prepareStatement("INSERT INTO afk (server_name, world, player_name, x, y, z, is_staff, date)" +
                " VALUES(?,?,?,?,?,?,?,?)");
    }

    public PreparedStatement getWrongPasswordStsm(Connection connection) throws SQLException {

        return connection.prepareStatement("INSERT INTO wrong_password (server_name, world, player_name," +
                " is_staff) VALUES(?,?,?,?)");
    }

    public PreparedStatement getItemActionStsm(Connection connection) throws SQLException {

        return connection.prepareStatement("INSERT INTO item_action (server_name, world, player_name, item, amount," +
                " x, y, z ,changed_name, is_staff, item_action_type, date, enchantment) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)");
    }

    public PreparedStatement getFurnaceStsm(Connection connection) throws SQLException {

        return connection.prepareStatement("INSERT INTO furnace (server_name, world, player_name, item, amount," +
                " x, y, z, is_staff, date) VALUES(?,?,?,?,?,?,?,?,?,?)");
    }

    public void insertRCON(String serverName, String command) {

        try (final Connection connection = this.getConnection();
             final PreparedStatement rCon = connection.prepareStatement("INSERT INTO rcon (server_name, command," +
                     " date) VALUES(?,?,?)")) {

            rCon.setString(1, serverName);
            rCon.setString(2, command);

            rCon.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    public PreparedStatement getGamemodeStsm(Connection connection) throws SQLException {

        return connection.prepareStatement("INSERT INTO game_mode (server_name, world, player_name, game_mode," +
                " is_staff, date) VALUES(?,?,?,?,?,?)");
    }

    public PreparedStatement getPlayerCraftStsm(Connection connection) throws SQLException {

        return connection.prepareStatement("INSERT INTO crafting (server_name, world, player_name, item," +
                " amount, x, y, z, is_staff, date) VALUES(?,?,?,?,?,?,?,?,?,?)");
    }

    public PreparedStatement getVaultStsm(Connection connection) throws SQLException {

        return connection.prepareStatement("INSERT INTO vault (server_name, player_name, old_balance, new_balance," +
                " is_staff) VALUES(?,?,?,?,?)");
    }

    public void insertPlayerRegistration(String serverName, String playerName, String playeruuid, String joinDate) {

        try (final Connection connection = this.getConnection();
             final PreparedStatement register = connection.prepareStatement("REPLACE INTO registration (server_name," +
                     " player_name, player_uuid, join_date) VALUES(?,?,?,?)")) {

            register.setString(1, serverName);
            register.setString(2, playerName);
            register.setString(3, playeruuid);
            register.setString(4, joinDate);

            register.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }


    public PreparedStatement getPrimedTntStsm(Connection connection) throws SQLException {

        return connection.prepareStatement("INSERT INTO primed_tnt (server_name, world, player_uuid, player_name," +
                " x, y, z, is_staff, date) VALUES(?,?,?,?,?,?,?,?,?)");
    }


    public PreparedStatement getLiteBansStsm(Connection connection) throws SQLException {

        return connection.prepareStatement("INSERT INTO litebans (server_name, sender, command, onwho, reason," +
                " duration, is_silent, date) VALUES(?,?,?,?,?,?,?,?)");
    }


    public PreparedStatement getAdvancedDataStsm(Connection connection) throws SQLException {

        return connection.prepareStatement("INSERT INTO advanced_ban (server_name, type, executor_uuid, executor, executed_on," +
                " reason, expiration_date, date) VALUES(?,?,?,?,?,?,?)");
    }


    public PreparedStatement getCommandBlockStsm(Connection connection) throws SQLException {

        return connection.prepareStatement("INSERT INTO command_block (server_name, command, date) VALUES(?,?,?)");
    }


    public PreparedStatement getWoodStrippingStsm(Connection connection) throws SQLException {

        return connection.prepareStatement("INSERT INTO wood_stripping (server_name, world, uuid, player_name," +
                " log_name, x, y, z, is_staff, date) VALUES(?,?,?,?,?,?,?,?,?,?)");
    }


    public PreparedStatement getChestInteractionStsm(Connection connection) throws SQLException {

        return connection.prepareStatement("INSERT INTO chest_interaction (server_name, world, player_uuid," +
                " player_name, x, y, z, items, is_staff, date) VALUES(?,?,?,?,?,?,?,?,?,?)");
    }


    public PreparedStatement getEntityDeathStsm(Connection connection) throws SQLException {

        return connection.prepareStatement("INSERT INTO entity_death (server_name, world, player_uuid, player_name," +
                " mob, x, y, z, is_staff, date) VALUES(?,?,?,?,?,?,?,?,?,?)");
    }


    public PreparedStatement getConsoleCommandStsm(Connection connection) throws SQLException {

        return connection.prepareStatement("INSERT INTO console_commands (server_name, command, date) VALUES(?,?,?)");
    }


    public void insertServerReload(String serverName, String playerName, boolean isStaff) {

    }


    public PreparedStatement getPlayerLoginStsm(Connection connection) {
        return null;
    }


    public PreparedStatement getServerSwitchStsm(Connection connection) throws SQLException {
        return null;
    }

    public PreparedStatement getServerAddressStsm(Connection connection) throws SQLException {

        return connection.prepareStatement("INSERT INTO server_address (server_name, player_name, player_uuid, " +
                "domain, date) VALUES(?,?,?,?,?)");
    }


    public PreparedStatement getPAFPartyMessageStsm(Connection connection) throws SQLException {
        return null;
    }


    public PreparedStatement getPAFFriendMessageStsm(Connection connection) throws SQLException {
        return null;
    }

    public PreparedStatement getLeverInteractionStsm(Connection connection) throws SQLException {

        return connection.prepareStatement("INSERT INTO lever_interaction (date, server_name, world, player_uuid, player_name, " +
                "x, y, z, is_staff) VALUES(?,?,?,?,?,?,?,?,?)");
    }

    public PreparedStatement getSpawnEggStsm(Connection connection) throws SQLException {
        return null;
    }

    public PreparedStatement getWorldGuardStsm(Connection connection) throws SQLException {
        return null;
    }

    public PreparedStatement getPlayerCountStsm(Connection connection) throws SQLException {

        return connection.prepareStatement("INSERT INTO player_count (server_name, date) VALUES(?,?)");
    }

    public List<PlayerChat> getPlayerChatByPlayerName(String playerName, int offset, int limit) {
        return Collections.emptyList();
    }

    public Long countByTable(String action) {
        return 0L;
    }

    public PreparedStatement getBlockInteractionStsm(Connection connection) throws SQLException {

        return connection.prepareStatement("INSERT INTO block_interaction (server_name, world, player_name, block," +
                " x, y, z, is_staff, interaction_type, date) VALUES(?,?,?,?,?,?,?,?,?,?)");
    }

    public PreparedStatement getStandCrystalStsm(Connection connection) throws SQLException {

        return connection.prepareStatement("INSERT INTO armorstand_endcrystal (server_name, world, player_name," +
                " player_uuid, x, y, z, is_staff, interaction_type, date, block) VALUES(?,?,?,?,?,?,?,?,?,?,?)");
    }

    public PreparedStatement getArmorStandStsm(Connection connection) throws SQLException {

        return connection.prepareStatement("INSERT INTO armorstand_interaction (server_name, world, player_name," +
                " player_uuid, x, y, z, is_staff, date) VALUES(?,?,?,?,?,?,?,?,?)");
    }

    public PreparedStatement getLevelChangeStsm(Connection connection) throws SQLException {

        return connection.prepareStatement("INSERT INTO player_level (server_name, player_name, is_staff, date, player_level) VALUES(?,?,?,?,?)");
    }

    public void insertAnvil(String serverName, String playerName, String newName, boolean isStaff) { //todo Sidna

        try (final Connection connection = this.getConnection();
             final PreparedStatement anvil = connection.prepareStatement("INSERT INTO anvil" +
                     " (server_name, player_name, new_name, is_staff) VALUES(?,?,?,?)")) {

            anvil.setString(1, serverName);
            anvil.setString(2, playerName);
            anvil.setString(3, newName);
            anvil.setBoolean(4, isStaff);

            anvil.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    public void disconnect(){
        this.getDatabaseQueue().flushQueue();
    }

    public void dropAllTables() {
        try (final Connection connection = this.getConnection();
             final Statement statement = connection.createStatement()) {

            statement.executeUpdate("DROP TABLE IF EXISTS `anvil`, `armorstand_endcrystal`, `armorstand_interaction`, `block_interaction`, `book_editing`, `bucket_action`, `chest_interaction`, `command_block`, `console_commands`, `crafting`, `enchanting`, `entity_death`, `furnace`, `game_mode`, `item_action`, `lever_interaction`, `player_chat`, `player_commands`, `player_connection`, `player_count`, `player_death`, `player_kick`, `player_level`, `player_sign_text`, `player_teleport`, `portal_creation`, `primed_tnt`, `ram`, `rcon`, `registration`, `server_address`, `server_start`, `server_stop`, `tps`;");



        } catch (final SQLException e) {
            e.printStackTrace();
        }
    }
}
