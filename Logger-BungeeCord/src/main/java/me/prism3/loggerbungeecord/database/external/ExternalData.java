package me.prism3.loggerbungeecord.database.external;

import me.prism3.loggerbungeecord.Main;
import me.prism3.loggerbungeecord.api.LiteBansUtil;

import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static me.prism3.loggerbungeecord.utils.Data.externalDataDel;

public class ExternalData {

    private static final Main plugin = Main.getInstance();

    private static final List<String> tablesNames = Stream.of("player_chat_proxy", "player_commands_proxy", "player_login_proxy",
            "player_leave_proxy", "server_reload_proxy", "server_start_proxy", "server_stop_proxy", "ram_proxy").collect(Collectors.toCollection(ArrayList::new));

    public void createTable() {

        try (Connection connection = plugin.getExternal().getHikari().getConnection();
             final Statement stsm = connection.createStatement()) {

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS player_chat_proxy "
                    + "(server_name VARCHAR(30), date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), player_name VARCHAR(100), message VARCHAR(200), is_staff TINYINT)");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS player_commands_proxy "
                    + "(server_name VARCHAR(30), date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), player_name VARCHAR(100), command VARCHAR(200), is_staff TINYINT)");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS player_login_proxy "
                    + "(server_name VARCHAR(30), date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), player_name VARCHAR(100), ip INT UNSIGNED, is_staff TINYINT)");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS player_leave_proxy "
                    + "(server_name VARCHAR(30), date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), player_name VARCHAR(100), is_staff TINYINT)");

            // Server Side Part
            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS server_reload_proxy "
                    + "(server_name VARCHAR(30), date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), player_name VARCHAR(100), is_staff TINYINT)");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS server_start_proxy "
                    + "(server_name VARCHAR(30), date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP())");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS server_stop_proxy "
                    + "(server_name VARCHAR(30), date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP())");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS ram_proxy "
                    + "(server_name VARCHAR(30), date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), total_memory INT, used_memory INT, free_memory INT)");

            // Extra Side Part
            if (LiteBansUtil.getLiteBansAPI() != null) {

                stsm.executeUpdate("CREATE TABLE IF NOT EXISTS litebans_proxy "
                        + "(server_name VARCHAR(30), date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), sender VARCHAR(100), command VARCHAR(20), onwho VARCHAR(100)," +
                        "reason VARCHAR(200), duration VARCHAR(30), is_silent TINYINT)");

                tablesNames.add("litebans_proxy");
            }

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void playerChat(String serverName, String playerName, String message, boolean staff) {

        try (Connection connection = plugin.getExternal().getHikari().getConnection();
             final PreparedStatement playerChat = connection.prepareStatement("INSERT INTO player_chat_proxy" +
                     " (server_name, player_name, message, is_staff) VALUES(?,?,?,?)")) {

            playerChat.setString(1, serverName);
            playerChat.setString(2, playerName);
            playerChat.setString(3, message);
            playerChat.setBoolean(4, staff);

            playerChat.executeUpdate();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void playerCommands(String serverName, String playerName, String command, boolean staff) {

        try (Connection connection = plugin.getExternal().getHikari().getConnection();
             final PreparedStatement playerCommand = connection.prepareStatement("INSERT INTO player_commands_proxy" +
                     " (server_name, player_name, command, is_staff) VALUES(?,?,?,?)")) {

            playerCommand.setString(1, serverName);
            playerCommand.setString(2, playerName);
            playerCommand.setString(3, command);
            playerCommand.setBoolean(4, staff);

            playerCommand.executeUpdate();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void playerLogin(String serverName, String playerName, InetSocketAddress ip, boolean staff) {

        try (Connection connection = plugin.getExternal().getHikari().getConnection();
             final PreparedStatement playerLogin = connection.prepareStatement("INSERT INTO player_login_proxy" +
                     " (server_name, player_name, ip, is_staff) VALUES(?,?,?,?)")) {

            playerLogin.setString(1, serverName);
            playerLogin.setString(2, playerName);
            if (plugin.getConfig().getBoolean("Player-Login.Player-IP")) {

                playerLogin.setString(3, ip.getHostString());

            } else {

                playerLogin.setString(3, null);
            }
            playerLogin.setBoolean(4, staff);

            playerLogin.executeUpdate();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void playerLeave(String serverName, String playerName, boolean staff) {

        try (Connection connection = plugin.getExternal().getHikari().getConnection();
             final PreparedStatement playerLeave = connection.prepareStatement("INSERT INTO player_leave_proxy" +
                     " (server_name, player_name, is_staff) VALUES(?,?,?)")) {

            playerLeave.setString(1, serverName);
            playerLeave.setString(2, playerName);
            playerLeave.setBoolean(3, staff);

            playerLeave.executeUpdate();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void serverReload(String serverName, String playerName, boolean staff) {

        try (Connection connection = plugin.getExternal().getHikari().getConnection();
             final PreparedStatement serverReload = connection.prepareStatement("INSERT INTO server_reload_proxy" +
                     " (server_name, player_name, is_staff) VALUES(?,?,?)")) {

            serverReload.setString(1, serverName);
            serverReload.setString(2, playerName);
            serverReload.setBoolean(3, staff);

            serverReload.executeUpdate();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void serverStart(String serverName) {

        try (Connection connection = plugin.getExternal().getHikari().getConnection();
             final PreparedStatement serverStart = connection.prepareStatement("INSERT INTO server_start_proxy (server_name) VALUES(?)")) {

            serverStart.setString(1, serverName);

            serverStart.executeUpdate();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void serverStop(String serverName) {

        try (Connection connection = plugin.getExternal().getHikari().getConnection();
             final PreparedStatement serverStop = connection.prepareStatement("INSERT INTO server_stop_proxy (server_name) VALUES(?)")) {

            serverStop.setString(1, serverName);

            serverStop.executeUpdate();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void ram(String serverName, long totalMemo, long usedMemo, long freeMemo) {

        try (Connection connection = plugin.getExternal().getHikari().getConnection();
             final PreparedStatement ram = connection.prepareStatement("INSERT INTO ram_proxy" +
                     " (server_name, total_memory, used_memory, free_memory) VALUES(?,?,?,?)")) {

            ram.setString(1, serverName);
            ram.setLong(2, totalMemo);
            ram.setLong(3, usedMemo);
            ram.setLong(4, freeMemo);

            ram.executeUpdate();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void liteBans(String serverName, String executor, String command, String onWho, String duration, String reason, boolean isSilent) {

        try (Connection connection = plugin.getExternal().getHikari().getConnection();
             final PreparedStatement liteBans = connection.prepareStatement("INSERT INTO litebans_proxy" +
                     " (server_name, sender, command, onwho, reason, duration, is_silent) VALUES(?,?,?,?,?,?,?)")) {

            liteBans.setString(1, serverName);
            liteBans.setString(2, executor);
            liteBans.setString(3, command);
            liteBans.setString(4, onWho);
            liteBans.setString(5, duration);
            liteBans.setString(6, reason);
            liteBans.setBoolean(7, isSilent);

            liteBans.executeUpdate();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void emptyTable() {

        if (externalDataDel <= 0) return;

        try (Connection connection = plugin.getExternal().getHikari().getConnection();
             Statement stsm = connection.createStatement()) {

            stsm.executeUpdate("DELETE FROM player_chat_proxy WHERE date < NOW() - INTERVAL " + externalDataDel + " DAY");

            stsm.executeUpdate("DELETE FROM player_commands_proxy WHERE date < NOW() - INTERVAL " + externalDataDel + " DAY");

            stsm.executeUpdate("DELETE FROM player_login_proxy WHERE date < NOW() - INTERVAL " + externalDataDel + " DAY");

            stsm.executeUpdate("DELETE FROM player_leave_proxy WHERE date < NOW() - INTERVAL " + externalDataDel + " DAY");

            stsm.executeUpdate("DELETE FROM server_reload_proxy WHERE date < NOW() - INTERVAL " + externalDataDel + " DAY");

            stsm.executeUpdate("DELETE FROM server_start_proxy WHERE date < NOW() - INTERVAL " + externalDataDel + " DAY");

            stsm.executeUpdate("DELETE FROM server_stop_proxy WHERE date < NOW() - INTERVAL " + externalDataDel + " DAY");

            stsm.executeUpdate("DELETE FROM ram_proxy WHERE date < NOW() - INTERVAL " + externalDataDel + " DAY");

            if (LiteBansUtil.getLiteBansAPI() != null) {

                stsm.executeUpdate("DELETE FROM litebans_proxy WHERE date < NOW() - INTERVAL " + externalDataDel + " DAY");

            }
        } catch (SQLException e) {

            plugin.getLogger().severe("An error has occurred while cleaning the tables, if the error persists, contact the Authors!");
            e.printStackTrace();

        }
    }
    public static List<String> getTablesNames() { return tablesNames; }
}