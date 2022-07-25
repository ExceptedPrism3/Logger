package me.prism3.loggervelocity.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import me.prism3.loggervelocity.Main;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;

import static me.prism3.loggervelocity.utils.Data.loggerReload;
import static me.prism3.loggervelocity.utils.Data.pluginPrefix;

public class Reload implements SimpleCommand {

    private final Main main = Main.getInstance();

    @Override
    public void execute(Invocation invocation) {

        final CommandSource sender = invocation.source();
        final String[] args = invocation.arguments();

        if (sender.hasPermission(loggerReload)) {

            if (args.length == 0) {

                sender.sendMessage(Identity.nil(), Component.text("Thank you for using the Logger Plugin!"));

            } else if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {

                this.main.getMessages().reload();

                sender.sendMessage(Identity.nil(), Component.text(this.main.getMessages().getString("General.Reload").replace("%prefix%", pluginPrefix)));

            } else {

                sender.sendMessage(Identity.nil(), Component.text(this.main.getMessages().getString("General.Invalid-Syntax").replace("%prefix%", pluginPrefix)));

            }
        } else {

            sender.sendMessage(Identity.nil(), Component.text(this.main.getMessages().getString("General.No-Permission").replace("%prefix%", pluginPrefix)));

        }
    }
}
