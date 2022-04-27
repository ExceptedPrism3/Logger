package me.prism3.logger.Commands;

import me.prism3.logger.Commands.SubCommands.PlayerInventoryCommand;
import me.prism3.logger.Commands.SubCommands.ReloadCommand;
import me.prism3.logger.Commands.SubCommands.ToggleSpyCommand;
import me.prism3.logger.Utils.Data;
import me.prism3.logger.Utils.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static me.prism3.logger.Utils.Data.loggerStaff;

public class CommandManager implements TabExecutor {

    private final ArrayList<SubCommand> subCommands = new ArrayList<>();

    public CommandManager() {

        this.subCommands.add(new ReloadCommand());
        this.subCommands.add(new PlayerInventoryCommand());
        this.subCommands.add(new ToggleSpyCommand());

    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player) {

            if (!sender.hasPermission(loggerStaff)) {

                sender.sendMessage(Objects.requireNonNull(Messages.get().getString("General.No-Permission")).replaceAll("&", "§"));
                return false;
            }

            final Player player = (Player) sender;

            if (args.length > 0) {

                for (int i = 0; i < getSubCommands().size(); i++) {
                    if (args[0].equalsIgnoreCase(getSubCommands().get(i).getName())) {
                        getSubCommands().get(i).perform(player, args);
                    }
                }
            } else {

                if (player.hasPermission(loggerStaff)) {
                    player.sendMessage("--------------------------------------------");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "Running Logger &a&l" + Data.pluginVersion));
                    for (int i = 0; i < getSubCommands().size(); i++) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9&l " + getSubCommands().get(i).getSyntax() + " &8&l| &r" + getSubCommands().get(i).getDescription()));
                    }
                    player.sendMessage("--------------------------------------------");
                }
            }
        }
        return true;
    }

    public ArrayList<SubCommand> getSubCommands() { return this.subCommands; }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length == 1) {

            final ArrayList<String> subCommandsArgs = new ArrayList<>();

            for (int i = 0; i < getSubCommands().size(); i++) {
                subCommandsArgs.add(getSubCommands().get(i).getName());
            }

            return subCommandsArgs;
        } else if (args.length >= 2) {

            for (int i = 0; i < getSubCommands().size(); i++) {

                if (args[0].equalsIgnoreCase(getSubCommands().get(i).getName())) {

                    return getSubCommands().get(i).getSubCommandsArgs((Player) sender, args);

                }
            }
        } return null;
    }
}
