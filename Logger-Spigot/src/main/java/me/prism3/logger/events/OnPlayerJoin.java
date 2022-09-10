package me.prism3.logger.events;

import me.prism3.logger.Main;
import me.prism3.logger.database.sqlite.global.registration.SQLiteDataRegistration;
import me.prism3.logger.utils.BedrockChecker;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import me.prism3.logger.utils.Log;
import me.prism3.loggercore.database.data.Coordinates;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.ZonedDateTime;
import java.util.UUID;

import static me.prism3.logger.utils.Data.loggerStaffLog;

public class OnPlayerJoin implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(final PlayerJoinEvent event) {

        if (Data.isRegistration && !SQLiteDataRegistration.playerExists(event.getPlayer())) {

            SQLiteDataRegistration.insertRegistration(event.getPlayer());
            new OnPlayerRegister();

        }

        final Player player = event.getPlayer();

        if (player.hasPermission(Data.loggerExempt) || BedrockChecker.isBedrock(player.getUniqueId())) return;

        final World world = player.getWorld();
        final String worldName = world.getName();
        final String playerName = player.getName();
        final UUID playerUUID = player.getUniqueId();
        InetSocketAddress ip = player.getAddress();
        if (!Data.isPlayerIP) ip = null;
        final int x = player.getLocation().getBlockX();
        final int y = player.getLocation().getBlockY();
        final int z = player.getLocation().getBlockZ();


        final Coordinates coordinates = new Coordinates(x, y, z, worldName);


        if (Data.isCommandsToBlock && Data.isCommandsToLog && player.hasPermission(Data.loggerStaff)) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&bLogger &8&l| &cEnabling both Whitelist" +
                            " and Blacklist isn't supported. Disable one of them" +
                            " to continue logging Player Commands."));
        }

        // Log To Files
        if (Data.isLogToFiles) {

            if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true))) {
                   
                    out.write(this.main.getMessages().get().getString("Files.Player-Join-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%IP%", String.valueOf(ip)) + "\n");

                } catch (final IOException e) {

                    Log.warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            } else {

                try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getPlayerJoinLogFile(), true))) {

                    out.write(this.main.getMessages().get().getString("Files.Player-Join").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%IP%", String.valueOf(ip)) + "\n");

                } catch (final IOException e) {

                    Log.warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }
        }

        // Discord
        if (!player.hasPermission(Data.loggerExemptDiscord)) {

            if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                if (!this.main.getMessages().get().getString("Discord.Player-Join-Staff").isEmpty()) {

                    this.main.getDiscord().staffChat(playerName, playerUUID, this.main.getMessages().get().getString("Discord.Player-Join-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%IP%", String.valueOf(ip)), false);

                }
            } else {

                if (!this.main.getMessages().get().getString("Discord.Player-Join").isEmpty()) {

                    this.main.getDiscord().playerJoin(playerName, playerUUID, this.main.getMessages().get().getString("Discord.Player-Join").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%IP%", String.valueOf(ip)), false);
                }
            }
        }

        // External
        if (Data.isExternal) {

            try {

                Main.getInstance().getDatabase().insertPlayerJoin(Data.serverName, playerName, playerUUID.toString(), coordinates, ip, player.hasPermission(loggerStaffLog));

            } catch (final Exception e) { e.printStackTrace(); }
        }

        // SQLite
        if (Data.isSqlite) {

            try {

                Main.getInstance().getSqLite().insertPlayerJoin(Data.serverName, playerName, playerUUID.toString(), coordinates, ip, player.hasPermission(loggerStaffLog));

            } catch (final Exception e) { e.printStackTrace(); }
        }
    }
}
