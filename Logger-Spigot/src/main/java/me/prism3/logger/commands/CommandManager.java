package me.prism3.logger.commands;

import me.prism3.logger.Main;
import me.prism3.logger.commands.subcommands.*;
import me.prism3.logger.utils.Data;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static me.prism3.logger.utils.Data.loggerStaff;
import static me.prism3.logger.utils.Data.pluginPrefix;

public class CommandManager implements TabExecutor {

    private final ArrayList<SubCommand> subCommands = new ArrayList<>();

    public CommandManager() {

        this.subCommands.add(new Reload());
        this.subCommands.add(new PlayerInventory());
        this.subCommands.add(new ToggleSpy());
        this.subCommands.add(new Discord());
        this.subCommands.add(new Dump());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        final Main main = Main.getInstance();

        if (!sender.hasPermission(loggerStaff)) {

            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getMessages().get()
                    .getString("General.No-Permission").replace("%prefix%", pluginPrefix)));

            return false;
        }

        if (args.length > 0) {
            for (int i = 0; i < this.getSubCommands().size(); i++) {
                if (args[0].equalsIgnoreCase(this.getSubCommands().get(i).getName())) {
                    try {
                        this.getSubCommands().get(i).perform((Player) sender, args);
                    } catch (final IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        else {
            if (sender.hasPermission(loggerStaff)) {
                sender.sendMessage("--------------------------------------------");
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "Running Logger: &a&l" + Data.pluginVersion));
                for (int i = 0; i < this.getSubCommands().size(); i++)
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9&l" + this.getSubCommands().get(i).getSyntax() + " &8&l| &r" + this.getSubCommands().get(i).getDescription()));
                sender.sendMessage("--------------------------------------------");
            }
        }
        return true;
    }

    public List<SubCommand> getSubCommands() { return this.subCommands; }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length == 1) {

            final ArrayList<String> subCommandsArgs = new ArrayList<>();

            for (int i = 0; i < this.getSubCommands().size(); i++)
                subCommandsArgs.add(this.getSubCommands().get(i).getName());

            return subCommandsArgs;
        } else if (args.length >= 2) {

            for (int i = 0; i < this.getSubCommands().size(); i++)
                if (args[0].equalsIgnoreCase(this.getSubCommands().get(i).getName()))
                    return this.getSubCommands().get(i).getSubCommandsArgs((Player) sender, args);
        } return null;
    }
}
