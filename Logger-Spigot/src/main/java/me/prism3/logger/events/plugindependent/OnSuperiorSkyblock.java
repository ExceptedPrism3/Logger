package me.prism3.logger.events.plugindependent;

import com.bgsoftware.superiorskyblock.api.events.IslandChatEvent;
import me.prism3.logger.Main;
import me.prism3.logger.database.external.ExternalData;
import me.prism3.logger.database.sqlite.global.SQLiteData;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import me.prism3.logger.utils.Log;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Objects;


public class OnSuperiorSkyblock implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSuperiorSkyblockChat(final IslandChatEvent event) {

        final Player player = event.getPlayer().asPlayer();
        final String playerName = player.getName();
        final String worldName = player.getWorld().getName();
        final String message = event.getMessage().replace("\\", "\\\\");

        // Log To Files
        if (Data.isLogToFiles) {

            if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                if (!Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Player-Chat-Staff")).isEmpty()) {

                    this.main.getDiscord().staffChat(player, Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Player-Chat-Staff")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%message%", message), false);

                }

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true));
                    out.write(Objects.requireNonNull(this.main.getMessages().get().getString("Files.Player-Chat-Staff")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%message%", message) + "\n");
                    out.close();

                } catch (IOException e) {

                    Log.warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }

                if (Data.isExternal && this.main.getExternal().isConnected()) {

                    ExternalData.playerChat(Data.serverName, player, message, true);

                }

                if (Data.isSqlite && this.main.getSqLite().isConnected()) {

                    SQLiteData.insertPlayerChat(Data.serverName, player, message, true);

                }

                return;

            }

            try {

                BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getChatLogFile(), true));
                out.write(Objects.requireNonNull(this.main.getMessages().get().getString("Files.Player-Chat")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%message%", message) + "\n");
                out.close();

            } catch (IOException e) {

                Log.warning("An error occurred while logging into the appropriate file.");
                e.printStackTrace();

            }
        }

        // Discord Integration
        if (!player.hasPermission(Data.loggerExemptDiscord)) {

            if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                if (!Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Player-Chat-Staff")).isEmpty()) {

                    this.main.getDiscord().staffChat(player, Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Player-Chat-Staff")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%message%", message), false);

                }
            } else {

                if (!Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Player-Chat")).isEmpty()) {

                    this.main.getDiscord().playerChat(player, Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Player-Chat")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%message%", message), false);
                }
            }
        }

        // External
        if (Data.isExternal && this.main.getExternal().isConnected()) {

            try {

                ExternalData.playerChat(Data.serverName, player, message, player.hasPermission(Data.loggerStaffLog));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // SQLite
        if (Data.isSqlite && this.main.getSqLite().isConnected()) {

            try {

                SQLiteData.insertPlayerChat(Data.serverName, player, message, player.hasPermission(Data.loggerStaffLog));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

