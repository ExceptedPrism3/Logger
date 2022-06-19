/*
package com.carpour.logger.Commands.onCommands;

import com.carpour.logger.Commands.SubCommands;
import com.carpour.logger.Main;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class get extends SubCommands {

    private final Main main = Main.getInstance();

    @Override
    public String getName() {
        return "get";
    }

    @Override
    public String getDescription() {
        return "get";
    }

    @Override
    public String getSyntax() {
        return "/logger get";
    }

    @Override
    public void perform(Player player, String[] args) {

        List<String> messages = new ArrayList<>();

        try (Connection connection = main.mySQL.getConnection()) {

            try (PreparedStatement getStatement = connection.prepareStatement("SELECT Message FROM Player_Chat")) {


                try (ResultSet resultSet = getStatement.executeQuery()) {

                    while (resultSet.next()) {

                        messages.add(resultSet.getString("Message"));

                    }

                } catch (SQLException throwables) {

                    throwables.printStackTrace();

                }
            }

        } catch (SQLException throwables) {

            throwables.printStackTrace();

        }
    }
}
*/
