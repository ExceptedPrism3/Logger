package com.carpour.logger.Database.PostgreSQL;

import com.carpour.logger.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgreSQL {
    private final Main main = Main.getInstance();

    private final String host = main.getConfig().getString("PostgreSQL.Host");
    private final int port = main.getConfig().getInt("PostgreSQL.Port");
    private final String username = main.getConfig().getString("PostgreSQL.Username");
    private final String password = main.getConfig().getString("PostgreSQL.Password");
    private final String database = main.getConfig().getString("PostgreSQL.Database");
    private Connection connection;


    public boolean isConnected() {
        return (connection != null);
    }

    public void connect(){

        if (main.getConfig().getBoolean("PostgreSQL.Enable")) {

            if (!isConnected()) {

                try {

                    Class.forName("org.postgresql.Driver");
                    connection = DriverManager.getConnection("jdbc:postgresql://" + host + ":" + port + "/" + database, username, password);
                    Bukkit.getLogger().info("[Logger] " + ChatColor.GREEN + "PostgreSQL Connection has been established!");

                } catch (SQLException | ClassNotFoundException e) {

                    Bukkit.getLogger().warning("[Logger] " + ChatColor.RED + "Could not connect to the PostgreSQL Database!");

                }
            }
        }
    }

    public void disconnect() {

        if (isConnected()) {

            try {

                connection.close();
                Bukkit.getConsoleSender().sendMessage("[Logger] " + ChatColor.GREEN + "PostgreSQL Connection has been closed!");

            } catch (SQLException e) {

                Bukkit.getConsoleSender().sendMessage("[Logger] " + ChatColor.RED + "PostgreSQL Database couldn't be closed safely, if the issue persists contact the Authors!");

            }
        }
    }

    public Connection getConnection(){ return connection; }



}
