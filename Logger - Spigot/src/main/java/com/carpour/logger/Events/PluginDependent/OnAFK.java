package com.carpour.logger.Events.PluginDependent;

import com.carpour.logger.Database.External.ExternalData;
import com.carpour.logger.Database.SQLite.SQLiteData;
import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Main;
import com.carpour.logger.Utils.FileHandler;
import com.carpour.logger.Utils.Messages;
import net.ess3.api.events.AfkStatusChangeEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;


public class OnAFK implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void afk(AfkStatusChangeEvent e) {

        boolean afk = e.getAffected().isAfk();

        Player player = e.getAffected().getBase();
        String playerName = player.getName();
        int x = player.getLocation().getBlockX();
        int y = player.getLocation().getBlockY();
        int z = player.getLocation().getBlockZ();
        String worldName = player.getWorld().getName();
        String serverName = main.getConfig().getString("Server-Name");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        if (player.hasPermission("logger.exempt")) return;

        if (!afk) {

            if (main.getConfig().getBoolean("Log-Extras.Essentials-AFK")) {

                // Log To Files Handling
                if (main.getConfig().getBoolean("Log-to-Files")) {

                    if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff.log")) {

                        if (!Objects.requireNonNull(Messages.get().getString("Discord.Extras.AFK-Staff")).isEmpty()) {

                            Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Extras.AFK-Staff")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)), false);
                        }

                        try {

                            BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getstaffFile(), true));
                            out.write(Objects.requireNonNull(Messages.get().getString("Files.Extras.AFK-Staff")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%player%", playerName).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)) + "\n");
                            out.close();

                        } catch (IOException event) {

                            main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                            event.printStackTrace();

                        }

                        if (main.getConfig().getBoolean("Database.Enable") && main.external.isConnected()) {

                            ExternalData.afk(serverName, worldName, playerName, x, y, z, true);

                        }

                        if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

                            SQLiteData.insertAFK(serverName, player, true);

                        }

                        return;

                    }

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getAfkFile(), true));
                        out.write(Objects.requireNonNull(Messages.get().getString("Files.Extras.AFK")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%player%", playerName).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)) + "\n");
                        out.close();

                    } catch (IOException event) {

                        main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                        event.printStackTrace();

                    }
                }

                // Discord
                if (!player.hasPermission("logger.exempt.discord")) {

                    if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff.log")) {

                        if (!Objects.requireNonNull(Messages.get().getString("Discord.Extras.AFK-Staff")).isEmpty()) {

                            Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Extras.AFK-Staff")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)), false);

                        }

                    } else {

                        if (!Objects.requireNonNull(Messages.get().getString("Discord.Extras.AFK")).isEmpty()) {

                            Discord.afk(player, Objects.requireNonNull(Messages.get().getString("Discord.Extras.AFK")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)), false);
                        }
                    }
                }

                // MySQL
                if (main.getConfig().getBoolean("Database.Enable") && main.external.isConnected()) {

                    try {

                        ExternalData.afk(serverName, worldName, playerName, x, y, z, player.hasPermission("logger.staff.log"));

                    } catch (Exception event) { event.printStackTrace(); }
                }

                // SQLite
                if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

                    try {

                        SQLiteData.insertAFK(serverName, player, player.hasPermission("logger.staff.log"));

                    } catch (Exception exception) { exception.printStackTrace(); }
                }
            }
        }
    }
}
