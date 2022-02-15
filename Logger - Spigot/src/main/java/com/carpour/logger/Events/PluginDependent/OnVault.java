package com.carpour.logger.Events.PluginDependent;

import com.carpour.logger.API.VaultUtil;
import com.carpour.logger.Database.External.ExternalData;
import com.carpour.logger.Database.SQLite.SQLiteData;
import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Main;
import com.carpour.logger.Utils.FileHandler;
import com.carpour.logger.Utils.Messages;
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
import java.time.format.DateTimeFormatter;
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

        for (Player player : Bukkit.getServer().getOnlinePlayers()) {

            if (player.hasPermission("logger.exempt")) return;

            for (Map.Entry<UUID, Double> bal : players.entrySet()) {

                String playerName = player.getName();
                String serverName = main.getConfig().getString("Server-Name");
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                if (econ.getBalance(player.getPlayer()) != players.get(player.getUniqueId())) {

                    double oldBalance = bal.getValue();
                    players.put(player.getUniqueId(), econ.getBalance(player.getPlayer()));
                    double newBalance = bal.getValue();

                    if (main.getConfig().getBoolean("Log-Extras.Vault")) {

                        // Log To Files Handling
                        if (main.getConfig().getBoolean("Log-to-Files")) {

                            if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff.log")) {

                                if (!Objects.requireNonNull(Messages.get().getString("Discord.Extras.Vault-Staff")).isEmpty()) {

                                    Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Extras.Vault-Staff")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%player%", playerName).replaceAll("%oldbal%", String.valueOf(oldBalance)).replaceAll("%newbal%", String.valueOf(newBalance)), false);

                                }

                                try {

                                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getstaffFile(), true));
                                    out.write(Objects.requireNonNull(Messages.get().getString("Files.Extras.Vault-Staff")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%player%", playerName).replaceAll("%oldbal%", String.valueOf(oldBalance)).replaceAll("%newbal%", String.valueOf(newBalance)) + "\n");
                                    out.close();

                                } catch (IOException e) {

                                    main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                                    e.printStackTrace();

                                }

                                if (main.getConfig().getBoolean("Database.Enable") && main.external.isConnected()) {

                                    ExternalData.vault(serverName, playerName, oldBalance, newBalance, true);

                                }

                                if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

                                    SQLiteData.insertVault(serverName, player, oldBalance, newBalance, true);

                                }

                                return;

                            }

                            try {

                                BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getVaultFile(), true));
                                out.write(Objects.requireNonNull(Messages.get().getString("Files.Extras.Vault")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%player%", playerName).replaceAll("%oldbal%", String.valueOf(oldBalance)).replaceAll("%newbal%", String.valueOf(newBalance)) + "\n");
                                out.close();

                            } catch (IOException e) {

                                main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                                e.printStackTrace();

                            }
                        }

                        // Discord Integration
                        if (!player.hasPermission("logger.exempt.discord")) {

                            if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff.log")) {

                                if (!Objects.requireNonNull(Messages.get().getString("Discord.Extras.Vault-Staff")).isEmpty()) {

                                    Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Extras.Vault-Staff")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%player%", playerName).replaceAll("%oldbal%", String.valueOf(oldBalance)).replaceAll("%newbal%", String.valueOf(newBalance)), false);

                                }

                            } else {

                                if (!Objects.requireNonNull(Messages.get().getString("Discord.Extras.Vault")).isEmpty()) {

                                    Discord.vault(player, Objects.requireNonNull(Messages.get().getString("Discord.Extras.Vault")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%player%", playerName).replaceAll("%oldbal%", String.valueOf(oldBalance)).replaceAll("%newbal%", String.valueOf(newBalance)), false);
                                }
                            }
                        }

                        // MySQL Handling
                        if (main.getConfig().getBoolean("Database.Enable") && main.external.isConnected()) {

                            try {

                                ExternalData.vault(serverName, playerName, oldBalance, newBalance, player.hasPermission("logger.staff.log"));

                            } catch (Exception e) { e.printStackTrace(); }
                        }

                        // SQLite Handling
                        if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

                            try {

                                SQLiteData.insertVault(serverName, player, oldBalance, newBalance, player.hasPermission("logger.staff.log"));

                            } catch (Exception exception) { exception.printStackTrace(); }
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event){

        players.put(event.getPlayer().getUniqueId(), econ.getBalance(event.getPlayer()));

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLeave(PlayerQuitEvent event){

        players.remove(event.getPlayer().getUniqueId());

    }
}
