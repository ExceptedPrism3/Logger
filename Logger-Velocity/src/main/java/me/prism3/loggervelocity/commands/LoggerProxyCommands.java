package me.prism3.loggervelocity.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import me.prism3.loggervelocity.Main;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import static me.prism3.loggervelocity.utils.Data.*;


public class LoggerProxyCommands implements SimpleCommand {

    private final Main main = Main.getInstance();

    @Override
    public void execute(Invocation invocation) {
        final CommandSource sender = invocation.source();
        final String[] args = invocation.arguments();

        // If no arguments, show a help message.
        if (args.length == 0) {
            Component helpMessage = LegacyComponentSerializer.legacyAmpersand().deserialize(
                    "&b&lUsage&b: /loggerproxy <&areload&8&l|&9discord&b>"
            );
            sender.sendMessage(Identity.nil(), helpMessage);
            return;
        }

        // Subcommand: reload
        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission(loggerReload)) {
                sender.sendMessage(Identity.nil(),
                        Component.text(this.main.getMessages()
                                .getString("General.No-Permission").replace("%prefix%", pluginPrefix)));
                return;
            }
            // Reload the messages
            this.main.getMessages().reload();
            sender.sendMessage(Identity.nil(),
                    Component.text(this.main.getMessages()
                            .getString("General.Reload").replace("%prefix%", pluginPrefix)));
            return;
        }

        // Subcommand: discord (example)
        if (args[0].equalsIgnoreCase("discord")) {
            // Dispatch to Discord command logic here
            // For example:
            sender.sendMessage(Identity.nil(), Component.text(discordSupportServer));
            return;
        }

        // If subcommand is not recognized:
        sender.sendMessage(Identity.nil(),
                Component.text(this.main.getMessages()
                        .getString("General.Invalid-Syntax").replace("%prefix%", pluginPrefix)));
    }
}
