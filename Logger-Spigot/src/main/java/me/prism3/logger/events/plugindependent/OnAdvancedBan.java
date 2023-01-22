package me.prism3.logger.events.plugindependent;

import me.leoko.advancedban.bukkit.event.PunishmentEvent;
import me.prism3.logger.Main;
import me.prism3.logger.discord.DiscordChannels;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import static me.prism3.logger.utils.Data.*;

public class OnAdvancedBan implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPunishment(final PunishmentEvent event) {

        final String type = event.getPunishment().getType().toString();
        final String executor = event.getPunishment().getOperator();
        final Player player = Bukkit.getPlayer(executor);
        final String playerUUID = player == null ? "" : player.getUniqueId().toString();
        final String executedOn = event.getPunishment().getName();
        final String reason = event.getPunishment().getReason();
        final long expirationDate = event.getPunishment().getEnd();

        final Map<String, String> placeholders = new HashMap<>();
        placeholders.put("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now()));
        placeholders.put("%uuid%", playerUUID);
        placeholders.put("%executor%", executor);
        placeholders.put("%executed_on%", executedOn);
        placeholders.put("%expiration%", String.valueOf(expirationDate));
        placeholders.put("%type%", type);

        // Log To Files
        if (Data.isLogToFiles)
            FileHandler.handleFileLog("Files.Extras.AdvancedBan", placeholders, FileHandler.getAdvancedBanFile());

        // Discord Integration
        if (!player.hasPermission(loggerExemptDiscord) && this.main.getDiscordFile().getBoolean("Discord.Enable"))
                this.main.getDiscord().handleDiscordLog("Discord.Extras.AdvancedBan", placeholders, DiscordChannels.ADVANCED_BAN, executor, player.getUniqueId());

        // External
        if (Data.isExternal) {

            try {

                Main.getInstance().getDatabase().getDatabaseQueue().queueAdvanceBanData(Data.serverName, type, executor, executedOn, reason, expirationDate, playerUUID);

            } catch (final Exception e) { e.printStackTrace(); }
        }

        // SQLite
        if (Data.isSqlite) {

            try {

                Main.getInstance().getDatabase().getDatabaseQueue().queueAdvanceBanData(Data.serverName, type, executor, executedOn, reason, expirationDate, playerUUID);

            } catch (final Exception e) { e.printStackTrace(); }
        }
    }
}
