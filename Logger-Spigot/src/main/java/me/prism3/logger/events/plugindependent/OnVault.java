package me.prism3.logger.events.plugindependent;

import me.prism3.logger.Main;
import me.prism3.logger.utils.BedrockChecker;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.enums.LogCategory;
import me.prism3.logger.utils.enums.DiscordChannels;
import me.prism3.logger.hooks.VaultUtil;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static me.prism3.logger.utils.Data.*;


public class OnVault implements Listener, Runnable {

    private final Main main = Main.getInstance();
    private final Map<UUID, Double> players = new HashMap<>();
    private final Economy econ = VaultUtil.getVaultEcon();

    @Override
    public void run() {

        for (Player player : Bukkit.getServer().getOnlinePlayers()) {

            if (this.shouldSkipLogging(player))
                continue;

            final String playerName = player.getName();
            final UUID playerUUID = player.getUniqueId();
            final double currentBalance = this.econ.getBalance(player);

            final Double previousBalance = this.players.get(playerUUID);

            if (previousBalance == null || previousBalance != currentBalance) {
                this.players.put(playerUUID, currentBalance);
                final Map<String, String> placeholders = this.createPlaceholders(playerUUID, playerName, previousBalance, currentBalance);
                this.logToFile(placeholders, player);
                this.logToDiscord(placeholders, player);
                this.handleExternalLogging(playerName, playerUUID, previousBalance, currentBalance);
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

    private boolean shouldSkipLogging(Player player) {
        return player.hasPermission(loggerExempt) ||
                Bukkit.getOnlinePlayers().isEmpty() ||
                BedrockChecker.isBedrock(player.getUniqueId());
    }

    private Map<String, String> createPlaceholders(UUID playerUUID, String playerName, Double oldBalance, double newBalance) {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("%time%", String.valueOf(System.currentTimeMillis()));
        placeholders.put("%uuid%", playerUUID.toString());
        placeholders.put("%player%", playerName);
        placeholders.put("%oldbal%", String.valueOf(oldBalance));
        placeholders.put("%newbal%", String.valueOf(newBalance));
        return placeholders;
    }

    private void logToFile(Map<String, String> placeholders, Player player) {
        if (Data.isLogToFiles) {
            final String fileKey = Data.isStaffEnabled && player.hasPermission(loggerStaffLog) ? "Files.Extras.Vault-Staff" : "Files.Extras.Vault";
            this.main.getFileHandler().handleFileLog(LogCategory.VAULT, fileKey, placeholders);
        }
    }

    private void logToDiscord(Map<String, String> placeholders, Player player) {
        if (!player.hasPermission(loggerExemptDiscord) && this.main.getDiscordFile().get().getBoolean("Discord.Enable")) {
            String channelKey = isStaffEnabled && player.hasPermission(loggerStaffLog) ? "Discord.Extras.Vault-Staff" : "Discord.Extras.Vault";
            this.main.getDiscord().handleDiscordLog(channelKey, placeholders, DiscordChannels.VAULT, player.getName(), player.getUniqueId());
        }
    }

    private void handleExternalLogging(String playerName, UUID playerUUID, Double oldBalance, double newBalance) {
        if (Data.isExternal || Data.isSqlite) {
            try {
                Main.getInstance().getDatabase().getDatabaseQueue().queueVault(Data.serverName, playerName, playerUUID.toString(), oldBalance, newBalance, oldBalance != null);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }
}
