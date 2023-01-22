package me.prism3.logger.events;

import me.prism3.logger.Main;
import me.prism3.logger.discord.DiscordChannels;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static me.prism3.logger.utils.Data.*;

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

        final Map<String, String> placeholders = new HashMap<>();
        placeholders.put("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now()));
        placeholders.put("%date%", dateFormat.format(ZonedDateTime.now()));
        placeholders.put("%uuid%", playerUUID.toString());
        placeholders.put("%player%", playerName);

        // Log To Files
        if (Data.isLogToFiles)
            FileHandler.handleFileLog("Files.Player-Registration", placeholders, FileHandler.getRegistrationFile());

        // Discord Integration
        if (!player.hasPermission(loggerExemptDiscord) && this.main.getDiscordFile().getBoolean("Discord.Enable"))
            this.main.getDiscord().handleDiscordLog("Discord.Player-Registration", placeholders, DiscordChannels.REGISTRATION, playerName, playerUUID);

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




