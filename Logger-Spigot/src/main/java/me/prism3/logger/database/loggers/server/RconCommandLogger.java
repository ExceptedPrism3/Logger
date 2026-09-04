package me.prism3.logger.database.loggers.server;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.database.DatabaseServerLogger;
import me.prism3.logger.database.loggers.AbstractLogger;

import java.util.Map;


public class RconCommandLogger extends AbstractLogger implements DatabaseServerLogger { //TODO TEST

    public RconCommandLogger(final LoggerAPI plugin) { super(plugin); }

    @Override
    public void logEvent(final Map<String, String> placeholders) {

        final String command = placeholders.get("command");
        final String sender = placeholders.get("sender");

        final String sql = "INSERT INTO server_rcon_command (date, server_name, command, sender) VALUES (?, ?, ?, ?)";

        executeUpdate(sql, ps -> {
            setCommonFields(ps);
            ps.setString(3, command);
            ps.setString(4, sender);
        });
    }
}
