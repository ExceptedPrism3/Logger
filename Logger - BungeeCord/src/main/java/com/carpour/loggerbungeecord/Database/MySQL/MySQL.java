package com.carpour.loggerbungeecord.Database.MySQL;

import com.carpour.loggerbungeecord.Main;

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

    public void connect() {

        if (!isConnected()) {

            try {

                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?AllowPublicKeyRetrieval=true?useSSL=false&jdbcCompliantTruncation=false", username, password);
                Main.getInstance().getLogger().info("MySQL Connection has been established!");

            } catch (SQLException | ClassNotFoundException e) {

                Main.getInstance().getLogger().severe("Could not connect to MySQL Database!");

            }
        }
    }

    public void disconnect() {

        if (isConnected()) {

            try {

                connection.close();
                Main.getInstance().getLogger().info("MySQL Connection has been closed!");

            } catch (SQLException e) {

                Main.getInstance().getLogger().severe("MySQL Database couldn't be closed safely, if the issue persists contact the Authors!");

            }
        }
    }

    public Connection getConnection(){ return connection; }

}
