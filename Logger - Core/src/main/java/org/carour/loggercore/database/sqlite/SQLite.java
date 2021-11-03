package org.carour.loggercore.database.sqlite;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.carour.loggercore.util.ServerType;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLite {
    private Connection connection;
    private ProxyServer proxyServer;
    private final File databaseFile;
    private final ServerType serverType;

    public SQLite(@NotNull JavaPlugin plugin) {
        databaseFile = new File(plugin.getDataFolder(), "LoggerData.db");
        serverType = ServerType.SPIGOT;
    }

    public SQLite(@NotNull Plugin plugin) {
        proxyServer = plugin.getProxy();
        databaseFile = new File(plugin.getDataFolder(), "LoggerData.db");
        serverType = ServerType.BUNGEECORD;
    }


    public boolean isConnected() {
        return (connection != null);
    }

    public void connect() {

        if (!isConnected()) {

            try {

                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection("jdbc:sqlite:" + databaseFile.getAbsolutePath());

                if (serverType == ServerType.SPIGOT) {
                    Bukkit.getLogger().info(ChatColor.GREEN + "SQLite Connection has been established!");
                }
                if (serverType == ServerType.BUNGEECORD) {
                    proxyServer.getLogger().info(net.md_5.bungee.api.ChatColor.GREEN + "SQLite Connection has been established!");
                }


            } catch (ClassNotFoundException | SQLException e) {

                if (serverType == ServerType.SPIGOT) {
                    Bukkit.getLogger().warning(ChatColor.RED + "Couldn't load SQLite Database, if the issue persists contact the Authors!");
                }
                if (serverType == ServerType.BUNGEECORD) {
                    proxyServer.getLogger().warning(net.md_5.bungee.api.ChatColor.RED + "Couldn't load SQLite Database, if the issue persists contact the Authors!");
                }

                e.printStackTrace();

            }
        }
    }

    public void disconnect() {

        if (isConnected()) {

            try {

                if (serverType == ServerType.SPIGOT) {
                    Bukkit.getLogger().info(ChatColor.GREEN + "SQLite Database has been closed!");
                }
                if (serverType == ServerType.BUNGEECORD) {
                    proxyServer.getLogger().info(net.md_5.bungee.api.ChatColor.GREEN + "SQLite Database has been closed!");
                }

                connection.close();

            } catch (SQLException e) {

                if (serverType == ServerType.SPIGOT) {
                    Bukkit.getLogger().warning(ChatColor.RED + "SQLite Database couldn't be closed safely, if the issue persists contact the Authors!");
                }
                if (serverType == ServerType.BUNGEECORD) {
                    proxyServer.getLogger().warning(net.md_5.bungee.api.ChatColor.RED + "SQLite Database couldn't be closed safely, if the issue persists contact the Authors!");
                }


            }
        }
    }

    public Connection getConnection() {
        return connection;
    }

}
