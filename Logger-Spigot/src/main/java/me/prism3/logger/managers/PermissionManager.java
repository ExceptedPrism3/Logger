package me.prism3.logger.managers;

import me.prism3.logger.utils.Log;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


/**
 * PermissionManager is a utility class that handles permission checks for players and console.
 * It provides methods to check various permissions related to logging and staff activities.
 */
public class PermissionManager {

    // Define all permissions
    public static final String LOGGER_EXEMPT = "logger.exempt";
    public static final String LOGGER_EXEMPT_DISCORD = "logger.exempt.discord";
    public static final String LOGGER_STAFF = "logger.staff";
    public static final String LOGGER_STAFF_LOG = "logger.staff.log";
    public static final String LOGGER_RELOAD = "logger.reload";
    public static final String LOGGER_VIEW = "logger.view";

    /**
     * Utility method to check if the CommandSender has the given permission.
     * Can be used for both Players and Console.
     *
     * @param sender     The CommandSender (Player or Console) to check.
     * @param permission The permission string to check against.
     * @return true if the sender has the permission, false otherwise.
     */
    public static boolean hasPermission(final @NotNull CommandSender sender, final @NotNull String permission) {
        return sender.hasPermission(permission);
    }

    /**
     * Checks if a player has the exempt permission (prevents logging).
     *
     * @param player The player to check.
     * @return true if the player has the exempt permission, false otherwise.
     */
    public static boolean isExempt(final Player player) { return player != null && hasPermission(player, LOGGER_EXEMPT); }

    /**
     * Checks if a player has the Discord exempt permission (prevents Discord logging).
     *
     * @param player The player to check.
     * @return true if the player has the Discord exempt permission, false otherwise.
     */
    public static boolean isExemptDiscord(final Player player) { return player != null && hasPermission(player, LOGGER_EXEMPT_DISCORD); }

    /**
     * Checks if a player is considered staff.
     *
     * @param player The player to check.
     * @return true if the player is considered staff, false otherwise.
     */
    public static boolean isStaff(final Player player) { return hasPermission(player, LOGGER_STAFF); }

    /**
     * Checks if a player is allowed to log staff activities.
     *
     * @param player The player to check.
     * @return true if the player has permission to log staff activities, false otherwise.
     */
    public static boolean canLogStaff(final Player player) { return hasPermission(player, LOGGER_STAFF_LOG); }

    /**
     * Checks if a player or console has permission to reload the plugin.
     *
     * @param sender The sender (either player or console) to check.
     * @return true if the sender has the reload permission, false otherwise.
     */
    public static boolean canReload(final CommandSender sender) { return hasPermission(sender, LOGGER_RELOAD); }

    /**
     * Checks if a player or console has permission to view logs.
     *
     * @param sender The sender (either player or console) to check.
     * @return true if the sender has the view permission, false otherwise.
     */
    public static boolean canView(final CommandSender sender) { return hasPermission(sender, LOGGER_VIEW); }

    /**
     * Convenience method to check if the sender is a player.
     * Ensures safer player checks by casting only when the sender is indeed a player.
     *
     * @param sender The CommandSender to check.
     * @return true if the sender is a player, false otherwise.
     */
    public static boolean isPlayer(final CommandSender sender) { return sender instanceof Player; }

    /**
     * Logs a message for debugging permissions, useful during testing.
     *
     * @param player     The player being checked.
     * @param permission The permission string being checked.
     */
    public static void logPermissionCheck(final Player player, String permission) {
        Log.info("Checking permission: " + permission + " for player: " + player.getName());
    }
}
