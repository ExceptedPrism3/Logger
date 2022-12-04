package me.prism3.logger.events.plugindependent;

import me.leoko.advancedban.bukkit.event.PunishmentEvent;
import me.prism3.logger.Main;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import me.prism3.logger.utils.Log;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;

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

        // Log To Files
        if (Data.isLogToFiles) {

            try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getAdvancedBanFile(), true))) {

                out.write(this.main.getMessages().get().getString("Files.Extras.AdvancedBan").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%executor%", executor).replace("%executed_on%", executedOn).replace("%reason%", reason).replace("%expiration%", String.valueOf(expirationDate)).replace("%type%", type).replace("%uuid%", playerUUID) + "\n");

            } catch (final IOException e) {

                Log.warning("An error occurred while logging into the appropriate file.");
                e.printStackTrace();
            }
        }

        // Discord Integration
        if (this.main.getDiscordFile().getBoolean("Discord.Enable") && !this.main.getMessages().get().getString("Discord.Extras.AdvancedBan").isEmpty())
            this.main.getDiscord().advancedBan(this.main.getMessages().get().getString("Discord.Extras.AdvancedBan").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%executor%", executor).replace("%executed_on%", executedOn).replace("%reason%", reason).replace("%expiration%", String.valueOf(expirationDate)).replace("%type%", type).replace("%uuid%", playerUUID), false);


        // External
        if (Data.isExternal) {

            try {

                Main.getInstance().getDatabase().getDatabaseQueue().queueAdvanceBanData(Data.serverName, type, executor, executedOn, reason, expirationDate);

            } catch (final Exception e) { e.printStackTrace(); }
        }

        // SQLite
        if (Data.isSqlite) {

            try {

                Main.getInstance().getDatabase().getDatabaseQueue().queueAdvanceBanData(Data.serverName, type, executor, executedOn, reason, expirationDate);

            } catch (final Exception e) { e.printStackTrace(); }
        }
    }
}
