package com.carpour.loggercore.database.mysql;

import com.carpour.loggercore.database.AbstractDataSource;
import com.carpour.loggercore.database.data.DatabaseCredentials;
import com.carpour.loggercore.database.data.Options;
import com.carpour.loggercore.database.entity.Coordinates;
import com.carpour.loggercore.database.entity.EntityPlayer;

import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class MySQL extends AbstractDataSource{

    public MySQL(DatabaseCredentials dbSettings, Options options) throws SQLException {
        super(dbSettings, options, "com.mysql.cj.jdbc.Driver");

    }




    @Override
    public void insertPlayerChat(String serverName, EntityPlayer player, String worldName, String msg) {

        try (final Connection connection = this.dataSource.getConnection();
             final PreparedStatement playerChat = connection.prepareStatement("INSERT INTO player_chat" +
                     " (server_name, world, player_name, message, is_staff) VALUES(?,?,?,?,?)")) {

            playerChat.setString(1, serverName);
            playerChat.setString(2, worldName);
            playerChat.setString(3, player.getPlayerName());
            playerChat.setString(4, msg);
            playerChat.setBoolean(5, player.isStaff());

            playerChat.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertPlayerCommands(String serverName, EntityPlayer player, String worldName, String command) {

        try (final Connection connection = this.dataSource.getConnection();
             final PreparedStatement playerCommands = connection.prepareStatement("INSERT INTO player_commands" +
                     " (server_name, world, player_name, command, is_staff) VALUES(?,?,?,?,?)")) {

            playerCommands.setString(1, serverName);
            playerCommands.setString(2, worldName);
            playerCommands.setString(3, player.getPlayerName());
            playerCommands.setString(4, command);
            playerCommands.setBoolean(5, player.isStaff());

            playerCommands.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertPlayerSignText(String serverName, EntityPlayer player, Coordinates coords, String lines) {

        try (final Connection connection = this.dataSource.getConnection();
             final PreparedStatement playerSignText = connection.prepareStatement("INSERT INTO player_sign_text" +
                     " (server_name, world, x, y, z, player_name, line, is_staff) VALUES(?,?,?,?,?,?,?,?)")) {

            playerSignText.setString(1, serverName);
            playerSignText.setString(2, coords.getWorldName());
            playerSignText.setInt(3, coords.getX());
            playerSignText.setInt(4, coords.getY());
            playerSignText.setInt(5, coords.getZ());
            playerSignText.setString(6, player.getPlayerName());
            playerSignText.setString(7, lines);
            playerSignText.setBoolean(8, player.isStaff());

            playerSignText.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertPlayerDeath(String serverName, EntityPlayer player, int level, String cause, String who, Coordinates coordinates) {

        try (final Connection connection = this.dataSource.getConnection();
             final PreparedStatement playerDeath = connection.prepareStatement("INSERT INTO player_death" +
                     " (server_name, world, player_name, player_level, x, y, z, cause, by_who, is_staff) VALUES(?,?,?,?,?,?,?,?,?,?)")) {

            playerDeath.setString(1, serverName);
            playerDeath.setString(2, coordinates.getWorldName());
            playerDeath.setString(3, player.getPlayerName());
            playerDeath.setInt(4, level);
            playerDeath.setInt(5, coordinates.getX());
            playerDeath.setInt(6, coordinates.getY());
            playerDeath.setInt(7, coordinates.getZ());
            playerDeath.setString(8, cause);
            playerDeath.setString(9, who);
            playerDeath.setBoolean(10, player.isStaff());

            playerDeath.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertPlayerTeleport(String serverName, EntityPlayer player, Coordinates oldCoords, Coordinates newCoords) {

        try (final Connection connection = this.dataSource.getConnection();
             final PreparedStatement playerTeleport = connection.prepareStatement("INSERT INTO player_teleport" +
                     " (server_name, world, player_name, from_x, from_y, from_z, to_x, to_y, to_z, is_staff)" +
                     " VALUES(?,?,?,?,?,?,?,?,?,?)")) {

            playerTeleport.setString(1, serverName);
            playerTeleport.setString(2, oldCoords.getWorldName());
            playerTeleport.setString(3, player.getPlayerName());
            playerTeleport.setInt(4, oldCoords.getX());
            playerTeleport.setInt(5, oldCoords.getY());
            playerTeleport.setInt(6, oldCoords.getZ());
            playerTeleport.setInt(7, newCoords.getX());
            playerTeleport.setInt(8, newCoords.getY());
            playerTeleport.setInt(9, newCoords.getZ());
            playerTeleport.setBoolean(10, player.isStaff());

            playerTeleport.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertPlayerJoin(String serverName, EntityPlayer player, Coordinates coords, InetSocketAddress ip) {

        try (final Connection connection = this.dataSource.getConnection();
             final PreparedStatement playerJoin = connection.prepareStatement("INSERT INTO player_join" +
                     " (server_name, world, player_name, x, y, z, ip, is_staff) VALUES(?,?,?,?,?,?,INET_ATON(?),?)")) {

            playerJoin.setString(1, serverName);
            playerJoin.setString(2, coords.getWorldName());
            playerJoin.setString(3, player.getPlayerName());
            playerJoin.setInt(4, coords.getX());
            playerJoin.setInt(5, coords.getY());
            playerJoin.setInt(6, coords.getZ());
            if (this.options.isPlayerIPEnabled())
                playerJoin.setString(7, ip.getHostString());
            else
                playerJoin.setString(7, null);
            playerJoin.setBoolean(8, player.isStaff());

            playerJoin.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertPlayerLeave(String serverName, EntityPlayer player, Coordinates coords) {

        try (final Connection connection = this.dataSource.getConnection();
             final PreparedStatement playerLeave = connection.prepareStatement("INSERT INTO player_leave" +
                     " (server_name, world, player_name, x, y, z, is_staff) VALUES(?,?,?,?,?,?,?)")) {

            playerLeave.setString(1, serverName);
            playerLeave.setString(2, coords.getWorldName());
            playerLeave.setString(3, player.getPlayerName());
            playerLeave.setInt(4, coords.getX());
            playerLeave.setInt(5, coords.getY());
            playerLeave.setInt(6, coords.getZ());
            playerLeave.setBoolean(7, player.isStaff());

            playerLeave.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertBlockPlace(String serverName, EntityPlayer player, String block, Coordinates coords) {

        try (final Connection connection = this.dataSource.getConnection();
             final PreparedStatement blockPlace = connection.prepareStatement("INSERT INTO block_place" +
                     " (server_name, world, player_name, block, x, y, z, is_staff) VALUES(?,?,?,?,?,?,?,?)")) {

            blockPlace.setString(1, serverName);
            blockPlace.setString(2, coords.getWorldName());
            blockPlace.setString(3, player.getPlayerName());
            blockPlace.setString(4, block);
            blockPlace.setInt(5, coords.getX());
            blockPlace.setInt(6, coords.getY());
            blockPlace.setInt(7, coords.getZ());
            blockPlace.setBoolean(8, player.isStaff());

            blockPlace.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertBlockBreak(String serverName, EntityPlayer player, String blockName, Coordinates coords) {

        try (final Connection connection = this.dataSource.getConnection();
             final PreparedStatement blockBreak = connection.prepareStatement("INSERT INTO block_break" +
                     " (server_name, world, player_name, block, x, y, z, is_staff) VALUES(?,?,?,?,?,?,?,?)")) {

            blockBreak.setString(1, serverName);
            blockBreak.setString(2, coords.getWorldName());
            blockBreak.setString(3, player.getPlayerName());
            blockBreak.setString(4, blockName);
            blockBreak.setInt(5, coords.getX());
            blockBreak.setInt(6, coords.getY());
            blockBreak.setInt(7, coords.getZ());
            blockBreak.setBoolean(8, player.isStaff());

            blockBreak.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertTps(String serverName, double tpss) {

        try (final Connection connection = this.dataSource.getConnection();
             final PreparedStatement tps = connection.prepareStatement("INSERT INTO tps (server_name, tps) VALUES(?,?)")) {

            tps.setString(1, serverName);
            tps.setDouble(2, tpss);

            tps.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertRam(String serverName, long tm, long um, long fm) {

        try (final Connection connection = this.dataSource.getConnection();
             final PreparedStatement ram = connection.prepareStatement("INSERT INTO ram" +
                     " (server_name, total_memory, used_memory, free_memory) VALUES(?,?,?,?)")) {

            ram.setString(1, serverName);
            ram.setLong(2, tm);
            ram.setLong(3, um);
            ram.setLong(4, fm);

            ram.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertPlayerKick(String serverName, EntityPlayer player, Coordinates coords, String reason) {

        try (final Connection connection = this.dataSource.getConnection();
             final PreparedStatement playerKick = connection.prepareStatement("INSERT INTO player_kick" +
                     " (server_name, world, player_name, x, y, z, reason, is_staff) VALUES(?,?,?,?,?,?,?,?)")) {

            playerKick.setString(1, serverName);
            playerKick.setString(2, coords.getWorldName());
            playerKick.setString(3, player.getPlayerName());
            playerKick.setInt(4, coords.getX());
            playerKick.setInt(5, coords.getY());
            playerKick.setInt(6, coords.getZ());
            playerKick.setString(7, reason);
            playerKick.setBoolean(8, player.isStaff());

            playerKick.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertPortalCreate(String serverName, String worldName, String by) {

        try (final Connection connection = this.dataSource.getConnection();
             final PreparedStatement portalCreation = connection.prepareStatement("INSERT INTO portal_creation" +
                     " (server_name, world, caused_by) VALUES(?,?,?)")) {

            portalCreation.setString(1, serverName);
            portalCreation.setString(2, worldName);
            portalCreation.setString(3, by);

            portalCreation.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertLevelChange(String serverName, String playerName, boolean isStaff) {

        try (final Connection connection = this.dataSource.getConnection();
             final PreparedStatement playerLevel = connection.prepareStatement("INSERT INTO player_level" +
                     " (server_name, player_name, is_staff) VALUES(?,?,?)")) {

            playerLevel.setString(1, serverName);
            playerLevel.setString(2, playerName);
            playerLevel.setBoolean(3, isStaff);

            playerLevel.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertBucketFill(String serverName, EntityPlayer player, String bucket, Coordinates coords) {

        try (final Connection connection = this.dataSource.getConnection();
             final PreparedStatement bucketPlace = connection.prepareStatement("INSERT INTO bucket_fill" +
                     " (server_name, world, player_name, bucket, x, y, z, is_staff) VALUES(?,?,?,?,?,?,?,?)")) {

            bucketPlace.setString(1, serverName);
            bucketPlace.setString(2, coords.getWorldName());
            bucketPlace.setString(3, player.getPlayerName());
            bucketPlace.setString(4, bucket);
            bucketPlace.setInt(5, coords.getX());
            bucketPlace.setInt(6, coords.getY());
            bucketPlace.setInt(7, coords.getZ());
            bucketPlace.setBoolean(8, player.isStaff());

            bucketPlace.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertBucketEmpty(String serverName, EntityPlayer player, String bucket, Coordinates coords) {

        try (final Connection connection = this.dataSource.getConnection();
             final PreparedStatement bucketPlace = connection.prepareStatement("INSERT INTO bucket_empty" +
                     " (server_name, world, player_name, bucket, x, y, z, is_staff) VALUES(?,?,?,?,?,?,?,?)")) {


            bucketPlace.setString(1, serverName);
            bucketPlace.setString(2, coords.getWorldName());
            bucketPlace.setString(3, player.getPlayerName());
            bucketPlace.setString(4, bucket);
            bucketPlace.setInt(5, coords.getX());
            bucketPlace.setInt(6, coords.getY());
            bucketPlace.setInt(7, coords.getZ());
            bucketPlace.setBoolean(8, player.isStaff());

            bucketPlace.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertAnvil(String serverName, String playerName, String newName, boolean isStaff) {

        try (final Connection connection = this.dataSource.getConnection();
             final PreparedStatement anvil = connection.prepareStatement("INSERT INTO anvil" +
                     " (server_name, player_name, new_name, is_staff) VALUES(?,?,?,?)")) {

            anvil.setString(1, serverName);
            anvil.setString(2, playerName);
            anvil.setString(3, newName);
            anvil.setBoolean(4, isStaff);

            anvil.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertServerStart(String serverName) {

        try (final Connection connection = this.dataSource.getConnection();
             final PreparedStatement serverStart = connection.prepareStatement("INSERT INTO server_start (server_name) VALUES(?)")) {

            serverStart.setString(1, serverName);

            serverStart.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertServerStop(String serverName) {

        try (final Connection connection = this.dataSource.getConnection();
             final PreparedStatement serverStop = connection.prepareStatement("INSERT INTO server_stop (server_name) VALUES(?)")) {

            serverStop.setString(1, serverName);

            serverStop.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertItemDrop(String serverName, EntityPlayer player, String item, int amount, Coordinates coords, List<String> enchantment, String changedName) {

        try (final Connection connection = this.dataSource.getConnection();
             final PreparedStatement itemDrop = connection.prepareStatement("INSERT INTO item_drop" +
                     " (server_name, world, player_name, item, amount, x, y, z, enchantment, changed_name, is_staff)" +
                     " VALUES(?,?,?,?,?,?,?,?,?,?,?)")) {

            itemDrop.setString(1, serverName);
            itemDrop.setString(2, coords.getWorldName());
            itemDrop.setString(3, player.getPlayerName());
            itemDrop.setString(4, item);
            itemDrop.setInt(5, amount);
            itemDrop.setInt(6, coords.getX());
            itemDrop.setInt(7, coords.getY());
            itemDrop.setInt(8, coords.getZ());
            itemDrop.setString(9, enchantment.toString());
            itemDrop.setString(10, changedName);
            itemDrop.setBoolean(11, player.isStaff());

            itemDrop.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertEnchant(String serverName, EntityPlayer player, List<String> enchantment, int enchantmentLevel,
                              String item, int cost, Coordinates coordinates) {

        try (final Connection connection = this.dataSource.getConnection();
             final PreparedStatement enchanting = connection.prepareStatement("INSERT INTO enchanting" +
                     " (server_name, world, player_name, x, y, z, enchantment, enchantment_level, item, cost, is_staff)" +
                     " VALUES(?,?,?,?,?,?,?,?,?,?,?)")) {

            enchanting.setString(1, serverName);
            enchanting.setString(2, coordinates.getWorldName());
            enchanting.setString(3, player.getPlayerName());
            enchanting.setInt(4, coordinates.getX());
            enchanting.setInt(5, coordinates.getY());
            enchanting.setInt(6, coordinates.getZ());
            enchanting.setString(7, enchantment.toString());
            enchanting.setInt(8, enchantmentLevel);
            enchanting.setString(9, item);
            enchanting.setInt(10, cost);
            enchanting.setBoolean(11, player.isStaff());

            enchanting.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertBookEditing(String serverName, EntityPlayer player, String worldName, int pages, List<String> content, String signedBy) {

        try (final Connection connection = this.dataSource.getConnection();
             final PreparedStatement enchanting = connection.prepareStatement("INSERT INTO book_editing" +
                     " (server_name, world, player_name, page_count, page_content, signed_by, is_staff) VALUES(?,?,?,?,?,?,?)")) {

            enchanting.setString(1, serverName);
            enchanting.setString(2, worldName);
            enchanting.setString(3, player.getPlayerName());
            enchanting.setInt(4, pages);
            enchanting.setString(5, content.toString());
            enchanting.setString(6, signedBy);
            enchanting.setBoolean(7, player.isStaff());

            enchanting.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertAfk(String serverName, EntityPlayer player, Coordinates coords) {

        try (final Connection connection = this.dataSource.getConnection();
             final PreparedStatement afk = connection.prepareStatement("INSERT INTO afk" +
                     " (server_name, world, player_name, x, y, z, is_staff) VALUES(?,?,?,?,?,?,?)")) {

            afk.setString(1, serverName);
            afk.setString(2, coords.getWorldName());
            afk.setString(3, player.getPlayerName());
            afk.setInt(4, coords.getX());
            afk.setInt(5, coords.getY());
            afk.setInt(6, coords.getZ());
            afk.setBoolean(7, player.isStaff());

            afk.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertWrongPassword(String serverName, EntityPlayer player, String worldName) {

        try (final Connection connection = this.dataSource.getConnection();
             final PreparedStatement wrongPassword = connection.prepareStatement("INSERT INTO wrong_password" +
                     " (server_name, world, player_name, is_staff) VALUES(?,?,?,?)")) {

            wrongPassword.setString(1, serverName);
            wrongPassword.setString(2, worldName);
            wrongPassword.setString(3, player.getPlayerName());
            wrongPassword.setBoolean(4, player.isStaff());

            wrongPassword.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertItemPickup(String serverName, EntityPlayer player, String item, int amount, Coordinates coords, String changedName) {

        try (final Connection connection = this.dataSource.getConnection();
             final PreparedStatement itemPickup = connection.prepareStatement("INSERT INTO item_pickup" +
                     " (server_name, world, player_name, item, amount, x, y, z, changed_name, is_staff) VALUES(?,?,?,?,?,?,?,?,?,?)")) {

            itemPickup.setString(1, serverName);
            itemPickup.setString(2, coords.getWorldName());
            itemPickup.setString(3, player.getPlayerName());
            itemPickup.setString(4, item.toString());
            itemPickup.setInt(5, amount);
            itemPickup.setInt(6, coords.getX());
            itemPickup.setInt(7, coords.getY());
            itemPickup.setInt(8, coords.getZ());
            itemPickup.setString(9, changedName);
            itemPickup.setBoolean(10, player.isStaff());

            itemPickup.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertFurnace(String serverName, EntityPlayer player, String item, int amount, Coordinates coords) {

        try (final Connection connection = this.dataSource.getConnection();
             final PreparedStatement furnace = connection.prepareStatement("INSERT INTO furnace" +
                     " (server_name, world, player_name, item, amount, x, y, z, is_staff) VALUES(?,?,?,?,?,?,?,?,?)")) {

            furnace.setString(1, serverName);
            furnace.setString(2, coords.getWorldName());
            furnace.setString(3, player.getPlayerName());
            furnace.setString(4, item);
            furnace.setInt(5, amount);
            furnace.setInt(6, coords.getX());
            furnace.setInt(7, coords.getY());
            furnace.setInt(8, coords.getZ());
            furnace.setBoolean(9, player.isStaff());

            furnace.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertRCON(String serverName, String ip, String command) {

        try (final Connection connection = this.dataSource.getConnection();
             final PreparedStatement rCon = connection.prepareStatement("INSERT INTO rcon" +
                     " (server_name, ip, command) VALUES(?,?,?)")) {

            rCon.setString(1, serverName);
            rCon.setString(2, ip);
            rCon.setString(3, command);

            rCon.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertGameMode(String serverName, EntityPlayer player, String theGameMode, String worldName) {

        try (final Connection connection = this.dataSource.getConnection();
             final PreparedStatement gameMode = connection.prepareStatement("INSERT INTO game_mode" +
                     " (server_name, world, player_name, game_mode, is_staff) VALUES(?,?,?,?,?)")) {

            gameMode.setString(1, serverName);
            gameMode.setString(2, worldName);
            gameMode.setString(3, player.getPlayerName());
            gameMode.setString(4, theGameMode);
            gameMode.setBoolean(5, player.isStaff());

            gameMode.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertPlayerCraft(String serverName, EntityPlayer player, String item, int amount, Coordinates coordinates) {

        try (final Connection connection = this.dataSource.getConnection();
             final PreparedStatement craft = connection.prepareStatement("INSERT INTO crafting" +
                     " (server_name, world, player_name, item, amount, x, y, z, is_staff) VALUES(?,?,?,?,?,?,?,?,?)")) {

            craft.setString(1, serverName);
            craft.setString(2, coordinates.getWorldName());
            craft.setString(3, player.getPlayerName());
            craft.setString(4, item);
            craft.setInt(5, amount);
            craft.setInt(6, coordinates.getX());
            craft.setInt(7, coordinates.getY());
            craft.setInt(8, coordinates.getZ());
            craft.setBoolean(9, player.isStaff());

            craft.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertVault(String serverName, EntityPlayer player, double oldBal, double newBal) {

        try (final Connection connection = this.dataSource.getConnection();
             final PreparedStatement vault = connection.prepareStatement("INSERT INTO vault" +
                     " (server_name, player_name, old_balance, new_balance, is_staff) VALUES(?,?,?,?,?)")) {

            vault.setString(1, serverName);
            vault.setString(2, player.getPlayerName());
            vault.setDouble(3, oldBal);
            vault.setDouble(4, newBal);
            vault.setBoolean(5, player.isStaff());

            vault.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertPlayerRegistration(String serverName, EntityPlayer player, String joinDate) {

        try (final Connection connection = this.dataSource.getConnection();
             final PreparedStatement register = connection.prepareStatement("INSERT INTO registration" +
                     " (server_name, player_name, player_uuid, join_date) VALUES(?,?,?,?)")) {

            register.setString(1, serverName);
            register.setString(2, player.getPlayerName());
            register.setString(3, player.getPlayerUniqueID());
            register.setString(4, joinDate);

            register.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertPrimedTnt(String serverName, EntityPlayer player, Coordinates coords) {

        try (final Connection connection = this.dataSource.getConnection();
             final PreparedStatement primedTNT = connection.prepareStatement("INSERT INTO primed_tnt" +
                     " (server_name, world, player_uuid, player_name, x, y, z, is_staff) VALUES(?,?,?,?,?,?,?,?)")) {

            primedTNT.setString(1, serverName);
            primedTNT.setString(2, coords.getWorldName());
            primedTNT.setString(3, player.getPlayerUniqueID());
            primedTNT.setString(4, player.getPlayerName());
            primedTNT.setInt(5, coords.getX());
            primedTNT.setInt(6, coords.getY());
            primedTNT.setInt(7, coords.getZ());
            primedTNT.setBoolean(8, player.isStaff());

            primedTNT.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertLiteBans(String serverName, String executor, String command, String onWho, String duration, String reason, boolean isSilent) {

        try (final Connection connection = this.dataSource.getConnection();
             final PreparedStatement liteBans = connection.prepareStatement("INSERT INTO litebans" +
                     " (server_name, sender, command, onwho, reason, duration, is_silent) VALUES(?,?,?,?,?,?,?)")) {

            liteBans.setString(1, serverName);
            liteBans.setString(2, executor);
            liteBans.setString(3, command);
            liteBans.setString(4, onWho);
            liteBans.setString(5, duration);
            liteBans.setString(6, reason);
            liteBans.setBoolean(7, isSilent);

            liteBans.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertAdvanceBanData(String serverName, String type, String executor, String executedOn, String reason, long expirationDate) {

        try (final Connection connection = this.dataSource.getConnection();
             final PreparedStatement advancedBan = connection.prepareStatement("INSERT INTO advanced_ban" +
                     " (server_name, type, executor, executed_on, reason, expiration_date) VALUES(?,?,?,?,?,?)")) {

            advancedBan.setString(1, serverName);
            advancedBan.setString(2, type);
            advancedBan.setString(3, executor);
            advancedBan.setString(4, executedOn);
            advancedBan.setString(5, reason);
            advancedBan.setLong(6, expirationDate);

            advancedBan.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertCommandBlock(String serverName, String msg) {

        try (final Connection connection = this.dataSource.getConnection();
             final PreparedStatement commandBlock = connection.prepareStatement("INSERT INTO command_block" +
                     " (server_name, command) VALUES(?,?)")) {

            commandBlock.setString(1, serverName);
            commandBlock.setString(2, msg);

            commandBlock.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertWoodStripping(String serverName, EntityPlayer player, String logName, Coordinates coords) {

        try (final Connection connection = this.dataSource.getConnection();
             final PreparedStatement woodStripping = connection.prepareStatement("INSERT INTO wood_stripping" +
                     " (server_name, world, uuid, player_name, log_name, x, y, z, is_staff) VALUES(?,?,?,?,?,?,?,?,?)")) {

            woodStripping.setString(1, serverName);
            woodStripping.setString(2, coords.getWorldName());
            woodStripping.setString(3, player.getPlayerUniqueID());
            woodStripping.setString(4, player.getPlayerName());
            woodStripping.setString(5, logName);
            woodStripping.setInt(6, coords.getX());
            woodStripping.setInt(7, coords.getY());
            woodStripping.setInt(8, coords.getZ());
            woodStripping.setBoolean(9, player.isStaff());

            woodStripping.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertChestInteraction(String serverName, EntityPlayer player, Coordinates coords, String[] items) {

        try (final Connection connection = this.dataSource.getConnection();
             final PreparedStatement chestInteraction = connection.prepareStatement("INSERT INTO chest_interaction" +
                     " (server_name, world, player_uuid, player_name, x, y, z, items, is_staff) VALUES(?,?,?,?,?,?,?,?,?)")) {

            chestInteraction.setString(1, serverName);
            chestInteraction.setString(2, coords.getWorldName());
            chestInteraction.setString(3, player.getPlayerUniqueID());
            chestInteraction.setString(4, player.getPlayerName());
            chestInteraction.setInt(5, coords.getX());
            chestInteraction.setInt(6, coords.getY());
            chestInteraction.setInt(7, coords.getZ());
            chestInteraction.setString(8, Arrays.toString(items));
            chestInteraction.setBoolean(9, player.isStaff());

            chestInteraction.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertEntityDeath(String serverName, EntityPlayer player, String mob, Coordinates coords) {

        try (final Connection connection = this.dataSource.getConnection();
             final PreparedStatement entityDeath = connection.prepareStatement("INSERT INTO entity_death" +
                     " (server_name, world, player_uuid, player_name, mob, x, y, z, is_staff) VALUES(?,?,?,?,?,?,?,?,?)")) {

            entityDeath.setString(1, serverName);
            entityDeath.setString(2, coords.getWorldName());
            entityDeath.setString(3, player.getPlayerUniqueID());
            entityDeath.setString(4, player.getPlayerName());
            entityDeath.setString(5, mob);
            entityDeath.setInt(6, coords.getX());
            entityDeath.setInt(7, coords.getY());
            entityDeath.setInt(8, coords.getZ());
            entityDeath.setBoolean(9, player.isStaff());

            entityDeath.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    public void emptyTable() {

        if (this.options.getDataDelete() <= 0) return;

        try (final Connection connection = this.dataSource.getConnection();
             final Statement statement = connection.createStatement()) {

            // Player Side Part

            statement.executeUpdate("DELETE FROM player_chat WHERE date < NOW() - INTERVAL " + this.options.getDataDelete() + " DAY");

            statement.executeUpdate("DELETE FROM player_commands WHERE date < NOW() - INTERVAL " + this.options.getDataDelete() + " DAY");

            statement.executeUpdate("DELETE FROM player_sign_text WHERE date < NOW() - INTERVAL " + this.options.getDataDelete() + " DAY");

            statement.executeUpdate("DELETE FROM player_join WHERE date < NOW() - INTERVAL " + this.options.getDataDelete() + " DAY");

            statement.executeUpdate("DELETE FROM player_leave WHERE date < NOW() - INTERVAL " + this.options.getDataDelete() + " DAY");

            statement.executeUpdate("DELETE FROM player_kick WHERE date < NOW() - INTERVAL " + this.options.getDataDelete() + " DAY");

            statement.executeUpdate("DELETE FROM player_death WHERE date < NOW() - INTERVAL " + this.options.getDataDelete() + " DAY");

            statement.executeUpdate("DELETE FROM player_teleport WHERE date < NOW() - INTERVAL " + this.options.getDataDelete() + " DAY");

            statement.executeUpdate("DELETE FROM player_level WHERE date < NOW() - INTERVAL " + this.options.getDataDelete() + " DAY");

            statement.executeUpdate("DELETE FROM block_place WHERE date < NOW() - INTERVAL " + this.options.getDataDelete() + " DAY");

            statement.executeUpdate("DELETE FROM block_break WHERE date < NOW() - INTERVAL " + this.options.getDataDelete() + " DAY");

            statement.executeUpdate("DELETE FROM bucket_fill WHERE date < NOW() - INTERVAL " + this.options.getDataDelete() + " DAY");

            statement.executeUpdate("DELETE FROM bucket_empty WHERE date < NOW() - INTERVAL " + this.options.getDataDelete() + " DAY");

            statement.executeUpdate("DELETE FROM anvil WHERE date < NOW() - INTERVAL " + this.options.getDataDelete() + " DAY");

            statement.executeUpdate("DELETE FROM item_drop WHERE date < NOW() - INTERVAL " + this.options.getDataDelete() + " DAY");

            statement.executeUpdate("DELETE FROM enchanting WHERE date < NOW() - INTERVAL " + this.options.getDataDelete() + " DAY");

            statement.executeUpdate("DELETE FROM book_editing WHERE date < NOW() - INTERVAL " + this.options.getDataDelete() + " DAY");

            statement.executeUpdate("DELETE FROM item_pickup WHERE date < NOW() - INTERVAL " + this.options.getDataDelete() + " DAY");

            statement.executeUpdate("DELETE FROM furnace WHERE date < NOW() - INTERVAL " + this.options.getDataDelete() + " DAY");

            statement.executeUpdate("DELETE FROM game_mode WHERE date < NOW() - INTERVAL " + this.options.getDataDelete() + " DAY");

            statement.executeUpdate("DELETE FROM crafting WHERE date < NOW() - INTERVAL " + this.options.getDataDelete() + " DAY");

            statement.executeUpdate("DELETE FROM registration WHERE date < NOW() - INTERVAL " + this.options.getDataDelete() + " DAY");

            statement.executeUpdate("DELETE FROM primed_tnt WHERE date < NOW() - INTERVAL " + this.options.getDataDelete() + " DAY");

            statement.executeUpdate("DELETE FROM chest_interaction WHERE date < NOW() - INTERVAL " + this.options.getDataDelete() + " DAY");

            statement.executeUpdate("DELETE FROM entity_death WHERE date < NOW() - INTERVAL " + this.options.getDataDelete() + " DAY");

            // Server Side Part
            statement.executeUpdate("DELETE FROM server_start WHERE date < NOW() - INTERVAL " + this.options.getDataDelete() + " DAY");

            statement.executeUpdate("DELETE FROM server_stop WHERE date < NOW() - INTERVAL " + this.options.getDataDelete() + " DAY");

            statement.executeUpdate("DELETE FROM console_commands WHERE date < NOW() - INTERVAL " + this.options.getDataDelete() + " DAY");

            statement.executeUpdate("DELETE FROM ram WHERE date < NOW() - INTERVAL " + this.options.getDataDelete() + " DAY");

            statement.executeUpdate("DELETE FROM tps WHERE date < NOW() - INTERVAL " + this.options.getDataDelete() + " DAY");

            statement.executeUpdate("DELETE FROM portal_creation WHERE date < NOW() - INTERVAL " + this.options.getDataDelete() + " DAY");

            statement.executeUpdate("DELETE FROM rcon WHERE date < NOW() - INTERVAL " + this.options.getDataDelete() + " DAY");

            statement.executeUpdate("DELETE FROM command_block WHERE date < NOW() - INTERVAL " + this.options.getDataDelete() + " DAY");

            // Extras Side Part
            if (this.options.isEssentialsEnabled())
                statement.executeUpdate("DELETE FROM afk WHERE date < NOW() - INTERVAL " + this.options.getDataDelete() + " DAY");

            if (this.options.isAuthMeEnabled())
                statement.executeUpdate("DELETE FROM wrong_password WHERE date < NOW() - INTERVAL " + this.options.getDataDelete() + " DAY");

            if (this.options.isVaultEnabled())
                statement.executeUpdate("DELETE FROM vault WHERE date < NOW() - INTERVAL " + this.options.getDataDelete() + " DAY");

            if (this.options.isLiteBansEnabled())
                statement.executeUpdate("DELETE FROM litebans WHERE date < NOW() - INTERVAL " + this.options.getDataDelete() + " DAY");

            if (this.options.isAdvancedBanEnabled())
                statement.executeUpdate("DELETE FROM advanced_ban WHERE date < NOW() - INTERVAL " + this.options.getDataDelete() + " DAY");

            // Version Exception Part
            if (this.options.isViaVersion())
                statement.executeUpdate("DELETE FROM wood_stripping WHERE date < NOW() - INTERVAL " + this.options.getDataDelete() + " DAY");

        } catch (final SQLException e) {

            this.logger.severe("An error has occurred while cleaning the tables, if the error persists, contact the Authors!");
            e.printStackTrace();

        }
    }

    @Override
    public void insertConsoleCommand(String serverName, String msg) {

        try (final Connection connection = this.dataSource.getConnection();
             final PreparedStatement consoleCommands = connection.prepareStatement("INSERT INTO console_commands" +
                     " (server_name, command) VALUES(?,?)")) {

            consoleCommands.setString(1, serverName);
            consoleCommands.setString(2, msg);

            consoleCommands.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    protected String getJdbcUrl() {
        return ("jdbc:mysql://"
                + this.databaseCredentials.getDbHost()
                + ":" + this.databaseCredentials.getDbPort()
                + "/" + this.databaseCredentials.getDbName());
    }
    public static List<String> getTableNames() { return tablesNames; }
}
