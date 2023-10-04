package me.prism3.logger.events.plugindependent;

import me.prism3.logger.Main;
import me.prism3.logger.api.VaultUtil;
import me.prism3.logger.database.external.ExternalData;
import me.prism3.logger.database.sqlite.global.SQLiteData;
import me.prism3.logger.utils.BedrockChecker;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import me.prism3.logger.utils.Log;
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

                if (player.hasPermission(Data.loggerExempt) || Bukkit.getOnlinePlayers().isEmpty() || BedrockChecker.isBedrock(player.getUniqueId())) return;

                for (Map.Entry<UUID, Double> bal : this.players.entrySet()) {

                    final String playerName = player.getName();

                    if (this.econ.getBalance(player.getPlayer()) != this.players.get(player.getUniqueId())) {

                        double oldBalance = bal.getValue();
                        this.players.put(player.getUniqueId(), this.econ.getBalance(player.getPlayer()));
                        double newBalance = bal.getValue();

                        // Log To Files
                        if (Data.isLogToFiles) {

                            if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                                if (!Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Extras.Vault-Staff")).isEmpty()) {

                                    this.main.getDiscord().staffChat(player, Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Extras.Vault-Staff")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%player%", playerName).replace("%oldbal%", String.valueOf(oldBalance)).replace("%newbal%", String.valueOf(newBalance)), false);

                                }

                                try {

                                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true));
                                    out.write(Objects.requireNonNull(this.main.getMessages().get().getString("Files.Extras.Vault-Staff")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%player%", playerName).replace("%oldbal%", String.valueOf(oldBalance)).replace("%newbal%", String.valueOf(newBalance)) + "\n");
                                    out.close();

                                } catch (IOException e) {

                                    Log.warning("An error occurred while logging into the appropriate file.");
                                    e.printStackTrace();

                                }

                                if (Data.isExternal && this.main.getExternal().isConnected()) {

                                    ExternalData.vault(Data.serverName, player, oldBalance, newBalance, true);

                                }

                                if (Data.isSqlite && this.main.getSqLite().isConnected()) {

                                    SQLiteData.insertVault(Data.serverName, player, oldBalance, newBalance, true);

                                }

                                return;

                            }

                            try {

                                BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getVaultFile(), true));
                                out.write(Objects.requireNonNull(this.main.getMessages().get().getString("Files.Extras.Vault")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%player%", playerName).replace("%oldbal%", String.valueOf(oldBalance)).replace("%newbal%", String.valueOf(newBalance)) + "\n");
                                out.close();

                            } catch (IOException e) {

                                Log.warning("An error occurred while logging into the appropriate file.");
                                e.printStackTrace();

                            }
                        }

                        // Discord Integration
                        if (!player.hasPermission(Data.loggerExemptDiscord)) {

                            if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                                if (!Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Extras.Vault-Staff")).isEmpty()) {

                                    this.main.getDiscord().staffChat(player, Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Extras.Vault-Staff")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%player%", playerName).replace("%oldbal%", String.valueOf(oldBalance)).replace("%newbal%", String.valueOf(newBalance)), false);

                                }

                            } else {

                                if (!Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Extras.Vault")).isEmpty()) {

                                    this.main.getDiscord().vault(player, Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Extras.Vault")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%player%", playerName).replace("%oldbal%", String.valueOf(oldBalance)).replace("%newbal%", String.valueOf(newBalance)), false);
                                }
                            }
                        }

                        // External
                        if (Data.isExternal && this.main.getExternal().isConnected()) {

                            try {

                                ExternalData.vault(Data.serverName, player, oldBalance, newBalance, player.hasPermission(Data.loggerStaffLog));

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
    private void onJoin(final PlayerJoinEvent event) {

        this.players.put(event.getPlayer().getUniqueId(), this.econ.getBalance(event.getPlayer()));

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onLeave(final PlayerQuitEvent event) {

        this.players.remove(event.getPlayer().getUniqueId());

    }
}
