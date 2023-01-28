package me.prism3.logger.serverside;

import me.prism3.logger.Main;
import me.prism3.logger.utils.enums.DiscordChannels;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

public class CommandBlock implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommandBlocks(ServerCommandEvent event) {

        if (event.getSender() instanceof BlockCommandSender) {

            final String command = event.getCommand().replace("\\", "\\\\");

            final Map<String, String> placeholders = new HashMap<>();
            placeholders.put("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now()));
            placeholders.put("%command%", command);

            // Log To Files
            if (Data.isLogToFiles)
                FileHandler.handleFileLog("Files.Server-Side.Command-Block", placeholders, FileHandler.getCommandBlockFile());

            // Discord
            if (this.main.getDiscordFile().get().getBoolean("Discord.Enable"))
                this.main.getDiscord().handleDiscordLog("Discord.Enchanting", placeholders, DiscordChannels.COMMAND_BLOCK, "Command Block", null);

            // External
            if (Data.isExternal) {

                try {

                    Main.getInstance().getDatabase().getDatabaseQueue().queueCommandBlock(Data.serverName, command);

                } catch (final Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (Data.isSqlite) {

                try {

                    Main.getInstance().getDatabase().getDatabaseQueue().queueCommandBlock(Data.serverName, command);

                } catch (final Exception e) { e.printStackTrace(); }
            }
        }
    }
}
