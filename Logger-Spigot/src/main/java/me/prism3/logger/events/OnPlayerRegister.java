package me.prism3.logger.events;

import me.prism3.logger.Main;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import me.prism3.logger.utils.Log;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class OnPlayerRegister {

    private final Main main = Main.getInstance();

    public OnPlayerRegister() {
        for (Player player : Bukkit.getServer().getOnlinePlayers())
            this.onRegister(player);
    }

    private void onRegister(Player player) {

        final String playerName = player.getName();
        final UUID playerUUID = player.getUniqueId();

        final LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochMilli(player.getFirstPlayed()), ZoneId.systemDefault());

        final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        // Log To Files
        if (Data.isLogToFiles) {

            try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getRegistrationFile(), true))) {

                out.write(this.main.getMessages().get().getString("Files.Player-Registration").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%player%", playerName).replace("%date%", dateFormat.format(ZonedDateTime.now())) + "\n");

            } catch (final IOException e) {

                Log.warning("An error occurred while logging into the appropriate file.");
                e.printStackTrace();

            }
        }

        // Discord Integration
        if (!player.hasPermission(Data.loggerExemptDiscord) && this.main.getMessages().get().getString("Discord.Player-Registration").isEmpty())
            this.main.getDiscord().playerRegistration(playerName, playerUUID, this.main.getMessages().get().getString("Discord.Player-Registration").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%player%", playerName).replace("%date%", dateFormat.format(ZonedDateTime.now())), false);

        // External
        if (Data.isExternal) {

            try {

                Main.getInstance().getDatabase().insertPlayerRegistration(Data.serverName, playerName, playerUUID.toString(), dateFormat.format(ZonedDateTime.now()));

            } catch (final Exception e) { e.printStackTrace(); }
        }

        // SQLite
        if (Data.isSqlite) {

            try {

                Main.getInstance().getSqLite().insertPlayerRegistration(Data.serverName, playerName, playerUUID.toString(), dateFormat.format(ZonedDateTime.now()));

            } catch (final Exception e) { e.printStackTrace(); }
        }
    }
}




