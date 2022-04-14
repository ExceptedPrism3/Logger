package me.prism3.logger.Commands.Getting;

import me.prism3.logger.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Chat implements CommandExecutor {

    private final Main main = Main.getInstance();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if (args.length == 1) {

            if (args[0].equalsIgnoreCase("get")) {

                try (Connection connection = main.getExternal().getConnection();

                     PreparedStatement getStatement = connection.prepareStatement("SELECT Message FROM Player_Chat WHERE Player_Name=? LIMIT 10;")) {

                    getStatement.setString(1, sender.getName());

                    try (ResultSet resultSet = getStatement.executeQuery()) {

                        while (resultSet.next()) {

                            sender.sendMessage(resultSet.getString("Message"));

                        }

                    } catch (SQLException e) {

                        e.printStackTrace();

                    }
                } catch (SQLException e) {

                    e.printStackTrace();

                }
            }
        }
        return true;
    }
}