package me.prism3.logger.Events.Misc;

import me.prism3.logger.Database.External.ExternalData;
import me.prism3.logger.Database.SQLite.Global.SQLiteData;
import me.prism3.logger.Discord.Discord;
import me.prism3.logger.Main;
import me.prism3.logger.Utils.FileHandler;
import me.prism3.logger.Utils.Messages;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Objects;

import static me.prism3.logger.Utils.Data.*;

public class OnPrimedTNT implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTntPrime(final EntityExplodeEvent event){

        if (!event.isCancelled() && this.main.getConfig().getBoolean("Log-Player.Primed-TNT")) {

            if (event.getEntity() instanceof TNTPrimed) {

                final Player player = (Player) ((TNTPrimed) event.getEntity()).getSource();

                if (player == null || player.hasPermission(loggerExempt)) return;

                final String playerName = player.getName();
                final World world = player.getWorld();
                final String worldName = world.getName();
                final int x = event.getLocation().getBlockX();
                final int y = event.getLocation().getBlockY();
                final int z = event.getLocation().getBlockZ();

                // Log To Files
                if (isLogToFiles) {

                    if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                        if (!Objects.requireNonNull(Messages.get().getString("Discord.Primed-TNT-Staff")).isEmpty()) {

                            Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Primed-TNT-Staff")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)), false);

                        }

                        try {

                            BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getstaffFile(), true));
                            out.write(Objects.requireNonNull(Messages.get().getString("Files.Primed-TNT-Staff")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%player%", playerName).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)) + "\n");
                            out.close();

                        } catch (IOException e) {

                            this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                            e.printStackTrace();

                        }

                        if (isExternal && this.main.getExternal().isConnected()) {

                            ExternalData.primedTNT(serverName, player, x, y, z, true);

                        }

                        if (isSqlite && this.main.getSqLite().isConnected()) {

                            SQLiteData.insertPrimedTNT(serverName, player, x, y, z, true);

                        }

                        return;

                    }

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getPrimedTNTFile(), true));
                        out.write(Objects.requireNonNull(Messages.get().getString("Files.Primed-TNT")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%player%", playerName).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)) + "\n");
                        out.close();

                    } catch (IOException e) {

                        this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }
                }

                // Discord Integration
                if (!player.hasPermission(loggerExemptDiscord)) {

                    if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                        if (!Objects.requireNonNull(Messages.get().getString("Discord.Primed-TNT-Staff")).isEmpty()) {

                            Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Primed-TNT-Staff")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)), false);

                        }
                    } else {

                        if (!Objects.requireNonNull(Messages.get().getString("Discord.Primed-TNT")).isEmpty()) {

                            Discord.playerChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Primed-TNT")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)), false);
                        }
                    }
                }

                // External
                if (isExternal && this.main.getExternal().isConnected()) {

                    try {

                        ExternalData.primedTNT(serverName, player, x, y, z, player.hasPermission(loggerStaffLog));

                    } catch (Exception e) { e.printStackTrace(); }
                }

                // SQLite
                if (isSqlite && this.main.getSqLite().isConnected()) {

                    try {

                        SQLiteData.insertPrimedTNT(serverName, player, x, y, z, player.hasPermission(loggerStaffLog));

                    } catch (Exception e) { e.printStackTrace(); }
                }
            }
        }
    }
}
