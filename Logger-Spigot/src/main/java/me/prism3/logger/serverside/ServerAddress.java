package me.prism3.logger.serverside;

import me.prism3.logger.Main;
import me.prism3.logger.utils.BedrockChecker;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import me.prism3.logger.utils.Log;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.UUID;

public class ServerAddress implements Listener {

    private final Main main = Main.getInstance();//todo velocity

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onServerAddress(final PlayerLoginEvent event) {

        final Player player = event.getPlayer();

        if (player.hasPermission(Data.loggerExempt) || BedrockChecker.isBedrock(player.getUniqueId())) return;

        final UUID playerUUID = player.getUniqueId();
        final String playerName = player.getName();
        final String address = event.getHostname();

        // Log To Files
        if (Data.isLogToFiles) {

            if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true))) {

                    out.write(this.main.getMessages().get().getString("Files.Server-Side.Server-Address-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%player%", playerName).replace("%address%", address).replace("%uuid%", playerUUID.toString()) + "\n");

                } catch (final IOException e) {

                    Log.warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();
                }
            } else {

                try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getServerAddressFile(), true))) {

                    out.write(this.main.getMessages().get().getString("Files.Server-Side.Server-Address").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%player%", playerName).replace("%address%", address).replace("%uuid%", playerUUID.toString()) + "\n");

                } catch (final IOException e) {

                    Log.warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();
                }
            }
        }

        // Discord
        if (!player.hasPermission(Data.loggerExemptDiscord) && this.main.getDiscordFile().getBoolean("Discord.Enable")) {

            if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                if (!this.main.getMessages().get().getString("Discord.Server-Side.Server-Address-Staff").isEmpty()) {

                    this.main.getDiscord().staffChat(playerName, playerUUID, this.main.getMessages().get().getString("Discord.Server-Side.Server-Address-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%address%", address).replace("%uuid%", playerUUID.toString()), false);
                }
            } else {
                if (!this.main.getMessages().get().getString("Discord.Server-Side.Server-Address").isEmpty()) {

                    this.main.getDiscord().playerJoin(playerName, playerUUID, this.main.getMessages().get().getString("Discord.Server-Side.Server-Address").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%address%", address).replace("%uuid%", playerUUID.toString()), false);
                }
            }
        }

        // External
        if (Data.isExternal) {

            try {

              Main.getInstance().getDatabase().getDatabaseQueue().queueServerAddress(Data.serverName, playerName, playerUUID.toString(), address);

            } catch (final Exception e) { e.printStackTrace(); }
        }

        // SQLite
        if (Data.isSqlite) {

            try {

                Main.getInstance().getDatabase().getDatabaseQueue().queueServerAddress(Data.serverName, playerName, playerUUID.toString(), address);

            } catch (final Exception e) { e.printStackTrace(); }
        }
    }
}
