package me.prism3.logger.events;

import me.prism3.logger.Main;
import me.prism3.logger.database.external.ExternalData;
import me.prism3.logger.database.sqlite.global.SQLiteData;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
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
import java.util.Objects;

public class OnPlayerRegister {

    private final Main main = Main.getInstance();

    public OnPlayerRegister() {

        for (Player player : Bukkit.getServer().getOnlinePlayers()) {

            this.onRegister(player);

        }
    }

    private void onRegister(Player player) {

        final String playerName = player.getName();

        final LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochMilli(player.getFirstPlayed()), ZoneId.systemDefault());

        final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        // Log To Files
        if (Data.isLogToFiles) {

            try {

                BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getRegistrationFile(), true));
                out.write(Objects.requireNonNull(this.main.getMessages().get().getString("Files.Player-Registration")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%player%", playerName).replace("%date%", dateFormat.format(ZonedDateTime.now())) + "\n");
                out.close();

            } catch (IOException e) {

                this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                e.printStackTrace();

            }
        }

        // Discord Integration
        if (!player.hasPermission(Data.loggerExemptDiscord)) {

            if (!Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Player-Registration")).isEmpty()) {

                this.main.getDiscord().playerRegistration(player, Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Player-Registration")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%player%", playerName).replace("%date%", dateFormat.format(ZonedDateTime.now())), false);
            }
        }

        // External
        if (Data.isExternal && this.main.getExternal().isConnected()) {

            try {

                ExternalData.playerRegistration(Data.serverName, player, dateFormat.format(ZonedDateTime.now()));

            } catch (Exception e) { e.printStackTrace(); }
        }

        // SQLite
        if (Data.isSqlite && this.main.getSqLite().isConnected()) {

            try {

                SQLiteData.insertRegistration(Data.serverName, player, dateFormat.format(ZonedDateTime.now()));

            } catch (Exception e) { e.printStackTrace(); }
        }
    }
}




