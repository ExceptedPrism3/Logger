package me.prism3.logger.Events.PluginDependent;

import me.leoko.advancedban.bukkit.event.PunishmentEvent;
import me.prism3.logger.Database.External.ExternalData;
import me.prism3.logger.Database.SQLite.Global.SQLiteData;
import me.prism3.logger.Discord.Discord;
import me.prism3.logger.Main;
import me.prism3.logger.Utils.Data;
import me.prism3.logger.Utils.FileHandler;
import me.prism3.logger.Utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Objects;

public class OnAdvancedBan implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPunishment(final PunishmentEvent event) {

        if (this.main.getConfig().getBoolean("Log-Extras.AdvancedBan")) {

            final String type = event.getPunishment().getType().toString();
            final String executor = event.getPunishment().getOperator();

            final Player player = Bukkit.getPlayer(executor);
            assert player != null;

            final String executed_on = event.getPunishment().getName();
            final String reason = event.getPunishment().getReason();
            final long expirationDate = event.getPunishment().getEnd();

            // Log To Files
            if (Data.isLogToFiles) {

                if (Data.isStaffEnabled) {
                    if (player.hasPermission(Data.loggerStaffLog)) {

                        if (!Objects.requireNonNull(Messages.get().getString("Discord.Extras.AdvancedBan")).isEmpty()) {

                            Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Extras.AdvancedBan")).replaceAll("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%executor%", executor).replaceAll("%executed_on%", executed_on).replaceAll("%reason%", reason).replaceAll("%expiration%", String.valueOf(expirationDate)).replaceAll("%type%", type), false);

                        }

                        try {

                            BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getstaffFile(), true));
                            out.write(Objects.requireNonNull(Messages.get().getString("Files.Extras.AdvancedBan")).replaceAll("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%executor%", executor).replaceAll("%executed_on%", executed_on).replaceAll("%reason%", reason).replaceAll("%expiration%", String.valueOf(expirationDate)).replaceAll("%type%", type) + "\n");
                            out.close();

                        } catch (IOException e) {

                            this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                            e.printStackTrace();

                        }

                        if (Data.isExternal && this.main.getExternal().isConnected()) {

                            ExternalData.advancedBanData(Data.serverName, type, executor, executed_on, reason, expirationDate);

                        }

                        if (Data.isSqlite && this.main.getSqLite().isConnected()) {

                            SQLiteData.insertAdvancedBan(Data.serverName, type, executor, executed_on, reason, expirationDate);

                        }

                        return;

                    }
                }

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getAdvancedBanFile(), true));
                    out.write(Objects.requireNonNull(Messages.get().getString("Files.Extras.AdvancedBan")).replaceAll("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%executor%", executor).replaceAll("%executed_on%", executed_on).replaceAll("%reason%", reason).replaceAll("%expiration%", String.valueOf(expirationDate)).replaceAll("%type%", type) + "\n");
                    out.close();

                } catch (IOException e) {

                    this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord Integration
            if (player == null) { // This is essential when performed by the console

                if (!Objects.requireNonNull(Messages.get().getString("Discord.Extras.AdvancedBan")).isEmpty()) {

                    Discord.advancedBan(Objects.requireNonNull(Messages.get().getString("Discord.Extras.AdvancedBan")).replaceAll("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%executor%", executor).replaceAll("%executed_on%", executed_on).replaceAll("%reason%", reason).replaceAll("%expiration%", String.valueOf(expirationDate)).replaceAll("%type%", type), false);
                }
            } else if (!player.hasPermission(Data.loggerExemptDiscord)) {

                if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                    if (!Objects.requireNonNull(Messages.get().getString("Discord.Extras.AdvancedBan")).isEmpty()) {

                        Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Extras.AdvancedBan")).replaceAll("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%executor%", executor).replaceAll("%executed_on%", executed_on).replaceAll("%reason%", reason).replaceAll("%expiration%", String.valueOf(expirationDate)).replaceAll("%type%", type), false);

                    }
                } else {
                    if (!Objects.requireNonNull(Messages.get().getString("Discord.Extras.AdvancedBan")).isEmpty()) {

                        Discord.advancedBan(Objects.requireNonNull(Messages.get().getString("Discord.Extras.AdvancedBan")).replaceAll("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%executor%", executor).replaceAll("%executed_on%", executed_on).replaceAll("%reason%", reason).replaceAll("%expiration%", String.valueOf(expirationDate)).replaceAll("%type%", type), false);
                    }
                }
            }

            // External
            if (Data.isExternal && this.main.getExternal().isConnected()) {

                try {

                    ExternalData.advancedBanData(Data.serverName, type, executor, executed_on, reason, expirationDate);

                } catch (Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (Data.isSqlite && this.main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertAdvancedBan(Data.serverName, type, executor, executed_on, reason, expirationDate);

                } catch (Exception e) { e.printStackTrace(); }
            }
        }
    }
}
