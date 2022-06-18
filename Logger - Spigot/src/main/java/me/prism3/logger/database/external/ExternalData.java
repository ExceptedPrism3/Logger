package me.prism3.logger.database.external;

import me.prism3.logger.api.*;
import me.prism3.logger.Main;
import me.prism3.logger.utils.enums.NmsVersions;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.world.PortalCreateEvent;


import java.net.InetSocketAddress;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


import static me.prism3.logger.utils.Data.externalDataDel;

public class ExternalData {

    private static final Main plugin = Main.getInstance();

    private static final List<String> tablesNames = Stream.of("player_chat", "player_commands", "player_sign_text",
            "player_death", "player_teleport", "player_join", "player_leave", "block_place", "block_break",
            "player_kick", "player_level", "Bucket_fill", "bucket_empty", "anvil", "item_drop", "enchanting",
            "book_editing", "item_pickup", "furnace", "game_mode", "crafting", "registration", "server_start",
            "server_stop", "console_commands", "ram", "tps", "portal_creation", "rcon", "primed_tnt", "command_block")
            .collect(Collectors.toCollection(ArrayList::new));

    public void createTable() {

        try (Connection connection = plugin.getExternal().getHikari().getConnection();
                final Statement stsm = connection.createStatement()) {

            // Player Side Part
            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS player_chat "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30), date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), world VARCHAR(100),"
                    +
                    "player_name VARCHAR(100), message VARCHAR(200), is_staff TINYINT) CHARSET=utf8");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS player_commands "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30), date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), world VARCHAR(100),"
                    +
                    "player_name VARCHAR(100), command VARCHAR(256), is_staff TINYINT) CHARSET=utf8");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS player_sign_text "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30), date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), world VARCHAR(100),"
                    +
                    "x INT, y INT, z INT, player_name VARCHAR(100), line VARCHAR(60), is_staff TINYINT) CHARSET=utf8");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS player_death "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30), date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), world VARCHAR(100),"
                    +
                    "player_name VARCHAR(100), player_level INT, x INT, y INT, z INT, cause VARCHAR(40), by_who VARCHAR(30), is_staff TINYINT) CHARSET=utf8");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS player_teleport "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30), date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), world VARCHAR(100),"
                    +
                    "player_name VARCHAR(100), from_x INT, from_y INT, from_z INT, to_x INT, to_y INT, to_z INT, is_staff TINYINT) CHARSET=utf8");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS player_join "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30), date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), world VARCHAR(100),"
                    +
                    "player_name VARCHAR(100), x INT, y INT, z INT, ip INT UNSIGNED, is_staff TINYINT) CHARSET=utf8");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS player_leave "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30), date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), world VARCHAR(100),"
                    +
                    "player_name VARCHAR(100), x INT, y INT, z INT, is_staff TINYINT) CHARSET=utf8");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS block_place "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30), date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), world VARCHAR(100),"
                    +
                    "player_name VARCHAR(100), block VARCHAR(40), x INT, y INT, z INT, is_staff TINYINT) CHARSET=utf8");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS block_break "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30), date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), world VARCHAR(100),"
                    +
                    "player_name VARCHAR(100), block VARCHAR(40), x INT, y INT, z INT, is_staff TINYINT) CHARSET=utf8");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS player_kick "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30), date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), world VARCHAR(100),"
                    +
                    "player_name VARCHAR(100), x INT, y INT, z INT, reason VARCHAR(50), is_staff TINYINT) CHARSET=utf8");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS player_level "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30), date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),"
                    +
                    "player_name VARCHAR(100), is_staff TINYINT) CHARSET=utf8" );

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS bucket_fill "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30), date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), world VARCHAR(100),"
                    +
                    "player_name VARCHAR(100), bucket VARCHAR(40), x INT, y INT, z INT, is_staff TINYINT) CHARSET=utf8");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS bucket_empty "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30), date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), world VARCHAR(100),"
                    +
                    "player_name VARCHAR(100), bucket VARCHAR(40), x INT, y INT, z INT, is_staff TINYINT) CHARSET=utf8");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS anvil "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30), date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),"
                    +
                    "player_name VARCHAR(100), new_name VARCHAR(100), is_staff TINYINT) CHARSET=utf8");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS item_drop "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30), date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), world VARCHAR(100),"
                    +
                    "player_name VARCHAR(100), item VARCHAR(50), amount INT, x INT, y INT, z INT, enchantment VARCHAR(50),"
                    +
                    "changed_name VARCHAR(50), is_staff TINYINT) CHARSET=utf8");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS enchanting "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30), date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), world VARCHAR(100),"
                    +
                    "player_name VARCHAR(100), x INT, y INT, z INT, enchantment VARCHAR(50), enchantment_level INT, " +
                    "item VARCHAR(50), cost INT(5), is_staff TINYINT) CHARSET=utf8");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS book_editing "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30), date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), world VARCHAR(100),"
                    +
                    "player_name VARCHAR(100), page_count INT, page_content VARCHAR(250), signed_by VARCHAR(25), is_staff TINYINT) CHARSET=utf8");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS item_pickup "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30), date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), world VARCHAR(100),"
                    +
                    "player_name VARCHAR(100), item VARCHAR(250), amount INT, x INT, y INT, z INT, changed_name VARCHAR(250), is_staff TINYINT) CHARSET=utf8");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS furnace "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30), date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), world VARCHAR(100),"
                    +
                    "player_name VARCHAR(100), item VARCHAR(250), amount INT, x INT, y INT, z INT, is_staff TINYINT) CHARSET=utf8");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS game_mode "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30), date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), world VARCHAR(100),"
                    +
                    "player_name VARCHAR(100), game_mode VARCHAR(15), is_staff TINYINT) CHARSET=utf8");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS crafting "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30), date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), world VARCHAR(100),"
                    +
                    "player_name VARCHAR(100), item VARCHAR(50), amount INT, x INT, y INT, z INT, is_staff TINYINT) CHARSET=utf8");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS registration "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30), date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), player_name VARCHAR(30),"
                    +
                    " player_uuid VARCHAR(80), join_date VARCHAR(30)) CHARSET=utf8");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS primed_tnt "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30), date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), world VARCHAR(100),"
                    +
                    "player_name VARCHAR(100), x INT, y INT, z INT, is_staff TINYINT) CHARSET=utf8");

            // Server Side Part
            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS server_start "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30), date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP()) CHARSET=utf8");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS server_stop "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30), date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP()) CHARSET=utf8");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS console_commands "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30), date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),"
                    +
                    "command VARCHAR(256)) CHARSET=utf8");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS ram "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30), date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),"
                    +
                    "total_memory INT, used_memory INT, free_memory INT) CHARSET=utf8");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS tps "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30), date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),"
                    +
                    "tps INT) CHARSET=utf8");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS portal_creation "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30), date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),"
                    +
                    "world VARCHAR(100), caused_by VARCHAR(50)) CHARSET=utf8");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS rcon "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30), date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),"
                    +
                    "ip INT UNSIGNED, command VARCHAR(50)) CHARSET=utf8");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS command_block "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30), date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),"
                    +
                    "command VARCHAR(256)) CHARSET=utf8");

            // Extras Side Part
            if (EssentialsUtil.getEssentialsAPI() != null) {

                stsm.executeUpdate("CREATE TABLE IF NOT EXISTS afk (id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT," +
                        " server_name VARCHAR(30), date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), world VARCHAR(100),"
                        +
                        " player_name VARCHAR(100), x INT, y INT, z INT, is_staff TINYINT) CHARSET=utf8");

                tablesNames.add("afk");
            }

            if (AuthMeUtil.getAuthMeAPI() != null) {

                stsm.executeUpdate(
                        "CREATE TABLE IF NOT EXISTS wrong_password (id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT," +
                                " server_name VARCHAR(30), date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), world VARCHAR(100),"
                                +
                                "player_name VARCHAR(100), is_staff TINYINT) CHARSET=utf8");

                tablesNames.add("wrong_password");
            }

            if (VaultUtil.getVaultAPI() && VaultUtil.getVault() != null) {

                stsm.executeUpdate("CREATE TABLE IF NOT EXISTS vault (id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT," +
                        " server_name VARCHAR(30), date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), " +
                        "player_name VARCHAR(100), old_balance DOUBLE, new_balance DOUBLE, is_staff TINYINT) CHARSET=utf8");

                tablesNames.add("vault");
            }

            if (LiteBansUtil.getLiteBansAPI() != null) {

                stsm.executeUpdate("CREATE TABLE IF NOT EXISTS litebans_proxy "
                        + "(server_name VARCHAR(30), date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), sender VARCHAR(100), command VARCHAR(20), onwho VARCHAR(100),"
                        +
                        "reason VARCHAR(200), duration VARCHAR(30), is_silent TINYINT) CHARSET=utf8");

                tablesNames.add("litebans_proxy");
            }

            if (AdvancedBanUtil.getAdvancedBanAPI() != null) {

                stsm.executeUpdate("CREATE TABLE IF NOT EXISTS advanced_ban (server_name VARCHAR(30)," +
                        " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), type VARCHAR(30),executor VARCHAR(30)," +
                        " executed_on VARCHAR(30), reason VARCHAR(100), expiration_date VARCHAR(50)) CHARSET=utf8");

                tablesNames.add("advanced_ban");
            }

            // Version Exceptions Part
            if (plugin.getVersion().isAtLeast(NmsVersions.v1_13_R1)) {

                stsm.executeUpdate(
                        "CREATE TABLE IF NOT EXISTS wood_stripping (id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT" +
                                " server_name VARCHAR(30), date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), world VARCHAR(100), "
                                +
                                "uuid VARCHAR(100), player_name VARCHAR(100), log_name VARCHAR(30), x INT, y INT, z INT, is_staff TINYINT) CHARSET=utf8");

                tablesNames.add("wood_stripping");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void playerChat(String serverName, Player player, String msg, boolean staff) {

        try (Connection connection = plugin.getExternal().getHikari().getConnection();
                final PreparedStatement playerChat = connection.prepareStatement("INSERT INTO player_chat" +
                        " (server_name, world, player_name, message, is_staff) VALUES(?,?,?,?,?)")) {

            playerChat.setString(1, serverName);
            playerChat.setString(2, player.getWorld().getName());
            playerChat.setString(3, player.getName());
            playerChat.setString(4, msg);
            playerChat.setBoolean(5, staff);

            playerChat.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void playerCommands(String serverName, Player player, String msg, boolean staff) {

        try (Connection connection = plugin.getExternal().getHikari().getConnection();
                final PreparedStatement playerCommands = connection.prepareStatement("INSERT INTO player_commands" +
                        " (server_name, world, player_name, command, is_staff) VALUES(?,?,?,?,?)")) {

            playerCommands.setString(1, serverName);
            playerCommands.setString(2, player.getWorld().getName());
            playerCommands.setString(3, player.getName());
            playerCommands.setString(4, msg);
            playerCommands.setBoolean(5, staff);

            playerCommands.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void consoleCommands(String serverName, String msg) {

        try (Connection connection = plugin.getExternal().getHikari().getConnection();
                final PreparedStatement consoleCommands = connection.prepareStatement("INSERT INTO console_commands" +
                        " (server_name, command) VALUES(?,?)")) {

            consoleCommands.setString(1, serverName);
            consoleCommands.setString(2, msg);

            consoleCommands.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void playerSignText(String serverName, Player player, int x, int y, int z, String lines,
            boolean staff) {

        try (Connection connection = plugin.getExternal().getHikari().getConnection();
                final PreparedStatement playerSignText = connection.prepareStatement("INSERT INTO player_sign_text" +
                        " (server_name, world, x, y, z, player_name, line, is_staff) VALUES(?,?,?,?,?,?,?,?)")) {

            playerSignText.setString(1, serverName);
            playerSignText.setString(2, player.getWorld().getName());
            playerSignText.setInt(3, x);
            playerSignText.setInt(4, y);
            playerSignText.setInt(5, z);
            playerSignText.setString(6, player.getName());
            playerSignText.setString(7, lines);
            playerSignText.setBoolean(8, staff);

            playerSignText.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void playerDeath(String serverName, Player player, int level, String cause, String who,
            boolean staff) {

        try (Connection connection = plugin.getExternal().getHikari().getConnection();
                final PreparedStatement playerDeath = connection.prepareStatement("INSERT INTO player_death" +
                        " (server_name, world, player_name, player_level, x, y, z, cause, by_who, is_staff) VALUES(?,?,?,?,?,?,?,?,?,?)")) {

            playerDeath.setString(1, serverName);
            playerDeath.setString(2, player.getWorld().getName());
            playerDeath.setString(3, player.getName());
            playerDeath.setInt(4, level);
            playerDeath.setInt(5, player.getLocation().getBlockX());
            playerDeath.setInt(6, player.getLocation().getBlockY());
            playerDeath.setInt(7, player.getLocation().getBlockZ());
            playerDeath.setString(8, cause);
            playerDeath.setString(9, who);
            playerDeath.setBoolean(10, staff);

            playerDeath.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void playerTeleport(String serverName, Player player, int tx, int ty, int tz, boolean staff) {

        try (Connection connection = plugin.getExternal().getHikari().getConnection();
                final PreparedStatement playerTeleport = connection.prepareStatement("INSERT INTO player_teleport" +
                        " (server_name, world, player_name, from_x, from_y, from_z, to_x, to_y, to_z, is_staff)" +
                        " VALUES(?,?,?,?,?,?,?,?,?,?)")) {

            playerTeleport.setString(1, serverName);
            playerTeleport.setString(2, player.getWorld().getName());
            playerTeleport.setString(3, player.getName());
            playerTeleport.setInt(4, player.getLocation().getBlockX());
            playerTeleport.setInt(5, player.getLocation().getBlockY());
            playerTeleport.setInt(6, player.getLocation().getBlockZ());
            playerTeleport.setInt(7, tx);
            playerTeleport.setInt(8, ty);
            playerTeleport.setInt(9, tz);
            playerTeleport.setBoolean(10, staff);

            playerTeleport.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void playerJoin(String serverName, Player player, int x, int y, int z, InetSocketAddress ip,
            boolean staff) {

        try (Connection connection = plugin.getExternal().getHikari().getConnection();
                final PreparedStatement playerJoin = connection.prepareStatement("INSERT INTO player_join" +
                        " (server_name, world, player_name, x, y, z, ip, is_staff) VALUES(?,?,?,?,?,?,INET_ATON(?),?)")) {

            playerJoin.setString(1, serverName);
            playerJoin.setString(2, player.getWorld().getName());
            playerJoin.setString(3, player.getName());
            playerJoin.setInt(4, x);
            playerJoin.setInt(5, y);
            playerJoin.setInt(6, z);
            if (plugin.getConfig().getBoolean("Player-Join.Player-ip")) {
                playerJoin.setString(7, ip.getHostString());
            } else {

                playerJoin.setString(7, null);
            }
            playerJoin.setBoolean(8, staff);

            playerJoin.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void playerLeave(String serverName, Player player, int x, int y, int z, boolean staff) {

        try (Connection connection = plugin.getExternal().getHikari().getConnection();
                final PreparedStatement playerLeave = connection.prepareStatement("INSERT INTO player_leave" +
                        " (server_name, world, player_name, x, y, z, is_staff) VALUES(?,?,?,?,?,?,?)")) {

            playerLeave.setString(1, serverName);
            playerLeave.setString(2, player.getWorld().getName());
            playerLeave.setString(3, player.getName());
            playerLeave.setInt(4, x);
            playerLeave.setInt(5, y);
            playerLeave.setInt(6, z);
            playerLeave.setBoolean(7, staff);

            playerLeave.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void blockPlace(String serverName, Player player, String block, int x, int y, int z, boolean staff) {

        try (Connection connection = plugin.getExternal().getHikari().getConnection();
                final PreparedStatement blockPlace = connection.prepareStatement("INSERT INTO block_place" +
                        " (server_name, world, player_name, block, x, y, z, is_staff) VALUES(?,?,?,?,?,?,?,?)")) {

            blockPlace.setString(1, serverName);
            blockPlace.setString(2, player.getWorld().getName());
            blockPlace.setString(3, player.getName());
            blockPlace.setString(4, block);
            blockPlace.setInt(5, x);
            blockPlace.setInt(6, y);
            blockPlace.setInt(7, z);
            blockPlace.setBoolean(8, staff);

            blockPlace.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void blockBreak(String serverName, Player player, String blockname, int x, int y, int z,
            boolean staff) {

        try (Connection connection = plugin.getExternal().getHikari().getConnection();
                final PreparedStatement blockBreak = connection.prepareStatement("INSERT INTO block_break" +
                        " (server_name, world, player_name, block, x, y, z, is_staff) VALUES(?,?,?,?,?,?,?,?)")) {

            blockBreak.setString(1, serverName);
            blockBreak.setString(2, player.getWorld().getName());
            blockBreak.setString(3, player.getName());
            blockBreak.setString(4, blockname);
            blockBreak.setInt(5, x);
            blockBreak.setInt(6, y);
            blockBreak.setInt(7, z);
            blockBreak.setBoolean(8, staff);

            blockBreak.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void tps(String serverName, double tpss) {

        try (Connection connection = plugin.getExternal().getHikari().getConnection();
                final PreparedStatement tps = connection
                        .prepareStatement("INSERT INTO tps (server_name, tps) VALUES(?,?)")) {

            tps.setString(1, serverName);
            tps.setDouble(2, tpss);

            tps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void ram(String serverName, long tm, long um, long fm) {

        try (Connection connection = plugin.getExternal().getHikari().getConnection();
                final PreparedStatement ram = connection.prepareStatement("INSERT INTO ram" +
                        " (server_name, total_memory, used_memory, free_memory) VALUES(?,?,?,?)")) {

            ram.setString(1, serverName);
            ram.setLong(2, tm);
            ram.setLong(3, um);
            ram.setLong(4, fm);

            ram.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void playerKick(String serverName, Player player, int x, int y, int z, String reason, boolean staff) {

        try (Connection connection = plugin.getExternal().getHikari().getConnection();
                final PreparedStatement playerKick = connection.prepareStatement("INSERT INTO player_kick" +
                        " (server_name, world, player_name, x, y, z, reason, is_staff) VALUES(?,?,?,?,?,?,?,?)")) {

            playerKick.setString(1, serverName);
            playerKick.setString(2, player.getWorld().getName());
            playerKick.setString(3, player.getName());
            playerKick.setInt(4, x);
            playerKick.setInt(5, y);
            playerKick.setInt(6, z);
            playerKick.setString(7, reason);
            playerKick.setBoolean(8, staff);

            playerKick.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void portalCreate(String serverName, String worldName, PortalCreateEvent.CreateReason by) {

        try (Connection connection = plugin.getExternal().getHikari().getConnection();
                final PreparedStatement portalCreation = connection.prepareStatement("INSERT INTO portal_creation" +
                        " (server_name, world, caused_by) VALUES(?,?,?)")) {

            portalCreation.setString(1, serverName);
            portalCreation.setString(2, worldName);
            portalCreation.setString(3, by.toString());

            portalCreation.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void levelChange(String serverName, String playerName, boolean staff) {

        try (Connection connection = plugin.getExternal().getHikari().getConnection();
                final PreparedStatement playerLevel = connection.prepareStatement("INSERT INTO player_level" +
                        " (server_name, player_name, is_staff) VALUES(?,?,?)")) {

            playerLevel.setString(1, serverName);
            playerLevel.setString(2, playerName);
            playerLevel.setBoolean(3, staff);

            playerLevel.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void bucketFill(String serverName, Player player, String bucket, int x, int y, int z, boolean staff) {

        try (Connection connection = plugin.getExternal().getHikari().getConnection();
                final PreparedStatement bucketPlace = connection.prepareStatement("INSERT INTO bucket_fill" +
                        " (server_name, world, player_name, bucket, x, y, z, is_staff) VALUES(?,?,?,?,?,?,?,?)")) {

            bucketPlace.setString(1, serverName);
            bucketPlace.setString(2, player.getWorld().getName());
            bucketPlace.setString(3, player.getName());
            bucketPlace.setString(4, bucket);
            bucketPlace.setInt(5, x);
            bucketPlace.setInt(6, y);
            bucketPlace.setInt(7, z);
            bucketPlace.setBoolean(8, staff);

            bucketPlace.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void bucketEmpty(String serverName, Player player, String bucket, int x, int y, int z,
            boolean staff) {

        try (Connection connection = plugin.getExternal().getHikari().getConnection();
                final PreparedStatement bucketPlace = connection.prepareStatement("INSERT INTO bucket_empty" +
                        " (server_name, world, player_name, bucket, x, y, z, is_staff) VALUES(?,?,?,?,?,?,?,?)")) {

            bucketPlace.setString(1, serverName);
            bucketPlace.setString(2, player.getWorld().getName());
            bucketPlace.setString(3, player.getName());
            bucketPlace.setString(4, bucket);
            bucketPlace.setInt(5, x);
            bucketPlace.setInt(6, y);
            bucketPlace.setInt(7, z);
            bucketPlace.setBoolean(8, staff);

            bucketPlace.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void anvil(String serverName, String playerName, String newName, boolean staff) {

        try (Connection connection = plugin.getExternal().getHikari().getConnection();
                final PreparedStatement anvil = connection.prepareStatement("INSERT INTO anvil" +
                        " (server_name, player_name, new_name, is_staff) VALUES(?,?,?,?)")) {

            anvil.setString(1, serverName);
            anvil.setString(2, playerName);
            anvil.setString(3, newName);
            anvil.setBoolean(4, staff);

            anvil.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void serverStart(String serverName) {

        try (Connection connection = plugin.getExternal().getHikari().getConnection();
                final PreparedStatement serverStart = connection
                        .prepareStatement("INSERT INTO server_start (server_name) VALUES(?)")) {

            serverStart.setString(1, serverName);

            serverStart.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void serverStop(String serverName) {

        try (Connection connection = plugin.getExternal().getHikari().getConnection();
                final PreparedStatement serverStop = connection
                        .prepareStatement("INSERT INTO server_stop (server_name) VALUES(?)")) {

            serverStop.setString(1, serverName);

            serverStop.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void itemDrop(String serverName, Player player, String item, int amount, int x, int y, int z,
            List<String> enchantment, String changedName, boolean staff) {

        try (Connection connection = plugin.getExternal().getHikari().getConnection();
                final PreparedStatement itemDrop = connection.prepareStatement("INSERT INTO item_drop" +
                        " (server_name, world, player_name, item, amount, x, y, z, enchantment, changed_name, is_staff)"
                        +
                        " VALUES(?,?,?,?,?,?,?,?,?,?,?)")) {

            itemDrop.setString(1, serverName);
            itemDrop.setString(2, player.getWorld().getName());
            itemDrop.setString(3, player.getName());
            itemDrop.setString(4, item);
            itemDrop.setInt(5, amount);
            itemDrop.setInt(6, x);
            itemDrop.setInt(7, y);
            itemDrop.setInt(8, z);
            itemDrop.setString(9, String.valueOf(enchantment));
            itemDrop.setString(10, changedName);
            itemDrop.setBoolean(11, staff);

            itemDrop.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void enchant(String serverName, Player player, List<String> enchantment, int enchantmentLevel,
            String item, int cost, boolean staff) {

        try (Connection connection = plugin.getExternal().getHikari().getConnection();
                final PreparedStatement enchanting = connection.prepareStatement("INSERT INTO enchanting" +
                        " (server_name, world, player_name, x, y, z, enchantment, enchantment_level, item, cost, is_staff)"
                        +
                        " VALUES(?,?,?,?,?,?,?,?,?,?,?)")) {

            enchanting.setString(1, serverName);
            enchanting.setString(2, player.getWorld().getName());
            enchanting.setString(3, player.getName());
            enchanting.setInt(4, player.getLocation().getBlockX());
            enchanting.setInt(5, player.getLocation().getBlockY());
            enchanting.setInt(6, player.getLocation().getBlockZ());
            enchanting.setString(7, String.valueOf(enchantment));
            enchanting.setInt(8, enchantmentLevel);
            enchanting.setString(9, item);
            enchanting.setInt(10, cost);
            enchanting.setBoolean(11, staff);

            enchanting.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void bookEditing(String serverName, Player player, int pages, List<String> content, String signedBy,
            boolean staff) {

        try (Connection connection = plugin.getExternal().getHikari().getConnection();
                final PreparedStatement enchanting = connection.prepareStatement("INSERT INTO book_editing" +
                        " (server_name, world, player_name, page_count, page_content, signed_by, is_staff) VALUES(?,?,?,?,?,?,?)")) {

            enchanting.setString(1, serverName);
            enchanting.setString(2, player.getWorld().getName());
            enchanting.setString(3, player.getName());
            enchanting.setInt(4, pages);
            enchanting.setString(5, String.valueOf(content));
            enchanting.setString(6, signedBy);
            enchanting.setBoolean(7, staff);

            enchanting.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void afk(String serverName, Player player, int x, int y, int z, boolean staff) {

        if (EssentialsUtil.getEssentialsAPI() != null) {

            try (Connection connection = plugin.getExternal().getHikari().getConnection();
                    final PreparedStatement afk = connection.prepareStatement("INSERT INTO afk" +
                            " (server_name, world, player_name, x, y, z, is_staff) VALUES(?,?,?,?,?,?,?)")) {

                afk.setString(1, serverName);
                afk.setString(2, player.getWorld().getName());
                afk.setString(3, player.getName());
                afk.setInt(4, x);
                afk.setInt(5, y);
                afk.setInt(6, z);
                afk.setBoolean(7, staff);

                afk.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void wrongPassword(String serverName, Player player, boolean staff) {

        if (AuthMeUtil.getAuthMeAPI() != null) {

            try (Connection connection = plugin.getExternal().getHikari().getConnection();
                    final PreparedStatement wrongPassword = connection.prepareStatement("INSERT INTO wrong_password" +
                            " (server_name, world, player_name, is_staff) VALUES(?,?,?,?)")) {

                wrongPassword.setString(1, serverName);
                wrongPassword.setString(2, player.getWorld().getName());
                wrongPassword.setString(3, player.getName());
                wrongPassword.setBoolean(4, staff);

                wrongPassword.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void itemPickup(String serverName, Player player, Material item, int amount, int x, int y, int z,
            String changedName, boolean staff) {

        try (Connection connection = plugin.getExternal().getHikari().getConnection();
                final PreparedStatement itemPickup = connection.prepareStatement("INSERT INTO item_pickup" +
                        " (server_name, world, player_name, item, amount, x, y, z, changed_name, is_staff) VALUES(?,?,?,?,?,?,?,?,?,?)")) {

            itemPickup.setString(1, serverName);
            itemPickup.setString(2, player.getWorld().getName());
            itemPickup.setString(3, player.getName());
            itemPickup.setString(4, String.valueOf(item));
            itemPickup.setInt(5, amount);
            itemPickup.setInt(6, x);
            itemPickup.setInt(7, y);
            itemPickup.setInt(8, z);
            itemPickup.setString(9, changedName);
            itemPickup.setBoolean(10, staff);

            itemPickup.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void furnace(String serverName, Player player, Material item, int amount, int x, int y, int z,
            boolean staff) {

        try (Connection connection = plugin.getExternal().getHikari().getConnection();
                final PreparedStatement furnace = connection.prepareStatement("INSERT INTO furnace" +
                        " (server_name, world, player_name, item, amount, x, y, z, is_staff) VALUES(?,?,?,?,?,?,?,?,?)")) {

            furnace.setString(1, serverName);
            furnace.setString(2, player.getWorld().getName());
            furnace.setString(3, player.getName());
            furnace.setString(4, item.toString());
            furnace.setInt(5, amount);
            furnace.setInt(6, x);
            furnace.setInt(7, y);
            furnace.setInt(8, z);
            furnace.setBoolean(9, staff);

            furnace.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void rCon(String serverName, String ip, String command) {

        try (Connection connection = plugin.getExternal().getHikari().getConnection();
                final PreparedStatement rCon = connection.prepareStatement("INSERT INTO rcon" +
                        " (server_name, ip, command) VALUES(?,?,?)")) {

            rCon.setString(1, serverName);
            rCon.setString(2, ip);
            rCon.setString(3, command);

            rCon.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void gameMode(String serverName, Player player, String theGameMode, boolean staff) {

        try (Connection connection = plugin.getExternal().getHikari().getConnection();
                final PreparedStatement gameMode = connection.prepareStatement("INSERT INTO game_mode" +
                        " (server_name, world, player_name, game_mode, is_staff) VALUES(?,?,?,?,?)")) {

            gameMode.setString(1, serverName);
            gameMode.setString(2, player.getWorld().getName());
            gameMode.setString(3, player.getName());
            gameMode.setString(4, theGameMode);
            gameMode.setBoolean(5, staff);

            gameMode.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void playerCraft(String serverName, Player player, String item, int amount, boolean staff) {

        try (Connection connection = plugin.getExternal().getHikari().getConnection();
                final PreparedStatement craft = connection.prepareStatement("INSERT INTO crafting" +
                        " (server_name, world, player_name, item, amount, x, y, z, is_staff) VALUES(?,?,?,?,?,?,?,?,?)")) {

            craft.setString(1, serverName);
            craft.setString(2, player.getWorld().getName());
            craft.setString(3, player.getName());
            craft.setString(4, item);
            craft.setInt(5, amount);
            craft.setInt(6, player.getLocation().getBlockX());
            craft.setInt(7, player.getLocation().getBlockY());
            craft.setInt(8, player.getLocation().getBlockZ());
            craft.setBoolean(9, staff);

            craft.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void vault(String serverName, Player player, double oldBal, double newBal, boolean staff) {

        try (Connection connection = plugin.getExternal().getHikari().getConnection();
                final PreparedStatement vault = connection.prepareStatement("INSERT INTO vault" +
                        " (server_name, player_name, old_balance, new_balance, is_staff) VALUES(?,?,?,?,?)")) {

            vault.setString(1, serverName);
            vault.setString(2, player.getName());
            vault.setDouble(3, oldBal);
            vault.setDouble(4, newBal);
            vault.setBoolean(5, staff);

            vault.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void playerRegistration(String serverName, Player player, String joinDate) {

        try (Connection connection = plugin.getExternal().getHikari().getConnection();
                final PreparedStatement register = connection.prepareStatement("INSERT INTO registration" +
                        " (server_name, player_name, player_uuid, join_date) VALUES(?,?,?,?)")) {

            register.setString(1, serverName);
            register.setString(2, player.getName());
            register.setString(3, player.getUniqueId().toString());
            register.setString(4, joinDate);

            register.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void primedTNT(String serverName, Player player, int x, int y, int z, boolean staff) {

        try (Connection connection = plugin.getExternal().getHikari().getConnection();
                final PreparedStatement primedTNT = connection.prepareStatement("INSERT INTO primed_tnt" +
                        " (server_name, world, player_name, x, y, z, is_staff) VALUES(?,?,?,?,?,?,?)")) {

            primedTNT.setString(1, serverName);
            primedTNT.setString(2, player.getWorld().getName());
            primedTNT.setString(3, player.getName());
            primedTNT.setInt(4, x);
            primedTNT.setInt(5, y);
            primedTNT.setInt(6, z);
            primedTNT.setBoolean(7, staff);

            primedTNT.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void liteBans(String serverName, String executor, String command, String onWho, String duration,
            String reason, boolean isSilent) {

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

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void advancedBanData(String serverName, String type, String executor, String executedOn,
            String reason, long expirationDate) {

        try (Connection connection = plugin.getExternal().getHikari().getConnection();
                final PreparedStatement advancedBan = connection.prepareStatement("INSERT INTO advanced_ban" +
                        " (server_name, type, executor, executed_on, reason, expiration_date) VALUES(?,?,?,?,?,?)")) {

            advancedBan.setString(1, serverName);
            advancedBan.setString(2, type);
            advancedBan.setString(3, executor);
            advancedBan.setString(4, executedOn);
            advancedBan.setString(5, reason);
            advancedBan.setLong(6, expirationDate);

            advancedBan.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void commandBlock(String serverName, String msg) {

        try (Connection connection = plugin.getExternal().getHikari().getConnection();
                final PreparedStatement commandBlock = connection.prepareStatement("INSERT INTO command_block" +
                        " (server_name, command) VALUES(?,?)")) {

            commandBlock.setString(1, serverName);
            commandBlock.setString(2, msg);

            commandBlock.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void woodStripping(String serverName, Player player, String logName, int x, int y, int z,
            boolean staff) {

        try (Connection connection = plugin.getExternal().getHikari().getConnection();
                final PreparedStatement woodStripping = connection.prepareStatement("INSERT INTO wood_stripping" +
                        " (server_name, world, uuid, player_name, log_name, x, y, z, is_staff) VALUES(?,?,?,?,?,?,?,?,?)")) {

            woodStripping.setString(1, serverName);
            woodStripping.setString(2, player.getWorld().getName());
            woodStripping.setString(3, player.getUniqueId().toString());
            woodStripping.setString(4, player.getName());
            woodStripping.setString(5, logName);
            woodStripping.setInt(6, x);
            woodStripping.setInt(7, y);
            woodStripping.setInt(8, z);
            woodStripping.setBoolean(9, staff);

            woodStripping.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public void emptyTable() {

        if (externalDataDel <= 0)
            return;

        try (Connection connection = plugin.getExternal().getHikari().getConnection();
                Statement stsm = connection.createStatement()) {

            // Player Side Part
            stsm.executeUpdate("DELETE FROM player_chat WHERE date < NOW() - INTERVAL " + externalDataDel + " DAY");

            stsm.executeUpdate("DELETE FROM player_commands WHERE date < NOW() - INTERVAL " + externalDataDel + " DAY");

            stsm.executeUpdate(
                    "DELETE FROM player_sign_text WHERE date < NOW() - INTERVAL " + externalDataDel + " DAY");

            stsm.executeUpdate("DELETE FROM player_join WHERE date < NOW() - INTERVAL " + externalDataDel + " DAY");

            stsm.executeUpdate("DELETE FROM player_leave WHERE date < NOW() - INTERVAL " + externalDataDel + " DAY");

            stsm.executeUpdate("DELETE FROM player_kick WHERE date < NOW() - INTERVAL " + externalDataDel + " DAY");

            stsm.executeUpdate("DELETE FROM player_death WHERE date < NOW() - INTERVAL " + externalDataDel + " DAY");

            stsm.executeUpdate("DELETE FROM player_teleport WHERE date < NOW() - INTERVAL " + externalDataDel + " DAY");

            stsm.executeUpdate("DELETE FROM player_level WHERE date < NOW() - INTERVAL " + externalDataDel + " DAY");

            stsm.executeUpdate("DELETE FROM block_place WHERE date < NOW() - INTERVAL " + externalDataDel + " DAY");

            stsm.executeUpdate("DELETE FROM block_break WHERE date < NOW() - INTERVAL " + externalDataDel + " DAY");

            stsm.executeUpdate("DELETE FROM bucket_fill WHERE date < NOW() - INTERVAL " + externalDataDel + " DAY");

            stsm.executeUpdate("DELETE FROM bucket_empty WHERE date < NOW() - INTERVAL " + externalDataDel + " DAY");

            stsm.executeUpdate("DELETE FROM anvil WHERE date < NOW() - INTERVAL " + externalDataDel + " DAY");

            stsm.executeUpdate("DELETE FROM item_drop WHERE date < NOW() - INTERVAL " + externalDataDel + " DAY");

            stsm.executeUpdate("DELETE FROM enchanting WHERE date < NOW() - INTERVAL " + externalDataDel + " DAY");

            stsm.executeUpdate("DELETE FROM book_editing WHERE date < NOW() - INTERVAL " + externalDataDel + " DAY");

            stsm.executeUpdate("DELETE FROM item_pickup WHERE date < NOW() - INTERVAL " + externalDataDel + " DAY");

            stsm.executeUpdate("DELETE FROM furnace WHERE date < NOW() - INTERVAL " + externalDataDel + " DAY");

            stsm.executeUpdate("DELETE FROM game_mode WHERE date < NOW() - INTERVAL " + externalDataDel + " DAY");

            stsm.executeUpdate("DELETE FROM crafting WHERE date < NOW() - INTERVAL " + externalDataDel + " DAY");

            stsm.executeUpdate("DELETE FROM registration WHERE date < NOW() - INTERVAL " + externalDataDel + " DAY");

            stsm.executeUpdate("DELETE FROM primed_tnt WHERE date < NOW() - INTERVAL " + externalDataDel + " DAY");

            // Server Side Part
            stsm.executeUpdate("DELETE FROM server_start WHERE date < NOW() - INTERVAL " + externalDataDel + " DAY");

            stsm.executeUpdate("DELETE FROM server_stop WHERE date < NOW() - INTERVAL " + externalDataDel + " DAY");

            stsm.executeUpdate(
                    "DELETE FROM console_commands WHERE date < NOW() - INTERVAL " + externalDataDel + " DAY");

            stsm.executeUpdate("DELETE FROM ram WHERE date < NOW() - INTERVAL " + externalDataDel + " DAY");

            stsm.executeUpdate("DELETE FROM tps WHERE date < NOW() - INTERVAL " + externalDataDel + " DAY");

            stsm.executeUpdate("DELETE FROM portal_creation WHERE date < NOW() - INTERVAL " + externalDataDel + " DAY");

            stsm.executeUpdate("DELETE FROM rcon WHERE date < NOW() - INTERVAL " + externalDataDel + " DAY");

            stsm.executeUpdate("DELETE FROM command_block WHERE date < NOW() - INTERVAL " + externalDataDel + " DAY");

            // Extras Side Part
            if (EssentialsUtil.getEssentialsAPI() != null) {

                stsm.executeUpdate("DELETE FROM afk WHERE date < NOW() - INTERVAL " + externalDataDel + " DAY");

            }

            if (AuthMeUtil.getAuthMeAPI() != null) {

                stsm.executeUpdate(
                        "DELETE FROM wrong_password WHERE date < NOW() - INTERVAL " + externalDataDel + " DAY");

            }

            if (VaultUtil.getVaultAPI() && VaultUtil.getVault() != null) {

                stsm.executeUpdate("DELETE FROM vault WHERE date < NOW() - INTERVAL " + externalDataDel + " DAY");

            }

            if (LiteBansUtil.getLiteBansAPI() != null) {

                stsm.executeUpdate(
                        "DELETE FROM litebans_proxy WHERE date < NOW() - INTERVAL " + externalDataDel + " DAY");

            }

            if (AdvancedBanUtil.getAdvancedBanAPI() != null) {

                stsm.executeUpdate(
                        "DELETE FROM advanced_ban WHERE date < NOW() - INTERVAL " + externalDataDel + " DAY");

            }

            // Version Exception Part
            if (plugin.getVersion().isAtLeast(NmsVersions.v1_13_R1)) {

                stsm.executeUpdate(
                        "DELETE FROM wood_stripping WHERE date < NOW() - INTERVAL " + externalDataDel + " DAY");

            }

        } catch (SQLException e) {

            plugin.getLogger().severe(
                    "An error has occurred while cleaning the tables, if the error persists, contact the Authors!");
            e.printStackTrace();

        }
    }

    public static List<String> getTableNames() {
        return tablesNames;
    }
}