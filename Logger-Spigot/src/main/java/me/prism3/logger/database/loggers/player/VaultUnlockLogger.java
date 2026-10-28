package me.prism3.logger.database.loggers.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.database.DatabasePlayerLogger;
import me.prism3.logger.database.loggers.AbstractLogger;
import me.prism3.logger.managers.PermissionManager;
import org.bukkit.entity.Player;

import java.util.Map;

public class VaultUnlockLogger extends AbstractLogger implements DatabasePlayerLogger {

    public VaultUnlockLogger(final LoggerAPI plugin) {
        super(plugin);
    }

    @Override
    public void logEvent(Player player, Map<String, String> placeholders) {
        int vaultX = 0;
        int vaultY = 0;
        int vaultZ = 0;
        try {
            vaultX = Integer.parseInt(placeholders.getOrDefault("vault_x", "0"));
            vaultY = Integer.parseInt(placeholders.getOrDefault("vault_y", "0"));
            vaultZ = Integer.parseInt(placeholders.getOrDefault("vault_z", "0"));
        } catch (NumberFormatException ignored) {}

        final int finalX = vaultX;
        final int finalY = vaultY;
        final int finalZ = vaultZ;
        final String items = placeholders.getOrDefault("items", "");

        final String sql = "INSERT INTO player_vault_unlock (date, server_name, player_uuid, player_name, world_name, location_x, location_y, location_z, vault_x, vault_y, vault_z, items, is_staff) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        executeUpdate(sql, ps -> {
            setPlayerFields(ps, player);
            ps.setInt(9, finalX);
            ps.setInt(10, finalY);
            ps.setInt(11, finalZ);
            ps.setString(12, items);
            ps.setBoolean(13, PermissionManager.isStaff(player));
        });
    }
}
