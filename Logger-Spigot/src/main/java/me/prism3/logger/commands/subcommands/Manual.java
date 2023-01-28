package me.prism3.logger.commands.subcommands;

import me.prism3.logger.Main;
import me.prism3.logger.commands.SubCommand;
import me.prism3.logger.utils.enums.DiscordChannels;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import me.prism3.logger.utils.Log;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Manual implements SubCommand {

    private final Main main = Main.getInstance();

    @Override
    public String getName() { return "manual"; }

    @Override
    public String getDescription() { return "Manually save the log in it's separated file."; }

    @Override
    public String getSyntax() { return "logger manual <your_log>"; }

    @Override
    public void perform(CommandSender commandSender, String[] args) {

        if (!(commandSender instanceof ConsoleCommandSender)) {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&cThis command can only be executed in the console."));
            return;
        }

        if (args.length < 2 || !args[0].equalsIgnoreCase("manual")) {

            Log.severe("You must provide a log info!");
            Log.severe(this.getSyntax());
            return;
        }

        final String finalMessage = String.join(" ", args).replaceFirst("manual", "").trim();
        final Map<String, String> placeholders = new HashMap<>();
        placeholders.put("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now()));
        placeholders.put("%log%", finalMessage);

        // Log To Files
        if (Data.isLogToFiles)
            FileHandler.handleFileLog("Files.Custom.Manual", placeholders, FileHandler.getManualFile());

        // Discord
        if (this.main.getDiscordFile().get().getBoolean("Discord.Enable"))
            this.main.getDiscord().handleDiscordLog("Discord.Custom.Manual", placeholders, DiscordChannels.MANUAL, "Manual Log", null);

        // External
                /*if (Data.isExternal) { //TODO DB

                    try {

                        Main.getInstance().getDatabase().getDatabaseQueue().queueConsoleCommand(Data.serverName, command);

                    } catch (final Exception e) { e.printStackTrace(); }
                }

                // SQLite
                if (Data.isSqlite) {

                    try {

                        Main.getInstance().getDatabase().getDatabaseQueue().queueConsoleCommand(Data.serverName, command);

                    } catch (final Exception e) { e.printStackTrace(); }
                }*/
    }

    @Override
    public List<String> getSubCommandsArgs(CommandSender commandSender, String[] args) { return Collections.emptyList(); }
}
