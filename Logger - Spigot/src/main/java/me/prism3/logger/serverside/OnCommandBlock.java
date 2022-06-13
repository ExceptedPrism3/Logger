package me.prism3.logger.serverside;

import me.prism3.logger.database.external.ExternalData;
import me.prism3.logger.database.sqlite.global.SQLiteData;
import me.prism3.logger.Main;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import me.prism3.logger.utils.Messages;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Objects;

public class OnCommandBlock implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommandBlocks(ServerCommandEvent event) {

        if (event.getSender() instanceof BlockCommandSender) {

            final String command = event.getCommand().replace("\\", "\\\\");

            if (this.main.getConfig().getBoolean("Log-Server.Command-Block")) {

                // Log To Files
                if (Data.isLogToFiles) {

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getCommandBlockFile(), true));
                        out.write(Objects.requireNonNull(Messages.get().getString("Files.Server-Side.Command-Block")).replaceAll("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%command%", command) + "\n");
                        out.close();

                    } catch (IOException e) {

                        this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }
                }

                // Discord
                if (!Objects.requireNonNull(Messages.get().getString("Discord.Server-Side.Command-Block")).isEmpty()) {

                    this.main.getDiscord().commandBlock(Objects.requireNonNull(Messages.get().getString("Discord.Server-Side.Command-Block")).replaceAll("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%command%", command), false);
                }

                // External
                if (Data.isExternal && this.main.getExternal().isConnected()) {

                    try {

                        ExternalData.commandBlock(Data.serverName, command);

                    } catch (Exception e) { e.printStackTrace(); }
                }

                // SQLite
                if (Data.isSqlite && this.main.getSqLite().isConnected()) {

                    try {

                        SQLiteData.insertCommandBlock(Data.serverName, command);

                    } catch (Exception e) { e.printStackTrace(); }
                }
            }
        }
    }
}
