package me.prism3.logger.serverside;

import me.prism3.logger.Main;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import me.prism3.logger.utils.Log;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;

public class CommandBlock implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommandBlocks(ServerCommandEvent event) {

        if (event.getSender() instanceof BlockCommandSender) {

            final String command = event.getCommand().replace("\\", "\\\\");

            // Log To Files
            if (Data.isLogToFiles) {

                try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getCommandBlockFile(), true))){
                    
                    out.write(this.main.getMessages().get().getString("Files.Server-Side.Command-Block").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%command%", command) + "\n");

                } catch (final IOException e) {

                    Log.warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();
                }
            }

            // Discord
            if (!this.main.getMessages().get().getString("Discord.Server-Side.Command-Block").isEmpty() && this.main.getDiscordFile().getBoolean("Discord.Enable"))
                this.main.getDiscord().commandBlock(this.main.getMessages().get().getString("Discord.Server-Side.Command-Block").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%command%", command), false);

            // External
            if (Data.isExternal) {

                try {

                    Main.getInstance().getDatabase().insertCommandBlock(Data.serverName, command);

                } catch (final Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (Data.isSqlite) {

                try {

                    Main.getInstance().getSqLite().insertCommandBlock(Data.serverName, command);

                } catch (final Exception e) { e.printStackTrace(); }
            }
        }
    }
}
