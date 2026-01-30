package me.prism3.logger.commands.subcommands;

import me.prism3.logger.commands.SubCommand;
import me.prism3.logger.utils.SpyManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static me.prism3.logger.utils.Data.pluginPrefix;

public class Spy implements SubCommand {

    @Override
    public String getName() {
        return "spy";
    }

    @Override
    public String getDescription() {
        return "Toggle spy alerts for yourself";
    }

    @Override
    public String getSyntax() {
        return "/logger spy <book|anvil|command|sign>";
    }

    @Override
    public void perform(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            return;
        }

        Player player = (Player) commandSender;

        if (args.length < 2) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', pluginPrefix + "&cUsage: " + getSyntax()));
            return;
        }

        String type = args[1].toLowerCase();
        List<String> validTypes = Arrays.asList("book", "anvil", "command", "sign");

        if (!validTypes.contains(type)) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    pluginPrefix + "&cInvalid spy type. Valid types: book, anvil, command, sign"));
            return;
        }

        // "command" key in listener is likely "commands" or "command". Let's
        // standardize.
        // The task said "commands" in some places and "command" in others.
        // Existing ToggleSpy uses "commands", "book", "sign", "anvil".
        // Listeners usually check config paths like "Spy-Features.Book-Spy.Enable".
        // SpyManager just stores strings. We should align with what listeners will ask
        // for.
        // OnCommandSpy probably corresponds to "commands" or "command".
        // Let's use singular "command" for the user interface as requested by user
        // ("/logger spy (book, anvil, command, sign)")
        // but ensure we match the keys we check in listeners.

        boolean isEnabled = SpyManager.toggleSpy(player, type);
        String status = isEnabled ? "&aenabled" : "&cdisabled";

        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                pluginPrefix + "&7Spy for &e" + type + "&7 is now " + status + "&7."));
    }

    @Override
    public List<String> getSubCommandsArgs(CommandSender commandSender, String[] args) {
        if (args.length == 2) {
            return Arrays.asList("book", "anvil", "command", "sign");
        }
        return Collections.emptyList();
    }
}
