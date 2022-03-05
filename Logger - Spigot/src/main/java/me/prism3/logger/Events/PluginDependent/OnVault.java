package me.prism3.logger.Events.PluginDependent;

import me.prism3.logger.API.VaultUtil;
import me.prism3.logger.Database.External.ExternalData;
import me.prism3.logger.Database.SQLite.SQLiteData;
import me.prism3.logger.Discord.Discord;
import me.prism3.logger.Main;
import me.prism3.logger.Utils.FileHandler;
import me.prism3.logger.Utils.Messages;
import me.prism3.logger.Utils.Data;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class OnVault implements Listener, Runnable {

    private final Main main = Main.getInstance();
    private final HashMap<UUID, Double> players = new HashMap<>();
    private final Economy econ = VaultUtil.getVault();

    @Override
    public void run() {

        if (this.main.getConfig().getBoolean("Log-Extras.Vault")) {

            for (Player player : Bukkit.getServer().getOnlinePlayers()) {

                if (player.hasPermission(Data.loggerExempt)) return;

                for (Map.Entry<UUID, Double> bal : this.players.entrySet()) {

                    final String playerName = player.getName();

                    if (this.econ.getBalance(player.getPlayer()) != this.players.get(player.getUniqueId())) {

                        double oldBalance = bal.getValue();
                        this.players.put(player.getUniqueId(), this.econ.getBalance(player.getPlayer()));
                        double newBalance = bal.getValue();

                        // Log To Files
                        if (Data.isLogToFiles) {

                            if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                                if (!Objects.requireNonNull(Messages.get().getString("Discord.Extras.Vault-Staff")).isEmpty()) {

                                    Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Extras.Vault-Staff")).replaceAll("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%player%", playerName).replaceAll("%oldbal%", String.valueOf(oldBalance)).replaceAll("%newbal%", String.valueOf(newBalance)), false);

                                }

                                try {

                                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getstaffFile(), true));
                                    out.write(Objects.requireNonNull(Messages.get().getString("Files.Extras.Vault-Staff")).replaceAll("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%player%", playerName).replaceAll("%oldbal%", String.valueOf(oldBalance)).replaceAll("%newbal%", String.valueOf(newBalance)) + "\n");
                                    out.close();

                                } catch (IOException e) {

                                    this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                                    e.printStackTrace();

                                }

                                if (Data.isExternal && this.main.getExternal().isConnected()) {

                                    ExternalData.vault(Data.serverName, playerName, oldBalance, newBalance, true);

                                }

                                if (Data.isSqlite && this.main.getSqLite().isConnected()) {

                                    SQLiteData.insertVault(Data.serverName, player, oldBalance, newBalance, true);

                                }

                                return;

                            }

                            try {

                                BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getVaultFile(), true));
                                out.write(Objects.requireNonNull(Messages.get().getString("Files.Extras.Vault")).replaceAll("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%player%", playerName).replaceAll("%oldbal%", String.valueOf(oldBalance)).replaceAll("%newbal%", String.valueOf(newBalance)) + "\n");
                                out.close();

                            } catch (IOException e) {

                                this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                                e.printStackTrace();

                            }
                        }

                        // Discord Integration
                        if (!player.hasPermission(Data.loggerExemptDiscord)) {

                            if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                                if (!Objects.requireNonNull(Messages.get().getString("Discord.Extras.Vault-Staff")).isEmpty()) {

                                    Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Extras.Vault-Staff")).replaceAll("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%player%", playerName).replaceAll("%oldbal%", String.valueOf(oldBalance)).replaceAll("%newbal%", String.valueOf(newBalance)), false);

                                }

                            } else {

                                if (!Objects.requireNonNull(Messages.get().getString("Discord.Extras.Vault")).isEmpty()) {

                                    Discord.vault(player, Objects.requireNonNull(Messages.get().getString("Discord.Extras.Vault")).replaceAll("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%player%", playerName).replaceAll("%oldbal%", String.valueOf(oldBalance)).replaceAll("%newbal%", String.valueOf(newBalance)), false);
                                }
                            }
                        }

                        // External
                        if (Data.isExternal && this.main.getExternal().isConnected()) {

                            try {

                                ExternalData.vault(Data.serverName, playerName, oldBalance, newBalance, player.hasPermission(Data.loggerStaffLog));

                            } catch (Exception e) { e.printStackTrace(); }
                        }

                        // External
                        if (Data.isSqlite && this.main.getSqLite().isConnected()) {

                            try {

                                SQLiteData.insertVault(Data.serverName, player, oldBalance, newBalance, player.hasPermission(Data.loggerStaffLog));

                            } catch (Exception exception) { exception.printStackTrace(); }
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(final PlayerJoinEvent event){

        this.players.put(event.getPlayer().getUniqueId(), this.econ.getBalance(event.getPlayer()));

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLeave(final PlayerQuitEvent event){

        this.players.remove(event.getPlayer().getUniqueId());

    }
}
