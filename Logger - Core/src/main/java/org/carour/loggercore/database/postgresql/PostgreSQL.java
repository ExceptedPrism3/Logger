package org.carour.loggercore.database.postgresql;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.carour.loggercore.util.ServerType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgreSQL {

    private final String host;
    private final int port;
    private final String username;
    private final String password;
    private final String database;
    private Connection connection;
    private final ServerType serverType;
    private ProxyServer proxyServer;

    public PostgreSQL(JavaPlugin plugin) {
        this.host = plugin.getConfig().getString("PostgreSQL.Host");
        this.port = plugin.getConfig().getInt("PostgreSQL.Port");
        this.username = plugin.getConfig().getString("PostgreSQL.Username");
        this.password = plugin.getConfig().getString("PostgreSQL.Password");
        this.database = plugin.getConfig().getString("PostgreSQL.Database");
        serverType = ServerType.BUNGEECORD;
    }

    public PostgreSQL(Plugin plugin, Configuration configuration) {

        this.proxyServer = plugin.getProxy();

        this.host = configuration.getString("PostgreSQL.Host");
        this.port = configuration.getInt("PostgreSQL.Port");
        this.username = configuration.getString("PostgreSQL.Username");
        this.password = configuration.getString("PostgreSQL.Password");
        this.database = configuration.getString("PostgreSQL.Database");
        serverType = ServerType.BUNGEECORD;

    }

    public boolean isConnected() {
        return (connection != null);
    }

    public void connect() {

        if (!isConnected()) {

            try {

                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection("jdbc:postgresql://" + host + ":" + port + "/" + database, username, password);

                if (serverType == ServerType.SPIGOT) {
                    Bukkit.getLogger().info(ChatColor.GREEN + "PostgreSQL Connection has been established!");
                }
                if (serverType == ServerType.BUNGEECORD) {
                    proxyServer.getLogger().info(net.md_5.bungee.api.ChatColor.GREEN + "PostgreSQL Connection has been established!");
                }

            } catch (SQLException | ClassNotFoundException e) {

                if (serverType == ServerType.SPIGOT) {
                    Bukkit.getLogger().warning(ChatColor.RED + "Could not connect to the PostgreSQL Database!");
                }
                if (serverType == ServerType.BUNGEECORD) {
                    proxyServer.getLogger().warning(net.md_5.bungee.api.ChatColor.RED + "Could not connect to the PostgreSQL Database!");
                }


            }
        }

    }

    public void disconnect() {

        if (isConnected()) {

            try {

                connection.close();

                if (serverType == ServerType.SPIGOT) {
                    Bukkit.getLogger().info(ChatColor.GREEN + "PostgreSQL Connection has been closed!");
                }
                if (serverType == ServerType.BUNGEECORD) {
                    proxyServer.getLogger().info(net.md_5.bungee.api.ChatColor.GREEN + "PostgreSQL Connection has been closed!");
                }

            } catch (SQLException e) {

                if (serverType == ServerType.SPIGOT) {
                    Bukkit.getLogger().warning(ChatColor.RED + "PostgreSQL Database couldn't be closed safely, if the issue persists contact the Authors!");
                }
                if (serverType == ServerType.BUNGEECORD) {
                    proxyServer.getLogger().warning(net.md_5.bungee.api.ChatColor.RED + "PostgreSQL Database couldn't be closed safely, if the issue persists contact the Authors!");
                }

            }
        }
    }

    public Connection getConnection() {
        return connection;
    }


}
