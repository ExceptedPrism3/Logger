package me.prism3.logger.events;

import me.prism3.logger.Main;
import me.prism3.logger.database.external.ExternalData;
import me.prism3.logger.database.sqlite.global.SQLiteData;
import me.prism3.logger.utils.BedrockChecker;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import me.prism3.logger.utils.Log;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketFillEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Objects;

public class OnBucketFill implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBucket(final PlayerBucketFillEvent event) {

        if (!event.isCancelled() && (this.main.getConfig().getBoolean("Log-Player.Bucket-Fill"))) {

            final Player player = event.getPlayer();

            if (player.hasPermission(Data.loggerExempt) || BedrockChecker.isBedrock(player.getUniqueId())) return;

            final String playerName = player.getName();
            final World world = event.getPlayer().getWorld();
            final String worldName = world.getName();
            final String bucket = Objects.requireNonNull(event.getItemStack()).getType().name().replace("_", " ");
            final int x = event.getBlockClicked().getX();
            final int y = event.getBlockClicked().getY();
            final int z = event.getBlockClicked().getZ();

            // Log To Files
            if (Data.isLogToFiles) {

                if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                    if (!Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Bucket-Fill-Staff")).isEmpty()) {

                        this.main.getDiscord().staffChat(player, Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Bucket-Fill-Staff")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%bucket%", bucket), false);

                    }

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true));
                        out.write(Objects.requireNonNull(this.main.getMessages().get().getString("Files.Bucket-Fill-Staff")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%bucket%", bucket) + "\n");
                        out.close();

                    } catch (IOException e) {

                        Log.warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }

                    if (Data.isExternal && this.main.getExternal().isConnected()) {

                        ExternalData.bucketFill(Data.serverName, player, bucket, x, y, z, true);

                    }

                    if (Data.isSqlite && this.main.getSqLite().isConnected()) {

                        SQLiteData.insertBucketFill(Data.serverName, player, bucket, true);

                    }

                    return;

                }

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getBucketFillFile(), true));
                    out.write(Objects.requireNonNull(this.main.getMessages().get().getString("Files.Bucket-Fill")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%bucket%", bucket) + "\n");
                    out.close();

                } catch (IOException e) {

                    Log.warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord
            if (!player.hasPermission(Data.loggerExemptDiscord)) {

                if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                    if (!Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Bucket-Fill-Staff")).isEmpty()) {

                        this.main.getDiscord().staffChat(player, Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Bucket-Fill-Staff")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%bucket%", bucket), false);

                    }
                } else {

                    if (!Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Bucket-Fill")).isEmpty()) {

                        this.main.getDiscord().bucketFill(player, Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Bucket-Fill")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%bucket%", bucket), false);
                    }
                }
            }

            // External
            if (Data.isExternal && this.main.getExternal().isConnected()) {

                try {

                    ExternalData.bucketFill(Data.serverName, player, bucket, x, y, z, player.hasPermission(Data.loggerStaffLog));

                } catch (Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (Data.isSqlite && this.main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertBucketFill(Data.serverName, player, bucket, player.hasPermission(Data.loggerStaffLog));

                } catch (Exception e) { e.printStackTrace(); }
            }
        }
    }
}
