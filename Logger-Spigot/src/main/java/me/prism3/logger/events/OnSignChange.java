package me.prism3.logger.events;

import me.prism3.logger.Main;
import me.prism3.logger.database.external.ExternalData;
import me.prism3.logger.database.sqlite.global.SQLiteData;
import me.prism3.logger.utils.BedrockChecker;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Objects;

public class OnSignChange implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSignChange(final SignChangeEvent event) {

        boolean shouldLogSignChange = !event.isCancelled() && main.getConfig().getBoolean("Log-Player.Sign-Change");

        if (shouldLogSignChange) {

            final Player player = event.getPlayer();

            if (player.hasPermission(Data.loggerExempt) || BedrockChecker.isBedrock(player.getUniqueId())) {
                return;
            }

            final String[] lines = event.getLines();
            final String worldName = player.getWorld().getName();
            final int x = event.getBlock().getX();
            final int y = event.getBlock().getY();
            final int z = event.getBlock().getZ();

            // Log To Files
            if (Data.isLogToFiles) {

                if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                    if (!this.main.getMessages().get().getString("Discord.Sign-Change-Staff").isEmpty()) {

                        this.main.getDiscord().staffChat(player, this.main.getMessages().get().getString("Discord.Sign-Change-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%line1%", lines[0].replace("\\", "\\\\")).replace("%line2%", lines[1].replace("\\", "\\\\")).replace("%line3%", lines[2].replace("\\", "\\\\")).replace("%line4%", lines[3].replace("\\", "\\\\")), false);
                    }

                    try {
                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true));
                        out.write(main.getMessages().get().getString("Files.Sign-Change-Staff")
                                .replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now()))
                                .replace("%world%", worldName)
                                .replace("%player%", player.getName())
                                .replace("%line1%", lines[0])
                                .replace("%line2%", lines[1])
                                .replace("%line3%", lines[2])
                                .replace("%line4%", lines[3])
                                .replace("%x%", String.valueOf(x))
                                .replace("%y%", String.valueOf(y))
                                .replace("%z%", String.valueOf(z)) + "\n");
                        out.close();

                    } catch (IOException e) {
                        main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();
                    }

                    if (Data.isExternal && this.main.getExternal().isConnected())
                        ExternalData.signChange(Data.serverName, player, x, y, z, "[" + lines[0] + "] " + "[" + lines[1] + "] " + "[" + lines[2] + "] " + "[" + lines[3] + "]", true);


                    if (Data.isSqlite && this.main.getSqLite().isConnected())
                        SQLiteData.insertSignChange(Data.serverName, player, x, y, z, "[" + lines[0] + "] " + "[" + lines[1] + "] " + "[" + lines[2] + "] " + "[" + lines[3] + "]", true);

                    return;
                }

                try {
                    Data data = new Data();

                    data.initializeDateFormatter();

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getSignChangeFile(), true));

                    out.write(Objects.requireNonNull(main.getMessages().get().getString("Files.Sign-Change"))
                            .replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now()))
                            .replace("%world%", worldName)
                            .replace("%player%", player.getName())
                            .replace("%line1%", lines[0])
                            .replace("%line2%", lines[1])
                            .replace("%line3%", lines[2])
                            .replace("%line4%", lines[3])
                            .replace("%x%", String.valueOf(x))
                            .replace("%y%", String.valueOf(y))
                            .replace("%z%", String.valueOf(z)) + "\n");

                    out.close();
                } catch (IOException e) {
                    main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();
                }
            }

            // Discord
            if (!player.hasPermission(Data.loggerExemptDiscord)) {

                if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                    if (!this.main.getMessages().get().getString("Discord.Sign-Change-Staff").isEmpty()) {

                        this.main.getDiscord().staffChat(player, this.main.getMessages().get().getString("Discord.Sign-Change-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%line1%", lines[0].replace("\\", "\\\\")).replace("%line2%", lines[1].replace("\\", "\\\\")).replace("%line3%", lines[2].replace("\\", "\\\\")).replace("%line4%", lines[3].replace("\\", "\\\\")), false);

                    }
                } else {

                    if (!this.main.getMessages().get().getString("Discord.Sign-Change").isEmpty()) {

                        this.main.getDiscord().signChange(player, this.main.getMessages().get().getString("Discord.Sign-Change-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%line1%", lines[0].replace("\\", "\\\\")).replace("%line2%", lines[1].replace("\\", "\\\\")).replace("%line3%", lines[2].replace("\\", "\\\\")).replace("%line4%", lines[3].replace("\\", "\\\\")), false);
                    }
                }
            }

            // External
            if (Data.isExternal && main.getExternal().isConnected()) {
                try {
                    ExternalData.signChange(Data.serverName, player, x, y, z, "[" + lines[0] + "] " + "[" + lines[1] + "] " + "[" + lines[2] + "] " + "[" + lines[3] + "]", player.hasPermission(Data.loggerStaffLog));
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }

            // SQLite
            if (Data.isSqlite && main.getSqLite().isConnected()) {
                try {
                    SQLiteData.insertSignChange(Data.serverName, player, x, y, z, "[" + lines[0] + "] " + "[" + lines[1] + "] " + "[" + lines[2] + "] " + "[" + lines[3] + "]", player.hasPermission(Data.loggerStaffLog));
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
