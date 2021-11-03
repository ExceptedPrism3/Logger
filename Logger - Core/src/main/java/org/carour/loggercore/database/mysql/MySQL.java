package org.carour.loggercore.database.mysql;

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

public class MySQL {


    private final String host;
    private final int port;
    private final String username;
    private final String password;
    private final String database;
    private Connection connection;
    private final ServerType serverType;
    private ProxyServer proxyServer;

    public MySQL(JavaPlugin plugin) {
        this.host = plugin.getConfig().getString("MySQL.Host");
        this.port = plugin.getConfig().getInt("MySQL.Port");
        this.username = plugin.getConfig().getString("MySQL.Username");
        this.password = plugin.getConfig().getString("MySQL.Password");
        this.database = plugin.getConfig().getString("MySQL.Database");
        serverType = ServerType.BUNGEECORD;
    }

    public MySQL(Plugin plugin, Configuration configuration) {

        this.proxyServer = plugin.getProxy();

        this.host = configuration.getString("MySQL.Host");
        this.port = configuration.getInt("MySQL.Port");
        this.username = configuration.getString("MySQL.Username");
        this.password = configuration.getString("MySQL.Password");
        this.database = configuration.getString("MySQL.Database");
        serverType = ServerType.BUNGEECORD;

    }

    public boolean isConnected() {
        return connection != null;
    }

    public void connect() {

        if (!isConnected()) {

            try {

                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?AllowPublicKeyRetrieval=true?useSSL=false&jdbcCompliantTruncation=false", username, password);
                if (serverType == ServerType.SPIGOT) {
                    Bukkit.getLogger().info(ChatColor.GREEN + "MySQL Connection has been established!");
                }
                if (serverType == ServerType.BUNGEECORD) {
                    proxyServer.getLogger().info(net.md_5.bungee.api.ChatColor.GREEN + "MySQL Connection has been established!");
                }

            } catch (SQLException | ClassNotFoundException e) {

                if (serverType == ServerType.SPIGOT) {
                    Bukkit.getLogger().warning(ChatColor.RED + "Could not connect to the MySQL Database!");
                }
                if (serverType == ServerType.BUNGEECORD) {
                    proxyServer.getLogger().warning(net.md_5.bungee.api.ChatColor.RED + "Could not connect to the MySQL Database!");
                }

            }
        }
    }

    public void disconnect() {

        if (isConnected()) {

            try {

                connection.close();

                if (serverType == ServerType.SPIGOT) {
                    Bukkit.getLogger().info(ChatColor.GREEN + "MySQL Connection has been closed!");
                }
                if (serverType == ServerType.BUNGEECORD) {
                    proxyServer.getLogger().info(net.md_5.bungee.api.ChatColor.GREEN + "MySQL Connection has been closed!");
                }

            } catch (SQLException e) {

                if (serverType == ServerType.SPIGOT) {
                    Bukkit.getLogger().warning(ChatColor.RED + "MySQL Database couldn't be closed safely, if the issue persists contact the Authors!");
                }
                if (serverType == ServerType.BUNGEECORD) {
                    proxyServer.getLogger().warning(net.md_5.bungee.api.ChatColor.RED + "MySQL Database couldn't be closed safely, if the issue persists contact the Authors!");
                }


            }
        }
    }

    public Connection getConnection() {
        return connection;
    }

}
