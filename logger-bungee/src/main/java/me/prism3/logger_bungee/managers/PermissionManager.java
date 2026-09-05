package me.prism3.logger_bungee.managers;

import me.prism3.logger_bungee.LoggerBungee;
import me.prism3.logger_bungee.utils.Constants;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PermissionManager {

    private final LoggerBungee plugin;

    public PermissionManager(LoggerBungee plugin) {
        this.plugin = plugin;
    }

    public boolean hasStaffLog(ProxiedPlayer player) {
        return player.hasPermission(Constants.Permissions.STAFF_LOG.getValue())
                || player.hasPermission("logger.staff.log");
    }

    public boolean hasStaffAlerts(ProxiedPlayer player) {
        return player.hasPermission(Constants.Permissions.STAFF_ALERTS.getValue())
                || player.hasPermission("logger.staff.alerts");
    }

    public boolean hasStaffNotifications(ProxiedPlayer player) {
        return player.hasPermission(Constants.Permissions.STAFF_NOTIFICATIONS.getValue())
                || player.hasPermission("logger.staff.notifications");
    }

    public boolean isStaff(ProxiedPlayer player) {
        return hasStaffLog(player) || hasStaffAlerts(player) || hasStaffNotifications(player);
    }

    public boolean isExempt(ProxiedPlayer player) {
        if (player == null) return false;
        return player.hasPermission(Constants.Permissions.EXEMPT.getValue())
                || player.hasPermission("logger.exempt");
    }

    public boolean isExemptDiscord(ProxiedPlayer player) {
        if (player == null) return false;
        return player.hasPermission(Constants.Permissions.EXEMPT_DISCORD.getValue())
                || player.hasPermission("loggerproxy.exempt.discord")
                || player.hasPermission("logger.exempt.discord");
    }

    public boolean canReload(CommandSender sender) {
        return sender.hasPermission(Constants.Permissions.RELOAD.getValue())
                || sender.hasPermission(Constants.Permissions.ADMIN.getValue())
                || sender.hasPermission("logger.admin")
                || sender.hasPermission("logger.reload");
    }
}
