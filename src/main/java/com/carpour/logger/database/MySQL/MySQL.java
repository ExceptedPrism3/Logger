package com.carpour.logger.database.MySQL;

import com.carpour.logger.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {

    private final Main main = Main.getInstance();

    private final String host = main.getConfig().getString("MySQL.Host");
    private final int port = main.getConfig().getInt("MySQL.Port");
    private final String username = main.getConfig().getString("MySQL.Username");
    private final String password = main.getConfig().getString("MySQL.Password");
    private final String database = main .getConfig().getString("MySQL.Database");
    private Connection connection;

    public boolean isConnected(){ return (connection != null); }

    public void connect(){

        if (main.getConfig().getBoolean("MySQL.Enable")) {

            if (!isConnected()) {

                try {

                    Class.forName("com.mysql.cj.jdbc.Driver");
                    connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false&jdbcCompliantTruncation=false", username, password);
                    Bukkit.getConsoleSender().sendMessage("[Logger] " + ChatColor.GREEN + "MySQL Connection has been established!");

                } catch (SQLException | ClassNotFoundException e) {

                    Bukkit.getConsoleSender().sendMessage("[Logger] " + ChatColor.RED + "Could not connect to the Database!");

                }

            }
        }

    }

    public void disconnect() {

        if (isConnected()) {

            try {

                connection.close();
                Bukkit.getConsoleSender().sendMessage("[Logger] " + ChatColor.GREEN + "MySQL Connection has been safely shutdown!");

            } catch (SQLException e) {

                Bukkit.getConsoleSender().sendMessage("[Logger] " + ChatColor.RED + "An error has occurred while shutting down the Database!");

            }
        }
    }

    public Connection getConnection(){ return connection; }

}
