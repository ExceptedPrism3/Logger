package me.prism3.logger.serverside;

import me.prism3.logger.Main;
import me.prism3.logger.discord.Discord2;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Console implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onConsoleCommand(final ServerCommandEvent event) {

        if (event.getSender() instanceof BlockCommandSender) return;

        final String command = event.getCommand().replace("\\", "\\\\");
        final List<String> commandParts = Arrays.asList(event.getCommand().split("\\s+"));

        // Blacklisted Commands
        if (Data.isConsoleCommands && Data.consoleCommandsToBlock.stream().anyMatch(commandParts.get(0)::equalsIgnoreCase)) {
            event.setCancelled(true);
            return;
        }

        final Map<String, String> placeholders = new HashMap<>();
        placeholders.put("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now()));
        placeholders.put("%command%", command);

        // Log To Files
        if (Data.isLogToFiles)
            FileHandler.handleFileLog("Files.Server-Side.Console-Commands", placeholders, FileHandler.getConsoleLogFile());

        // Discord
        if (this.main.getDiscordFile().getBoolean("Discord.Enable"))
            this.main.getDiscord().handleDiscordLog("Discord.Server-Side.Console-Commands", placeholders, this.main.getDiscord()::console);

        final EmbedBuilder builder = new EmbedBuilder().setAuthor("Manual Log");

        builder.setDescription(command);

        this.main.getDiscord2().getChannels().get(Discord2.CONSOLE_CHANNEL).sendMessage(builder.build()).queue();

        // External
        if (Data.isExternal) {

            try {

                Main.getInstance().getDatabase().getDatabaseQueue().queueConsoleCommand(Data.serverName, command);

            } catch (final Exception e) { e.printStackTrace(); }
        }

        // SQLite
        if (Data.isSqlite) {

            try {

                Main.getInstance().getDatabase().getDatabaseQueue().queueConsoleCommand(Data.serverName, command);

            } catch (final Exception e) { e.printStackTrace(); }
        }
    }
}
