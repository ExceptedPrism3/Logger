package me.prism3.logger.events;

import me.prism3.logger.Main;
import me.prism3.logger.database.external.ExternalData;
import me.prism3.logger.database.sqlite.global.SQLiteData;
import me.prism3.logger.utils.BedrockChecker;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Objects;

public class OnBlockPlace implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlace(final BlockPlaceEvent event) {

        if (!event.isCancelled() && this.main.getConfig().getBoolean("Log-Player.Block-Place")) {

            final Player player = event.getPlayer();

            if (player.hasPermission(Data.loggerExempt) || BedrockChecker.isBedrock(player.getUniqueId())) return;

            final String playerName = player.getName();
            final World world = player.getWorld();
            final String worldName = world.getName();
            final int x = event.getBlock().getLocation().getBlockX();
            final int y = event.getBlock().getLocation().getBlockY();
            final int z = event.getBlock().getLocation().getBlockZ();
            final Material blockType = event.getBlock().getType();

            // Log To Files
            if (Data.isLogToFiles) {

                if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                    if (!Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Block-Place-Staff")).isEmpty()) {

                        this.main.getDiscord().staffChat(player, Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Block-Place-Staff")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%block%", String.valueOf(blockType)), false);

                    }

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true));
                        out.write(Objects.requireNonNull(this.main.getMessages().get().getString("Files.Block-Place-Staff")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%block%", String.valueOf(blockType)) + "\n");
                        out.close();

                    } catch (IOException e) {

                        this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }

                    if (Data.isExternal && this.main.getExternal().isConnected()) {

                        ExternalData.blockPlace(Data.serverName, player, blockType.toString(), x, y, z, true);

                    }

                    if (Data.isSqlite && this.main.getSqLite().isConnected()) {

                        SQLiteData.insertBlockPlace(Data.serverName, player, blockType, true);
                    }

                    return;

                }

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getBlockPlaceLogFile(), true));
                    out.write(Objects.requireNonNull(this.main.getMessages().get().getString("Files.Block-Place")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%block%", String.valueOf(blockType)) + "\n");
                    out.close();

                } catch (IOException e) {

                    this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord
            if (!player.hasPermission(Data.loggerExemptDiscord)) {

                if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                    if (!Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Block-Place-Staff")).isEmpty()) {

                        this.main.getDiscord().staffChat(player, Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Block-Place-Staff")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%block%", String.valueOf(blockType)), false);

                    }
                } else {

                    if (!Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Block-Place")).isEmpty()) {

                        this.main.getDiscord().blockPlace(player, Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Block-Place")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%block%", String.valueOf(blockType)), false);
                    }
                }
            }

            // External
            if (Data.isExternal && this.main.getExternal().isConnected()) {

                try {

                    ExternalData.blockPlace(Data.serverName, player, blockType.toString(), x, y, z, player.hasPermission(Data.loggerStaffLog));

                } catch (Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (Data.isSqlite && this.main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertBlockPlace(Data.serverName, player, blockType, player.hasPermission(Data.loggerStaffLog));

                } catch (Exception e) { e.printStackTrace(); }
            }
        }
    }
}
