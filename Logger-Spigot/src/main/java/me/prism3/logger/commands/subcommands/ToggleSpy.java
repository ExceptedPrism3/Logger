package me.prism3.logger.commands.subcommands;

import me.prism3.logger.Main;
import me.prism3.logger.commands.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static me.prism3.logger.utils.Data.pluginPrefix;


public class ToggleSpy implements SubCommand {

    public String getName() { return "toggle"; }

    public String getDescription() { return "Toggle spy features ON/OFF"; }

    public String getSyntax() { return "/logger " + this.getName() + " spy [Commands | Book | Sign | Anvil]"; }

    public void perform(final CommandSender commandSender, final String[] args) {

        final Main main = Main.getInstance();

        if (args.length < 3 || !args[1].equalsIgnoreCase("spy")) {
            commandSender.sendMessage(this.getSyntax());
            return;
        }

        final String option = args[2].toLowerCase();
        final String[] validOptions = {"commands", "book", "sign", "anvil"};

        if (!Arrays.asList(validOptions).contains(option)) {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    pluginPrefix + "&cInvalid option, correct options are [Commands | Book | Sign | Anvil]"));
            return;
        }

        final String path = "Spy-Features." + option + "-Spy.Enable";
        final boolean isToggled = main.getConfig().getBoolean(path);

        main.getConfig().set(path, !isToggled);
        main.saveConfig();
        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                pluginPrefix + option + " Spy Toggled."));
    }

    public List<String> getSubCommandsArgs(CommandSender commandSender, String[] args) { return Collections.emptyList(); }
}