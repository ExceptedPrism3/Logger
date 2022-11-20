package me.prism3.loggerbungeecord.events.plugindependent;

import me.leoko.advancedban.bungee.event.PunishmentEvent;
import me.prism3.loggerbungeecord.Main;
import me.prism3.loggerbungeecord.utils.Data;
import me.prism3.loggerbungeecord.utils.FileHandler;
import me.prism3.loggerbungeecord.utils.Log;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;

import static me.prism3.loggerbungeecord.utils.Data.*;

public class OnAdvancedBan implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPunishment(final PunishmentEvent event) {

        final String type = event.getPunishment().getType().toString();
        final String executor = event.getPunishment().getOperator();
        final ProxiedPlayer player = ProxyServer.getInstance().getPlayer(executor);
        final String playerUUID = player == null ? "" : player.getUniqueId().toString();
        final String executedOn = event.getPunishment().getName();
        final String reason = event.getPunishment().getReason();
        final long expirationDate = event.getPunishment().getEnd();

        // Log To Files
        if (isLogToFiles) {

            try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getAdvancedBanLogFile(), true))) {

                out.write(main.getMessages().getString("Files.Extras.AdvancedBan").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%executor%", executor).replace("%executed_on%", executedOn).replace("%reason%", reason).replace("%expiration%", String.valueOf(expirationDate)).replace("%type%", type).replace("%uuid%", playerUUID) + "\n");

            } catch (final IOException e) {

                Log.severe("An error occurred while logging into the appropriate file.");
                e.printStackTrace();
            }
        }

        // Discord Integration
        if (!main.getMessages().getString("Discord.Extras.AdvancedBan").isEmpty() && main.getDiscordFile().getBoolean("Discord.Enable"))
            this.main.getDiscord().advancedBan(this.main.getMessages().getString("Discord.Extras.AdvancedBan").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%executor%", executor).replace("%executed_on%", executedOn).replace("%reason%", reason).replace("%expiration%", String.valueOf(expirationDate)).replace("%type%", type).replace("%uuid%", playerUUID), false);

        // External
        if (isExternal) {

            try {

//                        Main.getInstance().getDatabase().insertAdvancedBan(serverName, executorName, entryType.toUpperCase(), onWho, duration, reason, isSilent);

            } catch (final Exception e) { e.printStackTrace(); }
        }

        // SQLite
        if (isSqlite) {

            try {

//                        Main.getInstance().getSqLite().insertAdvancedBan(serverName, executorName, entryType.toUpperCase(), onWho, duration, reason, isSilent);

            } catch (final Exception e) { e.printStackTrace(); }
        }
    }
}
