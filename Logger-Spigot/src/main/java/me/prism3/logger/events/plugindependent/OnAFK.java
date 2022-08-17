package me.prism3.logger.events.plugindependent;

import com.carpour.loggercore.database.entity.Coordinates;
import me.prism3.logger.Main;
import me.prism3.logger.utils.BedrockChecker;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import net.ess3.api.events.AfkStatusChangeEvent;
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

public class OnAFK implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void afk(final AfkStatusChangeEvent e) {

        if (!e.isCancelled() && !e.getAffected().isAfk()) {

            final Player player = e.getAffected().getBase();

            if (player.hasPermission(Data.loggerExempt) || BedrockChecker.isBedrock(player.getUniqueId())) return;

            final String playerName = player.getName();
            final UUID playerUUID = player.getUniqueId();
            final int x = player.getLocation().getBlockX();
            final int y = player.getLocation().getBlockY();
            final int z = player.getLocation().getBlockZ();
            final String worldName = player.getWorld().getName();


            final Coordinates coordinates = new Coordinates(x, y, z, worldName);

            // Log To Files
            if (Data.isLogToFiles) {

                if (Data.isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    try {

                        final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true));
                        out.write(this.main.getMessages().get().getString("Files.Extras.AFK-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)) + "\n");
                        out.close();

                    } catch (IOException event) {

                        this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                        event.printStackTrace();

                    }
                } else {

                    try {

                        final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getAfkFile(), true));
                        out.write(this.main.getMessages().get().getString("Files.Extras.AFK").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)) + "\n");
                        out.close();

                    } catch (IOException event) {

                        this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                        event.printStackTrace();

                    }
                }
            }

            // Discord
            if (!player.hasPermission(Data.loggerExemptDiscord)) {

                if (Data.isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    if (!this.main.getMessages().get().getString("Discord.Extras.AFK-Staff").isEmpty()) {

                        this.main.getDiscord().staffChat(playerName, playerUUID, this.main.getMessages().get().getString("Discord.Extras.AFK-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)), false);

                    }
                } else {

                    if (!this.main.getMessages().get().getString("Discord.Extras.AFK").isEmpty()) {

                        this.main.getDiscord().afk(playerName, playerUUID, this.main.getMessages().get().getString("Discord.Extras.AFK").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)), false);
                    }
                }
            }

            // External
            if (Data.isExternal) {

                try {

                    Main.getInstance().getDatabase().insertAfk(Data.serverName, playerName, playerUUID.toString(), coordinates, player.hasPermission(loggerStaffLog));

                } catch (Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (Data.isSqlite) {

                try {

                    Main.getInstance().getSqLite().insertAfk(Data.serverName, playerName, playerUUID.toString(), coordinates, player.hasPermission(loggerStaffLog));

                } catch (Exception e) { e.printStackTrace(); }
            }
        }
    }
}
