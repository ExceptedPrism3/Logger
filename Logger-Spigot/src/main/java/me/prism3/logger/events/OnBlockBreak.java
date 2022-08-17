package me.prism3.logger.events;

import com.carpour.loggercore.database.entity.Coordinates;
import me.prism3.logger.Main;
import me.prism3.logger.utils.BedrockChecker;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.UUID;

import static me.prism3.logger.utils.Data.loggerStaffLog;

public class OnBlockBreak implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreak(final BlockBreakEvent event) {

        if (!event.isCancelled()) {

            final Player player = event.getPlayer();

            if (player.hasPermission(Data.loggerExempt) || BedrockChecker.isBedrock(player.getUniqueId())) return;

            final String playerName = player.getName();
            final UUID playerUUID = player.getUniqueId();
            final World world = player.getWorld();
            final String worldName = world.getName();
            final int x = event.getBlock().getLocation().getBlockX();
            final int y = event.getBlock().getLocation().getBlockY();
            final int z = event.getBlock().getLocation().getBlockZ();
            final Material blockType = event.getBlock().getType();


            final Coordinates coordinates = new Coordinates(x, y, z, worldName);

            // Log To Files
            if (Data.isLogToFiles) {

                if (Data.isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    try {

                        final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true));
                        out.write(this.main.getMessages().get().getString("Files.Block-Break-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%block%", String.valueOf(blockType)) + "\n");
                        out.close();

                    } catch (IOException e) {

                        this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }
                } else {

                    try {

                        final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getBlockBreakLogFile(), true));
                        out.write(this.main.getMessages().get().getString("Files.Block-Break").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%block%", String.valueOf(blockType)) + "\n");
                        out.close();

                    } catch (IOException e) {

                        this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }
                }
            }

            // Discord
            if (!player.hasPermission(Data.loggerExemptDiscord)) {

                if (Data.isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    if (!this.main.getMessages().get().getString("Discord.Block-Break-Staff").isEmpty()) {

                        this.main.getDiscord().staffChat(playerName, playerUUID, this.main.getMessages().get().getString("Discord.Block-Break-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%block%", String.valueOf(blockType)), false);

                    }

                } else {

                    if (!this.main.getMessages().get().getString("Discord.Block-Break").isEmpty()) {

                        this.main.getDiscord().blockBreak(playerName, playerUUID, this.main.getMessages().get().getString("Discord.Block-Break").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%block%", String.valueOf(blockType)), false);
                    }
                }
            }

            // External
            if (Data.isExternal ) {

                try {

                    Main.getInstance().getDatabase().insertBlockBreak(Data.serverName, playerName, playerUUID.toString(), blockType.name(), coordinates, player.hasPermission(loggerStaffLog));

                } catch (Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (Data.isSqlite) {

                try {

                    Main.getInstance().getSqLite().insertBlockBreak(Data.serverName, playerName, playerUUID.toString(), blockType.name(), coordinates, player.hasPermission(loggerStaffLog));

                } catch (Exception e) { e.printStackTrace(); }
            }
        }
    }
}
