package me.prism3.logger.database.sqlite.global;

import me.prism3.logger.api.*;
import me.prism3.logger.Main;
import me.prism3.logger.utils.enums.NmsVersions;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.world.PortalCreateEvent;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static me.prism3.logger.utils.Data.sqliteDataDel;

public class SQLiteData {

    private static final Main plugin = Main.getInstance();

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss:SSSXXX");

    public void createTable() {

        final PreparedStatement playerChat, playerCommands, consoleCommands, playerSignText, playerJoin, playerLeave,
                playerDeath, playerTeleport, blockPlace, blockBreak, tps, ram, playerKick, portalCreation, playerLevel,
                bucketFill, bucketEmpty, anvil, serverStart, serverStop, itemDrop, enchant, bookEditing, afk,
                wrongPassword, itemPickup, furnace, rCon, gameMode, craft, vault, registration, primedTNT, chestInteraction, liteBans,
                advancedBan, commandBlock, woodStripping, entityDeath, signChange;

        try {

            // Player Side Part
            playerChat = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS player_chat " +
                    "(server_name TEXT(30), date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, world TEXT(100), player_name TEXT(20)," +
                    "message TEXT(256), is_staff INTEGER)");

            playerCommands = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS player_commands" +
                    "(server_name TEXT(30), date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, world TEXT(100), player_name TEXT(40)," +
                    "command TEXT(100), is_staff INTEGER)");

