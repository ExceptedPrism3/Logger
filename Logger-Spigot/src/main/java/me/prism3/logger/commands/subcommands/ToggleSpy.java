package me.prism3.logger.commands.subcommands;

import me.prism3.logger.Main;
import me.prism3.logger.commands.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

import static me.prism3.logger.utils.Data.pluginPrefix;

public class ToggleSpy implements SubCommand {

    public String getName() { return "toggle"; }

    public String getDescription() { return "Toggle spy features ON/OFF"; }

    public String getSyntax() { return "/logger toggle spy [Commands | Book | Sign | Anvil]"; }

    public void perform(Player player, String[] args) {

        final Main main = Main.getInstance();

        if ((args.length == 1 && args[0].equalsIgnoreCase("toggle")) ||
                args.length == 2 && args[1].equalsIgnoreCase("spy")) {

            player.sendMessage(this.getSyntax());
            return;

        }

        if (args.length <= 3 && args[1].equalsIgnoreCase("spy")) {

            final String args2 = args[2];

            boolean isToggled;

            switch (args2) {

                case "commands":
                    isToggled = main.getConfig().getBoolean("Spy-Features.Commands-Spy.Enable");
                    isToggled = !isToggled;
                    main.getConfig().set("Spy-Features.Commands-Spy.Enable", isToggled);
                    main.saveConfig();
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', pluginPrefix
                            + "Commands Spy Toggled."));
                    break;
                case "book":
                    isToggled = main.getConfig().getBoolean("Spy-Features.Book-Spy.Enable");
                    isToggled = !isToggled;
                    main.getConfig().set("Spy-Features.Book-Spy.Enable", isToggled);
                    main.saveConfig();
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', pluginPrefix
                            + "Book Spy Toggled."));
                    break;
                case "sign":
                    isToggled = main.getConfig().getBoolean("Spy-Features.Sign-Spy.Enable");
                    isToggled = !isToggled;
                    main.getConfig().set("Spy-Features.Sign-Spy.Enable", isToggled);
                    main.saveConfig();
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', pluginPrefix
                            + "Sign Spy Toggled."));
                    break;
                case "anvil":
                    isToggled = main.getConfig().getBoolean("Spy-Features.Anvil-Spy.Enable");
                    isToggled = !isToggled;
                    main.getConfig().set("Spy-Features.Anvil-Spy.Enable", isToggled);
                    main.saveConfig();
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', pluginPrefix
                            + "Anvil Spy Toggled."));
                    break;
                default:
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            pluginPrefix + "&cInvalid option, correct options are [Commands | Book | Sign | Anvil]"));
            }
        }
    }
    public List<String> getSubCommandsArgs(Player player, String[] args) { return Collections.emptyList(); }
}
