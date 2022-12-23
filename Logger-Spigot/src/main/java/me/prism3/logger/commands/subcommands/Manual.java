package me.prism3.logger.commands.subcommands;

import me.prism3.logger.commands.SubCommand;
import me.prism3.logger.utils.Log;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import java.util.Collections;
import java.util.List;

public class Manual implements SubCommand {

    @Override
    public String getName() { return "manual"; }

    @Override
    public String getDescription() { return "Manually save the log in it's separated file."; }

    @Override
    public String getSyntax() { return "logger manual <your_log>"; }

    @Override
    public void perform(CommandSender commandSender, String[] args) {

        if (commandSender instanceof ConsoleCommandSender) {

            if (args.length == 1 && args[0].equalsIgnoreCase("manual")) {

                Log.severe("You must provide a log info!");
                Log.severe(this.getSyntax());

            } else {

                final StringBuilder builder = new StringBuilder();

                for (String arg : args) {

                    builder.append(arg);
                    builder.append(" ");
                }

                String finalMessage = builder.toString();
                finalMessage = finalMessage.replaceFirst("manual", "").trim();

                commandSender.sendMessage(finalMessage);
            }
        } else {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&cThis command can only be executed in the console."));
        }
    }

    @Override
    public List<String> getSubCommandsArgs(CommandSender commandSender, String[] args) { return Collections.emptyList(); }
}
