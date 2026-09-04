package me.prism3.logger.commands.subcommands;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.commands.SubCommand;
import me.prism3.logger.utils.enums.LogType;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * /logger manual <message...>
 * Logs an arbitrary server‑side message to the console_command table.
 * Intended for integration hooks (e.g. other plugins can call it).
 */
public class Manual implements SubCommand {

    private final LoggerAPI plugin;

    public Manual(final LoggerAPI plugin) {
        this.plugin = plugin;
    }

    /**
     * Get the command name.
     *
     * @return the command name
     */
    @Override
    public String getName() {
        return "manual";
    }

    /**
     * Get the command description.
     *
     * @return the command description
     */
    @Override
    public String getDescription() {
        return "Log a custom server message (for integrations). Only ran by the console";
    }

    /**
     * Get the command syntax.
     *
     * @return the command syntax
     */
    @Override
    public String getSyntax() {
        return "/logger manual <message...>";
    }

    /**
     * The command functionality.
     *
     * @param sender the command sender
     * @param args   the command arguments
     */
    @Override
    public void perform(final CommandSender sender, final String[] args) {

        // Check if the command is enabled
        if (!this.plugin.getData().isEnabled(LogType.SERVER_MANUAL_LOG)) {
            sender.sendMessage(ChatColor.RED + "This command is disabled.");
            return;
        }

        // permission check (only console or plugins by default)
        if (!(sender instanceof ConsoleCommandSender)) {
            sender.sendMessage(ChatColor.RED + "This command can only be run by the console.");
            return;
        }

        // Check if the args for the right usage
        if (args.length < 2) {
            sender.sendMessage("The usage: " + this.getSyntax());
            return;
        }

        // join the rest of args into one message
        final String message = String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length));

        // prepare placeholders for console_command table
        final Map<String, String> placeholders = new HashMap<>();
        placeholders.put("log", message);
        placeholders.put("command", message);

        this.plugin.getLoggerManager().logEvent(LogType.SERVER_MANUAL_LOG, null, placeholders);
    }

    /**
     * Get the subcommands args.
     *
     * @param sender null
     * @param args   null
     * @return nada
     */
    @Override
    public List<String> getSubCommandsArgs(final CommandSender sender, final String[] args) {
        return Collections.emptyList();
    }

    @Override
    public String getPermission() {
        return me.prism3.logger.managers.PermissionManager.LOGGER_RELOAD;
    }
}