            playerSignText = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS player_sign_text" +
                    "(server_name TEXT(30), date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, world TEXT(50)," +
                    " x INTEGER, y INTEGER, z INTEGER, player_name TEXT(40), line TEXT(100), is_staff INTEGER)");

            playerJoin = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS player_join" +
                    "(server_name TEXT(30), date TIMESTAMP DEFAULT(STRFTIME('%Y-%m-%d %H:%M:%f', 'now')) PRIMARY KEY, world TEXT(50)," +
                    "x INTEGER, y INTEGER, z INTEGER, ip TEXT, is_staff INTEGER)");

            playerLeave = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS player_leave " +
                    "(server_name TEXT(30), date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, world TEXT(50)," +
                    " x INTEGER, y INTEGER, z INTEGER, player_name TEXT(40), is_staff INTEGER)");

            playerDeath = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS player_death" +
                    "(server_name TEXT(30), date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, world TEXT(100), player_name TEXT(40)," +
                    "player_level INTEGER, x INTEGER, y INTEGER, z INTEGER, cause TEXT(100), by_who TEXT(100), is_staff INTEGER)");

            playerTeleport = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS player_teleport" +
                    "(server_name TEXT(30), date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, world TEXT(100), player_name TEXT(40)," +
                    "from_x INTEGER, from_y INTEGER, from_z INTEGER, to_x INTEGER, to_y INTEGER, to_z INTEGER, is_staff INTEGER)");

            blockPlace = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS block_place" +
                    "(server_name TEXT(30), date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, world TEXT(100), player_name TEXT(40)," +
                    "block TEXT(40), x INTEGER, y INTEGER, z INTEGER, is_staff INTEGER)");

            blockBreak = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS block_break" +
                    "(server_name TEXT(30), date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, world TEXT(100), player_name TEXT(40)," +
                    "block TEXT(40), x INTEGER, y INTEGER, z INTEGER, is_staff INTEGER)");

            playerKick = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS player_kick" +
                    "(server_name TEXT(30), date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, world TEXT(100), player_name TEXT(40)," +
                    " x INTEGER, y INTEGER, z INTEGER, reason TEXT(50), is_staff INTEGER )");

            playerLevel = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS player_level" +
                    "(server_name TEXT(30), date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, player_name TEXT(40), is_staff INTEGER )");

            bucketFill = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS bucket_fill" +
                    "(server_name TEXT(30), date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, world TEXT(100), player_name TEXT(40)," +
                    "bucket TEXT(40), x INTEGER, y INTEGER, z INTEGER, is_staff INTEGER)");

            bucketEmpty = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS bucket_empty" +
                    "(server_name TEXT(30), date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, world TEXT(100), player_name TEXT(40)," +
                    "bucket TEXT(40), x INTEGER, y INTEGER, z INTEGER, is_staff INTEGER)");

            anvil = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS anvil" +
                    "(server_name TEXT(30), date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, player_name TEXT(40), new_name TEXT(100), is_staff INTEGER)");

            itemDrop = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS item_drop" +
                    "(server_name TEXT(30), date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, world TEXT(100), player_name TEXT(40)," +
                    "item TEXT(50), amount INTEGER, x INTEGER, y INTEGER, z INTEGER, enchantment TEXT(50), changed_name TEXT(50), is_staff INTEGER)");

            enchant = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS enchanting" +
                    "(server_name TEXT(30), date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, world TEXT(100), player_name TEXT(40)," +
                    "x INTEGER, y INTEGER, z INTEGER, enchantment TEXT(50), enchantment_level INTEGER, item TEXT(50)," +
                    " cost INTEGER, is_staff INTEGER)");

            bookEditing = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS book_editing" +
                    "(server_name TEXT(30), date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, world TEXT(100), player_name TEXT(40)," +
                    "page_count INTEGER, page_content TEXT(100), signed_by TEXT(25), is_staff INTEGER)");

            itemPickup = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS item_pickup" +
                    "(server_name TEXT(30), date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, world TEXT(100), player_name TEXT(40)," +
                    "item TEXT(50), amount INTEGER, x INTEGER, y INTEGER, z INTEGER, changed_name TEXT(100), is_staff INTEGER)");

            furnace = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS furnace" +
                    "(server_name TEXT(30), date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, world TEXT(100), player_name TEXT(40)," +
                    "item TEXT(50), amount INTEGER, x INTEGER, y INTEGER, z INTEGER, is_staff INTEGER)");

            gameMode = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS game_mode" +
                    "(server_name TEXT(30), date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, world TEXT(100), player_name TEXT(40)," +
                    "game_mode TEXT(15), is_staff INTEGER)");

            craft = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS crafting" +
                    "(server_name TEXT(30), date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, world TEXT(100), player_name TEXT(40)," +
                    "item TEXT(50), amount INTEGER, x INTEGER, y INTEGER, z INTEGER, is_staff INTEGER)");

            registration = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS registration" +
                    "(server_name TEXT(30), date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, player_name TEXT(40)," +
                    "player_uuid INTEGER, join_date TEXT(30))");

            primedTNT = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS primed_tnt" +
                    "(server_name TEXT(30), date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, world TEXT(100), player_uuid INTEGER," +
                    "player_name TEXT(40), x INTEGER, y INTEGER, z INTEGER, is_staff INTEGER)");

            chestInteraction = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS chest_interaction" +
                    "(server_name TEXT(30), date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, world TEXT(50), player_uuid INTEGER, " +
                    "player_name TEXT(40), x INTEGER, y INTEGER, z INTEGER, items TEXT, is_staff INTEGER)");

            entityDeath = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS entity_death" +
                    "(server_name TEXT(30), date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, world TEXT(50), player_uuid INTEGER, " +
                    "player_name TEXT(40), mob TEXT(50), x INTEGER, y INTEGER, z INTEGER, is_staff INTEGER)");

            signChange = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS sign_change" +
                    "(server_name TEXT(30), date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, world TEXT(50), player_uuid INTEGER, " +
                    "player_name TEXT(40), x INTEGER, y INTEGER, z INTEGER, new_text TEXT(50), is_staff INTEGER)");

            // Server Side Part
            serverStart = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS server_start" +
                    "(server_name TEXT(30), date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY)");

            serverStop = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS server_stop" +
                    "(server_name TEXT(30), date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY )");

            consoleCommands = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS console_commands" +
                    "(server_name TEXT(30), date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, command TEXT(256))");

            ram = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS ram" +
                    "(server_name TEXT(30), date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY," +
                    " total_memory INTEGER, used_memory INTEGER, free_memory INTEGER)");

            tps = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS tps" +
                    "(server_name TEXT(30), date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, tps INTEGER)");

            portalCreation = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS portal_creation" +
                    "(server_name TEXT(30), date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, world TEXT(50), caused_by TEXT(50))");

            rCon = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS rcon" +
                    "(server_name TEXT(30), date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, command TEXT(100))");

            commandBlock = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS command_block" +
                    "(server_name TEXT(30), date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, command TEXT(256))");

            // Extras Side Part
            if (EssentialsUtil.getEssentialsAPI() != null) {

                afk = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS afk" +
                        "(server_name TEXT(30), date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, world TEXT(100), player_name TEXT(40)," +
                        "x INTEGER, y INTEGER, z INTEGER, is_staff INTEGER)");

                afk.executeUpdate();
                afk.close();
            }

            if (AuthMeUtil.getAuthMeAPI() != null) {

                wrongPassword = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS wrong_password" +
                        "(server_name TEXT(30), date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, world TEXT(100), player_name TEXT(40)," +
                        "is_staff INTEGER)");

                wrongPassword.executeUpdate();
                wrongPassword.close();
            }

            if (VaultUtil.getVaultAPI() && VaultUtil.getVault() != null) {

                vault = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS vault" +
                        "(server_name TEXT(30), date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, player_name TEXT(30)," +
                        " old_balance REAL(200), new_balance REAL(200), is_staff INTEGER)");

                vault.executeUpdate();
                vault.close();
            }

            if (LiteBansUtil.getLiteBansAPI() != null) {

                liteBans = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS litebans" +
                        "(server_name TEXT(30), date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, sender TEXT(30), command TEXT(10)," +
                        " onwho TEXT(30), reason TEXT(60), duration TEXT(50), is_silent INTEGER)");

                liteBans.executeUpdate();
                liteBans.close();

            }

            if (AdvancedBanUtil.getAdvancedBanAPI() != null) {

                advancedBan = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS advanced_ban " +
                        "(server_name, date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, type VARCHAR(30), executor TEXT(30)," +
                        " executed_on TEXT(30), reason TEXT(120), expiration_date VARCHAR(60))");

                advancedBan.executeUpdate();
                advancedBan.close();
            }

            // Version Exception Part
            if (plugin.getVersion().isAtLeast(NmsVersions.v1_13_R1)) {

                woodStripping = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS wood_stripping" +
                        "(server_name TEXT(30), date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, world TEXT(100)," +
                        "player_uuid TEXT(100), player_name TEXT(40), log_name TEXT(50), x INTEGER, y INTEGER, z INTEGER, is_staff INTEGER)");

                woodStripping.executeUpdate();
                woodStripping.close();
            }

            playerChat.executeUpdate();
            playerChat.close();
            playerCommands.executeUpdate();
            playerCommands.close();
            playerSignText.executeUpdate();
            playerSignText.close();
            playerDeath.executeUpdate();
            playerDeath.close();
            playerTeleport.executeUpdate();
            playerTeleport.close();
            playerJoin.executeUpdate();
            playerJoin.close();
            playerLeave.executeUpdate();
            playerLeave.close();
            blockPlace.executeUpdate();
            blockPlace.close();
            blockBreak.executeUpdate();
            blockBreak.close();
            playerKick.executeUpdate();
            playerKick.close();
            playerLevel.executeUpdate();
            playerLevel.close();
            bucketFill.executeUpdate();
            bucketFill.close();
            bucketEmpty.executeUpdate();
            bucketEmpty.close();
            anvil.executeUpdate();
            anvil.close();
            itemDrop.executeUpdate();
            itemDrop.close();
            enchant.executeUpdate();
            enchant.close();
            bookEditing.executeUpdate();
            bookEditing.close();
            itemPickup.executeUpdate();
            itemPickup.close();
            furnace.executeUpdate();
            furnace.close();
            gameMode.executeUpdate();
            gameMode.close();
            craft.executeUpdate();
            craft.close();
            registration.executeUpdate();
            registration.close();
            primedTNT.executeUpdate();
            primedTNT.close();
            chestInteraction.executeUpdate();
            chestInteraction.close();
            entityDeath.executeUpdate();
            entityDeath.close();
            signChange.executeUpdate();
            signChange.close();

            serverStart.executeUpdate();
            serverStart.close();
            serverStop.executeUpdate();
            serverStop.close();
            consoleCommands.executeUpdate();
            consoleCommands.close();
            ram.executeUpdate();
            ram.close();
            tps.executeUpdate();
            tps.close();
            portalCreation.executeUpdate();
            portalCreation.close();
            rCon.executeUpdate();
            rCon.close();
            commandBlock.executeUpdate();
            commandBlock.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertPlayerChat(String serverName, Player player, String message, boolean staff) {

        try {
            final PreparedStatement playerChat = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO player_chat (server_name, date, world, player_name, message, is_staff) VALUES (?,?,?,?,?,?)");
            playerChat.setString(1, serverName);
            playerChat.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            playerChat.setString(3, player.getWorld().getName());
            playerChat.setString(4, player.getName());
            playerChat.setString(5, message);
            playerChat.setBoolean(6, staff);

            playerChat.executeUpdate();
            playerChat.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertPlayerCommands(String serverName, Player player, String command, boolean staff) {
        try {
            final PreparedStatement playerCommands = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO player_commands (server_name, date, world, player_name, command, is_staff) VALUES (?,?,?,?,?,?)");
            playerCommands.setString(1, serverName);
            playerCommands.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            playerCommands.setString(3, player.getWorld().getName());
            playerCommands.setString(4, player.getName());
            playerCommands.setString(5, command);
            playerCommands.setBoolean(6, staff);

            playerCommands.executeUpdate();
            playerCommands.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertConsoleCommands(String serverName, String command) {
        try {
            final PreparedStatement consoleCommands = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO console_commands (server_name, date, command) VALUES (?,?,?)");
            consoleCommands.setString(1, serverName);
            consoleCommands.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            consoleCommands.setString(3, command);

            consoleCommands.executeUpdate();
            consoleCommands.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertPlayerSignText(String serverName, Player player, String lines, boolean isStaff) {
        try {
            final PreparedStatement playerSignText = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO player_sign_text (server_name, date, world, x, y, z, player_name, line, is_staff) VALUES (?,?,?,?,?,?,?,?,?)");
            playerSignText.setString(1, serverName);
            playerSignText.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            playerSignText.setString(3, player.getWorld().getName());
            playerSignText.setInt(4, player.getLocation().getBlockX());
            playerSignText.setInt(5, player.getLocation().getBlockY());
            playerSignText.setInt(6, player.getLocation().getBlockZ());
            playerSignText.setString(7, player.getName());
            playerSignText.setString(8, lines);
            playerSignText.setBoolean(9, isStaff);

            playerSignText.executeUpdate();
            playerSignText.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertPlayerDeath(String serverName, Player player, String cause, String byWho, boolean isStaff) {
        try {
            final PreparedStatement playerDeath = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO player_death (server_name, date, world, player_name, player_level, x, y, z, cause, by_who, is_staff) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
            playerDeath.setString(1, serverName);
            playerDeath.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            playerDeath.setString(3, player.getWorld().getName());
            playerDeath.setString(4, player.getName());
            playerDeath.setInt(5, player.getLevel());
            playerDeath.setInt(6, player.getLocation().getBlockX());
            playerDeath.setInt(7, player.getLocation().getBlockY());
            playerDeath.setInt(8, player.getLocation().getBlockZ());
            playerDeath.setString(9, cause);
            playerDeath.setString(10, byWho);
            playerDeath.setBoolean(11, isStaff);

            playerDeath.executeUpdate();
            playerDeath.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertPlayerTeleport(String serverName, Player player, Location from, Location to, boolean isStaff) {
        try {
            final PreparedStatement playerTeleport = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO player_teleport (server_name, date, world, player_name, from_x, from_y, from_z, to_x, to_y, to_z, is_staff) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
            playerTeleport.setString(1, serverName);
            playerTeleport.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            playerTeleport.setString(3, player.getWorld().getName());
            playerTeleport.setString(4, player.getName());
            playerTeleport.setInt(5, from.getBlockX());
            playerTeleport.setInt(6, from.getBlockY());
            playerTeleport.setInt(7, from.getBlockZ());
            playerTeleport.setInt(8, to.getBlockX());
            playerTeleport.setInt(9, to.getBlockY());
            playerTeleport.setInt(10, to.getBlockZ());
            playerTeleport.setBoolean(11, isStaff);

            playerTeleport.executeUpdate();
            playerTeleport.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertPlayerJoin(String serverName, Player player, boolean isStaff) {
        try {
            final PreparedStatement playerJoin = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO player_join (server_name, world, x, y, z, ip, is_staff) VALUES (?,?,?,?,?,?,?)");
            playerJoin.setString(1, serverName);
            playerJoin.setString(2, player.getWorld().getName());
            playerJoin.setInt(3, player.getLocation().getBlockX());
            playerJoin.setInt(4, player.getLocation().getBlockY());
            playerJoin.setInt(5, player.getLocation().getBlockZ());
            if (plugin.getConfig().getBoolean("Player-Join.Player-ip")) {
                playerJoin.setString(6, Objects.requireNonNull(player.getAddress()).getHostString());
            } else {
                playerJoin.setString(6, null);
            }
            playerJoin.setBoolean(7, isStaff);

            playerJoin.executeUpdate();
            playerJoin.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertPlayerLeave(String serverName, Player player, boolean isStaff) {
        try {
            final PreparedStatement playerLeave = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO player_leave (server_name, date, world, x, y, z, player_name, is_staff) VALUES (?,?,?,?,?,?,?,?)");
            playerLeave.setString(1, serverName);
            playerLeave.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            playerLeave.setString(3, player.getWorld().getName());
            playerLeave.setInt(4, player.getLocation().getBlockX());
            playerLeave.setInt(5, player.getLocation().getBlockY());
            playerLeave.setInt(6, player.getLocation().getBlockZ());
            playerLeave.setString(7, player.getName());
            playerLeave.setBoolean(8, isStaff);

            playerLeave.executeUpdate();
            playerLeave.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertBlockPlace(String serverName, Player player, Material block, boolean isStaff) {
        try {
            final PreparedStatement blockPlace = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO block_place (server_name, date, world, player_name, block, x, y, z, is_staff) VALUES (?,?,?,?,?,?,?,?,?)");
            blockPlace.setString(1, serverName);
            blockPlace.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            blockPlace.setString(3, player.getWorld().getName());
            blockPlace.setString(4, player.getName());
            blockPlace.setString(5, block.toString());
            blockPlace.setInt(6, player.getLocation().getBlockX());
            blockPlace.setInt(7, player.getLocation().getBlockY());
            blockPlace.setInt(8, player.getLocation().getBlockZ());
            blockPlace.setBoolean(9, isStaff);

            blockPlace.executeUpdate();
            blockPlace.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertBlockBreak(String serverName, Player player, Material block, boolean isStaff) {
        try {
            final PreparedStatement blockBreak = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO block_break (server_name, date, world, player_name, block, x, y, z, is_staff) VALUES (?,?,?,?,?,?,?,?,?)");
            blockBreak.setString(1, serverName);
            blockBreak.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            blockBreak.setString(3, player.getWorld().getName());
            blockBreak.setString(4, player.getName());
            blockBreak.setString(5, block.toString());
            blockBreak.setInt(6, player.getLocation().getBlockX());
            blockBreak.setInt(7, player.getLocation().getBlockY());
            blockBreak.setInt(8, player.getLocation().getBlockZ());
            blockBreak.setBoolean(9, isStaff);

            blockBreak.executeUpdate();
            blockBreak.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertTPS(String serverName, double tps) {
        try {
            final PreparedStatement tpsStatement = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO tps (server_name, date, tps) VALUES (?,?,?)");

            tpsStatement.setString(1, serverName);
            tpsStatement.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            tpsStatement.setDouble(3, tps);

            tpsStatement.executeUpdate();
            tpsStatement.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertRAM(String serverName, long totalMemory, long usedMemory, long freeMemory) {
        try {
            final PreparedStatement ramStatement = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO ram (server_name, date, total_memory, used_memory, free_memory) VALUES (?,?,?,?,?)");
            ramStatement.setString(1, serverName);
            ramStatement.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            ramStatement.setLong(3, totalMemory);
            ramStatement.setLong(4, usedMemory);
            ramStatement.setLong(5, freeMemory);

            ramStatement.executeUpdate();
            ramStatement.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertPlayerKick(String serverName, Player player, String reason, boolean isStaff) {
        try {
            final PreparedStatement playerKickStatement = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO player_kick (server_name, date, world, player_name, x, y, z, reason, is_staff) VALUES (?,?,?,?,?,?,?,?,?)");
            playerKickStatement.setString(1, serverName);
            playerKickStatement.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            playerKickStatement.setString(3, player.getWorld().getName());
            playerKickStatement.setString(4, player.getName());
            playerKickStatement.setInt(5, player.getLocation().getBlockX());
            playerKickStatement.setInt(6, player.getLocation().getBlockY());
            playerKickStatement.setInt(7, player.getLocation().getBlockZ());
            playerKickStatement.setString(8, reason);
            playerKickStatement.setBoolean(9, isStaff);

            playerKickStatement.executeUpdate();
            playerKickStatement.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertPortalCreate(String serverName, String worldName, PortalCreateEvent.CreateReason reason) {
        try {
            final PreparedStatement portalCreateStatement = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO portal_creation (server_name, date, world, caused_by) VALUES (?,?,?,?)");
            portalCreateStatement.setString(1, serverName);
            portalCreateStatement.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            portalCreateStatement.setString(3, worldName);
            portalCreateStatement.setString(4, reason.toString());

            portalCreateStatement.executeUpdate();
            portalCreateStatement.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertLevelChange(String serverName, Player player, boolean isStaff) {
        try {
            final PreparedStatement levelChangeStatement = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO player_level (server_name, date, player_name, is_staff) VALUES (?,?,?,?)");
            levelChangeStatement.setString(1, serverName);
            levelChangeStatement.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            levelChangeStatement.setString(3, player.getName());
            levelChangeStatement.setBoolean(4, isStaff);

            levelChangeStatement.executeUpdate();
            levelChangeStatement.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertBucketFill(String serverName, Player player, String bucket, boolean isStaff) {
        try {
            final PreparedStatement bucketFillStatement = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO bucket_fill (server_name, date, world, player_name, bucket, x, y, z, is_staff) VALUES (?,?,?,?,?,?,?,?,?)");
            bucketFillStatement.setString(1, serverName);
            bucketFillStatement.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            bucketFillStatement.setString(3, player.getWorld().getName());
            bucketFillStatement.setString(4, player.getName());
            bucketFillStatement.setString(5, bucket);
            bucketFillStatement.setInt(6, player.getLocation().getBlockX());
            bucketFillStatement.setInt(7, player.getLocation().getBlockY());
            bucketFillStatement.setInt(8, player.getLocation().getBlockZ());
            bucketFillStatement.setBoolean(9, isStaff);

            bucketFillStatement.executeUpdate();
            bucketFillStatement.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertBucketEmpty(String serverName, Player player, String bucket, boolean isStaff) {
        try {
            final PreparedStatement bucketEmptyStatement = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO bucket_empty (server_name, date, world, player_name, bucket, x, y, z, is_staff) VALUES (?,?,?,?,?,?,?,?,?)");
            bucketEmptyStatement.setString(1, serverName);
            bucketEmptyStatement.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            bucketEmptyStatement.setString(3, player.getWorld().getName());
            bucketEmptyStatement.setString(4, player.getName());
            bucketEmptyStatement.setString(5, bucket);
            bucketEmptyStatement.setInt(6, player.getLocation().getBlockX());
            bucketEmptyStatement.setInt(7, player.getLocation().getBlockY());
            bucketEmptyStatement.setInt(8, player.getLocation().getBlockZ());
            bucketEmptyStatement.setBoolean(9, isStaff);

            bucketEmptyStatement.executeUpdate();
            bucketEmptyStatement.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertAnvil(String serverName, Player player, String newName, boolean isStaff) {
        try {
            final PreparedStatement anvilStatement = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO anvil (server_name, date, player_name, new_name, is_staff) VALUES (?,?,?,?,?)");
            anvilStatement.setString(1, serverName);
            anvilStatement.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            anvilStatement.setString(3, player.getName());
            anvilStatement.setString(4, newName);
            anvilStatement.setBoolean(5, isStaff);

            anvilStatement.executeUpdate();
            anvilStatement.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertServerStart(String serverName) {
        try {
            final PreparedStatement serverStartStatement = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO server_start (server_name, date) VALUES (?,?)");
            serverStartStatement.setString(1, serverName);
            serverStartStatement.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));

            serverStartStatement.executeUpdate();
            serverStartStatement.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertServerStop(String serverName) {
        try {
            final PreparedStatement serverStopStatement = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO server_stop (server_name, date) VALUES (?,?)");
            serverStopStatement.setString(1, serverName);
            serverStopStatement.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));

            serverStopStatement.executeUpdate();
            serverStopStatement.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertItemDrop(String serverName, Player player, String item, int amount, int x, int y, int z, List<String> enchantment, String changedName, boolean isStaff) {
        try {
            final PreparedStatement itemDropStatement = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO item_drop (server_name, date, world, player_name, item, amount, x, y, z, enchantment, changed_name, is_staff) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");
            itemDropStatement.setString(1, serverName);
            itemDropStatement.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            itemDropStatement.setString(3, player.getWorld().getName());
            itemDropStatement.setString(4, player.getName());
            itemDropStatement.setString(5, item);
            itemDropStatement.setInt(6, amount);
            itemDropStatement.setInt(7, x);
            itemDropStatement.setInt(8, y);
            itemDropStatement.setInt(9, z);
            itemDropStatement.setString(10, enchantment.toString());
            itemDropStatement.setString(11, changedName);
            itemDropStatement.setBoolean(12, isStaff);

            itemDropStatement.executeUpdate();
            itemDropStatement.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertEnchant(String serverName, Player player, List<String> enchantment, int enchantmentLevel, String item, int cost, boolean isStaff) {
        try {
            final PreparedStatement enchantStatement = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO enchanting (server_name, date, world, player_name, x, y, z, enchantment, enchantment_level, item, cost, is_staff) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");
            enchantStatement.setString(1, serverName);
            enchantStatement.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            enchantStatement.setString(3, player.getWorld().getName());
            enchantStatement.setString(4, player.getName());
            enchantStatement.setInt(5, player.getLocation().getBlockX());
            enchantStatement.setInt(6, player.getLocation().getBlockY());
            enchantStatement.setInt(7, player.getLocation().getBlockZ());
            enchantStatement.setString(8, enchantment.toString());
            enchantStatement.setInt(9, enchantmentLevel);
            enchantStatement.setString(10, item);
            enchantStatement.setInt(11, cost);
            enchantStatement.setBoolean(12, isStaff);

            enchantStatement.executeUpdate();
            enchantStatement.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertBook(String serverName, Player player, int pages, List<String> content, String signedBy, boolean staff) {
        try {
            final PreparedStatement bookEditing = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO book_editing (server_name, date, world, player_name, page_count, page_content, signed_by, is_staff) VALUES(?,?,?,?,?,?,?,?)");
            bookEditing.setString(1, serverName);
            bookEditing.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            bookEditing.setString(3, player.getWorld().getName());
            bookEditing.setString(4, player.getName());
            bookEditing.setInt(5, pages);
            bookEditing.setString(6, content.toString());
            bookEditing.setString(7, signedBy);
            bookEditing.setBoolean(8, staff);

            bookEditing.executeUpdate();
            bookEditing.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertAFK(String serverName, Player player, boolean isStaff) {

        if (EssentialsUtil.getEssentialsAPI() != null) {

            try {
                final PreparedStatement enchantStatement = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO afk (server_name, date, world, player_name, x, y, z, is_staff) VALUES (?,?,?,?,?,?,?,?)");
                enchantStatement.setString(1, serverName);
                enchantStatement.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
                enchantStatement.setString(3, player.getWorld().getName());
                enchantStatement.setString(4, player.getName());
                enchantStatement.setInt(5, player.getLocation().getBlockX());
                enchantStatement.setInt(6, player.getLocation().getBlockY());
                enchantStatement.setInt(7, player.getLocation().getBlockZ());
                enchantStatement.setBoolean(8, isStaff);

                enchantStatement.executeUpdate();
                enchantStatement.close();

            } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public static void insertWrongPassword(String serverName, Player player, boolean isStaff) {

        try {
            final PreparedStatement wrongPasswordStatement = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO wrong_password (server_name, date, world, player_name, is_staff) VALUES (?,?,?,?,?)");
            wrongPasswordStatement.setString(1, serverName);
            wrongPasswordStatement.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            wrongPasswordStatement.setString(3, player.getWorld().getName());
            wrongPasswordStatement.setString(4, player.getName());
            wrongPasswordStatement.setBoolean(5, isStaff);

            wrongPasswordStatement.executeUpdate();
            wrongPasswordStatement.close();

        } catch (SQLException e) { e.printStackTrace(); }

    }

    public static void insertItemPickup(String serverName, Player player, Material item, int amount, int x, int y, int z, String changed_name, boolean isStaff) {
        try {
            final PreparedStatement itemPickupStatement = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO item_pickup (server_name, date, world, player_name, item, amount, x, y, z, changed_name, is_staff) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
            itemPickupStatement.setString(1, serverName);
            itemPickupStatement.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            itemPickupStatement.setString(3, player.getWorld().getName());
            itemPickupStatement.setString(4, player.getName());
            itemPickupStatement.setString(5, item.toString());
            itemPickupStatement.setInt(6, amount);
            itemPickupStatement.setInt(7, x);
            itemPickupStatement.setInt(8, y);
            itemPickupStatement.setInt(9, z);
            itemPickupStatement.setString(10, changed_name);
            itemPickupStatement.setBoolean(11, isStaff);

            itemPickupStatement.executeUpdate();
            itemPickupStatement.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertFurnace(String serverName, Player player, Material item, int amount, int x, int y, int z, boolean isStaff) {
        try {
            final PreparedStatement furnaceStatement = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO furnace (server_name, date, world, player_name, item, amount, x, y, z, is_staff) VALUES (?,?,?,?,?,?,?,?,?,?)");
            furnaceStatement.setString(1, serverName);
            furnaceStatement.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            furnaceStatement.setString(3, player.getWorld().getName());
            furnaceStatement.setString(4, player.getName());
            furnaceStatement.setString(5, item.toString());
            furnaceStatement.setInt(6, amount);
            furnaceStatement.setInt(7, x);
            furnaceStatement.setInt(8, y);
            furnaceStatement.setInt(9, z);
            furnaceStatement.setBoolean(10, isStaff);

            furnaceStatement.executeUpdate();
            furnaceStatement.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertRcon(String serverName, String command) {
        try {
            final PreparedStatement rCon = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO rcon (server_name, date, Command) VALUES (?,?,?)");
            rCon.setString(1, serverName);
            rCon.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            rCon.setString(3, command);

            rCon.executeUpdate();
            rCon.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertGameMode(String serverName, Player player, String gameMode, boolean staff) {
        try {
            final PreparedStatement gameModeStatement = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO game_mode (server_name, date, world, player_name, game_mode, is_staff) VALUES (?,?,?,?,?,?)");
            gameModeStatement.setString(1, serverName);
            gameModeStatement.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            gameModeStatement.setString(3, player.getWorld().toString());
            gameModeStatement.setString(4, player.getName());
            gameModeStatement.setString(5, gameMode);
            gameModeStatement.setBoolean(6, staff);

            gameModeStatement.executeUpdate();
            gameModeStatement.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertPlayerCraft(String serverName, Player player, String item, int amount, int x, int y, int z, boolean isStaff) {
        try {
            final PreparedStatement craftStatement = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO crafting (server_name, date, world, player_name, item, amount, x, y, z, is_staff) VALUES (?,?,?,?,?,?,?,?,?,?)");
            craftStatement.setString(1, serverName);
            craftStatement.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            craftStatement.setString(3, player.getWorld().getName());
            craftStatement.setString(4, player.getName());
            craftStatement.setString(5, item);
            craftStatement.setInt(6, amount);
            craftStatement.setInt(7, x);
            craftStatement.setInt(8, y);
            craftStatement.setInt(9, z);
            craftStatement.setBoolean(10, isStaff);

            craftStatement.executeUpdate();
            craftStatement.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertVault(String serverName, Player player, double oldBal, double newBal, boolean isStaff) {
        try {
            final PreparedStatement vault = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO vault (server_name, date, player_name, old_balance, new_balance, is_staff) VALUES (?,?,?,?,?,?)");
            vault.setString(1, serverName);
            vault.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            vault.setString(3, player.getName());
            vault.setDouble(4, oldBal);
            vault.setDouble(5, newBal);
            vault.setBoolean(6, isStaff);

            vault.executeUpdate();
            vault.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertRegistration(String serverName, Player player, String joinDate) {
        try {
            final PreparedStatement register = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO registration (server_name, date, player_name, player_uuid, join_date) VALUES (?,?,?,?,?)");
            register.setString(1, serverName);
            register.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            register.setString(3, player.getName());
            register.setString(4, player.getUniqueId().toString());
            register.setString(5, joinDate);

            register.executeUpdate();
            register.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertPrimedTNT(String serverName, Player player, int x, int y, int z, boolean isStaff) {
        try {
            final PreparedStatement primedTNT = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO primed_tnt (server_name, date, world, player_uuid, player_name, x, y, z, is_staff) VALUES (?,?,?,?,?,?,?,?,?)");
            primedTNT.setString(1, serverName);
            primedTNT.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            primedTNT.setString(3, player.getWorld().getName());
            primedTNT.setString(4, player.getUniqueId().toString());
            primedTNT.setString(5, player.getName());
            primedTNT.setInt(6, x);
            primedTNT.setInt(7, y);
            primedTNT.setInt(8, z);
            primedTNT.setBoolean(9, isStaff);

            primedTNT.executeUpdate();
            primedTNT.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertLiteBans(String serverName, String executor, String command, String onWho, String duration, String reason, boolean isSilent) {

        try {

            final PreparedStatement liteBansStatement = plugin.getSqLite().getConnection().prepareStatement(
                    "INSERT INTO litebans (server_name, date, sender, command, onwho, reason, " +
                            "duration, is_silent) VALUES(?,?,?,?,?,?,?,?)");

            liteBansStatement.setString(1, serverName);
            liteBansStatement.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            liteBansStatement.setString(3, executor);
            liteBansStatement.setString(4, command);
            liteBansStatement.setString(5, onWho);
            liteBansStatement.setString(6, duration);
            liteBansStatement.setString(7, reason);
            liteBansStatement.setBoolean(8, isSilent);

            liteBansStatement.executeUpdate();
            liteBansStatement.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertAdvancedBan(String serverName, String executor, String executedOn, String type, String reason, long expirationDate) {
        try {
            final PreparedStatement advancedBan = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO advanced_ban (server_name, date, type, executor, executed_on, reason, expiration_date) VALUES (?,?,?,?,?,?,?)");
            advancedBan.setString(1, serverName);
            advancedBan.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            advancedBan.setString(3, type);
            advancedBan.setString(4, executor);
            advancedBan.setString(5, executedOn);
            advancedBan.setString(6, reason);
            advancedBan.setLong(7, expirationDate);

            advancedBan.executeUpdate();
            advancedBan.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }


    public static void insertCommandBlock(String serverName, String command) {
        try {
            final PreparedStatement commandBlock = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO command_block (server_name, date, command) VALUES (?,?,?)");
            commandBlock.setString(1, serverName);
            commandBlock.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            commandBlock.setString(3, command);

            commandBlock.executeUpdate();
            commandBlock.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertWoodStripping(String serverName, Player player, String logName, int x, int y, int z, boolean isStaff) {
        try {
            final PreparedStatement woodStrippingStatement = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO wood_stripping (server_name, date, world, player_uuid, player_name, log_name, x, y, z, is_staff) VALUES (?,?,?,?,?,?,?,?,?,?)");
            woodStrippingStatement.setString(1, serverName);
            woodStrippingStatement.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            woodStrippingStatement.setString(3, player.getWorld().getName());
            woodStrippingStatement.setString(4, player.getUniqueId().toString());
            woodStrippingStatement.setString(5, player.getName());
            woodStrippingStatement.setString(6, logName);
            woodStrippingStatement.setInt(7, x);
            woodStrippingStatement.setInt(8, y);
            woodStrippingStatement.setInt(9, z);
            woodStrippingStatement.setBoolean(10, isStaff);

            woodStrippingStatement.executeUpdate();
            woodStrippingStatement.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertChestInteraction(String serverName, Player player, int x, int y, int z, String[] items, boolean isStaff) {
        try {
            final PreparedStatement chestInteractionStatement = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO chest_interaction (server_name, date, world, player_uuid, player_name, x, y, z, items, is_staff) VALUES (?,?,?,?,?,?,?,?,?,?)");
            chestInteractionStatement.setString(1, serverName);
            chestInteractionStatement.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            chestInteractionStatement.setString(3, player.getWorld().getName());
            chestInteractionStatement.setString(4, player.getUniqueId().toString());
            chestInteractionStatement.setString(5, player.getName());
            chestInteractionStatement.setInt(6, x);
            chestInteractionStatement.setInt(7, y);
            chestInteractionStatement.setInt(8, z);
            chestInteractionStatement.setString(9, Arrays.toString(items));
            chestInteractionStatement.setBoolean(10, isStaff);

            chestInteractionStatement.executeUpdate();
            chestInteractionStatement.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertEntityDeath(String serverName, Player player, String mob, int x, int y, int z, boolean isStaff) {
        try {
            final PreparedStatement entityDeathStatement = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO entity_death (server_name, date, world, player_uuid, player_name, mob, x, y, z, is_staff) VALUES (?,?,?,?,?,?,?,?,?,?)");
            entityDeathStatement.setString(1, serverName);
            entityDeathStatement.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            entityDeathStatement.setString(3, player.getWorld().getName());
            entityDeathStatement.setString(4, player.getUniqueId().toString());
            entityDeathStatement.setString(5, player.getName());
            entityDeathStatement.setString(6, mob);
            entityDeathStatement.setInt(7, x);
            entityDeathStatement.setInt(8, y);
            entityDeathStatement.setInt(9, z);
            entityDeathStatement.setBoolean(10, isStaff);

            entityDeathStatement.executeUpdate();
            entityDeathStatement.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertSignChange(String serverName, Player player, int x, int y, int z, String text, boolean isStaff) {
        try {
            final PreparedStatement entityDeathStatement = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO sign_change (server_name, date, world, player_uuid, player_name, x, y, z, new_text, is_staff) VALUES (?,?,?,?,?,?,?,?,?,?)");
            entityDeathStatement.setString(1, serverName);
            entityDeathStatement.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            entityDeathStatement.setString(3, player.getWorld().getName());
            entityDeathStatement.setString(4, player.getUniqueId().toString());
            entityDeathStatement.setString(5, player.getName());
            entityDeathStatement.setInt(6, x);
            entityDeathStatement.setInt(7, y);
            entityDeathStatement.setInt(8, z);
            entityDeathStatement.setString(9, text);
            entityDeathStatement.setBoolean(10, isStaff);

            entityDeathStatement.executeUpdate();
            entityDeathStatement.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void emptyTable() {

        if (sqliteDataDel <= 0) return;

        try {

            // Player Side Part
            final PreparedStatement playerChat = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM player_chat WHERE date <= datetime('now','-" + sqliteDataDel + " day')");

            final PreparedStatement playerCommands = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM player_commands WHERE date <= datetime('now','-" + sqliteDataDel + " day')");

            final PreparedStatement playerSignText = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM player_sign_text WHERE date <= datetime('now','-" + sqliteDataDel + " day')");

            final PreparedStatement playerJoin = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM player_join WHERE date <= datetime('now','-" + sqliteDataDel + " day')");

            final PreparedStatement playerLeave = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM player_leave WHERE date <= datetime('now','-" + sqliteDataDel + " day')");

            final PreparedStatement playerKick = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM player_kick WHERE date <= datetime('now','-" + sqliteDataDel + " day')");

            final PreparedStatement playerDeath = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM player_death WHERE date <= datetime('now','-" + sqliteDataDel + " day')");

            final PreparedStatement playerTeleport = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM player_teleport WHERE date <= datetime('now','-" + sqliteDataDel + " day')");

            final PreparedStatement playerLevel = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM player_level WHERE date <= datetime('now','-" + sqliteDataDel + " day')");

            final PreparedStatement blockPlace = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM block_place WHERE date <= datetime('now','-" + sqliteDataDel + " day')");

            final PreparedStatement blockBreak = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM block_break WHERE date <= datetime('now','-" + sqliteDataDel + " day')");

            final PreparedStatement bucketFill = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM bucket_fill WHERE date <= datetime('now','-" + sqliteDataDel + " day')");

            final PreparedStatement bucketEmpty = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM bucket_empty WHERE date <= datetime('now','-" + sqliteDataDel + " day')");

            final PreparedStatement anvil = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM anvil WHERE date <= datetime('now','-" + sqliteDataDel + " day')");

            final PreparedStatement itemDrop = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM item_drop WHERE date <= datetime('now','-" + sqliteDataDel + " day')");

            final PreparedStatement enchanting = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM enchanting WHERE date <= datetime('now','-" + sqliteDataDel + " day')");

            final PreparedStatement bookEditing = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM book_editing WHERE date <= datetime('now','-" + sqliteDataDel + " day')");

            final PreparedStatement itemPickup = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM item_pickup WHERE date <= datetime('now','-" + sqliteDataDel + " day')");

            final PreparedStatement furnace = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM furnace WHERE date <= datetime('now','-" + sqliteDataDel + " day')");

            final PreparedStatement gameMode = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM game_mode WHERE date <= datetime('now','-" + sqliteDataDel + " day')");

            final PreparedStatement craft = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM crafting WHERE date <= datetime('now','-" + sqliteDataDel + " day')");

            final PreparedStatement register = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM registration WHERE date <= datetime('now','-" + sqliteDataDel + " day')");

            final PreparedStatement primedTNT = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM primed_tnt WHERE date <= datetime('now','-" + sqliteDataDel + " day')");

            final PreparedStatement chestInteraction = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM chest_interaction WHERE date <= datetime('now','-" + sqliteDataDel + " day')");

            final PreparedStatement entityDeath = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM entity_death WHERE date <= datetime('now','-" + sqliteDataDel + " day')");

            final PreparedStatement signChange = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM sign_change WHERE date <= datetime('now','-" + sqliteDataDel + " day')");


            // Server Side Part
            final PreparedStatement serverStart = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM server_start WHERE date <= datetime('now','-" + sqliteDataDel + " day')");

            final PreparedStatement serverStop = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM server_stop WHERE date <= datetime('now','-" + sqliteDataDel + " day')");

            final PreparedStatement consoleCommands = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM console_commands WHERE date <= datetime('now','-" + sqliteDataDel + " day')");

            final PreparedStatement ram = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM ram WHERE date <= datetime('now','-" + sqliteDataDel + " day')");

            final PreparedStatement tps = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM tps WHERE date <= datetime('now','-" + sqliteDataDel + " day')");

            final PreparedStatement portalCreation = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM portal_creation WHERE date <= datetime('now','-" + sqliteDataDel + " day')");

            final PreparedStatement rCon = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM rcon WHERE date <= datetime('now','-" + sqliteDataDel + " day')");

            final PreparedStatement commandBlock = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM command_block WHERE date <= datetime('now','-" + sqliteDataDel + " day')");

            // Extras Side Part
            if (EssentialsUtil.getEssentialsAPI() != null) {

                final PreparedStatement afk = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM afk WHERE date <= datetime('now','-" + sqliteDataDel + " day')");

                afk.executeUpdate();
                afk.close();

            }

            if (AuthMeUtil.getAuthMeAPI() != null) {

                final PreparedStatement wrongPassword = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM wrong_password WHERE date <= datetime('now','-" + sqliteDataDel + " day')");

                wrongPassword.executeUpdate();
                wrongPassword.close();

            }

            if (VaultUtil.getVaultAPI() && VaultUtil.getVault() != null) {

                final PreparedStatement vault = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM vault WHERE date <= datetime('now','-" + sqliteDataDel + " day')");

                vault.executeUpdate();
                vault.close();

            }

            if (LiteBansUtil.getLiteBansAPI() != null) {

                final PreparedStatement liteBans = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM litebans WHERE date <= datetime('now','-" + sqliteDataDel + " day')");

                liteBans.executeUpdate();
                liteBans.close();
            }

            if (AdvancedBanUtil.getAdvancedBanAPI() != null) {

                final PreparedStatement advancedBan = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM advanced_ban WHERE date <= datetime('now','-" + sqliteDataDel + " day')");

                advancedBan.executeUpdate();
                advancedBan.close();

            }

            // Version Exceptions Part
            if (plugin.getVersion().isAtLeast(NmsVersions.v1_13_R1)) {

                final PreparedStatement woodStripping = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM wood_stripping WHERE date <= datetime('now','-" + sqliteDataDel + " day')");

                woodStripping.executeUpdate();
                woodStripping.close();

            }

            playerChat.executeUpdate();
            playerChat.close();
            playerCommands.executeUpdate();
            playerCommands.close();
            playerSignText.executeUpdate();
            playerSignText.close();
            playerJoin.executeUpdate();
            playerJoin.close();
            playerLeave.executeUpdate();
            playerLeave.close();
            playerKick.executeUpdate();
            playerKick.close();
            playerDeath.executeUpdate();
            playerDeath.close();
            playerTeleport.executeUpdate();
            playerTeleport.close();
            playerLevel.executeUpdate();
            playerLevel.close();
            blockPlace.executeUpdate();
            blockPlace.close();
            blockBreak.executeUpdate();
            blockBreak.close();
            bucketFill.executeUpdate();
            bucketFill.close();
            bucketEmpty.executeUpdate();
            bucketEmpty.close();
            anvil.executeUpdate();
            anvil.close();
            itemDrop.executeUpdate();
            itemDrop.close();
            enchanting.executeUpdate();
            enchanting.close();
            bookEditing.executeUpdate();
            bookEditing.close();
            itemPickup.executeUpdate();
            itemPickup.close();
            furnace.executeUpdate();
            furnace.close();
            gameMode.executeUpdate();
            gameMode.close();
            craft.executeUpdate();
            craft.close();
            register.executeUpdate();
            register.close();
            primedTNT.executeUpdate();
            primedTNT.close();
            chestInteraction.executeUpdate();
            chestInteraction.close();
            entityDeath.executeUpdate();
            entityDeath.close();
            signChange.executeUpdate();
            signChange.close();

            serverStart.executeUpdate();
            serverStart.close();
            serverStop.executeUpdate();
            serverStop.close();
            consoleCommands.executeUpdate();
            consoleCommands.close();
            ram.executeUpdate();
            ram.close();
            tps.executeUpdate();
            tps.close();
            portalCreation.executeUpdate();
            portalCreation.close();
            rCon.executeUpdate();
            rCon.close();
            commandBlock.executeUpdate();
            commandBlock.close();

        } catch (SQLException e) {

            plugin.getLogger().severe("An error has occurred while cleaning the tables, if the error persists, contact the Authors!");
            e.printStackTrace();

        }
    }
}