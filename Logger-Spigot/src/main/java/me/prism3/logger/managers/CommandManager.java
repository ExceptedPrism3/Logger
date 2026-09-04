package me.prism3.logger.managers;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.commands.SubCommand;
import me.prism3.logger.commands.subcommands.Manual;
import me.prism3.logger.commands.subcommands.Reload;
import me.prism3.logger.commands.subcommands.Dump;
import me.prism3.logger.commands.subcommands.View;
import me.prism3.logger.commands.subcommands.Support;
import me.prism3.logger.utils.enums.GeneralSideMessages;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * CommandManager handles the registration and execution of commands.
 * It implements the TabExecutor interface for tab completion.
 */
public class CommandManager implements TabExecutor {

    private final LoggerAPI plugin;
    private final Map<String, SubCommand> commands = new LinkedHashMap<>();

    /**
     * Constructor for CommandManager.
     *
     * @param plugin The main plugin instance.
     */
    public CommandManager(final LoggerAPI plugin) {
        this.plugin = plugin;

        // register all subcommands here
        this.register(new Reload(this.plugin));
        this.register(new Dump(this.plugin));
        this.register(new View(this.plugin));
        this.register(new Manual(this.plugin));
        this.register(new Support(this.plugin));
    }

    /**
     * Registers a subcommand with the command manager.
     *
     * @param cmd The subcommand to register.
     */
    private void register(final SubCommand cmd) {
        this.commands.put(cmd.getName().toLowerCase(), cmd);
    }

    /**
     * Handles command execution.
     *
     * @param sender  The command sender.
     * @param command The command being executed.
     * @param label   The label of the command.
     * @param args    The arguments passed to the command.
     * @return true if the command was executed successfully, false otherwise.
     */
    @Override
    public boolean onCommand(final @NotNull CommandSender sender, final @NotNull Command command,
            final @NotNull String label, final String[] args) {

        // permission check
        if (!PermissionManager.hasPermission(sender, PermissionManager.LOGGER_STAFF)) {
            sender.sendMessage(this.plugin.getMessageManager()
                    .getGeneralMessage(GeneralSideMessages.NO_PERMISSION));
            return true;
        }

        // if no args, print help
        if (args.length == 0) {
            this.sendUsage(sender);
            return true;
        }

        final SubCommand sub = this.commands.get(args[0].toLowerCase());

        // if the subcommand is not registered, print help
        if (sub == null) {
            this.sendUsage(sender);
            return true;
        }

        // Check specific subcommand permission
        if (sub.getPermission() != null && !PermissionManager.hasPermission(sender, sub.getPermission())) {
            sender.sendMessage(this.plugin.getMessageManager()
                    .getGeneralMessage(GeneralSideMessages.NO_PERMISSION));
            return true;
        }

        // if the subcommand is not the first arg, print help
        try {
            sub.perform(sender, args);
        } catch (final IOException e) {
            this.plugin.getLogger().severe("Error executing /" + label + " " + sub.getName() + ": " + e.getMessage());
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.getData().getPluginPrefix())
                    + ChatColor.RED + "An error occurred. Check console.");
        }
        return true;
    }

    /**
     * Handles tab completion for commands.
     *
     * @param sender  The command sender.
     * @param command The command being executed.
     * @param label   The label of the command.
     * @param args    The arguments passed to the command.
     * @return A list of possible completions.
     */
    @Override
    public List<String> onTabComplete(final @NotNull CommandSender sender, final @NotNull Command command,
            final @NotNull String label, final String[] args) {

        // if no args, return all commands
        if (args.length == 1) {
            return this.commands.values().stream()
                    .filter(cmd -> cmd.getName().startsWith(args[0].toLowerCase()))
                    .filter(cmd -> cmd.getPermission() == null
                            || PermissionManager.hasPermission(sender, cmd.getPermission()))
                    .map(SubCommand::getName)
                    .collect(Collectors.toList());
        }

        // if the first arg is a subcommand, get its subcommands
        if (args.length > 1) {

            final SubCommand sub = this.commands.get(args[0].toLowerCase());

            if (sub != null) {
                // Check permission before showing args
                if (sub.getPermission() != null && !PermissionManager.hasPermission(sender, sub.getPermission())) {
                    return Collections.emptyList();
                }
                return sub.getSubCommandsArgs(sender, args);
            }
        }

        return Collections.emptyList();
    }

    /**
     * Sends the usage information for the commands to the sender.
     *
     * @param sender The command sender.
     */
    private void sendUsage(final CommandSender sender) {

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.getData().getPluginPrefix()) +
                ChatColor.DARK_GRAY + "-------------- " +
                ChatColor.GOLD + "v" + plugin.getData().getPluginVersion() +
                ChatColor.DARK_GRAY + " --------------");

        commands.values().stream()
                .filter(cmd -> cmd.getPermission() == null
                        || PermissionManager.hasPermission(sender, cmd.getPermission()))
                .forEach(cmd -> sender.sendMessage(ChatColor.AQUA + cmd.getSyntax() +
                        ChatColor.GRAY + " — " + cmd.getDescription()));
        sender.sendMessage(ChatColor.DARK_GRAY + "--------------------------------");
    }
}
