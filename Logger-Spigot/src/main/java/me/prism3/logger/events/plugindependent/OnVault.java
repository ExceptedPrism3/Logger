package me.prism3.logger.events.plugindependent;

import me.prism3.logger.Main;
import me.prism3.logger.utils.enums.DiscordChannels;
import me.prism3.logger.hooks.VaultUtil;
import me.prism3.logger.utils.BedrockChecker;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static me.prism3.logger.utils.Data.*;

public class OnVault implements Listener, Runnable {

    private final Main main = Main.getInstance();
    private final HashMap<UUID, Double> players = new HashMap<>();
    private final Economy econ = VaultUtil.getVaultEcon();

    @Override
    public void run() {

        for (Player player : Bukkit.getServer().getOnlinePlayers()) {

            if (player.hasPermission(Data.loggerExempt) || Bukkit.getOnlinePlayers().isEmpty()
                    || BedrockChecker.isBedrock(player.getUniqueId()))
                return;

            final String playerName = player.getName();
            final UUID playerUUID = player.getUniqueId();

            final Map<String, String> placeholders = new HashMap<>();
            placeholders.put("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now()));
            placeholders.put("%uuid%", playerUUID.toString());
            placeholders.put("%player%", playerName);

            for (Map.Entry<UUID, Double> bal : this.players.entrySet()) {

                if (this.econ.getBalance(player.getPlayer()) != this.players.get(player.getUniqueId())) {

                    double oldBalance = bal.getValue();
                    this.players.put(player.getUniqueId(), this.econ.getBalance(player.getPlayer()));
                    double newBalance = bal.getValue();

                    placeholders.put("%oldbal%", String.valueOf(oldBalance));
                    placeholders.put("%newbal%", String.valueOf(newBalance));

                    // Log To Files
                    if (Data.isLogToFiles) {
                        if (Data.isStaffEnabled && player.hasPermission(loggerStaffLog)) {
                            FileHandler.handleFileLog("Files.Extras.Vault-Staff", placeholders, FileHandler.getStaffFile());
                        } else {
                            FileHandler.handleFileLog("Files.Extras.Vault", placeholders, FileHandler.getVaultFile());
                        }
                    }

                    // Discord Integration
                    if (!player.hasPermission(loggerExemptDiscord) && this.main.getDiscordFile().get().getBoolean("Discord.Enable")) {

                        if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                            this.main.getDiscord().handleDiscordLog("Discord.Extras.Vault-Staff", placeholders, DiscordChannels.STAFF, playerName, playerUUID);
                        } else {

                            this.main.getDiscord().handleDiscordLog("Discord.Extras.Vault", placeholders, DiscordChannels.VAULT, playerName, playerUUID);
                        }
                    }

                    // External
                    if (Data.isExternal) {

                        try {

                            Main.getInstance().getDatabase().getDatabaseQueue().queueVault(Data.serverName, playerName, playerUUID.toString(), oldBalance, newBalance, player.hasPermission(loggerStaffLog));

                        } catch (final Exception e) { e.printStackTrace(); }
                    }

                    // External
                    if (Data.isSqlite) {

                        try {

                            Main.getInstance().getDatabase().getDatabaseQueue().queueVault(Data.serverName, playerName, playerUUID.toString(), oldBalance, newBalance, player.hasPermission(loggerStaffLog));

                        } catch (final Exception e) { e.printStackTrace(); }
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
