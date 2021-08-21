package com.carpour.logger.database.SQLite;

import com.carpour.logger.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLite {

    private Main main = Main.getInstance();

    private Connection connection;
    File databaseFile = new File(main.getDataFolder(), "loggerData.db");

    public boolean isConnected() {
        return (connection != null);
    }

    public void connect() {
        if (!main.getConfig().getBoolean("SQLite.Enable")) {
            return;
        }
        if (!isConnected()) {
            try {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection("jdbc:sqlite:" + databaseFile.getAbsolutePath());
                main.getLogger().info(ChatColor.GREEN + "SQLite Connection has been established!");
            } catch (ClassNotFoundException | SQLException e) {
                main.getLogger().warning(ChatColor.RED + "Couldn't load SQLite Database");
                e.printStackTrace();
            }
        }
    }
    public void disconnect() {

        if (isConnected()) {

            try {

                connection.close();
                main.getLogger().info("SQLite Database has been closed!");

            } catch (SQLException e) {

                main.getLogger().warning("SQLite Database couldn't be closed");

            }
        }
    }

    public Connection getConnection(){ return connection; }


}
