package me.prism3.logger.events;

import com.carpour.loggercore.database.entity.EntityPlayer;
import me.prism3.logger.Main;
import me.prism3.logger.utils.BedrockChecker;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLevelChangeEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.UUID;

import static me.prism3.logger.utils.Data.loggerStaffLog;

public class OnPlayerLevel implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLevelChange(final PlayerLevelChangeEvent event) {

        if (this.main.getConfig().getBoolean("Log-Player.Level")) {

            final Player player = event.getPlayer();

            if (player.hasPermission(Data.loggerExempt) || BedrockChecker.isBedrock(player.getUniqueId())) return;

            final UUID playerUUID = player.getUniqueId();
            final String playerName = player.getName();
            final int logAbove = Data.abovePlayerLevel;
            final double playerLevel = event.getNewLevel();

            final EntityPlayer ePlayer = new EntityPlayer(player.getName(), playerUUID.toString());

            if (playerLevel == logAbove) {

                // Log To Files
                if (Data.isLogToFiles) {

                    if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                        try {

                            final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true));
                            out.write(this.main.getMessages().get().getString("Files.Player-Level-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%player%", playerName).replace("%level%", String.valueOf(logAbove)) + "\n");
                            out.close();

                        } catch (IOException e) {

                            this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                            e.printStackTrace();

                        }
                    } else {

                        try {

                            final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getPlayerLevelFile(), true));
                            out.write(this.main.getMessages().get().getString("Files.Player-Level").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%player%", playerName).replace("%level%", String.valueOf(logAbove)) + "\n");
                            out.close();

                        } catch (IOException e) {

                            this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                            e.printStackTrace();

                        }
                    }
                }

                // Discord
                if (!player.hasPermission(Data.loggerExemptDiscord)) {

                    if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                        if (!this.main.getMessages().get().getString("Discord.Player-Level-Staff").isEmpty()) {

                            this.main.getDiscord().staffChat(player, this.main.getMessages().get().getString("Discord.Player-Level-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%level%", String.valueOf(logAbove)), false);

                        }
                    } else {

                        if (!this.main.getMessages().get().getString("Discord.Player-Level").isEmpty()) {

                            this.main.getDiscord().playerLevel(player, this.main.getMessages().get().getString("Discord.Player-Level").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%level%", String.valueOf(logAbove)), false);
                        }
                    }
                }

                // External
                if (Data.isExternal) {

                    try {

                        Main.getInstance().getDatabase().insertLevelChange(Data.serverName, ePlayer, player.hasPermission(loggerStaffLog));

                    } catch (Exception e) { e.printStackTrace(); }
                }

                // SQLite
                if (Data.isSqlite) {

                    try {

                        Main.getInstance().getSqLite().insertLevelChange(Data.serverName, ePlayer, player.hasPermission(loggerStaffLog));

                    } catch (Exception e) { e.printStackTrace(); }
                }
            }
        }
    }
}
