package me.prism3.logger.events.plugindependent;

import fr.xephi.authme.events.FailedLoginEvent;
import me.prism3.logger.Main;
import me.prism3.logger.utils.BedrockChecker;
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
import java.util.UUID;

import static me.prism3.logger.utils.Data.loggerStaffLog;

public class OnAuthMePassword implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void password(final FailedLoginEvent event) {

        final Player player = event.getPlayer();

        if (player.hasPermission(Data.loggerExempt) || BedrockChecker.isBedrock(player.getUniqueId())) return;

        final String playerName = player.getName();
        final UUID playerUUID = player.getUniqueId();
        final String worldName = player.getWorld().getName();


        // Log To Files
        if (Data.isLogToFiles) {

            if (Data.isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true))) {
                   
                    out.write(this.main.getMessages().get().getString("Files.Extras.Wrong-Password-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName) + "\n");

                } catch (final IOException e) {

                    Log.warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            } else {

                try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getWrongPasswordFile(), true))) {

                    out.write(this.main.getMessages().get().getString("Files.Extras.Wrong-Password").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName) + "\n");

                } catch (final IOException e) {

                    Log.warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }
        }

        // Discord
        if (!player.hasPermission(Data.loggerExemptDiscord)) {

            if (Data.isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                if (!this.main.getMessages().get().getString("Discord.Extras.Wrong-Password-Staff").isEmpty()) {

                    this.main.getDiscord().staffChat(playerName, playerUUID, this.main.getMessages().get().getString("Discord.Extras.Wrong-Password-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName), false);

                }
            } else {

                if (!this.main.getMessages().get().getString("Discord.Extras.Wrong-Password").isEmpty()) {

                    this.main.getDiscord().wrongPassword(playerName, playerUUID, this.main.getMessages().get().getString("Discord.Extras.Wrong-Password").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName), false);
                }
            }
        }

        // External
        if (Data.isExternal) {

            try {

                Main.getInstance().getDatabase().insertWrongPassword(Data.serverName, playerName, playerUUID.toString(), worldName, player.hasPermission(loggerStaffLog));

            } catch (final Exception e) { e.printStackTrace(); }
        }

        // SQLite
        if (Data.isSqlite) {

            try {

                Main.getInstance().getSqLite().insertWrongPassword(Data.serverName, playerName, playerUUID.toString(), worldName, player.hasPermission(loggerStaffLog));

            } catch (final Exception e) { e.printStackTrace(); }
        }
    }
}
