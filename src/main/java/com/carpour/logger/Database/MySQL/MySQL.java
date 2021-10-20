package com.carpour.logger.Database.MySQL;

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
    private final String database = main.getConfig().getString("MySQL.Database");
    private Connection connection;

    public boolean isConnected(){ return connection != null; }

    public void connect(){

        if (main.getConfig().getBoolean("MySQL.Enable")) {

            if (!isConnected()) {

                try {

                    Class.forName("com.mysql.cj.jdbc.Driver");
                    connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?AllowPublicKeyRetrieval=true?useSSL=false&jdbcCompliantTruncation=false", username, password);
                    Bukkit.getLogger().info("[Logger] " + ChatColor.GREEN + "MySQL Connection has been established!");

                } catch (SQLException | ClassNotFoundException e) {

                    Bukkit.getLogger().warning("[Logger] " + ChatColor.RED + "Could not connect to the MySQL Database!");

                }
            }
        }
    }

    public void disconnect() {

        if (isConnected()) {

            try {

                connection.close();
                Bukkit.getConsoleSender().sendMessage("[Logger] " + ChatColor.GREEN + "MySQL Connection has been closed!");

            } catch (SQLException e) {

                Bukkit.getConsoleSender().sendMessage("[Logger] " + ChatColor.RED + "MySQL Database couldn't be closed safely, if the issue persists contact the Authors!");

            }
        }
    }

    public Connection getConnection(){ return connection; }

}
