package me.prism3.logger.events;

import me.prism3.logger.Main;
import me.prism3.logger.database.external.ExternalData;
import me.prism3.logger.database.sqlite.global.SQLiteData;
import me.prism3.logger.events.spy.OnSignSpy;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class OnSign implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerSign(final SignChangeEvent event) {

        // Sign Spy
        if (this.main.getConfig().getBoolean("Spy-Features.Book-Spy.Enable")) {

            new OnSignSpy().onSignSpy(event);

        }

        if (!event.isCancelled() && this.main.getConfig().getBoolean("Log-Player.Sign-Text")) {

            final Player player = event.getPlayer();

            if (player.hasPermission(Data.loggerExempt)) return;

            final String playerName = player.getName();
            final World world = player.getWorld();
            final List<String> lines = Arrays.asList(event.getLines());
            final String worldName = world.getName();
            final int x = player.getLocation().getBlockX();
            final int y = player.getLocation().getBlockY();
            final int z = player.getLocation().getBlockZ();

            // Log To Files
            if (Data.isLogToFiles) {

                if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                    if (!Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Player-Sign-Text-Staff")).isEmpty()) {

                        this.main.getDiscord().staffChat(player, Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Player-Sign-Text-Staff")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%line1%", lines.get(0).replace("\\", "\\\\")).replace("%line2%", lines.get(1).replace("\\", "\\\\")).replace("%line3%", lines.get(2).replace("\\", "\\\\")).replace("%line4%", lines.get(3).replace("\\", "\\\\")), false);

                    }

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true));
                        out.write(Objects.requireNonNull(this.main.getMessages().get().getString("Files.Player-Sign-Text-Staff")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%line1%", lines.get(0).replace("\\", "\\\\")).replace("%line2%", lines.get(1).replace("\\", "\\\\")).replace("%line3%", lines.get(2).replace("\\", "\\\\")).replace("%line4%", lines.get(3).replace("\\", "\\\\")) + "\n");
                        out.close();

                    } catch (IOException e) {

                        this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }

                    if (Data.isExternal && this.main.getExternal().isConnected()) {

                        ExternalData.playerSignText(Data.serverName, player, x, y, z, "[" + lines.get(0) + "] " + "[" + lines.get(1) + "] " + "[" + lines.get(2) + "] " + "[" + lines.get(3) + "]", true);

                    }

                    if (Data.isSqlite && this.main.getSqLite().isConnected()) {

                        SQLiteData.insertPlayerSignText(Data.serverName, player, "[" + lines.get(0) + "] " + "[" + lines.get(1) + "] " + "[" + lines.get(2) + "] " + "[" + lines.get(3) + "]", true);

                    }

                    return;
                }

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getSignLogFile(), true));
                    out.write(Objects.requireNonNull(this.main.getMessages().get().getString("Files.Player-Sign-Text")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%line1%", lines.get(0).replace("\\", "\\\\")).replace("%line2%", lines.get(1).replace("\\", "\\\\")).replace("%line3%", lines.get(2).replace("\\", "\\\\")).replace("%line4%", lines.get(3).replace("\\", "\\\\")) + "\n");
                    out.close();

                } catch (IOException e) {

                    this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord
            if (!player.hasPermission(Data.loggerExemptDiscord)) {

                if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                    if (!Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Player-Sign-Text-Staff")).isEmpty()) {

                        this.main.getDiscord().staffChat(player, Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Player-Sign-Text-Staff")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%line1%", lines.get(0).replace("\\", "\\\\")).replace("%line2%", lines.get(1).replace("\\", "\\\\")).replace("%line3%", lines.get(2).replace("\\", "\\\\")).replace("%line4%", lines.get(3).replace("\\", "\\\\")), false);

                    }
                } else {

                    if (!Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Player-Sign-Text")).isEmpty()) {

                        this.main.getDiscord().playerSignText(player, Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Player-Sign-Text-Staff")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%line1%", lines.get(0).replace("\\", "\\\\")).replace("%line2%", lines.get(1).replace("\\", "\\\\")).replace("%line3%", lines.get(2).replace("\\", "\\\\")).replace("%line4%", lines.get(3).replace("\\", "\\\\")), false);
                    }
                }
            }

            // External
            if (Data.isExternal && this.main.getExternal().isConnected()) {

                try {

                    ExternalData.playerSignText(Data.serverName, player, x, y, z, "[" + lines.get(0) + "] " + "[" + lines.get(1) + "] " + "[" + lines.get(2) + "] " + "[" + lines.get(3) + "]", player.hasPermission(Data.loggerStaffLog));

                } catch (Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (Data.isSqlite && this.main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertPlayerSignText(Data.serverName, player, "[" + lines.get(0) + "] " + "[" + lines.get(1) + "] " + "[" + lines.get(2) + "] " + "[" + lines.get(3) + "]", player.hasPermission(Data.loggerStaffLog));

                } catch (Exception exception) { exception.printStackTrace(); }
            }
        }
    }
}
