package me.prism3.logger.events.misc;

import com.carpour.loggercore.database.entity.Coordinates;
import com.carpour.loggercore.database.entity.EntityPlayer;
import me.prism3.logger.Main;
import me.prism3.logger.utils.BedrockChecker;
import me.prism3.logger.utils.FileHandler;
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
import java.util.UUID;

import static me.prism3.logger.utils.Data.*;

public class OnPrimedTNT implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTntPrime(final EntityExplodeEvent event) {

        if (!event.isCancelled() && this.main.getConfig().getBoolean("Log-Player.Primed-TNT") && event.getEntity() instanceof TNTPrimed) {

            final Player player = (Player) ((TNTPrimed) event.getEntity()).getSource();

            if (player == null || player.hasPermission(loggerExempt) || BedrockChecker.isBedrock(player.getUniqueId())) return;

            final String playerName = player.getName();
            final UUID playerUUID = player.getUniqueId();
            final World world = player.getWorld();
            final String worldName = world.getName();
            final int x = event.getLocation().getBlockX();
            final int y = event.getLocation().getBlockY();
            final int z = event.getLocation().getBlockZ();

            final EntityPlayer entityPlayer = new EntityPlayer(playerName, playerUUID.toString());
            final Coordinates coordinates = new Coordinates(x, y, z, worldName);

            // File Logging
            if (isLogToFiles) {

                if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    try {

                        final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true));
                        out.write(this.main.getMessages().get().getString("Files.Primed-TNT-Staff").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%uuid%", playerUUID.toString()) + "\n");
                        out.close();

                    } catch (IOException e) {

                        this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }
                } else {

                    try {

                        final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getPrimedTNTFile(), true));
                        out.write(this.main.getMessages().get().getString("Files.Primed-TNT").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%uuid%", playerUUID.toString()) + "\n");
                        out.close();

                    } catch (IOException e) {

                        this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }
                }
            }

            // Discord Integration
            if (!player.hasPermission(loggerExemptDiscord)) {

                if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    if (!this.main.getMessages().get().getString("Discord.Primed-TNT-Staff").isEmpty()) {

                        this.main.getDiscord().staffChat(playerName, playerUUIDName, playerUUID, this.main.getMessages().get().getString("Discord.Primed-TNT-Staff").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%uuid%", playerUUID.toString()), false);

                    }
                } else {

                    if (!this.main.getMessages().get().getString("Discord.Primed-TNT").isEmpty()) {

                        this.main.getDiscord().primedTNT(playerName, playerUUID, this.main.getMessages().get().getString("Discord.Primed-TNT").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%uuid%", playerUUID.toString()), false);
                    }
                }
            }

            // External
            if (isExternal) {

                try {

                    Main.getInstance().getDatabase().insertPrimedTnt(serverName, entityPlayer, coordinates, player.hasPermission(loggerStaffLog));

                } catch (Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (isSqlite) {

                try {

                    Main.getInstance().getSqLite().insertPrimedTnt(serverName, entityPlayer, coordinates, player.hasPermission(loggerStaffLog));

                } catch (Exception e) { e.printStackTrace(); }
            }
        }
    }
}
